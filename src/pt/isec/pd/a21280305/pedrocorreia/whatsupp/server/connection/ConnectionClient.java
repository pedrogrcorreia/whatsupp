package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import java.io.*;
import java.net.*;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.Messages;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.User;
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

    public ConnectionClient(Socket clientSocket, Server server, ObjectOutputStream oout, ObjectInputStream oin) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.oout = oout;
        this.oin = oin;
    }

    public void sendMsgToClient(SharedMessage msg) {

        try {
            oout.writeObject(msg);
            oout.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

                    // oout = new ObjectOutputStream(clientSocket.getOutputStream());
                    // oin = new ObjectInputStream(clientSocket.getInputStream());
                } else {
                    System.out.println("Client still connected.");
                }

                SharedMessage request = (SharedMessage) oin.readObject();

                switch (request.getClientServerConnection().getClass().getSimpleName()) {
                    case "Register":
                        oout.writeObject(dbManager.registerUser(request));
                        break;
                    case "Login":
                        oout.writeObject(dbManager.loginUser(request));
                        break;
                    case "SearchUser":
                        System.out.println("User info");
                        oout.writeObject(dbManager.getUser(request));
                        break;
                    case "FriendsList":
                        if (request.getMsgType() == Strings.USER_REQUEST_FRIENDS) {
                            System.out.println("User friends");
                            oout.writeObject(dbManager.getFriends(request));
                        }
                        if (request.getMsgType() == Strings.USER_REQUEST_FRIENDS_REQUESTS) {
                            System.out.println("User friends requests");
                            oout.writeObject(dbManager.getFriendsRequests(request));
                        }
                        break;
                    case "Friend":
                        System.out.println("User requesting a friend");
                        oout.writeObject(dbManager.addFriend(request));
                        break;
                    case "Messages":
                        if (request.getMsgType() == Strings.USER_REQUEST_MESSAGES) {
                            oout.writeObject(dbManager.getMessages(request));
                        }
                        if (request.getMsgType() == Strings.MESSAGE_DELETE) {
                            oout.writeObject(dbManager.deleteMessage(request));
                        }
                        if (request.getMsgType() == Strings.USER_SENT_MESSAGE) {
                            oout.writeObject(dbManager.sendMessage(request));
                        }
                        break;
                }
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