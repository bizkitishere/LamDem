/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.main;

import bfh.ch.labdem.helper.DB;
import bfh.ch.labdem.helper.LabDemLogger;
import bfh.ch.labdem.main.BfhChLabDem.ClientType;
import bfh.ch.labdem.main.BfhChLabDem.MQTTMessages;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Action;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author Philippe Lüthi, Elia Kocher
 */
public class LabDemDaemon {

    //broker information
    private final String PROTOCOL = "tcp";
    private final String BROKER = "localhost";
    private final String PORT = "1883";
    private final String TOPIC_MAIN = "LabDem";
    private final String TOPIC_APP2SERVER = "/App2Server";
    private final String TOPIC_SERVER2APP = "/Server2App";
    private final String TOPIC_SERVER2HW = "/Server2HW";
    private final String TOPIC_HW2SERVER = "/HW2Server";
    private final String WILL = MQTTMessages.Offline.toString();
    private final String WILL_SERVER2APP = "Error;" + MQTTMessages.OfflineDaemon + ";" + "Daemon is offline and needs to be restarted";
    

    private List<Action> actions = null;
    
    //MQTT publisher and subscriber
    private final Subscriber sApp, sHW;
    private final Publisher pApp, pHW;
    
    //threads
    private final ActionExecuter ACTION_EXEC;
    private Thread TAExe = null;
    
    //db connection
    private final DB db;
    
    public LabDemDaemon() throws MqttException{
        //setup DB
        db = new DB();
        
        //setup MQTT
        pApp = new Publisher(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_SERVER2APP, WILL_SERVER2APP, BfhChLabDem.ClientType.Publisher);
        pApp.connectToBroker();
        pApp.Publish(MQTTMessages.Online.toString(), 1, true);
        sApp = new Subscriber(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_APP2SERVER, WILL_SERVER2APP, BfhChLabDem.ClientType.Subscriber);
        sApp.connectToBroker();
        sApp.subscribe();
        pHW = new Publisher(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_SERVER2HW, WILL, BfhChLabDem.ClientType.Publisher);
        pHW.connectToBroker();
        pHW.Publish(MQTTMessages.Online.toString(), 1, true);
        sHW = new Subscriber(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_HW2SERVER, WILL, BfhChLabDem.ClientType.Subscriber);
        sHW.connectToBroker();
        sHW.subscribe();
        
        //prepare threads
        ACTION_EXEC = new ActionExecuter(pHW);
    }
    
    public void getActions(int performanceId, int regionId, int roleId, int enter){
        actions = db.getActions(performanceId, regionId, roleId, enter);
    }
    
    public void executeActions() throws MqttException{
        
        //use base thread if it runs for the first time or if it finished its work
        if(TAExe == null || TAExe.getState() == Thread.State.TERMINATED){
            ACTION_EXEC.setActions(actions);
            TAExe = new Thread(ACTION_EXEC);
            TAExe.start();
            
        //base thread is busy, use a new thread to execute the actions
        }else{            
            ActionExecuter tmpAExe = new ActionExecuter(pHW);
            tmpAExe.setActions(actions);
            Thread tmpThread = new Thread(tmpAExe);
            tmpThread.start();
        }
    }
    
    /**
     * Tries to reconnect the MQTT client
     * @param c client to reconnect
     * The application will terminate if the reconnect is not possible
     */
    public void reconnect(Client c){
        LabDemLogger.LOGGER.info(String.format(LabDemLogger.RECONNECT_ATTEMPT, c.getClass().getName()));
        
        try {
            c.connectToBroker();
            if(c.TYPE == ClientType.Subscriber){
                Subscriber s = (Subscriber) c;
                s.subscribe();
            }
            LabDemLogger.LOGGER.info(LabDemLogger.RECONNECT_SUCCESSFULL);
        } catch (MqttException ex) {
            LabDemLogger.LOGGER.log(Level.SEVERE, LabDemLogger.RECONNECT_FAILED);
            System.exit(1);
        }
    }
    
    public void publishToApp(String m){
        try {
            pApp.Publish(m, 1, true);
        } catch (MqttException ex) {
            //cannot notify app that another service is not running, shutting down
            //System.exit(1);
            Logger.getLogger(LabDemDaemon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
