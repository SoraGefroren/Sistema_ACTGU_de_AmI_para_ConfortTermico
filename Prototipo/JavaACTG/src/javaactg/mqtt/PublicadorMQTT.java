/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.mqtt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javaactg.Principal;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author Jorge
 */
public class PublicadorMQTT {
    // Variables
    public static final Lock candadoPUB = new ReentrantLock();
    private MqttClient cliMQTT;
    private String mensajeMQTT;
    private String topicAW;

    // Constructor
    // "\"id\":\"" + ideAct + "\",\"edo\":\"a\",\"tipo\":\"" + tipoAct + "\",\"valor\":\"" + nwTemp + "\"";
    public PublicadorMQTT (String strAryData) {
        // Atrapar errores
        try {
            // Establecer tema en donde escribir un mensaje
            this.topicAW = Principal.topicActuators;
            this.mensajeMQTT = strAryData;
            // Crear cliente (Escuchador de servidor MQTT)
            cliMQTT = new MqttClient("tcp://" + Principal.ip_servidor_MQTT + ":1883" /*Broker URL*/, "demonMQTT_W_" + "HVACS" /*ID de Cliente*/);
        } catch (MqttException e) {
            // Marcar error en la creación de Escuchador
            e.printStackTrace();
        }
    }
    
    // Función que comunica 
    public void publicarConfiguracion() {
        // Atrapar errores
        try {
            // Levantar candado
            if (candadoPUB.tryLock(1, TimeUnit.SECONDS)) {
                // Conectarse y lanzar mensaje MQTT
                cliMQTT.connect();
                String strMesge = mensajeMQTT;
                if(!strMesge.equals("")) {
                    MqttMessage msjMQTT = new MqttMessage();
                    msjMQTT.setPayload(strMesge.getBytes());
                    cliMQTT.publish(topicAW, msjMQTT);
                }
                cliMQTT.disconnect();
            }
            candadoPUB.unlock();
        } catch (Exception e) {
            // Marcar error en la creación de Escuchador
            e.printStackTrace();
        }
    }
}
