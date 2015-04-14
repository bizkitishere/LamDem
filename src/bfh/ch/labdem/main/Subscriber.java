/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.main;

import bfh.ch.labdem.helper.LabDemLogger;
import bfh.ch.labdem.main.BfhChLabDem.ClientType;
import java.util.ArrayList;
import java.util.logging.Level;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Used to receive messages from a MQTT broker
 * 
 * @author Philippe Lüthi, Elia Kocher
 */
public class Subscriber extends Client {
    
    //client parameters
    private final MqttCallback MESSAGE_HABDLER = new MQTTMessageHandler();
    
    public Subscriber(String protocol, String broker, String port, String topic, String will, ClientType type) throws MqttException{
        super(protocol, broker, port, topic, will, type);
    }
    
    /**
     * subscribe to the subscribers topic
     * @throws MqttException 
     */
    public void subscribe() throws MqttException{
        mqttClient.setCallback(MESSAGE_HABDLER);
        mqttClient.subscribe(TOPIC);
    }
    
    /**
     * unsubscribe from the subscribers topic
     * @throws MqttException 
     */
    public void unsubscribe() throws MqttException{
        mqttClient.setCallback(null);
        mqttClient.unsubscribe(TOPIC);
    }

    /**
     * Handles the arriving messages, connection loss and complete delivery
     */
    class MQTTMessageHandler implements MqttCallback{

        @Override
        public void connectionLost(Throwable thrwbl) {
            //TODO implement
            System.out.println("Connection Lost...");
            System.out.println(thrwbl.getCause());
            System.out.println(thrwbl.getMessage());
            String m = Subscriber.class.getName() + "\n" + thrwbl.getMessage();
            LabDemLogger.LOGGER.log(Level.SEVERE, m);
        }

        @Override
        //messega that is called when a new mqtt message arrives
        public void messageArrived(String string, MqttMessage mm) throws MqttException {
            //TODO implement
            
            System.out.printf("Topic: (%s) Payload: (%s) Retained: (%b) \n", string, new String(mm.getPayload()), mm.isRetained());
            
                                                //performance, rolle, region, enter/exit
            //messages need to be in the format: "[int], [int], [int], [int]"
            
            int performanceId, regionId, roleId, enter;
            
            //ArrayList<Integer> ids = new ArrayList<>();
            
            String message = new String(mm.getPayload());
            
            if(message.equals("online") || message.equals("offline")) return;
            
            String[] tokens = message.split(";", -1);
            //int[] numbers = new int[tokens.length];
            /*
            for (String t : tokens) {
                //ids[i] = Integer.parseInt(tokens[i]);
                //System.out.println(t);
                ids.add(Integer.parseInt(t));
            }
            */
            
            
            try{
                performanceId = Integer.parseInt(tokens[0]);
                regionId = Integer.parseInt(tokens[1]);
                roleId = Integer.parseInt(tokens[2]);
                enter = Integer.parseInt(tokens[3]);
                BfhChLabDem.getActions(performanceId, regionId, roleId, enter);
            }catch (NumberFormatException ex){
                String m = Subscriber.class.getName() + "\n" + ex.getMessage();
                LabDemLogger.LOGGER.log(Level.WARNING, m);
            }
            
            
            
            
            //BfhChLabDem.getActions(ids.get(0), ids.get(1), ids.get(2), ids.get(3));
            //BfhChLabDem.getActions(performanceId, regionId, roleId, enter);
            
            
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken imdt) {
            //TODO implement
            System.out.println("Delivery Complete...");
        }
        
    }
    
}
