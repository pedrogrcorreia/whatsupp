package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.states;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

public interface IState {

    IState createConnection();

    IState contactServerManager();

    IState initialOption(String opt);

    IState login(String username, String password);

    IState register(String username, String password, String confPassword, String fname, String lname);

    IState searchUsers();

    IState seeFriends();

    IState seeGroups();

    IState searchUser(String username);

    IState seeMessages(User user);

    IState back();

    IState deleteMessage(Message msg);

    IState sendMessage(Message msg);

    IState seeFriendsRequests();

    IState seeFriendsRequestsPending();

    IState createGroup();

    IState addGroups();

    Situation getAtualState();
}
