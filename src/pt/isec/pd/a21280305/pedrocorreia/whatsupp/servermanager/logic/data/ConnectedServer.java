package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

import java.net.DatagramPacket;
import java.util.Calendar;

public class ConnectedServer extends Server {

    // private DatagramPacket serverPacket;
    private int listeningUdpPort;
    private String serverAddress;
    private int listeningTcpPort;
    private int filesTcpPort;
    private Calendar pingedTime;
    private boolean isSuspended = false;
    private int timeoutPenalties = 0;
    private DatagramPacket serverPacket;

    public ConnectedServer(DatagramPacket serverPacket) {
        super(serverPacket);
        isSuspended = false;
        this.serverPacket = serverPacket;
        listeningUdpPort = serverPacket.getPort();
        serverAddress = serverPacket.getAddress().getHostAddress();
    }

    public Calendar getPingedTime() {
        return pingedTime;
    }

    public void setPingedTime(Calendar pingedTime) {
        this.pingedTime = pingedTime;
    }

    public void setSuspended(boolean state) {
        isSuspended = state;
    }

    public boolean getSuspended() {
        return isSuspended;
    }

    public void setTimeoutPenalty() {
        timeoutPenalties++;
    }

    public int getTimeoutPenalties() {
        return timeoutPenalties;
    }

    public void resetTimeoutPenalty() {
        timeoutPenalties = 0;
    }

    public int getListeningTcpPort() {
        return listeningTcpPort;
    }

    public int getFilesTcpSocketPort() { return filesTcpPort; }

    public void setListeningTcpPort(int tcpPort) {
        listeningTcpPort = tcpPort;
    }

    public void setFilesTcpPort(int filesTcpPort) { this.filesTcpPort = filesTcpPort; }

    public DatagramPacket getServerPacket() {
        return serverPacket;
    }

    @Override
    public String toString() {
        return "\nServer at " + serverAddress + ":" + listeningUdpPort + " is connected.";
    }
}
