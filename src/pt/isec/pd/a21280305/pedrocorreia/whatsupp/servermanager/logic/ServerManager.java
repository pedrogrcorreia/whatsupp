package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic;

import java.io.*;
import java.net.*;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.DownloadFile;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data.ActiveServers;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data.ConnectedServer;

import javax.net.ssl.SSLHandshakeException;

public class ServerManager implements Runnable{

    static final int MAX_SIZE = 100000;

    ActiveServers activeServers;

    int listeningPort;

    MulticastSocket multiSocket = null;
    DatagramSocket mySocket = null;
    DatagramPacket myPacket;

    ByteArrayInputStream bin;
    ByteArrayInputStream mbin;
    ObjectInputStream oin;
    ObjectInputStream moin;

    ByteArrayOutputStream bout;
    ByteArrayOutputStream mbout;
    ObjectOutputStream oout;
    ObjectOutputStream moout;

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
            multiSocket = new MulticastSocket(3030);
            InetAddress group = InetAddress.getByName("230.30.30.30");
            multiSocket.joinGroup(group);
            Thread t = new Thread(this);
            t.start();
            mySocket = new DatagramSocket(listeningPort);
            System.out.println("Server Manager initialized at port " + mySocket.getLocalPort());
            runServerManager();
        } catch (SocketException e) {
            System.out.println("Error at UDP socket: \r\n\t" + e);
        } catch (IOException e) {
            System.out.println("Error at Multicast socket:\r\n\t" + e);
        }
    }

    /**
     * Main thread receiving UDP requests.
     */

    private void runServerManager() {
        System.out.println("Ready to receive requests.");
        while (true) {
            request = receiveRequests();
            System.out.println("Request: " + request.getMsgType().name());
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
                continue;
            }
            if (request.getMsgType().equals(Strings.SERVER_PING)) {
                String ports = request.getMsg();
                String[] split = ports.split(":");
                int tcpPort = Integer.parseInt(split[0]);
                int filesPort = Integer.parseInt(split[1]);
                activeServers.pingedServer(myPacket, tcpPort, filesPort);
                answer = new SharedMessage(Strings.SERVER_PING, "Ping registered.");
                answerToRequest(answer, myPacket);
                continue;
            }
            if (request.getMsgType().equals(Strings.CLIENT_REQUEST_SERVER)) {
                String serverCoord = activeServers.registerClient();
                if(serverCoord != null) {
                    answer = new SharedMessage(Strings.CLIENT_REQUEST_SERVER,
                            serverCoord);
                    answerToRequest(answer, myPacket);
                }
                continue;
            }
            if(request.getMsgType().equals(Strings.NEW_FILE_SENT_USER)){
                answer = new SharedMessage(Strings.NEW_FILE_SENT_USER, myPacket.getAddress().getHostAddress(),
                        activeServers.getPortForTcp(myPacket), request.getFilePath());
                answerException(answer);
                continue;
            }
            if(request.getMsgType().equals(Strings.NEW_FILE_SENT_GROUP)){
                answer = new SharedMessage(Strings.NEW_FILE_SENT_GROUP, myPacket.getAddress().getHostAddress(),
                        activeServers.getPortForTcp(myPacket), request.getFilePath());
                answerException(answer);
                continue;
            }
            else {
                System.out.println("\n\t here \n\t");
                answerToAll(request);
            }
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

            return newRequest;
        } catch (IOException e) {
            System.out.println("[ServerManager] Error receiving request: \r\n\t" + e);
        } catch (ClassNotFoundException e) {
            System.out.println("[ServerManager] Object sent is invalid: \r\n\t" + e);
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
            oout.writeObject(msgToSend);

            oout.flush();

            serverPacket.setData(bout.toByteArray());
            serverPacket.setLength(bout.size());
            mySocket.send(serverPacket);
        } catch (IOException e) {
            System.out.println("[ServerManager] Error writing object: \r\n\t" + e);
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
                System.out.println("[ServerManager] Error writing object:\r\n\t" + e);
            }
        }
    }

    private void answerException(SharedMessage msgToSend){
        List<ConnectedServer> cs = activeServers.getServers();

        for (int i = 0; i < cs.size(); i++) {
            try {
                if(cs.get(i).getServerPacket().getPort() == myPacket.getPort()){
                    continue;
                }
                bout = new ByteArrayOutputStream();
                oout = new ObjectOutputStream(bout);
                oout.writeUnshared(msgToSend);
                DatagramPacket serverPacket;
                serverPacket = cs.get(i).getServerPacket();
                serverPacket.setData(bout.toByteArray());
                serverPacket.setLength(bout.size());
                mySocket.send(serverPacket);
            } catch (IOException e) {
                System.out.println("[ServerManager] Error writing object:\r\n\t" + e);
            }
        }
    }

    @Override
    public void run() {
        DatagramPacket myPacket;
        System.out.println("Multicast thread started at port 3030");
        while(true) {
            try {
                SharedMessage newRequest;
                myPacket = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                multiSocket.receive(myPacket);

                mbin = new ByteArrayInputStream(myPacket.getData(), 0, myPacket.getLength());
                moin = new ObjectInputStream(mbin);

                newRequest = (SharedMessage) moin.readObject();
                System.out.println("[ServerManager] Request: " + newRequest.getMsgType().name() + " from " + myPacket.getPort());
                if (newRequest.getMsgType().equals(Strings.SERVER_REGISTER_REQUEST)) {
                    try {
                        if (!registerServers(myPacket)) {
                            answer = new SharedMessage(Strings.SERVER_REGISTER_SUCCESS,
                                    new String("Registered successfully."));
                        } else {
                            answer = new SharedMessage(Strings.SERVER_REGISTER_FAIL,
                                    new String("Couldn't register the server."));
                        }
                        mbout = new ByteArrayOutputStream();
                        moout = new ObjectOutputStream(mbout);
                        moout.writeUnshared(answer);

                        myPacket.setData(mbout.toByteArray());
                        myPacket.setLength(mbout.size());
                        multiSocket.send(myPacket);
                        continue;
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("[ServerManager] Error registering the server.");
                    }
                }
                if (newRequest.getMsgType().equals(Strings.SERVER_PING)) {
                    String ports = newRequest.getMsg();
                    String[] split = ports.split(":");
                    int tcpPort = Integer.parseInt(split[0]);
                    int filesPort = Integer.parseInt(split[1]);
                    activeServers.pingedServer(myPacket, tcpPort, filesPort);
                    answer = new SharedMessage(Strings.SERVER_PING, "Ping registered.");
                    mbout = new ByteArrayOutputStream();
                    moout = new ObjectOutputStream(mbout);
                    moout.writeUnshared(answer);

                    myPacket.setData(mbout.toByteArray());
                    myPacket.setLength(mbout.size());
                    multiSocket.send(myPacket);
                    continue;
                }
                if (request.getMsgType().equals(Strings.CLIENT_REQUEST_SERVER)) {
                    String serverCoord = activeServers.registerClient();
                    if(serverCoord != null) {
                        answer = new SharedMessage(Strings.CLIENT_REQUEST_SERVER,
                                serverCoord);

                        mbout = new ByteArrayOutputStream();
                        moout = new ObjectOutputStream(mbout);
                        moout.writeUnshared(answer);
                        myPacket.setData(mbout.toByteArray());
                        myPacket.setLength(mbout.size());
                        multiSocket.send(myPacket);
                        continue;
                    }
                    continue;
                }
                if(request.getMsgType().equals(Strings.NEW_FILE_SENT_USER)){
                    answer = new SharedMessage(Strings.NEW_FILE_SENT_USER, myPacket.getAddress().getHostAddress(),
                            activeServers.getPortForTcp(myPacket), request.getFilePath());
                    mbout = new ByteArrayOutputStream();
                    moout = new ObjectOutputStream(mbout);
                    moout.writeUnshared(answer);
                    myPacket.setData(mbout.toByteArray());
                    myPacket.setLength(mbout.size());
                    multiSocket.send(myPacket);
                    continue;
                }
                if(request.getMsgType().equals(Strings.NEW_FILE_SENT_GROUP)){
                    answer = new SharedMessage(Strings.NEW_FILE_SENT_GROUP, myPacket.getAddress().getHostAddress(),
                            activeServers.getPortForTcp(myPacket), request.getFilePath());
                    mbout = new ByteArrayOutputStream();
                    moout = new ObjectOutputStream(mbout);
                    moout.writeUnshared(answer);
                    myPacket.setData(mbout.toByteArray());
                    myPacket.setLength(mbout.size());
                    multiSocket.send(myPacket);
                    continue;
                }
                List<ConnectedServer> cs = activeServers.getServers();
                for (int i = 0; i < cs.size(); i++) {
                    try {
                        bout = new ByteArrayOutputStream();
                        oout = new ObjectOutputStream(bout);
                        oout.writeUnshared(newRequest);
                        DatagramPacket serverPacket;
                        serverPacket = cs.get(i).getServerPacket();
                        serverPacket.setData(bout.toByteArray());
                        serverPacket.setLength(bout.size());
                        mySocket.send(serverPacket);
                    } catch (IOException e) {
                        System.out.println("Error writing object:\r\n\t" + e);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error receiving request: \r\n\t" + e);
            } catch (ClassNotFoundException e) {
                System.out.println("Object sent is invalid: \r\n\t" + e);
            }
        }
    }

}
