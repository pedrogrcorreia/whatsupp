package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class LoginState extends StateAdapter {
    public LoginState(Data model) {
        super(model);
    }

    @Override
    public IState login(String username, String password) {
        // boolean aux;
        // // Debug
        // getModel().login(username, password);
        // return new LoginState(getModel());
        if (getModel().login(username, password)) {
            return new UserState(getModel());
        } else {
            return new LoginState(getModel());
        }

    }

    @Override
    public Situation getAtualState() {
        return Situation.LOGIN_USER;
    }

}
