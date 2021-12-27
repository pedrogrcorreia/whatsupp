package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.requests;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

public class Messages extends ClientRequests {

    public Messages(User user, User selectedUser, List<Message> messages) {
        super(user, selectedUser, messages);
    }

    public Messages(User user, User selectedUser, Message message) {
        super(user, selectedUser, message);
    }

    public Messages(User user, Message message) {
        super(user, message);
    }

    @Override
    public boolean getMessagesFromUser(ObjectOutputStream oout) {
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

    @Override
    public boolean getMessagesFromGroup(ObjectOutputStream oout) {
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

    @Override
    public boolean sendMessageToUser(ObjectOutputStream oout) {
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

    @Override
    public boolean deleteMessage(ObjectOutputStream oout) {
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
}
