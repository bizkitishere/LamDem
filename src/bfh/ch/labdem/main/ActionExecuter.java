/*
 *  ActionExecuter.java
 *  LabDem
 *
 *  Copyright (c) 2015 BFH. All rights reserved.
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

    //used to generate a random number to group actions in the same list
    private final static Random RAND = new Random();
    //list conraining the actions to send
    private List<Action> actions = new ArrayList<>();
    //publisher used to publish to a MQTT broker
    private final Publisher PUBLISHER;

    /**
     * constructs a new Action Executer
     * @param p publisher used to send the messages
     */
    public ActionExecuter(Publisher p){
        this.PUBLISHER = p;
    }

    @Override
    public void run() {
        
        if(actions == null) return;
        
        //get a random int to "group" actions that belong together
        int messageId = RAND.nextInt();
        
        boolean error = false;
        
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
                    PUBLISHER.Publish(m);
                } catch (MqttException ex) {
                    error = true;
                }
        }
        
        //send a message to the app, that the actions could not be sent
        if(error){
            StringBuilder sb = new StringBuilder();
            sb.append("Error;");
            sb.append("Could not send the following Actions to topic: ").append(PUBLISHER.TOPIC).append(";");
            
            actions.stream().forEach((a) -> {sb.append(a.toString()).append("\n"); });
            
            BfhChLabDem.publishToApp(sb.toString());
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

}
