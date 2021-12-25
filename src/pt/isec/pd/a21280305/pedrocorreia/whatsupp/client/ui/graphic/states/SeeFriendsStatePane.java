package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientRequestFriends;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.User;

public class SeeFriendsStatePane extends GridPane {

    private ClientObservable clientObservable;

    List<User> lFriends;

    private Label friends;

    public SeeFriendsStatePane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        lFriends = new ArrayList<>();
        createWindow();
        registerObserver();
        update();
    }

    private void createWindow() {
        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));

        friends = new Label("Contact list");
        // add(friends, 0, 1);
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("updateView", e -> update());
        clientObservable.addPropertyChangeListener("success", e -> updateSuccess());
        clientObservable.addPropertyChangeListener("fail", e -> updateFail());
    }

    private void update() {
        setVisible(clientObservable.getAtualState() == Situation.SEE_FRIENDS);
    }

    private void updateSuccess() {
        getChildren().clear();
        setAlignment(Pos.TOP_LEFT);
        add(friends, 0, 1);
        lFriends = ((ClientRequestFriends) clientObservable.getNotificationSM().getClientServerConnection())
                .getFriends();
        for (int i = 0; i < lFriends.size(); i++) {
            Label friend = new Label(lFriends.get(i).getUsername());
            add(friend, 1, i + 1);
            Button friendSee = new Button("See messages");
            add(friendSee, 2, i + 1);

            friendSee.setOnAction(e -> {
                // Label debug = new Label(lFriends.get(getRowIndex(friendSee)).getUsername());
                clientObservable.seeMessages(lFriends.get(getRowIndex(friendSee) - 1));
                // clientObservable.seeMessages(2);
                // add(debug, 10, 10);
            });
        }
    }

    private void updateFail() {
        Label failed = new Label("No friends available");
        add(failed, 0, 1);
    }

}
