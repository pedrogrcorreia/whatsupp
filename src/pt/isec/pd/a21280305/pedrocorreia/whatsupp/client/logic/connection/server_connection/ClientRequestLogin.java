package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;

/**
 * Class used to request the login
 * from the user (client) that asks.
 */

public class ClientRequestLogin extends ClientServerConnection {
    private String username;
    private String password;

    public ClientRequestLogin(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public boolean login(ObjectInputStream oin, ObjectOutputStream oout, List<SharedMessage> list) {
        try {
            SharedMessage msgToSend = new SharedMessage(Strings.USER_REQUEST_LOGIN,
                    Strings.USER_REQUEST_LOGIN.toString(), this);
            oout.writeObject(msgToSend);
            oout.flush();

            SharedMessage response = (SharedMessage) oin.readObject();
            synchronized (list) {
                list.add(response);
                list.notifyAll();
            }
            if (response.getMsgType() == Strings.USER_SUCCESS_LOGIN) {
                // System.out.println(response.getMsg());
                return true;
            } else {
                return false;
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Couldn't establish connection with this socket:\r\n\t" +
                    e);
            return false;
        }
        // DEBUG
        // try {
        // oout.writeObject(this);
        // oout.flush();
        // return true;
        // } catch (IOException e) {
        // e.printStackTrace();
        // return false;
        // }
    }
}
