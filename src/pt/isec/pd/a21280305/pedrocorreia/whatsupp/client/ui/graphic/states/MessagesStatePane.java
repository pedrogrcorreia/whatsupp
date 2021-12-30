package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.FriendsRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

public class MessagesStatePane extends BorderPane {

    private ClientObservable clientObservable;

    List<Message> lMessages;

    private Label messages;

    private ScrollPane scrollPane;
    private GridPane gridPane;
    private HBox bottom;
    private Button send;
    private TextField msgToSend;
    private Button sendFile;
    User friend;
    boolean stateFlag; // users is false, groups is true

    public MessagesStatePane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        createWindow();
        registerObserver();
        update();
    }

    private void createWindow() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
        scrollPane = new ScrollPane(gridPane);

        bottom = new HBox(10);
        msgToSend = new TextField("Write message...");
        msgToSend.requestFocus();

        send = new Button("Send");
        sendFile = new Button("Send file");
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        bottom.getChildren().addAll(msgToSend, send, sendFile);

        messages = new Label("Messages");
        setCenter(gridPane);
        setBottom(bottom);

    }

    private ContextMenu createContextMenu(Label label, Message m) {
        ContextMenu menu = new ContextMenu();
        MenuItem details = new MenuItem("Details");
        MenuItem delete = new MenuItem("Delete");
        menu.getItems().addAll(details, delete);
        if(m.getSender().getID() == clientObservable.getUser().getID()) {
            delete.setOnAction(e -> {
                Alert msgBox = new Alert(Alert.AlertType.WARNING, null, ButtonType.YES, ButtonType.CANCEL);
                msgBox.setTitle("Delete message");
                msgBox.setHeaderText("Sure you want to delete this message?");
                Optional<ButtonType> result = msgBox.showAndWait();
                if (result.get() == ButtonType.YES) {
                    clientObservable.deleteMessage(m);
                }
            });
        }else{
            delete.setDisable(true);
        }
        details.setOnAction(e -> {
            Alert msgBox = new Alert(Alert.AlertType.INFORMATION);
            if (!stateFlag){
                msgBox.setTitle("Message from " + m.getSender().getName() + " to " + m.getReceiver().getName());
            }
            else{
                msgBox.setTitle("Message from " + m.getSender().getName() + " to group " + m.getGroup().getName());
            }
            msgBox.setHeaderText("Message: " + m.getMsgTxt() + "\nSent at: " + m.getTime());
            msgBox.showAndWait();
        });
        return menu;
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("updateView", e -> update());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_MESSAGES_SUCCESS.name(), e -> updateSuccess());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_MESSAGES_FAIL.name(), e -> updateFail());
        clientObservable.addPropertyChangeListener(Strings.NEW_MESSAGE.name(), e -> updateNewMessage());
