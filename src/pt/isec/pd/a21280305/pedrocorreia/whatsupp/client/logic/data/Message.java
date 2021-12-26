package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    User sender;
    User receiver;
    String msg;
    Timestamp time;
    int group;
    int id;

    /**
     * Constructor for messages sent between users when
     * querying them from database
     * 
     * @param sender   - user that sent the message
     * @param receiver - user that addresses the message
     * @param msg      - message text
     * @param id       - message id
     */

    public Message(User sender, User receiver, String msg, int id, Timestamp timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = new String(msg);
        this.id = id;
        this.time = timestamp;
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

    /**
     * Constructor for messages sent between users when
     * creating them to be sent.
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

    public int getID() {
        return id;
    }

    public Timestamp getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "(ID: " + id + ") User " + sender.getID() + " said: " + msg + " to " + receiver.getID() + ".";
    }
}
