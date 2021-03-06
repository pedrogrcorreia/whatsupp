package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.DownloadFile;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.UploadFile;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Client;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.requests.*;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.FriendsRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Group;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.GroupRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

public class Data {
    private static final int MAX_SIZE = 8192;

    protected static User user = new User();
    protected static User selectedFriend;

    protected String serverManagerAddress;
    protected int serverManagerPort;
    protected String serverAddress;
    protected int serverPort;
    protected int filesPort;
    protected InetAddress serverManager;

    public List<SharedMessage> notLog;

    DatagramSocket socket;
    DatagramPacket packet;
    ByteArrayOutputStream bout;
    ObjectOutputStream oout;
    ByteArrayInputStream bin;
    ObjectInputStream oin;

    // To communicate with Server
    protected Socket socketToServer;

    protected static List<Message> messages;
    protected static List<FriendsRequests> friends;
    protected static List<FriendsRequests> friendsPending;
    protected static List<FriendsRequests> friendsSent;

    protected static List<GroupRequests> myGroups;
    protected static List<GroupRequests> pendingGroups;
    protected static List<Group> manageGroups;
    protected static List<Group> availableGroups;
    protected static List<GroupRequests> groupMembers;

    protected static Group selectedGroup;

    Thread t;
    Notifications not;

    LoginRegister loginRegister;

    public Data(String serverManagerAddress, int serverManagerPort) {
        this.serverManagerAddress = serverManagerAddress;
        this.serverManagerPort = serverManagerPort;
        notLog = new ArrayList<>();
        friends = new ArrayList<>();
        friendsPending = new ArrayList<>();
        friendsSent = new ArrayList<>();
        messages = new ArrayList<>();
        myGroups = new ArrayList<>();
        pendingGroups = new ArrayList<>();
        manageGroups = new ArrayList<>();
        availableGroups = new ArrayList<>();
        groupMembers = new ArrayList<>();
    }

    public Data() {
        notLog = new ArrayList<>();
        friends = new ArrayList<>();
        friendsPending = new ArrayList<>();
        friendsSent = new ArrayList<>();
        messages = new ArrayList<>();
        myGroups = new ArrayList<>();
        pendingGroups = new ArrayList<>();
        manageGroups = new ArrayList<>();
        availableGroups = new ArrayList<>();
        groupMembers = new ArrayList<>();
    }

    public boolean contactServerManager() {
        try {
            serverManager = InetAddress.getByName(serverManagerAddress);

            socket = new DatagramSocket();
            socket.setSoTimeout(3000);
            bout = new ByteArrayOutputStream();
            oout = new ObjectOutputStream(bout);

            oout.writeUnshared(
                    new SharedMessage(Strings.CLIENT_REQUEST_SERVER, "Client wants a connection"
                            +
                            "to a server."));

            packet = new DatagramPacket(bout.toByteArray(), bout.size(), serverManager,
                    serverManagerPort);
        } catch (IOException e) {
            System.out.println("[Data] Error creating socket and packet:\r\n\t" + e);
            return false;
        }

        try {
            serverManager = InetAddress.getByName(serverManagerAddress);
        } catch (UnknownHostException e) {
            System.out.println("[Data] Error recognizing the host:\r\n\t" + e);
        }
        try {
            System.out.println(packet.getAddress().getHostAddress());
            socket.send(packet);
        } catch (SocketTimeoutException e) {
            System.out.println("[Data] Timeout on sending message to Server Manager:\r\n\t" +
                    e);
            return false;
        } catch (IOException e) {
            System.out.println("[Data] Error at sending the message to Server Manager:\r\n\t" +
                    e);
            return false;
        }

        try {
            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            socket.receive(packet);
        } catch (SocketTimeoutException e) {
            System.out.println("[Data] Timeout on receiving message from Server Manager:\r\n\t"
                    + e);
            return false;
        } catch (IOException e) {
            packet = new DatagramPacket(bout.toByteArray(), bout.size(), serverManager,
                    serverManagerPort);
            System.out.println("[Data] Error at receiving the message to Server Manager:\r\n\t"
                    + e);
            return false;
        }
        try {
            bin = new ByteArrayInputStream(packet.getData());
            oin = new ObjectInputStream(bin);

            SharedMessage responseFromServerManager = (SharedMessage) oin.readObject();

            Scanner sc = new Scanner(new String(responseFromServerManager.getMsg()));
            sc.useDelimiter(":");
            String serverAddressReceived = sc.next();
            serverAddress = serverAddressReceived.replace("/", "");
            serverPort = Integer.parseInt(sc.next());
            filesPort = Integer.parseInt(sc.next());
            socketToServer = new Socket(serverAddress, serverPort);
            oin = new ObjectInputStream(socketToServer.getInputStream());
            oout = new ObjectOutputStream(socketToServer.getOutputStream());
            not = new Notifications(serverManagerAddress, serverManagerPort, oin, oout, notLog);

            synchronized (notLog) {
                notLog.add(new SharedMessage(Strings.CLIENT_REQUEST_SERVER,
                        new String("Connected successfully to a server.")));
                notLog.notifyAll();
            }

            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("[Data] Message error:\r\n\t" + e);
            return false;
        } catch (UnknownHostException e) {
            System.out.println("[Data] Couldn't connect to that host:\r\n\t" + e);
            return false;
        } catch (BindException e) {
            System.out.println("[Data] No servers available:\r\n\t" + e);
            contactServerManager();
            return false;
        } catch (IOException e) {
            System.out.println("[Data] Error on socket:\r\n\t" + e);
            contactServerManager();
            return false;
        }
    }

