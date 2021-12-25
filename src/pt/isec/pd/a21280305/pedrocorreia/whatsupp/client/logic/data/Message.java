package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data;

import java.io.Serial;
import java.io.Serializable;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    User sender;
    User receiver;
    String msg;
    int group;

    /**
     * Constructor for messages sent between users
     * 
     * @param sender   - user that sent the message
     * @param receiver - user that addresses the message
     * @param msg      - message text
     */
    public Message(User sender, User receiver, String msg) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = new String(msg);
    }

    /**
     * Constructor for messages sent in groups
     * 
     * @param sender - user that sent the message
     * @param msg    - message text
     * @param group  - group that addresses the message
     */

    public Message(User sender, String msg, int group) {
        this.sender = sender;
        this.msg = new String(msg);
        this.group = group;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getMsg() {
        return msg;
    }

    public int getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return "User " + sender.getID() + " said: " + msg + " to " + receiver.getID() + ".";
    }
}
