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
        // System.out.println(debug++);
        welcomeText = new Label("Welcome to Whatsupp... bla bla bla");
        start = new Button("Connect");
        text = new Label("Connecting to Server Manager...");
        tryAgain = new Button("Try again.");
        tryAgain.setVisible(false);
        text.setVisible(false);

        // new Thread(new Runnable() {
        // @Override
        // public void run() {
        // Platform.runLater(() -> {
        // start.setOnAction(e -> clientObservable.contactServerManager());
        // });
        // }
        // }).start();

        // Task<Void> task = new Task<Void>() {
        // @Override
        // public Void call() {
        // // clientObservable.createConnection();
        // clientObservable.contactServerManager();
        // return null;
        // }
        // };
        // task.setOnRunning(taskRunning -> {
        // System.out.println(debug);
        // text.setText("Connecting to Server Manager...");
        // text.setVisible(true);
        // });
        // task.setOnFailed(taskFailed -> {
        // debug = true;
        // System.out.println(debug);
        // text.setText("Couldn't establish a connection...");
        // start.setText("Try Again");
        // Alert msgBox = new Alert(Alert.AlertType.ERROR);
        // msgBox.setHeaderText("ERROR");
        // msgBox.showAndWait();
        // update();
        // // clientObservable.initialStatus("update");
        // });
        // task.setOnSucceeded(taskSuccess -> {
        // clientObservable.update();
        // });

        // start.setOnAction(e -> {
        // setCursor(Cursor.WAIT);
        // start.setDisable(true);
        // Thread th;
        // th = new Thread(task);
        // th.setDaemon(true);
        // th.start();
        // });

        start.setOnAction(e -> {
            setCursor(Cursor.WAIT);
            clientObservable.contactServerManager();
        });

        layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(10);
        layout.getChildren().addAll(welcomeText, text, start);
        // getChildren().addAll(layout);
        setCenter(layout);
        // if (debug) {
        // clientObservable.update();
        // }
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("DEBUG", e -> update());
    }

    private void update() {
        if (!firstRun) {
            start.setText("Try again");
        }

        if (firstRun) {
            firstRun = false;
        }

        setVisible(clientObservable.getAtualState() == Situation.CONTACT_SERVER_MANAGER);
        // Task task = new Task<Void>() {
        // @Override
        // public Void call() {
        // clientObservable.contactServerManager();
        // return null;
        // }
        // };
        // // task.setOnSucceeded(taskFinishEvent -> text.setText("Done!"));
        // new Thread(task).start();
        // clientObservable.contactServerManager();
        // setVisible(false);
    }
}
