/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.main;

import bfh.ch.labdem.helper.LabDemLogger;
import bfh.ch.labdem.main.BfhChLabDem.ClientType;
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
    private final MqttCallback MESSAGE_HANDLER = new MQTTMessageHandler();
    
    public Subscriber(String protocol, String broker, String port, String topic, String will, ClientType type) throws MqttException{
        super(protocol, broker, port, topic, will, type);
    }
    
    /**
     * subscribe to the subscribers topic
     * @throws MqttException 
     */
    public void subscribe() throws MqttException{
        mqttClient.setCallback(MESSAGE_HANDLER);
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
            //System.out.println("Connection Lost...");
            //System.out.println(thrwbl.getCause());
            //System.out.println(thrwbl.getMessage());
            String m = Subscriber.class.getName() + "\nCause: " + thrwbl.getCause() + " Message: " + thrwbl.getMessage();
            LabDemLogger.LOGGER.log(Level.SEVERE, m);
            
            //tries to reconnect the subscriber to the broker
            BfhChLabDem.reconnect(Subscriber.this);
        }

        @Override
        //messega that is called when a new mqtt message arrives
        public void messageArrived(String string, MqttMessage mm) throws MqttException {            
            //System.out.printf("Topic: (%s) Payload: (%s) Retained: (%b) \n", string, new String(mm.getPayload()), mm.isRetained());

            //messages need to be in the format: "[int], [int], [int], [int]"
            int performanceId, regionId, roleId, enter;

            String message = new String(mm.getPayload());
            
            //check if the received message is the online or offline status
            if(message.equals(BfhChLabDem.MQTTMessages.Online.toString()) 
            || message.equals(BfhChLabDem.MQTTMessages.Offline.toString())) return;
            
            //split the message, using separator ";"
            String[] tokens = message.split(";", -1);

            //try to get 4 integers from the message that arrived,
            //log input in wrong format
            try{
                performanceId = Integer.parseInt(tokens[0]);
                regionId = Integer.parseInt(tokens[1]);
                roleId = Integer.parseInt(tokens[2]);
                enter = Integer.parseInt(tokens[3]);
                BfhChLabDem.getActions(performanceId, regionId, roleId, enter);
            }catch (NumberFormatException ex){
                String m = Subscriber.class.getName() + " - " + ex.getMessage() + " -  Message: " + message;
                LabDemLogger.LOGGER.log(Level.WARNING, m);
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken imdt) {
            //TODO implement
            System.out.println("Delivery Complete...");
        }
        
    }
    
}
