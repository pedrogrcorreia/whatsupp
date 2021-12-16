package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class EnterState extends StateAdapter {

    public EnterState(Data model) {
        super(model);
    }

    public IState createConnection() {
        if (getModel().createConnection()) {
            return new ContactServerManagerState(getModel());
        }
        return new EnterState(getModel());
        // return new ContactServerManagerState(getModel());
    }

    @Override
    public Situation getAtualState() {
        return Situation.ENTER_STATE;
    }
}
