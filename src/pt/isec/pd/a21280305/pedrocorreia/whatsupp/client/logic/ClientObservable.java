package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ClientObservable {
    private Client client;
    private final PropertyChangeSupport propertyChangeSupport;

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

    public void update() {
        propertyChangeSupport.firePropertyChange("DEBUG", null, null);
    }

    public Situation getAtualState() {
        return client.getAtualState();
    }
}
