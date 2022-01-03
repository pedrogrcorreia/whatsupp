package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Client;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.File;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.ClientUI;

public class ClientRun extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parameters parameters = this.getParameters();
        List<String> args = parameters.getUnnamed();

        if (args.size() < 2) {
            System.out.println("Syntax error.\nYou must input the Server Manager IP" +
                    "Address and Port.\n"
                    + "<ServerManagerIpAddress> <ServerManagerPort>");
            System.exit(0);
        } else {
            Client client = new Client(args.get(0), Integer.parseInt(args.get(1)));
            ClientObservable clientObservable = new ClientObservable(client);
            ClientUI clientUI = new ClientUI(clientObservable);
            InputStream url = getClass().getResourceAsStream("resources/images/icon.png");
            Image icon = new Image(url);


            Scene scene = new Scene(clientUI, 800, 800);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Whatsupp Messenger");
            stage.setOnCloseRequest(windowEvent -> {
                Platform.exit();
                System.exit(0);
            });
            stage.getIcons().add(icon);
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
