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
        
        LOGGER.info(MQTTMessages.Started.toString());
        
        try {
            //tries to load all data necessary for the performances
            //if(LoadAllData() != null){
            //TODO send message to client that server could not load data from db
            //LabDemLogger.LOGGER.log(Level.SEVERE, "Could not load data from db");
            //}
            
            lbd = new LabDemDaemon();
            lbd.run();
            
        } catch (MqttException ex) {
            ex.printStackTrace();
            LabDemLogger.LOGGER.log(Level.SEVERE, null, ex);
        }
        
        
        
        //lbd.getActions(1, 1, 1, 1);
        
        //System.exit(0);
        
        /*
        try {
            System.out.println("Creating Subscriber");
            Subscriber s = new Subscriber(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_APP, WILL, ClientType.Subscriber);
            System.out.println("Connecting to broker");
            s.connectToBroker();
            System.out.println("Subscribing to topic");
            s.subscribe();
            
            System.out.println("Creating Publisher");
            Publisher p = new Publisher(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_APP, WILL, ClientType.Publisher);
            System.out.println("Connecting to broker");
            p.connectToBroker();
            System.out.println("Publishing message");
            
            
            Publisher pHW = new Publisher(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_HW, WILL, ClientType.Publisher);
            pHW.connectToBroker();
            
            
        } catch (MqttException ex) {
            ex.printStackTrace();
            String m = BfhChLabDem.class.getName() + "\nReason code: " + ex.getReasonCode() + " cause: " + ex.getMessage();
            LabDemLogger.LOGGER.log(Level.SEVERE, m);
        }
        */
        
    }
    
    public static void getActions(int performanceId, int regionId, int roleId, int enter){
        lbd.getActions(performanceId, regionId, roleId, enter);
        
        try {
            lbd.executeActions();
        } catch (MqttException ex) {
            LabDemLogger.LOGGER.log(Level.SEVERE, null, ex);
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
    
    
    /**
     * enum containing the different client types
     */
    public enum ClientType{
        Subscriber,
        Publisher
    }
    
    public enum MQTTMessages{
        Online,
        Offline,
        Started
    }
    
}
