package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Group;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data.Data;

import java.io.File;

public abstract class StateAdapter implements IState {
    private final Data model;

    public StateAdapter(Data model) {
        this.model = model;
    }

    public final Data getModel() {
        return model;
    }

    /**
     * Contact Server Manager
     */

    @Override
    public IState contactServerManager() {
        return this;
    }

    /**
     * Initial menu
     */

    @Override
    public IState initialOption(String opt) {
        return this;
    }

    /**
     * Login or register
     */

    @Override
    public IState login(String username, String password) {
        return this;
    }

    @Override
    public IState register(String username, String password, String confPassword, String fname, String lname) {
        return this;
    }

    @Override
    public IState updateUser(User u) {
        return this;
    }

    /**
     * Search users
     */

    @Override
    public IState searchUsers() {
        return this;
    }

    @Override
    public IState searchUser(String username) {
        return this;
    }

    @Override
    public IState getAllUsers() {
        return this;
    }

    /** Friends */

    @Override
    public IState seeFriends() {
        return this;
    }

    @Override
    public IState seeFriendsRequests() {
        return this;
    }

    @Override
    public IState seeFriendsRequestsPending() {
        return this;
    }

    @Override
    public IState acceptRequest(User user) {
        return this;
    }

    @Override
    public IState cancelRequest(User user) {
        return this;
    }

    @Override
    public IState addFriend(User user) {
        return this;
    }

    @Override
    public IState deleteFriendship(User user) {
        return this;
    }

    /**
     * Groups
     */

    @Override
    public IState seeGroups() {
        return this;
    }

    @Override
    public IState createNewGroup(String name) {
        return this;
    }

    @Override
    public IState createGroup() {
        return this;
    }

    @Override
    public IState seeAvailableGroups() {
        return this;
    }

    @Override
    public IState seePendingGroups() {
        return this;
    }

    @Override
    public IState seeManageGroups() {
        return this;
    }

    @Override
    public IState deleteGroup(Group g) {
        return this;
    }

    @Override
    public IState quitGroup(Group g) {
        return this;
    }

    @Override
    public IState quitGroup(User u, Group g) {
        return this;
    }

    @Override
    public IState manageMembers(Group g) {
        return this;
    }

    @Override
    public IState changeName(Group g) {
        return this;
    }

    @Override
    public IState sendGroupRequest(Group g) {
        return this;
    }

    @Override
    public IState cancelGroupRequest(Group g) {
        return this;
    }

    @Override
    public IState acceptGroupRequest(User u, Group g) {
        return this;
    }

    /**
     * Messages
     */

    @Override
    public IState seeMessages(User user) {
        return this;
    }

    @Override
    public IState seeMessages(Group group) {
        return this;
    }

    @Override
    public IState deleteMessage(Message msg) {
        return this;
    }

    @Override
    public IState sendMessage(Message msg) {
        return this;
    }

    @Override
    public IState sendMessageToGroup(Message msg) {
        return this;
    }

    /**
     * Files
     * @param file
     */

    @Override
    public IState sendFile(Message file){
        return this;
    }

    @Override
    public IState sendFileToGroup(Message file) {
        return this;
    }

    @Override
    public IState uploadFile(Message file) {
        return this;
    }

    @Override
    public IState downloadFile(Message file, File path){
        return this;
    }

    @Override
    public IState deleteFile(Message file) {
        return this;
    }

    /**
     * Back button
     */

    @Override
    public IState back() {
        return new UserState(getModel());
    }

    @Override
    public IState backToInitialState() {
        return new InitialState(getModel());
    }
}
