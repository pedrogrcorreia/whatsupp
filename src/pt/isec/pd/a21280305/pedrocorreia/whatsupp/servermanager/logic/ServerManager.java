package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data.ActiveServers;

public class ServerManager {

    static final int MAX_SIZE = Strings.MaxSize();

    ActiveServers activeServers;

    int listeningPort;

    DatagramSocket mySocket = null;
    DatagramPacket myPacket;

    ByteArrayInputStream bin;
    ObjectInputStream oin;

    ByteArrayOutputStream bout;
    ObjectOutputStream oout;

    // To receive server requests
    Strings requestFromServer;

    public ServerManager(int listeningPort){
        this.listeningPort = listeningPort;
        activeServers = new ActiveServers();
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
        activeServers.start();
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
                activeServers.pingedServer(myPacket);
                answerToServer(Strings.SERVER_PING, myPacket);
            }
        }
    }

    private boolean registerServers(DatagramPacket serverPacket) throws IOException, ClassNotFoundException {
        return activeServers.registerServer(serverPacket);
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

//        for(DatagramPacket activeServersPacket : activeServersPackets){
//            activeServersPacket.setData(msgToSend.getBytes());
//            activeServersPacket.setLength(msgToSend.length());
//            mySocket.send(activeServersPacket);
//        }
    }
}
