package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

public class ServerManager {

    static final int MAX_SIZE = 4096;
    static final String SERVER_REQUEST = "DEBUG SERVER REQUEST";

    List<Server> activeServers;

    int listeningPort;

    DatagramSocket mySocket = null;
    DatagramPacket myPacket;
    String receivedMsg;

    // To receive server requests
    Server receivedServer;
    ByteArrayInputStream bin;
    ObjectInputStream oin;
    ByteArrayOutputStream bout;
    ObjectOutputStream oout;

    String responseToServer;

    public ServerManager(int listeningPort){
        this.listeningPort = listeningPort;
    }

    public void initializeServerManager(){
        try{
            mySocket = new DatagramSocket(listeningPort);

            System.out.println("Server Manager initialized at port " + mySocket.getLocalPort());
        } catch(SocketException e){
            System.out.println("Error at UDP socket: \r\n\t" + e);
        }
    }

    public void registerServers() throws IOException, ClassNotFoundException {

        System.out.println("Ready to receive server registers.");

        while(true){
            myPacket = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            mySocket.receive(myPacket);

            // Receive serialized object
            bin = new ByteArrayInputStream(myPacket.getData(), 0, myPacket.getLength());
            oin = new ObjectInputStream(bin);

            receivedServer = (Server) oin.readObject();
            System.out.println("Received request \"" + receivedServer + "\" from " + myPacket.getAddress().getHostAddress() + ":"
                    + myPacket.getPort());


            //TODO Verify is server can be added
            activeServers.add(receivedServer);


            // Serialize response to server
            responseToServer = "Server registered successfully";

            bout = new ByteArrayOutputStream();
            oout = new ObjectOutputStream(bout);
            oout.writeUnshared(responseToServer);

            myPacket.setData(bout.toByteArray());
            myPacket.setLength(bout.size());

            mySocket.send(myPacket);
        }
    }
}
