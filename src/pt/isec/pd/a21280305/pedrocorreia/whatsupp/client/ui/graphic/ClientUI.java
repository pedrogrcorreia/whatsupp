package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states.*;

public class ClientUI extends BorderPane {

    // private final Background lBckColor = new Background(new
    // BackgroundFill(Color.WHITE, null, null));
    // private final Background dBckColor = new Background(new
    // BackgroundFill(Color.DARKGRAY.darker(), null, null));
    // private final Color lFontColor = Color.BLACK;
    // private final Color dFontColor = Color.WHITE;
    // private final Background lNotBckColor = new Background(new
    // BackgroundFill(Color.WHITE, null, null));
    // private final Background dNotBckColor = new Background(new
    // BackgroundFill(Color.GRAY, null, null));

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

        notificationPanelScroll.setContent(notificationPanel);
        contactServerManagerPane = new ContactServerManagerPane(clientObservable);
        setCenter(contactServerManagerPane);
        initialStatePane = new InitialStatePane(clientObservable);
        loginStatePane = new LoginStatePane(clientObservable);
        registerStatePane = new RegisterStatePane(clientObservable);
        userStatePane = new UserStatePane(clientObservable);
        searchUsersPane = new SearchUsersPane(clientObservable);
        seeFriendsStatePane = new SeeFriendsStatePane(clientObservable);
        messagesStatePane = new MessagesStatePane(clientObservable);

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
        update = new MenuItem("Update");
        // MenuItem exit = new MenuItem("Exit");

        file.getItems().addAll(back, exit, update);

        // menu.setOnAction(e -> clientObservable.close());
        exit.setOnAction((ActionEvent e) -> {
            Stage janela2 = (Stage) this.getScene().getWindow();
            fireEvent(new WindowEvent(janela2, WindowEvent.WINDOW_CLOSE_REQUEST));
        });

        back.setOnAction(e -> {
            clientObservable.back();
        });

        // DEBUG
        update.setOnAction((ActionEvent e) -> {
            clientObservable.update();
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
            System.out.println("Notificacao vazia");
        }
    }

    private void updateNotification() {
        addNotifications(clientObservable.getNotification());
    }

    private void update() {
        System.out.println(clientObservable.getAtualState());
        back.setDisable((clientObservable.getAtualState() == Situation.INITIAL_OPTION)
                || (clientObservable.getAtualState() == Situation.LOGIN_USER)
                || (clientObservable.getAtualState() == Situation.REGISTER_USER));
        switch (clientObservable.getAtualState()) {
            case CONTACT_SERVER_MANAGER -> setCenter(contactServerManagerPane);
            case INITIAL_OPTION -> setCenter(initialStatePane);
            case REGISTER_USER -> setCenter(registerStatePane);
            case LOGIN_USER -> setCenter(loginStatePane);
            case LOGGED_IN -> setCenter(userStatePane);
            case SEARCH_USERS -> setCenter(searchUsersPane);
            case SEE_FRIENDS -> setCenter(seeFriendsStatePane);
            case MESSAGE -> setCenter(messagesStatePane);
            default -> throw new IllegalArgumentException("Unexpected value: " +
                    clientObservable.getAtualState());
        }
    }
}
