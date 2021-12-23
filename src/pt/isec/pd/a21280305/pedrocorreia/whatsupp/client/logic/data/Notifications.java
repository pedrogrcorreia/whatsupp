package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientRequestFriends;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientRequestUser;

public class Notifications extends Data implements Runnable {

    ObjectInputStream oin;
    ObjectOutputStream oout;
    List<SharedMessage> notLog;
    List<String> friends;

    public Notifications(ObjectInputStream oin, ObjectOutputStream oout, List<SharedMessage> notLog) {
        super();
        this.oin = oin;
        this.oout = oout;
        this.notLog = notLog;
    }

    private void notifyList(SharedMessage response) {
        synchronized (notLog) {
            notLog.add(response);
            notLog.notify();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                SharedMessage response = (SharedMessage) oin.readObject();
                switch (response.getMsgType()) {
                    case USER_REQUEST_OWN_INFO_SUCCESS -> {
                        Data.user = ((ClientRequestUser) response.getClientServerConnection()).getUser();
                        notifyList(response);
                    }
                    case USER_REQUEST_FRIENDS_SUCCESS -> {
                        Data.friends = ((ClientRequestFriends) response.getClientServerConnection()).getFriends();
                    }
                    default -> {
                    }
                }
                // if (response.getMsgType() == Strings.USER_REQUEST_INFO_SUCCESS) {
                // Data.friends = ((ClientRequestInfo)
                // response.getClientServerConnection()).getFriends();
                // } else if (response.getMsgType() == Strings.USER_REQUEST_OWN_INFO_SUCCESS) {
                // Data.user = ((ClientRequestUser)
                // response.getClientServerConnection()).getUser();
                // } else if (response.getMsgType() == Strings.USER_REQUEST_MESSAGES_SUCCESS) {
                // Data.messages = null;
                // } else {
                // synchronized (notLog) {
                // notLog.add(response);
                // notLog.notify();
                // }
                // }
            } catch (ClassNotFoundException e) {
                System.out.println("Couldn't load the message:\r\n\t" + e);
            } catch (IOException e) {
                System.out.println("IOException:\r\n\t" + e);
                e.printStackTrace();
                return;
            }
        }
    }

}
