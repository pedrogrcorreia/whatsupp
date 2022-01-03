package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Group;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.GroupRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

import java.util.List;
import java.util.Optional;

public class SeeGroupsStatePane extends BorderPane {

    private ClientObservable clientObservable;

    private Label groups;

    private ScrollPane scrollPane;
    private GridPane gridPane;
    private HBox title;

    Background btBkg = new Background(new BackgroundFill(Color.rgb(180, 180, 180), new CornerRadii(10), Insets.EMPTY));
    Font lblFont = new Font(20);
    Font btnFont = new Font(15);

    public SeeGroupsStatePane(ClientObservable clientObservable) {
        this.clientObservable = clientObservable;
        createWindow();
        registerObserver();
        update();
    }

    private void createWindow() {

        gridPane = new GridPane();

        scrollPane = new ScrollPane();

        setPadding(new Insets(25, 25, 25, 25));

        title = new HBox(10);
        groups = new Label("Groups");
        groups.setFont(lblFont);
        title.setAlignment(Pos.TOP_CENTER);
        title.getChildren().add(groups);

        setTop(title);
        scrollPane.setContent(gridPane);
        setCenter(scrollPane);
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("updateView", e -> update());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_GROUPS_SUCCESS.name(), e -> updateListMyGroups());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_PENDING_GROUPS_SUCCESS.name(), e -> updateListPendingGroups());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_AVAILABLE_GROUPS_SUCCESS.name(), e -> updateListAvailableGroups());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_MANAGE_GROUPS_SUCCESS.name(), e -> updateListAdminGroups());
        clientObservable.addPropertyChangeListener(Strings.USER_MANAGE_GROUP_SUCCESS.name(), e -> updateMembers());
        clientObservable.addPropertyChangeListener(Strings.QUIT_GROUP.name(), e -> {
            if(clientObservable.getState() == State.MANAGE){
                clientObservable.seeManageGroups();
            }else{
                clientObservable.seeGroups();
            }
        });
        clientObservable.addPropertyChangeListener(Strings.DELETED_GROUP.name(), e -> {
            if(clientObservable.getState() == State.GROUP){
                clientObservable.seeGroups();
            }
            if(clientObservable.getState() == State.AVAILABLE){
                clientObservable.seeAvailableGroups();
            }
            if(clientObservable.getState() == State.PENDING){
                clientObservable.seePendingGroups();
            }
        });
        clientObservable.addPropertyChangeListener(Strings.NEW_GROUP_REQUEST.name(), e -> {
            if(clientObservable.getState() == State.MANAGE){
                clientObservable.manageMembers(clientObservable.getGroup());
            }
        });
        clientObservable.addPropertyChangeListener(Strings.ACCEPTED_GROUP_REQUEST.name(), e -> {
            if(clientObservable.getState() == State.GROUP){
                clientObservable.seeGroups();
            }
            if(clientObservable.getState() == State.PENDING){
                clientObservable.seePendingGroups();
            }
        });
        clientObservable.addPropertyChangeListener(Strings.ADMIN_ACCEPT_GROUP_REQUEST.name(), e -> clientObservable.manageMembers(clientObservable.getGroup()));

        clientObservable.addPropertyChangeListener(Strings.USER_CHANGE_GROUP.name(), e -> clientObservable.seeManageGroups());

        clientObservable.addPropertyChangeListener(Strings.USER_QUIT_GROUP_SUCCESS.name(), e -> {
            if(clientObservable.getState() == State.PENDING){
                clientObservable.seePendingGroups();
            }
            if(clientObservable.getState() == State.GROUP){
                clientObservable.seeGroups();
            }
        });
        clientObservable.addPropertyChangeListener(Strings.USER_DELETE_GROUP_SUCCESS.name(), e -> clientObservable.seeManageGroups());
    }

    private void update() {
        setVisible(clientObservable.getAtualState() == Situation.SEE_GROUPS);
    }

    private void updateListMyGroups() {
        getChildren().clear();
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setHgap(15);
        gridPane.setVgap(15);

        List<GroupRequests> myGroups = clientObservable.getMyGroups();
        if(myGroups.size() == 0){
            Label noGroups = new Label("No groups to display!");
            gridPane.add(noGroups, 0, 1);
            return;
        }

        for(int i = 0; i < myGroups.size(); i++){
            Label group;
            Group g = myGroups.get(i).getGroup();
            group = new Label(g.getName());
            group.setFont(lblFont);
            gridPane.add(group, 0, i+1);

            Button seeGroupMessages = new Button("Messages");
            seeGroupMessages.setBackground(btBkg);
            seeGroupMessages.setFont(btnFont);
            gridPane.add(seeGroupMessages, 1, i+1);

            Button quitGroup = new Button("Quit");
            quitGroup.setBackground(btBkg);
            quitGroup.setFont(btnFont);
            gridPane.add(quitGroup, 2, i +1);

            seeGroupMessages.setOnAction(e -> clientObservable.seeMessages(g));
            quitGroup.setOnAction(e -> clientObservable.quitGroup(g));
        }

        activateWindow();
    }

    private void updateListAdminGroups() {
        getChildren().clear();
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setHgap(15);
        gridPane.setVgap(15);

        List<Group> myGroups = clientObservable.getManageGroups();
        if(myGroups.size() == 0){
            Label noGroups = new Label("No groups to display!");
            gridPane.add(noGroups, 0, 1);
            return;
        }
        for(int i = 0; i < myGroups.size(); i++) {
            Label group;
            Group g = myGroups.get(i);
            group = new Label(g.getName());
            group.setFont(lblFont);
            gridPane.add(group, 0, i + 1);

            Button deleteGroup = new Button("Delete");
            deleteGroup.setBackground(btBkg);
            deleteGroup.setFont(btnFont);
            gridPane.add(deleteGroup, 1, i + 1);

            Button manageMembers = new Button("Manage");
            manageMembers.setBackground(btBkg);
            manageMembers.setFont(btnFont);
            gridPane.add(manageMembers, 2, i + 1);

            Button changeName = new Button("Change name");
            changeName.setBackground(btBkg);
            changeName.setFont(btnFont);
            gridPane.add(changeName, 3, i + 1);

            deleteGroup.setOnAction(e -> clientObservable.deleteGroup(g));

            manageMembers.setOnAction(e -> clientObservable.manageMembers(g));

            changeName.setOnAction(e -> {
                TextInputDialog txtDlg = new TextInputDialog();
                txtDlg.setHeaderText("Rename group");
                txtDlg.setContentText("Please enter the new name:");

                Optional<String> result = txtDlg.showAndWait();

                if(result.isPresent()){
                    Group newGroup = new Group(g.getAdmin(), result.get(), g.getID());
                    clientObservable.changeName(newGroup);
                }
            });
        }

        activateWindow();
    }

    private void updateListAvailableGroups() {
        getChildren().clear();
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setHgap(15);
        gridPane.setVgap(15);

        List<Group> myGroups = clientObservable.getAvailableGroups();
        if(myGroups.size() == 0){
            Label noGroups = new Label("No groups to display!");
            gridPane.add(noGroups, 0, 1);
            return;
        }
        for(int i = 0; i < myGroups.size(); i++) {
            Label group;
            Group g = myGroups.get(i);
            group = new Label(g.getName());
            group.setFont(lblFont);
            gridPane.add(group, 0, i + 1);

            Button sendRequest = new Button("Enter");
            sendRequest.setBackground(btBkg);
            sendRequest.setFont(btnFont);
            gridPane.add(sendRequest, 1, i+1);

            sendRequest.setOnAction(e -> clientObservable.sendGroupRequest(g));
        }

        activateWindow();
    }

    private void updateListPendingGroups(){
        getChildren().clear();
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setHgap(15);
        gridPane.setVgap(15);

        List<GroupRequests> myGroups = clientObservable.getPendingGroups();

        if(myGroups.size() == 0){
            Label noGroups = new Label("No groups to display!");
            gridPane.add(noGroups, 0, 1);
            return;
        }
        for(int i = 0; i < myGroups.size(); i++) {
            Label group;
            Group g = myGroups.get(i).getGroup();
            group = new Label(g.getName());
            group.setFont(lblFont);
            gridPane.add(group, 0, i + 1);

            Button cancelRequest = new Button("Cancel request");
            cancelRequest.setBackground(btBkg);
            cancelRequest.setFont(btnFont);
            gridPane.add(cancelRequest, 1, i+1);

            cancelRequest.setOnAction(e -> clientObservable.cancelGroupRequest(g));
        }

        activateWindow();
    }

    private void updateMembers(){
        getChildren().clear();
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setHgap(15);
        gridPane.setVgap(15);

        List<GroupRequests> myGroups = clientObservable.getGroupMembers();

        if(myGroups.size() == 0){
            Label noGroups = new Label("No members to display!");
            gridPane.add(noGroups, 0, 1);
            return;
        }
        for(int i = 0; i < myGroups.size(); i++) {
            Label user;
            Group g = myGroups.get(i).getGroup();
            User u = myGroups.get(i).getRequester();
            user = new Label(u.getName());
            user.setFont(lblFont);
            gridPane.add(user, 0, i + 1);

            Button action = new Button();
            if(myGroups.get(i).getStatus() == 1){
                action.setText("Delete");
                action.setBackground(btBkg);
                action.setFont(btnFont);
                action.setOnAction(e -> clientObservable.quitGroup(u, g));
            }
            else{
                action.setBackground(btBkg);
                action.setFont(btnFont);
                action.setText("Accept request");
                action.setOnAction(e -> clientObservable.acceptGroupRequest(u, g));
            }
            gridPane.add(action, 1, i+1);

            activateWindow();
        }
    }

    private void activateWindow(){
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(gridPane);
        setTop(title);
        setCenter(scrollPane);
    }
}
