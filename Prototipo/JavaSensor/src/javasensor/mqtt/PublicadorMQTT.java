/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensor.mqtt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javasensor.Principal;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author Jorge
 */
public class PublicadorMQTT {
    // Variables
    private MqttClient cliMQTT;
    private String temaSuscripcion;
    public static final Lock candadoPub = new ReentrantLock();
    // Constructor
    // "\"id\":\"" + ideAct + "\",\"edo\":\"a\",\"tipo\":\"" + tipoAct + "\",\"valor\":\"" + nwTemp + "\"";
    public PublicadorMQTT(String nomDemon, String temaSuscrip) {    
        // Atrapar errores
        try {
            // Establecer tema al cual suscribirse
            this.temaSuscripcion = temaSuscrip;
            // Crear cliente (Escuchador de servidor MQTT)
            cliMQTT = new MqttClient("tcp://" + Principal.ip_servidor_MQTT + ":1883" /*Broker URL*/, "demonMQTT_W_" + nomDemon /*ID de Cliente*/);
        } catch (MqttException e) {
            // Marcar error en la creación de Escuchador
            System.out.println("Redactor " + temaSuscripcion + ": " +  e.getMessage());
        }
    }
    // Enviar mensaje
    public void publicarMensaje(String strMensaje) {
        // Atrapar errores
        try {
            // Levantar candado
            if (PublicadorMQTT.candadoPub.tryLock(1, TimeUnit.SECONDS)) {
                // Conectarse y lanzar mensaje MQTT
                cliMQTT.connect();
                // Validar existencia de mensaje
                if(!strMensaje.equals("")) {
                    // Armar y enviar mensaje
                    MqttMessage msjMQTT = new MqttMessage();
                    msjMQTT.setPayload(strMensaje.getBytes());
                    cliMQTT.publish(temaSuscripcion, msjMQTT);
                    // System.out.println(temaSuscripcion + "<" + strMensaje);
                }
                // Desconectar MQTT
                cliMQTT.disconnect();
            }
            PublicadorMQTT.candadoPub.unlock();
        } catch (Exception e) {
            // Marcar error en la creación de Escuchador
            System.out.println("Redactor.Lanzar " + temaSuscripcion + ": " +  e.getMessage());
        }
    }
}
