package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import java.io.*;
import java.net.*;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.DownloadFile;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.UploadFile;
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
    private ServerSocket fileSocket;
    User user = new User();


    public ConnectionClient(Socket clientSocket, Server server, ObjectOutputStream oout, ObjectInputStream oin) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.oout = oout;
        this.oin = oin;
    }

    public ConnectionClient(Socket clientSocket, Server server, ObjectOutputStream oout, ObjectInputStream oin, ServerSocket fileSocket){
        this.clientSocket = clientSocket;
        this.server = server;
        this.oout = oout;
        this.oin = oin;
        this.fileSocket = fileSocket;
    }

    public void sendMsgToClient(SharedMessage msg) {

        try {
            if(msg.getMsgType() == Strings.NEW_MESSAGE_GROUP){
                oout.writeObject(msg);
                oout.flush();
            }else {
                if (msg.getID() == user.getID()) {
                    oout.writeObject(msg);
                    oout.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("[sendMsgToClient] Error sending message:\r\n\t" + e);
        }
    }

    public void downloadToLocal(SharedMessage request){
        try{
            FileOutputStream localFileOutputStream = new FileOutputStream(request.getClientRequest().getSelectedMessage().getFile().getPath()); // nao existe
//                SharedMessage request = (SharedMessage) oin.readObject();
//            localFileOutputStream.write(request.getFileChunk());
            System.out.println("IM HERE");
        } catch (IOException e) {
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
                } else {
                    System.out.println("Client still connected.");
                }

                SharedMessage request = (SharedMessage) oin.readObject();
                System.out.println(request.getMsgType().name());
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
                    /** Groups */
                    case REQUEST_NEW_GROUP -> {
                        oout.writeObject(dbManager.createNewGroup(request));
                    }
                    case USER_REQUEST_GROUPS -> {
                        System.out.println("Pedido recebido");
                        oout.writeObject(dbManager.getGroups(request));
                    }
                    case USER_REQUEST_PENDING_GROUPS -> {
                        oout.writeObject(dbManager.getGroupsPending(request));
                    }
                    case USER_REQUEST_AVAILABLE_GROUPS -> {
                        oout.writeObject(dbManager.getGroupsAvailable(request));
                    }
                    case USER_REQUEST_MANAGE_GROUPS -> {
                        oout.writeObject(dbManager.getGroupsManage(request));
                    }
                    case USER_QUIT_GROUP -> {
                        oout.writeObject(dbManager.quitGroup(request));
                    }
                    case USER_DELETE_GROUP -> {
                        oout.writeObject(dbManager.deleteGroup(request));
                    }
                    case USER_MANAGE_GROUP -> {
                        oout.writeObject(dbManager.manageGroup(request));
                    }
                    case USER_CHANGE_GROUP -> {
                        oout.writeObject(dbManager.changeGroupName(request));
                    }
                    case USER_SEND_GROUP_REQUEST -> {
                        oout.writeObject(dbManager.sendGroupRequest(request));
                    }
                    case ADMIN_ACCEPT_GROUP_REQUEST -> {
                        oout.writeObject(dbManager.acceptGroupRequest(request));
                    }
                    case USER_SEND_FILE ->{
                        oout.writeObject(dbManager.newFile(request));
                        Thread f = new Thread(new DownloadFile(new File("C:\\\\Users\\\\pedro\\\\Desktop\\\\whatsupp\\\\server\\\\files\\\\"), fileSocket));
                        f.start();
                    }
                    case UPLOAD_FILE -> {
//                        downloadToLocal(request);
//                        oout.writeObject(dbManager.uploadFile(request));
//                        System.out.println("TA TUDO");
                    }
                    case DOWNLOAD_FILE -> {
                        String path = dbManager.downloadFile(request);
                        File f = new File(path);
                        System.out.println(path);

                        Thread u = new Thread(new UploadFile(f, clientSocket.getLocalAddress().getHostAddress(), request.getClientRequest().getPort()));
                        u.start();
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