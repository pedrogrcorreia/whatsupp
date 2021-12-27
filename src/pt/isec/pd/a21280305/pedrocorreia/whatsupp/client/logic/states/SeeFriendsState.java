package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class SeeFriendsState extends StateAdapter {

    public SeeFriendsState(Data model) {
        super(model);
    }

    @Override
    public IState seeFriends() {
        getModel().seeFriends();
        return new SeeFriendsState(getModel());
    }

    @Override
    public IState seeMessages(User user) {
        getModel().seeMessages(user);
        return new MessagesState(getModel());
    }

    @Override
    public Situation getAtualState() {
        return Situation.SEE_FRIENDS;
    }

}
