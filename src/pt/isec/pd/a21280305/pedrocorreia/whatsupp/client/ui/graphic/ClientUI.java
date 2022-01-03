package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Group;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states.*;

public class ClientUI extends BorderPane {

    private static class Results{
        String username;
        String password;
        String confPassword;
        String name;

        public Results(String username, String password, String confPassword, String name){
            this.username = username;
            this.password = password;
            this.confPassword = confPassword;
            this.name = name;
        }
    }

    private ClientObservable clientObservable;

    // Menu
    private MenuItem exit;
    private MenuItem back;
    private MenuItem update;

    // Layout
    private VBox notificationPanel;
    private ScrollPane notificationPanelScroll;

    ContactServerManagerPane contactServerManagerPane;
    InitialStatePane initialStatePane;
    LoginStatePane loginStatePane;
    RegisterStatePane registerStatePane;
    UserStatePane userStatePane;
    SearchUsersPane searchUsersPane;
    SeeFriendsStatePane seeFriendsStatePane;
    MessagesStatePane messagesStatePane;
    SeeGroupsStatePane seeGroupsStatePane;
    CreateGroupStatePane createGroupStatePane;

    private Label txtN;
    private List<Label> notifications;
    private List<HBox> notificationBox;

    public ClientUI(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        notifications = new ArrayList<>();
        notificationBox = new ArrayList<>();
        createView();
        createMenus();
        registerObserver();
        update();
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("updateView", event -> update());
        clientObservable.addPropertyChangeListener("notification", event -> updateNotification());
    }

    private void createView() {
        notificationPanel = new VBox(10);
        notificationPanelScroll = new ScrollPane();

        notificationPanel.setPadding(new Insets(10, 10, 10, 10));
        txtN = new Label("Notifications: ");
        notificationPanel.setPrefWidth(100);
        notificationPanel.getChildren().add(txtN);
        notificationPanel.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        notificationPanelScroll.setContent(notificationPanel);
        contactServerManagerPane = new ContactServerManagerPane(clientObservable);
        initialStatePane = new InitialStatePane(clientObservable);
        loginStatePane = new LoginStatePane(clientObservable);
        registerStatePane = new RegisterStatePane(clientObservable);
        userStatePane = new UserStatePane(clientObservable);
        searchUsersPane = new SearchUsersPane(clientObservable);
        seeFriendsStatePane = new SeeFriendsStatePane(clientObservable);
        messagesStatePane = new MessagesStatePane(clientObservable);
        seeGroupsStatePane = new SeeGroupsStatePane(clientObservable);
        createGroupStatePane = new CreateGroupStatePane(clientObservable);

        setRight(notificationPanel);
        Thread t = new Thread(clientObservable);
        t.start();
    }

    private void createMenus() {
        MenuBar menuBar = new MenuBar();
        setTop(menuBar);

        Menu file = new Menu("_File");

        exit = new MenuItem("Exit");
        back = new MenuItem("Start");
        update = new MenuItem("Account Settings");

        file.getItems().addAll(back, update, exit);

        exit.setOnAction((ActionEvent e) -> {
            Stage janela2 = (Stage) this.getScene().getWindow();
            fireEvent(new WindowEvent(janela2, WindowEvent.WINDOW_CLOSE_REQUEST));
        });

        back.setOnAction(e -> {
            clientObservable.back();
        });

        update.setOnAction((ActionEvent e) -> {
            User me = clientObservable.getUser();
            Dialog<Results> dialog = new Dialog<>();
            dialog.setTitle("Change account settings");
            dialog.setHeaderText("Enter the new values please");

            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);

            Label lblUsername = new Label("New username: ");
            TextField username = new TextField(me.getUsername());
            HBox HUsername = new HBox(10, lblUsername, username);

            Label lblPassword = new Label("Actual password: ");
            PasswordField password = new PasswordField();
            HBox HPassword = new HBox(10, lblPassword, password);

            Label lblNewPassword = new Label("New password: ");
            PasswordField confPassword = new PasswordField();
            HBox HNewPassword = new HBox(10, lblNewPassword, confPassword);

            Label lblName = new Label("New name: ");
            TextField name = new TextField(me.getName());
            HBox HName = new HBox(10, lblName, name);
            dialogPane.setContent(new VBox(10, HUsername, HPassword, HNewPassword, HName));

            dialog.setResultConverter((ButtonType button) -> {
                if (button == ButtonType.APPLY) {
                    return new Results(username.getText(), password.getText(), confPassword.getText(), name.getText());
                }
                return null;
            });

            Optional<Results> result = dialog.showAndWait();

            result.ifPresent((Results r) -> {
                User newUser = new User(r.username, r.password, r.confPassword, r.name, me.getID());
                clientObservable.updateUser(newUser);
            });
        });

        menuBar.getMenus().addAll(file);
    }

    private void addNotifications(String notification) {
        if (!notification.equals("")) {
            notificationBox.add(new HBox(10));
            notifications.add(new Label(notification));

            notificationBox.get(notificationBox.size() - 1).getChildren()
                    .add(notifications.get(notifications.size() - 1));
            notificationBox.get(notificationBox.size() - 1).setBorder(new Border(new BorderStroke(Color.BLUE,
                    BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
            notificationBox.get(notificationBox.size() - 1)
                    .setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
            notifications.get(notifications.size() - 1).setWrapText(true);
            notificationPanel.getChildren().add(notificationBox.get(notifications.size() - 1));
            notificationPanelScroll.setPrefWidth(115);
            notificationPanelScroll.setContent(notificationPanel);
            setRight(notificationPanelScroll);
        } else {
            System.out.println("Empty notification");
        }
    }

    private void updateNotification() {
        addNotifications(clientObservable.getNotification());
    }

    private void update() {
        back.setDisable((clientObservable.getAtualState() == Situation.INITIAL_OPTION)
                || (clientObservable.getAtualState() == Situation.LOGIN_USER)
                || (clientObservable.getAtualState() == Situation.REGISTER_USER));
        update.setDisable(clientObservable.getAtualState() != Situation.LOGGED_IN);
        switch (clientObservable.getAtualState()) {
            case CONTACT_SERVER_MANAGER -> setCenter(contactServerManagerPane);
            case INITIAL_OPTION -> setCenter(initialStatePane);
            case REGISTER_USER -> setCenter(registerStatePane);
            case LOGIN_USER -> setCenter(loginStatePane);
            case LOGGED_IN -> setCenter(userStatePane);
            case SEARCH_USERS -> setCenter(searchUsersPane);
            case SEE_FRIENDS -> setCenter(seeFriendsStatePane);
            case MESSAGE -> setCenter(messagesStatePane);
            case SEE_GROUPS -> setCenter(seeGroupsStatePane);
            case CREATE_GROUP -> setCenter(createGroupStatePane);
            default -> throw new IllegalArgumentException("Unexpected value: " +
                    clientObservable.getAtualState());
        }
    }
}
