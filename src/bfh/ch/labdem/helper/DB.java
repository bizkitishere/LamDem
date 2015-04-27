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
 * Used to connect to the database and load data using JDBC
 * @author Philippe Lüthi, Elia Kocher
 */
public class DB {
    
   //JDBC driver name and database URL
   private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   private static final String DB_URL = "jdbc:mysql://localhost/apodeixis_db";

   //Database credentials
   private static final String USER = "apodeixisUser";
   private static final String PASS = "userPassApo15";   
   
   //db views
   private static final String DAEMONVIEW = "daemonview";
   
   //db column names
   private static final String PERFORMANCE_ID = "performance_id";
   private static final String REGION_ID = "region_id";
   private static final String ROLE_ID = "role_id";
   private static final String ENTER = "enter";
   private static final String HW_ID = "hw_id";
   private static final String HW_NAME = "hw_name";
   private static final String COMMAND = "command";
   private static final String VALUE = "value";
   private static final String DELAY = "delay";
   private static final String TYPE_ID = "type_id";
   private static final String ACTIONS_SELECT = HW_NAME + "," + COMMAND + "," + VALUE + "," + DELAY + "," + TYPE_ID;
   
   //private static StringBuilder sb;
   
    /**
     * loads all actions associated with the given performance, region, role, enter/exit
     * @param performanceId id of performance
     * @param regionId id of region
     * @param roleId id of role
     * @param enter 1 for enter, 0 for leave
     * @return List<Action> containing a list of actions or null
     */
    public static List<Action> getActions(int performanceId, int regionId, int roleId, int enter){
       
       //use a new Stringbuilder that will be empty
       //sb = new StringBuilder();
       //db query
        /*
       sb.append("SELECT ").append(ACTIONS_SELECT).append(" FROM ").append(DAEMONVIEW);
       sb.append(" WHERE ").append(PERFORMANCE_ID).append(" = ").append(performanceId);
       sb.append(" AND ").append(REGION_ID).append(" =").append(regionId);
       sb.append(" AND ").append(ROLE_ID).append(" =").append(roleId);
       sb.append(" AND ").append(ENTER).append(" = ").append(enter);
       sb.append(" GROUP BY ").append(HW_ID);
       */
       
       //System.out.println(sb.toString());
       
       //query to get all actions with the given parameters
       String getActions = String.format(
            "SELECT %s FROM %s"
          + " WHERE %s = %d"
          + " AND %s = %d"     
          + " AND %s = %d"
          + " AND %s = %d"
          + " GROUP BY %s"
          , ACTIONS_SELECT, DAEMONVIEW, PERFORMANCE_ID, performanceId, REGION_ID, regionId, ROLE_ID, roleId, ENTER, enter, HW_ID
       );
       
       //System.out.println(getActions);
       
       List<Action> actions = new ArrayList<>();
       
        //open connection and statement in try as resources -> will be closed automatically 
        try(Connection conn = DriverManager.getConnection(DB_URL,USER,PASS); Statement stmt = conn.createStatement()) {
            
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);
            
            //query the db
            try (ResultSet rs = stmt.executeQuery(getActions)) {
                
                Action action;
                
                //Extract data from result set
                while(rs.next()){
                    //Retrieve by column name
                    String name = rs.getString(HW_NAME);
                    String command = rs.getString(COMMAND);
                    String value = rs.getString(VALUE);
                    int delay = rs.getInt(DELAY);
                    int typeId = rs.getInt(TYPE_ID);
                    
                    //create a list of Action objects with the retrieved values
                    action = new Action(name, command, value, delay, typeId);
                    actions.add(action); 
                }
            }
        }catch(SQLException | ClassNotFoundException e){
            //e.printStackTrace();
            String m = DB.class.getName() + "\nCause: " + e.getCause() + " -  Message: " + e.getMessage();
            LabDemLogger.LOGGER.log(Level.WARNING, m);
            actions = null;
        }
        return actions;
   }
   
}
