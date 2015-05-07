/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.main;

import bfh.ch.labdem.helper.LabDemLogger;
import static bfh.ch.labdem.helper.LabDemLogger.LOGGER;
import java.util.logging.Level;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author Philippe Lüthi, Elia Kocher
 */
public class BfhChLabDem {
    
    private static LabDemDaemon lbd;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        LOGGER.info(LabDemLogger.STARTED);
        
        try {
            lbd = new LabDemDaemon();
        } catch (MqttException ex) {
            ex.printStackTrace();
            
            LabDemLogger.LOGGER.log(Level.SEVERE, String.format(LabDemLogger.ERR_TEMPLATE, LabDemDaemon.class.getName(), ex.getCause(), ex.getMessage()));
            //could not create the Daemon, application will shut down
            LabDemLogger.LOGGER.log(Level.SEVERE, "Could not create the daemon... " + LabDemLogger.TERMINATED);
            System.exit(1);
        }
    }
    
    public static void getActions(int performanceId, int regionId, int roleId, int enter){
        lbd.getActions(performanceId, regionId, roleId, enter);
        
        try {
            lbd.executeActions();
        } catch (MqttException ex) {
            LabDemLogger.LOGGER.log(Level.SEVERE, String.format(LabDemLogger.ERR_TEMPLATE, LabDemDaemon.class.getName(), ex.getCause(), ex.getMessage()));
        }
    }
    
    /**
     * Tries to reconnect the MQTT client
     * @param c client to reconnect
     * The application will terminate if the reconnect is not possible
     */
    public static void reconnect(Client c){
        lbd.reconnect(c);
    }
    
    public static void publishToApp(String m){
        lbd.publishToApp(m);
    }
    
    /**
     * enum containing the different client types
     */
    public enum ClientType{
        Subscriber,
        Publisher
    }
    
    /**
     * enum containing different messages that will be sent using MQTT
     */
    public enum MQTTMessages{
        Online,
        Offline,
        OfflineAdHocHue,
        LampServletOffline,
        Error
    }
    
    /**
     * enum containing all the services
     */
    public enum Services{
        Daemon,
        LampService,
        AdHocHUE
    }
    
}
