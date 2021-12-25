package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.User;

public class SearchUsersPane extends GridPane {

    private ClientObservable clientObservable;

    private Label search;
    private TextField searchBox;
    private Button submit;
    private Label user;
    private Button add;

    public SearchUsersPane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        createWindow();
        registerObserver();
        update();
    }

    private void createWindow() {
        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));

        search = new Label("Search username: ");
        add(search, 0, 1);
        searchBox = new TextField();
        add(searchBox, 1, 1);

        submit = new Button("Submit");
        add(submit, 0, 2);

        user = new Label();
        add(user, 0, 4);

        add = new Button("Add friend");
        add(add, 1, 4);

        user.setVisible(false);
        add.setVisible(false);

        submit.setOnAction(e -> clientObservable.searchUser(searchBox.getText()));

    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("updateView", e -> update());
        clientObservable.addPropertyChangeListener("success", e -> updateSuccess());
        clientObservable.addPropertyChangeListener("fail", e -> updateFail());
    }

    private void update() {
        setVisible(clientObservable.getAtualState() == Situation.SEARCH_USERS);
    }

    private void updateSuccess() {
        User foundUser = clientObservable.getNotificationUser();
        user.setText(foundUser.getUsername());
        user.setVisible(true);
        add.setVisible(true);
        add.setOnAction(e -> clientObservable.addFriend(foundUser.getID()));
    }

    private void updateFail() {
        user.setText(clientObservable.getNotification());
        user.setVisible(true);
        add.setVisible(false);
    }
}
