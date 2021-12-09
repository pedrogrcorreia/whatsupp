package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;

public class ServerManager {

    static final int MAX_SIZE = Strings.MaxSize();

    List<DatagramPacket> activeServersPackets;

    int listeningPort;

    DatagramSocket mySocket = null;
    DatagramPacket myPacket;
    String receivedRequest;

    ByteArrayInputStream bin;
    ObjectInputStream oin;

    ByteArrayOutputStream bout;
    ObjectOutputStream oout;

    // To receive server requests

    Strings requestFromServer;

    public ServerManager(int listeningPort){
        this.listeningPort = listeningPort;
        activeServersPackets = new ArrayList<>();
    }

    public void startServerManager(){
        try{
            mySocket = new DatagramSocket(listeningPort);
            System.out.println("Server Manager initialized at port " + mySocket.getLocalPort());
            runServerManager();
        } catch(SocketException e){
            System.out.println("Error at UDP socket: \r\n\t" + e);
        }
    }

    // Main thread running to receiver UDP requests
    private void runServerManager(){
        System.out.println("Ready to receive requests.");
        while(true){
            Strings request;
            request = receiveRequests();
            if(request.equals(Strings.SERVER_REGISTER_REQUEST)){
                try {
                    if(!registerServers(myPacket)){
                        answerToServer(Strings.SERVER_REGISTER_SUCCESS, myPacket);
                    }
                    else{
                        answerToServer(Strings.SERVER_REGISTER_FAIL, myPacket);
                    }
                }catch(IOException | ClassNotFoundException e){
                    System.out.println("Error registering the server.");
                }
            }
            else if(request.equals(Strings.SERVER_PING)){
                answerToServer(Strings.SERVER_PING, myPacket);
            }
        }
    }

    private boolean registerServers(DatagramPacket serverPacket) throws IOException, ClassNotFoundException {

        boolean isRegistered = false;

        for (DatagramPacket activeServersPacket : activeServersPackets) {
            if (activeServersPacket.getAddress().equals(serverPacket.getAddress()) && activeServersPacket.getPort() == serverPacket.getPort()){
                isRegistered = true;
                System.out.println("Server already registered.");
                break;
            }
        }

        if(!isRegistered){
            System.out.println("Server registered successfully.");
            activeServersPackets.add(serverPacket);
        }

        return isRegistered;
    }

    private Strings receiveRequests(){
        try {
            myPacket = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            mySocket.receive(myPacket);

            bin = new ByteArrayInputStream(myPacket.getData(), 0, myPacket.getLength());
            oin = new ObjectInputStream(bin);

            requestFromServer = (Strings) oin.readObject();

            System.out.println("Request from server: " + requestFromServer.toString());

            return requestFromServer;
        } catch(IOException e){
            System.out.println("Error receiving request: \r\n\t" + e);
        } catch(ClassNotFoundException e){
            System.out.println("Object sent is invalid: \r\n\t" + e);
        }
        return null;
    }

    private void answerToServer(Strings msgToSend, DatagramPacket serverPacket){
        try {
            bout = new ByteArrayOutputStream();
            oout = new ObjectOutputStream(bout);
            oout.writeUnshared(msgToSend);

            serverPacket.setData(bout.toByteArray());
            serverPacket.setLength(bout.size());
            mySocket.send(serverPacket);
        } catch(IOException e){
            System.out.println("Error writing object: \r\n\t" + e);
        }
    }



    private void echoToServer(String msgToSend, DatagramPacket serverPacket) throws IOException{
        serverPacket.setData(msgToSend.getBytes());
        serverPacket.setLength(msgToSend.length());
        mySocket.send(serverPacket);
    }

    private void echoToAllServers(String msgToSend) throws IOException {

        for(DatagramPacket activeServersPacket : activeServersPackets){
            activeServersPacket.setData(msgToSend.getBytes());
            activeServersPacket.setLength(msgToSend.length());
            mySocket.send(activeServersPacket);
        }
    }
}
