package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import java.io.*;
import java.net.*;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.data.DBManager;

/** This class represents each client connected. */

public class ConnectionClient extends Thread {
    private Socket clientSocket;
    private Server server;
    private ObjectOutputStream oout;
    private ObjectInputStream oin;

    public ConnectionClient(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        DBManager dbManager = new DBManager(server);
        boolean firstRun = true;

        while (!clientSocket.isClosed()) {
            try {
                if (firstRun) {
                    System.out
                            .println("Client connecteced from: " + clientSocket.getInetAddress().getHostAddress() + ":"
                                    + clientSocket.getPort());
                    firstRun = false;

                    oout = new ObjectOutputStream(clientSocket.getOutputStream());
                    oin = new ObjectInputStream(clientSocket.getInputStream());
                } else {
                    System.out.println("Client still connected.");
                }

                // ClientServerConnection request = (ClientServerConnection) oin.readObject();
                SharedMessage request = (SharedMessage) oin.readObject();

                // System.out.println(request.getUsername() + " " + request.getPassword());

                // switch (request.getClass().getSimpleName()) {
                switch (request.getClientServerConnection().getClass().getSimpleName()) {
                    case "ClientRequestRegister":
                        // oout.writeObject(dbManager.registerUser(request.getClientServerConnection().getUsername(),
                        // request.getClientServerConnection().getPassword(),
                        // request.getClientServerConnection().getConfPassword(),
                        // request.getClientServerConnection().getFName(),
                        // request.getClientServerConnection().getLName()));
                        oout.writeObject(dbManager.registerUser(request));
                        break;
                    case "ClientRequestLogin":
                        oout.writeObject(dbManager.loginUser(request));
                        break;
                    // case "ClientRequestInfo":
                    // System.out.println("User requesting info");
                    // oout.writeObject(dbManager.getFriends(request));
                    // break;
                    case "ClientRequestUser":
                        System.out.println("User info");
                        oout.writeObject(dbManager.getUser(request));
                        break;
                    case "ClientRequestFriends":
                        System.out.println("User friends");
                        oout.writeObject(dbManager.getFriends(request));
                        break;
                    case "UserRequestFriend":
                        System.out.println("User requesting a friend");
                        oout.writeObject(dbManager.addFriend(request));
                        break;
                    case "ClientRequestMessages":
                        System.out.println("User requesting messages");
                        oout.writeObject(dbManager.getMessages(request));
                        break;
                }
                System.out.println("Wrote the response...");
                oout.flush();
                // closeSocket when client disconnects
            } catch (SocketException e) {
                System.out.println("Client terminated abruptly: \r\n\t" + e);
                try {
                    clientSocket.close();
                    oout.flush();
                    // return;
                } catch (IOException e1) {
                    System.out.println("Error closing client socket:\r\n\t" + e1);
                }
            } catch (IOException e) {
                System.out.println("Problem communicating with client: \r\n\t" + e);
            } catch (ClassNotFoundException e) {
                System.out.println("Problem receiving the message from the client:\r\n\t" + e);
            }
        }
    }
}