package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Client;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.ClientUI;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Client client = new Client();
        ClientObservable clientObservable = new ClientObservable(client);
        ClientUI clientUI = new ClientUI(clientObservable);

        Scene scene = new Scene(clientUI, 600, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Whatsupp");
        stage.setOnCloseRequest(windowEvent -> Platform.exit());
        stage.show();

//        VBox root = new VBox();
//        Button b1 = new Button("teste");
//        root.getChildren().addAll(b1);
//        Scene scene = new Scene(root, 400, 400);
//        stage.setScene(scene);
//        stage.show();

        //System.out.println(clientObservable.getAtualState());
    }

    public static void main(String[] args){
        launch(args);
    }
}
