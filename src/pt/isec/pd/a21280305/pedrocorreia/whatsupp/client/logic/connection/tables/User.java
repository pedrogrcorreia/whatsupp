package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int id;
    private String username;
    private String password;
    private String confPassword;
    private String lName;
    private String fName;
    private String name;
    private int status;

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String name, int id) {
        this.username = username;
        this.name = name;
        this.id = id;
    }

    public User(String username, String name, int id, int status) {
        this.username = username;
        this.name = name;
        this.id = id;
        this.status = status;
    }

    public User(String username, String password, String name, int id) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.id = id;
    }

    public User(String username, String password, String confPassword, String fName, String lName) {
        this.username = username;
        this.password = password;
        this.confPassword = confPassword;
        this.fName = fName;
        this.lName = lName;
    }

    public User(String username, String password, String confPassword, String name, int user_id){
        this.username = username;
        this.password = password;
        this.confPassword = confPassword;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfPassword() {
        return confPassword;
    }

    public String getFName() {
        return fName;
    }

    public String getLName() {
        return lName;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User id: " + id);
        sb.append("\nUsername: " + username);
        sb.append("\nName: " + name);
        sb.append("\nStatus " + status);
        return sb.toString();
    }

}
