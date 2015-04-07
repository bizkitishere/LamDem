/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.helper;
import java.sql.*;
import java.util.logging.Level;

/**
 * This class loads all the relevant data for performances from the database
 * @author Philippe Lüthi, Elia Kocher
 */
public class DB {
    
   //JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/apodeixis_db";

   //Database credentials
   static final String USER = "root";
   static final String PASS = "";
   
   //Query to load all data
   //TODO create this magical query
   static final String Q_LOADALL = "SELECT * FROM performance";
   
   /**
    * loads all the relevant data for performances from the database
     * @return String containing error details or null
    */
   public static String loadAllData(){
       
       String retVal = null;
       
        //open connection and statement in try as resources -> will be closed automatically 
        try(Connection conn = DriverManager.getConnection(DB_URL,USER,PASS); Statement stmt = conn.createStatement()) {
            
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            
            try (ResultSet rs = stmt.executeQuery(Q_LOADALL)) {
                //Extract data from result set
                while(rs.next()){
                    //Retrieve by column name
                    int id  = rs.getInt("id");
                    String name = rs.getString("name");
                    
                    //Display values
                    //TODO create return objects filled with data...
                    System.out.print("ID: " + id);
                    System.out.println(", name: " + name);
                    
                }
                //end of data
            }
      
        }catch(SQLException | ClassNotFoundException e){
            //TODO reasonable error handling
            String m = DB.class.getName() + e.getCause().getMessage();
            LabDemLogger.LOGGER.log(Level.SEVERE, m);
            retVal = e.getCause().getMessage();
        }
        return retVal;
   }
   
}
