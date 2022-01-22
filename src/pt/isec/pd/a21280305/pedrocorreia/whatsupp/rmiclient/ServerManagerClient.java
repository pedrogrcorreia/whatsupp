package pt.isec.pd.a21280305.pedrocorreia.whatsupp.rmiclient;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.ServerManager;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.ServerManagerServiceInterface;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data.ConnectedServer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerManagerClient extends UnicastRemoteObject implements ServerManagerClientInterface {
    public ServerManagerClient() throws RemoteException{

    }

    public static void main(String[] args){
        ServerManagerClient myRemoteService = null;
        ServerManagerServiceInterface remoteServerService;

        String objectUrl;

        objectUrl = "rmi://localhost/GRDS_Service";

        try{
            remoteServerService = (ServerManagerServiceInterface) Naming.lookup(objectUrl);

            myRemoteService = new ServerManagerClient();

            String received = remoteServerService.getServers();

            System.out.println("Server List: \n\n" + received);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(myRemoteService != null){
                try{
                    UnicastRemoteObject.unexportObject(myRemoteService, true);
                } catch (NoSuchObjectException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
