package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Group;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

import java.io.File;

public interface IState {

    /**
     * Contact Server Manager
     */
    IState contactServerManager();

    /**
     * Initial menu
     */

    IState initialOption(String opt);

    /**
     * Login or register
     */

    IState login(String username, String password);

    IState register(String username, String password, String confPassword, String fname, String lname);

    /**
     * Search Users
     */

    IState searchUsers();

    IState searchUser(String username);

    IState getAllUsers();

    /**
     * Friends
     */

    IState seeFriends();

    IState seeFriendsRequests();

    IState seeFriendsRequestsPending();

    IState acceptRequest(User user);

    IState cancelRequest(User user);

    IState addFriend(User user);

    IState deleteFriendship(User user);

    /**
     * Groups
     */

    IState seeGroups();

    IState createGroup();

    IState createNewGroup(String name);

    IState seeAvailableGroups();

    IState seePendingGroups();

    IState seeManageGroups();

    IState deleteGroup(Group g);

    IState quitGroup(Group g);

    IState quitGroup(User u, Group g);

    IState manageMembers(Group g);

    IState changeName(Group g);

    IState sendGroupRequest(Group g);

    IState cancelGroupRequest(Group g);

    IState acceptGroupRequest(User u, Group g);

    /**
     * Messages
     */

    IState seeMessages(User user);

    IState seeMessages(Group group);

    IState deleteMessage(Message msg);

    IState sendMessage(Message msg);

    IState sendMessageToGroup(Message msg);

    /**
     * Files
     * @param file
     */

    IState sendFile(Message file);

    IState sendFileToGroup(Message file);

    IState uploadFile(Message file);

    IState downloadFile(Message file, File path);

    IState deleteFile(Message file);

    /**
     * Back button
     */

    IState back();

    /** Returns the atual state */
    Situation getAtualState();


}
