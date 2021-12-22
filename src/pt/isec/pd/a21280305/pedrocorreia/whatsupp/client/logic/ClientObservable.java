package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;

public class ClientObservable implements Runnable {
    private Client client;
    private final PropertyChangeSupport propertyChangeSupport;

    private SharedMessage notification = null;
    private String notificationMessage = "";
    private Strings notificationCode;
    // private List<S notifications;

    public ClientObservable(Client client) {
        this.client = client;
        propertyChangeSupport = new PropertyChangeSupport(client);
        propertyChangeSupport.firePropertyChange("DEBUG", null, null);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void createConnection() {
        client.createConnection();
        propertyChangeSupport.firePropertyChange("DEBUG", null, null);
    }

    public void contactServerManager() {
        client.contactServerManager();
        propertyChangeSupport.firePropertyChange("DEBUG", null, null);
    }

    public void initialStatus(String opt) {
        client.initialStatus(opt);
        propertyChangeSupport.firePropertyChange("DEBUG", null, null);
    }

    public void login(String username, String password) {
        client.login(username, password);
        propertyChangeSupport.firePropertyChange("DEBUG", null, null);
    }

    public void register(String username, String password, String confPassword, String fname, String lname) {
        client.register(username, password, confPassword, fname, lname);
        propertyChangeSupport.firePropertyChange("DEBUG", null, null);
    }

    public void userLoggedIn() {
        client.userLoggedIn();
    }

    public String getNotification() {
        return notificationMessage;
        // return client.getNotification();
    }

    public void update() {
        propertyChangeSupport.firePropertyChange("DEBUG", null, null);
    }

    public void updateNotification() {
        propertyChangeSupport.firePropertyChange("notification", null, null);
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
            // notification = null;
            // notificationMessage = "";
            notification = client.getNotification();
            // System.out.println(notification);
            // if (notification.equals("erro")) {
            // System.out.println("Trying another server");
            // client.contactServerManager();
            // Platform.runLater(() -> update());
            // }
            StringBuilder notificationBuilder = new StringBuilder();
            notificationBuilder.append(notification.getMsg());
            if (notification.getMsgType() == Strings.USER_FAILED_LOGIN) {
                notificationBuilder.append(" (" + notification.getMsgType().name() + ")");
                notificationMessage = notificationBuilder.toString();
                // Platform.runLater(() -> update());
            }
            notificationMessage = notificationBuilder.toString();
            System.out.println(notificationMessage);
            Platform.runLater(() -> updateNotification());
            // notification = null;
            // notificationMessage = "";
        }
    }

}
