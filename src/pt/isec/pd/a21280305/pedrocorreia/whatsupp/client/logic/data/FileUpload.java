package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables.Message;

import java.io.*;
import java.net.Socket;

public class FileUpload extends Thread {

    String serverAddress;
    Message file;

    public FileUpload(String serverAddress, Message file){
        this.serverAddress = serverAddress;
        this.file = file;
    }

    @Override
    public void run() {
        try {
            Socket uSocket = new Socket(serverAddress,55504);
            ObjectInputStream uoin = new ObjectInputStream(uSocket.getInputStream());
            ObjectOutputStream uoout = new ObjectOutputStream(uSocket.getOutputStream());
            FileInputStream requestedFile = new FileInputStream(file.getFile().getPath());

            int nbytes = 0;
            byte[] fileChunk = new byte[4096];
            do{
                System.out.println("Writing a file");
                nbytes = requestedFile.read(fileChunk, 0, 4096);
                if(nbytes > 0){
                    uoout.writeObject(fileChunk);
                    uoout.flush();
                }
                else{
                    break;
                }
            }while(nbytes > 0);
            requestedFile.close();
            uSocket.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        }
    }
}
