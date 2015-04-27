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
 */
public class LabDemLogger {
    
    private final static String LOG_PATH = "log/";
    private final static String LOG_NAME = "log.log";
    public final static Logger LOGGER = Logger.getLogger("LabDem");
    
    //logg messages
    private final static String ERR_MSG_TEMPLATE = "%s  -  cause: %s  -  message: %s";
    
    static{
        try {

            File f = new File(LOG_PATH);
            
            if(!f.exists()){
                f.mkdir();
            }
            
            f = new File(LOG_NAME);
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
            ex.printStackTrace();
            Logger.getLogger(LabDemLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
