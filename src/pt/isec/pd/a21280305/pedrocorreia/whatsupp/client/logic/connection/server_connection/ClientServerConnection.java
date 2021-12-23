package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection;

import java.io.Serial;
import java.io.Serializable;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.User;

/**
 * Abstract class that is the adapter of an
 * interface. This allow to receiver objects from this class
 * on the server, adapting for the class object
 * after getting the name of the class.
 */

public abstract class ClientServerConnection implements IClientServerConnection, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected User user;

    public ClientServerConnection() {
    }

    public ClientServerConnection(User user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getConfPassword() {
        return user.getConfPassword();
    }

    @Override
    public String getFName() {
        return user.getFName();
    }

    @Override
    public String getLName() {
        return user.getLName();
    }

    @Override
    public User getUser() {
        return user;
    }
}
