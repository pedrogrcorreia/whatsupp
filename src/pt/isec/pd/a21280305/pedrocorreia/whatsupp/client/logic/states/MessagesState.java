package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Group;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

import java.io.File;

public class MessagesState extends StateAdapter {

    public MessagesState(Data model) {
        super(model);
    }

    @Override
    public IState contactServerManager() {
        if (getModel().contactServerManager()) {
            getModel().retrieveInfo();
            return new MessagesState(getModel());
        }

        return new ContactServerManagerState(getModel());
    }


    @Override
    public IState seeMessages(User user) {
        getModel().seeMessages(user);
        return new MessagesState(getModel());
    }

    @Override
    public IState seeMessages(Group group){
        getModel().seeMessages(group);
        return new MessagesState(getModel());
    }

    @Override
    public IState seeFriends() {
        getModel().seeFriends();
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
    public IState sendMessageToGroup(Message msg) {
        getModel().sendMessageToGroup(msg);
        seeMessages(getModel().getGroup());
        return new MessagesState(getModel());
    }

    @Override
    public IState sendFile(Message file) {
        getModel().sendFile(file);
        seeMessages(getModel().getFriend());
        return new MessagesState(getModel());
    }

    @Override
    public IState sendFileToGroup(Message file) {
        getModel().sendFileToGroup(file);
        seeMessages(getModel().getGroup());
        return new MessagesState(getModel());
    }

    @Override
    public IState uploadFile(Message file) {
        getModel().uploadFileToServer(file);
        return new MessagesState(getModel());
    }

    @Override
    public IState downloadFile(Message file, File path){
        getModel().downloadFile(file, path);
        return new MessagesState(getModel());
    }

    @Override
    public IState deleteFile(Message file) {
        getModel().deleteFile(file);
        return new MessagesState(getModel());
    }

    @Override
    public Situation getAtualState() {
        return Situation.MESSAGE;
    }

}
