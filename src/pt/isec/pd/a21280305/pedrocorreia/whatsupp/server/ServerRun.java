package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServerRun {

    public static void main(String[] args) throws UnknownHostException {

        Server server = null;

        if(args.length == 2){
            // TODO MULTICAST SEARCH
            System.out.println("Not implemented yet.");
        }
        else if(args.length == 4){
            server = new Server(Integer.parseInt(args[0]), args[1], InetAddress.getByName(args[2]), Integer.parseInt(args[3]));
        }
        else{
            System.out.println("Syntax: java ServerRun <listeningPort> <dbAddress> <ServerManagerAddress>* <ServerManagerPort>*");
            return;
        }
        

        server.registerServer();
    }

//    static final String JDBC_DRIVER = "com.mysql.c.jdbc.Driver";
//    private static int x;
//    String dbAddress;
//
//    private String dbName;
//    private String username;
//    private String password;
//
//    int x;
//
//    private Connection con;
//
//    private static void connectDB(){
//        String db = "jdbc:mysql://" + ip + "/" + dbName;
//
//        try{
//            Class.forName(JDBC_DRIVER);
//        }catch(ClassNotFoundException e){
//            System.out.println("Exception with JDBC: \r\n\t" + e);
//        }
//        try {
//            con = DriverManager.getConnection(db, username, password);
//        }catch(SQLException e){
//            System.out.println("Error getting connection: \r\n\t" + e);
//        }
//    }
//
//    public static void main(String[] args){
//
//        if(args.length != 4){
//            System.out.println("Syntax: <dbAddress> <dbName> <dbUsername> <dbPassword>");
//            return;
//        }
//
//        connectDB()
//    }
}