    /**
     * Login and Register
     */

    public boolean login(String username, String password) {
        loginRegister = new LoginRegister(new User(username, password));
        return loginRegister.login(oin, oout, notLog);
    }

    public boolean register(String username, String password, String confPassword, String fName, String lName) {
        loginRegister = new LoginRegister(new User(username, password, confPassword, fName, lName));
        return loginRegister.register(oin, oout, notLog);
    }

    public void updateUser(User u){
        ClientRequests cr = new ClientRequests(user, u);
        cr.sendRequest(oout, Strings.USER_UPDATE_INFO);
    }

    /**
     * Start notification thread
     */

    public boolean retrieveInfo() {
        t = new Thread(not);
        t.setDaemon(true);
        t.start();

        user = loginRegister.getUser();
        return true;
    }

    /**
     * Search user
     */

    public boolean searchUser(String username) {
        selectedFriend = new User(username);
        ClientRequests cr = new ClientRequests(user, selectedFriend);
        return cr.sendRequest(oout, Strings.USER_REQUEST_USER);
    }

    public void getAllUsers(){
        ClientRequests cr = new ClientRequests(user);
        cr.sendRequest(oout, Strings.USER_REQUEST_ALL_USERS);
    }

    /**
     * Friends
     */

    public boolean seeFriends() {
        ClientRequests cr = new ClientRequests(user);
        return cr.sendRequest(oout, Strings.USER_REQUEST_FRIENDS);
    }

    public boolean seeFriendsRequests() {
        ClientRequests cr = new ClientRequests(user);
        return cr.sendRequest(oout, Strings.USER_REQUEST_FRIENDS_REQUESTS);
    }

    public boolean seeFriendsRequestsPending() {
        ClientRequests cr = new ClientRequests(user);
        return cr.sendRequest(oout, Strings.USER_REQUEST_FRIENDS_REQUESTS_PENDING);
    }

    public boolean addFriend(User friend) {
        ClientRequests cr = new ClientRequests(user, friend);
        return cr.sendRequest(oout, Strings.USER_SEND_FRIEND_REQUEST);
    }

    public boolean acceptRequest(User friend) {
        selectedFriend = friend;
        ClientRequests cr = new ClientRequests(user, friend);
        return cr.sendRequest(oout, Strings.USER_ACCEPT_FRIEND_REQUEST);
    }

    public boolean cancelRequest(User friend) {
        selectedFriend = friend;
        ClientRequests cr = new ClientRequests(user, friend);
        return cr.sendRequest(oout, Strings.USER_CANCEL_FRIEND_REQUEST);
    }

    public boolean deleteFriendship(User friend) {
        ClientRequests cr = new ClientRequests(user, friend);
        return cr.sendRequest(oout, Strings.USER_CANCEL_FRIENDSHIP);
    }

    /**
     * Groups
     */

    public boolean createNewGroup(String name){
        Group newGroup = new Group(user, name);
        ClientRequests cr = new ClientRequests(user, newGroup);
        return cr.sendRequest(oout, Strings.REQUEST_NEW_GROUP);
    }

    public boolean seeGroups(){
        ClientRequests cr = new ClientRequests(user);
        return cr.sendRequest(oout, Strings.USER_REQUEST_GROUPS);
    }

    public boolean seeGroupsPending(){
        ClientRequests cr = new ClientRequests(user);
        return cr.sendRequest(oout, Strings.USER_REQUEST_PENDING_GROUPS);
    }

    public boolean seeAvailableGroups(){
        ClientRequests cr = new ClientRequests(user);
        return cr.sendRequest(oout, Strings.USER_REQUEST_AVAILABLE_GROUPS);
    }

    public boolean seeManageGroups(){
        ClientRequests cr = new ClientRequests(user);
        return cr.sendRequest(oout, Strings.USER_REQUEST_MANAGE_GROUPS);
    }

    public void deleteGroup(Group g) {
        ClientRequests cr = new ClientRequests(user, g);
        cr.sendRequest(oout, Strings.USER_DELETE_GROUP);
    }

