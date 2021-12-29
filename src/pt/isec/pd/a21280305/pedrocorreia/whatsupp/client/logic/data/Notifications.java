package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.FriendsRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Group;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.GroupRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;

public class Notifications extends Data implements Runnable {

    ObjectInputStream oin;
    ObjectOutputStream oout;
    List<SharedMessage> notLog;
    List<String> friends;

    public Notifications(ObjectInputStream oin, ObjectOutputStream oout, List<SharedMessage> notLog) {
        super();
        this.oin = oin;
        this.oout = oout;
        this.notLog = notLog;
    }

    private void notifyList(SharedMessage response) {
        synchronized (notLog) {
            notLog.add(response);
            notLog.notify();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                SharedMessage response = (SharedMessage) oin.readObject();
                System.out.println("Response type: " + response.getMsgType().name());
                switch (response.getMsgType()) {
                    case USER_REQUEST_USER_INFO_SUCCESS -> {
                        Data.user = response.getClientRequest().getUser();
                        notifyList(response);
                    }
                    case USER_REQUEST_FRIENDS_SUCCESS -> {
                        Data.friends = (List<FriendsRequests>) response.getClientRequest().getList();
                        notifyList(response);
                    }
                    case USER_REQUEST_FRIENDS_FAIL, USER_ACCEPT_FRIEND_REQUEST_SUCCESS -> {
                        notifyList(response);
                    }
                    case USER_REQUEST_FRIENDS_REQUESTS_SUCCESS -> {
                        Data.friendsSent = (List<FriendsRequests>) response.getClientRequest().getList();
                        notifyList(response);
                    }
                    case USER_REQUEST_FRIENDS_REQUESTS_PENDING_SUCCESS -> {
                        Data.friendsPending = (List<FriendsRequests>) response.getClientRequest().getList();
                        notifyList(response);
                    }
                    case USER_REQUEST_GROUPS_SUCCESS -> {
                        Data.myGroups = (List<GroupRequests>) response.getClientRequest().getList();
                        notifyList(response);
                    }
                    case USER_REQUEST_PENDING_GROUPS_SUCCESS -> {
                        Data.pendingGroups = (List<GroupRequests>) response.getClientRequest().getList();
                        notifyList(response);
                    }
                    case USER_REQUEST_AVAILABLE_GROUPS_SUCCESS -> {
                        Data.availableGroups = (List<Group>) response.getClientRequest().getList();
                        notifyList(response);
                    }
                    case USER_REQUEST_MANAGE_GROUPS_SUCCESS -> {
                        Data.manageGroups = (List<Group>) response.getClientRequest().getList();
                        notifyList(response);
                    }
                    case USER_REQUEST_MESSAGES_SUCCESS -> {
                        Data.messages = (List<Message>) response.getClientRequest().getList();
                        notifyList(response);
                    }
                    case USER_REQUEST_USER_SUCCESS -> {
                        Data.selectedFriend = response.getClientRequest().getSelectedUser();
                        notifyList(response);
                    }
                    case USER_MANAGE_GROUP_SUCCESS -> {
                        Data.groupMembers = (List<GroupRequests>) response.getClientRequest().getList();
                        notifyList(response);
                    }
                    default -> {
                        notifyList(response);
                    }
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Couldn't load the message:\r\n\t" + e);
            } catch (IOException e) {
                System.out.println("IOException:\r\n\t" + e);
                e.printStackTrace();
                return;
            }
        }
    }

}
