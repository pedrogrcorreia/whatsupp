package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class RegisterState extends StateAdapter {
    public RegisterState(Data model){ super(model); }

    @Override
    public IState register(String username, String password, String fname, String lname) {
        return super.register(username, password, fname, lname);
    }

    @Override
    public Situation getAtualState() {
        return Situation.REGISTER_USER;
    }


}
