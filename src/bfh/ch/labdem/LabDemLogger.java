/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Snisä
 */
public class LabDemLogger {
    
    public final static Logger LOGGER = Logger.getLogger("LabDem");
    
    public LabDemLogger(String path) throws IOException{
        
        FileHandler fh = new FileHandler(path);
        LOGGER.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);
        LOGGER.setUseParentHandlers(false);

        // the following statement is used to log any messages  
        LOGGER.info("Created successfully\n");
        
    }
    
    
    
}
