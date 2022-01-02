package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection.ConnectionClient;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection.ConnectionServerManager;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection.PingServerManager;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.data.DBManager;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates an instance of a Server.
 * 
 * @author Pedro Correia
 */
public class Server {

    @Serial
    private static final long serialVersionUID = 1L;

    static final int MAX_SIZE = 8192;
    static final int TIMEOUT = 10 * 1000; // 10 seconds timeout
    static int i = 0;

    // Server variables
    InetAddress serverManagerAddress;
    int serverManagerPort;
    String serverAddress;

    String dbAddress;
    // int serverPort;

    /** UDP communication */
    DatagramSocket mySocket;
    DatagramPacket myPacket;

    /** Object serialization. */
    ByteArrayInputStream bin;
    ObjectInputStream oin;
    ByteArrayOutputStream bout;
    ObjectOutputStream oout;

    /** Messages sent and received */
    SharedMessage responseFromServerManager;
    SharedMessage receivedFromServerManager;

    /** Thread to keep server alive on Server Manager */
    Thread pingServerManager;

    /** Thread to receive requests from ServerManager */
    Thread connectionServerManager;

    /** Object that makes the connection to the database */
    DBManager dbManager;

    /** TCP communication */
    ServerSocket tcpSocket;
    Socket nextClient;
    ServerSocket filesSocket;

    /** Thread to communicate with each new client */
    ConnectionClient newClient;

    /** ArrayList of all connected clients */
    List<Socket> clients;
    List<ConnectionClient> clientsConnected;

    /**
     * Constructor of Server to create an initial instance when the Server
     * Manager address and port is provided.
     * 
     * @param dbAddress            - provided database address
     * @param serverManagerAddress - provided Server Manager address
     * @param serverManagerPort    - provided Server Manager UDP listening port
     * @throws UnknownHostException on requesting the local host address
     * @throws SocketException
     * @see UnknownHostException
     * @see SocketException
     */

    public Server(String dbAddress, InetAddress serverManagerAddress, int serverManagerPort)
            throws UnknownHostException, SocketException {
        this.dbAddress = dbAddress;
        this.serverManagerAddress = serverManagerAddress;
        this.serverManagerPort = serverManagerPort;
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();
        registerServer();
    }

    /**
     * Constructor of Server to create an initial instance when the Server
     * Manager address and port is NOT provided.
     * 
     * @param dbAddress - provided database address
     * @throws UnknownHostException
     * @throws SocketException
     */

    public Server(String dbAddress) throws UnknownHostException, SocketException {
        this.dbAddress = dbAddress;
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();
        registerServerMulticast();
    }

    /**
     * Constructor of Server to create an instance of this object to
     * run on a separate {@code Thread}.
     * 
     * @param serverPacket
     */

    public Server(DatagramPacket serverPacket) {
        this.myPacket = serverPacket;
    }

    public void startServer() {
        registerServer();
    }

    /**
     * Sends a request to the Server Manager to register
     * this as an active {@code Server} through UDP.
     */

