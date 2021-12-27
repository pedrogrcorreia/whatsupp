package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.data;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.requests.Friends;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.requests.LoginRegister;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.requests.Messages;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.requests.SearchUser;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.FriendsRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private String dbAddress;
    Connection con;
    static Statement stmt;

    private Server server;

    public DBManager(Server server) {
        this.dbAddress = server.getDB();
        this.server = server;
        // DEBUG
        // String db = "jdbc:mysql://localhost:3306/whatsupp_db";

        String db = "jdbc:mysql://" + this.dbAddress;
        // DEBUG
        // final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(Strings.JDBC_DRIVE.toString());
            // DEBUG
            // Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Couldn't find JDBC Driver: \r\n\t " + e);
            return;
        }

        try {
            con = DriverManager.getConnection(db, "root", "BCMFMAsp13");
            // stmt = con.createStatement();
            System.out.println("Connection successful.");
        } catch (SQLException e) {
            System.out.println("Couldn't connect to Database: \r\n\t " + e);
            return;
        }
    }

    public SharedMessage registerUser(SharedMessage request) {
        String username = request.getClientRequest().getUser().getUsername();
        String password = request.getClientRequest().getUser().getPassword();
        String confPassword = request.getClientRequest().getUser().getConfPassword();
        String fName = request.getClientRequest().getUser().getFName();
        String lName = request.getClientRequest().getUser().getLName();

        if (!password.equals(confPassword)) {
            return new SharedMessage(Strings.USER_REGISTER_FAIL, new String("Passwords don't match."));
        }
        String name = fName + " " + lName;
        if (username.equals(name)) {
            return new SharedMessage(Strings.USER_REGISTER_FAIL, new String("Username and name are equal."));
        }

        int status = 0;

        String query = new String("INSERT INTO users (username, password, name, status) " +
                "VALUES('" + username + "', '" + password + "', '" + name + "', " + status + ");");

        try {
            stmt = con.createStatement();
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
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    // login user
    public SharedMessage loginUser(SharedMessage request) {
        String username = request.getClientRequest().getUser().getUsername();
        String password = request.getClientRequest().getUser().getPassword();
        String query = new String("SELECT COUNT(*) AS nusers, user_id, username, password, name " +
                "FROM users where username = '" + username + "'");
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            int nUsers = rs.getInt("nusers");
            if (nUsers > 1) {
                rs.close();
                return new SharedMessage(Strings.USER_FAILED_LOGIN,
                        new String("Failed registering users. Multiple username."));
            } else if (nUsers == 1) {
                if (rs.getString("username").equals(username) && rs.getString("password").equals(password)) {
                    User user = new User(rs.getString("username"), rs.getString("name"), rs.getInt("user_id"));
                    rs.close();
                    return new SharedMessage(Strings.USER_SUCCESS_LOGIN, new String("Logged in successfully."),
                            new LoginRegister(user));
                }
                rs.close();
                return new SharedMessage(Strings.USER_FAILED_LOGIN, new String("Password doesn't match."));
            } else {
                rs.close();
                return new SharedMessage(Strings.USER_FAILED_LOGIN, new String("Username doesn't exist."));
            }
        } catch (SQLException e) {
            System.out.println("[login] Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_FAILED_LOGIN, new String("Error at database."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage getUser(SharedMessage request) {
        User user = request.getClientRequest().getUser();
        User selectedUser = request.getClientRequest().getSelectedUser();
        String query = new String(
                "SELECT COUNT(*) AS nusers, username, name, user_id, status " +
                        "FROM users where username = '" + selectedUser.getUsername() + "'");
        User userResponse = null;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            int nUsers = rs.getInt("nusers");
            if (nUsers == 0) {
                rs.close();
                return new SharedMessage(Strings.USER_REQUEST_USER_FAIL, new String("No users " +
                        "found."));
            } else {
                userResponse = new User(rs.getString("username"), rs.getString("name"),
                        rs.getInt("user_id"), rs.getInt("status"));
                rs.close();
                return new SharedMessage(Strings.USER_REQUEST_USER_SUCCESS,
                        Strings.USER_REQUEST_USER_SUCCESS.toString(), new SearchUser(user, userResponse));
            }
        } catch (SQLException e) {
            System.out.println("[getUser] Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_USER_FAIL,
                    Strings.USER_REQUEST_USER_FAIL.toString(),
                    request.getClientRequest());
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage getFriends(SharedMessage request) {
        User user = request.getClientRequest().getUser();
        List<FriendsRequests> friends = new ArrayList<>();
        FriendsRequests newFriend = null;
        String query = new String("SELECT user_id, username, users.name, " +
                "requester_user_id, friend_user_id, friends_requests.request_time, " +
                "friends_requests.answer_time, friends_requests.request_status " +
                "FROM users " +
                "JOIN friends_requests ON user_id in (requester_user_id, friend_user_id) " +
                "AND friends_requests.request_status = 1 " +
                "WHERE (requester_user_id = " + user.getID() + " and friend_user_id = user_id) " +
                "or (requester_user_id = user_id and friend_user_id = " + user.getID() + ")");
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                if (rs.getInt("requester_user_id") == user.getID()) {
                    newFriend = new FriendsRequests(user,
                            new User(rs.getString("username"), rs.getString("name"),
                                    rs.getInt("user_id")),
                            rs.getTimestamp("request_time"), rs.getInt("request_status"),
                            rs.getTimestamp("answer_time"));
                } else {
                    newFriend = new FriendsRequests(
                            new User(rs.getString("username"), rs.getString("name"),
                                    rs.getInt("user_id")),
                            user,
                            rs.getTimestamp("request_time"), rs.getInt("request_status"),
                            rs.getTimestamp("answer_time"));
                }
                friends.add(newFriend);
            }
            rs.close();
            return new SharedMessage(Strings.USER_REQUEST_FRIENDS_SUCCESS, new String(friends.toString()),
                    new Friends(user, friends));
        } catch (SQLException e) {
            System.out.println("[getFriends] Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_FRIENDS_FAIL, new String("Error " +
                    "on database."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage getFriendsRequests(SharedMessage request) {
        User user = request.getClientRequest().getUser();
        List<FriendsRequests> friends = new ArrayList<>();
        FriendsRequests newFriend = null;
        String query = new String("SELECT user_id, username, users.name, " +
                "requester_user_id, friend_user_id, friends_requests.request_time, " +
                "friends_requests.answer_time " +
                "FROM users " +
                "JOIN friends_requests ON user_id in (requester_user_id, friend_user_id) " +
                "AND friends_requests.request_status = 0 " +
                "WHERE (requester_user_id = " + user.getID() + " and friend_user_id = user_id) ");
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                if (rs.getInt("requester_user_id") == user.getID()) {
                    newFriend = new FriendsRequests(user,
                            new User(rs.getString("username"), rs.getString("name"), rs.getInt("user_id")),
                            rs.getTimestamp("request_time"), rs.getTimestamp("answer_time"));
                } else {
                    newFriend = new FriendsRequests(
                            new User(rs.getString("username"), rs.getString("name"), rs.getInt("user_id")), user,
                            rs.getTimestamp("request_time"), rs.getTimestamp("answer_time"));
                }
                friends.add(newFriend);
            }
            rs.close();
            return new SharedMessage(Strings.USER_REQUEST_FRIENDS_REQUESTS_SUCCESS, new String(friends.toString()),
                    new Friends(user, friends));
        } catch (SQLException e) {
            System.out.println("[getFriendsRequests] Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_FRIENDS_REQUESTS_FAIL, new String("Error " +
                    "on database."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage getFriendsRequestsPending(SharedMessage request) {
        User user = request.getClientRequest().getUser();
        List<FriendsRequests> friends = new ArrayList<>();
        FriendsRequests newFriend = null;
        String query = new String("SELECT user_id, username, users.name, " +
                "requester_user_id, friend_user_id, friends_requests.request_time, " +
                "friends_requests.answer_time, friends_requests.request_status " +
                "FROM users " +
                "JOIN friends_requests ON user_id in (requester_user_id, friend_user_id) " +
                "AND friends_requests.request_status = 0 " +
                "WHERE (requester_user_id = user_id and friend_user_id = " + user.getID() + ") ");
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                if (rs.getInt("requester_user_id") == user.getID()) {
                    newFriend = new FriendsRequests(user,
                            new User(rs.getString("username"), rs.getString("name"), rs.getInt("user_id")),
                            rs.getTimestamp("request_time"), rs.getTimestamp("answer_time"));
                } else {
                    newFriend = new FriendsRequests(
                            new User(rs.getString("username"), rs.getString("name"), rs.getInt("user_id")), user,
                            rs.getTimestamp("request_time"), rs.getTimestamp("answer_time"));
                }
                friends.add(newFriend);
            }
            rs.close();
            return new SharedMessage(Strings.USER_REQUEST_FRIENDS_REQUESTS_PENDING_SUCCESS,
                    new String(friends.toString()),
                    new Friends(user, friends));
        } catch (SQLException e) {
            System.out.println("[getFriendsRequests] Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_FRIENDS_REQUESTS_PENDING_FAIL, new String("Error " +
                    "on database."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage addFriend(SharedMessage request) {
        User user = request.getClientRequest().getUser();
        User userToAdd = request.getClientRequest().getSelectedUser();

        String query = new String(
                "INSERT INTO friends_requests (requester_user_id, friend_user_id, request_time, request_status) " +
                        "VALUES (" + user.getID() + ", " + userToAdd.getID() + " , current_timestamp(), " + 0 + ")");
        try {
            stmt = con.createStatement();
            if (stmt.executeUpdate(query) < 1) {
                return new SharedMessage(Strings.USER_SEND_FRIEND_REQUEST_FAIL,
                        new String("Problem inserting on table new friend request."));
            } else {
                return new SharedMessage(Strings.USER_SEND_FRIEND_REQUEST_SUCCESS, new String("Friend request sent."));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Friend request already sent:\r\n\t" + e);
            return new SharedMessage(Strings.USER_SEND_FRIEND_REQUEST_FAIL, new String("Friend request already sent."));
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.USER_SEND_FRIEND_REQUEST_FAIL, new String("Problem with SQL query."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage getMessages(SharedMessage request) {
        User user = request.getClientRequest().getUser();
        User friend = request.getClientRequest().getSelectedUser();
        List<Message> messages = new ArrayList<>();
        String query = new String(
                "SELECT username, user_id, users.name, message_id, messages.text, user_id_to, user_id_from, messages.sent_time "
                        +
                        "FROM users " +
                        "JOIN messages ON user_id = user_id_to " +
                        "WHERE (user_id_from = " + user.getID() + " and user_id_to = " + friend.getID() + ") " +
                        "OR (user_id_from = " + friend.getID() + " and user_id_to = " + user.getID() + ")" +
                        "ORDER BY sent_time");
        if (friend.getID() == 0) {
            return new SharedMessage(Strings.USER_REQUEST_MESSAGES_FAIL, new String(messages.toString()),
                    new Messages(user, friend, messages));
        }
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Message newMessage;
                if (rs.getInt("user_id_to") == friend.getID()) {
                    newMessage = new Message(user,
                            friend,
                            rs.getString("text"),
                            rs.getInt("message_id"),
                            rs.getTimestamp("sent_time"));
                } else {
                    newMessage = new Message(
                            friend, user,
                            rs.getString("text"), rs.getInt("message_id"), rs.getTimestamp("sent_time"));
                }
                messages.add(newMessage);
            }
            rs.close();
            stmt.close();
            return new SharedMessage(Strings.USER_REQUEST_MESSAGES_SUCCESS, new String(messages.toString()),
                    new Messages(user, friend, messages));
        } catch (SQLException e) {
            System.out.println("[getMessages] Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_MESSAGES_FAIL, new String("Error on database."));
        } catch (NullPointerException e) {
            System.out.println("[getMessages] Error:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_MESSAGES_FAIL, new String("ERRO QUE NAO SEI"));
        }
    }

    public SharedMessage deleteMessage(SharedMessage request) {
        Message message = request.getClientRequest().getSelectedMessage();

        String query = new String("DELETE FROM messages WHERE message_id = " + message.getID());
        try {
            if (stmt.executeUpdate(query) < 1) {
                return new SharedMessage(Strings.MESSAGE_DELETE_FAIL,
                        new String("Problem deleting message."));
            } else {
                SharedMessage msgToSend = new SharedMessage(Strings.MESSAGE_SENT_SUCCESS, new String("Message sent"));
                server.sendToServerManager(msgToSend);
                return new SharedMessage(Strings.MESSAGE_DELETE_SUCCESS, new String("Message deleted"));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Couldn't delete this message:\r\n\t" + e);
            return new SharedMessage(Strings.MESSAGE_DELETE_FAIL, new String("Couldn't delete this message."));
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.MESSAGE_DELETE_FAIL, new String("Problem with SQL query."));
        }
    }

    public SharedMessage sendMessage(SharedMessage request) {
        User sender = request.getClientRequest().getUser();
        User receiver = request.getClientRequest().getSelectedUser();
        Message message = request.getClientRequest().getSelectedMessage();
        // System.out.println("TESTE" + messages);

        String query = new String("INSERT INTO messages (user_id_from, text, sent_time, user_id_to) " +
                "VALUES (" + sender.getID() + ", '" + message.getMsgTxt() + "', current_timestamp(), "
                + receiver.getID() + ")");
        try {
            stmt = con.createStatement();
            if (stmt.executeUpdate(query) < 1) {
                return new SharedMessage(Strings.MESSAGE_SENT_FAIL,
                        new String("Problem sending message."));
            } else {
                SharedMessage msgToSend = new SharedMessage(Strings.NEW_MESSAGE, new String("Message sent"));
                server.sendToServerManager(msgToSend);
                return new SharedMessage(Strings.MESSAGE_SENT_SUCCESS, new String("Message sent"));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Couldn't send this message:\r\n\t" + e);
            return new SharedMessage(Strings.MESSAGE_SENT_FAIL, new String("Couldn't send this message."));
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.MESSAGE_SENT_FAIL, new String("Problem with SQL query."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    // }
    // public SharedMessage getMessages(SharedMessage request){
    // String username = request.getClientServerConnection().getUsername();
    // List<String> messages = new ArrayList<>();
    // String query = new String("SELECT messages.text from messages" +
    // "where user_id_from");
    // try{
    // ResultSet rs = stmt.executeQuery(query);
    // while(rs.next()){
    // System.out.println(rs.getString("text"));
    // }
    // }catch(SQLException e){
    // System.out.println("");
    // }
    // }

    // public static void main(String[] args) {
    // DBManager db = new DBManager(new Server(new DatagramPacket(new byte[1], 1)));
    // User user = new User("pedro", "pedro correia", 1);
    // User friend = new User("zetolo", "ze bartolo", 2);
    // List<FriendsRequests> friends;
    // Friends f = new Friends(user);
    // SharedMessage request = new SharedMessage(null, null, f);
    // SharedMessage response = db.getFriends(request);
    // System.out.println(response.getClientRequest().getFriendsRequests());
    // }

    // public static void main(String[] args) throws SQLException {
    // int id;
    // // String username, name;
    // DBManager db = new DBManager(new Server(new DatagramPacket(new byte[1], 1)));
    // Statement stmt = db.con.createStatement();
    // // ResultSet rs = stmt.executeUpdate("SELECT * FROM users;");
    // String username = "p";
    // String password = "1234";
    // String name = "pedro correia";
    // int status = 0;
    // String query = new String("SELECT COUNT(*) AS nusers, username, password FROM
    // users where username = '" +
    // username + "'");
    // ResultSet rs = stmt.executeQuery(query);
    // rs.next();
    // System.out.println(rs.getInt("nusers"));

    // // while (rs.next()) {
    // // id = rs.getInt("user_id");
    // // username = rs.getString("username");
    // // name = rs.getString("name");

    // // System.out.println(id + " " + username + " " + name);
    // // }
    // }

    // public boolean register(String username, String password, String
    // confPassword, String fname, String lname){
    // if(!password.equals(confPassword)){
    // return false;
    // }

    // }

    // public boolean login(String username, String password){

    // }
}
