/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem;

import java.net.URI;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * This class implements a MQTT subscriber.
 * 
 * @author Philippe Lüthi, Elia Kocher
 */
public class Subscriber {
    
    //parameters to connect to broker
    private final String TYPE = "Subscriber";
    private final String PROTOCOL;
    private final String BROKER;
    private final String PORT;
    private final URI BROKER_URI;
    private final String CON_ID;
    private final String TOPIC;
    private final String WILL = "offline";
    
    //client parameters
    private final MqttClient mqttClient;
    private final MqttCallback MESSAGE_HABDLER = new MQTTMessageHandler();
    
    public Subscriber(String protocol, String broker, String port, String topic) throws MqttException{
        this.PROTOCOL = protocol;
        this.BROKER = broker;
        this.PORT = port;
        this.TOPIC = topic;
        this.BROKER_URI = URI.create(protocol + "://" + broker + ":" + port);
        this.CON_ID = broker + "." + "topic" + "." + TYPE;
        
        mqttClient = new MqttClient(BROKER_URI.toString(), CON_ID);
    }
    
    public void connectToBroker() throws MqttException {
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setWill(TOPIC, WILL.getBytes(), 1, true);
	mqttClient.connect(connectOptions);
	mqttClient.publish(TOPIC, "online".getBytes(), 1, true);
    }

    public void disconnectFromBroker() throws MqttException {
	mqttClient.disconnect();
    }
    
    public void subscribe() throws MqttException{
        mqttClient.setCallback(MESSAGE_HABDLER);
        mqttClient.subscribe(TOPIC);
    }
    
    public void unsubscribe() throws MqttException{
        mqttClient.setCallback(null);
        mqttClient.unsubscribe(TOPIC);
    }
    
    /**
     * Handles the arriving messages
     */
    class MQTTMessageHandler implements MqttCallback{

        @Override
        public void connectionLost(Throwable thrwbl) {
            //TODO implement
            System.out.println("Connection Lost...");
            System.out.println(thrwbl.getCause());
        }

        @Override
        //messega that is called when a new mqtt message arrives
        public void messageArrived(String string, MqttMessage mm) throws Exception {
            //TODO implement
            
            //System.out.printf("Topic: (%s) Payload: (%s) Retained: (%b) \n", string, new String(mm.getPayload()), mm.isRetained());
            
                                                //performance, rolle, region, enter/exit
            //messages need to be in the format: "[int], [int], [int], [int]"
            String message = new String(mm.getPayload());
            
            String[] tokens = message.split(",", -1);
            //int[] numbers = new int[tokens.length];
            for (String t : tokens) {
                //numbers[i] = Integer.parseInt(tokens[i]);
                System.out.println(t);
            }
            
            
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken imdt) {
            //TODO implement
            System.out.println("Delivery Complete...");
        }
        
    }
    
}
