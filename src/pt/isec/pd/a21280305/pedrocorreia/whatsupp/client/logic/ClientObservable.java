package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ClientObservable {
    private Client client;
    private final PropertyChangeSupport propertyChangeSupport;

    public ClientObservable(Client client){
        this.client = client;
        propertyChangeSupport = new PropertyChangeSupport(client);
        propertyChangeSupport.firePropertyChange("DEBUG", null, null);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener){
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void initialStatus(String opt) {
        client.initialStatus(opt);
//        propertyChangeSupport.firePropertyChange(Properties.INITIAL_STATE.toString(), null, null);
        propertyChangeSupport.firePropertyChange("DEBUG", null, null);
    }

    public void login(String username, String password) {
        client.login(username, password);
//        propertyChangeSupport.firePropertyChange(Properties.LOGIN_STATE.toString(), null, null);
        propertyChangeSupport.firePropertyChange("DEBUG", null, null);
    }

    public void register(String username, String password, String fname, String lname) { client.register(username, password, fname, lname); }

    public Situation getAtualState() { return client.getAtualState(); }
}

