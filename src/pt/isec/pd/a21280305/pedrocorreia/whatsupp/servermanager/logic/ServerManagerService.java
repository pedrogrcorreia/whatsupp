package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.rmiobserver.ServerManagerObserverInterface;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data.ConnectedServer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ServerManagerService extends UnicastRemoteObject implements ServerManagerServiceInterface {
    public static final String SERVICE_NAME = "GRDS_Service";

    List<ServerManagerObserverInterface> observers;

    private ServerManager serverManager;

    public ServerManagerService(List<ServerManagerObserverInterface> observers, ServerManager serverManager) throws RemoteException{
//        observers = new ArrayList<>();
        this.serverManager = serverManager;
        this.observers = observers;
    }

    public ServerManagerService(ServerManager serverManager) throws RemoteException{
//        this.serverManager = serverManager;
    }

    @Override
    public String getServers() throws RemoteException, IOException {
        var servers = serverManager.getActiveServers().getServers();
        StringBuilder sb = new StringBuilder();
        for(var server : servers){
            Calendar curTime = GregorianCalendar.getInstance();
            long delay = (curTime.getTimeInMillis() - server.getPingedTime().getTimeInMillis()) / 1000;
            sb.append("\nServer Tcp Port: " + server.getListeningTcpPort());
            sb.append("\nServer Tcp Port for files: " + server.getFilesTcpSocketPort());
            sb.append("\nServer address: " + server.getServerPacket().getAddress().getHostAddress() + "\n\t");
            sb.append("Last ping time: " + delay + " seconds ago.");
        }

        return sb.toString();
    }

    // RMI


    @Override
    public synchronized void addObserver(ServerManagerObserverInterface observer) throws RemoteException {
        if(!observers.contains(observer)){
            observers.add(observer);
            System.out.println(observers.size());
            System.out.println("Registado um novo observador.");
        }
    }

    @Override
    public synchronized void removeObserver(ServerManagerObserverInterface observer) throws RemoteException {
        if(observers.remove(observer)){
            System.out.println("Removido um observador.");
        }
    }

    protected synchronized void notifyObservers(SharedMessage msg){

        for (ServerManagerObserverInterface observer : observers) {
            System.out.println("a notificar observer");
            try {
                observer.newNotification(msg.getMsg());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void startService(ServerManager serverManager){
        // RMI
        observers = new ArrayList<>();
        try{
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            System.out.println("Registry lancado!");

            ServerManagerService serverService = new ServerManagerService(observers, serverManager);
            Naming.bind("rmi://localhost/" + SERVICE_NAME, serverService);
            System.out.println("Servico GetRemoteFile criado e em execucao ("+serverService.getRef().remoteToString()+"...");
            System.out.println("Servico " + SERVICE_NAME + " registado no registry");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

}
