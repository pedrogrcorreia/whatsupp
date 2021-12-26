package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.User;

/**
 * Class used to request the list of messages
 * from the user (client) that asks.
 */

public class Messages extends ClientServerConnection {
    private List<Message> messages;
    private User friend;
    private int userID = 0;
    private int groupID = 0;
    private Message msg;

    public Messages(User user, List<Message> messages) {
        super(user);
        this.messages = messages;
    }

    public boolean getAllMessages(ObjectOutputStream oout) {
        try {
            SharedMessage msgToSend = new SharedMessage(Strings.USER_REQUEST_MESSAGES, this);

            oout.writeObject(msgToSend);
            oout.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error receiving the message:\r\n\t" + e);
            return false;
        }
    }

    public boolean getMessagesFromUser(ObjectOutputStream oout, User userf) {
        this.friend = userf;

        try {
            SharedMessage msgToSend = new SharedMessage(Strings.USER_REQUEST_MESSAGES, this);

            oout.writeObject(msgToSend);
            oout.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error receiving the message:\r\n\t" + e);
            return false;
        }
    }

    public boolean getMessagesFromGroup(ObjectOutputStream oout, int groupID) {
        this.groupID = groupID;
        try {
            SharedMessage msgToSend = new SharedMessage(Strings.USER_REQUEST_MESSAGES, this);

            oout.writeObject(msgToSend);
            oout.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error receiving the message:\r\n\t" + e);
            return false;
        }
    }

    public boolean deleteMessage(ObjectOutputStream oout, Message msg) {
        this.msg = new Message(msg.getSender(), msg.getReceiver(), msg.getMsg(), msg.getID(), msg.getTime());
        try {
            SharedMessage msgToSend = new SharedMessage(Strings.MESSAGE_DELETE, this);

            oout.writeObject(msgToSend);
            oout.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error receiving the message:\r\n\t" + e);
            return false;
        }
    }

    public boolean sendMessage(ObjectOutputStream oout, Message msg) {
        this.msg = msg;
        try {
            SharedMessage msgToSend = new SharedMessage(Strings.USER_SENT_MESSAGE, this);

            oout.writeObject(msgToSend);
            oout.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error receiving the message:\r\n\t" + e);
            return false;
        }
    }

    public List<Message> getMessages() {
        return messages;
    }

    public int getUserID() {
        return userID;
    }

    public int getGroupID() {
        return groupID;
    }

    public User friend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public Message getMsg() {
        return msg;
    }

    public int getMsgID() {
        return this.msg.getID();
    }
}
