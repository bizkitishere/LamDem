/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Level;

/**
 * loads properties from the properties file
 * @author Philippe Lüthi, Elia Kocher
 */
public class PropertiesLoader {
    
    private static final String PATH = "LabDem.properties";
    private static final Properties PROP = new Properties();
    
    //load properties file, once
    static {
        try (InputStream input = new FileInputStream(PATH)) {
            // load the properties file
            PROP.load(input);
        } catch (IOException ex) {
            //cannot continue without the data from the properties file
            LabDemLogger.logErrTemplate(Level.SEVERE, PropertiesLoader.class.getSimpleName(), ex.getClass().getSimpleName(), ex.getMessage());
            LabDemLogger.LOGGER.log(Level.SEVERE, LabDemLogger.TERMINATED);
            System.exit(4);
        }
    }
    
    /**
     * gets the property that belongs to the given name
     * @param name name of property to load
     * @return String with the property or null if nothing could be found
     */
    public static String getProperty(String name){
        return new String(Base64.getDecoder().decode(PROP.getProperty(name)));
    }
    
}
