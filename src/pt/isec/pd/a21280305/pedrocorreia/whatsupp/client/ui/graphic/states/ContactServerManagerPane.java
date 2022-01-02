package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;

import java.io.InputStream;

public class ContactServerManagerPane extends BorderPane {

    private ClientObservable clientObservable;

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
//        welcomeText = new Label("Welcome to Whatsupp... bla bla bla");
        InputStream inputStream = getClass().getResourceAsStream("images/welcome.png");
        Image image = new Image(inputStream);
        ImageView img = new ImageView(image);
        start = new Button("Connect");
        text = new Label("Connecting to Server Manager...");
//        tryAgain = new Button("Try again.");
//        tryAgain.setVisible(false);
        text.setVisible(false);
//        img.setViewport(new Rectangle2D(500, 320, 420, 300));
//        Label welcome = new Label();
//        welcome.setGraphic(img);
        start.setOnAction(e -> {
            // setCursor(Cursor.WAIT);
            clientObservable.contactServerManager();
        });

        start.setBackground(new Background(new BackgroundFill(Color.rgb(147, 147, 147), new CornerRadii(10), Insets.EMPTY)));
        start.setFont(new Font(15));
        img.setFitHeight(600);
        img.setPreserveRatio(true);

        layout = new VBox(10,img);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(10);
        layout.getChildren().addAll(text, start);
//        getChildren().add(img);
//        setCenter(img);
        setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
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
            setCursor(Cursor.DEFAULT);
            firstRun = false;
        }

        setVisible(clientObservable.getAtualState() == Situation.CONTACT_SERVER_MANAGER);
    }
}
