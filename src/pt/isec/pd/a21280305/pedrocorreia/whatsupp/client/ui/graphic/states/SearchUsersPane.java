package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

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
        Background btBkg = new Background(new BackgroundFill(Color.rgb(180, 180, 180), new CornerRadii(10), Insets.EMPTY));
        Font lblFont = new Font(15);

        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));

        search = new Label("Search username: ");
        search.setFont(lblFont);
        add(search, 0, 1);
        searchBox = new TextField();
        add(searchBox, 1, 1);

        submit = new Button("Submit");
        submit.setBackground(btBkg);
        submit.setFont(lblFont);
        add(submit, 0, 2);

        user = new Label();
        user.setFont(lblFont);
        add(user, 0, 4);

        add = new Button("Add friend");
        add.setBackground(btBkg);
        add.setFont(lblFont);

        add(add, 1, 4);

        user.setVisible(false);
        add.setVisible(false);

        submit.setOnAction(e -> clientObservable.searchUser(searchBox.getText()));

    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("updateView", e -> update());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_USER_SUCCESS.name(), e -> updateSuccess());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_USER_FAIL.name(), e -> updateFail());
    }

    private void update() {
        setVisible(clientObservable.getAtualState() == Situation.SEARCH_USERS);
    }

    private void updateSuccess() {
        user.setVisible(false);
        add.setVisible(false);
        searchBox.clear();
        searchBox.requestFocus();
        User foundUser = clientObservable.getNotificationSM().getClientRequest().getSelectedUser();
        user.setText(foundUser.getUsername());
        user.setVisible(true);
        add.setVisible(true);
        add.setOnAction(e -> clientObservable.addFriend(foundUser));
    }

    private void updateFail() {
        user.setText(clientObservable.getNotification());
        user.setVisible(true);
        add.setVisible(false);
    }
}
