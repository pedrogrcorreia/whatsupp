package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Group;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.GroupRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

import java.util.List;
import java.util.Optional;

public class SeeGroupsStatePane extends GridPane {

    private ClientObservable clientObservable;

    private Label groups;

    public SeeGroupsStatePane(ClientObservable clientObservable) {
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
        groups = new Label("Groups list");
    }

    private void registerObserver() {
        clientObservable.addPropertyChangeListener("updateView", e -> update());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_GROUPS_SUCCESS.name(), e -> updateListMyGroups());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_PENDING_GROUPS_SUCCESS.name(), e -> updateListPendingGroups());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_AVAILABLE_GROUPS_SUCCESS.name(), e -> updateListAvailableGroups());
        clientObservable.addPropertyChangeListener(Strings.DELETED_GROUP.name(), e -> clientObservable.seeManageGroups());
        clientObservable.addPropertyChangeListener(Strings.USER_REQUEST_MANAGE_GROUPS_SUCCESS.name(), e -> updateListAdminGroups());
        clientObservable.addPropertyChangeListener(Strings.USER_MANAGE_GROUP_SUCCESS.name(), e -> updateMembers());
    }

    private void update() {
        setVisible(clientObservable.getAtualState() == Situation.SEE_GROUPS);
    }

    private void updateListMyGroups() {
        getChildren().clear();
        add(groups, 0, 1);
        List<GroupRequests> myGroups = clientObservable.getMyGroups();
        if(myGroups.size() == 0){
            Label noGroups = new Label("No groups to display!");
            add(noGroups, 1, 1);
            return;
        }
        for(int i = 0; i < myGroups.size(); i++){
            Label group;
            Group g = myGroups.get(i).getGroup();
            group = new Label(g.getName());
            add(group, 1, i+1);

            Button seeGroupMessages = new Button("See messages");
            add(seeGroupMessages, 2, i+1);

            Button quitGroup = new Button("Quit group");
            add(quitGroup, 3, i +1);

            seeGroupMessages.setOnAction(e -> clientObservable.seeMessages(g));
            quitGroup.setOnAction(e -> clientObservable.quitGroup(g));
        }
    }

    private void updateListAdminGroups() {
        getChildren().clear();
        add(groups, 0, 1);
        List<Group> myGroups = clientObservable.getManageGroups();
        if(myGroups.size() == 0){
            Label noGroups = new Label("No groups to display!");
            add(noGroups, 1, 1);
            return;
        }
        for(int i = 0; i < myGroups.size(); i++) {
            Label group;
            Group g = myGroups.get(i);
            group = new Label(g.getName());
            add(group, 1, i + 1);

            Button deleteGroup = new Button("Delete group");
            add(deleteGroup, 2, i + 1);
            Button manageMembers = new Button("Manage members");
            add(manageMembers, 3, i + 1);
            Button changeName = new Button("Change group name");
            add(changeName, 4, i + 1);

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
    }

    private void updateListAvailableGroups() {
        getChildren().clear();
        add(groups, 0, 1);
        List<Group> myGroups = clientObservable.getAvailableGroups();
        if(myGroups.size() == 0){
            Label noGroups = new Label("No groups to display!");
            add(noGroups, 1, 1);
            return;
        }
        for(int i = 0; i < myGroups.size(); i++) {
            Label group;
            Group g = myGroups.get(i);
            group = new Label(g.getName());
            add(group, 1, i + 1);

            Button sendRequest = new Button("Send request");
            add(sendRequest, 2, i+1);

            sendRequest.setOnAction(e -> clientObservable.sendGroupRequest(g));
        }
    }

    private void updateListPendingGroups(){
        getChildren().clear();
        add(groups, 0, 1);
        System.out.println("Groups: ");
        List<GroupRequests> myGroups = clientObservable.getPendingGroups();


        if(myGroups.size() == 0){
            Label noGroups = new Label("No groups to display!");
            add(noGroups, 1, 1);
            return;
        }
        for(int i = 0; i < myGroups.size(); i++) {
            Label group;
            Group g = myGroups.get(i).getGroup();
            group = new Label(g.getName());
            add(group, 1, i + 1);

            Button cancelRequest = new Button("Cancel request");
            add(cancelRequest, 2, i+1);

            cancelRequest.setOnAction(e -> clientObservable.cancelGroupRequest(g));
        }
    }

    private void updateMembers(){
        getChildren().clear();
        add(groups, 0, 1);
        System.out.println("Groups: ");
        List<GroupRequests> myGroups = clientObservable.getGroupMembers();


        if(myGroups.size() == 0){
            Label noGroups = new Label("No members to display!");
            add(noGroups, 1, 1);
            return;
        }
        for(int i = 0; i < myGroups.size(); i++) {
            Label user;
            Group g = myGroups.get(i).getGroup();
            User u = myGroups.get(i).getRequester();
            user = new Label(u.getName());
            add(user, 1, i + 1);

            Button action = new Button();
            if(myGroups.get(i).getStatus() == 1){
                action.setText("Delete from group");
                action.setOnAction(e -> clientObservable.quitGroup(u, g));
            }
            else{
                action.setText("Accept request");
            }
            add(action, 2, i+1);

            action.setOnAction(e -> clientObservable.acceptGroupRequest(u, g));
        }
    }
}
