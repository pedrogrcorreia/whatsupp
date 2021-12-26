package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables;

import java.io.Serial;
import java.io.Serializable;

public class Group implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    User admin;
    String name;
    int id;

    public Group(User admin, String name, int id) {
        this.admin = admin;
        this.name = name;
        this.id = id;
    }

    public User getAdmin() {
        return admin;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }
}
