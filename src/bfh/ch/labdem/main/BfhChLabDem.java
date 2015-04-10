/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.main;

import bfh.ch.labdem.helper.LabDemLogger;
import java.util.logging.Level;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author Philippe Lüthi, Elia Kocher
 */
public class BfhChLabDem {
    
    //broker information
    private final static String PROTOCOL = "tcp";
    //private final static String BROKER = "broker.mqttdashboard.com"; //public broker, for test purposes
    private final static String BROKER = "147.87.117.73"; //LabDem broker
    private final static String PORT = "1883";
    private final static String TOPIC_MAIN = "LabDem";
    private final static String TOPIC_APP = "/App";
    private final static String WILL = "offline";
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //tries to load all data necessary for the performances
        //if(LoadAllData() != null){
            //TODO send message to client that server could not load data from db
            //LabDemLogger.LOGGER.log(Level.SEVERE, "Could not load data from db");
        //}
              
        LabDemDaemon lbd = new LabDemDaemon();
        
        lbd.run();
        
        lbd.getActions(1, 1, 1, 1);
        
        System.exit(0);
        
        
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

            
            //s.disconnectFromBroker();
            //p.disconnectFromBroker();
            
            
        } catch (MqttException ex) {
            String m = BfhChLabDem.class.getName() + "\n" + ex.getMessage();
            LabDemLogger.LOGGER.log(Level.SEVERE, m);
        }
        
        
    }
    
    /**
     * loads all the relevant data for performances from the database
     * @return String containing error details or null
     */
    /*
    public static String LoadAllData(){
        
        return DB.loadAllData();
    }
    */
    
    /**
     * enum containing the different client types
     */
    public enum ClientType{
        Subscriber, Publisher
    }
    
}
