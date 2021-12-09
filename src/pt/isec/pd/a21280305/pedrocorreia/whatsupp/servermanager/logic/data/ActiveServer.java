package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data;

import java.net.DatagramPacket;
import java.util.Calendar;

public class ActiveServer {
    DatagramPacket serverPacket;
    Calendar pingedTime;
    boolean isSuspended;

    public ActiveServer(DatagramPacket serverPacket){
        this.serverPacket = serverPacket;
        isSuspended = false;
    }
}
