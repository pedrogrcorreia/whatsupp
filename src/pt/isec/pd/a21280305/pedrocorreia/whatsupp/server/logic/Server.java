package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic;

import java.io.*;
import java.net.*;

public class Server implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    static final String SERVER_REQUEST = "DEBUG SERVER REQUEST";
    static final int MAX_SIZE = 4096;
    static final int TIMEOUT = 10 * 1000; // 10 seconds timeout

    InetAddress serverManagerAddress;
    int serverManagerPort;
    int serverPort;
    String serverAddress;
    String dbAddress;

    // Send serialized objects
    ByteArrayInputStream bin;
    ObjectInputStream oin;
    ByteArrayOutputStream bout;
    ObjectOutputStream oout;


    DatagramSocket mySocket;
    DatagramPacket myPacket;

    String response;

    // Constructor to use when GRDS address is provided
    public Server(int serverPort, String dbAddress, InetAddress serverManagerAddress, int serverManagerPort) throws UnknownHostException {
        this.serverPort = serverPort;
        this.dbAddress = dbAddress;
        this.serverManagerAddress = serverManagerAddress;
        this.serverManagerPort = serverManagerPort;
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();
    }

    // Constructor to use when no GRDS address is provided
    public Server(int serverPort, String dbAddress) throws UnknownHostException {
        this.serverPort = serverPort;
        this.dbAddress = dbAddress;
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();
    }

    public void registerServer(){
        try{
            mySocket = new DatagramSocket();
            //System.out.println(mySocket.getLocalAddress().getHostName());
            mySocket.setSoTimeout(TIMEOUT);

            // Serialize this object
            bout = new ByteArrayOutputStream();
            oout = new ObjectOutputStream(bout);

            oout.writeUnshared(new Server(serverPort, dbAddress, serverManagerAddress, serverManagerPort));

            myPacket = new DatagramPacket(bout.toByteArray(), bout.size(), serverManagerAddress, serverManagerPort);
            mySocket.send(myPacket);

            // Deserialize response

            myPacket = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            mySocket.receive(myPacket);

            bin = new ByteArrayInputStream(myPacket.getData());
            oin = new ObjectInputStream(bin);

            response = (String) oin.readObject();

            System.out.println("Response was: " + response);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "\nServer at " + serverAddress + ":" + serverPort + " is connected.";
    }
}
