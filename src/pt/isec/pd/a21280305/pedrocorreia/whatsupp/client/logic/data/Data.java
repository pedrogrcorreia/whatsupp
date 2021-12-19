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
import java.util.Scanner;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.GetRequestFromServer;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.RequestServer;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientRequestLogin;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientRequestRegister;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientServerConnection;

public class Data {
    private static final int MAX_SIZE = 4096;

    private User user;

    // To communicate with Server Manager
    protected String serverManagerAddress;
    protected int serverManagerPort;
    protected String serverAddress;
    protected int serverPort;
    protected InetAddress serverManager;

    DatagramSocket socket;
    DatagramPacket packet;
    ByteArrayOutputStream bout;
    ObjectOutputStream oout;
    ByteArrayInputStream bin;
    ObjectInputStream oin;

    protected boolean connected = false;

    // To communicate with Server
    protected Socket socketToServer;
    protected RequestServer request;
    protected GetRequestFromServer requestFromServer;

    public Data(String serverManagerAddress, int serverManagerPort) {
        this.serverManagerAddress = serverManagerAddress;
        this.serverManagerPort = serverManagerPort;
    }

    public boolean contactServerManager() {

        try {
            System.out.println("create connection");

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
            System.out.println("SMA " + serverAddress);
            System.out.println("SP " + serverPort);
            socketToServer = new Socket(serverAddress, serverPort);
            oin = new ObjectInputStream(socketToServer.getInputStream());
            oout = new ObjectOutputStream(socketToServer.getOutputStream());
            requestFromServer = new GetRequestFromServer(this, socketToServer);
            // requestFromServer.start();
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

    public boolean getConnected() {
        return connected;
    }

    public boolean login(String username, String password) {
        RequestServer request = new RequestServer(socketToServer, oin, oout);
        return request.sendLogin(username, password);
    }

    public boolean register(String username, String password, String confPassword, String fname, String lname) {
        RequestServer requestRegister = new RequestServer(socketToServer, oin, oout);
        return requestRegister.sendRegister(username, password, confPassword, fname,
                lname);
    }

    public String getNotification() {
        SharedMessage debug;
        try {
            debug = (SharedMessage) oin.readObject();
        } catch (ClassNotFoundException | IOException e) {
            return "erro";
        }
        return debug.getMsg();
    }
}
