package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

public class PingServerManager extends Thread {
    private static Server server = null;

    public PingServerManager(Server server){
        this.server = server;
    }

    @Override
    public void run() {
        while(true) {
            server.sendToServerManager(Strings.SERVER_PING);
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //    @Override
//    public void run {
//        server.sendToServerManager(Strings.SERVER_PING);
//        Thread.sleep(3*1000);
//    }
}
