package pt.isec.pd.a21280305.pedrocorreia.whatsupp.rmiobserver;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerManagerObserverInterface extends Remote {
    void newNotification(String message) throws RemoteException;
}
