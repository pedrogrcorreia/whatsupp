package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;

public class ContactServerManagerPane extends BorderPane {

    private ClientObservable clientObservable;
    // private static int debug = 0;

    private Label welcomeText;
    private Label text;
    private Button start;
    private Button tryAgain;
    private VBox layout;

    boolean firstRun = true;

    public ContactServerManagerPane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        createWindow();
        registerObserver();
        update();
    }

    private void createWindow() {
        welcomeText = new Label("Welcome to Whatsupp... bla bla bla");
        start = new Button("Connect");
        text = new Label("Connecting to Server Manager...");
        tryAgain = new Button("Try again.");
        tryAgain.setVisible(false);
        text.setVisible(false);

        start.setOnAction(e -> {
            setCursor(Cursor.WAIT);
            clientObservable.contactServerManager();
        });

        layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(10);
        layout.getChildren().addAll(welcomeText, text, start);
        setCenter(layout);
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("updateView", e -> update());
    }

    private void update() {
        if (!firstRun) {
            start.setText("Try again");
        }

        if (firstRun) {
            firstRun = false;
        }

        setVisible(clientObservable.getAtualState() == Situation.CONTACT_SERVER_MANAGER);
    }
}
