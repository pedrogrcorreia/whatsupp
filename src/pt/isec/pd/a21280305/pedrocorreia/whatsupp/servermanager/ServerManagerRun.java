package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.ServerManager;
// import pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.ui.console.UIConsole;

import java.io.IOException;

public class ServerManagerRun {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        if (args.length != 1) {
            System.out.println("Syntax: java ServerManagerRun <listeningPort>");
            return;
        }

        ServerManager serverManager = new ServerManager(Integer.parseInt(args[0]));
        serverManager.startServerManager();
        // serverManager.runServerManager();
        // UIConsole uiConsole = new UIConsole(serverManager);
    }
}
