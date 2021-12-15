package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;

public class ContactServerManagerPane extends BorderPane {

    private ClientObservable clientObservable;

    private Label text;
    private Button start;
    private VBox layout;

    public ContactServerManagerPane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        createWindow();
        registerObserver();
        update();
    }

    private void createWindow() {

        start = new Button("Enter");

        text = new Label("Contacting Server Manager to obtain a server...");
        text.setVisible(false);

        start.setOnAction(e -> {
            text.setVisible(true);
            start.setVisible(false);
            clientObservable.contactServerManager();
        });

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
        setVisible(clientObservable.getAtualState() == Situation.CONTACT_SERVER_MANAGER);
    }
}
