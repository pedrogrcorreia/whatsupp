package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;

public class InitialStatePane extends BorderPane {
    private ClientObservable clientObservable;

    private Button login;
    private Button register;
    private HBox layout;

    public InitialStatePane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        createWindow();
        registerObserver();
        update();
    }

    private void createWindow() {
        login = new Button("Login");
        register = new Button("Register");

        login.setOnAction(e -> {
            clientObservable.initialStatus("login");
        });

        register.setOnAction(e -> clientObservable.initialStatus("register"));
        layout = new HBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(10);
        layout.getChildren().addAll(login, register);
        setCenter(layout);
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("DEBUG", e -> update());
    }

    private void update() {
        setVisible(clientObservable.getAtualState() == Situation.INITIAL_OPTION);
    }
}
