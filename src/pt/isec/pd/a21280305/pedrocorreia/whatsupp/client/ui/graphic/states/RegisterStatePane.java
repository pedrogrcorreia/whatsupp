package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;

public class RegisterStatePane extends GridPane {
    private ClientObservable clientObservable;

    private Label lblUsername, lblPassword, lblConfPassword, lblFName, lblLName;
    private TextField txtUsername, txtPassword, txtConfPassword, txtFName, txtLName;
    private Button submit;

    public RegisterStatePane(ClientObservable clientObservable) {
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

        lblUsername = new Label("Username: ");
        add(lblUsername, 0, 1);
        txtUsername = new TextField();
        add(txtUsername, 1, 1);

        lblPassword = new Label("Password: ");
        add(lblPassword, 0, 2);
        txtPassword = new PasswordField();
        add(txtPassword, 1, 2);

        lblConfPassword = new Label("Password: ");
        add(lblConfPassword, 0, 3);
        txtConfPassword = new PasswordField();
        add(txtConfPassword, 1, 3);

        lblFName = new Label("First name: ");
        add(lblFName, 0, 4);
        txtFName = new TextField();
        add(txtFName, 1, 4);

        lblLName = new Label("Last name: ");
        add(lblLName, 0, 5);
        txtLName = new TextField();
        add(txtLName, 1, 5);

        submit = new Button("Submit");
        add(submit, 1, 6);

        submit.setOnAction(e -> {
            clientObservable.register(txtUsername.getText(), txtPassword.getText(), txtConfPassword.getText(),
                    txtFName.getText(), txtLName.getText());
        });
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("updateView", e -> update());
    }

    private void update() {
        setVisible(clientObservable.getAtualState() == Situation.REGISTER_USER);
    }

}
