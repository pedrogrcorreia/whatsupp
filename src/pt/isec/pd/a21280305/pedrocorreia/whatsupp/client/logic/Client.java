package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.FriendsRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states.ContactServerManagerState;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states.IState;

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

    /**
     * Login and Register
     */
    public void login(String username, String password) {
        setState(state.login(username, password));
    }

    public void register(String username, String password, String confPassword, String fname, String lname) {
        setState(state.register(username, password, confPassword, fname, lname));
    }

    /**
     * Search user
     */
    public void searchUsers() {
        setState(state.searchUsers());
    }

    public void searchUser(String username) {
        setState(state.searchUser(username));
    }

    /**
     * Friends
     */
    public void seeFriends() {
        setState(state.seeFriends());
    }

    public void seeFriendsRequests() {
        setState(state.seeFriendsRequests());
    }

    public void seeFriendsRequestsPending() {
        setState(state.seeFriendsRequestsPending());
    }

    public void addFriend(User user) {
        model.addFriend(user);
    }

    /**
     * Groups
     */
    public void seeGroups() {
        setState(state.seeGroups());
    }

    public void createGroup() {
        setState(state.createGroup());
    }

    public void addGroups() {
        setState(state.addGroups());
    }

    /**
     * Messages
     */
    public void seeMessages(User user) {
        setState(state.seeMessages(user));
    }

    public void deleteMessage(Message msg) {
        setState(state.deleteMessage(msg));
    }

    public void sendMessage(Message msg) {
        setState(state.sendMessage(msg));
    }

    /**
     * Back to start state
     */
    public void back() {
        setState(state.back());
    }

    /** Get notifications */
    public SharedMessage getNotification() {
        return model.getNotification();
    }

    public Situation getAtualState() {
        return state.getAtualState();
    }

    /** Gets directly from model */

    public User getUser() {
        return model.getUser();
    }

    public User getFriend() {
        return model.getFriend();
    }

    public List<FriendsRequests> getFriendsRequests() {
        return model.getFriendsRequests();
    }

    public List<FriendsRequests> getFriendsRequestsPending() {
        return model.getFriendsRequestsPending();
    }

    public List<FriendsRequests> getFriendsRequestsSent() {
        return model.getFriendsRequestsSent();
    }

    public List<Message> getMessages() {
        return model.getMessages();
    }
}
