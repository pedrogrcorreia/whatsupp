package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.*;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Group;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.GroupRequests;
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
    private ServerSocket fileServer;
    User user = new User();
    List<GroupRequests> gr = new ArrayList<>();



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

    public ConnectionClient(Socket clientSocket, Server server, ObjectOutputStream oout, ObjectInputStream oin, ServerSocket fileSocket, ServerSocket fileServer){
        this.clientSocket = clientSocket;
        this.server = server;
        this.oout = oout;
        this.oin = oin;
        this.fileSocket = fileSocket;
        this.fileServer = fileServer;
    }

    public void sendMsgToClient(SharedMessage msg) {
            switch(msg.getMsgType()){
                case NEW_MESSAGE_GROUP, DELETE_MESSAGE_GROUP ->  sendMsgToClientByGroup(msg);
                case NEW_USER_LOGIN, NEW_USER_REGISTERED, NEW_USER_ONLINE, NEW_USER_OFFLINE -> {
                    if (msg.getID() != user.getID()){
                        try {
                            oout.writeObject(msg);
                            oout.flush();
                        } catch (IOException e) {
                            System.out.println("[sendMsgToClient] Error sending message:\r\n\t" + e);
                        }
                    }
                }
                default -> sendMsgToClientByID(msg);
            }
    }

    public void sendMsgToClientByID(SharedMessage msg){
        if(msg.getID() == user.getID()){
            try{
                oout.writeObject(msg);
                oout.flush();
            }catch(IOException e){
                System.out.println("[sendMsgToClient] Error sending message:\r\n\t" + e);
            }
        }

    }

    public void sendMsgToClientByGroup(SharedMessage msg){
        for(GroupRequests groupRequests : gr){
            Group g = groupRequests.getGroup();
            if(msg.getID() == g.getID()){
                try {
                    oout.writeObject(msg);
                    oout.flush();
                } catch(IOException e){
                    System.out.println("[sendMsgToClient] Error sending message:\r\n\t" + e);
                }
            }
        }
    }

    @Override
    public void run() {
        DBManager dbManager = new DBManager(server);
        boolean firstRun = true;
        boolean on = false;
        boolean logged = false;

        while (!clientSocket.isClosed()) {
            try {
                if (firstRun) {
                    System.out
                            .println("[ConnectionClient] Client connected from: " + clientSocket.getInetAddress().getHostAddress() + ":"
                                    + clientSocket.getPort());
                    firstRun = false;
                }

                SharedMessage request = (SharedMessage) oin.readObject();
                System.out.println("[ConnectionClient] Request from client: " + request.getMsgType().name());
                clientSocket.setSoTimeout(OFFLINE_TIMEOUT);
                switch (request.getMsgType()) {
                    /** Login or register */
                    case USER_REQUEST_LOGIN -> {
                        SharedMessage response = dbManager.loginUser(request);
                        oout.writeObject(response);
                        if (response.getMsgType() == Strings.USER_SUCCESS_LOGIN) {
                            user = response.getClientRequest().getUser();
                            System.out.println("User " + user.getUsername() + " is connected on this server.");
                            request = dbManager.getGroups(response);
                            gr = (List<GroupRequests>) request.getClientRequest().getList();
                            on = true;
                            logged = true;
                            dbManager.setStatus(user, 1);
                        }
                    }
                    case USER_REQUEST_REGISTER -> {
                        oout.writeObject(dbManager.registerUser(request));
                    }
                    case USER_UPDATE_INFO -> oout.writeObject(dbManager.updateUser(request));
                    /** Search user */
                    case USER_REQUEST_USER -> {
                        oout.writeObject(dbManager.getUser(request));
                    }
                    case USER_REQUEST_ALL_USERS -> oout.writeObject(dbManager.getAllUsers(request));
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
                        request = dbManager.getGroups(request);
                        gr = (List<GroupRequests>) request.getClientRequest().getList();
                        oout.writeObject(request);
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
                    /** Files */
                    case USER_SEND_FILE ->{
                        Thread f = new Thread(new DownloadFile(new File("./server_" + server.getServerID() + "/downloads/"), fileSocket));
                        f.start();
                        Thread u = new Thread(new UploadFileServer(new File(request.getClientRequest().getSelectedMessage().getFile().getPath()), fileServer));
                        u.start();
                        oout.writeObject(dbManager.newFile(request));
                    }
                    case DOWNLOAD_FILE -> {
                        String path = dbManager.downloadFile(request);
                        File f = new File(path);
                        Thread u = new Thread(new UploadFile(new File("./server_"+ server.getServerID() + "/downloads/"+
                                f.getName()), clientSocket.getLocalAddress().getHostAddress(), request.getClientRequest().getPort()));
                        u.start();
                    }
                    case USER_DELETE_FILE -> oout.writeObject(dbManager.deleteFile(request));
                    default -> {
                    }
                }
                oout.flush();
                if(!on && logged) {
                    dbManager.setStatus(user, 1);
                    oout.flush();
                    System.out.println("[ConnectionClient] Client reconnected.");
                    firstRun = false;
                    on = true;
                }
                // closeSocket when client disconnects
            } catch (SocketException e) {
                System.out.println("[ConnectionClient] User disconnected...");
                try {
                    clientSocket.close();
                    on = false;
                    dbManager.setStatus(user, 0);
                    oout.flush();
                    return;
                } catch (IOException e1) {
                    System.out.println("[ConnectionClient] Error closing client socket:\r\n\t" + e1);
                }
            } catch (SocketTimeoutException e) {
                System.out.println("[ConnectionClient] Setting user offline (30 sec without actions)");
                on = false;
                dbManager.setStatus(user, 0);
            } catch (IOException e) {
                System.out.println("[ConnectionClient] Problem communicating with client: \r\n\t" + e);
            } catch (ClassNotFoundException e) {
                System.out.println("[ConnectionClient] Problem receiving the message from the client:\r\n\t" + e);
            }
        }
    }
}