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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;

public class MessagesStatePane extends BorderPane {

    private ClientObservable clientObservable;

    List<Message> lMessages;

    private Label messages;

    private ScrollPane scrollPane;
    private GridPane gridPane;
    private HBox top;
    private HBox bottom;
    private Button send;
    private TextField msgToSend;
    private Button sendFile;

    public MessagesStatePane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        createWindow();
        registerObserver();
        update();
    }

    private void createWindow() {
        Background btBkg = new Background(new BackgroundFill(Color.rgb(180, 180, 180), new CornerRadii(10), Insets.EMPTY));
        Font lblFont = new Font(15);
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
//        gridPane.setPadding(new Insets(25, 25, 25, 25));
        scrollPane = new ScrollPane(gridPane);

        bottom = new HBox(10);
        msgToSend = new TextField();
        msgToSend.requestFocus();
        msgToSend.setFont(lblFont);
        msgToSend.setPromptText("Message to send...");


        send = new Button("Send");
        sendFile = new Button("Send file");
        send.setBackground(btBkg);
        send.setFont(lblFont);
        sendFile.setBackground(btBkg);
        sendFile.setFont(lblFont);
        HBox.setHgrow(msgToSend, Priority.ALWAYS);
        bottom.setPadding(new Insets(10, 10, 10, 10));
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        bottom.getChildren().addAll(msgToSend, send, sendFile);

        messages = new Label("Messages with");

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50);
        gridPane.getColumnConstraints().addAll(column1, column2); // each get 50% of width
        top = new HBox(10);
        top.getChildren().addAll(messages);

        setCenter(gridPane);
        setBottom(bottom);
        setTop(top);

    }

    private ContextMenu createContextMenu(Label label, Message m) {
        ContextMenu menu = new ContextMenu();
        MenuItem details = new MenuItem("Details");
        MenuItem download = new MenuItem("Download file");
        MenuItem delete = new MenuItem("Delete");
        menu.getItems().addAll(details, delete);
        if(m.getSender().getID() == clientObservable.getUser().getID()) {
            delete.setOnAction(e -> {
                Alert msgBox = new Alert(Alert.AlertType.WARNING, null, ButtonType.YES, ButtonType.CANCEL);
                msgBox.setTitle("Delete message");
                msgBox.setHeaderText("Sure you want to delete this message?");
                Optional<ButtonType> result = msgBox.showAndWait();
                if(result.isPresent()) {
                    if (result.get() == ButtonType.YES) {
                        clientObservable.deleteMessage(m);
                    }
                }
            });
        }else{
            delete.setDisable(true);
        }
        if(m.getFile().getPath() != null){
            delete.setOnAction(e -> {
                Alert msgBox = new Alert(Alert.AlertType.WARNING, null, ButtonType.YES, ButtonType.CANCEL);
                msgBox.setTitle("Delete file");
                msgBox.setHeaderText("Sure you want to delete this file?");
                Optional<ButtonType> result = msgBox.showAndWait();
                if(result.isPresent()){
                    if(result.get() == ButtonType.YES){
                        clientObservable.deleteFile(m);
                    }
                }
            });
            download.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File("./"));
                File d = new File(label.getText());
                String name = d.getName();
                int lastIndexOf = name.lastIndexOf(".");
                fileChooser.setInitialFileName(name.substring(lastIndexOf));
                File selectedFile = fileChooser.showSaveDialog(null);
                if(selectedFile != null){
                    clientObservable.downloadFile(m, selectedFile);
                }

            });
            menu.getItems().add(download);
        }
        details.setOnAction(e -> {
            Alert msgBox = new Alert(Alert.AlertType.INFORMATION);
            if (clientObservable.getState() == State.USER){
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
//        /** DEBUG */
//        clientObservable.addPropertyChangeListener("updateView", e -> updateSuccess());
//        /** END DEBUG */
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_MESSAGES_SUCCESS.name(), e -> updateSuccess());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_MESSAGES_FAIL.name(), e -> updateFail());
        clientObservable.addPropertyChangeListener(Strings.NEW_MESSAGE_USER.name(), e -> updateNewMessageUser());
        clientObservable.addPropertyChangeListener(Strings.NEW_FILE_SENT_USER.name(), e -> updateNewMessageUser());
        clientObservable.addPropertyChangeListener(Strings.FILE_REMOVED_USER.name(), e -> updateNewMessageUser());
        clientObservable.addPropertyChangeListener(Strings.NEW_FILE_SENT_GROUP.name(), e -> updateNewMessageGroup());
        clientObservable.addPropertyChangeListener(Strings.FILE_REMOVED_GROUP.toString(), e -> updateNewMessageGroup());
        clientObservable.addPropertyChangeListener(Strings.NEW_MESSAGE_GROUP.name(), e -> updateNewMessageGroup());
        clientObservable.addPropertyChangeListener(Strings.REMOVED_FRIEND.name(), e -> updateRemovedFriend());
        clientObservable.addPropertyChangeListener(Strings.DELETE_MESSAGE_USER.name(), e -> updateNewMessageUser());
        clientObservable.addPropertyChangeListener(Strings.DELETE_MESSAGE_GROUP.name(), e -> updateNewMessageGroup());
        clientObservable.addPropertyChangeListener(Strings.MESSAGE_DELETE_SUCCESS.name(), e -> updateNewMessageUser());
    }

    private void registerListener(){
        send.setOnAction(e -> {
            if(clientObservable.getState() == State.USER) {
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
                    if(clientObservable.getState() == State.USER) {
                        clientObservable.sendFile(new Message(clientObservable.getUser(), clientObservable.getFriend(), new pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.File(selectedFile.getCanonicalPath())));
                    }else{
                        clientObservable.sendFileToGroup(new Message(clientObservable.getUser(), clientObservable.getGroup(), new pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.File(selectedFile.getCanonicalPath())));                    }
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
        gridPane.setAlignment(Pos.CENTER);

        lMessages = clientObservable.getMessages();
        String name = (clientObservable.getState() == State.USER) ? clientObservable.getFriend().getUsername() :
                clientObservable.getGroup().getName();
        messages.setText(name);
        messages.setFont(new Font(25.0));
        top.setAlignment(Pos.CENTER);

        registerListener();
        if(lMessages.size() == 0){
            setTop(top);
            setCenter(scrollPane);
            setBottom(bottom);
            return;
        }

        for(int i = 0; i < lMessages.size(); i++) {
            Message msg = lMessages.get(i);
            Label msgLbl = new Label();
            File file;
            if(msg.getMsgTxt() != null) {
                msgLbl = (clientObservable.getState() == State.USER) ? new Label(msg.getMsgTxt()) :
                        new Label(msg.getSender().getUsername() + "\n" + msg.getMsgTxt());
                msgLbl.setTextFill(Color.WHITE);
            }
            else if (msg.getFile().getPath() != null){
                file = new File(msg.getFile().getPath());
                msgLbl = new Label(file.getName());
                msgLbl.setTextFill(Color.BLUE);
            }
            if (msg.getSender().getID() == clientObservable.getUser().getID()){
                msgLbl.setBackground(new Background(new BackgroundFill(Color.rgb(38, 118, 43), new CornerRadii(15), Insets.EMPTY)));
                msgLbl.setPadding(new Insets(15, 15, 15, 15));
                gridPane.add(msgLbl, 1, i+1);
            }
            else{
                msgLbl.setBackground(new Background(new BackgroundFill(Color.rgb(57, 59, 57),
                        new CornerRadii(15),
                        Insets.EMPTY)));
                msgLbl.setPadding(new Insets(15, 15, 15, 15));
                gridPane.add(msgLbl, 0, i+1);
            }
            msgLbl.setFont(new Font(15));
            msgLbl.setWrapText(true);

            msgLbl.setContextMenu(createContextMenu(msgLbl, msg));
        }
        scrollPane.vvalueProperty().bind(gridPane.heightProperty());
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10, 10, 10, 10));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setCenter(scrollPane);
        setBottom(bottom);
        setTop(top);
    }

    private void updateNewMessageUser() {
        clientObservable.seeMessages(clientObservable.getFriend());
    }

    private void updateNewMessageGroup(){
        clientObservable.seeMessages(clientObservable.getGroup());
    }

    private void updateRemovedFriend() {
        getChildren().clear();
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.TOP_LEFT);
            Label userBlocked = new Label("This friendship is over...");
            gridPane.add(userBlocked, 0, 1);
            send.setDisable(true);
            msgToSend.setDisable(true);
            scrollPane.setContent(gridPane);
            setCenter(scrollPane);
            setBottom(bottom);
    }

    private void updateFail() {
        getChildren().clear();
        Label failed = new Label("No messages availabe");
        gridPane.add(failed, 0, 1);
    }
}
