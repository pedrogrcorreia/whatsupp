package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.data;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientRequestInfo;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientServerConnection;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private final Server server;
    private String dbAddress;
    private String dbName;
    Connection con;
    static Statement stmt;

    public DBManager(Server server) {
        this.server = server;
        this.dbAddress = server.getDB();
        // DEBUG
        // String db = "jdbc:mysql://localhost:3306/whatsupp_db";

        String db = "jdbc:mysql://" + this.dbAddress;

        final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
        try {
            // Class.forName(Strings.JDBC_DRIVE.toString());
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Couldn't find JDBC Driver: \r\n\t " + e);
            return;
        }

        try {
            con = DriverManager.getConnection(db, "root", "BCMFMAsp13");
            stmt = con.createStatement();
            System.out.println("Connection successful.");
        } catch (SQLException e) {
            System.out.println("Couldn't connect to Database: \r\n\t " + e);
            return;
        }
    }

    public SharedMessage registerUser(SharedMessage request) {
        String username = request.getClientServerConnection().getUsername();
        String password = request.getClientServerConnection().getPassword();
        String confPassword = request.getClientServerConnection().getConfPassword();
        String fName = request.getClientServerConnection().getFName();
        String lName = request.getClientServerConnection().getLName();

        if (!password.equals(confPassword)) {
            return new SharedMessage(Strings.USER_REGISTER_FAIL, new String("Passwords don't match."));
        }
        String name = fName + " " + lName;
        if (username.equals(name)) {
            return new SharedMessage(Strings.USER_REGISTER_FAIL, new String("Username and name are equal."));
        }

        int status = 0;

        String query = new String("INSERT INTO users (username, password, name, status) VALUES('" + username + "', '"
                + password + "', '" + name + "', " + status + ");");

        try {
            if (stmt.executeUpdate(query) < 1) {
                return new SharedMessage(Strings.USER_REGISTER_FAIL, new String("Problem inserting on table."));
            } else {
                return new SharedMessage(Strings.USER_REGISTER_SUCCESS, new String("User registered successfully."));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username already exists:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REGISTER_FAIL, new String("Username already exists."));
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REGISTER_FAIL, new String("Problem with SQL query."));
        }
    }

    // login user
    public SharedMessage loginUser(SharedMessage request) {
        String username = request.getClientServerConnection().getUsername();
        String password = request.getClientServerConnection().getPassword();
        String dbUsername;
        String dbPassword;
        String query = new String("SELECT COUNT(*) AS nusers, username, password FROM users where username = '" +
                username + "'");
        try {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            int nUsers = rs.getInt("nusers");
            if (nUsers > 1) {
                return new SharedMessage(Strings.USER_FAILED_LOGIN,
                        new String("Failed registering users. Multiple username."));
            } else if (nUsers == 1) {
                if (rs.getString("username").equals(username) && rs.getString("password").equals(password)) {
                    return new SharedMessage(Strings.USER_SUCCESS_LOGIN, new String("Logged in successfully."));
                }
                return new SharedMessage(Strings.USER_FAILED_LOGIN, new String("Password doesn't match."));
            } else {
                return new SharedMessage(Strings.USER_FAILED_LOGIN, new String("Username doesn't exist."));
            }
        } catch (SQLException e) {
            System.out.println("Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_FAILED_LOGIN, new String("Error at database."));
        }
    }

    public SharedMessage getFriends(SharedMessage request) {
        String username = request.getClientServerConnection().getUsername();
        List<String> friends = new ArrayList<>();
        // StringBuilder friends = new StringBuilder();
        String query = new String("select username from users where user_id in (" +
                "select friend_user_id from users join friends_requests on (users.user_id = requester_user_id)" +
                "and request_status = 1 where users.username = '" + username + "')");
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                // friends.add(rs.getString("username"));
                System.out.println(rs.getString("username"));
                ((ClientRequestInfo) request.getClientServerConnection()).addFriend(rs.getString("username"));
            }
            return new SharedMessage(Strings.USER_REQUEST_INFO_SUCCESS, new String(friends.toString()),
                    request.getClientServerConnection());
        } catch (SQLException e) {
            System.out.println("Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_INFO_FAIL, new String("Error on database."));
        }
    }

    public static void main(String[] args) throws SQLException {
        int id;
        // String username, name;
        DBManager db = new DBManager(new Server(new DatagramPacket(new byte[1], 1)));
        Statement stmt = db.con.createStatement();
        // ResultSet rs = stmt.executeUpdate("SELECT * FROM users;");
        String username = "p";
        String password = "1234";
        String name = "pedro correia";
        int status = 0;
        String query = new String("SELECT COUNT(*) AS nusers, username, password FROM users where username = '" +
                username + "'");
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        System.out.println(rs.getInt("nusers"));

        // while (rs.next()) {
        // id = rs.getInt("user_id");
        // username = rs.getString("username");
        // name = rs.getString("name");

        // System.out.println(id + " " + username + " " + name);
        // }
    }

    // public boolean register(String username, String password, String
    // confPassword, String fname, String lname){
    // if(!password.equals(confPassword)){
    // return false;
    // }

    // }

    // public boolean login(String username, String password){

    // }
}
