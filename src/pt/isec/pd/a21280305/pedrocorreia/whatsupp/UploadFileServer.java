package pt.isec.pd.a21280305.pedrocorreia.whatsupp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class UploadFileServer extends Thread{

    File file;
    ServerSocket socket;
    Socket conSocket;

    public UploadFileServer(File file, ServerSocket socket){
        this.file = file;
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("[UploadFileServer] Thread to upload files as a server started...");
        while(true) {
            System.out.println("Waiting for requests...");
            try {
                conSocket = socket.accept();
                while (!conSocket.isClosed()) {

                    ObjectInputStream uoin = new ObjectInputStream(conSocket.getInputStream());
                    ObjectOutputStream uoout = new ObjectOutputStream(conSocket.getOutputStream());
                    FileInputStream requestedFile = new FileInputStream(file.getCanonicalPath());

                    int nbytes = 0;
                    byte[] fileChunk = new byte[4096];
                    do{
                        System.out.println("[UploadFileServer] Uploading file");
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
                    System.out.println("[UploadFileServer] Upload finished.");
                    requestedFile.close();;
                    conSocket.close();
                    System.out.println("[UploadFileServer] Thread closing...");
                    break;
                }
                return;
            }catch (SocketTimeoutException e){
                System.out.println("[UploadFileServer] No more servers to receive the file... closing.");
                return;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
