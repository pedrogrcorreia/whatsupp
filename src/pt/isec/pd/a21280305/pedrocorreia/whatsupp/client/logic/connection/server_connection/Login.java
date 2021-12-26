package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.User;

/**
 * Class used to request the login
 * from the user (client) that asks.
 */

public class Login extends ClientServerConnection {

    public Login(User user) {
        super(user);
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
                user = response.getClientServerConnection().getUser();
                return true;
            } else {
                return false;
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Couldn't establish connection with this socket:\r\n\t" +
                    e);
            return false;
        }
    }
}
