package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Group;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class SeeGroupsState extends StateAdapter {

    public SeeGroupsState(Data model) {
        super(model);
    }

    @Override
    public IState seeGroups() {
        getModel().seeGroups();
        return new SeeGroupsState(getModel());
    }

    @Override
    public IState seeAvailableGroups() {
        getModel().seeAvailableGroups();
        return new SeeGroupsState(getModel());
    }

    @Override
    public IState seePendingGroups() {
        getModel().seeGroupsPending();
        return new SeeGroupsState(getModel());
    }

    @Override
    public IState seeManageGroups() {
        getModel().seeManageGroups();
        return new SeeGroupsState(getModel());
    }

    @Override
    public IState deleteGroup(Group g) {
        getModel().deleteGroup(g);
        return new SeeGroupsState(getModel());
    }

    @Override
    public IState quitGroup(Group g) {
        getModel().quitGroup(g);
        return new SeeGroupsState(getModel());
    }

    @Override
    public IState quitGroup(User u, Group g) {
        getModel().quitGroup(u, g);
        return new SeeGroupsState(getModel());
    }

    @Override
    public IState manageMembers(Group g) {
        getModel().manageMembers(g);
        return new SeeGroupsState(getModel());
    }

    @Override
    public IState changeName(Group g) {
        getModel().changeName(g);
        return new SeeGroupsState(getModel());
    }

    @Override
    public IState sendGroupRequest(Group g) {
        getModel().sendGroupRequest(g);
        return new SeeGroupsState(getModel());
    }

    @Override
    public IState cancelGroupRequest(Group g) {
        getModel().cancelGroupRequest(g);
        return new SeeGroupsState(getModel());
    }

    @Override
    public IState acceptGroupRequest(User u, Group g) {
        getModel().acceptGroupRequest(u, g);
        return new SeeGroupsState(getModel());
    }

    @Override
    public IState seeMessages(Group group) {
        getModel().seeMessages(group);
        return new MessagesState(getModel());
    }

    @Override
    public IState sendMessageToGroup(Message message) {
        getModel().sendMessageToGroup(message);
        return new MessagesState(getModel());
    }


    @Override
    public Situation getAtualState() {
        return Situation.SEE_GROUPS;
    }

}
