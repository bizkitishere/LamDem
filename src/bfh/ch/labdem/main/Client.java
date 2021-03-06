/*
 *  Client.java
 *  LabDem
 *
 *  Copyright (c) 2015 BFH. All rights reserved.
 */
package bfh.ch.labdem.main;

import bfh.ch.labdem.helper.PropertiesLoader;
import bfh.ch.labdem.main.BfhChLabDem.ClientType;
import bfh.ch.labdem.main.BfhChLabDem.PropertyKeys;
import java.net.URI;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Parent Class for MQTT clients, offers some basic functionality and required parameters
 * @author Philippe Lüthi, Elia Kocher
 */
public abstract class Client {
    
    //parameters to connect to broker
    final ClientType TYPE;
    final String PROTOCOL;
    final String BROKER;
    final String PORT;
    final URI BROKER_URI;
    final String CON_ID;
    final String TOPIC;
    final String WILL;
    
    //password for mqtt
    private final String USER;
    private final String PASS;
    
    //client parameters
    final MqttClient mqttClient;
    MqttCallback msgHandler;
    
    /**
     * Constructor
     * @param protocol protocol to use
     * @param broker broker name
     * @param port port to connect
     * @param topic topic to publish/subscribe to
     * @param will message to send when connection is interrupted
     * @param type client type
     * @throws MqttException 
     */
    public Client(String protocol, String broker, String port, String topic, String will, ClientType type) throws MqttException{
        this.PROTOCOL = protocol;
        this.BROKER = broker;
        this.PORT = port;
        this.TOPIC = topic;
        this.WILL = will;
        this.TYPE = type;
        this.BROKER_URI = URI.create(protocol + "://" + broker + ":" + port);
        this.CON_ID = broker + "." + topic + "." + "Server" + "." + TYPE.toString();
        
        //get username and password
        USER = PropertiesLoader.getProperty(PropertyKeys.MQTTUser.toString());
        PASS = PropertiesLoader.getProperty(PropertyKeys.MQTTPass.toString());
        
        mqttClient = new MqttClient(BROKER_URI.toString(), CON_ID);
    }
    
    /**
     * opens the connection to the broker and sets the will
     * @throws MqttException 
     */
    public void connectToBroker() throws MqttException {
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        if(WILL != null) connectOptions.setWill(TOPIC, WILL.getBytes(), 1, true);
        connectOptions.setUserName(USER);
        connectOptions.setPassword(PASS.toCharArray());
	mqttClient.connect(connectOptions);
    }

    /**
     * closes the connection to the broker
     * @throws MqttException 
     */
    public void disconnectFromBroker() throws MqttException {
	mqttClient.disconnect();
    }

}
