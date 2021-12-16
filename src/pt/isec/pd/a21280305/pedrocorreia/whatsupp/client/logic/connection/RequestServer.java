package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientRequestLogin;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientServerConnection;

public class RequestServer {
    Socket socket;
    ObjectInputStream oin;
    ObjectOutput oout;

    public RequestServer(Socket socketToServer) {
        this.socket = socketToServer;
    }

    public boolean sendLogin(String username, String password) {
        try {
            oin = new ObjectInputStream(socket.getInputStream());
            oout = new ObjectOutputStream(socket.getOutputStream());
            // oout.writeObject(new SharedMessage(Strings.CLIENT_REQUEST_LOGIN, new
            // String(username + ", " + password)));
            ClientServerConnection csc = new ClientRequestLogin(username, password);
            oout.writeObject(csc);
            oout.flush();

            SharedMessage response = (SharedMessage) oin.readObject();

            if (response.getMsgType() == Strings.CLIENT_SUCCESS_LOGIN) {
                return true;
            } else {
                return false;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Couldn't establish connection with this socket:\r\n\t" + e);
            return false;
        }
    }

    public boolean sendRegister(String username, String password, String confPassword, String fname, String lname) {
        try {
            oin = new ObjectInputStream(socket.getInputStream());
            oout = new ObjectOutputStream(socket.getOutputStream());
            oout.writeObject(new SharedMessage(Strings.CLIENT_REQUEST_LOGIN,
                    new String(username + ", " + password + ", " + confPassword + ", " + fname + ", " + lname)));
            oout.flush();

            SharedMessage response = (SharedMessage) oin.readObject();

            if (response.getMsgType() == Strings.CLIENT_SUCCESS_LOGIN) {
                return true;
            } else {
                return false;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Couldn't establish connection with this socket:\r\n\t" + e);
            return false;
        }
    }
}
