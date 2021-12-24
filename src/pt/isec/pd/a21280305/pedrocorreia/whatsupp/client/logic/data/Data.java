package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientRequestFriends;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientRequestLogin;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientRequestRegister;

public class Data {
    private static final int MAX_SIZE = 4096;

    protected static User user = new User();
    static boolean loggedIn = false;

    // To communicate with Server Manager
    protected String serverManagerAddress;
    protected int serverManagerPort;
    protected String serverAddress;
    protected int serverPort;
    protected InetAddress serverManager;

    public List<SharedMessage> notLog;

    DatagramSocket socket;
    DatagramPacket packet;
    ByteArrayOutputStream bout;
    ObjectOutputStream oout;
    ByteArrayInputStream bin;
    ObjectInputStream oin;

    protected boolean connected = false;

    // To communicate with Server
    protected Socket socketToServer;

    protected static List<String> messages;
    protected static List<User> friends;
    protected List<String> groups;

    Thread t;
    Notifications not;

    ClientRequestLogin crl;

    public Data(String serverManagerAddress, int serverManagerPort) {
        this.serverManagerAddress = serverManagerAddress;
        this.serverManagerPort = serverManagerPort;
        notLog = new ArrayList<>();
        friends = new ArrayList<>();
        messages = new ArrayList<>();
        groups = new ArrayList<>();
    }

    public Data() {
        notLog = new ArrayList<>();
        friends = new ArrayList<>();
        messages = new ArrayList<>();
        groups = new ArrayList<>();
    };

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
            System.out.println("Packet: " + packet.getAddress().getHostAddress() + ":" +
                    packet.getPort());
            // return true;
        } catch (IOException e) {
            System.out.println("Error creating socket and packet:\r\n\t" + e);
            return false;
        }

        try {
            serverManager = InetAddress.getByName(serverManagerAddress);
        } catch (UnknownHostException e) {
            System.out.println("Error recognizing the host:\r\n\t" + e);
        }
        try {
            System.out.println(packet.getAddress().getHostAddress());
            socket.send(packet);
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout on sending message to Server Manager:\r\n\t" +
                    e);
            return false;
        } catch (IOException e) {
            System.out.println("Error at sending the message to Server Manager:\r\n\t" +
                    e);
            return false;
        }

        try {
            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            socket.receive(packet);
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout on receiving message from Server Manager:\r\n\t"
                    + e);
            return false;
        } catch (IOException e) {
            packet = new DatagramPacket(bout.toByteArray(), bout.size(), serverManager,
                    serverManagerPort);
            System.out.println("Error at receiving the message to Server Manager:\r\n\t"
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
            // System.out.println("SMA " + serverAddress);
            // System.out.println("SP " + serverPort);
            socketToServer = new Socket(serverAddress, serverPort);
            oin = new ObjectInputStream(socketToServer.getInputStream());
            oout = new ObjectOutputStream(socketToServer.getOutputStream());

            // notIn = new ObjectInputStream(socketToServer.getInputStream());
            // notOut = new ObjectOutputStream(socketToServer.getOutputStream());
            not = new Notifications(oin, oout, notLog);

            synchronized (notLog) {
                notLog.add(new SharedMessage(Strings.CLIENT_REQUEST_SERVER,
                        new String("Connected successfully to a server.")));
                notLog.notifyAll();
            }

            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("Message error:\r\n\t" + e);
            return false;
        } catch (UnknownHostException e) {
            System.out.println("Couldn't connect to that host:\r\n\t" + e);
            return false;
        } catch (BindException e) {
            System.out.println("No servers available:\r\n\t" + e);
            return false;
        } catch (IOException e) {
            System.out.println("Error on socket:\r\n\t" + e);
            return false;
        }
    }

    public boolean login(String username, String password) {
        crl = new ClientRequestLogin(new User(username, password));
        user = crl.getUser();
        return crl.login(oin, oout, notLog);
    }

    public boolean register(String username, String password, String confPassword, String fName, String lName) {
        ClientRequestRegister crr = new ClientRequestRegister(new User(username, password, confPassword, fName, lName));
        return crr.register(oin, oout, notLog);
    }

    public boolean retrieveInfo() {
        t = new Thread(not);
        t.start();
        ClientRequestFriends crf = new ClientRequestFriends(user, friends);
        crf.getFriends(oout);
        user = crl.getUser();
        for (User friend : friends) {
            System.out.println("Friend :");
            System.out.println(friend);
        }
        return true;
    }

    public List<User> getFriends() {
        return friends;
    }

    public User getUser() {
        return user;
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<String> groups() {
        return groups;
    }

    public SharedMessage getNotification() {
        synchronized (notLog) {
            while (notLog.isEmpty()) {
                try {
                    notLog.wait();
                } catch (InterruptedException e) {
                    System.out.println("Couldn't wait on this method:\r\n\t" + e);
                }
            }
            return notLog.remove(0);
        }
    }
}
