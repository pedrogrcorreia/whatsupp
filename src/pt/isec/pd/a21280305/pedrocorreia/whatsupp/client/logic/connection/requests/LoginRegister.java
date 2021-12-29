package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.requests;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.FriendsRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Group;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.GroupRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

public class LoginRegister extends ClientRequests {

    public LoginRegister(User user) {
        super(user);
    }

    @Override
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
                user = response.getClientRequest().getUser();
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

    @Override
    public boolean register(ObjectInputStream oin, ObjectOutputStream oout, List<SharedMessage> list) {
        try {
            SharedMessage msgToSend = new SharedMessage(Strings.USER_REQUEST_REGISTER,
                    Strings.USER_REQUEST_REGISTER.toString(), this);
            oout.writeObject(msgToSend);
            oout.flush();

            SharedMessage response = (SharedMessage) oin.readObject();

            synchronized (list) {
                list.add(response);
                list.notifyAll();
            }

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
