package pt.isec.pd.a21280305.pedrocorreia.whatsupp;

import java.io.Serial;
import java.io.Serializable;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientServerConnection;

public class SharedMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Strings msgType;
    private String msg;
    private ClientServerConnection csc;

    public SharedMessage(Strings msgType, String msg) {
        this.msgType = msgType;
        this.msg = new String(msg);
    }

    public SharedMessage(Strings msgType, String msg, ClientServerConnection csc) {
        this.msgType = msgType;
        this.msg = new String(msg);
        this.csc = csc;
    }

    public SharedMessage(Strings msgType, ClientServerConnection csc) {
        this.msgType = msgType;
        this.csc = csc;
    }

    public Strings getMsgType() {
        return msgType;
    }

    public String getMsg() {
        return msg;
    }

    public ClientServerConnection getClientServerConnection() {
        return csc;
    }

    @Override
    public String toString() {
        return msgType.name() + ": " + msg;
    }
}
