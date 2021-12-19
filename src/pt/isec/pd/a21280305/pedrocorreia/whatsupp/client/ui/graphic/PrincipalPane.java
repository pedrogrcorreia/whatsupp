package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states.ContactServerManagerPane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states.InitialStatePane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states.LoginStatePane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states.RegisterStatePane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states.UserStatePane;

class Notification extends Thread {
    ClientObservable clientObservable;
    PrincipalPane principalPane;

    public Notification(ClientObservable clientObservable, PrincipalPane principalPane) {
        this.clientObservable = clientObservable;
        this.principalPane = principalPane;
    }

    @Override
    public void run() {
        while (true) {
            // String aux = clientObservable.getNotification();
            // System.out.println(aux);
            // if (aux.equals("erro")) {
            // System.out.println("Trying another server");
            // clientObservable.contactServerManager();
            // clientObservable.update();
            // // console.prints();

            // }
        }
    }
}

public class PrincipalPane extends BorderPane {
    private ClientObservable clientObservable;
    private HBox notifications;
    private Label txtOfNotification;

    public PrincipalPane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        createView();
        registerObserver();
        update();
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("DEBUG", e -> update());
    }

    private void createView() {

        ContactServerManagerPane contactServerManagerPane = new ContactServerManagerPane(clientObservable);
        InitialStatePane initialStatePane = new InitialStatePane(clientObservable);
        LoginStatePane loginStatePane = new LoginStatePane(clientObservable);
        RegisterStatePane registerStatePane = new RegisterStatePane(clientObservable);
        UserStatePane userStatePane = new UserStatePane(clientObservable);
        StackPane app = new StackPane(contactServerManagerPane, initialStatePane, loginStatePane, registerStatePane,
                userStatePane);

        setCenter(app);
        app.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
        app.setPadding(new Insets(10, 10, 10, 10));

        Label txtN = new Label("Notification: ");
        txtOfNotification = new Label("RECEBI UMA NOTIFICACAO");
        notifications = new HBox(10);
        notifications.getChildren().addAll(txtN, txtOfNotification);
        setBottom(notifications);
    }

    private void update() {
        txtOfNotification.setText("E DUAS");
    }
}
