package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data;

import java.net.DatagramPacket;

import java.util.*;

public class ActiveServers extends Thread{

    private static final int pingTime = 20 * 1000; // 20 seconds between pings

    List<ConnectedServer> servers;

    public ActiveServers(){
        servers = new ArrayList<>();
    }

    public boolean registerServer(DatagramPacket serverPacket){
        boolean isRegistered = false;

        for(ConnectedServer server : servers){
            if(server.getServerPacket().getAddress().equals(serverPacket.getAddress()) && server.getServerPacket().getPort() == serverPacket.getPort()){
                isRegistered = true;
                System.out.println("Server already registered.");
                break;
            }
        }
        if(!isRegistered){
            servers.add(new ConnectedServer(serverPacket));
        }
        return isRegistered;
    }

    public void pingedServer(DatagramPacket serverPacket){
        for(ConnectedServer server : servers){
            if(server.getServerPacket().getAddress().equals(serverPacket.getAddress()) && server.getServerPacket().getPort() == serverPacket.getPort()){
                Calendar curTime = GregorianCalendar.getInstance();
                server.setPingedTime(curTime);
            }
        }
    }

    @Override
    public void run() {
        while(true) {
            //TODO Verify server TCP port
            // contains(Strings.SERVER_PINGING) ...
            List<ConnectedServer> serversToRemove = new ArrayList<>();
            for(ConnectedServer server : servers){
                Calendar curTime = GregorianCalendar.getInstance();
                long delay = (curTime.getTimeInMillis() - server.getPingedTime().getTimeInMillis()) / 1000;
                System.out.println("Delay: " + delay);
                if(delay > pingTime){
                    server.setSuspended(true);
                    server.setTimeoutPenalty();
                    if(server.getTimeoutPenalties() >= 3){
                        System.out.println("A server is offline.");
                        serversToRemove.add(server);
                    }
                }
                else{
                    server.setSuspended(false);
                    server.resetTimeoutPenalty();
                }
            }
            if(serversToRemove.size() > 0) {
                System.out.println("Removing " + serversToRemove.size() + " servers.");
                servers.removeAll(serversToRemove);
            }
            try {
                Thread.sleep(pingTime);
            } catch (InterruptedException e) {
                System.out.println("Error at thread: \r\n\t" + e);
            }
        }
    }
}
