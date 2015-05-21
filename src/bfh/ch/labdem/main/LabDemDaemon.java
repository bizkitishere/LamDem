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
    private Subscriber sApp = null, sHW = null;
    private Publisher pApp = null, pHW = null;
    
    //threads
    private ActionExecuter actionExec = null;
    private Thread TAExe = null;
    
    //db connection
    private final DB db;
    
    /**
     * Constructor
     * tries to create a connection to the database, if the connection could not successfully be established, the program will terminate
     * tries to connect to a MQTT broker, will terminate if the connection could not be established
     */
    public LabDemDaemon(){
        
        //setup DB, terminates if there is an error
        db = new DB();
        
        try {
            //setup MQTT
            pApp = new Publisher(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_SERVER2APP, WILL_SERVER2APP, BfhChLabDem.ClientType.Publisher);
            pApp.connectToBroker();
            pApp.Publish(MQTTMessages.Online.toString(), 1, true);
            sApp = new Subscriber(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_APP2SERVER, WILL_SERVER2APP, BfhChLabDem.ClientType.Subscriber);
            sApp.connectToBroker();
            sApp.subscribe();
            pHW = new Publisher(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_SERVER2HW, null, BfhChLabDem.ClientType.Publisher);
            pHW.connectToBroker();
            pHW.Publish(MQTTMessages.Online.toString(), 1, true);
            sHW = new Subscriber(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_HW2SERVER, null, BfhChLabDem.ClientType.Subscriber);
            sHW.connectToBroker();
            sHW.subscribe();
            
            //prepare threads
            actionExec = new ActionExecuter(pHW);
        } catch (MqttException ex) {
            //log and terminate
            if(ex.getCause() == null){
                LabDemLogger.logErrTemplate(Level.SEVERE, LabDemDaemon.class.getSimpleName(), ex.getClass().getName(), ex.getMessage());
            }else{
                LabDemLogger.logErrTemplate(Level.SEVERE, LabDemDaemon.class.getSimpleName(), ex.getClass().getName(), ex.getCause().toString());
            }
            
            LabDemLogger.LOGGER.log(Level.SEVERE, "Could not initialise daemon... " + LabDemLogger.TERMINATED);
            System.exit(1);
        }
    }
    
    /**
     * gets the actions for the given parameters from the database
     * @param performanceId id of the performance
     * @param regionId id of the region
     * @param roleId id of the role
     * @param enter  enter (1) or exit (0)
     */
    public void getActions(int performanceId, int regionId, int roleId, int enter){
        actions = db.getActions(performanceId, regionId, roleId, enter);
    }
    
    /**
     * executes the actions that were fetches from getActions()
     */
    public void executeActions(){
        //use base thread if it runs for the first time or if it terminated
        if(TAExe == null || TAExe.getState() == Thread.State.TERMINATED){
            actionExec.setActions(actions);
            TAExe = new Thread(actionExec);
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
        LabDemLogger.LOGGER.info(String.format(LabDemLogger.RECONNECT_ATTEMPT, c.getClass().getSimpleName() + " - " + c.TOPIC));
        
        try {
            c.connectToBroker();
            //subscribers need to subscribe to their topic once again
            if(c.TYPE == ClientType.Subscriber){
                Subscriber s = (Subscriber) c;
                s.subscribe();
            }
            LabDemLogger.LOGGER.info(LabDemLogger.RECONNECT_SUCCESSFULL);
        } catch (MqttException ex) {
            if(ex.getCause() == null){
                LabDemLogger.logErrTemplate(Level.SEVERE, LabDemDaemon.class.getSimpleName(), ex.getClass().getName(), ex.getMessage());
            }else{
                LabDemLogger.logErrTemplate(Level.SEVERE, LabDemDaemon.class.getSimpleName(), ex.getClass().getName(), ex.getCause().toString());
            }
            LabDemLogger.LOGGER.log(Level.SEVERE, LabDemLogger.RECONNECT_FAILED + "\n" + LabDemLogger.TERMINATED);
            System.exit(1);
        }
    }
    
    /**
     * publishes a message to the app (topic: LabDem/Server2App)
     * uses Quality of service 1
     * shuts the program down if the message could not be sent
     * @param m message to publish
     */
    public void publishToApp(String m){
        try {
            pApp.Publish(m, 1, false);
        } catch (MqttException ex) {
            //cannot notify app that another service is not running, shutting down
            if(ex.getCause() == null){
                LabDemLogger.logErrTemplate(Level.SEVERE, LabDemDaemon.class.getSimpleName(), ex.getClass().getName(), ex.getMessage());
            }else{
                LabDemLogger.logErrTemplate(Level.SEVERE, LabDemDaemon.class.getSimpleName(), ex.getClass().getName(), ex.getCause().toString());
            }
            LabDemLogger.LOGGER.log(Level.SEVERE, LabDemLogger.TERMINATED);
            System.exit(1);
        }
    }
    
}
