package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.requests;

import java.io.*;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.FriendsRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Group;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.GroupRequests;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.User;

public class ClientRequests implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected User user;
    protected User selectedUser;
    protected List<?> list;
    protected Message selectedMessage;
    protected Group group;
    protected byte[] fileChunk = new byte[4096];
    int port;

    public ClientRequests(User user) {
        this.user = user;
    }

    /**
     * Constructor to get an user info
     * 
     * @param user
     * @param selectedUser
     */
    public ClientRequests(User user, User selectedUser) {
        this.user = user;
        this.selectedUser = selectedUser;
    }

    /**
     * Constructor to get messages
     * 
     * @param user
     * @param selectedUser
     * @param list
     */
    public ClientRequests(User user, User selectedUser, List<?> list) {
        this.user = user;
        this.selectedUser = selectedUser;
        this.list = list;
    }

    /**
     * Constructor to delete a message
     * 
     * @param user
     * @param selectedMessage
     */
    public ClientRequests(User user, Message selectedMessage) {
        this.user = user;
        this.selectedMessage = selectedMessage;
    }

    /** Constructor to send a message */
    public ClientRequests(User user, User selectedUser, Message selectedMessage) {
        this.user = user;
        this.selectedUser = selectedUser;
        this.selectedMessage = selectedMessage;
    }

    /** Constructor to create a new group */
    public ClientRequests(User user, Group group){
        this.user = user;
        this.group = group;
    }

    public ClientRequests(User user, List<?> list){
        this.user = user;
        this.list = list;
    }

    public ClientRequests(Group group){
        this.group = group;
    }

    public ClientRequests(User user, Group group, Message message){
        this.user = user;
        this.group = group;
        this.selectedMessage = message;
    }

    public ClientRequests(User user, User selectedUser, Group group){
        this.user = user;
        this.group = group;
        this.selectedUser = selectedUser;
    }

    public ClientRequests(User user, Message message, byte[] fileChunk){
        this.user = user;
        this.selectedMessage = message;
        this.fileChunk = fileChunk;
    }

    public ClientRequests(User user, Message message, int port){
        this.user = user;
        this.selectedMessage = message;
        this.port = port;
    }

    /** Login and Register */
    public boolean login(ObjectInputStream oin, ObjectOutputStream oout, List<SharedMessage> list) {
        return false;
    }

    /** Method on LoginRegister class */
    public boolean register(ObjectInputStream oin, ObjectOutputStream oout, List<SharedMessage> list) {
        return false;
    }

    /** GENERIC METHOD TO SEND REQUESTS IM SO DUMB */

    public boolean sendRequest(ObjectOutputStream oout, Strings requestMsg){
        try {
            SharedMessage msgToSend = new SharedMessage(requestMsg, this, this.getFileChunk());

            oout.writeObject(msgToSend);
            oout.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error receiving the message:\r\n\t" + e);
            return false;
        }
    }

//    public boolean sendFileRequest(ObjectOutputStream oout, Strings requestMsg){
//        try{
//            SharedMessage msgToSend = new SharedMessage(requestMsg, this);
//            oout.writeObject();
//        }
//    }

    public User getUser() {
        return user;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public List<?> getList() { return list; }

    public Message getSelectedMessage() {
        return selectedMessage;
    }

    public Group getGroup(){
        return group;
    }

    public byte[] getFileChunk(){
        return fileChunk;
    }

    public int getPort(){ return port; }
}
