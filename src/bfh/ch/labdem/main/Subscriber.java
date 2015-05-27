/*
 *  Subscriber.java
 *  LabDem
 *
 *  Copyright (c) 2015 BFH. All rights reserved.
 */
package bfh.ch.labdem.main;

import bfh.ch.labdem.helper.LabDemLogger;
import bfh.ch.labdem.main.BfhChLabDem.ClientType;
import bfh.ch.labdem.main.BfhChLabDem.MQTTMessages;
import bfh.ch.labdem.main.BfhChLabDem.Services;
import java.util.logging.Level;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Receives messages from a MQTT broker
 * 
 * @author Philippe Lüthi, Elia Kocher
 */
public class Subscriber extends Client {
    
    public Subscriber(String protocol, String broker, String port, String topic, String will, ClientType type) throws MqttException{
        super(protocol, broker, port, topic, will, type);
        msgHandler = new MQTTMessageHandler();
    }
    
    /**
     * subscribe to the subscribers topic
     * @throws MqttException 
     */
    public void subscribe() throws MqttException{
        mqttClient.setCallback(msgHandler);
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
    private class MQTTMessageHandler implements MqttCallback{

        @Override
        public void connectionLost(Throwable thrwbl) {
            LabDemLogger.logErrTemplate(Level.SEVERE, Subscriber.class.getSimpleName(), thrwbl.getClass().getSimpleName(), thrwbl.getMessage());
            //tries to reconnect the subscriber to the broker
            BfhChLabDem.reconnect(Subscriber.this);
        }

        @Override
        //messega that is called when a new mqtt message arrives
        public void messageArrived(String string, MqttMessage mm) throws MqttException {            
            String message = new String(mm.getPayload());
            
            //check if the received message is the online or offline status
            if(message.equals(MQTTMessages.Online.toString())){
                //nothing to do, everything is ok
                return;
            //lamp service is offline, can not turn lamps on or off anymore
            }else if(message.equals(MQTTMessages.LampServletOffline.toString())){
                //send message to app
                BfhChLabDem.publishToApp(MQTTMessages.Error + ";" + MQTTMessages.LampServletOffline.toString() + ";");
                LabDemLogger.LOGGER.log(Level.SEVERE, String.format(LabDemLogger.ERR_OFFLINE_TEMPLATE, Services.LampService));
                return;
            }else if(message.equals(BfhChLabDem.MQTTMessages.OfflineAdHocHue.toString())){
                //send message to app
                BfhChLabDem.publishToApp(MQTTMessages.Error + ";" + MQTTMessages.OfflineAdHocHue.toString() + ";" + "It needs to be restarted");
                LabDemLogger.LOGGER.log(Level.SEVERE, String.format(LabDemLogger.ERR_OFFLINE_TEMPLATE, Services.AdHocHUE));
                return;
            }
            
            //split the message, using separator ";"
            String[] tokens = message.split(";", -1);

            //messages need to be in the format: "[int], [int], [int], [int]"
            int performanceId, regionId, roleId, enter;
            
            //try to get 4 integers from the message that arrived,
            //log input in wrong format
            try{
                performanceId = Integer.parseInt(tokens[0]);
                regionId = Integer.parseInt(tokens[1]);
                roleId = Integer.parseInt(tokens[2]);
                enter = Integer.parseInt(tokens[3]);
                BfhChLabDem.getActions(performanceId, regionId, roleId, enter);
            }catch (NumberFormatException ex){
                String m = Subscriber.class.getSimpleName()+ " - Topic: " + TOPIC + " - " + ex.getMessage() + " -  Message: " + message;
                LabDemLogger.LOGGER.log(Level.WARNING, m);
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken imdt) {
            //not needed since this class will not publish messages
        }
        
    }
    
}
