package pt.isec.pd.a21280305.pedrocorreia.whatsupp.rmiobserver;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.ServerManagerServiceInterface;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerManagerObserver extends UnicastRemoteObject implements ServerManagerObserverInterface {
    public ServerManagerObserver() throws RemoteException{}


    @Override
    public void newNotification(String message) throws RemoteException {
        System.out.println(message);
    }

    public static void main(String[] args){
        try {
            ServerManagerObserver observer = new ServerManagerObserver();

            String objectUrl = "rmi://localhost/GRDS_Service";

            ServerManagerServiceInterface serverManagerServiceInterface = (ServerManagerServiceInterface) Naming.lookup(objectUrl);

            serverManagerServiceInterface.addObserver(observer);

            System.out.println("<Enter> para terminar");
            System.in.read();

            serverManagerServiceInterface.removeObserver(observer);
            UnicastRemoteObject.unexportObject(observer, true);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