//        clientObservable.addPropertyChangeListener(Strings.REMOVED_FRIEND.name(), e -> updateRemovedFriend());
    }

    private void registerListener(){
        send.setOnAction(e -> {
            if(!clientObservable.messagesTo) {
                clientObservable.sendMessage(
                        new Message(clientObservable.getUser(), clientObservable.getFriend(),
                                msgToSend.getText()));
            }
            else{
                clientObservable.sendMessageToGroup(new Message(clientObservable.getUser(), clientObservable.getGroup(), msgToSend.getText()));
            }
        });

        sendFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("./"));

            File selectedFile = fileChooser.showOpenDialog(null);
            if(selectedFile != null) {
                try {
                    clientObservable.sendFile(new Message(clientObservable.getUser(), clientObservable.getFriend(), new pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.File(selectedFile.getCanonicalPath())));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void update() {
        setVisible(clientObservable.getAtualState() == Situation.MESSAGE);
    }

    private void updateSuccess() {
        getChildren().clear();
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.TOP_LEFT);
        lMessages = clientObservable.getMessages();
        messages.setFont(new Font(25.0));
        gridPane.add(messages, 2, 0);
        registerListener();
        if(lMessages.size() == 0){
            Label noMessages = new Label("No messages to show.");
            gridPane.add(noMessages, 4, 0);
            scrollPane.setContent(gridPane);
            setCenter(scrollPane);
            setBottom(bottom);
            return;
        }
        if(lMessages.get(0).getReceiver() != null) {
            stateFlag = false;
            for (int i = 0; i < lMessages.size(); i++) {
                Message msg = lMessages.get(i);
                Label message = new Label(msg.getMsgTxt());
                Button file = new Button();
//                if(msg.getFile().getPath() != null){
//                    message = new Label(msg.getMsgTxt() + msg.getFile().getPath());
//                }
                if (clientObservable.getUser().getID() == msg.getSender().getID()) {
                    Label name = new Label(msg.getSender().getUsername());
                    if(msg.getFile().getPath() != null){
                        file = new Button("Download file.");
                        gridPane.add(file, 1, i+1);
                        gridPane.add(name, 0, i + 1);
                        file.setOnAction(e -> clientObservable.downloadFile(msg));
                        continue;
                    }
                    gridPane.add(name, 0, i + 1);
                    gridPane.add(message, 1, i + 1);

                } else {
                    Label name = new Label(msg.getSender().getUsername());
                    if(msg.getFile().getPath() != null){
                        file = new Button("Download file.");
                        gridPane.add(file, 4, i+1);
                        gridPane.add(name, 3, i + 1);
                        file.setOnAction(e -> clientObservable.downloadFile(msg));
                        continue;
                    }
                    gridPane.add(name, 3, i + 1);
                    gridPane.add(message, 4, i + 1);

                }
                message.setContextMenu(createContextMenu(message, msg));
                message.setBorder(new Border(new BorderStroke(Color.BLUE,
                        BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
                file.setBorder(new Border(new BorderStroke(Color.GREEN,
                        BorderStrokeStyle.SOLID, null, new BorderWidths(2))));


            }
        }
        else{
            stateFlag = true;
            for (int i = 0; i < lMessages.size(); i++) {
                Message msg = lMessages.get(i);
                Label message = new Label(msg.getMsgTxt());
                if (clientObservable.getUser().getID() == msg.getSender().getID()) {
                    Label name = new Label(msg.getSender().getUsername());
                    gridPane.add(name, 0, i + 1);
                    gridPane.add(message, 1, i + 1);
                } else {
                    Label name = new Label(msg.getSender().getUsername());
                    gridPane.add(name, 3, i + 1);
                    gridPane.add(message, 4, i + 1);
                }

                message.setContextMenu(createContextMenu(message, msg));
                message.setBorder(new Border(new BorderStroke(Color.BLUE,
                        BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
            }
        }

        scrollPane.vvalueProperty().bind(gridPane.heightProperty());
        scrollPane.setContent(gridPane);
        setCenter(scrollPane);
        setBottom(bottom);
    }

    private void updateNewMessage() {
        if(!stateFlag) {
            clientObservable.seeMessages(clientObservable.getFriend());
        }
        else {
            clientObservable.seeMessages(clientObservable.getGroup());
        }
    }

    private void updateRemovedFriend() {
        getChildren().clear();
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.TOP_LEFT);
        boolean match = false;
        clientObservable.seeFriends();
        User userFriend = clientObservable.getFriend();
        List<FriendsRequests> friends = clientObservable.getFriendsRequests();
        for (FriendsRequests friend : friends) {
            if (friend.getRequester() == userFriend) {
                match = true;
            } else if (friend.getReceiver() == userFriend) {
                match = true;
            }
        }
        if (match) {
            Label userBlocked = new Label("This friendship is over...");
            gridPane.add(userBlocked, 0, 1);
            send.setDisable(true);
            msgToSend.setDisable(true);
            scrollPane.setContent(gridPane);
            setCenter(scrollPane);
            setBottom(bottom);
        } else {
            updateNewMessage();
        }
    }

    private void updateFail() {
        getChildren().clear();
        Label failed = new Label("No messages availabe");
        gridPane.add(failed, 0, 1);
    }
}
