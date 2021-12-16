package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection.ConnectionClient;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection.ConnectionServerManager;
// import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection.PingServerManager;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.data.DBManager;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {

    @Serial
    private static final long serialVersionUID = 1L;

    static final int MAX_SIZE = 4096;
    static final int TIMEOUT = 10 * 1000; // 10 seconds timeout

    // Server variables
    InetAddress serverManagerAddress;
    int serverManagerPort;
    String serverAddress;
    String dbAddress;
    // int serverPort;

    // UDP communication
    DatagramSocket mySocket;
    DatagramPacket myPacket;

    // Object serialization
    ByteArrayInputStream bin;
    ObjectInputStream oin;
    ByteArrayOutputStream bout;
    ObjectOutputStream oout;

    // Strings responseFromServerManager;
    SharedMessage responseFromServerManager;
    SharedMessage receivedFromServerManager;

    // Thread to ping server manager
    Thread pingServerManager;

    // Thread to receive requests from ServerManager
    Thread connectionServerManager;

    // Data management
    DBManager dbManager;

    // Clients communication
    ServerSocket tcpSocket;
    Socket nextClient;

    // Thread for each client
    ConnectionClient newClient;

    // List of connected clients
    List<Socket> clients;

    // Constructor to use when GRDS address is provided
    public Server(String dbAddress, InetAddress serverManagerAddress, int serverManagerPort)
            throws UnknownHostException, SocketException {
        this.dbAddress = dbAddress;
        this.serverManagerAddress = serverManagerAddress;
        this.serverManagerPort = serverManagerPort;
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();
        registerServer();
    }

    // Constructor to use when no GRDS address is provided
    public Server(int serverPort, String dbAddress) throws UnknownHostException, SocketException {
        this.dbAddress = dbAddress;
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();
        registerServer();
    }

    // Constructor to create thread
    public Server(DatagramPacket serverPacket) {
        this.myPacket = serverPacket;
    }

    public void startServer() {
        registerServer();
    }

    private void registerServer() {
        try {
            mySocket = new DatagramSocket();
            // serverPort = mySocket.getPort();
            // TODO set socket timeout?
            sendToServerManager(new SharedMessage(Strings.SERVER_REGISTER_REQUEST,
                    "Server wants to register to this Server Manager."));
            receivedFromServerManager = receiveFromServerManager();
            System.out.println(receivedFromServerManager.getMsg());
            if (!receivedFromServerManager.getMsgType().equals(Strings.SERVER_REGISTER_SUCCESS)) {
                return;
            } else {
                runServer();
            }
        } catch (SocketException e) {
            System.out.println("Error connecting the socket:\r\n\t" + e);
        } catch (IOException e) {
            System.out.println("Error creating tcp socket: \r\n\t" + e);
        }
    }

    private void runServer() {
        try {
            tcpSocket = new ServerSocket(0);
            System.out.println("TCP Server initialized at port " + tcpSocket.getLocalPort());
            pingServerManager = new Thread(new PingServerManager(this), "Thread to ping Server Manager");
            connectionServerManager = new Thread(new ConnectionServerManager(this),
                    "Thread to receive communication from ServerManager");
            pingServerManager.start();
            connectionServerManager.start();
            clients = new ArrayList<>();
            // dbManager = new DBManager(this);
            // Main thread just accepts connections from clients
            while (true) {
                nextClient = tcpSocket.accept();
                clients.add(nextClient);
                newClient = new ConnectionClient(nextClient);
                newClient.start();
            }

        } catch (IOException e) {
            System.out.println("Error creating TCP Socket: \r\n\t" + e);
        }
    }

    // public void sendToServerManager(Strings msgToSend){
    public void sendToServerManager(SharedMessage msgToSend) {
        try {
            // Serialize object to send
            bout = new ByteArrayOutputStream();
            oout = new ObjectOutputStream(bout);
            oout.writeUnshared(msgToSend);

            myPacket = new DatagramPacket(bout.toByteArray(), bout.size(), serverManagerAddress, serverManagerPort);
            mySocket.send(myPacket);
        } catch (IOException e) {
            System.out.println("Socket or IO exception: \r\n\t" + e);
        }
    }

    // public Strings receiveFromServerManager(){
    public SharedMessage receiveFromServerManager() {
        try {
            // Clear packet
            myPacket = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            mySocket.receive(myPacket);

            // Desserialize object
            bin = new ByteArrayInputStream(myPacket.getData());
            oin = new ObjectInputStream(bin);
            responseFromServerManager = (SharedMessage) oin.readObject();

            return responseFromServerManager;

        } catch (IOException e) {
            System.out.println("Socket or IO exception: \r\n\t" + e);
        } catch (ClassNotFoundException e) {
            System.out.println("Invalid object sent: \r\n\t" + e);
        }
        return null;
    }

    public String getDB() {
        return dbAddress;
    }

    public DatagramPacket getServerPacket() {
        return myPacket;
    }

    public int getTcpPort() {
        return tcpSocket.getLocalPort();
    }

    // @Override
    // public String toString() {
    // return "\nServer at " + serverAddress + ":" + serverPort + " is connected.";
    // }
}
