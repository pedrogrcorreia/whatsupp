package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.requests;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.FriendsRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Group;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.GroupRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

public abstract class ClientRequests implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected User user;
    protected User selectedUser;
    protected List<Message> messages;
    protected List<FriendsRequests> friends;
    protected List<Group> groups;
    protected List<GroupRequests> groupRequests;
    protected Message selectedMessage;

    public ClientRequests(User user) {
        this.user = user;
    }

    /**
     * Constructor to get friends
     * 
     * @param user
     * @param friends
     */
    public ClientRequests(User user, List<FriendsRequests> friends) {
        this.user = user;
        this.friends = friends;
    }

    /**
     * Constructor to get an user info
     * 
     * @param user
     * @param selectedUser
     */
    public ClientRequests(User user, User selectedUser) {
        this.user = user;
        this.selectedUser = selectedUser;
    }

    /**
     * Constructor to get messages
     * 
     * @param user
     * @param selectedUser
     * @param messages
     */
    public ClientRequests(User user, User selectedUser, List<Message> messages) {
        this.user = user;
        this.selectedUser = selectedUser;
        this.messages = messages;
    }

    /**
     * Constructor to delete a message
     * 
     * @param user
     * @param selectedMessage
     */
    public ClientRequests(User user, Message selectedMessage) {
        this.user = user;
        this.selectedMessage = selectedMessage;
    }

    /** Constructor to send a message */
    public ClientRequests(User user, User selectedUser, Message selectedMessage) {
        this.user = user;
        this.selectedUser = selectedUser;
        this.selectedMessage = selectedMessage;
    }

    public ClientRequests(User user, List<Message> messages, List<FriendsRequests> friends, List<Group> groups,
            List<GroupRequests> groupRequests) {
        this.user = user;
        this.messages = messages;
        this.friends = friends;
        this.groups = groups;
        this.groupRequests = groupRequests;
    }

    /** Method on LoginRegister class */
    public boolean login(ObjectInputStream oin, ObjectOutputStream oout, List<SharedMessage> list) {
        return false;
    }

    /** Method on LoginRegister class */
    public boolean register(ObjectInputStream oin, ObjectOutputStream oout, List<SharedMessage> list) {
        return false;
    }

    /** Method on Friends class */
    public boolean getFriends(ObjectOutputStream oout) {
        return false;
    }

    /** Method on LoginRegister class */
    public boolean getFriendsRequests(ObjectOutputStream oout) {
        return false;
    }

    /** Method on LoginRegister class */
    public boolean addFriend(ObjectOutputStream oout) {
        return false;
    }

    /** Method on Messages class */
    public boolean getMessagesFromUser(ObjectOutputStream oout) {
        return false;
    }

    /** Method on Messages class */
    public boolean getMessagesFromGroup(ObjectOutputStream oout) {
        return false;
    }

    /** Method on Messages class */
    public boolean sendMessageToUser(ObjectOutputStream oout) {
        return false;
    }

    /** Method on Messages class */
    public boolean deleteMessage(ObjectOutputStream oout) {
        return false;
    }

    public User getUser() {
        return user;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<FriendsRequests> getFriendsRequests() {
        return friends;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public Message getSelectedMessage() {
        return selectedMessage;
    }
}
