/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.helper;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * Class used to log into a single file, using a single logger
 * Contains strings for log messages
 * @author Philippe Lüthi, Elia Kocher
 */
public class LabDemLogger {
    
    private final static String LOG_PATH = "log/";
    private final static String LOG_NAME = "log.log";
    public final static Logger LOGGER = Logger.getLogger("LabDem");
    
    //logg messages ---
    //
    //general
    public final static String STARTED = "Started";
    //add class name, exeption name and exception message in String.format()
    public final static String ERR_TEMPLATE = "class: %s  -  exception: %s  -  message: %s";
    public final static String ERR_OFFLINE_TEMPLATE = "The following Service is offline: %s";
    public final static String TERMINATED = "Terminated\n";
    //MQTT
    public final static String RECONNECT_ATTEMPT = "Attempting to reconnect: %s";
    public final static String RECONNECT_FAILED = "Reconnection failed";
    public final static String RECONNECT_SUCCESSFULL = "Reconnected successfully";
    //db
    public final static String DB_UNABLE_TO_CONNECT = "Could not connect to Database\n";
    
    //initialises the logger
    //sets the log path and deletes everything but the log file
    static{
        try {

            File f = new File(LOG_PATH);
            
            //create folder if it does not exist
            if(!f.exists()){
                f.mkdir();
            }
            
            //list all files exept the log.log file
            File[] files = f.listFiles((file) -> !file.getName().equals(LOG_NAME));
            
            //delete all the files
            for(File file : files){
                file.delete();
            }
            
            //create log.log file if it does not exist
            f = new File(LOG_PATH + LOG_NAME);
            if(!f.exists()){
                f.createNewFile();
            }
            
            FileHandler fh = new FileHandler(LOG_PATH + LOG_NAME, true);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            
            LOGGER.addHandler(fh);
            LOGGER.setUseParentHandlers(false);
            
        } catch (IOException | SecurityException ex) {
            //not much we can do here, the programm mustn't run without the logger
            //shut down
            System.exit(3);
        }
    }
    
    /**
     * logs a standard error, using a template, the log entry will be formatted using the template provided within the logger class
     * @param lvl log level
     * @param className name of the class where the exception occured
     * @param exName exception class name
     * @param exMsg  exception message
     */
    public static void logErrTemplate(Level lvl, String className, String exName, String exMsg){
        LOGGER.log(lvl, String.format(ERR_TEMPLATE, DB.class.getName(), exName, exMsg));
    }
    
}
