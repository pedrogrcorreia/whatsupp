package pt.isec.pd.a21280305.pedrocorreia.whatsupp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public enum Strings {
    JDBC_DRIVE("com.mysql.cj.jdbc.Driver"),
    SERVER_DOWNLOAD_PATH("./server/download"),
    SERVER_MANAGER_DOWNLOAD_PATH("./server manager/download"),

    /** Server <-> Server Manager */


    SERVER_REGISTER_REQUEST("Request to register a server"),
    SERVER_REGISTER_SUCCESS("Registered the server successfully"),
    SERVER_REGISTER_FAIL("Failed to register the server"),
    SERVER_PING("Server pinging"),

    /** Notifications to all users */

    NEW_MESSAGE_GROUP("A new message has been sent to the system on a group"),
    DELETE_MESSAGE_GROUP("A new message was deleted on a group"),
    NEW_GROUP("A new group was created"),
    QUIT_GROUP("A user quit a group"),
    CHANGE_NAME("Group changed name"),
    NEW_GROUP_REQUEST("A new group request has been done"),
    ACCEPTED_GROUP_REQUEST("A new group request has been accepted"),
    DELETED_GROUP("A group is deleted"),
    NEW_USER_REGISTERED("User register"),
    NEW_USER_LOGIN("User login"),

    /** Notifications by user id */
    NEW_MESSAGE_USER("A new message has been sent to the system"),
    DELETE_MESSAGE_USER("A new message was deleted"),
    NEW_FRIEND_REQUEST("A new friend request has been updated on the system"),
    FRIEND_REQUEST_ACCEPT(""),
    FRIEND_REQUEST_CANCEL(""),
    REMOVED_FRIEND("A friendship has been cancelled"),
    NEW_FILE_SENT_USER(""),
    NEW_FILE_SENT_GROUP(""),
    FILE_REMOVED_USER(""),
    FILE_REMOVED_GROUP(""),


    /** Client <-> Server Manager */

    CLIENT_REQUEST_SERVER("Client is requesting a server"),

    /** Client <-> Server */

    /** Set users status */
    SET_USER_ONLINE("User is online"),
    SET_USER_OFFLINE("User is offline"),
    NEW_USER_ONLINE("An user came online"),
    NEW_USER_OFFLINE("An user went offline"),
    NEW_FILE("User sent file"),

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

    /**
     * User info
     * 
     * @deprecated
     */
    USER_REQUEST_USER_INFO("Get user info"),
    USER_REQUEST_USER_INFO_SUCCESS("Retrieved an User object"),
    USER_REQUEST_USER_INFO_FAIL("Couldn't retrive the User"),

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
    USER_REQUEST_FRIENDS_REQUESTS("Get sent friends requests"),
    USER_REQUEST_FRIENDS_REQUESTS_SUCCESS("User got the friends requests list"),
    USER_REQUEST_FRIENDS_REQUESTS_FAIL("Couldn't retrive the friends requests list"),
    USER_REQUEST_FRIENDS_REQUESTS_PENDING("Get received friends requests"),
    USER_REQUEST_FRIENDS_REQUESTS_PENDING_SUCCESS("User got the friends requests list"),
    USER_REQUEST_FRIENDS_REQUESTS_PENDING_FAIL("Couldn't retrieve the friends requests list"),
    USER_SEND_FRIEND_REQUEST("Friend request"),
    USER_SEND_FRIEND_REQUEST_SUCCESS("Success"),
    USER_SEND_FRIEND_REQUEST_FAIL("Failed"),
    USER_ACCEPT_FRIEND_REQUEST("Accepting a friend request"),
    USER_ACCEPT_FRIEND_REQUEST_SUCCESS("Success"),
    USER_ACCEPT_FRIEND_REQUEST_FAIL("Failed"),
    USER_CANCEL_FRIEND_REQUEST("Cancel a friend request"),
    USER_CANCEL_FRIEND_REQUEST_SUCCESS("Success"),
    USER_CANCEL_FRIEND_REQUEST_FAIL("Fail"),
    USER_CANCEL_FRIENDSHIP("Delete friendship"),
    USER_CANCEL_FRIENDSHIP_SUCCESS("Friendship cancelled"),
    USER_CANCEL_FRIENDSHIP_FAIL("Couldn't cancel this friendship"),

    /** Messages */
    USER_REQUEST_MESSAGES("Get messages"),
    USER_REQUEST_MESSAGES_SUCCESS("User got the messages"),
    USER_REQUEST_MESSAGES_FAIL("Couldn't retrieve the messages to the user"),

    /** Groups */
    USER_REQUEST_GROUPS("Get groups"),
    USER_REQUEST_GROUPS_SUCCESS("User got the groups"),
    USER_REQUEST_GROUPS_FAIL("Couldn't retrieve the groups to the user"),
    USER_REQUEST_AVAILABLE_GROUPS("Get available groups"),
    USER_REQUEST_AVAILABLE_GROUPS_SUCCESS("User got the available groups"),
    USER_REQUEST_AVAILABLE_GROUPS_FAIL("Couldn't retrieve the groups to the user"),
    USER_REQUEST_PENDING_GROUPS("Get pending groups"),
    USER_REQUEST_PENDING_GROUPS_SUCCESS("User got the pending groups"),
    USER_REQUEST_PENDING_GROUPS_FAIL("Couldn't retrieve the groups to the user"),
    USER_REQUEST_MANAGE_GROUPS("Get the groups to manage"),
    USER_REQUEST_MANAGE_GROUPS_SUCCESS("User got the manageable groups"),
    USER_REQUEST_MANAGE_GROUPS_FAIL("Couldn't retrieve the groups to the user"),
    REQUEST_NEW_GROUP("New group"),
    REQUEST_NEW_GROUP_SUCCESS("New group created"),
    REQUEST_NEW_GROUP_FAIL("Couldn't create new group"),
    USER_QUIT_GROUP("Quit a group"),
    USER_QUIT_GROUP_SUCCESS("Success"),
    USER_QUIT_GROUP_FAIL("Fail"),
    USER_DELETE_GROUP("Delete group"),
    USER_DELETE_GROUP_SUCCESS("Success"),
    USER_DELETE_GROUP_FAIL("Fail"),
    USER_MANAGE_GROUP("Manage group"),
    USER_MANAGE_GROUP_SUCCESS("Success"),
    USER_MANAGE_GROUP_FAIL("Fail"),
    USER_CHANGE_GROUP("Change group name"),
    USER_CHANGE_GROUP_SUCCESS("Success"),
    USER_CHANGE_GROUP_FAIL("Fail"),
    USER_SEND_GROUP_REQUEST("Send a request to a group"),
    USER_SEND_GROUP_REQUEST_SUCCESS("Success"),
    USER_SEND_GROUP_REQUEST_FAIL("Fail"),
    ADMIN_ACCEPT_GROUP_REQUEST("Accept an user group request"),
    ADMIN_ACCEPT_GROUP_REQUEST_SUCCESS("Success"),
    ADMIN_ACCEPT_GROUP_REQUEST_FAIL("Fail"),

    /**
     * Files
     */

    USER_SEND_FILE("User sent a new file"),
    USER_SEND_FILE_SUCCESS("Success"),
    USER_SEND_FILE_FAIL("Fail"),
    USER_DELETE_FILE("Delete a file"),
    USER_DELETE_FILE_SUCCESS("Success"),
    USER_DELETE_FILE_FAIL("Fail"),
    UPLOAD_FILE("File upload"),
    UPLOAD_SUCCESS("Success"),
    UPLOAD_FAIL("Fail"),
    DOWNLOAD_FILE("File download"),
    DOWNLOAD_SUCCESS("Success"),
    DOWNLOAD_FAIL("Fail");

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
