/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Action;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Class used to send all actions using MQTT.
 * Every action in the list of actions will be published to a MQTT
 * broker using the publisher given in the constructor
 * @author Philippe Lüthi, Elia Kocher
 */
public class ActionExecuter implements Runnable{

    private final static Random RAND = new Random();
    
    //list conraining the actions to send
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
        
        //get a random int to "group" actions that belong together
        int messageId = RAND.nextInt();
        
        for(Action a : actions){
                try {
                    int delay = a.getDelay();
                    //let the read sleep for the duration of the delay
                    if(delay > 0){
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException ex) {
                            //nothing to do here
                        }
                    }
                    String m = a.getTypeId() + ";" + a.getName() + ";" + a.getCommand() + ";" + a.getValue() + ";" + messageId;
                    publisher.Publish(m);
                } catch (MqttException ex) {
                    //TODO something
                    
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
