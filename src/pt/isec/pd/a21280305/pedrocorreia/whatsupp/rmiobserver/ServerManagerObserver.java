package pt.isec.pd.a21280305.pedrocorreia.whatsupp.rmiobserver;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.ServerManagerServiceInterface;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ServerManagerObserver extends UnicastRemoteObject implements ServerManagerObserverInterface {
    public ServerManagerObserver() throws RemoteException{}


    @Override
    public void newNotification(SharedMessage message) throws RemoteException {
        System.out.println("\t\n New Notification!");
        System.out.println("\n" + message.getMsgType().name());
        System.out.println("\nMenu: ");
        System.out.println("1. List Active Servers");
        System.out.println("2. Exit");
        System.out.print("\nOption: ");
    }

    public static void main(String[] args){

        String serviceAddress;

        if(args.length == 1){
            serviceAddress = args[0];
        }else{
            System.out.println("Syntax: java ServerManagerObserver <ServerManagerAddress>");
            return;
        }

        try {
            ServerManagerObserver observer = new ServerManagerObserver();

            String objectUrl = "rmi://" + serviceAddress + "/GRDS_Service";

            ServerManagerServiceInterface serverManagerServiceInterface = (ServerManagerServiceInterface) Naming.lookup(objectUrl);

            serverManagerServiceInterface.addObserver(observer);

            boolean exit = false;
            Scanner scanner = new Scanner(System.in);

            System.out.println("\n\tYou will see here the notifications from the Server Manager\n\t");

            System.out.println("\nMenu: ");
            System.out.println("1. List Active Servers");
            System.out.println("2. Exit");

            while(!exit) {
                System.out.print("\nOption: ");
                String input = scanner.nextLine();
                if (input.equals("1")) {
                    String servers = serverManagerServiceInterface.getServers();
                    System.out.println(servers);
                } else if(input.equals("2")) {
                    exit = true;
                }
            }

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
        } finally{
            return;
        }
    }
}
