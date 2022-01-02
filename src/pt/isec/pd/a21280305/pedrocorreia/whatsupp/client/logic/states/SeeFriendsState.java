package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class SeeFriendsState extends StateAdapter {

    public SeeFriendsState(Data model) {
        super(model);
    }

    @Override
    public IState contactServerManager() {
        if (getModel().contactServerManager()) {
            getModel().retrieveInfo();
            return new SeeFriendsState(getModel());
        }

        return new ContactServerManagerState(getModel());
    }

    @Override
    public IState seeFriends() {
        getModel().seeFriends();
        return new SeeFriendsState(getModel());
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
    public IState seeMessages(User user) {
        if (user == null) {
            return new SeeFriendsState(getModel());
        }
        getModel().seeMessages(user);
        return new MessagesState(getModel());
    }

    @Override
    public IState addFriend(User user) {
        getModel().addFriend(user);
        return new SeeFriendsState(getModel());
    }

    @Override
    public IState acceptRequest(User user) {
        getModel().acceptRequest(user);
        return new SeeFriendsState(getModel());
    }

    @Override
    public IState cancelRequest(User user) {
        getModel().cancelRequest(user);
        return new SeeFriendsState(getModel());
    }

    @Override
    public IState deleteFriendship(User user) {
        getModel().deleteFriendship(user);
        return new SeeFriendsState(getModel());
    }

    @Override
    public Situation getAtualState() {
        return Situation.SEE_FRIENDS;
    }

}
