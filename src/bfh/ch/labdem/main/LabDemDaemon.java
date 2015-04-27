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
    //private AsyncPublisher aPHW;
    
    private final ActionExecuter ACTION_EXEC;
    private Thread TAExe = null;
    
    public LabDemDaemon() throws MqttException{
        sApp = new Subscriber(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_APP, WILL, BfhChLabDem.ClientType.Subscriber);
        pApp = new Publisher(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_APP, WILL, BfhChLabDem.ClientType.Publisher);
        pHW = new Publisher(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_HW, WILL, BfhChLabDem.ClientType.Publisher);
        //aPHW = new AsyncPublisher(PROTOCOL, BROKER, PORT, TOPIC_MAIN + TOPIC_HW, WILL, BfhChLabDem.ClientType.Publisher);
        
        sApp.connectToBroker();
        sApp.subscribe();
        pApp.connectToBroker();
        pApp.Publish(MQTTMessages.Online.toString(), 2, true);
        pHW.connectToBroker();
        pHW.Publish(MQTTMessages.Online.toString(), 2, true);
        
        
        //aPHW.connectToBroker();
        
        //prepare threads
        //ACTION_EXEC = new ActionExecuter(aPHW);
        ACTION_EXEC = new ActionExecuter(pHW);
    }

    @Override
    public void run() {
        //TODO remove...
        
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void getActions(int performanceId, int regionId, int roleId, int enter){
        actions = DB.getActions(performanceId, regionId, roleId, enter);
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
            //ActionExecuter tmpAExe = new ActionExecuter(aPHW);
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
        
        String m = "Attempting to reconnect " + c.getClass().getName();
        LabDemLogger.LOGGER.info(m);
        
        try {
            c.connectToBroker();
            if(c.TYPE == ClientType.Subscriber){
                Subscriber s = (Subscriber) c;
                s.subscribe();
            }
            m = "Reconnected successfully";
            LabDemLogger.LOGGER.info(m);
        } catch (MqttException ex) {
            m = "Could not reconnect \nTerminating";
            LabDemLogger.LOGGER.log(Level.SEVERE, m);
            System.exit(1);
        }
    }
    
}
