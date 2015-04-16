/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.main;

import bfh.ch.labdem.helper.DB;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Action;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author Philippe Lüthi, Elia Kocher
 */
public class LabDemDaemon implements Runnable{

    //broker information
    private final String PROTOCOL = "tcp";
    //private final static String BROKER = "broker.mqttdashboard.com"; //public broker, for test purposes
    //private final static String BROKER = "147.87.117.73"; //LabDem broker
    private final String BROKER = "localhost";
    private final String PORT = "1883";
    private final String TOPIC_MAIN = "LabDem";
    private final String TOPIC_APP = "/App";
    private final String TOPIC_HW = "/HW";
    private final String WILL = BfhChLabDem.MQTTMessages.Offline.toString();

    private List<Action> actions = null;
    
    private Subscriber sApp;
    private Publisher pApp, pHW;
    
    
    public LabDemDaemon() throws MqttException{
        sApp = new Subscriber(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_APP, WILL, BfhChLabDem.ClientType.Subscriber);
        pApp = new Publisher(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_APP, WILL, BfhChLabDem.ClientType.Publisher);
        pHW = new Publisher(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_HW, WILL, BfhChLabDem.ClientType.Publisher);
        
        sApp.connectToBroker();
        sApp.subscribe();
        pApp.connectToBroker();
        pHW.connectToBroker();
    }

    @Override
    public void run() {
        
        
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    public void getActions(int performanceId, int regionId, int roleId, int enter){
        actions = DB.getActions(performanceId, regionId, roleId, enter);
    }
    
    public void executeActions() throws MqttException{
        
        actions.stream().forEach((a) -> {
            System.out.println(a.toString());
        });
        
        if(actions == null) return;
        
        for(Action a : actions){
            
            int delay = a.getDelay();
            if(delay > 0){
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Logger.getLogger(LabDemDaemon.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String m = a.getTypeId() + ";" + a.getName() + ";" + a.getCommand() + ";" + a.getValue();
            pHW.Publish(m);
        }
        
        actions = null;
        
    }
    
}
