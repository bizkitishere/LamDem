/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.helper;

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
    
    private final static String LOG_PATH = "log/log.log";
    public final static Logger LOGGER = Logger.getLogger("LabDem");
    
    static{
        try {
            FileHandler fh = new FileHandler(LOG_PATH, true);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            
            LOGGER.addHandler(fh);
            LOGGER.setUseParentHandlers(false);
            
            // the following statement is used to log any messages
            LOGGER.info("Created successfully");
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(LabDemLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
