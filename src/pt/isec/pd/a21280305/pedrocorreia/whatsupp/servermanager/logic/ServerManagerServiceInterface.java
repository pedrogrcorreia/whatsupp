package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.rmiobserver.ServerManagerObserverInterface;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data.ConnectedServer;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerManagerServiceInterface extends Remote {

    public String getServers() throws RemoteException, IOException;

    public void addObserver(ServerManagerObserverInterface observer) throws RemoteException;
    public void removeObserver(ServerManagerObserverInterface observer) throws RemoteException;
}
