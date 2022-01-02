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

//    private static void uploadFileToServer(SharedMessage request){
//        System.out.println(request.getClientRequest().getSelectedMessage().getFile().getPath());
//        try {
//            FileInputStream fileInputStream = new FileInputStream(request.getClientRequest().getSelectedMessage().getFile().getPath());
//            int nbytes = 0;
//            byte[] fileChunk = new byte[8192];
////            do{
//                nbytes = fileInputStream.read(fileChunk, 0, 4096);
//
//                if(nbytes > 0){
//                    SharedMessage file = new SharedMessage(Strings.UPLOAD_FILE, new String(""));
//                    Data.oout.writeObject(file);
//                    Data.oout.flush();
//                }
////                else{
////                    break;
////                }
////            } while(nbytes > 0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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
            }catch(SocketException e){
                notifyList(new SharedMessage(Strings.LOST_CONNECTION, new String("Lost connection with server...")));
//                contactServerManager();
                return;
            } catch (IOException e) {
                System.out.println("IOException:\r\n\t" + e);
                e.printStackTrace();
                return;
            }
        }
    }

}
