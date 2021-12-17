package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection;

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

}
