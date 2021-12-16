package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection;

import java.io.Serializable;

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
}
