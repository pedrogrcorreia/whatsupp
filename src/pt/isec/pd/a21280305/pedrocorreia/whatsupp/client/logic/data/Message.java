package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data;

public class Message {
    User sender;
    User receiver;
    String msg;
    int group;

    public Message(User sender, User receiver, String msg, int group) {
        this.sender = sender;
        this.receiver = receiver;
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
}