    public void quitGroup(Group g) {
        ClientRequests cr = new ClientRequests(user, g);
        cr.sendRequest(oout, Strings.USER_QUIT_GROUP);
    }

    public void quitGroup(User u, Group g){
        ClientRequests cr = new ClientRequests(u, g);
        cr.sendRequest(oout, Strings.USER_QUIT_GROUP);
    }

    public void manageMembers(Group g) {
        selectedGroup = g;
        ClientRequests cr = new ClientRequests(user, g);
        cr.sendRequest(oout, Strings.USER_MANAGE_GROUP);
    }

    public void changeName(Group g) {
        ClientRequests cr = new ClientRequests(user, g);
        cr.sendRequest(oout, Strings.USER_CHANGE_GROUP);
    }

    public void sendGroupRequest(Group g) {
        ClientRequests cr = new ClientRequests(user, g);
        cr.sendRequest(oout, Strings.USER_SEND_GROUP_REQUEST);
    }

    public void cancelGroupRequest(Group g) {
        ClientRequests cr = new ClientRequests(user, g);
        cr.sendRequest(oout, Strings.USER_QUIT_GROUP);
    }

    public void acceptGroupRequest(User u, Group g){
        ClientRequests cr = new ClientRequests(user, u, g);
        cr.sendRequest(oout, Strings.ADMIN_ACCEPT_GROUP_REQUEST);
    }

    /**
     * Messages
     */

    public boolean seeMessages(User selectedUser) {
        selectedFriend = selectedUser;
        selectedGroup = null;
        ClientRequests cr = new ClientRequests(user, selectedUser, messages);
        return cr.sendRequest(oout, Strings.USER_REQUEST_MESSAGES);
    }

    public boolean seeMessages(Group group){
        selectedGroup = group;
        selectedFriend = null;
        ClientRequests cr = new ClientRequests(user, group);
        return cr.sendRequest(oout, Strings.USER_REQUEST_MESSAGES);
    }

    public boolean deleteMessage(Message message) {
        ClientRequests cr = new ClientRequests(user, message);
        return cr.sendRequest(oout, Strings.MESSAGE_DELETE);
    }

    public boolean sendMessage(Message message) {
        ClientRequests cr = new ClientRequests(user, selectedFriend, message);
        return cr.sendRequest(oout, Strings.USER_SENT_MESSAGE);
    }

    public boolean sendMessageToGroup(Message message){
        ClientRequests cr = new ClientRequests(user, selectedGroup, message);
        return cr.sendRequest(oout, Strings.USER_SENT_MESSAGE);
    }

    /**
     * Files
     * @param file
     */

    public void sendFile(Message file){
        selectedGroup = null;
        ClientRequests cr = new ClientRequests(user, selectedFriend, file);
        cr.sendRequest(oout, Strings.USER_SEND_FILE);
    }

    public void sendFileToGroup(Message file){
        selectedFriend = null;
        ClientRequests cr = new ClientRequests(user, selectedGroup, file);
        cr.sendRequest(oout, Strings.USER_SEND_FILE);
    }

    public void uploadFileToServer(Message file){
        File f = new File(file.getFile().getPath());
        Thread u = new Thread(new UploadFile(f, serverAddress, filesPort));
        u.start();
    }

    public void downloadFile(Message file, File path){
        try {
            ServerSocket s = new ServerSocket(0);

            Thread d = new Thread(new DownloadFile(path, s), "Download thread");
            d.start();
            ClientRequests cr = new ClientRequests(user, file, s.getLocalPort());
            cr.sendRequest(oout, Strings.DOWNLOAD_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public void deleteFile(Message file){
        ClientRequests cr = new ClientRequests(user, file);
        cr.sendRequest(oout, Strings.USER_DELETE_FILE);
    }

    /** GETS */

    public User getUser() {
        return user;
    }

    public User getFriend() {
        return selectedFriend;
    }

    public Group getGroup() { return selectedGroup; }

    public List<FriendsRequests> getFriendsRequests() {
        return friends;
    }

    public List<FriendsRequests> getFriendsRequestsPending() {
        return friendsPending;
    }

    public List<FriendsRequests> getFriendsRequestsSent() {
        return friendsSent;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<GroupRequests> getMyGroups() {
        return myGroups;
    }

    public List<GroupRequests> getPendingGroups() {
        return pendingGroups;
    }

    public List<Group> getManageGroups() {
        return manageGroups;
    }

    public List<Group> getAvailableGroups() {
        return availableGroups;
    }

    public List<GroupRequests> getGroupMembers() { return groupMembers; }

    public SharedMessage getNotification() {
        synchronized (notLog) {
            while (notLog.isEmpty()) {
                try {
                    notLog.wait();
                } catch (InterruptedException e) {
                    System.out.println("[Data] Couldn't wait on this method:\r\n\t" + e);
                }
            }
            return notLog.remove(0);
        }
    }
}
