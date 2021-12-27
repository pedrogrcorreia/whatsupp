package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

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

    IState addGroups();

    /**
     * Messages
     */

    IState seeMessages(User user);

    IState deleteMessage(Message msg);

    IState sendMessage(Message msg);

    /**
     * Back button
     */

    IState back();

    /** Returns the atual state */
    Situation getAtualState();
}
