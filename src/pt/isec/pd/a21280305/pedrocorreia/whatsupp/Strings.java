package pt.isec.pd.a21280305.pedrocorreia.whatsupp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public enum Strings {
    JDBC_DRIVE("com.mysql.cj.jdbc.Driver"),

    /** Server <-> Server Manager */

    SERVER_REGISTER_REQUEST("Request to register a server"),
    SERVER_REGISTER_SUCCESS("Registered the server successfully"),
    SERVER_REGISTER_FAIL("Failed to register the server"),
    SERVER_PING("Server pinging"),
    NEW_MESSAGE("Alert users that a new message has been sent"),

    /** Client <-> Server Manager */

    CLIENT_REQUEST_SERVER("Client is requesting a server"),

    /** Client <-> Server */

    /** Login */

    USER_REQUEST_LOGIN("User wants to login"),
    USER_SUCCESS_LOGIN("User logged in successful"),
    USER_FAILED_LOGIN("User failed to login"),

    /** Register */

    USER_REQUEST_REGISTER("User wants to register"),
    USER_REGISTER_FAIL("Register failed"),
    USER_REGISTER_SUCCESS("Register success"),

    /** Message */
    USER_SENT_MESSAGE("User sent a message"),
    MESSAGE_SENT_SUCCESS("Message was sent successfully"),
    MESSAGE_SENT_FAIL("Message couldn't be sent"),
    MESSAGE_DELETE("Delete a message"),
    MESSAGE_DELETE_SUCCESS("Message deleted"),
    MESSAGE_DELETE_FAIL("Failed to delete message"),

    /** Requests */

    /** User info */
    USER_REQUEST_OWN_INFO("Get user info"),
    USER_REQUEST_OWN_INFO_SUCCESS("Retrieved an User object"),
    USER_REQUEST_OWN_INFO_FAIL("Couldn't retrive the User"),

    /** Other users info */

    USER_REQUEST_USER("Get another user info"),
    USER_REQUEST_USER_SUCCESS("Retrieved an User object"),
    USER_REQUEST_USER_FAIL("Couldn't retreive the User"),

    /**
     * All info
     * 
     * @deprecated
     */
    USER_REQUEST_INFO("Get info"),
    USER_REQUEST_INFO_SUCCESS("User got the info he requested"),
    USER_REQUEST_INFO_FAIL("Couldn't retrieve the data request"),

    /** Friends */
    USER_REQUEST_FRIENDS("Get friends"),
    USER_REQUEST_FRIENDS_SUCCESS("User got the friends list"),
    USER_REQUEST_FRIENDS_FAIL("Couldn't retrieve the friends list"),
    USER_REQUEST_FRIENDS_REQUESTS("Get all friends requests"),
    USER_REQUEST_FRIENDS_REQUESTS_SUCCESS("User got the friends requests list"),
    USER_REQUEST_FRIENDS_REQUESTS_FAIL("Couldn't retrive the friends requests list"),
    USER_SEND_FRIEND_REQUEST("Friend request"),
    USER_SEND_FRIEND_REQUEST_SUCCESS("Success"),
    USER_SEND_FRIEND_REQUEST_FAIL("Failed"),

    /** Messages */
    USER_REQUEST_MESSAGES("Get messages"),
    USER_REQUEST_MESSAGES_SUCCESS("User got the messages"),
    USER_REQUEST_MESSAGES_FAIL("Couldn't retrieve the messages to the user"),

    /** Groups */
    USER_REQUEST_GROUPS("Get groups"),
    USER_REQUEST_GROUPS_SUCCESS("User got the groups"),
    USER_REQUEST_GROUPS_FAIL("Couldn't retrieve the groups to the user");

    private final String description;

    Strings(String s) {
        description = s;
    }

    @Override
    public String toString() {
        return description;
    }

    public static int MaxSize() {
        Strings longest = SERVER_REGISTER_REQUEST;
        for (Strings s : Strings.values()) {
            if (longest.toString().length() < s.toString().length()) {
                longest = s;
            }
        }
        ByteArrayOutputStream bout;
        ObjectOutputStream oout;
        bout = new ByteArrayOutputStream();
        try {
            oout = new ObjectOutputStream(bout);
            oout.writeUnshared(longest);
            for (byte b : bout.toByteArray()) {
                System.out.print((char) b);
            }
        } catch (IOException e) {

        }

        return bout.size();
    }
    // SERVER_REGISTER_REQUEST,
    // SERVER_REGISTER_SUCCESS,
    // SERVER_REGISTER_FAIL
}
