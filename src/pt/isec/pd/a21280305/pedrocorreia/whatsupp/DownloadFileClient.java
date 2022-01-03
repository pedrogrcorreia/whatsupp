package pt.isec.pd.a21280305.pedrocorreia.whatsupp;

import java.io.*;
import java.net.Socket;

public class DownloadFileClient extends Thread{

    File file;
    String serverAddress;
    int tcpPort;

    public DownloadFileClient(File file, String serverAddress, int tcpPort){
        this.file = file;
        this.tcpPort = tcpPort;
        this.serverAddress = serverAddress;
    }

    @Override
    public void run(){
        while(true) {
            try {
                Socket uSocket = new Socket(serverAddress, tcpPort);
                System.out.println("[DownloadFileClient] Thread to download files launched..");
                System.out.println("[DownloadFileClient] Waiting for requests...");
                byte[] fileChunk = new byte[4096];
                File localDirectory;
                String requestedFileName, requestedCanonicalFilePath = null;
                FileOutputStream fout;

                localDirectory = new File(file.getPath().trim());
                requestedFileName = localDirectory.getName();
                localDirectory = new File(localDirectory.getAbsolutePath().substring(0, localDirectory.getAbsolutePath().lastIndexOf("\\")));


                if (!localDirectory.exists()) {
                    System.out.println("[DownloadFileClient] Creating directories");
                    localDirectory.mkdirs();
                }
                if (!localDirectory.canWrite()) {
                    System.out.println("[DownloadFileClient] No permissions to write to: " + localDirectory + ".");
                    return;
                }

                System.out.println("[DownloadFileClient] Local directory: " + localDirectory);
                ObjectOutputStream oout = new ObjectOutputStream(uSocket.getOutputStream());
                ObjectInputStream oin = new ObjectInputStream(uSocket.getInputStream());

//            requestedFileName = (String) oin.readObject();
                requestedCanonicalFilePath = new File(localDirectory + File.separator + requestedFileName).getCanonicalPath();
//            requestedCanonicalFilePath = localDirectory.getCanonicalPath();
                fout = new FileOutputStream(requestedCanonicalFilePath);

                if (!requestedCanonicalFilePath.startsWith(localDirectory.getCanonicalPath() + File.separator)) {
                    System.out.println("[DownloadFileClient] Forbidden access to the file " + requestedCanonicalFilePath + ".");
                    System.out.println("[DownloadFileClient] Root directory isn't the same as " + localDirectory.getCanonicalPath() + ".");
                    return;
                }

                System.out.println("[DownloadFileClient] File " + requestedCanonicalFilePath + " open to write");
                do {
                    System.out.println("[DownloadFileClient] Downloading to file..");
                    try {
                        fileChunk = (byte[]) oin.readObject();
                        fout.write(fileChunk, 0, 4096);
                    } catch (EOFException e) {
                        fout.flush();
                        break;
                    }
                } while (true);
                System.out.println("[DownloadFileClient] Download finished.");
                fout.close();
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
            }
        }
    }
}
