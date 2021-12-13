package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

public class PingServerManager extends Thread {
    private Server server = null;
    private static final int pingTime = 3 * 1000;// 20 * 1000; // 20 seconds between pings

    public PingServerManager(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {

            // TODO Send the TCP port
            // Change sendToServerManager method to accept String instead of Strings
            // So it is possible to concatenate the message with the server port
            // server.sendToServerManager(Strings.SERVER_PING);
            server.sendToServerManager(
                    new SharedMessage(Strings.SERVER_PING, new String(String.valueOf(server.getTcpPort()))));

            try {
                Thread.sleep(pingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // @Override
    // public void run {
    // server.sendToServerManager(Strings.SERVER_PING);
    // Thread.sleep(3*1000);
    // }
}
