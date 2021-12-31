package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.List;

import javafx.application.Platform;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.*;

public class ClientObservable implements Runnable {
    private Client client;
    private final PropertyChangeSupport propertyChangeSupport;

    private SharedMessage notification = null;
    private String notificationMessage = "";

    // MELHORAR
    public boolean messagesTo = false;

    public ClientObservable(Client client) {
        this.client = client;
        propertyChangeSupport = new PropertyChangeSupport(client);
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void forceUpdate() {
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void updateNotification() {
        propertyChangeSupport.firePropertyChange("notification", null, null);
    }

    public void updateStates(Strings msg){ propertyChangeSupport.firePropertyChange(msg.name(), null, null);}

    /**
     * Contact server manager
     */

    public void contactServerManager() {
        client.contactServerManager();
        forceUpdate();
    }

    /**
     * Initial menu
     */

    public void initialStatus(String opt) {
        client.initialStatus(opt);
        forceUpdate();
    }

    /**
     * Login and register
     */

    public void login(String username, String password) {
        client.login(username, password);
        forceUpdate();
    }

    public void register(String username, String password, String confPassword, String fname, String lname) {
        client.register(username, password, confPassword, fname, lname);
        forceUpdate();
    }

    /**
     * Search users
     */

    public void searchUsers() {
        client.searchUsers();
        forceUpdate();
    }

    public void searchUser(String username) {
        client.searchUser(username);
    }

    /** Friends */

    public void seeFriends() {
        client.seeFriends();
        forceUpdate();
    }

    public void seeFriendsRequests() {
        client.seeFriendsRequests();
        forceUpdate();
    }

    public void seeFriendsRequestsPending() {
        client.seeFriendsRequestsPending();
        forceUpdate();
    }

    public void acceptRequest(User user) {
        client.acceptRequest(user);
    }

    public void cancelRequest(User user) {
        client.cancelRequest(user);
    }

    public void addFriend(User user) {
        client.addFriend(user);
    }

    public void deleteFriendship(User user) {
        client.deleteFriendship(user);
    }

    /**
     * Groups
     */

    public void seeGroups() {
        client.seeGroups();
        forceUpdate();
    }

    public void createGroup() {
        client.createGroup();
        forceUpdate();
    }

    public void createNewGroup(String name){
        client.createNewGroup(name);
        forceUpdate();
    }

    public void seeAvailableGroups() {
        client.seeAvailableGroups();
        forceUpdate();
    }

    public void seePendingGroups(){
        client.seePendingGroups();
        forceUpdate();
    }

    public void seeManageGroups(){
        client.seeManageGroups();
        forceUpdate();
    }

    public void deleteGroup(Group g){
        client.deleteGroup(g);
    }

    public void quitGroup(Group g){
        client.quitGroup(g);
    }

    public void quitGroup(User u, Group g) { client.quitGroup(u, g); }

    public void manageMembers(Group g){
        client.manageMembers(g);
    }

    public void changeName(Group g){
        client.changeName(g);
    }

    public void sendGroupRequest(Group g){
        client.sendGroupRequest(g);
    }

    public void cancelGroupRequest(Group g){
        client.cancelGroupRequest(g);
    }

    public void acceptGroupRequest(User u, Group g){ client.acceptGroupRequest(u, g); }

    /**
     * Messages
     */

    public void seeMessages(User user) {
        client.seeMessages(user);
        forceUpdate();
    }

    public void seeMessages(Group group){
        client.seeMessages(group);
        messagesTo = true;
        forceUpdate();
    }

    public void deleteMessage(Message msg) {
        client.deleteMessage(msg);
        forceUpdate();
    }

    public void sendMessage(Message msg) {
        client.sendMessage(msg);
        forceUpdate();
    }

    public void sendMessageToGroup(Message msg) {
        client.sendMessageToGroup(msg);
        forceUpdate();
    }

    /**
     * Files
     *
     * @param f*/

    public void sendFile(Message f){
        client.sendFile(f);
    }

    public void uploadFile(Message file){ client.uploadFile(file); }

    public void downloadFile(Message file){ client.downloadFile(file); }
    /**
     * Back to start state
     */

    public void back() {
        client.back();
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    /**
     * Get notifications
     */

    public String getNotification() {
        return notificationMessage;
    }

    public SharedMessage getNotificationSM() {
        return notification;
    }

    public Situation getAtualState() {
        return client.getAtualState();
    }

    /** Gets directly from model */

    public User getUser() {
        return client.getUser();
    }

    public User getFriend() {
        return client.getFriend();
    }

    public Group getGroup() { return client.getGroup(); }

    public List<FriendsRequests> getFriendsRequests() {
        return client.getFriendsRequests();
    }

    public List<FriendsRequests> getFriendsRequestsPending() {
        return client.getFriendsRequestsPending();
    }

    public List<FriendsRequests> getFriendsRequestsSent() {
        return client.getFriendsRequestsSent();
    }

    public List<Message> getMessages() {
        return client.getMessages();
    }

    public List<GroupRequests> getMyGroups() {
        return client.getMyGroups();
    }

    public List<GroupRequests> getPendingGroups() {
        return client.getPendingGroups();
    }

    public List<Group> getManageGroups() {
        return client.getManageGroups();
    }

    public List<Group> getAvailableGroups() {
        return client.getAvailableGroups();
    }

    public List<GroupRequests> getGroupMembers() { return client.getGroupMembers(); }

    /**
     * Thread to check for notifications
     * Only call update when a notification is received.
     * When something that change states is received, don't.
     */
    @Override
    public void run() {
        while (true) {
            notification = client.getNotification();
            switch (notification.getMsgType()) {
//                case CLIENT_REQUEST_SERVER:
//                    notificationMessage = notification.getMsg();
//                    Platform.runLater(() -> updateNotification());
//                    break;
//                case USER_FAILED_LOGIN:
//                    notificationMessage = notification.getMsg();
//                    Platform.runLater(() -> updateNotification());
//                    break;
//                case USER_SUCCESS_LOGIN:
//                    notificationMessage = notification.getMsg();
//                    Platform.runLater(() -> updateNotification());
//                    break;
//                case USER_REGISTER_FAIL:
//                    notificationMessage = notification.getMsg();
//                    Platform.runLater(() -> updateNotification());
//                    break;
//                case USER_REGISTER_SUCCESS:
//                    notificationMessage = notification.getMsg();
//                    Platform.runLater(() -> updateNotification());
//                    break;
                case MESSAGE_DELETE_SUCCESS -> Platform.runLater(() -> updateStates(notification.getMsgType()));
                case USER_REQUEST_FRIENDS_FAIL -> Platform.runLater(() -> updateStates(Strings.USER_REQUEST_FRIENDS_FAIL));
                case USER_REQUEST_FRIENDS_SUCCESS -> Platform.runLater(() -> updateStates(Strings.USER_REQUEST_FRIENDS_SUCCESS));
                case USER_REQUEST_FRIENDS_REQUESTS_SUCCESS -> Platform.runLater(() -> updateStates(Strings.USER_REQUEST_FRIENDS_REQUESTS_SUCCESS));
                case USER_REQUEST_FRIENDS_REQUESTS_FAIL -> Platform.runLater(() -> updateStates(Strings.USER_REQUEST_FRIENDS_REQUESTS_FAIL));
                case USER_REQUEST_FRIENDS_REQUESTS_PENDING_SUCCESS -> Platform.runLater(() -> updateStates(Strings.USER_REQUEST_FRIENDS_REQUESTS_PENDING_SUCCESS));
                case USER_REQUEST_FRIENDS_REQUESTS_PENDING_FAIL -> Platform.runLater(() -> updateStates(Strings.USER_REQUEST_FRIENDS_REQUESTS_PENDING_FAIL));
                case NEW_FRIEND_REQUEST, FRIEND_REQUEST_ACCEPT, FRIEND_REQUEST_CANCEL -> {
                    if (getAtualState() == Situation.SEE_FRIENDS) {
                        Platform.runLater(() -> updateStates(notification.getMsgType()));
                    } else {
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                }
                case REMOVED_FRIEND -> {
                    if (getAtualState() == Situation.MESSAGE || getAtualState() == Situation.SEE_FRIENDS) {
                        Platform.runLater(() -> updateStates(Strings.REMOVED_FRIEND));
                    }
                    notificationMessage = notification.getMsg();
                    Platform.runLater(() -> updateNotification());
                }
                case MESSAGE_SENT_SUCCESS -> Platform.runLater(() -> updateStates(Strings.MESSAGE_SENT_SUCCESS));
                case MESSAGE_SENT_FAIL -> Platform.runLater(() -> updateStates(Strings.MESSAGE_SENT_FAIL));
                case DELETE_MESSAGE_USER -> Platform.runLater(() -> updateStates(notification.getMsgType()));
                case USER_REQUEST_MESSAGES_SUCCESS -> Platform.runLater(() -> updateStates(Strings.USER_REQUEST_MESSAGES_SUCCESS));
                case USER_REQUEST_MESSAGES_FAIL -> Platform.runLater(() -> updateStates(Strings.USER_REQUEST_MESSAGES_FAIL));
                case USER_REQUEST_USER_SUCCESS -> Platform.runLater(() -> updateStates(Strings.USER_REQUEST_USER_SUCCESS));
                case USER_REQUEST_USER_FAIL -> Platform.runLater(() -> updateStates(Strings.USER_REQUEST_USER_FAIL));
                case NEW_MESSAGE_USER -> {
                    if (getAtualState() == Situation.MESSAGE) {
                        Platform.runLater(() -> propertyChangeSupport
                                .firePropertyChange(Strings.NEW_MESSAGE_USER.name(), null, null));
                    } else {
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                }
                case NEW_MESSAGE_GROUP -> {
                    if (getAtualState() == Situation.MESSAGE) {
                        Platform.runLater(() -> propertyChangeSupport
                                .firePropertyChange(Strings.NEW_MESSAGE_GROUP.name(), null, null));
                    } else {
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                }
                case REQUEST_NEW_GROUP_SUCCESS -> Platform.runLater(() -> updateStates(Strings.REQUEST_NEW_GROUP_SUCCESS));
                case REQUEST_NEW_GROUP_FAIL -> Platform.runLater(() -> updateStates(Strings.REQUEST_NEW_GROUP_FAIL));
                case NEW_GROUP -> {
                    if (getAtualState() != Situation.CREATE_GROUP) {
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                    if (getAtualState() == Situation.SEE_GROUPS) {

                    }
                }
                case USER_REQUEST_GROUPS_SUCCESS -> Platform.runLater(() -> updateStates(Strings.USER_REQUEST_GROUPS_SUCCESS));
                case USER_REQUEST_PENDING_GROUPS_SUCCESS -> Platform.runLater(() -> updateStates(Strings.USER_REQUEST_PENDING_GROUPS_SUCCESS));
                case USER_REQUEST_AVAILABLE_GROUPS_SUCCESS -> Platform.runLater(() -> updateStates(Strings.USER_REQUEST_AVAILABLE_GROUPS_SUCCESS));
                case USER_REQUEST_MANAGE_GROUPS_SUCCESS -> Platform.runLater(() -> updateStates(Strings.USER_REQUEST_MANAGE_GROUPS_SUCCESS));
                case NEW_GROUP_REQUEST -> Platform.runLater(() -> updateStates(Strings.NEW_GROUP_REQUEST));
                case DELETED_GROUP -> Platform.runLater(() -> updateStates(Strings.DELETED_GROUP));
                case USER_MANAGE_GROUP_SUCCESS -> Platform.runLater(() -> updateStates(Strings.USER_MANAGE_GROUP_SUCCESS));
                case USER_SEND_FILE_SUCCESS -> uploadFile(notification.getClientRequest().getSelectedMessage());
                default -> {
                    System.out.println("State: " + getAtualState());
                    notificationMessage = notification.getMsg();
                    Platform.runLater(() -> updateNotification());
                }
            }
        }
    }

}
