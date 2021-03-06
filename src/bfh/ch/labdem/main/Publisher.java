/*
 *  Publisher.java
 *  LabDem
 *
 *  Copyright (c) 2015 BFH. All rights reserved.
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
 * Sends messages to a MQTT broker
 * 
 * @author Philippe Lüthi, Elia Kocher
 */
public class Publisher extends Client {

    //client parameters
    private MqttMessage message;
    
    public Publisher(String protocol, String broker, String port, String topic, String will, ClientType type) throws MqttException {
        super(protocol, broker, port, topic, will, type);
        msgHandler = new MQTTMessageHandler();
    }
    
    /**
     * sends a message to the broker for the publishers topic
     * @param m message to send
     * @throws MqttException 
     */
    public void Publish(String m) throws MqttException{
        message = new MqttMessage(m.getBytes());
        mqttClient.publish(TOPIC, message);
    }
    
    /**
     * sends a message to the broker for the publishers topic, with some customiseable parameters
     * @param m message to send 
     * @param qos the Quality of Service to deliver the message at. Valid values are 0, 1 or 2
     * @param retained whether or not this message should be retained by the server
     * @throws MqttException 
     */
    public void Publish(String m, int qos, boolean retained) throws MqttException{
        mqttClient.publish(TOPIC, m.getBytes(), qos, retained);
    }
    
    /**
     * Handles the arriving messages, connection loss and complete delivery
     */
    private class MQTTMessageHandler implements MqttCallback{

        @Override
        public void connectionLost(Throwable thrwbl) {
            LabDemLogger.logErrTemplate(Level.SEVERE, Publisher.class.getSimpleName(), thrwbl.getClass().getSimpleName(), thrwbl.getMessage());
            //tries to reconnect the subscriber to the broker
            BfhChLabDem.reconnect(Publisher.this);
        }

        @Override
        //messega that is called when a new mqtt message arrives
        public void messageArrived(String string, MqttMessage mm) throws MqttException {            
            //not needed, since this class will not receive messages
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken imdt) {
            //nothing to do when the message could be delivered
        }
        
    }
}
