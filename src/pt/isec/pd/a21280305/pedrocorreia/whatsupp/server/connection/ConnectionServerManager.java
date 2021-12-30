package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

/** Class to receive requests from the Server Manager */
public class ConnectionServerManager extends Thread {

    private Server server = null;
    private SharedMessage receivedRequest;

    public ConnectionServerManager(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            receivedRequest = server.receiveFromServerManager();
            System.out.println("Request from ServerManager: " + receivedRequest.getMsgType().name());
            switch(receivedRequest.getMsgType()){
                case NEW_MESSAGE_USER, NEW_MESSAGE_GROUP, NEW_FRIEND, REMOVED_FRIEND, NEW_GROUP,
                        QUIT_GROUP, CHANGE_NAME, NEW_GROUP_REQUEST,
                        ACCEPTED_GROUP_REQUEST,
                        DELETED_GROUP -> server.alertClients(receivedRequest);
            }
//            if (receivedRequest.getMsgType().name().equalsIgnoreCase("NEW_MESSAGE")) {
//                server.alertClients(receivedRequest);
//            }
//            if (receivedRequest.getMsgType().name().equalsIgnoreCase("NEW_FRIEND")) {
//                server.alertClients(receivedRequest);
//            }
//            if (receivedRequest.getMsgType().name().equalsIgnoreCase("REMOVED_FRIEND")) {
//                server.alertClients(receivedRequest);
//            }
//            if(receivedRequest.getMsgType().name().equalsIgnoreCase(("NEW_GROUP"))){
//                server.alertClients(receivedRequest);
//            }
        }
    }

}
