package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client;

import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Client;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
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
            // Thread t = new Thread(clientUI);
            // t.start();
            Scene scene = new Scene(clientUI, 800, 800);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Whatsupp");
            stage.setOnCloseRequest(windowEvent -> Platform.exit());
            stage.show();
            // System.out.println(clientObservable.getAtualState());
        }

        // VBox root = new VBox();
        // Button b1 = new Button("teste");
        // root.getChildren().addAll(b1);
        // Scene scene = new Scene(root, 400, 400);
        // stage.setScene(scene);
        // stage.show();

    }

    public static void main(String[] args) {
        // Client client = new Client(args[0], Integer.parseInt(args[1]));
        // ConsoleUI console = new ConsoleUI(client);
        // console.prints();

        // ClientServerConnection csc = new ClientRequestLogin("pedro", "correia");
        // System.out.println(csc.getClass().getSimpleName());

        launch(args);
        // Connect to GRDS
        // ByteArrayOutputStream bout;
        // ObjectOutputStream oout;
        // ByteArrayInputStream bin;
        // ObjectInputStream oin;
        // DatagramSocket mySocket;
        // DatagramPacket myPacket;
        // Socket tcpSocket;
        // // Get the server from GRDS
        // // Send message to server
        // InetAddress serverManagerAddress = null;
        // try {
        // serverManagerAddress = InetAddress.getByName("localhost");
        // } catch (UnknownHostException e1) {
        // // TODO Auto-generated catch block
        // e1.printStackTrace();
        // }

        // try {
        // mySocket = new DatagramSocket();

        // bout = new ByteArrayOutputStream();
        // oout = new ObjectOutputStream(bout);

        // oout.writeUnshared(
        // new SharedMessage(Strings.CLIENT_REQUEST_SERVER, "Client wants a connection
        // to a server."));

        // myPacket = new DatagramPacket(bout.toByteArray(), bout.size(),
        // serverManagerAddress, 3000);
        // mySocket.send(myPacket);

        // myPacket = new DatagramPacket(new byte[4096], 4096);
        // mySocket.receive(myPacket);

        // bin = new ByteArrayInputStream(myPacket.getData());
        // oin = new ObjectInputStream(bin);

        // SharedMessage responseFromServerManager = (SharedMessage) oin.readObject();

        // System.out.println(responseFromServerManager.getMsg());

        // tcpSocket = new Socket(serverManagerAddress,
        // Integer.parseInt(responseFromServerManager.getMsg()));

        // oin = new ObjectInputStream(tcpSocket.getInputStream());
        // oout = new ObjectOutputStream(tcpSocket.getOutputStream());

        // oout.writeObject(new SharedMessage(Strings.CLIENT_SENT_MESSAGE, "Mensagem que
        // o user enviou."));
        // oout.flush();

        // SharedMessage response = (SharedMessage) oin.readObject();

        // System.out.println(response);
        // } catch (IOException e) {
        // } catch (ClassNotFoundException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }
}
