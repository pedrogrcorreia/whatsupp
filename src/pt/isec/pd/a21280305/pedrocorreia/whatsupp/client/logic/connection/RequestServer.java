package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientRequestLogin;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientRequestRegister;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientServerConnection;

public class RequestServer {
    Socket socket;
    ObjectInputStream oin;
    ObjectOutput oout;

    public RequestServer(Socket socketToServer) {
        this.socket = socketToServer;
    }

    public RequestServer(Socket socketToServer, ObjectInputStream oin, ObjectOutputStream oout) {
        this.socket = socketToServer;
        this.oin = oin;
        this.oout = oout;
    }

    public boolean sendLogin(String username, String password) {
        try {

            ClientServerConnection csc = new ClientRequestLogin(username, password);
            oout.writeObject(csc);
            oout.flush();

            SharedMessage response = (SharedMessage) oin.readObject();

            if (response.getMsgType() == Strings.CLIENT_SUCCESS_LOGIN) {
                System.out.println(response.getMsg());
                return true;
            } else {
                System.out.println(response.getMsg());
                return false;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Couldn't establish connection with this socket:\r\n\t" + e);
            return false;
        }
    }

    public boolean sendRegister(String username, String password, String confPassword, String fname, String lname) {
        try {

            ClientServerConnection csc = new ClientRequestRegister(username, password, confPassword, fname, lname);
            oout.writeObject(csc);
            oout.flush();

            SharedMessage response = (SharedMessage) oin.readObject();

            if (response.getMsgType() == Strings.USER_REGISTER_SUCCESS) {
                System.out.println(response.getMsg());
                return true;
            } else {
                System.out.println(response.getMsg());
                return false;
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Couldn't establish connection with this socket:\r\n\t" + e);
            return false;
        }
    }
}
