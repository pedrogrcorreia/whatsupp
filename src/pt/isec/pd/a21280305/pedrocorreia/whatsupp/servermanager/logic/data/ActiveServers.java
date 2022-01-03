package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data;

import java.net.DatagramPacket;

import java.util.*;

public class ActiveServers extends Thread {

    private static final int pingTime = 20 * 1000;// 20 * 1000; // 20 seconds between pings

    List<ConnectedServer> servers;
    int lastServer = 0;

    public ActiveServers() {
        servers = new ArrayList<>();
        start();
    }

    public boolean registerServer(DatagramPacket serverPacket) {
        boolean isRegistered = false;

        for (ConnectedServer server : servers) {
            if (server.getServerPacket().getAddress().equals(serverPacket.getAddress())
                    && server.getServerPacket().getPort() == serverPacket.getPort()) {
                isRegistered = true;
                System.out.println("[ActiveServers] Server already registered.");
                break;
            }
        }
        if (!isRegistered) {
            servers.add(new ConnectedServer(serverPacket));
        }
        return isRegistered;
    }

    public void pingedServer(DatagramPacket serverPacket, int tcpPort, int filesPort) {
        for (ConnectedServer server : servers) {
            if (server.getServerPacket().getAddress().equals(serverPacket.getAddress())
                    && server.getServerPacket().getPort() == serverPacket.getPort()) {
                Calendar curTime = GregorianCalendar.getInstance();
                server.setPingedTime(curTime);
                server.setListeningTcpPort(tcpPort);
                server.setFilesTcpPort(filesPort);
            }
        }
    }

    public List<ConnectedServer> getServers() {
        return servers;
    }


    public String registerClient() {
        if (lastServer == servers.size()) {
            lastServer = 0;
        }
        for (int i = lastServer; i < servers.size(); i++) {
            if (!servers.get(i).getSuspended()) {
                lastServer++;
                return new String(
                        servers.get(i).getServerPacket().getAddress() + ":" + servers.get(i).getListeningTcpPort()) + ":" + servers.get(i).getFilesTcpSocketPort();
            }
        }
        return null;
    }

    public int getPortForTcp(DatagramPacket serverPacket){
        for(int i = 0; i < servers.size(); i++){
            if(servers.get(i).getServerPacket().getPort() == serverPacket.getPort()){
                return servers.get(i).getFilesTcpSocketPort();
            }
        }
        return -1;
    }

    @Override
    public void run() {
        while (true) {
            List<ConnectedServer> serversToRemove = new ArrayList<>();
            for (ConnectedServer server : servers) {
                Calendar curTime = GregorianCalendar.getInstance();
                long delay = (curTime.getTimeInMillis() - server.getPingedTime().getTimeInMillis()) / 1000;
                if ((delay * 1000) > pingTime) {
                    server.setSuspended(true);
                    server.setTimeoutPenalty();
                    if (server.getTimeoutPenalties() >= 3) {
                        System.out.println("[ActiveServers] A server is offline.");
                        serversToRemove.add(server);
                    }
                } else {
                    server.setSuspended(false);
                    server.resetTimeoutPenalty();
                }
            }
            if (serversToRemove.size() > 0) {
                System.out.println("[ActiveServers] Removing " + serversToRemove.size() + " servers.");
                servers.removeAll(serversToRemove);
            }
            try {
                Thread.sleep(pingTime);
            } catch (InterruptedException e) {
                System.out.println("[ActiveServers] Error at thread: \r\n\t" + e);
            }
        }
    }
}
