package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;

public class UserStatePane extends GridPane {
    private ClientObservable clientObservable;
    private Label welcome;
    private Button searchUsers;
    private Button seeFriends;
    private Button seeFriendsRequests;
    private Button seeFriendsRequestsPending;
    private Button seeGroups;
    private Button createGroup;
    private Button pendingGroups;
    private Button manageGroups;
    private Button availableGroups;

    public UserStatePane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        createView();
        registerObserver();
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("updateView", e -> update());
    }

    public void createView() {
        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));

        welcome = new Label();
        add(welcome, 0, 1);

        searchUsers = new Button("Search Users");
        add(searchUsers, 0, 2);

        seeFriends = new Button("See friends");
        add(seeFriends, 0, 3);

        seeFriendsRequests = new Button("Friends requests sent");
        add(seeFriendsRequests, 1, 3);

        seeFriendsRequestsPending = new Button("Friends requests pending");
        add(seeFriendsRequestsPending, 2, 3);

        seeGroups = new Button("My groups");
        add(seeGroups, 0, 4);

        createGroup = new Button("Create new group");
        add(createGroup, 1, 4);

        manageGroups = new Button("Manage my groups");
        add(manageGroups, 3, 4);

        pendingGroups = new Button("See pending requests");
        add(pendingGroups, 4, 4);
        availableGroups = new Button("Available groups");
        add(availableGroups, 5, 4);

        searchUsers.setOnAction(e -> clientObservable.searchUsers());

        seeFriends.setOnAction(e -> clientObservable.seeFriends());

        seeFriendsRequests.setOnAction(e -> clientObservable.seeFriendsRequests());

        seeFriendsRequestsPending.setOnAction(e -> clientObservable.seeFriendsRequestsPending());

        seeGroups.setOnAction(e -> clientObservable.seeGroups());

        createGroup.setOnAction(e -> clientObservable.createGroup());

        availableGroups.setOnAction(e -> clientObservable.seeAvailableGroups());

        pendingGroups.setOnAction(e -> clientObservable.seePendingGroups());

        manageGroups.setOnAction(e -> clientObservable.seeManageGroups());
    }

    public void update() {
        welcome.setText("Welcome, " + clientObservable.getUser().getName());
    }
}
