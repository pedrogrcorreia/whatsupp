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
        registerServer();
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
            tcpSocket = new ServerSocket(0);
            System.out.println("TCP Server initialized at port " + tcpSocket.getLocalPort());
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
                // newClient = new ConnectionClient(nextClient, this);
                oout = new ObjectOutputStream(nextClient.getOutputStream());
                oin = new ObjectInputStream(nextClient.getInputStream());
                newClient = new ConnectionClient(nextClient, this, oout, oin);
                clientsConnected.add(newClient);
                newClient.start();
            }

        } catch (IOException e) {
            System.out.println("Error creating TCP Socket: \r\n\t" + e);
        }
    }

    public void alertClients() {
        for (int i = 0; i < clientsConnected.size(); i++) {
            // System.out.println("ESTOU AQUI");
            // oout.writeObject(new SharedMessage(Strings.USER_FAILED_LOGIN, "teste"));
            // oout.flush();
            SharedMessage request = new SharedMessage(Strings.NEW_MESSAGE, Strings.NEW_MESSAGE.toString());
            clientsConnected.get(i)
                    .sendMsgToClient(request);

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

    public ServerSocket getTcpSocket() {
        return tcpSocket;
    }
}
