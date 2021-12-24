package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class RegisterState extends StateAdapter {
    public RegisterState(Data model) {
        super(model);
    }

    @Override
    public IState register(String username, String password, String confPassword, String fname, String lname) {
        if (getModel().register(username, password, confPassword, fname, lname)) {
            return new InitialState(getModel());
        } else {
            return new RegisterState(getModel());
        }
    }

    @Override
    public Situation getAtualState() {
        return Situation.REGISTER_USER;
    }

}
