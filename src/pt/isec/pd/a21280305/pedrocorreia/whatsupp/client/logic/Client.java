package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states.IState;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states.InitialState;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states.LoginState;

public class Client {
    private Data model;
    private IState state;

    public Client(){
        model = new Data();
        state = new InitialState(model);
        //state = new LoginState(model);
    }

    private void setState(IState state) { this.state = state; }

    public void initialStatus(String opt) { setState(state.initialOption(opt)); }

    public void login(String username, String password) { setState(state.login(username, password)); }

    public void register(String username, String password, String fname, String lname) { setState(state.register(username, password, fname, lname));}

    public Situation getAtualState(){ return state.getAtualState(); }
}