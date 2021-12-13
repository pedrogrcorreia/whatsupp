package pt.isec.pd.a21280305.pedrocorreia.whatsupp;

import java.io.Serial;
import java.io.Serializable;

public class SharedMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Strings msgType;
    private String msg;

    public SharedMessage(Strings msgType, String msg) {
        this.msgType = msgType;
        this.msg = new String(msg);
    }

    public Strings getMsgType() {
        return msgType;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return msgType.name() + ": " + msg;
    }
}
