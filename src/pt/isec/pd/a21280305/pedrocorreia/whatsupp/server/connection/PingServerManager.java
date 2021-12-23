package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

/**
 * Class to ping the Server Manager,
 * telling that the server is alive.
 */

public class PingServerManager extends Thread {
    private Server server = null;
    private static final int pingTime = 3 * 1000;// 20 * 1000; // 20 seconds between pings

    public PingServerManager(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {

            server.sendToServerManager(
                    new SharedMessage(Strings.SERVER_PING, new String(String.valueOf(server.getTcpPort()))));

            try {
                Thread.sleep(pingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
