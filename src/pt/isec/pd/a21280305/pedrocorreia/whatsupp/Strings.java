package pt.isec.pd.a21280305.pedrocorreia.whatsupp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public enum Strings {
    SERVER_REGISTER_REQUEST("Request to register a server"),
    SERVER_REGISTER_SUCCESS("Registered the server successfully"),
    SERVER_REGISTER_FAIL("Failed to register the server"),
    JDBC_DRIVE("com.mysql.cj.jdbc.Driver"),
    SERVER_PING("Server pinging"),
    CLIENT_REQUEST_SERVER("Client is requesting a server"),
    USER_REQUEST_LOGIN("User wants to login"),
    USER_REQUEST_REGISTER("User wants to register"),
    USER_SUCCESS_LOGIN("User logged in successful"),
    USER_FAILED_LOGIN("User failed to login"),
    USER_REGISTER_FAIL("Register failed"),
    USER_REGISTER_SUCCESS("Register success"),
    USER_REQUEST_INFO("Get info"),
    USER_REQUEST_INFO_SUCCESS("User got the info he requested"),
    USER_REQUEST_INFO_FAIL("Couldn't retrieve the data request"),
    USER_REQUEST_FRIENDS("Get friends"),
    USER_REQUEST_FRIENDS_SUCCESS("User got the friends list"),
    USER_REQUEST_FRIENDS_FAIL("Couldn't retrieve the friends list"),
    USER_REQUEST_MESSAGES("Get messages"),
    USER_REQUEST_MESSAGES_SUCCESS("User got the messages"),
    USER_REQUEST_MESSAGES_FAIL("Couldn't retrieve the messages to the user"),
    USER_SENT_MESSAGE("User sent a message"),
    MESSAGE_SENT_SUCCESS("Message was sent successfully"),
    MESSAGE_SENT_FAIL("Message couldn't be sent");

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
