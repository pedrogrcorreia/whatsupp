package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection;

import java.io.Serializable;

/**
 * Abstract class that is the adapter of an
 * interface. This allow to receiver objects from this class
 * on the server, adapting for the class object
 * after getting the name of the class.
 */

public abstract class ClientServerConnection implements IClientServerConnection, Serializable {
    public ClientServerConnection() {
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getConfPassword() {
        return null;
    }

    @Override
    public String getFName() {
        return null;
    }

    @Override
    public String getLName() {
        return null;
    }
}
