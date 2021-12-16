package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class InitialState extends StateAdapter {
    public InitialState(Data model) {
        super(model);
    }

    @Override
    public IState initialOption(String opt) {
        if (opt.compareToIgnoreCase("register") == 0) {
            return new RegisterState(getModel());
        }
        if (opt.compareToIgnoreCase("login") == 0) {
            return new LoginState(getModel());
        } else {
            return new InitialState(getModel());
        }
    }

    @Override
    public Situation getAtualState() {
        return Situation.INITIAL_OPTION;
    }
}
