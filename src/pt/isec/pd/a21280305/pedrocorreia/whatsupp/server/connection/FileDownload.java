package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileDownload extends Thread{

    Server server;

    public FileDownload(Server server){
        this.server = server;
    }

    @Override
    public void run() {

        Socket client;
        try (ServerSocket transfer = new ServerSocket(0)) {
            while(true){
                client = transfer.accept();
                Socket finalClient = client;
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ObjectInputStream oin = (ObjectInputStream) finalClient.getInputStream();
                            ObjectOutputStream oout = (ObjectOutputStream) finalClient.getOutputStream();

                            FileInputStream requestsFile = new FileInputStream("C:\\Users\\pedro\\Desktop\\whatsupp\\docs\\Statement.pdf\\");
                            int nbytes = 0;
                            do{
                                byte[] fileChunk = new byte[8192];
                                nbytes = requestsFile.read(fileChunk, 0,8192);

                                if(nbytes > 0){
                                    oout.writeObject(fileChunk);
                                    oout.flush();
                                }
                                else{
                                    break;
                                }
                            }while(nbytes > 0);

                            requestsFile.close();
                        } catch(IOException e){}
                    }
                };
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
