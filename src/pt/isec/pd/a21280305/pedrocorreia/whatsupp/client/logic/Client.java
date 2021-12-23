package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.User;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states.ContactServerManagerState;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states.IState;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states.InitialState;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states.LoginState;

public class Client {
    private Data model;
    private IState state;

    public Client(String serverManagerAddress, int serverManagerPort) {
        model = new Data(serverManagerAddress, serverManagerPort);
        state = new ContactServerManagerState(model);
    }

    private void setState(IState state) {
        this.state = state;
    }

    public void createConnection() {
        setState(state.createConnection());
    }

    public void contactServerManager() {
        setState(state.contactServerManager());
    }

    public void initialStatus(String opt) {
        setState(state.initialOption(opt));
    }

    public void login(String username, String password) {
        setState(state.login(username, password));
    }

    public void register(String username, String password, String confPassword, String fname, String lname) {
        setState(state.register(username, password, confPassword, fname, lname));
    }

    public void userLoggedIn() {
        setState(state.userState());
    }

    public SharedMessage getNotification() {
        return model.getNotification();
    }

    public Situation getAtualState() {
        return state.getAtualState();
    }

    public User getUser() {
        return model.getUser();
    }
}
