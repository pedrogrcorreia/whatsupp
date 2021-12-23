package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.User;

/** Interface to describe the methods to override */

public interface IClientServerConnection {

    public String getUsername();

    public String getPassword();

    public String getConfPassword();

    public String getFName();

    public String getLName();

    public User getUser();
}
