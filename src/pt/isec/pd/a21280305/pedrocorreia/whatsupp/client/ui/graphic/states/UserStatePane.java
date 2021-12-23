package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;

public class UserStatePane extends BorderPane {
    private ClientObservable clientObservable;
    private Label debug;

    public UserStatePane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        createView();
        registerObserver();
        // update();
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("DEBUG", e -> update());
    }

    public void createView() {
        // clientObservable.userLoggedIn();
        debug = new Label("");
        setCenter(debug);
    }

    public void update() {
        debug.setText(clientObservable.getUser().toString());
        // clientObservable.userLoggedIn();
    }
}
