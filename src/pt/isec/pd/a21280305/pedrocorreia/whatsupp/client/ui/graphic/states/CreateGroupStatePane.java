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

public class CreateGroupStatePane extends GridPane {

    private ClientObservable clientObservable;
    private Label name;
    private TextField groupName;
    private Label successText;
    private Label failedText;
    private Button submit;

    public CreateGroupStatePane(ClientObservable clientObservable){
        this.clientObservable = clientObservable;
        createWindow();
        registerObserver();
        update();
    }

    private void createWindow(){
        Background btBkg = new Background(new BackgroundFill(Color.rgb(180, 180, 180), new CornerRadii(10), Insets.EMPTY));
        Font lblFont = new Font(20);
        Font btnFont = new Font(15);

        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));

        name = new Label("Enter group name");
        name.setFont(lblFont);
        add(name, 0, 1);

        groupName = new TextField();
        add(groupName, 1, 1);

        submit = new Button("Submit");
        submit.setBackground(btBkg);
        submit.setFont(btnFont);
        add(submit, 0, 2);

        successText = new Label("");
        failedText = new Label("");

        successText.setFont(lblFont);
        failedText.setFont(lblFont);

        add(successText, 0, 3);
        add(failedText, 0, 3);

        successText.setVisible(false);
        failedText.setVisible(false);

        submit.setOnAction(e -> clientObservable.createNewGroup(groupName.getText()));
    }

    private void registerObserver(){
        clientObservable.addPropertyChangeListener("updateView", e -> update());
        clientObservable.addPropertyChangeListener(Strings.REQUEST_NEW_GROUP_SUCCESS.name(), e -> updateSuccess());
        clientObservable.addPropertyChangeListener(Strings.REQUEST_NEW_GROUP_FAIL.name(), e -> updateFail());
    }

    private void update(){
        setVisible(clientObservable.getAtualState() == Situation.CREATE_GROUP);
    }

    private void updateSuccess(){
        failedText.setVisible(false);
        successText.setText("Group created successfully!");
        successText.setTextFill(Color.GREEN);
        successText.setVisible(true);
    }

    private void updateFail(){
        successText.setVisible(false);
        failedText.setText(clientObservable.getNotification());
        failedText.setTextFill(Color.RED);
        failedText.setVisible(true);
    }
}
