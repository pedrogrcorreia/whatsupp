package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class CreateGroupState extends StateAdapter {

    public CreateGroupState(Data model) {
        super(model);
    }

    @Override
    public IState contactServerManager() {
        if (getModel().contactServerManager()) {
            getModel().retrieveInfo();
            return new CreateGroupState(getModel());
        }

        return new ContactServerManagerState(getModel());
    }

    @Override
    public IState createNewGroup(String name) {
        getModel().createNewGroup(name);
        return new CreateGroupState(getModel());
    }

    @Override
    public Situation getAtualState() {
        return Situation.CREATE_GROUP;
    }
}
