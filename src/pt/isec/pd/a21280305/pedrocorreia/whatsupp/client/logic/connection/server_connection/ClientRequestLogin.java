package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection;

public class ClientRequestLogin extends ClientServerConnection {
    private String username;
    private String password;

    public ClientRequestLogin(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
