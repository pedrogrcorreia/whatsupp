package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection.PingServerManager;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.data.DBManager;
import java.io.*;
import java.net.*;

public class Server {

    @Serial
    private static final long serialVersionUID = 1L;

    static final int MAX_SIZE = Strings.MaxSize();
    static final int TIMEOUT = 10 * 1000; // 10 seconds timeout

    // Server variables
    InetAddress serverManagerAddress;
    int serverManagerPort;
    int serverPort;
    String serverAddress;
    String dbAddress;

    // UDP communication
    DatagramSocket mySocket;
    DatagramPacket myPacket;

    // Object serialization
    ByteArrayInputStream bin;
    ObjectInputStream oin;
    ByteArrayOutputStream bout;
    ObjectOutputStream oout;

    Strings responseFromServerManager;

    // Threads
    Thread pingServerManager;

    // Data management
    DBManager dbManager;

    // Clients communication
    ServerSocket tcpSocket;

    // Constructor to use when GRDS address is provided
    public Server(int serverPort, String dbAddress, InetAddress serverManagerAddress, int serverManagerPort) throws UnknownHostException, SocketException {
        this.serverPort = serverPort;
        this.dbAddress = dbAddress;
        this.serverManagerAddress = serverManagerAddress;
        this.serverManagerPort = serverManagerPort;
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();
        registerServer();
    }

    // Constructor to use when no GRDS address is provided
    public Server(int serverPort, String dbAddress) throws UnknownHostException, SocketException {
        this.serverPort = serverPort;
        this.dbAddress = dbAddress;
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();
        registerServer();
    }

    // Constructor to create thread
    public Server(DatagramPacket serverPacket){
        this.myPacket = serverPacket;
    }

    public void startServer(){
        registerServer();
    }

    private void registerServer(){
        try {
            mySocket = new DatagramSocket(serverPort);
            //TODO set socket timeout?
            sendToServerManager(Strings.SERVER_REGISTER_REQUEST);
            if (!receiveFromServerManager().equals(Strings.SERVER_REGISTER_SUCCESS)) {
                System.out.println("Can't connect to ServerManager.");
            } else {
                System.out.println("Connected successfuly to ServerManager");
                tcpSocket = new ServerSocket();
                pingServerManager = new Thread(new PingServerManager(this));
                pingServerManager.start();
                runServer();
            }
        }catch(SocketException e){
            System.out.println("Error connecting the socket:\r\n\t" + e);
        } catch (IOException e) {
            System.out.println("Error creating tcp socket: \r\n\t" + e);
        }
    }

    private void runServer(){
        // This main thread keeps running to accept request from ServerManager
        while(true){
            String response = receiveFromServerManager().toString();
            System.out.println("Server Manager said: " + response);
        }
    }

    public void sendToServerManager(Strings msgToSend){
        try{
            // Serialize object to send
            bout = new ByteArrayOutputStream();
            oout = new ObjectOutputStream(bout);
            oout.writeUnshared(msgToSend);

            myPacket = new DatagramPacket(bout.toByteArray(), bout.size(), serverManagerAddress, serverManagerPort);
            mySocket.send(myPacket);
        }catch(IOException e){
            System.out.println("Socket or IO exception: \r\n\t" + e);
        }
    }

    public Strings receiveFromServerManager(){
        try {
            // Clear packet
            myPacket = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            mySocket.receive(myPacket);

            // Desserialize object
            bin = new ByteArrayInputStream(myPacket.getData());
            oin = new ObjectInputStream(bin);
            responseFromServerManager = (Strings)oin.readObject();

            return responseFromServerManager;

        }catch(IOException e){
            System.out.println("Socket or IO exception: \r\n\t" + e);
        } catch(ClassNotFoundException e){
            System.out.println("Invalid object sent: \r\n\t" + e);
        }
        return null;
    }

    public String getDB(){
        return dbAddress;
    }

    public DatagramPacket getServerPacket(){
        return myPacket;
    }

    public int getTcpPort(){
        return tcpSocket.getLocalPort();
    }

    @Override
    public String toString() {
        return "\nServer at " + serverAddress + ":" + serverPort + " is connected.";
    }
}
