package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class ClientObservable implements Runnable {
    private Client client;
    private final PropertyChangeSupport propertyChangeSupport;

    private String notification = "";
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
    }

    public String getNotification() {
        return notification;
        // return client.getNotification();
    }

    public void update() {
        propertyChangeSupport.firePropertyChange("DEBUG", null, null);
    }

    public Situation getAtualState() {
        return client.getAtualState();
    }

    // Thread to update the notifications status bar
    @Override
    public void run() {
        while (true) {
            notification = client.getNotification();
            System.out.println(notification);
            if (notification.equals("erro")) {
                System.out.println("Trying another server");
                client.contactServerManager();
                Platform.runLater(() -> update());
            }
        }
    }

}
