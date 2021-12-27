package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import javafx.application.Platform;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.FriendsRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

public class ClientObservable implements Runnable {
    private Client client;
    private final PropertyChangeSupport propertyChangeSupport;

    private SharedMessage notification = null;
    private String notificationMessage = "";

    public ClientObservable(Client client) {
        this.client = client;
        propertyChangeSupport = new PropertyChangeSupport(client);
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void createConnection() {
        client.createConnection();
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void contactServerManager() {
        client.contactServerManager();
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void initialStatus(String opt) {
        client.initialStatus(opt);
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void login(String username, String password) {
        client.login(username, password);
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void register(String username, String password, String confPassword, String fname, String lname) {
        client.register(username, password, confPassword, fname, lname);
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void searchUsers() {
        client.searchUsers();
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void seeFriends() {
        client.seeFriends();
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void seeGroups() {
        client.seeGroups();
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void seeMessages(User user) {
        client.seeMessages(user);
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void searchUser(String username) {
        client.searchUser(username);
    }

    public void addFriend(User user) {
        client.addFriend(user);
    }

    public void acceptRequest(User user) {
        client.acceptRequest(user);
    }

    public void cancelRequest(User user) {
        client.cancelRequest(user);
    }

    public void deleteFriendship(User user) {
        client.deleteFriendship(user);
    }

    public User getUser() {
        return client.getUser();
    }

    public User getFriend() {
        return client.getFriend();
    }

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

    public void back() {
        client.back();
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void deleteMessage(Message msg) {
        client.deleteMessage(msg);
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void sendMessage(Message msg) {
        client.sendMessage(msg);
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void seeFriendsRequests() {
        client.seeFriendsRequests();
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void seeFriendsRequestsPending() {
        client.seeFriendsRequestsPending();
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void createGroup() {
        client.createGroup();
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void addGroups() {
        client.addGroups();
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public String getNotification() {
        return notificationMessage;
    }

    public User getNotificationUser() {
        return notification.getClientRequest().getSelectedUser();
    }

    public SharedMessage getNotificationSM() {
        return notification;
    }

    public void update() {
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public void updateNotification() {
        propertyChangeSupport.firePropertyChange("notification", null, null);
    }

    public void updateScreen() {
        propertyChangeSupport.firePropertyChange("viewChange", null, null);
    }

    public Situation getAtualState() {
        return client.getAtualState();
    }

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
                case CLIENT_REQUEST_SERVER:
                    notificationMessage = notification.getMsg();
                    Platform.runLater(() -> updateNotification());
                    break;
                case USER_FAILED_LOGIN:
                    notificationMessage = notification.getMsg();
                    Platform.runLater(() -> updateNotification());
                    break;
                case USER_SUCCESS_LOGIN:
                    notificationMessage = notification.getMsg();
                    Platform.runLater(() -> updateNotification());
                    break;
                case USER_REGISTER_FAIL:
                    notificationMessage = notification.getMsg();
                    Platform.runLater(() -> updateNotification());
                    break;
                case USER_REGISTER_SUCCESS:
                    notificationMessage = notification.getMsg();
                    Platform.runLater(() -> updateNotification());
                    break;
                case USER_REQUEST_FRIENDS_FAIL:
                    Platform.runLater(() -> propertyChangeSupport
                            .firePropertyChange(Strings.USER_REQUEST_FRIENDS_FAIL.name(), null, null));
                    break;
                case USER_REQUEST_FRIENDS_SUCCESS:
                    Platform.runLater(() -> propertyChangeSupport
                            .firePropertyChange(Strings.USER_REQUEST_FRIENDS_SUCCESS.name(), null, null));
                    break;
                case USER_REQUEST_FRIENDS_REQUESTS_SUCCESS:
                    Platform.runLater(() -> propertyChangeSupport
                            .firePropertyChange(Strings.USER_REQUEST_FRIENDS_REQUESTS_SUCCESS.name(), null, null));
                    break;
                case USER_REQUEST_FRIENDS_REQUESTS_FAIL:
                    Platform.runLater(() -> propertyChangeSupport
                            .firePropertyChange(Strings.USER_REQUEST_FRIENDS_REQUESTS_FAIL.name(), null, null));
                    break;
                case USER_REQUEST_FRIENDS_REQUESTS_PENDING_SUCCESS:
                    Platform.runLater(() -> propertyChangeSupport.firePropertyChange(
                            Strings.USER_REQUEST_FRIENDS_REQUESTS_PENDING_SUCCESS.name(), null, null));
                    break;
                case USER_REQUEST_FRIENDS_REQUESTS_PENDING_FAIL:
                    Platform.runLater(() -> propertyChangeSupport.firePropertyChange(
                            Strings.USER_REQUEST_FRIENDS_REQUESTS_PENDING_FAIL.name(), null, null));
                    break;
                case NEW_FRIEND:
                    if (getAtualState() == Situation.SEE_FRIENDS) {
                        Platform.runLater(() -> propertyChangeSupport
                                .firePropertyChange(Strings.NEW_FRIEND.name(), null, null));
                    } else {
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                    break;
                case REMOVED_FRIEND:
                    if (getAtualState() == Situation.MESSAGE) {
                        Platform.runLater(() -> propertyChangeSupport
                                .firePropertyChange(Strings.REMOVED_FRIEND.name(), null, null));
                    } else if (getAtualState() == Situation.SEE_FRIENDS) {
                        Platform.runLater(() -> propertyChangeSupport
                                .firePropertyChange(Strings.REMOVED_FRIEND.name(), null, null));
                    } else {
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                case MESSAGE_SENT_FAIL:
                    Platform.runLater(() -> propertyChangeSupport.firePropertyChange(Strings.MESSAGE_SENT_FAIL.name(),
                            null, null));
                    break;
                case MESSAGE_SENT_SUCCESS:
                    Platform.runLater(() -> propertyChangeSupport
                            .firePropertyChange(Strings.MESSAGE_SENT_SUCCESS.name(), null, null));
                    break;
                case USER_REQUEST_MESSAGES_FAIL:
                    Platform.runLater(() -> propertyChangeSupport
                            .firePropertyChange(Strings.USER_REQUEST_MESSAGES_FAIL.name(), null, null));
                    break;
                case USER_REQUEST_MESSAGES_SUCCESS:
                    Platform.runLater(
                            () -> propertyChangeSupport.firePropertyChange(Strings.USER_REQUEST_MESSAGES_SUCCESS.name(),
                                    null, null));
                    break;
                case USER_REQUEST_USER_FAIL:
                    notificationMessage = notification.getMsg();
                    Platform.runLater(() -> propertyChangeSupport
                            .firePropertyChange(Strings.USER_REQUEST_USER_FAIL.name(), null, null));
                    break;
                case USER_REQUEST_USER_SUCCESS:
                    Platform.runLater(() -> propertyChangeSupport
                            .firePropertyChange(Strings.USER_REQUEST_USER_SUCCESS.name(), null, null));
                    break;
                case USER_SENT_MESSAGE:
                    break;
                case NEW_MESSAGE:
                    if (getAtualState() == Situation.MESSAGE) {
                        Platform.runLater(() -> propertyChangeSupport
                                .firePropertyChange(Strings.NEW_MESSAGE.name(), null, null));
                    } else {
                        notificationMessage = notification.getMsg();
                        Platform.runLater(() -> updateNotification());
                    }
                    break;
                default:
                    System.out.println("State: " + getAtualState());
                    notificationMessage = notification.getMsg();
                    Platform.runLater(() -> updateNotification());
                    break;
            }
        }
    }

}
