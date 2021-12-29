package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;


import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class ManageGroupState extends StateAdapter {

    public ManageGroupState(Data model){
        super(model);
    }

    @Override
    public Situation getAtualState() {
        return Situation.MANAGE_GROUP;
    }
}
