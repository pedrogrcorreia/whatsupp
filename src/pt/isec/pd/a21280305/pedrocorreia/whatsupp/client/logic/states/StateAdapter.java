package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public abstract class StateAdapter implements IState {
    private final Data model;

    public StateAdapter(Data model){ this.model = model; }

    public final Data getModel() { return model; }

    @Override
    public IState initialOption(String opt) {
        return this;
    }

    @Override
    public IState login(String username, String password) {
        return this;
    }

    @Override
    public IState register(String username, String password, String fname, String lname) {
        return this;
    }
}
