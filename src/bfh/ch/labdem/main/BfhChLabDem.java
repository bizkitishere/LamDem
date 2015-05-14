/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.main;

import bfh.ch.labdem.helper.LabDemLogger;
import static bfh.ch.labdem.helper.LabDemLogger.LOGGER;

/**
 * Main class for the daemon application
 * Is used as a means for the different parts to communicate together
 * @author Philippe Lüthi, Elia Kocher
 */
public class BfhChLabDem {
    
    private static LabDemDaemon lbd;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        lbd = new LabDemDaemon();
        //application started successfully
        LOGGER.info(LabDemLogger.STARTED);
    }
    
    /**
     * gets the actions for the given parameters from the database
     * @param performanceId id of the performance
     * @param regionId id of the region
     * @param roleId id of the role
     * @param enter entered (1) a region or exited (0)
     */
    public static void getActions(int performanceId, int regionId, int roleId, int enter){
        lbd.getActions(performanceId, regionId, roleId, enter);
        lbd.executeActions();
    }
    
    /**
     * Tries to reconnect the MQTT client
     * @param c client to reconnect
     * The application will terminate if the reconnection is not possible
     */
    public static void reconnect(Client c){
        lbd.reconnect(c);
    }
    
    /**
     * publishes a message to the mobile application (topic: LabDem/Server2App)
     * uses Quality of service 1
     * shuts the program down if the message could not be sent
     * @param m message to publish
     */
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
        OfflineDaemon,
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
