package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;

/**
 * Class used to request all info
 * from the user (client) such as friends,
 * messages and groups.
 */

public class ClientRequestInfo extends ClientServerConnection {
    private String username;
    private List<String> friends;
    private List<String> messages;
    private List<String> groups;

    public ClientRequestInfo(String username, List<String> friends, List<String> messages, List<String> groups) {
        super();
        this.username = username;
        this.friends = friends;
        this.messages = messages;
        this.groups = groups;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /** Loads the friends, messages and groups when the user logs in */
    public boolean retrieveInfoFromServer(ObjectInputStream oin, ObjectOutputStream oout, List<SharedMessage> list) {
        // try {
        // SharedMessage msgToSend = new SharedMessage(Strings.USER_REQUEST_INFO, this);
        // oout.writeObject(msgToSend);
        // oout.flush();

        // SharedMessage response = (SharedMessage) oin.readObject();
        // // synchronized (list) {
        // // list.add(response);
        // // list.notifyAll();
        // // }
        // if (response.getMsgType() == Strings.USER_REQUEST_INFO_SUCCESS) {
        // friends = ((ClientRequestInfo)
        // response.getClientServerConnection()).getFriends();
        // for (String friend : friends) {
        // System.out.println(friend);
        // }
        // return true;
        // } else {
        // return false;
        // }
        // } catch (IOException | ClassNotFoundException e) {
        // System.out.println("Couldn't establish connection with this socket:\r\n\t" +
        // e);
        // return false;
        // }
        try {
            SharedMessage msgToSend = new SharedMessage(Strings.USER_REQUEST_INFO, this);
            oout.writeObject(msgToSend);
            oout.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Couldn't establish connection with this socket:\r\n\t" + e);
            return false;
        }
    }

    public List<String> getFriends() {
        return friends;
    }

    public void addFriend(String friend) {
        friends.add(friend);
        System.out.println("Friend added.");
    }
}
