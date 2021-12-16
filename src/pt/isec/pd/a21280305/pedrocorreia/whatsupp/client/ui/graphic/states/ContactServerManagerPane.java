package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.concurrent.Task;
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
    private static int debug = 0;

    private Label text;
    private Button tryAgain;
    private VBox layout;

    public ContactServerManagerPane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        createWindow();
        registerObserver();
        update();
    }

    private void createWindow() {
        System.out.println(debug++);
        text = new Label("Contacting Server Manager to obtain a server...");
        tryAgain = new Button("Try again.");
        tryAgain.setVisible(false);

        tryAgain.setOnAction(e -> {
            Task task = new Task<Void>() {
                @Override
                public Void call() {
                    clientObservable.contactServerManager();
                    return null;
                }
            };
            task.setOnRunning(taskRunning -> text.setText("..."));
            task.setOnFailed(taskFailed -> tryAgain.setVisible(true));
            task.setOnSucceeded(taskFinishEvent -> text.setText("Done!"));
            new Thread(task).start();
        });
        layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(10);
        layout.getChildren().addAll(text, tryAgain);
        setCenter(layout);

        Task task = new Task<Void>() {
            @Override
            public Void call() {
                System.out.println("HERE");
                clientObservable.contactServerManager();
                return null;
            }
        };
        task.setOnRunning(taskRunning -> text.setText("..."));
        task.setOnFailed(taskFailed -> tryAgain.setVisible(true));
        task.setOnSucceeded(taskFinishEvent -> text.setText("Done!"));
        Thread th;
        th = new Thread(task);
        th.setDaemon(false);
        th.start();

        // clientObservable.contactServerManager();
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("DEBUG", e -> update());
    }

    private void update() {
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
    }
}