    private void registerServer() {
        System.out.println("Contacting Server Manager");
        i++;
        try {
            mySocket = new DatagramSocket();
            mySocket.setSoTimeout(3000);

            SharedMessage msgToSend = new SharedMessage(Strings.SERVER_REGISTER_REQUEST, new String(""));
            bout = new ByteArrayOutputStream();
            oout = new ObjectOutputStream(bout);
            oout.writeUnshared(msgToSend);

            myPacket = new DatagramPacket(bout.toByteArray(), bout.size(), serverManagerAddress, serverManagerPort);
            mySocket.send(myPacket);

            myPacket = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            mySocket.receive(myPacket);

            // Desserialize object
            bin = new ByteArrayInputStream(myPacket.getData());
            oin = new ObjectInputStream(bin);
            responseFromServerManager = (SharedMessage) oin.readObject();

            System.out.println(responseFromServerManager.getMsg());
            if (!responseFromServerManager.getMsgType().equals(Strings.SERVER_REGISTER_SUCCESS)) {
                return;
            } else {
                runServer();
            }
        } catch (SocketException | SocketTimeoutException e){
            System.out.println("Server Manager not responding...\nAttempt " + i);
            registerServer();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void registerServerMulticast() {
        System.out.println("Contacting Server Manager");
        i++;
        try {
            serverManagerAddress = InetAddress.getByName("230.30.30.30");
            serverManagerPort = 3030;
            mySocket = new DatagramSocket();
            mySocket.setSoTimeout(3000);

            SharedMessage msgToSend = new SharedMessage(Strings.SERVER_REGISTER_REQUEST, new String(""));
            bout = new ByteArrayOutputStream();
            oout = new ObjectOutputStream(bout);
            oout.writeUnshared(msgToSend);

            myPacket = new DatagramPacket(bout.toByteArray(), bout.size(), serverManagerAddress, serverManagerPort);
            mySocket.send(myPacket);

            myPacket = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            mySocket.receive(myPacket);

            // Desserialize object
            bin = new ByteArrayInputStream(myPacket.getData());
            oin = new ObjectInputStream(bin);
            responseFromServerManager = (SharedMessage) oin.readObject();

            System.out.println(responseFromServerManager.getMsg());
            if (!responseFromServerManager.getMsgType().equals(Strings.SERVER_REGISTER_SUCCESS)) {
                return;
            } else {
                runServer();
            }
        } catch (SocketException | SocketTimeoutException e){
            System.out.println("[Multicast] Server Manager not responding...\nAttempt " + i);
            registerServerMulticast();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the {@code Thread} to ping the Server Manager.
     * Starts the {@code Thread} to receive requests from Server Manager.
     * This method is running to accept clients on the main {@code Thread}
     * of the Server that are redirected from the Server Manager.
     */

    private void runServer() {
        try {
            mySocket.setSoTimeout(0);
            tcpSocket = new ServerSocket(0);
            System.out.println("TCP Server initialized at port " + tcpSocket.getLocalPort());
            filesSocket = new ServerSocket(0);
            pingServerManager = new Thread(new PingServerManager(this), "Thread to ping Server Manager");
            connectionServerManager = new Thread(new ConnectionServerManager(this),
                    "Thread to receive communication from ServerManager");
            pingServerManager.start();
            connectionServerManager.start();
            clients = new ArrayList<>();
            clientsConnected = new ArrayList<>();
            while (true) {
                nextClient = tcpSocket.accept();
                clients.add(nextClient);
                oout = new ObjectOutputStream(nextClient.getOutputStream());
                oin = new ObjectInputStream(nextClient.getInputStream());
                newClient = new ConnectionClient(nextClient, this, oout, oin, filesSocket);
                clientsConnected.add(newClient);
                newClient.start();
            }

        } catch (IOException e) {
            System.out.println("Error creating TCP Socket: \r\n\t" + e);
        }
    }

    /**
     * Alert clients of a new notification
     * @param request - notification type
     */

    public void alertClients(SharedMessage request) {
        for (int i = 0; i < clientsConnected.size(); i++) {
            clientsConnected.get(i).sendMsgToClient(request);
        }
    }

    /**
     * Sends a message to ServerManager.
     * 
     * @param msgToSend a {@code SharedMessage} to be sent
     */

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

    /**
     * Receives requests from Server Manager through UDP.
     * 
     * @return the {@code SharedMessage} that was received.
     */

    public void deleteFile(String fPath) {
        File dFile = null;
        try {
            dFile = new File(new File(fPath).getCanonicalPath());
            if(dFile.delete()){
                System.out.println("File deleted successfully");
            }
            else{
                System.out.println("Couldn't delete the requested file");
            }
        } catch (IOException e) {
            System.out.println("Couldn't delete the file:\r\n\t " + e);
        }

    }

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

    public int getTcpFilesPort() { return filesSocket.getLocalPort(); }

    public ServerSocket getTcpSocket() {
        return tcpSocket;
    }

    public String getServerAddress() {
        return serverAddress;
    }
}
