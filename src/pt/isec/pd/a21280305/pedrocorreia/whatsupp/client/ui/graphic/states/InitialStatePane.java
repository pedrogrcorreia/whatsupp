package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;

public class InitialStatePane extends BorderPane {
    private ClientObservable clientObservable;

    private Button login;
    private Button register;
    private VBox layout;

    public InitialStatePane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        createWindow();
        registerObserver();
        update();
    }

    private void createWindow() {
        Background btBkg = new Background(new BackgroundFill(Color.rgb(180, 180, 180), new CornerRadii(10), Insets.EMPTY));
        Font lblFont = new Font(20);

        login = new Button("Login");
        register = new Button("Register");

        login.setOnAction(e -> {
            clientObservable.initialStatus("login");
        });

        login.setFont(lblFont);
        login.setBackground(btBkg);
        login.setPrefWidth(100);

        register.setFont(lblFont);
        register.setOnAction(e -> clientObservable.initialStatus("register"));
        register.setBackground(btBkg);
        register.setPrefWidth(100);

        layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(10);
        layout.getChildren().addAll(login, register);

        setCenter(layout);
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("updateView", e -> update());
    }

    private void update() {
        setVisible(clientObservable.getAtualState() == Situation.INITIAL_OPTION);
    }
}
