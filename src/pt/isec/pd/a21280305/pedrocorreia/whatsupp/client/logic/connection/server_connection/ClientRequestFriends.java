package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;

/**
 * Class used to request the list of friends
 * from the user (client) that asks.
 */

public class ClientRequestFriends extends ClientServerConnection {
    private String username;
    private List<String> friends;

    public ClientRequestFriends(String username, List<String> friends) {
        super();
        this.username = username;
        this.friends = friends;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public boolean getFriends(ObjectInputStream oin, ObjectOutputStream oout) {
        try {
            SharedMessage msgToSend = new SharedMessage(Strings.USER_REQUEST_FRIENDS, this);

            oout.writeObject(msgToSend);
            oout.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error receiving the message:\r\n\t" + e);
            return false;
        }
    }
}
