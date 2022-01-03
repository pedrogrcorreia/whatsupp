package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data;

import java.io.*;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.FriendsRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Group;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.GroupRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;

public class Notifications extends Data implements Runnable {

    ObjectInputStream oin;
    ObjectOutputStream oout;
    List<SharedMessage> notLog;

    public Notifications(String serverManagerAddress, int serverManagerPort, ObjectInputStream oin, ObjectOutputStream oout, List<SharedMessage> notLog) {
        super(serverManagerAddress, serverManagerPort);
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
                switch (response.getMsgType()) {
                    case USER_REQUEST_USER_INFO_SUCCESS -> {
                        Data.user = response.getClientRequest().getUser();
                        notifyList(response);
                    }
                    case USER_REQUEST_FRIENDS_SUCCESS -> {
                        Data.friends = (List<FriendsRequests>) response.getClientRequest().getList();
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
                    case USER_UPDATE_INFO_SUCCESS ->{
                        Data.user = response.getClientRequest().getUser();
                        notifyList(response);
                    }
                    default -> {
                        notifyList(response);
                    }
                }
            } catch (ClassNotFoundException e) {
                System.out.println("[Notifications] Couldn't load the message:\r\n\t" + e);
            }catch(SocketException e){
                notifyList(new SharedMessage(Strings.LOST_CONNECTION, new String("Lost connection with server...")));
            } catch (IOException e) {
                System.out.println("[Notifications] IOException:\r\n\t" + e);
                e.printStackTrace();
            }
        }
    }

}
