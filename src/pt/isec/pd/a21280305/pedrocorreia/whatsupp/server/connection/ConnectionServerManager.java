package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

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
            System.out.println(receivedRequest.getMsg());
        }
    }

}
