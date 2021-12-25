package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javafx.application.Platform;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.User;

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

    public void addFriend(int userID) {
        client.addFriend(userID);
    }

    public void userLoggedIn() {
        client.userLoggedIn();
    }

    public User getUser() {
        return client.getUser();
    }

    public void back() {
        client.back();
        propertyChangeSupport.firePropertyChange("updateView", null, null);
    }

    public String getNotification() {
        return notificationMessage;
    }

    public User getNotificationUser() {
        return notification.getClientServerConnection().getUser();
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

    // Thread to update the notifications status bar
    /**
     * Thread to check for notifications
     * Only call update when a notification is received.
     * When something that change states is received, don't.
     */
    @Override
    public void run() {
        while (true) {
            // notification = client.getNotification();
            // StringBuilder notificationBuilder = new StringBuilder();
            // notificationBuilder.append(notification.getMsg());
            // if (notification.getMsgType() == Strings.USER_FAILED_LOGIN) {
            // notificationBuilder.append(" (" + notification.getMsgType().name() + ")");
            // notificationMessage = notificationBuilder.toString();
            // }
            // notificationMessage = notificationBuilder.toString();
            // System.out.println(notificationMessage);
            // Platform.runLater(() -> updateNotification());
            notification = client.getNotification();
            System.out.println(notification);
            switch (notification.getMsgType()) {
                case CLIENT_REQUEST_SERVER:
                    notificationMessage = notification.getMsg();
                    Platform.runLater(() -> updateNotification());
                    break;
                case MESSAGE_SENT_FAIL:
                    break;
                case MESSAGE_SENT_SUCCESS:
                    break;
                case USER_FAILED_LOGIN:
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
                case USER_REQUEST_FRIENDS:
                    break;
                case USER_REQUEST_FRIENDS_FAIL:
                    Platform.runLater(() -> propertyChangeSupport.firePropertyChange("fail", null, null));
                    break;
                case USER_REQUEST_FRIENDS_SUCCESS:
                    Platform.runLater(() -> propertyChangeSupport.firePropertyChange("success", null, null));
                    break;
                case USER_REQUEST_GROUPS:
                    break;
                case USER_REQUEST_GROUPS_FAIL:
                    break;
                case USER_REQUEST_GROUPS_SUCCESS:
                    break;
                case USER_REQUEST_INFO_FAIL:
                    break;
                case USER_REQUEST_INFO_SUCCESS:
                    break;
                case USER_REQUEST_LOGIN:
                    break;
                case USER_REQUEST_MESSAGES:
                    break;
                case USER_REQUEST_MESSAGES_FAIL:
                    Platform.runLater(() -> propertyChangeSupport.firePropertyChange("messagesUpdateFail", null, null));
                    break;
                case USER_REQUEST_MESSAGES_SUCCESS:
                    Platform.runLater(
                            () -> propertyChangeSupport.firePropertyChange("messagesUpdateSuccess", null, null));
                    break;
                case USER_REQUEST_OWN_INFO:
                    break;
                case USER_REQUEST_OWN_INFO_FAIL:
                    break;
                case USER_REQUEST_OWN_INFO_SUCCESS:
                    break;
                case USER_REQUEST_REGISTER:
                    break;
                case USER_REQUEST_USER:
                    break;
                case USER_REQUEST_USER_FAIL:
                    notificationMessage = notification.getMsg();
                    Platform.runLater(() -> propertyChangeSupport.firePropertyChange("fail", null, null));
                    break;
                case USER_REQUEST_USER_SUCCESS:
                    Platform.runLater(() -> propertyChangeSupport.firePropertyChange("success", null, null));
                    break;
                case USER_SENT_MESSAGE:
                    break;
                case USER_SUCCESS_LOGIN:
                    notificationMessage = notification.getMsg();
                    Platform.runLater(() -> updateNotification());
                    break;
                default:
                    notificationMessage = notification.getMsg();
                    Platform.runLater(() -> updateNotification());
                    break;
            }
        }
    }

}
