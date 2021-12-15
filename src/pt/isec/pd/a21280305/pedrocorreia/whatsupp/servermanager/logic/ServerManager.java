package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data.ActiveServers;

public class ServerManager {

    static final int MAX_SIZE = 4096;

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
        // activeServers = new Thread(new ActiveServers(), "Thread to verify active
        // servers");
    }

    public void startServerManager() {
        try {
            mySocket = new DatagramSocket(listeningPort);
            System.out.println("Server Manager initialized at port " + mySocket.getLocalPort());
            runServerManager();
        } catch (SocketException e) {
            System.out.println("Error at UDP socket: \r\n\t" + e);
        }
    }

    // Main thread running to receiver UDP requests
    private void runServerManager() {
        System.out.println("Ready to receive requests.");
        // activeServers.start();
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
                int tcpPort = Integer.parseInt(request.getMsg());
                activeServers.pingedServer(myPacket, tcpPort);
                answer = new SharedMessage(Strings.SERVER_PING, "Ping registered.");
                answerToRequest(answer, myPacket);
            } else if (request.getMsgType().equals(Strings.CLIENT_REQUEST_SERVER)) {
                // answer = new SharedMessage(Strings.CLIENT_REQUEST_SERVER,
                // String.valueOf(activeServers.registerClient()));
                answer = new SharedMessage(Strings.CLIENT_REQUEST_SERVER,
                        activeServers.registerClient());
                answerToRequest(answer, myPacket);
            }
        }
    }

    private boolean registerServers(DatagramPacket serverPacket) throws IOException, ClassNotFoundException {
        return activeServers.registerServer(serverPacket);
    }

    private SharedMessage receiveRequests() {
        try {
            SharedMessage newRequest;
            myPacket = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            mySocket.receive(myPacket);

            bin = new ByteArrayInputStream(myPacket.getData(), 0, myPacket.getLength());
            oin = new ObjectInputStream(bin);

            newRequest = (SharedMessage) oin.readObject();
            System.out.println("Request: " + newRequest.getMsgType() + " from " + myPacket.getPort());

            return newRequest;
        } catch (IOException e) {
            System.out.println("Error receiving request: \r\n\t" + e);
        } catch (ClassNotFoundException e) {
            System.out.println("Object sent is invalid: \r\n\t" + e);
        }
        return null;
    }

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
