package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.requests;

import java.io.IOException;
import java.io.ObjectOutputStream;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

/**
 * Class used to request the user info
 * from the user (client) that asks.
 */

public class SearchUser extends ClientRequests {

    public SearchUser(User user, User selectedUser) {
        super(user, selectedUser);
    }

    public boolean getUser(ObjectOutputStream oout) {
        try {
            SharedMessage msgToSend = new SharedMessage(Strings.USER_REQUEST_USER, this);

            oout.writeObject(msgToSend);
            oout.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error receiving the message:\r\n\t" + e);
            return false;
        }
    }
}
