package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.User;

public class MessagesState extends StateAdapter {

    public MessagesState(Data model) {
        super(model);
    }

    @Override
    public IState seeMessages(User user) {
        getModel().getMessages(user);
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
