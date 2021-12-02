package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;

public interface IState {

    IState initialOption(String opt);
    IState login(String username, String password);
    IState register(String username, String password, String fname, String lname);

    Situation getAtualState();
}
