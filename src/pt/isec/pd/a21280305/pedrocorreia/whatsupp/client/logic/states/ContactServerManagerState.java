package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class ContactServerManagerState extends StateAdapter {

    public ContactServerManagerState(Data model) {
        super(model);
    }

    public IState contactServerManager() {
        if (getModel().contactServerManager()) {
            return new InitialState(getModel());
        } else {
            return new ContactServerManagerState(getModel());
        }
    }

    @Override
    public Situation getAtualState() {
        return Situation.CONTACT_SERVER_MANAGER;
    }

}
