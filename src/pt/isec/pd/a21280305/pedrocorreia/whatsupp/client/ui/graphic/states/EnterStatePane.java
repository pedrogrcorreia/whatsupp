package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;

public class EnterStatePane extends BorderPane {

    private ClientObservable clientObservable;

    private Label text;
    private Button start;
    private VBox layout;

    public EnterStatePane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        createWindow();
        registerObserver();
        update();
    }

    private void createWindow() {
        text = new Label("Welcome to whatsupp (later replace with image or something and text too");

        start = new Button("Enter");

        start.setOnAction(e -> clientObservable.createConnection());

        layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(10);
        layout.getChildren().addAll(start, text);
        setCenter(layout);
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("DEBUG", e -> update());
    }

    private void update() {
        setVisible(clientObservable.getAtualState() == Situation.ENTER_STATE);
    }
}
