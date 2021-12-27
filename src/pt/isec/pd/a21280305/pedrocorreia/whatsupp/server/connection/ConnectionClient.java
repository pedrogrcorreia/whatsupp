package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import java.io.*;
import java.net.*;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.data.DBManager;

/** This class represents each client connected. */

public class ConnectionClient extends Thread {
    private static final int OFFLINE_TIMEOUT = 30 * 1000;

    private Socket clientSocket;
    private Server server;
    private ObjectOutputStream oout;
    private ObjectInputStream oin;

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
            System.out.println("[sendMsgToClient] Error sending message:\r\n\t" + e);
        }
    }

    @Override
    public void run() {
        DBManager dbManager = new DBManager(server);
        boolean firstRun = true;
        User user = new User();
        while (!clientSocket.isClosed()) {
            try {
                if (firstRun) {
                    System.out
                            .println("Client connecteced from: " + clientSocket.getInetAddress().getHostAddress() + ":"
                                    + clientSocket.getPort());
                    firstRun = false;
                } else {
                    System.out.println("Client still connected.");
                }

                SharedMessage request = (SharedMessage) oin.readObject();
                clientSocket.setSoTimeout(OFFLINE_TIMEOUT);
                switch (request.getMsgType()) {
                    /** Login or register */
                    case USER_REQUEST_LOGIN -> {
                        SharedMessage response = dbManager.loginUser(request);
                        if (response.getMsgType() == Strings.USER_SUCCESS_LOGIN) {
                            user = response.getClientRequest().getUser();
                            System.out.println("The user on this server is " + user);
                            dbManager.setStatus(user, 1);
                        }
                        oout.writeObject(response);
                    }
                    case USER_REQUEST_REGISTER -> {
                        oout.writeObject(dbManager.registerUser(request));
                    }
                    /** Search user */
                    case USER_REQUEST_USER -> {
                        oout.writeObject(dbManager.getUser(request));
                    }
                    /** Friends requests */
                    case USER_REQUEST_FRIENDS -> {
                        oout.writeObject(dbManager.getFriends(request));
                    }
                    case USER_REQUEST_FRIENDS_REQUESTS -> {
                        oout.writeObject(dbManager.getFriendsRequests(request));
                    }
                    case USER_REQUEST_FRIENDS_REQUESTS_PENDING -> {
                        oout.writeObject(dbManager.getFriendsRequestsPending(request));
                    }
                    case USER_SEND_FRIEND_REQUEST -> {
                        oout.writeObject(dbManager.addFriend(request));
                    }
                    case USER_ACCEPT_FRIEND_REQUEST -> {
                        oout.writeObject(dbManager.acceptRequest(request));
                    }
                    case USER_CANCEL_FRIEND_REQUEST -> {
                        oout.writeObject(dbManager.deleteRequest(request));
                    }
                    case USER_CANCEL_FRIENDSHIP -> {
                        oout.writeObject(dbManager.deleteFriendship(request));
                    }
                    /** Messages requests */
                    case USER_REQUEST_MESSAGES -> {
                        oout.writeObject(dbManager.getMessages(request));
                    }
                    case USER_SENT_MESSAGE -> {
                        oout.writeObject(dbManager.sendMessage(request));
                    }
                    case MESSAGE_DELETE -> {
                        oout.writeObject(dbManager.deleteMessage(request));
                    }
                    default -> {
                        System.out.println("\t\nDEFAULT\n\t");
                    }
                }
                oout.flush();
                // closeSocket when client disconnects
            } catch (SocketException e) {
                System.out.println("User disconnected...");
                try {
                    clientSocket.close();
                    dbManager.setStatus(user, 0);
                    oout.flush();
                } catch (IOException e1) {
                    System.out.println("Error closing client socket:\r\n\t" + e1);
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Setting user offline (30 sec without actions)");
                dbManager.setStatus(user, 0);
            } catch (IOException e) {
                System.out.println("Problem communicating with client: \r\n\t" + e);
            } catch (ClassNotFoundException e) {
                System.out.println("Problem receiving the message from the client:\r\n\t" + e);
            }
        }
    }
}