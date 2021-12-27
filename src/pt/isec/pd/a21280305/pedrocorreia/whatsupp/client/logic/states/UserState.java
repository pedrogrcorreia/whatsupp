package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class UserState extends StateAdapter {
    public UserState(Data model) {
        super(model);
        // model.retrieveInfo();
    }

    @Override
    public IState searchUsers() {
        return new SearchUsersState(getModel());
    }

    @Override
    public IState seeFriends() {
        getModel().seeFriends();
        return new SeeFriendsState(getModel());
    }

    @Override
    public IState seeGroups() {
        return new SeeGroupsState(getModel());
    }

    @Override
    public IState seeMessages(User user) {
        getModel().seeMessages(user);
        return new MessagesState(getModel());
    }

    @Override
    public IState seeFriendsRequests() {
        getModel().seeFriendsRequests();
        return new SeeFriendsState(getModel());
    }

    @Override
    public IState seeFriendsRequestsPending() {
        getModel().seeFriendsRequestsPending();
        return new SeeFriendsState(getModel());
    }

    @Override
    public Situation getAtualState() {
        return Situation.LOGGED_IN;
    }
}
