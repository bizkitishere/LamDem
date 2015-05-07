/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.main;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Action;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Class used to send all actions using MQTT.
 * Every action in the list of actions will be published to a MQTT
 * broker using the publisher given in the constructor
 * @author Philippe Lüthi, Elia Kocher
 */
public class ActionExecuter implements Runnable{

    private List<Action> actions = new ArrayList<>();
    private Publisher publisher;

    /**
     * constructs a new Action Executer
     * @param p publisher used to send the messages
     */
    public ActionExecuter(Publisher p){
        this.publisher = p;
    }

    @Override
    public void run() {
        
        if(actions == null) return;
        
        for(Action a : actions){
                try {
                    int delay = a.getDelay();
                    //let the read sleep for the duration of the delay
                    if(delay > 0){
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                            Logger.getLogger(LabDemDaemon.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    String m = a.getTypeId() + ";" + a.getName() + ";" + a.getCommand() + ";" + a.getValue();
                    publisher.Publish(m);
                } catch (MqttException ex) {
                    //TODO something
                    //Logger.getLogger(ActionExecuter.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        
        //no more actions to execute, set the list to null
        actions = null;
    }
    
    /**
     * Set the list of actions that should be published to the MQTT broker
     * @param a list of actions
     */
    public void setActions(List<Action> a){
        this.actions = a;
    }
    
    /**
     * Set the publisher used to publish to the MQTT broker
     * @param p MQTT publisher
     */
    public void setPublisher(Publisher p){
        this.publisher = p;
    }

}
