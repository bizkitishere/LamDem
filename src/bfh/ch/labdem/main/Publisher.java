/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.main;

import bfh.ch.labdem.main.BfhChLabDem.ClientType;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Used to send messages to a MQTT broker
 * 
 * @author Philippe Lüthi, Elia Kocher
 */
public class Publisher extends Client {

    //client parameters
    private MqttMessage message;
    
    public Publisher(String protocol, String broker, String port, String topic, String will, ClientType type) throws MqttException {
        super(protocol, broker, port, topic, will, type);
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
    
    //TODO add callback!!!
    
}
