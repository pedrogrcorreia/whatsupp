package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;

public class UserStatePane extends BorderPane {
    private ClientObservable clientObservable;
    private Label welcome;
    private Button listAllUsers;
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
        Background btBkg = new Background(new BackgroundFill(Color.rgb(180, 180, 180), new CornerRadii(10), Insets.EMPTY));
        Font lblFont = new Font(20);
        Font btnFont = new Font(15);

        setPadding(new Insets(25, 25, 25, 25));

        HBox wlc = new HBox(10);
        welcome = new Label();
        welcome.setFont(new Font(25));
        wlc.setAlignment(Pos.TOP_CENTER);
        wlc.getChildren().add(welcome);

        VBox btns = new VBox(30);

        VBox u = new VBox(10);
        u.setAlignment(Pos.BOTTOM_CENTER);

        Label users = new Label("USERS");
        users.setFont(lblFont);
        searchUsers = new Button("Search");
        searchUsers.setBackground(btBkg);
        searchUsers.setPrefWidth(100);
        searchUsers.setFont(btnFont);

        listAllUsers = new Button("All");
        listAllUsers.setBackground(btBkg);
        listAllUsers.setPrefWidth(100);
        listAllUsers.setFont(btnFont);

        u.getChildren().addAll(users, searchUsers, listAllUsers);

        VBox f = new VBox(10);
        f.setAlignment(Pos.BOTTOM_CENTER);

        Label friends = new Label("FRIENDS");
        friends.setFont(lblFont);

        seeFriends = new Button("Your friends");
        seeFriends.setBackground(btBkg);
        seeFriends.setPrefWidth(100);
        seeFriends.setFont(btnFont);

        seeFriendsRequests = new Button("Requests sent");
        seeFriendsRequests.setBackground(btBkg);
        seeFriendsRequests.setPrefWidth(100);
        seeFriendsRequests.setWrapText(true);
        seeFriendsRequests.setFont(btnFont);
        seeFriendsRequests.setTextAlignment(TextAlignment.CENTER);

        seeFriendsRequestsPending = new Button("Requests pending");
        seeFriendsRequestsPending.setBackground(btBkg);
        seeFriendsRequestsPending.setPrefWidth(100);
        seeFriendsRequestsPending.setWrapText(true);
        seeFriendsRequestsPending.setFont(btnFont);
        seeFriendsRequestsPending.setTextAlignment(TextAlignment.CENTER);

        f.getChildren().addAll(friends, seeFriends, seeFriendsRequests, seeFriendsRequestsPending);

        VBox g = new VBox(10);
        g.setAlignment(Pos.TOP_CENTER);

        Label groups = new Label("GROUPS");
        groups.setFont(new Font(20));

        seeGroups = new Button("My groups");
        seeGroups.setBackground(btBkg);
        seeGroups.setPrefWidth(100);
        seeGroups.setFont(btnFont);

        createGroup = new Button("Create new group");
        createGroup.setBackground(btBkg);
        createGroup.setPrefWidth(100);
        createGroup.setWrapText(true);
        createGroup.setFont(btnFont);
        createGroup.setTextAlignment(TextAlignment.CENTER);

        manageGroups = new Button("Manage");
        manageGroups.setBackground(btBkg);
        manageGroups.setPrefWidth(100);
        manageGroups.setFont(btnFont);
        manageGroups.setWrapText(true);

        pendingGroups = new Button("Pending requests");
        pendingGroups.setBackground(btBkg);
        pendingGroups.setPrefWidth(100);
        pendingGroups.setWrapText(true);
        pendingGroups.setFont(btnFont);
        pendingGroups.setTextAlignment(TextAlignment.CENTER);

        availableGroups = new Button("Available");
        availableGroups.setBackground(btBkg);
        availableGroups.setFont(btnFont);
        availableGroups.setPrefWidth(100);

        g.getChildren().addAll(groups, seeGroups, createGroup, manageGroups, pendingGroups, availableGroups);

        searchUsers.setOnAction(e -> clientObservable.searchUsers());

        listAllUsers.setOnAction(e -> clientObservable.getAllUsers());

        seeFriends.setOnAction(e -> clientObservable.seeFriends());

        seeFriendsRequests.setOnAction(e -> clientObservable.seeFriendsRequests());

        seeFriendsRequestsPending.setOnAction(e -> clientObservable.seeFriendsRequestsPending());

        seeGroups.setOnAction(e -> clientObservable.seeGroups());

        createGroup.setOnAction(e -> clientObservable.createGroup());

        availableGroups.setOnAction(e -> clientObservable.seeAvailableGroups());

        pendingGroups.setOnAction(e -> clientObservable.seePendingGroups());

        manageGroups.setOnAction(e -> clientObservable.seeManageGroups());

        btns.setAlignment(Pos.CENTER);

        btns.getChildren().addAll(u,f,g);

        setTop(wlc);
        setCenter(btns);

        setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    }

    public void update() {
        welcome.setText("Welcome, " /*+ clientObservable.getUser().getName()*/);
    }
}
