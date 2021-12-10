package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

import java.net.DatagramPacket;
import java.util.Calendar;

public class ConnectedServer extends Server {

    private DatagramPacket serverPacket;
    private Calendar pingedTime;
    private boolean isSuspended = false;
    private int timeoutPenalties = 0;


    public ConnectedServer(DatagramPacket serverPacket){
        super(serverPacket);
        isSuspended = false;
    }

    public Calendar getPingedTime(){
        return pingedTime;
    }

    public void setPingedTime(Calendar pingedTime){
        this.pingedTime = pingedTime;
    }

    public void setSuspended(boolean state){
        isSuspended = state;
    }

    public void setTimeoutPenalty(){
        timeoutPenalties++;
    }

    public int getTimeoutPenalties(){
        return timeoutPenalties;
    }

    public void resetTimeoutPenalty(){
        timeoutPenalties = 0;
    }
}
