package Package;

import java.sql.*;


public class DataBaseConnectionClass {

    // METHOD TO CONNECT TO THE DATABASE
    public static Connection connect(){

        Connection conn = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:todo.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        //System.out.println("Opened database successfully");
        return conn;  // return Connection conn object
    }

}
