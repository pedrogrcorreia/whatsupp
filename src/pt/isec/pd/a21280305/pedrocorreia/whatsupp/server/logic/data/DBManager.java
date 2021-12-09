package pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.data;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.server.logic.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private final Server server;
    private String dbAddress;
    private String dbName;

    public DBManager(Server server){
        this.server = server;
        this.dbAddress = server.getDB();
        String db = "jdbc:mysql://"+this.dbAddress;

        try{
            Class.forName(Strings.JDBC_DRIVE.toString());
        } catch(ClassNotFoundException e){
            System.out.println("Couldn't find JDBC Driver: \r\n\t " + e);
            return;
        }

        try{
            Connection con = DriverManager.getConnection(db, "root", "BCMFMAsp13");

            System.out.println("Connection successful.");
        } catch (SQLException e) {
            System.out.println("Couldn't connect to Database: \r\n\t " + e);
        }
    }
}
