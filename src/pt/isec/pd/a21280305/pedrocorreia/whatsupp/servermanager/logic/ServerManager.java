package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data.ActiveServers;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data.ConnectedServer;

public class ServerManager {

    static final int MAX_SIZE = 8192;

    ActiveServers activeServers;

    int listeningPort;

    DatagramSocket mySocket = null;
    DatagramPacket myPacket;

    ByteArrayInputStream bin;
    ObjectInputStream oin;

    ByteArrayOutputStream bout;
    ObjectOutputStream oout;

    // To communicate with requests
    SharedMessage request;
    SharedMessage answer;

    // Thread activeServers;

    public ServerManager(int listeningPort) {
        this.listeningPort = listeningPort;
        activeServers = new ActiveServers();
    }

    /**
     * Starts the Server Manager by creating a socket
     * on the given listening port.
     */

    public void startServerManager() {
        try {
            mySocket = new DatagramSocket(listeningPort);
            System.out.println("Server Manager initialized at port " + mySocket.getLocalPort());
            runServerManager();
        } catch (SocketException e) {
            System.out.println("Error at UDP socket: \r\n\t" + e);
        }
    }

    /**
     * Main thread receiving UDP requests.
     */

    private void runServerManager() {
        System.out.println("Ready to receive requests.");
        while (true) {
            request = receiveRequests();
            if (request.getMsgType().equals(Strings.SERVER_REGISTER_REQUEST)) {
                try {
                    if (!registerServers(myPacket)) {
                        answer = new SharedMessage(Strings.SERVER_REGISTER_SUCCESS,
                                new String("Registered successfully."));
                        answerToRequest(answer, myPacket);
                    } else {
                        answer = new SharedMessage(Strings.SERVER_REGISTER_FAIL,
                                new String("Couldn't register the server."));
                        answerToRequest(answer, myPacket);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error registering the server.");
                }
            } else if (request.getMsgType().equals(Strings.SERVER_PING)) {
                String ports = request.getMsg();
                String[] split = ports.split(":");
                int tcpPort = Integer.parseInt(split[0]);
                int filesPort = Integer.parseInt(split[1]);

                System.out.println(tcpPort + ":" + filesPort);
//                int tcpPort = Integer.parseInt(request.getMsg());

                activeServers.pingedServer(myPacket, tcpPort, filesPort);
                answer = new SharedMessage(Strings.SERVER_PING, "Ping registered.");
                answerToRequest(answer, myPacket);
            } else if (request.getMsgType().equals(Strings.CLIENT_REQUEST_SERVER)) {
                answer = new SharedMessage(Strings.CLIENT_REQUEST_SERVER,
                        activeServers.registerClient());
                answerToRequest(answer, myPacket);
            } else {
                answerToAll(request);
            }
            // } else if (request.getMsgType().equals(Strings.NEW_MESSAGE)) {
            // // answer = new SharedMessage(Strings.NEW_MESSAGE,
            // // Strings.NEW_MESSAGE.toString());
            // // answerToAll(answer);
            // answerToAll(request);
            // } else if (request.getMsgType().equals(Strings.NEW_FRIEND)) {
            // answerToAll(request);
            // }
        }
    }

    /**
     * Register servers on the active servers list.
     * 
     * @param serverPacket - {@code DatagramPacket} of the Server to be registered.
     * @return {@code true} if the Server is added. {@code false} if some error
     *         ocurred.
     * @throws IOException
     * @throws ClassNotFoundException
     */

    private boolean registerServers(DatagramPacket serverPacket) throws IOException, ClassNotFoundException {
        return activeServers.registerServer(serverPacket);
    }

    /**
     * Receives requests through UDP.
     * 
     * @return {@code SharedMessage} that was received.
     */

    private SharedMessage receiveRequests() {
        try {
            SharedMessage newRequest;
            myPacket = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            mySocket.receive(myPacket);

            bin = new ByteArrayInputStream(myPacket.getData(), 0, myPacket.getLength());
            oin = new ObjectInputStream(bin);

            newRequest = (SharedMessage) oin.readObject();
            System.out.println("Request: " + newRequest.getMsgType().name() + " from " + myPacket.getPort());

            return newRequest;
        } catch (IOException e) {
            System.out.println("Error receiving request: \r\n\t" + e);
        } catch (ClassNotFoundException e) {
            System.out.println("Object sent is invalid: \r\n\t" + e);
        }
        return null;
    }

    /**
     * Sends a message to a server.
     * 
     * @param msgToSend    - {@code SharedMessage} to be sent.
     * @param serverPacket {@code DatagramPacket} of the server
     *                     that the message is addressed.
     */
    private void answerToRequest(SharedMessage msgToSend, DatagramPacket serverPacket) {
        try {
            bout = new ByteArrayOutputStream();
            oout = new ObjectOutputStream(bout);
            oout.writeUnshared(msgToSend);

            serverPacket.setData(bout.toByteArray());
            serverPacket.setLength(bout.size());
            mySocket.send(serverPacket);
        } catch (IOException e) {
            System.out.println("Error writing object: \r\n\t" + e);
        }
    }

    private void answerToAll(SharedMessage msgToSend) {
        List<ConnectedServer> cs = activeServers.getServers();
        for (int i = 0; i < cs.size(); i++) {
            try {
                bout = new ByteArrayOutputStream();
                oout = new ObjectOutputStream(bout);
                oout.writeUnshared(msgToSend);
                DatagramPacket serverPacket;
                serverPacket = cs.get(i).getServerPacket();
                serverPacket.setData(bout.toByteArray());
                serverPacket.setLength(bout.size());
                mySocket.send(serverPacket);
            } catch (IOException e) {
                System.out.println("Error writing object:\r\n\t" + e);
            }
        }
    }

    //
    // TO-DO
    // 1. Create method to send message to all servers
    // 2. Create method to send message to all servers except one.
    //

    // private void echoToServer(String msgToSend, DatagramPacket serverPacket)
    // throws IOException{
    // serverPacket.setData(msgToSend.getBytes());
    // serverPacket.setLength(msgToSend.length());
    // mySocket.send(serverPacket);
    // }

    // private void echoToAllServers(String msgToSend) throws IOException {

    // // for(DatagramPacket activeServersPacket : activeServersPackets){
    // // activeServersPacket.setData(msgToSend.getBytes());
    // // activeServersPacket.setLength(msgToSend.length());
    // // mySocket.send(activeServersPacket);
    // // }
    // }
}
