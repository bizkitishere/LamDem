/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.main;

import java.net.URI;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author Snisä
 */
public class AsyncPublisher {
    
    //parameters to connect to broker
    final BfhChLabDem.ClientType TYPE;
    final String PROTOCOL;
    final String BROKER;
    final String PORT;
    final URI BROKER_URI;
    final String CON_ID;
    final String TOPIC;
    final String WILL;
    
    //client parameters
    final MqttAsyncClient mqttClient;
    
    //client parameters
    private MqttMessage message;
    
    public AsyncPublisher(String protocol, String broker, String port, String topic, String will, BfhChLabDem.ClientType type) throws MqttException{
        this.PROTOCOL = protocol;
        this.BROKER = broker;
        this.PORT = port;
        this.TOPIC = topic;
        this.WILL = will;
        this.TYPE = type;
        this.BROKER_URI = URI.create(protocol + "://" + broker + ":" + port);
        this.CON_ID = broker + "." + topic + "." + "Server" + "." + TYPE.toString();
        
        //mqttClient = new MqttClient(BROKER_URI.toString(), CON_ID);
        
        mqttClient = new MqttAsyncClient(BROKER_URI.toString(), CON_ID);
        
        MqttCallback callback = new MQTTMessageHandler();
        
        mqttClient.setCallback(callback);
        
        //MqttConnectOptions connectOptions = new MqttConnectOptions();
        //connectOptions.setWill(TOPIC, WILL.getBytes(), 1, true);
        
        //IMqttToken conToken = mqttClient.connect(connectOptions, null, null);
        //conToken.waitForCompletion();
    }
    
    
    /**
     * opens the connection to the broker and sets the will
     * @throws MqttException 
     */
    public void connectToBroker() throws MqttException {
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setWill(TOPIC, WILL.getBytes(), 1, true);
        connectOptions.setCleanSession(true);
	//mqttClient.connect(connectOptions);
        //mqttClient.publish(TOPIC, BfhChLabDem.MQTTMessages.Online.toString().getBytes(), 1, true);
        
        IMqttToken conToken = mqttClient.connect(connectOptions, null, null);
        conToken.waitForCompletion();
    }

    /**
     * closes the connection to the broker
     * @throws MqttException 
     */
    public void disconnectFromBroker() throws MqttException {
        IMqttToken discToken = mqttClient.disconnect(null, null);
    	discToken.waitForCompletion();
	//mqttClient.disconnect();
    }
    
    /**
     * sends a message to the broker for the publishers topic
     * @param m message to send
     * @throws MqttException 
     */
    public void Publish(String m) throws MqttException{
        message = new MqttMessage(m.getBytes());
        //IMqttDeliveryToken pubToken = mqttClient.publish(TOPIC, message, null, null);
    	IMqttDeliveryToken pubToken = mqttClient.publish(TOPIC, message);
        pubToken.waitForCompletion();
        
        
        //message = new MqttMessage(m.getBytes());
        //mqttClient.publish(TOPIC, message);
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
    class MQTTMessageHandler implements MqttCallback{

        @Override
        public void connectionLost(Throwable thrwbl) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void messageArrived(String string, MqttMessage mm) throws Exception {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken imdt) {
            System.out.println("message delivered");
        }
    
    }
}
