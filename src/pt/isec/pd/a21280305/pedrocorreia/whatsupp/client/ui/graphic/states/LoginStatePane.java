package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;

public class LoginStatePane extends GridPane {
    private ClientObservable clientObservable;

    private Label lblUsername;
    private Label lblPassword;
    private TextField txtUsername;
    private TextField txtPassword;
    private Button submit;
    private Button back;

    public LoginStatePane(ClientObservable clientObservable) {
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

        lblUsername = new Label("Username: ");
        lblUsername.setFont(lblFont);
        add(lblUsername, 0, 1);
        txtUsername = new TextField();
        add(txtUsername, 1, 1);

        lblPassword = new Label("Password: ");
        lblPassword.setFont(lblFont);
        add(lblPassword, 0, 2);
        txtPassword = new PasswordField();
        add(txtPassword, 1, 2);
        submit = new Button("Submit");
        submit.setBackground(btBkg);
        submit.setFont(lblFont);
        add(submit, 1, 6);

        back = new Button("Back");
        back.setBackground(btBkg);
        back.setFont(lblFont);
        add(back, 1, 7);

        back.setOnAction(e -> clientObservable.backToInitialState());

        submit.setOnAction(e -> {
            clientObservable.login(txtUsername.getText(), txtPassword.getText());
        });


    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("updateView", e -> update());
    }

    private void update() {
        setVisible(clientObservable.getAtualState() == Situation.LOGIN_USER);
        // setVisible(false);
    }
}
