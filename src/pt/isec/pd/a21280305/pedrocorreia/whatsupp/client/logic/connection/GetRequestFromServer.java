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

    private boolean isLogged = false;
    private boolean messageSent = false;

    public GetRequestFromServer(Data model, Socket serverSocket) {
        this.model = model;
        this.socket = serverSocket;
    }

    public boolean requestedLogin() {
        return isLogged;
    }

    public boolean sentMessage() {
        return messageSent;
    }

    public String debug() {
        try {
            oin = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Couldn't start thread to receive requests from" +
                    "Server:\r\n\t" + e);
            return "ERRO";
        }

        try {
            SharedMessage receivedMessage = (SharedMessage) oin.readObject();
            System.out.println(receivedMessage.getMsgType());
            switch (receivedMessage.getMsgType()) {
                case CLIENT_FAILED_LOGIN:
                    isLogged = false;
                    break;
                case CLIENT_REQUEST_LOGIN:
                    break;
                case CLIENT_REQUEST_SERVER:
                    break;
                case CLIENT_SENT_MESSAGE:
                    messageSent = true;
                    break;
                case CLIENT_SUCCESS_LOGIN:
                    isLogged = true;
                    break;
                default:
                    break;
            }
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Error receiving the message:\r\n\t" + e);
            return "ERROR";
        }
        return "COCO";
    }

    @Override
    public void run() {

        // try {
        // oin = new ObjectInputStream(socket.getInputStream());
        // } catch (IOException e) {
        // System.out.println("Couldn't start thread to receive requests from
        // Server:\r\n\t" + e);
        // return;
        // }

        // try {
        // SharedMessage receivedMessage = (SharedMessage) oin.readObject();
        // System.out.println(receivedMessage.getMsgType());
        // switch (receivedMessage.getMsgType()) {
        // case CLIENT_FAILED_LOGIN:
        // isLogged = false;
        // break;
        // case CLIENT_REQUEST_LOGIN:
        // break;
        // case CLIENT_REQUEST_SERVER:
        // break;
        // case CLIENT_SENT_MESSAGE:
        // messageSent = true;
        // break;
        // case CLIENT_SUCCESS_LOGIN:
        // isLogged = true;
        // break;
        // default:
        // break;

        // }
        // if (receivedMessage.getMsgType() == Strings.CLIENT_SUCCESS_LOGIN) {
        // isLogged = true;
        // }

        // } catch (ClassNotFoundException | IOException e) {
        // System.out.println("Error receiving the message:\r\n\t" + e);
        // }
    }
}
