package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

import java.io.File;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.*;
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

    /**
     * Sets a new state
     * 
     * @param state
     */
    private void setState(IState state) {
        this.state = state;
    }

    /**
     * Contact server manager
     */

    public void contactServerManager() {
        setState(state.contactServerManager());
    }

    /**
     * Initial menu
     */

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

    public void acceptRequest(User user) {
        setState(state.acceptRequest(user));
    }

    public void cancelRequest(User user) {
        setState(state.cancelRequest(user));
    }

    public void addFriend(User user) {
        setState(state.addFriend(user));
    }

    public void deleteFriendship(User user) {
        setState(state.deleteFriendship(user));
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

    public void createNewGroup(String name){
        setState(state.createNewGroup(name));
    }

    public void seeAvailableGroups() {
        setState(state.seeAvailableGroups());
    }

    public void seePendingGroups(){ setState(state.seePendingGroups()); }

    public void seeManageGroups(){ setState(state.seeManageGroups()); }

    public void deleteGroup(Group g){
        setState(state.deleteGroup(g));
    }

    public void quitGroup(Group g){
        setState(state.quitGroup(g));
    }

    public void quitGroup(User u, Group g){ setState(state.quitGroup(u, g)); }

    public void manageMembers(Group g){
        setState(state.manageMembers(g));
    }

    public void changeName(Group g){
        setState(state.changeName(g));
    }

    public void sendGroupRequest(Group g){
        setState(state.sendGroupRequest(g));
    }

    public void cancelGroupRequest(Group g){
        setState(state.cancelGroupRequest(g));
    }

    public void acceptGroupRequest(User u, Group g){ setState(state.acceptGroupRequest(u, g)); }

    /**
     * Messages
     */

    public void seeMessages(User user) {
        setState(state.seeMessages(user));
    }

    public void seeMessages(Group group) {setState(state.seeMessages(group)); }

    public void deleteMessage(Message msg) {
        setState(state.deleteMessage(msg));
    }

    public void sendMessage(Message msg) {
        setState(state.sendMessage(msg));
    }

    public void sendMessageToGroup(Message msg) { setState(state.sendMessageToGroup(msg)); }

    /**
     * Files
     * @param file
     */

    public void sendFile(Message file){
        setState(state.sendFile(file));
    }

    public void uploadFile(Message file) { setState(state.uploadFile(file)); }

    public void downloadFile(Message file) { setState(state.downloadFile(file)); }
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

    public Group getGroup() { return model.getGroup(); }

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

    public List<GroupRequests> getMyGroups() {
        return model.getMyGroups();
    }

    public List<GroupRequests> getPendingGroups() {
        return model.getPendingGroups();
    }

    public List<Group> getManageGroups() {
        return model.getManageGroups();
    }

    public List<Group> getAvailableGroups() {
        return model.getAvailableGroups();
    }

    public List<GroupRequests> getGroupMembers() { return model.getGroupMembers(); }
}
