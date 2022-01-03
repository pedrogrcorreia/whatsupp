package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.data;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.requests.*;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.*;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private final String filesPath = Strings.SERVER_DOWNLOAD_PATH.toString();

    Connection con;
    static Statement stmt;

    private final Server server;

    public DBManager(Server server) {
        String dbAddress = server.getDB();
        this.server = server;
        // DEBUG
        // String db = "jdbc:mysql://localhost:3306/whatsupp_db";

        String db = "jdbc:mysql://" + dbAddress + "?allowMultiQueries=true";
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
            con = DriverManager.getConnection(db, "tester", "password-123");
             stmt = con.createStatement();
            System.out.println("Connection successful.");
        } catch (SQLException e) {
            System.out.println("Couldn't connect to Database: \r\n\t " + e);
        }
    }

    public SharedMessage setStatus(User user, int status) {
        String query = new String("UPDATE users SET status = " + status +
                " WHERE user_id = " + user.getID());
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            if (status == 1) {
                SharedMessage msgToSend = new SharedMessage(Strings.NEW_USER_ONLINE,
                        new String("New user online."), user.getID());
                server.sendToServerManager(msgToSend);
                return new SharedMessage(Strings.SET_USER_ONLINE,
                        new String("New user online."));
            } else {
                SharedMessage msgToSend = new SharedMessage(Strings.NEW_USER_OFFLINE,
                        new String("New user offline."), user.getID());
                server.sendToServerManager(msgToSend);
                return new SharedMessage(Strings.SET_USER_OFFLINE,
                        new String("New user offline."), user.getID());
            }
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.USER_ACCEPT_FRIEND_REQUEST_FAIL, new String("Problem with SQL query."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
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
                SharedMessage msgToSend = new SharedMessage(Strings.NEW_USER_REGISTERED, new String("A new user has registered."));
                server.sendToServerManager(msgToSend);
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

    public SharedMessage updateUser(SharedMessage request){
        User me = request.getClientRequest().getUser();
        User newUser = request.getClientRequest().getSelectedUser();
        System.out.println("USER ID: " + me.getID());
        String selectQuery = new String("SELECT password FROM users WHERE user_id = " + me.getID());

        String updateQuery = new String("UPDATE users\n" +
                "SET username = '" + newUser.getUsername() + "',\n" +
                "name = '" + newUser.getName() + "',\n" +
                "password = '" + newUser.getConfPassword() + "'\n" +
                "WHERE user_id = " + me.getID());
        try{
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);
            rs.next();
            if(!newUser.getPassword().equals(rs.getString("password"))){
                rs.close();
                stmt.close();
                return new SharedMessage(Strings.USER_UPDATE_INFO_FAIL, new String("Previous password incorrect."));
            }
            else{
                stmt = con.createStatement();
                stmt.executeUpdate(updateQuery);
                User userToSend = new User(newUser.getUsername(), newUser.getName(), me.getID());
                return new SharedMessage(Strings.USER_UPDATE_INFO_SUCCESS, new String("New user info"), new LoginRegister(userToSend));
            }
        } catch (SQLException e) {
            System.out.println("[update] Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_UPDATE_INFO_FAIL, new String("Error at database."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage getAllUsers(SharedMessage request){
        User user = request.getClientRequest().getUser();
        String query = new String("SELECT username, name, user_id, status " +
                "FROM users where user_id != " + user.getID());
        List<User> users = new ArrayList<>();
        try{
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                User nUser = new User(rs.getString("username"), rs.getString("name"), rs.getInt("user_id"));
                users.add(nUser);
            }
            rs.close();
            return new SharedMessage(Strings.USER_REQUEST_ALL_USERS_SUCCESS, new String("List of all users"), new ClientRequests(user, users));
        } catch (SQLException e) {
            System.out.println("[getUser] Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_ALL_USERS_FAIL,
                    Strings.USER_REQUEST_ALL_USERS_FAIL.toString(),
                    request.getClientRequest());
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
        User userResponse = new User();
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
                        Strings.USER_REQUEST_USER_SUCCESS.toString(), new ClientRequests(user, userResponse));
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
        String query = new String("SELECT user_id, username, users.name, users.status, " +
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
                                    rs.getInt("user_id"), rs.getInt("status")),
                            rs.getTimestamp("request_time"), rs.getInt("request_status"),
                            rs.getTimestamp("answer_time"));
                } else {
                    newFriend = new FriendsRequests(
                            new User(rs.getString("username"), rs.getString("name"),
                                    rs.getInt("user_id"), rs.getInt("status")),
                            user,
                            rs.getTimestamp("request_time"), rs.getInt("request_status"),
                            rs.getTimestamp("answer_time"));
                }
                friends.add(newFriend);
            }
            rs.close();
            return new SharedMessage(Strings.USER_REQUEST_FRIENDS_SUCCESS, new String(friends.toString()),
                    new ClientRequests(user, friends));
        } catch (SQLException e) {
            System.out.println("[getFriends] Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_FRIENDS_FAIL, new String("Error " +
                    "on database."));
        } catch (NullPointerException e){
            e.printStackTrace();
            return new SharedMessage(Strings.USER_REQUEST_FRIENDS_FAIL, new String("Error " +
                    "on database."));
        }finally {
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
        FriendsRequests newFriend = new FriendsRequests();
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
                    new ClientRequests(user, friends));
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
                    new ClientRequests(user, friends));
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
        if(user.getID() == userToAdd.getID()){
            return new SharedMessage(Strings.USER_SEND_FRIEND_REQUEST_FAIL, new String("You can't ask your self."));
        }
        try {
            stmt = con.createStatement();
            if (stmt.executeUpdate(query) < 1) {
                return new SharedMessage(Strings.USER_SEND_FRIEND_REQUEST_FAIL,
                        new String("Problem inserting on table new friend request."));
            } else {
                SharedMessage msgToSend = new SharedMessage(Strings.NEW_FRIEND_REQUEST, new String("Friend request sent."), userToAdd.getID());
                server.sendToServerManager(msgToSend);
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

    public SharedMessage acceptRequest(SharedMessage request) {
        User user = request.getClientRequest().getUser();
        User newFriend = request.getClientRequest().getSelectedUser();

        String query = new String("UPDATE friends_requests SET request_status = 1, answer_time = current_timestamp() " +
                "WHERE requester_user_id = " + newFriend.getID() + " AND friend_user_id = " + user.getID());
        try {
            Statement stmt = con.createStatement();
            if (stmt.executeUpdate(query) < 1) {
                return new SharedMessage(Strings.USER_ACCEPT_FRIEND_REQUEST_FAIL,
                        new String("Couldn't accept the friend request."));
            } else {
                SharedMessage msgToSend = new SharedMessage(Strings.FRIEND_REQUEST_ACCEPT,
                        new String("Your friend request to " + user.getName() + " is accepted"), newFriend.getID());
                server.sendToServerManager(msgToSend);
                return new SharedMessage(Strings.USER_ACCEPT_FRIEND_REQUEST_SUCCESS,
                        new String("Friend request accepted successfull."));
            }
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.USER_ACCEPT_FRIEND_REQUEST_FAIL, new String("Problem with SQL query."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage deleteRequest(SharedMessage request) {
        User user = request.getClientRequest().getUser();
        User friend = request.getClientRequest().getSelectedUser();

        String query = new String("DELETE FROM friends_requests WHERE (requester_user_id = " +
                user.getID() + " AND friend_user_id = " + friend.getID() + ") " +
                "OR (requester_user_id = " + friend.getID() + " AND friend_user_id = " + user.getID() + ")");
        try {
            Statement stmt = con.createStatement();
            if (stmt.executeUpdate(query) < 1) {
                return new SharedMessage(Strings.USER_CANCEL_FRIEND_REQUEST_FAIL,
                        new String("Couldn't delete the friend request."));
            } else {
                SharedMessage msgToSend = new SharedMessage(Strings.FRIEND_REQUEST_CANCEL,
                        new String("User " + user.getName() + " cancel their friend request."), friend.getID());
                server.sendToServerManager(msgToSend);
                return new SharedMessage(Strings.USER_CANCEL_FRIEND_REQUEST_SUCCESS,
                        new String("Friend request deleted successfully."));
            }
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.USER_CANCEL_FRIEND_REQUEST_FAIL, new String("Problem with SQL query."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage deleteFriendship(SharedMessage request) {
        User user = request.getClientRequest().getUser();
        User friend = request.getClientRequest().getSelectedUser();
        String query = new String("DELETE FROM friends_requests WHERE (requester_user_id = " +
                user.getID() + " AND friend_user_id = " + friend.getID() + ") " +
                "OR (requester_user_id = " + friend.getID() + " AND friend_user_id = " + user.getID() + ")");
        String nextQuery = new String("DELETE files FROM files JOIN messages\n" +
                "WHERE files.message_id = messages.message_id\n" +
                "AND (messages.user_id_to = " + user.getID() + " AND messages.user_id_from = " + friend.getID() +")\n" +
                "OR (messages.user_id_to = " + friend.getID() + " AND messages.user_id_from = " + user.getID() +")");
        String finalQuery = new String("DELETE FROM messages WHERE (user_id_from = " +
                user.getID() + " AND user_id_to = " + friend.getID() + ") " +
                "OR (user_id_from = " + friend.getID() + " AND user_id_to = " + user.getID() + ")");
        try {
            Statement stmt = con.createStatement();
            if (stmt.executeUpdate(query) < 1) {
                return new SharedMessage(Strings.USER_CANCEL_FRIENDSHIP_FAIL,
                        new String("Couldn't delete the friend request."));
            } else {
                stmt.close();
                stmt = con.createStatement();
                stmt.executeUpdate(nextQuery);
                stmt.close();
                stmt = con.createStatement();
                stmt.executeUpdate(finalQuery);
                SharedMessage msgToSend = new SharedMessage(Strings.REMOVED_FRIEND,
                        new String("A friendship is cancelled..."), friend.getID());
                server.sendToServerManager(msgToSend);
                return new SharedMessage(Strings.USER_CANCEL_FRIENDSHIP_SUCCESS, new String("Friendship deleted"));
            }
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.USER_CANCEL_FRIEND_REQUEST_FAIL, new String("Problem with SQL query."));
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
        Group group = request.getClientRequest().getGroup();
        List<Message> messages = new ArrayList<>();
        String query;
        if(group == null){
            query = new String(
                    "SELECT file_path, username, user_id, users.name, messages.message_id, messages.text, user_id_to, user_id_from, messages.sent_time "
                            +
                            "FROM users " +
                            "JOIN messages ON user_id = user_id_to " +
                            "LEFT JOIN files on files.message_id = messages.message_id " +
                            "WHERE (user_id_from = " + user.getID() + " and user_id_to = " + friend.getID() + ") " +
                            "OR (user_id_from = " + friend.getID() + " and user_id_to = " + user.getID() + ")" +
                            "ORDER BY sent_time");
        }
        else{
            query = new String("SELECT file_path, username, user_id, u.name, m.message_id, user_id_from, text, sent_time, m.group_id\n" +
                    "FROM users u\n" +
                    "JOIN messages m on user_id = m.user_id_from\n" +
                    "JOIN whatsupp_db.groups g on m.group_id = g.group_id\n" +
                    "LEFT JOIN files on files.message_id = m.message_id\n" +
                    "WHERE g.group_id = " + group.getID() + "\n" +
                    "ORDER BY sent_time");
        }
//        if (friend.getID() == 0) {
//            return new SharedMessage(Strings.USER_REQUEST_MESSAGES_FAIL, new String(messages.toString()),
//                    new ClientRequests(user, friend, messages));
//        }
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Message newMessage;
                if(group == null) {
                    if (rs.getInt("user_id_to") == friend.getID()) {
                        newMessage = new Message(user,
                                friend,
                                rs.getString("text"),
                                rs.getInt("message_id"),
                                rs.getTimestamp("sent_time"), null, new File(rs.getString("file_path"), rs.getInt("message_id")));
                    } else {
                        newMessage = new Message(
                                friend, user,
                                rs.getString("text"), rs.getInt("message_id"), rs.getTimestamp("sent_time"), null, new File(rs.getString("file_path"), rs.getInt("message_id")));
                    }
                }
                else {
                        newMessage = new Message(new User(rs.getString("username"), rs.getString("name"), rs.getInt("user_id")),
                                null,
                                rs.getString("text"),
                                rs.getInt("message_id"),
                                rs.getTimestamp("sent_time"),
                                group,
                                new File(rs.getString("file_path"), rs.getInt("message_id")));

                }
                messages.add(newMessage);
            }
            rs.close();
            stmt.close();
            return new SharedMessage(Strings.USER_REQUEST_MESSAGES_SUCCESS, new String("Sent success."),
                    new ClientRequests(user, friend, messages));
        } catch (SQLException e) {
            System.out.println("[getMessages] Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_MESSAGES_FAIL, new String("Error on database."));
        } catch (NullPointerException e) {
            System.out.println("[getMessages] Error:\r\n\t" + e);
            e.printStackTrace();
            return new SharedMessage(Strings.USER_REQUEST_MESSAGES_FAIL, new String("ERRO QUE NAO SEI"));
        }
    }

    public SharedMessage deleteMessage(SharedMessage request) {
        Message message = request.getClientRequest().getSelectedMessage();

        String query = new String("DELETE FROM messages WHERE message_id = " + message.getID());
        try {
            stmt = con.createStatement();
            if (stmt.executeUpdate(query) < 1) {
                return new SharedMessage(Strings.MESSAGE_DELETE_FAIL,
                        new String("Problem deleting message."));
            } else {
                SharedMessage msgToSend = (message.getGroup() == null) ? new SharedMessage(Strings.DELETE_MESSAGE_USER, new String("Message delete by a friend."), message.getReceiver().getID()) :
                        new SharedMessage(Strings.DELETE_MESSAGE_GROUP, new String("Message deleted on group."), message.getGroup().getID());
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
        Group group = request.getClientRequest().getGroup();
        Message message = request.getClientRequest().getSelectedMessage();
        String query;
        int id = (group == null) ? receiver.getID() : group.getID();
        String type = (group == null) ? "user_id_to" : "group_id";
            query = new String("INSERT INTO messages (user_id_from, text, sent_time, " + type + ") " +
                "VALUES (" + sender.getID() + ", '" + message.getMsgTxt() + "', current_timestamp(), "
                + id + ")");
        try {
            stmt = con.createStatement();
            if (stmt.executeUpdate(query) < 1) {
                return new SharedMessage(Strings.MESSAGE_SENT_FAIL,
                        new String("Problem sending message."));
            } else {
                SharedMessage msgToSend = (group == null) ? new SharedMessage(Strings.NEW_MESSAGE_USER, new String("New message received."), id)
                        : new SharedMessage(Strings.NEW_MESSAGE_GROUP, new String("New message received on group."), id);
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

    public SharedMessage createNewGroup(SharedMessage request) {
        User admin = request.getClientRequest().getUser();
        Group newGroup = request.getClientRequest().getGroup();

        String query = new String("INSERT INTO whatsupp_db.groups (admin_user_id, name) " +
                "VALUES(" + admin.getID() + ", '" + newGroup.getName() + "')");
        String filterQuery = new String("SELECT admin_user_id, name FROM whatsupp_db.groups " +
                "WHERE name = '" + newGroup.getName() + "'");
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(filterQuery);
            while(rs.next()){
                if(rs.getInt("admin_user_id") == admin.getID()) {
                    return new SharedMessage(Strings.REQUEST_NEW_GROUP_FAIL, new String("You already own a group with the same name."));
                }
            }
            stmt.close();
            rs.close();
            stmt = con.createStatement();
            if(stmt.executeUpdate(query) < 1){
                return new SharedMessage(Strings.REQUEST_NEW_GROUP_FAIL, new String("Problem inserting new group."));
            }else{
                SharedMessage msgToSend = new SharedMessage(Strings.NEW_GROUP, new String("New group created on the server."));
                server.sendToServerManager(msgToSend);
                return new SharedMessage(Strings.REQUEST_NEW_GROUP_SUCCESS, new String("Group created successfully."));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Couldn't delete this message:\r\n\t" + e);
            return new SharedMessage(Strings.MESSAGE_DELETE_FAIL, new String("Couldn't delete this message."));
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.MESSAGE_DELETE_FAIL, new String("Problem with SQL query."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage getGroups(SharedMessage request){
        User user = request.getClientRequest().getUser();
        List<GroupRequests> groupRequests = new ArrayList<>();
        String query = new String("SELECT g.group_id, g.admin_user_id, g.name, gr.*, u.name, u.username\n" +
                "FROM whatsupp_db.groups g\n" +
                "LEFT JOIN group_requests gr ON gr.group_id = g.group_id \n" +
                "LEFT JOIN users u on g.admin_user_id = u.user_id\n" +
                "WHERE admin_user_id = " + user.getID() + "\n" +
                "OR g.group_id IN (\n" +
                "SELECT group_id\n" +
                "FROM group_requests\n" +
                "WHERE requester_user_id = " + user.getID() + "\n" +
                "AND request_status = 1)" +
                "GROUP BY g.group_id");
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                User admin = new User(rs.getString("username"), rs.getString("name"), rs.getInt("admin_user_id"));
                if(admin.getID() == user.getID()){
                    Group newGroup = new Group(user, rs.getString("name"), rs.getInt("group_id"));
                    GroupRequests newGR = new GroupRequests(admin, newGroup);
                    groupRequests.add(newGR);
                }
                else {
                    Group newGroup = new Group(admin, rs.getString("name"), rs.getInt("g.group_id"));
                    GroupRequests newGR = new GroupRequests(user, newGroup, rs.getTimestamp("request_time"), rs.getTimestamp("answer_time"), rs.getInt("request_status"));
                    groupRequests.add(newGR);
                }
            }
            rs.close();
            return new SharedMessage(Strings.USER_REQUEST_GROUPS_SUCCESS, new String("Requested success"), new ClientRequests(user, groupRequests));
        } catch (SQLException e) {
            System.out.println("[getMessages] Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_GROUPS_FAIL, new String("Error on database."));
        } catch (NullPointerException e) {
            System.out.println("[getMessages] Error:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_GROUPS_FAIL, new String("NullPointException"));
        }finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage getGroupsPending(SharedMessage request){
        User user = request.getClientRequest().getUser();
        List<GroupRequests> groupRequests = new ArrayList<>();
        String query = new String("SELECT g.group_id, g.name, gr.group_id, gr.request_time, gr.request_status, u.username, u.name, u.user_id\n" +
                "FROM whatsupp_db.groups g, group_requests gr, users u\n" +
                "WHERE admin_user_id != " + user.getID() + "\n" +
                "AND g.group_id IN (\n" +
                "SELECT group_id\n" +
                "FROM group_requests\n" +
                "WHERE requester_user_id = " + user.getID() + "\n" +
                "AND request_status = 0)\n" +
                "AND gr.group_id = g.group_id\n" +
                "AND u.user_id = g.admin_user_id\n" +
                "group by g.group_id");
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                User admin = new User(rs.getString("username"), rs.getString("name"), rs.getInt("user_id"));
                Group newGroup = new Group(admin, rs.getString("name"), rs.getInt("group_id"));
                GroupRequests newGR = new GroupRequests(user, newGroup, rs.getTimestamp("request_time"), rs.getInt("request_status"));
                groupRequests.add(newGR);
            }
            rs.close();
            return new SharedMessage(Strings.USER_REQUEST_PENDING_GROUPS_SUCCESS, new String("Requested success"), new ClientRequests(user, groupRequests));
        } catch (SQLException e) {
            System.out.println("[getMessages] Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_PENDING_GROUPS_FAIL, new String("Error on database."));
        } catch (NullPointerException e) {
            System.out.println("[getMessages] Error:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_PENDING_GROUPS_FAIL, new String("ERRO QUE NAO SEI"));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage getGroupsAvailable(SharedMessage request){
        User user = request.getClientRequest().getUser();
        List<Group> groups = new ArrayList<>();

        String query = new String("SELECT g.group_id, g.name, u.name, u.username, g.admin_user_id\n" +
                "FROM whatsupp_db.groups g, users u\n" +
                "WHERE g.group_id NOT IN (\n" +
                "SELECT group_id\n" +
                "FROM group_requests\n" +
                "WHERE requester_user_id = " + user.getID() + ")\n" +
                "AND admin_user_id != " + user.getID() + "\n" +
                "AND u.user_id = admin_user_id\n" +
                "group by g.group_id");

        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                User admin = new User(rs.getString("username"), rs.getString("name"), rs.getInt("admin_user_id"));
                Group newGroup = new Group(admin, rs.getString("name"), rs.getInt("group_id"));
                groups.add(newGroup);
            }
            rs.close();
            return new SharedMessage(Strings.USER_REQUEST_AVAILABLE_GROUPS_SUCCESS, new String("Requested success"), new ClientRequests(user, groups));
        } catch (SQLException e) {
            System.out.println("[getMessages] Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_AVAILABLE_GROUPS_FAIL, new String("Error on database."));
        } catch (NullPointerException e) {
            System.out.println("[getMessages] Error:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_AVAILABLE_GROUPS_FAIL, new String("ERRO QUE NAO SEI"));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage getGroupsManage(SharedMessage request){
        User user = request.getClientRequest().getUser();
        List<Group> groups = new ArrayList<>();

        String query = new String("SELECT * FROM whatsupp_db.groups\n" +
                "WHERE admin_user_id = " + user.getID());

        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                Group newGroup = new Group(new User(rs.getInt("admin_user_id")), rs.getString("name"), rs.getInt("group_id"));
                groups.add(newGroup);
            }
            return new SharedMessage(Strings.USER_REQUEST_MANAGE_GROUPS_SUCCESS, new String("Requested success"), new ClientRequests(user, groups));
        } catch (SQLException e) {
            System.out.println("[getMessages] Error querying the database:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_MANAGE_GROUPS_FAIL, new String("Error on database."));
        } catch (NullPointerException e) {
            System.out.println("[getMessages] Error:\r\n\t" + e);
            return new SharedMessage(Strings.USER_REQUEST_MANAGE_GROUPS_FAIL, new String("ERRO QUE NAO SEI"));
        }
    }

    public SharedMessage quitGroup(SharedMessage request){
        User user = request.getClientRequest().getUser();
        Group group = request.getClientRequest().getGroup();
        String query = new String("DELETE FROM group_requests WHERE requester_user_id = " +
                user.getID() + " and group_id = " + group.getID());
        String deleteUserMessages = new String("DELETE FROM messages WHERE user_id_from = " + user.getID() + " AND group_id = " + group.getID());
        try {
            stmt = con.createStatement();
            if (stmt.executeUpdate(query) < 1) {
                return new SharedMessage(Strings.USER_QUIT_GROUP_FAIL,
                        new String("Couldn't quit the group."));
            } else {
                stmt.close();
                stmt = con.createStatement();
                stmt.executeUpdate(deleteUserMessages);
                SharedMessage msgToSend = new SharedMessage(Strings.QUIT_GROUP, new String("User left group."), group.getID());
                server.sendToServerManager(msgToSend);
                return new SharedMessage(Strings.USER_QUIT_GROUP_SUCCESS, new String("User left group."));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Couldn't delete this message:\r\n\t" + e);
            return new SharedMessage(Strings.USER_QUIT_GROUP_SUCCESS, new String("Couldn't quit the group."));
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.USER_QUIT_GROUP_SUCCESS, new String("Problem with SQL query."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage deleteGroup(SharedMessage request){
        User user = request.getClientRequest().getUser();
        Group group = request.getClientRequest().getGroup();

        String query = new String("DELETE FROM whatsupp_db.groups WHERE group_id = " + group.getID());
        String nextQuery = new String("DELETE FROM group_requests WHERE group_id = " + group.getID());
        String midQuery = new String("DELETE files FROM files JOIN messages\n" +
                "WHERE files.message_id = messages.message_id\n" +
                "AND group_id = " + group.getID() + " AND user_id_from = " + user.getID());
        String finalQuery = new String("DELETE FROM messages WHERE group_id = " + group.getID());
        try {
            Statement stmt = con.createStatement();
            stmt.addBatch(finalQuery);
            stmt.addBatch(nextQuery);
            stmt.addBatch(midQuery);
            stmt.addBatch(query);
            stmt.executeBatch();
            SharedMessage msgToSend = new SharedMessage(Strings.DELETED_GROUP,
                    new String("A friendship is cancelled..."), group.getID());
            server.sendToServerManager(msgToSend);
            return new SharedMessage(Strings.USER_DELETE_GROUP_SUCCESS, new String("Group deleted"));
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.USER_DELETE_GROUP_FAIL, new String("Problem with SQL query."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage manageGroup(SharedMessage request){
        User user = request.getClientRequest().getUser();
        Group group = request.getClientRequest().getGroup();
        List<GroupRequests> gr = new ArrayList<>();
        String query = new String("SELECT user_id, username, name, group_id, request_time, request_status, answer_time\n" +
                "FROM group_requests\n" +
                "JOIN users on user_id = requester_user_id\n" +
                "WHERE group_id = " + group.getID());

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                User requester = new User(rs.getString("username"), rs.getString("name"), rs.getInt("user_id"));
                GroupRequests fr = new GroupRequests(requester, group, rs.getTimestamp("request_time"), rs.getInt("request_status"));
                gr.add(fr);
            }
            return new SharedMessage(Strings.USER_MANAGE_GROUP_SUCCESS, new String("Success return list"),
                    new ClientRequests(user, gr));
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.USER_DELETE_GROUP_FAIL, new String("Problem with SQL query."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage changeGroupName(SharedMessage request){
        User user = request.getClientRequest().getUser();
        Group group = request.getClientRequest().getGroup();

        String query = new String("UPDATE whatsupp_db.groups SET name = '" + group.getName() + "'" +
                "\nWHERE group_id = " + group.getID());
        String filterQuery = new String("SELECT admin_user_id, name FROM whatsupp_db.groups " +
                "WHERE name = '" + group.getName() + "'");
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(filterQuery);
            while(rs.next()){
                if(rs.getInt("admin_user_id") == user.getID()) {
                    return new SharedMessage(Strings.USER_CHANGE_GROUP_FAIL, new String("You already own a group with the same name."));
                }
            }
            stmt.close();
            rs.close();
            stmt = con.createStatement();
            if(stmt.executeUpdate(query) < 1){
                return new SharedMessage(Strings.USER_CHANGE_GROUP_FAIL, new String("Couldn't change the group's name."));
            }else{
                SharedMessage msgToSend = new SharedMessage(Strings.CHANGE_NAME, new String("A group name was changed."), group.getID());
                server.sendToServerManager(msgToSend);
                return new SharedMessage(Strings.USER_CHANGE_GROUP_SUCCESS, new String("Group changed name."));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Couldn't delete this message:\r\n\t" + e);
            return new SharedMessage(Strings.USER_CHANGE_GROUP_FAIL, new String("Couldn't change the group's name."));
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.USER_CHANGE_GROUP_FAIL, new String("Problem with SQL query."));
        }

    }

    public SharedMessage sendGroupRequest(SharedMessage request){
        User user = request.getClientRequest().getUser();
        Group group = request.getClientRequest().getGroup();

        String query = new String("INSERT INTO group_requests(requester_user_id, group_id, request_time)\n" +
                "VALUES(" + user.getID() + ", " + group.getID() + ", current_timestamp)");
        try {
            stmt = con.createStatement();
            if (stmt.executeUpdate(query) < 1) {
                return new SharedMessage(Strings.USER_SEND_GROUP_REQUEST_FAIL,
                        new String("Problem inserting on table new friend request."));
            } else {
                SharedMessage msgToSend = new SharedMessage(Strings.NEW_GROUP_REQUEST, new String("New request to join a group."), group.getID());
                server.sendToServerManager(msgToSend);
                return new SharedMessage(Strings.USER_SEND_GROUP_REQUEST_SUCCESS, new String("Request to join a group sent."));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Friend request already sent:\r\n\t" + e);
            return new SharedMessage(Strings.USER_SEND_GROUP_REQUEST_FAIL, new String("Group request already sent."));
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.USER_SEND_GROUP_REQUEST_FAIL, new String("Problem with SQL query."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage acceptGroupRequest(SharedMessage request){
        User user = request.getClientRequest().getUser();
        User toAccept = request.getClientRequest().getSelectedUser();
        Group group = request.getClientRequest().getGroup();

        String query = new String("UPDATE group_requests SET request_status = 1 " +
                "WHERE group_id = " + group.getID() + " AND requester_user_id = " + toAccept.getID());

        try {
            stmt = con.createStatement();
            if (stmt.executeUpdate(query) < 1) {
                return new SharedMessage(Strings.ADMIN_ACCEPT_GROUP_REQUEST_FAIL,
                        new String("Couldn't accept the friend request."));
            } else {
                SharedMessage msgToSend = new SharedMessage(Strings.ACCEPTED_GROUP_REQUEST, new String("Group request accepted."), group.getID());
                server.sendToServerManager(msgToSend);
                return new SharedMessage(Strings.USER_SEND_GROUP_REQUEST_SUCCESS, new String("Group request accepted"));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Friend request already sent:\r\n\t" + e);
            return new SharedMessage(Strings.ADMIN_ACCEPT_GROUP_REQUEST_FAIL, new String("Couldn't accept the friend request."));
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.ADMIN_ACCEPT_GROUP_REQUEST_FAIL, new String("Problem with SQL query."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage newFile(SharedMessage request){
        User sender = request.getClientRequest().getUser();
        User receiver = request.getClientRequest().getSelectedUser();
        Group group = request.getClientRequest().getGroup();
        Message message = request.getClientRequest().getSelectedMessage();
        System.out.println(message.getFile().getPath());
        java.io.File f = new java.io.File(message.getFile().getPath());
        String fileName = f.getName();
        System.out.println(fileName);
        String query;

        int id = (group == null) ? receiver.getID() : group.getID();
        String type = (group == null) ? "user_id_to" : "group_id";
        query = new String("INSERT INTO messages (user_id_from, sent_time, " + type + ") " +
                "VALUES (" + sender.getID() + ", current_timestamp(), "
                + id + ")");

        try{
            stmt = con.createStatement();
            int updated = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            int message_id = rs.getInt(1);
            stmt.close();
            rs.close();
            String insertFile = new String("INSERT INTO files (message_id, file_path)\n" +
                    "VALUES (" + message_id + ", '" + filesPath+fileName + "')");
            stmt = con.createStatement();
            stmt.executeUpdate(insertFile);
            SharedMessage msgToServer = (group == null) ? new SharedMessage(Strings.NEW_FILE_SENT_USER, new String("A new file was sent."), receiver.getID(), fileName) :
                    new SharedMessage(Strings.NEW_FILE_SENT_GROUP, new String("A new file was sent to a group."), group.getID(), fileName);
            server.sendToServerManager(msgToServer);
            return new SharedMessage(Strings.USER_SEND_FILE_SUCCESS, new String("File sent success."), request.getClientRequest());
        } catch (SQLException e) {
            System.out.println("Error registering filename:\n\r\t " + e);
            return new SharedMessage(Strings.USER_SEND_FILE_FAIL, new String("Failed to register the file name"));
        }finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public String downloadFile(SharedMessage request){
        Message m = request.getClientRequest().getSelectedMessage();
        String query = new String("SELECT file_path FROM files WHERE message_id = " + m.getID());

        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String path = rs.getString("file_path");
            rs.close();
            return path;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Friend request already sent:\r\n\t" + e);
            return null;
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return null;
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }

    public SharedMessage deleteFile(SharedMessage request){
        Message m = request.getClientRequest().getSelectedMessage();
        String filePath = null;

        String query = new String("SELECT file_path FROM files WHERE message_id = " + m.getID());
        String deleteQueryFiles = new String("DELETE FROM files WHERE message_id = " + m.getID());
        String deleteQueryMessages = new String("DELETE FROM messages WHERE message_id = " + m.getID());

        int id = (m.getGroup() == null) ? m.getReceiver().getID() : m.getGroup().getID();

        // TODO group or user conv

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                filePath = new String(rs.getString("file_path"));
            }
            rs.close();
            stmt.close();
            stmt = con.createStatement();
            stmt.addBatch(deleteQueryFiles);
            stmt.addBatch(deleteQueryMessages);
            stmt.executeBatch();
            SharedMessage msgToSend = (m.getGroup() == null) ? new SharedMessage(Strings.FILE_REMOVED_USER,
                    new String("File removed on a conversation."), id, new String(filePath)) :
                    new SharedMessage(Strings.FILE_REMOVED_GROUP, new String("File removed on a group."), id, new String(filePath));
            server.sendToServerManager(msgToSend);
            return new SharedMessage(Strings.USER_DELETE_FILE_SUCCESS, new String("File deleted"));
        } catch (SQLException e) {
            System.out.println("SQLException problem:\r\n\t" + e);
            return new SharedMessage(Strings.USER_DELETE_FILE_FAIL, new String("Problem with SQL query."));
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("SQLException problem:\r\n\t" + e);
            }
        }
    }
}
