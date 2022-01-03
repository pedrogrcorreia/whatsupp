package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.connection;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.DownloadFileClient;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.UploadFile;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

import java.io.File;

/** Class to receive requests from the Server Manager */
public class ConnectionServerManager extends Thread {

    private Server server = null;
    private SharedMessage receivedRequest;

    public ConnectionServerManager(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            receivedRequest = null;
            receivedRequest = server.receiveFromServerManager();
            System.out.println("[Server] Request from ServerManager: " + receivedRequest.getMsgType().name());
            switch(receivedRequest.getMsgType()){
                case SERVER_PING, SERVER_REGISTER_FAIL, SERVER_REGISTER_SUCCESS -> {}
                case FILE_REMOVED_USER, FILE_REMOVED_GROUP -> {
                    server.alertClients(receivedRequest);
                    server.deleteFile(receivedRequest.getFilePath());
                }
                case NEW_FILE_SENT_USER -> {
                    Thread d = new Thread(new DownloadFileClient(new File("./server_"+server.getServerID()+"/downloads/"+receivedRequest.getFilePath()), receivedRequest.getMsg(), receivedRequest.getID()+1));
                    d.start();
                }
                default -> {
                    server.alertClients(receivedRequest);
                }
            }
        }
    }

}
