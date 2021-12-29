package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    User sender;
    User receiver;
    String msgTxt;
    Timestamp time;
    Group group;
    File file;
    int id;

    /**
     * Constructor for messages sent between users when
     * querying them from database
     * 
     * @param sender   - user that sent the message
     * @param receiver - user that addresses the message
     * @param msgTxt   - message text
     * @param id       - message id
     */

    public Message(User sender, User receiver, String msgTxt, int id, Timestamp timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.msgTxt = new String(msgTxt);
        this.id = id;
        this.time = timestamp;
    }

    /**
     * Constructor for messages sent in groups
     * 
     * @param sender - user that sent the message
     * @param msgTxt - message text
     * @param group  - group that addresses the message
     */

    public Message(User sender, Group group, String msgTxt, int id, Timestamp timestamp) {
        this.sender = sender;
        this.msgTxt = new String(msgTxt);
        this.group = group;
        this.id = id;
        this.time = timestamp;
    }

    public Message(User sender, Group group, String msgTxt){
        this.sender = sender;
        this.msgTxt = new String(msgTxt);
        this.group = group;
    }

    /**
     * Constructor for messages sent between users when
     * creating them to be sent.
     * 
     * @param sender   - user that sent the message
     * @param receiver - user that addresses the message
     * @param msgTxt   - message text
     */

    public Message(User sender, User receiver, String msgTxt) {
        this.sender = sender;
        this.receiver = receiver;
        this.msgTxt = new String(msgTxt);
    }

    public Message(User sender, User receiver, String msgTxt, int id, Timestamp time, Group group, File file){
        this.sender = sender;
        this.receiver = receiver;
        this.id = id;
        this.msgTxt = msgTxt;
        this.time = time;
        this.group = group;
        this.file = file;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getMsgTxt() {
        return msgTxt;
    }

    public Group getGroup() {
        return group;
    }

    public int getID() {
        return id;
    }

    public Timestamp getTime() {
        return time;
    }

    public File getFile() { return file; }

    @Override
    public String toString() {
        return "(ID: " + id + ") User " + sender.getID() + " said: " + msgTxt + " to " + receiver.getID() + ".";
    }
}
