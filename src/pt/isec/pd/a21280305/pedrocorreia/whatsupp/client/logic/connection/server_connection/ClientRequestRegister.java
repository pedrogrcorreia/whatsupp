package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;

public class ClientRequestRegister extends ClientServerConnection {
    private String username;
    private String password;
    private String confPassword;
    private String fname;
    private String lname;

    public ClientRequestRegister(String username, String password, String confPassword, String fname, String lname) {
        super();
        this.username = username;
        this.password = password;
        this.confPassword = confPassword;
        this.fname = fname;
        this.lname = lname;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getConfPassword() {
        return confPassword;
    }

    @Override
    public String getFName() {
        return fname;
    }

    @Override
    public String getLName() {
        return lname;
    }

    public boolean register(ObjectInputStream oin, ObjectOutputStream oout){
        try {
            oout.writeObject(this);
            oout.flush();

            SharedMessage response = (SharedMessage) oin.readObject();

            if (response.getMsgType() == Strings.USER_REGISTER_SUCCESS) {
                System.out.println(response.getMsg());
                return true;
            } else {
                System.out.println(response.getMsg());
                return false;
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Couldn't establish connection with this socket:\r\n\t" + e);
            return false;
        }
    }
}
