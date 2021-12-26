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
    private Button seeFriendsRequests;
    private Button seeFriends;
    private Button seeGroups;
    private Button createGroup;
    private Button addGroups;

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

        seeFriendsRequests = new Button("Friends requests");
        add(seeFriendsRequests, 1, 3);

        seeGroups = new Button("See groups");
        add(seeGroups, 0, 4);

        createGroup = new Button("Create new group");
        add(createGroup, 1, 4);

        addGroups = new Button("Enter a group");
        add(addGroups, 2, 4);

        searchUsers.setOnAction(e -> clientObservable.searchUsers());

        seeFriends.setOnAction(e -> clientObservable.seeFriends());

        seeFriendsRequests.setOnAction(e -> clientObservable.seeFriendsRequests());

        seeGroups.setOnAction(e -> clientObservable.seeGroups());

        createGroup.setOnAction(e -> clientObservable.createGroup());

        addGroups.setOnAction(e -> clientObservable.addGroups());

    }

    public void update() {
        welcome.setText("Welcome, " + clientObservable.getUser().getName());
    }
}
