package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.requests;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.FriendsRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

public class Friends extends ClientRequests {

    public Friends(User user) {
        super(user);
    }

    public Friends(User user, List<FriendsRequests> friends) {
        super(user, friends);
    }

    public Friends(User user, User selectedUser) {
        super(user, selectedUser);
    }

    @Override
    public boolean getFriends(ObjectOutputStream oout) {
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

    @Override
    public boolean getFriendsRequests(ObjectOutputStream oout) {
        try {
            SharedMessage msgToSend = new SharedMessage(Strings.USER_REQUEST_FRIENDS_REQUESTS, this);

            oout.writeObject(msgToSend);
            oout.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error receiving the message:\r\n\t" + e);
            return false;
        }
    }

    @Override
    public boolean addFriend(ObjectOutputStream oout) {
        try {
            SharedMessage msgToSend = new SharedMessage(Strings.USER_SEND_FRIEND_REQUEST, this);

            oout.writeObject(msgToSend);
            oout.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error receiving the message:\r\n\t" + e);
            return false;
        }
    }

    public boolean getFriendsRequestsPending(ObjectOutputStream oout) {
        try {
            SharedMessage msgToSend = new SharedMessage(Strings.USER_REQUEST_FRIENDS_REQUESTS_PENDING, this);

            oout.writeObject(msgToSend);
            oout.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error receiving the message:\r\n\t" + e);
            return false;
        }
    }
}
