package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables;

import java.io.Serial;
import java.io.Serializable;

public class File implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    Message message;
    String path;
    int id;

    public File(Message message, String path, int id) {
        this.message = message;
        this.path = path;
        this.id = id;
    }

    public File(Message message, String path){
        this.message = message;
        this.path = path;
    }

    public File(String path, int message_id){
        this.path = path;
        this.id = message_id;
    }

    public File(String path){
        this.path = path;
    }

    public Message getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public int id() {
        return id;
    }
}
