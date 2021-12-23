package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.User;

/**
 * Class used to request the list of groups
 * from the user (client) that asks.
 */

public class ClientRequestGroups extends ClientServerConnection {
    private List<String> groups;

    public ClientRequestGroups(User user, List<String> groups) {
        super(user);
        this.groups = groups;
    }

    public boolean getGroups(ObjectOutputStream oout) {
        try {
            SharedMessage msgToSend = new SharedMessage(Strings.USER_REQUEST_GROUPS, this);

            oout.writeObject(msgToSend);
            oout.flush();

            return true;
        } catch (IOException e) {
            System.out.println("Error receiving the message:\r\n\t" + e);
            return false;
        }
    }

    public List<String> getGroups() {
        return groups;
    }
}
