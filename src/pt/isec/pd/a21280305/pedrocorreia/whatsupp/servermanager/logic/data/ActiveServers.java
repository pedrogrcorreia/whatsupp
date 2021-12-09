package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

import java.net.DatagramPacket;
import java.net.ServerSocket;

import java.util.*;

public class ActiveServers extends Thread{
    List<DatagramPacket> activeServers;
    List<DatagramPacket> suspendServers;
    Map<DatagramPacket, Calendar> pingedServers;

    public ActiveServers(){
        activeServers = new ArrayList<>();
    }

    public boolean registerServer(DatagramPacket serverPacket){
        boolean isRegistered = false;

        for(DatagramPacket server : activeServers){
            if(server.getAddress().equals(serverPacket.getAddress()) && server.getPort() == serverPacket.getPort()){
                isRegistered = true;
                System.out.println("Server already registered.");
                break;
            }
        }
        for(DatagramPacket server : suspendServers){
            if(server.getAddress().equals(serverPacket.getAddress()) && server.getPort() == serverPacket.getPort()){
                isRegistered = true;
                System.out.println("Server already registered.");
                break;
            }
        }

        return isRegistered;
    }

    public void pingedServer(DatagramPacket serverPacket){
        for(DatagramPacket server : activeServers){
            if(server.getAddress().equals(serverPacket.getAddress()) && server.getPort() == serverPacket.getPort()){
                Calendar curTime = GregorianCalendar.getInstance();
                pingedServers.put(serverPacket, curTime);
            }
        }
    }

    @Override
    public void run() {
        while(true) {
            for (Map.Entry entry : pingedServers.entrySet()) {
                Calendar curTime = GregorianCalendar.getInstance();
                Calendar entryTime = (Calendar) entry.getValue();
                long diff = (curTime.getTimeInMillis() - entryTime.getTimeInMillis()) / 1000;
                if(diff > 20){
                    activeServers.remove(entry.getKey());
                    suspendServers.add((DatagramPacket) entry.getKey());
                }
            }
        }
    }
}
