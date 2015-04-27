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
 *
 * @author Snisä
 */
public class ActionExecuter implements Runnable{

    private List<Action> actions = new ArrayList<>();
    //private AsyncPublisher publisher;
    private Publisher publisher;

    
    public ActionExecuter(Publisher p){
        this.publisher = p;
    }
    
    /*
    public ActionExecuter(AsyncPublisher p){
        this.publisher = p;
    }
    */
    
    @Override
    public void run() {
        /*
        while(actions != null){
            actions.stream().forEach((a) -> {
            //System.out.println("Executing: " + a.toString());
        });}
        */
        
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
                    Logger.getLogger(ActionExecuter.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        
        //no more actions to execute, set the list to null
        actions = null;
        
    }
    
    public void setActions(List<Action> a){
        this.actions = a;
    }
    
    public void setPublisher(Publisher p){
        this.publisher = p;
    }
    
    /*
    public void setPublisher(AsyncPublisher p){
        this.publisher = p;
    }
    */
}
