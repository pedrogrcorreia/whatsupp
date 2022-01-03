package pt.isec.pd.a21280305.pedrocorreia.whatsupp;

import java.io.*;
import java.net.Socket;

public class UploadFile extends Thread{

    File file;
    String serverAddress;
    int tcpPort;

    public UploadFile(File file, String serverAddress, int tcpPort){
        this.file = file;
        this.tcpPort = tcpPort;
        this.serverAddress = serverAddress;
    }

    @Override
    public void run() {
        try {
            System.out.println("[UploadFile] Thread to upload files launched..");

            Socket uSocket = new Socket(serverAddress, tcpPort);
            ObjectInputStream uoin = new ObjectInputStream(uSocket.getInputStream());
            ObjectOutputStream uoout = new ObjectOutputStream(uSocket.getOutputStream());
            FileInputStream requestedFile = new FileInputStream(file.getCanonicalPath());

            uoout.writeObject(file.getName());
            uoout.flush();

            int nbytes = 0;
            byte[] fileChunk = new byte[4096];
            do{
                System.out.println("[UploadFile] Uploading file");
                nbytes = requestedFile.read(fileChunk, 0, 4096);
                if(nbytes > 0){
                    uoout.writeObject(fileChunk);
                    uoout.flush();
                }
                else{
                    requestedFile.close();
                    break;
                }
            }while(nbytes > 0);
            requestedFile.close();
            uSocket.close();
            System.out.println("[UploadFile] Thread closing...");
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }finally{
        }
    }
}
