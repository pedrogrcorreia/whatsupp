package pt.isec.pd.a21280305.pedrocorreia.whatsupp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class DownloadFile extends Thread{

    File file;
    ServerSocket socket;
    Socket conSocket;

    public DownloadFile(File file, ServerSocket socket){
        this.file = file;
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("[DownloadFile] Thread to receive download started...");
        while(true) {
            System.out.println("[DownloadFile] Waiting for requests...");
            File localDirectory;
            String requestedFileName, requestedCanonicalFilePath = null;
            FileOutputStream fout;
            try {
                conSocket = socket.accept();
                while (!conSocket.isClosed()) {
                    byte[] fileChunk = new byte[4096];
                    localDirectory = new File(file.getPath().trim());
                    if(!localDirectory.exists()){
                        System.out.println("[DownloadFile] Creating directory...");
                        localDirectory.mkdirs();
                    }
                    if(!localDirectory.canWrite()){
                        System.out.println("[DownloadFile] No permissions to write to: " + localDirectory + ".");
                        return;
                    }

                    System.out.println("[DownloadFile] Local directory: " + localDirectory);
                    ObjectOutputStream oout = new ObjectOutputStream(conSocket.getOutputStream());
                    ObjectInputStream oin = new ObjectInputStream(conSocket.getInputStream());

                    requestedFileName = (String) oin.readObject();
                    requestedCanonicalFilePath = new File(localDirectory + File.separator + requestedFileName).getCanonicalPath();
                    fout = new FileOutputStream(requestedCanonicalFilePath);

                    if (!requestedCanonicalFilePath.startsWith(localDirectory.getCanonicalPath() + File.separator)) {
                        System.out.println("[DownloadFile] Forbidden access to the file " + requestedCanonicalFilePath + ".");
                        System.out.println("[DownloadFile] Root directory isn't the same as " + localDirectory.getCanonicalPath() + ".");
                        return;
                    }

                    System.out.println("[DownloadFile] File " + requestedCanonicalFilePath + " open to write");
                    do {
                        System.out.println("[DownloadFile] Writing to file");
                        try {
                            fileChunk = (byte[]) oin.readObject();
                            fout.write(fileChunk, 0, 4096);
                        } catch (EOFException e) {
                            fout.flush();
                            break;
                        }
                    } while (true);
                    System.out.println("[DownloadFile] Download finished");
                    fout.close();
                    conSocket.close();
                    System.out.println("[DownloadFile] Closing thread...");
                    break;
                }
                return;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("[DownloadFile] Exception:\n\t" + e);
                return;
            }
        }
    }
}
