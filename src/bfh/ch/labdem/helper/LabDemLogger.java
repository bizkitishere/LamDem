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
 * @author Philippe Lüthi, Elia Kocher
 * Class used to log into a single file, using a single logger
 * Contains strings for log messages
 */
public class LabDemLogger {
    
    private final static String LOG_PATH = "log/";
    private final static String LOG_NAME = "log.log";
    public final static Logger LOGGER = Logger.getLogger("LabDem");
    
    //logg messages
    public final static String ERR_TEMPLATE = "%s  -  cause: %s  -  message: %s";
    public final static String RECONNECT_ATTEMPT = "Attempting to reconnect: %s";
    public final static String RECONNECT_FAILED = "Reconnection failed";
    public final static String RECONNECT_SUCCESSFULL = "Reconnected successfully";
    public final static String TERMINATED = "Terminated";
    
    
    static{
        try {

            File f = new File(LOG_PATH);
            
            //create folder if it does not exist
            if(!f.exists()){
                f.mkdir();
            }
            
            //list all files exept the log.log file
            //f = new File(LOG_PATH);
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
            //not much we can do here...
            //ex.printStackTrace();
            //LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
}
