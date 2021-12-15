package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;

public class Data {
    private static final int MAX_SIZE = 4096;

    private User user;
    private String serverManagerAddress;
    private int serverManagerPort;

    private String serverAddress;
    private int serverPort;

    private Socket socketToServer;

    public Data(String serverManagerAddress, int serverManagerPort) {
        this.serverManagerAddress = serverManagerAddress;
        this.serverManagerPort = serverManagerPort;
    }

    public boolean contactServerManager() {
        ByteArrayOutputStream bout;
        ObjectOutputStream oout;
        ByteArrayInputStream bin;
        ObjectInputStream oin;
        DatagramSocket socket;
        DatagramPacket packet;
        InetAddress serverManager;

        try {
            System.out.println("HERE");

            serverManager = InetAddress.getByName(serverManagerAddress);

            socket = new DatagramSocket();
            socket.setSoTimeout(3000);
            bout = new ByteArrayOutputStream();
            oout = new ObjectOutputStream(bout);

            oout.writeUnshared(
                    new SharedMessage(Strings.CLIENT_REQUEST_SERVER, "Client wants a connection to a server."));

            packet = new DatagramPacket(bout.toByteArray(), bout.size(), serverManager, serverManagerPort);
            socket.send(packet);

            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            socket.receive(packet);

            bin = new ByteArrayInputStream(packet.getData());
            oin = new ObjectInputStream(bin);

            SharedMessage responseFromServerManager = (SharedMessage) oin.readObject();

            System.out.println("Response from Server: " + responseFromServerManager.getMsg());
            Scanner sc = new Scanner(new String(responseFromServerManager.getMsg()));
            sc.useDelimiter(":");
            // if(sc.hasNext()){
            String serverAddressReceived = sc.next();
            serverAddress = serverAddressReceived.replace("/", "");
            serverPort = Integer.parseInt(sc.next());
            // }
            System.out.println("SMA " + serverAddress);
            System.out.println("SP " + serverPort);
            socketToServer = new Socket(serverAddress, serverPort);

            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error connecting the Server Manager." + e);
            return false;
        }
    }

    // TODO("registo e login funcoes booleanas que retornam para a maquina de
    // estados")

    public boolean login(String username, String password) {
        // TODO(QUERY SQL)
        return true;
    }

    public boolean register(String username, String password, String fname, String lname) {
        // TODO(QUERY SQL)
        return true;
    }
}
