package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.FriendsRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

public class SeeFriendsStatePane extends BorderPane {

//    enum PaneState{
//        WINDOW, FRIENDS, REQUESTS, PENDING;
//    }

    private ClientObservable clientObservable;

    List<FriendsRequests> lFriends;

    private ScrollPane scrollPane;
    private GridPane gridPane;
    private HBox title;

//    PaneState state;

    Background btBkg = new Background(new BackgroundFill(Color.rgb(180, 180, 180), new CornerRadii(10), Insets.EMPTY));
    Font lblFont = new Font(20);
    Font btnFont = new Font(15);

    private Label friends;
    private Label noFriends;
    private Label friend;
    private Button friendSee;
    private Button delete;
    private Button cancel;
    private Button accept;
    private int method = 0;

    public SeeFriendsStatePane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        lFriends = new ArrayList<>();
        createWindow();
        registerObserver();
        update();
    }

    private void createWindow() {


        gridPane = new GridPane();
        scrollPane = new ScrollPane();

        setPadding(new Insets(25, 25, 25, 25));
//        state = PaneState.WINDOW;

        title = new HBox(10);

        friends = new Label("Contact list");
        friends.setFont(lblFont);
        title.setAlignment(Pos.TOP_CENTER);
        title.getChildren().add(friends);

        setTop(title);
        scrollPane.setContent(gridPane);
        setCenter(scrollPane);



        cancel = new Button("Cancel");
        cancel.setFont(lblFont);
        cancel.setBackground(btBkg);

        accept = new Button("Accept");
        accept.setFont(lblFont);
        accept.setBackground(btBkg);
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
//            if(state == PaneState.FRIENDS){
//                clientObservable.seeFriends();
//            }
//            if(state == PaneState.REQUESTS){
//                clientObservable.seeFriendsRequests();
//            }
//            if(state == PaneState.PENDING){
//                clientObservable.seeFriendsRequestsPending();
//            }
        });
//        clientObservable.addPropertyChangeListener(Strings.);
//        clientObservable.addPropertyChangeListener(State.FRIENDS.name(), e -> updateFriends());
//        clientObservable.addPropertyChangeListener(State.REQUESTS.name(), e -> updateFriendsRequests());
//        clientObservable.addPropertyChangeListener(State.PENDING.name(), e -> updateFriendsRequestsPending());
    }

    private void update() {
        setVisible(clientObservable.getAtualState() == Situation.SEE_FRIENDS);
    }

    private void updateFriends() {
        getChildren().clear();
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setHgap(15);
        gridPane.setVgap(15);

        method = 0;
//        state = PaneState.FRIENDS;
        lFriends = clientObservable.getFriendsRequests();
        User me = clientObservable.getUser();
        if (lFriends.size() == 0) {
            noFriends = new Label("No friends to list.");
            gridPane.add(noFriends, 1, 1);
            return;
        }

        for (int i = 0; i < lFriends.size(); i++) {
            User f;
            Circle circle = new Circle(4, 4, 4);

            friendSee = new Button("Messages");
            friendSee.setFont(btnFont);
            friendSee.setBackground(btBkg);

            delete = new Button("Delete");
            delete.setFont(btnFont);
            delete.setBackground(btBkg);

            if (me.getID() == lFriends.get(i).getReceiver().getID()) {
                f = lFriends.get(i).getRequester();
            } else {
                f = lFriends.get(i).getReceiver();
            }

            friend = new Label(f.getName());
            friend.setFont(lblFont);
            if (f.getStatus() == 1) {
                circle.setFill(Color.GREEN);
            }
            else{
                circle.setFill(Color.RED);
            }

            gridPane.add(friend, 0, i + 1);
            friend.setContextMenu(createContextMenu(friend, lFriends.get(i)));
            gridPane.add(circle, 1, i+1);
            gridPane.add(friendSee, 2, i + 1);
            gridPane.add(delete, 3, i + 1);

            friendSee.setOnAction(e -> {
                clientObservable.seeMessages(f);
            });

            delete.setOnAction(e -> {
                clientObservable.deleteFriendship(f);
                method = 0;
            });
        }

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(gridPane);
        setTop(title);
        setCenter(scrollPane);
    }

    private void updateFriendsRequests() {
        getChildren().clear();
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.CENTER);

//        state = PaneState.REQUESTS;
        lFriends = clientObservable.getFriendsRequestsSent();
        User me = clientObservable.getUser();
        method = 1;
        if (lFriends.size() == 0) {
            noFriends = new Label("No friend requests sent.");
            gridPane.add(noFriends, 1, 1);
            return;
        }
        for (int i = 0; i < lFriends.size(); i++) {
            User f;
            if (me.getID() == lFriends.get(i).getReceiver().getID()) {
                f = lFriends.get(i).getRequester();
            } else {
                f = lFriends.get(i).getReceiver();
            }
            friend = new Label(f.getName());
            friend.setFont(lblFont);
            gridPane.add(friend, 1, i + 1);
            friend.setContextMenu(createContextMenu(friend, lFriends.get(i)));
            cancel = new Button("Cancel");
            cancel.setFont(btnFont);
            cancel.setBackground(btBkg);
            gridPane.add(cancel, 2, i + 1);

            cancel.setOnAction(e -> {
                method = 1;
                clientObservable.cancelRequest(f);
            });
        }
    }

    private void updateFriendsRequestsPending() {
        getChildren().clear();
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.CENTER);

//        state = PaneState.PENDING;
        lFriends = clientObservable.getFriendsRequestsPending();
        User me = clientObservable.getUser();
        method = 2;
        if (lFriends.size() == 0) {
            noFriends = new Label("No friend requests received.");
            gridPane.add(noFriends, 1, 1);
            return;
        }
        for (int i = 0; i < lFriends.size(); i++) {
            User f;
            if (me.getID() == lFriends.get(i).getReceiver().getID()) {
                f = lFriends.get(i).getRequester();
            } else {
                f = lFriends.get(i).getReceiver();
            }
            friend = new Label(f.getName());
            friend.setFont(lblFont);
            gridPane.add(friend, 1, i + 1);
            friend.setContextMenu(createContextMenu(friend, lFriends.get(i)));
            accept = new Button("Accept");
            accept.setFont(btnFont);
            accept.setBackground(btBkg);
            gridPane.add(accept, 2, i + 1);

            accept.setOnAction(e -> {
                method = 2;
                clientObservable.acceptRequest(f);
            });
        }
    }

    private void updateNewFriend() {
//        System.out.println(state);
        switch(clientObservable.getState()){
            case FRIENDS -> clientObservable.seeFriends();
            case REQUESTS -> clientObservable.seeFriendsRequests();
            case PENDING -> clientObservable.seeFriendsRequestsPending();
        }
    }

    private void updateFail() {
        Label failed = new Label("No friends available");
        gridPane.add(failed, 0, 1);
    }

}
