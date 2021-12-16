package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class ContactServerManagerState extends StateAdapter {

    boolean connected = false;
    int tries = 0;

    public ContactServerManagerState(Data model) {
        super(model);
    }

    public IState contactServerManager() {
        if (getModel().contactServerManager()) {
            return new InitialState(getModel());
        }
        return new ContactServerManagerState(getModel());
        // if (!connected && tries < 3) {
        // return new ContactServerManagerState(getModel());
        // } else if (!connected && tries == 3) {
        // return new EnterState(getModel());
        // }
        // return new InitialState(getModel());
    }

    // @Override
    // public void run() {
    // while (!connected) {
    // connected = getModel().getConnected();
    // System.out.println(getModel().getConnected());
    // tries++;
    // try {
    // Thread.sleep(3000);
    // } catch (InterruptedException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }

    // }
    // contactServerManager();
    // }

    @Override
    public Situation getAtualState() {
        return Situation.CONTACT_SERVER_MANAGER;
    }

}
