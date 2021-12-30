package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileDownload extends Thread{

    Server server;
    Socket nextClient;
    ServerSocket dSocket;
    public FileDownload(Server server, ServerSocket socket){
        this.server = server;
        this.dSocket = socket;
    }

    @Override
    public void run() {
        System.out.println("Thread to receive download started...");
        while(true) {
            System.out.println("Waiting for requests...");


            try {
                nextClient = dSocket.accept();
                while (!nextClient.isClosed()) {
                    byte[] fileChunk = new byte[4096];
                    File localDirectory = new File("C:\\\\Users\\\\pedro\\\\Desktop\\\\whatsupp\\\\files\\\\");
                    if(!localDirectory.exists()){
                        System.out.println("A criar diretoria");
                        localDirectory.mkdir();
                    }
                    if(!localDirectory.canWrite()){
                        System.out.println("No permissions to write to: " + localDirectory + ".");
                        return;
                    }
                    ObjectOutputStream oout = new ObjectOutputStream(nextClient.getOutputStream());
                    ObjectInputStream oin = new ObjectInputStream(nextClient.getInputStream());
                    FileOutputStream fout = new FileOutputStream(localDirectory + "\\\\testar.txt");

                    do {
                        try {
                            fileChunk = (byte[]) oin.readObject();
                            fout.write(fileChunk);
                        } catch (EOFException e) {
                            break;
                        }
                    } while (true);
                    nextClient.close();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
