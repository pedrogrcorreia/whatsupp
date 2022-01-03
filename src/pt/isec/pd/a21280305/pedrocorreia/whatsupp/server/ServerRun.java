package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ServerRun {

    public static void main(String[] args) throws UnknownHostException, SocketException {

        Server server = null;

        if (args.length == 1) {
            server = new Server(args[0]);
        } else if (args.length == 3) {
            server = new Server(args[0], InetAddress.getByName(args[1]), Integer.parseInt(args[2]));
        } else {
            System.out.println("Syntax: java ServerRun <dbAddress> <ServerManagerAddress>* <ServerManagerPort>*");
            return;
        }
    }
}
