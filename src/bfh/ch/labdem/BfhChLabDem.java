/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;


/**
 *
 * @author Philippe Lüthi, Elia Kocher
 */
public class BfhChLabDem {
    
    //broker information
    private final static String PROTOCOL = "tcp";
    private final static String BROKER = "broker.mqttdashboard.com"; //public broker, for test purposes
    //private final static String BROKER = "147.87.117.73"; //LabDem broker
    private final static String PORT = "1883";
    private final static String TOPIC_MAIN = "LabDem";
    private final static String TOPIC_APP = "/App";
    private final static String WILL = "offline";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
           
                
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
            
            
            for (int i = 0; i < 10; i++) {
                p.Publish(i + ";" + i + ";" + i + ";" + i);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BfhChLabDem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            s.disconnectFromBroker();
            p.disconnectFromBroker();
            
            
        } catch (MqttException ex) {
            Logger.getLogger(BfhChLabDem.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public enum ClientType{
        Subscriber, Publisher
    }
    
}
