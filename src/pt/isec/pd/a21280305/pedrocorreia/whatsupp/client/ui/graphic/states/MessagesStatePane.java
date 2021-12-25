package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientRequestMessages;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Message;

public class MessagesStatePane extends GridPane {

    private ClientObservable clientObservable;

    final ContextMenu contextMenu = new ContextMenu();

    List<Message> lMessages;

    private Label messages;

    public MessagesStatePane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        createWindow();
        registerObserver();
        update();
        createMenu();
    }

    private void createWindow() {
        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));

        messages = new Label("Messages");
    }

    public void createMenu() {
        MenuItem details = new MenuItem("Details");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(details, delete);
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("updateView", e -> update());
        clientObservable.addPropertyChangeListener("messagesUpdateSuccess", e -> updateSuccess());
        clientObservable.addPropertyChangeListener("messagesUpdateFail", e -> updateFail());
    }

    private void update() {
        setVisible(clientObservable.getAtualState() == Situation.MESSAGE);
    }

    private void updateSuccess() {
        getChildren().clear();
        setAlignment(Pos.TOP_LEFT);
        lMessages = ((ClientRequestMessages) clientObservable.getNotificationSM().getClientServerConnection())
                .getMessages();
        messages.setFont(new Font(25.0));
        add(messages, 2, 0);
        for (int i = 0; i < lMessages.size(); i++) {
            Message msg = lMessages.get(i);
            Label message = new Label(msg.getMsg());
            if (clientObservable.getUser().getID() == msg.getSender().getID()) {
                Label name = new Label(msg.getSender().getUsername());
                add(name, 0, i + 1);
                add(message, 1, i + 1);
            } else {
                Label name = new Label(msg.getSender().getUsername());
                add(name, 3, i + 1);
                add(message, 4, i + 1);
            }
            message.setContextMenu(contextMenu);
            message.setBorder(new Border(new BorderStroke(Color.BLUE,
                    BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
        }
    }

    private void updateFail() {
        getChildren().clear();
        Label failed = new Label("No messages availabe");
        add(failed, 0, 1);
    }
}
