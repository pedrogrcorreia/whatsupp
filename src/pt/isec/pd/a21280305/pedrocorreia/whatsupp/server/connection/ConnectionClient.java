package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import java.io.*;
import java.net.*;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

public class ConnectionClient extends Thread {
    private Socket clientSocket;
    private ObjectOutputStream oout;
    private ObjectInputStream oin;

    public ConnectionClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client connecteced from: " + clientSocket.getInetAddress().getHostAddress() + ":"
                    + clientSocket.getPort());

            oout = new ObjectOutputStream(clientSocket.getOutputStream());
            oin = new ObjectInputStream(clientSocket.getInputStream());

            SharedMessage request = (SharedMessage) oin.readObject();

            System.out.println(request);

            oout.writeObject(new SharedMessage(Strings.CLIENT_SENT_MESSAGE, request.getMsg()));
            oout.flush();
        } catch (IOException e) {
            System.out.println("Problem communicating with client: \r\n\t" + e);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing client socket.");
            }
        }
    }
}
