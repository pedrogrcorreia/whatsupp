package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class SearchUsersState extends StateAdapter {

    public SearchUsersState(Data model) {
        super(model);
    }

    @Override
    public IState searchUser(String username) {
        getModel().searchUser(username);
        return new SearchUsersState(getModel());
    }

    @Override
    public Situation getAtualState() {
        return Situation.SEARCH_USERS;
    }
}
