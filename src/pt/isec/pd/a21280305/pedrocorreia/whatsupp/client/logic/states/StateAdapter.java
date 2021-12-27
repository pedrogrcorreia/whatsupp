package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public abstract class StateAdapter implements IState {
    private final Data model;

    public StateAdapter(Data model) {
        this.model = model;
    }

    public final Data getModel() {
        return model;
    }

    @Override
    public IState createConnection() {
        return this;
    }

    @Override
    public IState contactServerManager() {
        return this;
    }

    @Override
    public IState initialOption(String opt) {
        return this;
    }

    @Override
    public IState login(String username, String password) {
        return this;
    }

    @Override
    public IState register(String username, String password, String confPassword, String fname, String lname) {
        return this;
    }

    @Override
    public IState searchUsers() {
        return this;
    }

    @Override
    public IState searchUser(String username) {
        return this;
    }

    @Override
    public IState seeFriends() {
        return this;
    }

    @Override
    public IState seeGroups() {
        return this;
    }

    @Override
    public IState seeMessages(User user) {
        return this;
    }

    @Override
    public IState back() {
        return new UserState(getModel());
    }

    @Override
    public IState deleteMessage(Message msg) {
        return this;
    }

    @Override
    public IState sendMessage(Message msg) {
        return this;
    }

    @Override
    public IState seeFriendsRequests() {
        return this;
    }

    @Override
    public IState seeFriendsRequestsPending() {
        return this;
    }

    @Override
    public IState createGroup() {
        return this;
    }

    @Override
    public IState addGroups() {
        return this;
    }
}
