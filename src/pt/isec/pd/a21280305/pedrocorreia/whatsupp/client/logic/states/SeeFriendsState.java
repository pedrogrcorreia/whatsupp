package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.User;

public class SeeFriendsState extends StateAdapter {

    public SeeFriendsState(Data model) {
        super(model);
    }

    @Override
    public IState seeFriends() {
        getModel().getFriends();
        return new SeeFriendsState(getModel());
    }

    @Override
    public IState seeMessages(User user) {
        getModel().getMessages(user);
        return new MessagesState(getModel());
    }

    @Override
    public Situation getAtualState() {
        return Situation.SEE_FRIENDS;
    }

}
