package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import java.io.*;
import java.net.*;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientServerConnection;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.data.DBManager;

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

                ClientServerConnection request = (ClientServerConnection) oin.readObject();

                System.out.println(request.getUsername() + " " + request.getPassword());

                switch (request.getClass().getSimpleName()) {
                    case "ClientRequestRegister":
                        oout.writeObject(dbManager.registerUser(request.getUsername(),
                                request.getPassword(),
                                request.getConfPassword(), request.getFName(), request.getLName()));
                        break;
                    case "ClientRequestLogin":
                        oout.writeObject(dbManager.loginUser(request.getUsername(), request.getPassword()));
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