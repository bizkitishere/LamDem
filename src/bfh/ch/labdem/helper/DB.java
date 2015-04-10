/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.helper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import model.Action;

/**
 * This class loads all the relevant data for performances from the database
 * @author Philippe Lüthi, Elia Kocher
 */
public class DB {
    
   //JDBC driver name and database URL
   private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   private static final String DB_URL = "jdbc:mysql://localhost/apodeixis_db";

   //Database credentials
   private static final String USER = "root";
   private static final String PASS = "root";
   
   private static StringBuilder sb;

    /**
     * loads all the relevant data for performances from the database
     * @param performanceId id of performance
     * @param regionId id of region
     * @param roleId id of role
     * @param enter 1 for enter, 0 for leave
     * @return List<Action> containing all Actions or null
     */
    //public static String getActions(int performanceId, int regionId, int roleId, int enter){
    public static List<Action> getActions(int performanceId, int regionId, int roleId, int enter){
       
        //db query
       sb = new StringBuilder();
       sb.append("SELECT * FROM daemonview");
       sb.append(" WHERE performance_id =").append(performanceId);
       sb.append(" AND region_id =").append(regionId);
       sb.append(" AND role_id =").append(roleId);
       sb.append(" AND enter =").append(enter);
       sb.append(" GROUP BY hw_id");
       
       System.out.println(sb.toString());
       
       List<Action> actions = new ArrayList<>();
       
        //open connection and statement in try as resources -> will be closed automatically 
        try(Connection conn = DriverManager.getConnection(DB_URL,USER,PASS); Statement stmt = conn.createStatement()) {
            
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            
            try (ResultSet rs = stmt.executeQuery(sb.toString())) {
                
                Action action;
                
                //Extract data from result set
                while(rs.next()){
                    //Retrieve by column name
                    
                    String name = rs.getString("name");
                    String command = rs.getString("command");
                    String value = rs.getString("value");
                    int delay = rs.getInt("delay");
                    int type = rs.getInt("type_id");
                    
                    action = new Action(name, command, value, delay, type);
                    actions.add(action);
                    
                    //Display values
                    //TODO create return objects filled with data...
                    System.out.print("name: " + name);
                    System.out.print(", command: " + command);
                    System.out.print(", value: " + value);
                    System.out.print(", delay: " + delay);
                    System.out.print(", type: " + type);
                    System.out.println(" ");
                    
                }
                //end of data
            }
      
        }catch(SQLException | ClassNotFoundException e){
            //TODO reasonable error handling
            String m = DB.class.getName() + "\n" + e.getMessage();
            LabDemLogger.LOGGER.log(Level.SEVERE, m);
            actions = null;
        }
        return actions;
   }
   
}
