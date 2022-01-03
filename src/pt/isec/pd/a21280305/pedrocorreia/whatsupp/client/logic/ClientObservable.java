package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.List;

import javafx.application.Platform;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.*;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states.State;

public class ClientObservable implements Runnable {

    private Client client;
    private final PropertyChangeSupport propertyChangeSupport;

    private SharedMessage notification = null;
    private String notificationMessage = "";

    private State state;

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

    public void updateStates(State msg){ propertyChangeSupport.firePropertyChange(msg.name(), null, null);}

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

    public void updateUser(User u){
        client.updateUser(u);
    }

    /**
     * Search users
     */

    public void searchUsers() {
        client.searchUsers();
        forceUpdate();
    }

    public void getAllUsers() {
        client.getAllUsers();
        forceUpdate();
    }

    public void searchUser(String username) {
        client.searchUser(username);
    }

    /** Friends */

    public void seeFriends() {
        state = State.FRIENDS;
        client.seeFriends();
        forceUpdate();
    }

    public void seeFriendsRequests() {
        state = State.REQUESTS;
        client.seeFriendsRequests();
        forceUpdate();
    }

    public void seeFriendsRequestsPending() {
        state = State.PENDING;
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
        state = State.GROUP;
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
        state = State.AVAILABLE;
        client.seeAvailableGroups();
        forceUpdate();
    }

    public void seePendingGroups(){
        state = State.PENDING;
        client.seePendingGroups();
        forceUpdate();
    }

    public void seeManageGroups(){
        state = State.MANAGE;
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
        state = State.USER;
        forceUpdate();
    }

    public void seeMessages(Group group){
        client.seeMessages(group);
        state = State.GROUP;
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

    public void sendFileToGroup(Message f){ client.sendFileToGroup(f);}

    public void uploadFile(Message file){ client.uploadFile(file); }

    public void downloadFile(Message file, File path){ client.downloadFile(file, path); }

    public void deleteFile(Message file){ client.deleteFile(file); }

    /**
     * Back to start state
     */

    public void back() {
        client.back();
        forceUpdate();
    }

    public void backToInitialState(){
        client.backToInitialState();
        forceUpdate();
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

    public State getState(){ return state; }

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

                /** Messages PANE */
                case MESSAGE_DELETE_SUCCESS, MESSAGE_SENT_SUCCESS,
                        USER_REQUEST_MESSAGES_SUCCESS, USER_DELETE_FILE_SUCCESS -> Platform.runLater(() -> updateStates(notification.getMsgType()));
                case NEW_MESSAGE_GROUP, NEW_FILE_SENT_GROUP, DELETE_MESSAGE_GROUP,
                        FILE_REMOVED_GROUP -> {
                    if(state == State.GROUP){
                        Platform.runLater(() -> updateStates(notification.getMsgType()));
                    }
                    else{
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                }
                case NEW_MESSAGE_USER, NEW_FILE_SENT_USER, DELETE_MESSAGE_USER,
                        FILE_REMOVED_USER -> {
                    if(state == State.USER){
                        Platform.runLater(() -> updateStates(notification.getMsgType()));
                    }
                    else{
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                }
                case USER_SEND_FILE_SUCCESS -> uploadFile(notification.getClientRequest().getSelectedMessage());

                /** Friends PANE */
                case USER_REQUEST_FRIENDS_SUCCESS,
                        USER_REQUEST_FRIENDS_REQUESTS_SUCCESS,
                        USER_REQUEST_FRIENDS_REQUESTS_PENDING_SUCCESS -> Platform.runLater(() -> updateStates(notification.getMsgType()));
                case NEW_FRIEND_REQUEST, USER_ACCEPT_FRIEND_REQUEST_SUCCESS,
                        USER_CANCEL_FRIEND_REQUEST_SUCCESS->{
                    if(state == State.REQUESTS || state == State.PENDING){
                        Platform.runLater(() -> updateStates(notification.getMsgType()));
                    }
                    else{
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                }
                case FRIEND_REQUEST_ACCEPT, FRIEND_REQUEST_CANCEL -> {
                    if(state == State.FRIENDS || state == State.PENDING || state == State.REQUESTS){
                        Platform.runLater(() -> updateStates(notification.getMsgType()));
                    }
                    else{
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                }
                case REMOVED_FRIEND, USER_CANCEL_FRIENDSHIP_SUCCESS -> {
                    if(state == State.FRIENDS || state == State.USER){
                        Platform.runLater(() -> updateStates(notification.getMsgType()));
                    }
                    else{
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                }
                case USER_REQUEST_ALL_USERS_SUCCESS -> Platform.runLater(() -> updateStates(notification.getMsgType()));

                /** Groups PANE */
                case USER_REQUEST_GROUPS_SUCCESS, USER_REQUEST_AVAILABLE_GROUPS_SUCCESS,
                        USER_REQUEST_PENDING_GROUPS_SUCCESS,
                        USER_REQUEST_MANAGE_GROUPS_SUCCESS -> Platform.runLater(() -> updateStates(notification.getMsgType()));
                case QUIT_GROUP -> {
                    if(state == State.MANAGE || state == State.GROUP){
                        Platform.runLater(() -> updateStates(notification.getMsgType()));
                    }
                    else{
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                }
                case DELETED_GROUP -> Platform.runLater(() -> updateStates(notification.getMsgType()));
                case NEW_GROUP_REQUEST -> {
                    if(state == State.MANAGE){
                        Platform.runLater(() -> updateStates(notification.getMsgType()));
                    }
                    else{
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                }
                case ACCEPTED_GROUP_REQUEST -> {
                    if(state == State.GROUP || state == State.PENDING){
                        Platform.runLater(() -> updateStates(notification.getMsgType()));
                    }
                    else{
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                }
                case USER_QUIT_GROUP_SUCCESS, USER_DELETE_GROUP_SUCCESS, USER_CHANGE_GROUP_SUCCESS,
                        USER_MANAGE_GROUP_SUCCESS, ADMIN_ACCEPT_GROUP_REQUEST_SUCCESS ->
                        Platform.runLater(() -> updateStates(notification.getMsgType()));
                case USER_UPDATE_INFO_SUCCESS -> Platform.runLater(() -> updateStates(notification.getMsgType()));
                default -> {
                    notificationMessage = notification.getMsg();
                    Platform.runLater(() -> updateNotification());
                }
            }
        }
    }
}
