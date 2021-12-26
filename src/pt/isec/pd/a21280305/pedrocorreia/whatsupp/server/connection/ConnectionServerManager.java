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
            if (receivedRequest.getMsgType().name().equalsIgnoreCase("NEW_MESSAGE")) {
                server.alertClients();
            }
            if (receivedRequest.getMsgType().name().equalsIgnoreCase("MESSAGE_SENT_SUCCESS")) {
                server.alertClients();
            }
        }
    }

}
