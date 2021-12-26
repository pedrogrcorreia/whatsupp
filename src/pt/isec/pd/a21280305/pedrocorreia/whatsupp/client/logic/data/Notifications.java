package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.FriendsList;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.Messages;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.SearchUser;

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
                System.out.println("Response type: " + response.getMsgType().name());
                switch (response.getMsgType()) {
                    case USER_REQUEST_OWN_INFO_SUCCESS -> {
                        Data.user = ((SearchUser) response.getClientServerConnection()).getUser();
                        notifyList(response);
                    }
                    case USER_REQUEST_FRIENDS_SUCCESS -> {
                        Data.friends = ((FriendsList) response.getClientServerConnection()).getFriends();
                        notifyList(response);
                    }
                    case USER_REQUEST_FRIENDS_FAIL -> {
                        notifyList(response);
                    }
                    case USER_REQUEST_FRIENDS_REQUESTS_SUCCESS -> {
                        Data.friends = ((FriendsList) response.getClientServerConnection()).getFriends();
                        notifyList(response);
                    }
                    case USER_REQUEST_USER_SUCCESS -> {
                        Data.searched = ((SearchUser) response.getClientServerConnection()).getUser();
                        notifyList(response);
                    }
                    case USER_REQUEST_USER_FAIL -> {
                        notifyList(response);
                    }
                    case USER_SEND_FRIEND_REQUEST_SUCCESS -> {
                        notifyList(response);
                    }
                    case USER_SEND_FRIEND_REQUEST_FAIL -> {
                        notifyList(response);
                    }
                    case USER_REQUEST_MESSAGES_SUCCESS -> {
                        Data.messages = ((Messages) response.getClientServerConnection()).getMessages();
                        notifyList(response);
                    }
                    case MESSAGE_DELETE_SUCCESS -> {
                        notifyList(response);
                    }
                    case MESSAGE_DELETE_FAIL -> {
                        notifyList(response);
                    }
                    case MESSAGE_SENT_SUCCESS -> {
                        notifyList(response);
                    }
                    case MESSAGE_SENT_FAIL -> {
                        notifyList(response);
                    }
                    case NEW_MESSAGE -> {
                        notifyList(response);
                    }
                    default -> {
                    }
                }
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
