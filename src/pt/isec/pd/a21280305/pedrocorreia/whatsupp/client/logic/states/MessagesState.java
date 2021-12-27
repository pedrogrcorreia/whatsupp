package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

public class MessagesState extends StateAdapter {

    public MessagesState(Data model) {
        super(model);
    }

    @Override
    public IState seeMessages(User user) {
        getModel().seeMessages(user);
        return new MessagesState(getModel());
    }

    @Override
    public IState deleteMessage(Message msg) {
        getModel().deleteMessage(msg);
        return new MessagesState(getModel());
    }

    @Override
    public IState sendMessage(Message msg) {
        getModel().sendMessage(msg);
        seeMessages(getModel().getFriend());
        return new MessagesState(getModel());
    }

    @Override
    public Situation getAtualState() {
        return Situation.MESSAGE;
    }

}
