package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.FriendsRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

public class SeeFriendsStatePane extends GridPane {

    enum PaneState{
        WINDOW, FRIENDS, REQUESTS, PENDING;
    }

    private ClientObservable clientObservable;

    List<FriendsRequests> lFriends;

    PaneState state;

    private Label friends;
    private int method = 0;

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
        state = PaneState.WINDOW;

        friends = new Label("Contact list");
        // add(friends, 0, 1);
    }

    private ContextMenu createContextMenu(Label label, FriendsRequests f) {
        ContextMenu menu = new ContextMenu();
        MenuItem details = new MenuItem("Details");
        menu.getItems().addAll(details);
        String msg = new String("Friend request sent from: " + f.getRequester().getName() + " to "
                + f.getReceiver().getName() + " at "
                + f.getRequestTime() + ".");
        StringBuilder sb = new StringBuilder(msg);
        if (f.getStatus() == 1) {
            sb.append("\nAccepted on " + f.getAnswerTime() + ".");
            long days = Duration.between(f.getRequestTime().toLocalDateTime(), f.getAnswerTime().toLocalDateTime())
                    .toDays();
            sb.append("\nYou're friends for: " + days + "days!");
        }
        details.setOnAction(e -> {
            Alert msgBox = new Alert(Alert.AlertType.INFORMATION);
            msgBox.setTitle("Friend Request");
            msgBox.setHeaderText(sb.toString());
            msgBox.showAndWait();
        });
        return menu;
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("updateView", e -> update());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_FRIENDS_SUCCESS.name(), e -> updateFriends());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_FRIENDS_FAIL.name(), e -> updateFail());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_FRIENDS_REQUESTS_SUCCESS.name(),
                e -> updateFriendsRequests());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_FRIENDS_REQUESTS_PENDING_SUCCESS.name(),
                e -> updateFriendsRequestsPending());
        clientObservable.addPropertyChangeListener(Strings.NEW_FRIEND_REQUEST.name(), e -> updateNewFriend());
        clientObservable.addPropertyChangeListener(Strings.FRIEND_REQUEST_ACCEPT.name(), e -> updateNewFriend());
        clientObservable.addPropertyChangeListener(Strings.FRIEND_REQUEST_CANCEL.name(), e -> updateNewFriend());
        clientObservable.addPropertyChangeListener(Strings.REMOVED_FRIEND.name(), e -> {
            if(state == PaneState.FRIENDS){
                clientObservable.seeFriends();
            }
            if(state == PaneState.REQUESTS){
                clientObservable.seeFriendsRequests();
            }
            if(state == PaneState.PENDING){
                clientObservable.seeFriendsRequestsPending();
            }
        });
    }

    private void update() {
        setVisible(clientObservable.getAtualState() == Situation.SEE_FRIENDS);
    }

    private void updateFriends() {
        getChildren().clear();
        setAlignment(Pos.TOP_LEFT);
        add(friends, 0, 1);
        method = 0;
        state = PaneState.FRIENDS;
        lFriends = clientObservable.getFriendsRequests();
        User me = clientObservable.getUser();
        if (lFriends.size() == 0) {
            Label noFriends = new Label("No friends to list.");
            add(noFriends, 1, 1);
            return;
        }
        for (int i = 0; i < lFriends.size(); i++) {
            Label friend;
            User f;
            if (me.getID() == lFriends.get(i).getReceiver().getID()) {
                f = lFriends.get(i).getRequester();
            } else {
                f = lFriends.get(i).getReceiver();
            }
            friend = new Label(f.getName());
            if (f.getStatus() == 1) {
                friend.setTextFill(Color.GREEN);
            }
            add(friend, 1, i + 1);
            friend.setContextMenu(createContextMenu(friend, lFriends.get(i)));
            Button friendSee = new Button("See messages");
            add(friendSee, 2, i + 1);
            Button delete = new Button("Delete friendship");
            add(delete, 3, i + 1);

            friendSee.setOnAction(e -> {
                clientObservable.seeMessages(f);
            });

            delete.setOnAction(e -> {
                clientObservable.deleteFriendship(f);
                method = 0;
            });
        }
    }

    private void updateFriendsRequests() {
        getChildren().clear();
        setAlignment(Pos.TOP_LEFT);
        add(friends, 0, 1);
        state = PaneState.REQUESTS;
        // lFriends =
        // clientObservable.getNotificationSM().getClientRequest().getFriendsRequests();
        lFriends = clientObservable.getFriendsRequestsSent();
        User me = clientObservable.getUser();
        method = 1;
        if (lFriends.size() == 0) {
            Label noFriends = new Label("No friend requests sent.");
            add(noFriends, 1, 1);
            return;
        }
        for (int i = 0; i < lFriends.size(); i++) {
            Label friend;
            User f;
            if (me.getID() == lFriends.get(i).getReceiver().getID()) {
                f = lFriends.get(i).getRequester();
            } else {
                f = lFriends.get(i).getReceiver();
            }
            friend = new Label(f.getName());
            add(friend, 1, i + 1);
            friend.setContextMenu(createContextMenu(friend, lFriends.get(i)));
            Button cancel = new Button("Cancel request");
            add(cancel, 2, i + 1);

            cancel.setOnAction(e -> {
                method = 1;
                clientObservable.cancelRequest(f);
            });
        }
    }

    private void updateFriendsRequestsPending() {
        getChildren().clear();
        setAlignment(Pos.TOP_LEFT);
        add(friends, 0, 1);
        state = PaneState.PENDING;
        // lFriends =
        // clientObservable.getNotificationSM().getClientRequest().getFriendsRequests();
        lFriends = clientObservable.getFriendsRequestsPending();
        User me = clientObservable.getUser();
        method = 2;
        if (lFriends.size() == 0) {
            Label noFriends = new Label("No friend requests received.");
            add(noFriends, 1, 1);
            return;
        }
        for (int i = 0; i < lFriends.size(); i++) {
            Label friend;
            User f;
            if (me.getID() == lFriends.get(i).getReceiver().getID()) {
                f = lFriends.get(i).getRequester();
            } else {
                f = lFriends.get(i).getReceiver();
            }
            friend = new Label(f.getName());
            add(friend, 1, i + 1);
            friend.setContextMenu(createContextMenu(friend, lFriends.get(i)));
            Button accept = new Button("Accept friendship");
            add(accept, 2, i + 1);

            accept.setOnAction(e -> {
                method = 2;
                clientObservable.acceptRequest(f);
            });
        }
    }

    private void updateNewFriend() {
        System.out.println(state);
        switch(state){
            case FRIENDS -> clientObservable.seeFriends();
            case REQUESTS -> clientObservable.seeFriendsRequests();
            case PENDING -> clientObservable.seeFriendsRequestsPending();
        }
    }

    private void updateFail() {
        Label failed = new Label("No friends available");
        add(failed, 0, 1);
    }

}
