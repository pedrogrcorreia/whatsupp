package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class GetRequestFromServer extends Thread {
    Data model;
    Socket socket;
    ObjectInputStream oin;

    boolean isLogged = false;

    public GetRequestFromServer(Data model, Socket serverSocket) {
        this.model = model;
        this.socket = serverSocket;
    }

    public boolean requestedLogin() {
        // System.out.println("HERE");
        // try {
        // socket.setSoTimeout(2000);
        // } catch (SocketException e1) {
        // // TODO Auto-generated catch block
        // e1.printStackTrace();
        // }
        // try {
        // oin = new ObjectInputStream(socket.getInputStream());
        // } catch (IOException e) {
        // System.out.println("Couldn't start thread to receive requests from
        // Server:\r\n\t" + e);
        // return false;
        // }
        // try {
        // System.out.println("HERE");
        // SharedMessage receivedMessage = (SharedMessage) oin.readObject();
        // if (receivedMessage.getMsgType() == Strings.CLIENT_SUCCESS_LOGIN) {
        // return true;
        // } else {
        // return false;
        // }
        // } catch (ClassNotFoundException | IOException e) {
        // System.out.println("Error receiving the message:\r\n\t" + e);
        // return false;
        // }
        return isLogged;
    }

    @Override
    public void run() {
        try {
            oin = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Couldn't start thread to receive requests from Server:\r\n\t" + e);
            return;
        }

        try {
            SharedMessage receivedMessage = (SharedMessage) oin.readObject();
            System.out.println(receivedMessage.getMsgType());
            if (receivedMessage.getMsgType() == Strings.CLIENT_SUCCESS_LOGIN) {
                isLogged = true;
            }
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Error receiving the message:\r\n\t" + e);
        }
    }
}
