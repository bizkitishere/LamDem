/*
 *  DB.java
 *  LabDem
 *
 *  Copyright (c) 2015 BFH. All rights reserved.
 */
package bfh.ch.labdem.helper;
import bfh.ch.labdem.main.BfhChLabDem.PropertyKeys;
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
   private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   private final String DB_URL = "jdbc:mysql://localhost/apodeixis_db";

   //Database credentials
   private final String USER;
   private final String PASS;
   
   //db views
   private final String DAEMONVIEW = "daemonview";
   
   //db column names
   private final String PERFORMANCE_ID = "performance_id";
   private final String REGION_ID = "region_id";
   private final String ROLE_ID = "role_id";
   private final String ENTER = "enter";
   private final String HW_ID = "hw_id";
   private final String HW_NAME = "hw_name";
   private final String COMMAND = "command";
   private final String VALUE = "value";
   private final String DELAY = "delay";
   private final String TYPE_ID = "type_id";
   private final String ACTIONS_SELECT = HW_NAME + "," + COMMAND + "," + VALUE + "," + DELAY + "," + TYPE_ID;
   
   public DB(){
       //get user and password
       USER = PropertiesLoader.getProperty(PropertyKeys.DBUser.toString());
       PASS = PropertiesLoader.getProperty(PropertyKeys.DBPass.toString());
       
       
       //try to connect to db
       try (Connection conn = DriverManager.getConnection(DB_URL,USER,PASS)){
           //nothing to do here, just test if the conenction could be established
           conn.close();
       } catch (SQLException e) {
           //problem with database, shut program down
           logAndTerminate(e);
       }
   }
   
    /**
     * loads all actions associated with the given performance, region, role, enter/exit
     * @param performanceId id of performance
     * @param regionId id of region
     * @param roleId id of role
     * @param enter 1 for enter, 0 for leave
     * @return List containing a list of actions or null if the query did not fetch any rows
     */
    public List<Action> getActions(int performanceId, int regionId, int roleId, int enter){

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
            //error with database, shut program down
            logAndTerminate(e);
        }
        return actions;
   }
   
    /**
     * shut the application down if no connection to the db could be established
     */
    private void logAndTerminate(Exception e){
        LabDemLogger.logErrTemplate(Level.SEVERE, DB.class.getSimpleName(), e.getClass().getSimpleName(), e.getMessage());
        LabDemLogger.LOGGER.log(Level.SEVERE, LabDemLogger.TERMINATED);
        System.exit(2);
    }
    
}
