package pt.isec.pd.a21280305.pedrocorreia.whatsupp;

import java.io.Serial;
import java.io.Serializable;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.requests.ClientRequests;

public class SharedMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Strings msgType;
    private String msg;
    private ClientRequests cr;
    private int id;
    private String filePath;

    public SharedMessage(Strings msgType, String msg) {
        this.msgType = msgType;
        this.msg = new String(msg);
    }

    public SharedMessage(Strings msgType, String msg, ClientRequests cr) {
        this.msgType = msgType;
        this.msg = msg;
        this.cr = cr;
    }

    public SharedMessage(Strings msgType, ClientRequests cr) {
        this.msgType = msgType;
        this.cr = cr;
    }

    public SharedMessage(Strings msgType, String msg, int id){
        this.msgType = msgType;
        this.msg = msg;
        this.id = id;
    }

    public SharedMessage(Strings msgType, String msg, int id, String fPath){
        this.msgType = msgType;
        this.msg = msg;
        this.id = id;
        this.filePath = fPath;
    }


    public Strings getMsgType() {
        return msgType;
    }

    public String getMsg() {
        return msg;
    }

    public ClientRequests getClientRequest() {
        return cr;
    }

    public int getID(){ return id; }

    public String getFilePath(){ return filePath; }

    @Override
    public String toString() {
        return msgType.name() + " : " + msg;
    }
}
