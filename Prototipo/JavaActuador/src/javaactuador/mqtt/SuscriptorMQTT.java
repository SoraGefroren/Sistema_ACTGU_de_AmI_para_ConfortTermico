/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactuador.mqtt;

import javaactuador.Principal;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author Jorge
 */
public class SuscriptorMQTT {
    // Variables
    private MqttClient clientMQTT;
    private String temaSuscripcion;
    // Constructor
    public SuscriptorMQTT(String nomDemonio, String temaSuscripcion) {
        // Atrapar errores
        try {
            // Establecer tema al cual suscribirse
            this.temaSuscripcion = temaSuscripcion;
            // Crear cliente (Escuchador de servidor MQTT)
            clientMQTT = new MqttClient("tcp://" + Principal.ip_servidor_MQTT + ":1883" /*Broker URL*/, "demonMQTT_R_" + nomDemonio /*ID de Cliente*/);
        } catch (MqttException e) {
            // Marcar error en la creación de Escuchador
            System.err.println("Suscriptor " + temaSuscripcion + ": " + e.getMessage());
        }
    }
    
    // Iniciar escuchador
    public void conectar() {
        // Atrapar errores
        try {
            // Establecer modo para tratar lectura de datos MQTT
            clientMQTT.setCallback(new RetornoMQTT());
            // Conectarse a servidor MQTT
            clientMQTT.connect();
            clientMQTT .subscribe(temaSuscripcion);
        } catch (MqttException e) {
            // Marcar error en la creación de Escuchador
            System.err.println("Suscriptor.Conectar " + temaSuscripcion + ": " + e.getMessage());
        }
    }
    
    public class RetornoMQTT implements MqttCallback {
        /* Ejemplo:
         * mosquitto_pub -h 192.168.100.18 -p 1883 -t "/CONFIG/HVACS/" -m "id:2,edo:e,tipo:calefactor,temp:28.00,aire:2.50,hum:35.00,gas:335.00|id:1,edo:a,tipo:aire acondicionado,temp:27.00,aire:0.50,hum:35.00,gas:335.00|id:3,edo:a,tipo:ventilador,temp:27.00,aire:0.50,hum:35.00,gas:335.00|id:4,edo:a,tipo:ventanas,temp:27.00,aire:0.50,hum:35.00,gas:335.00|id:5,edo:a,tipo:puerta,temp:27.00,aire:0.50,hum:35.00,gas:335.00"
        */
        // ** Funciones
        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            // Marcar error
            System.err.println("Entrega o liberación completa");
            //To change body of generated methods, choose Tools | Templates.
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void connectionLost(Throwable cause) {
            // Marcar error
            System.err.println("Conexión perdida");
            //To change body of generated methods, choose Tools | Templates.
            throw new UnsupportedOperationException("Not supported yet.");
        }

        // Mensaje que llega
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            // Mensaje MQTT a String
            String mMQ = message.toString();
            System.out.println(topic + ">" + mMQ);
            // Validar: Si es configuración para HVACS
            if (topic.equals(Principal.topicConfHVACS)) {
                // Dividir data
                String[] aryStrDAT = mMQ.split("\\|");
                int numberAryDAT = aryStrDAT.length;
                // Recorrer data
                for (int i = 0; i < numberAryDAT; i++) {
                    // MSJ = "id:ideAct, edo:a, tipo:tipoAct, temp:nwTemp, aire:nwVelAir, hum:nwHume, gas:nwCGas"
                    String[] mjsDiv = aryStrDAT[i].split(",");
                    // Extraer ID
                    String strInkSus = mjsDiv[0].split(":")[1];
                    // Validar ID
                    if ((Integer.parseInt(strInkSus) - 1) < Principal.numElemts_H) {
                        // Buscar y actualizar actuador
                        Principal.aryActuadores[Integer.parseInt(strInkSus) - 1].actualizarConfig(mjsDiv);
                    }
                }
                // Actualizar ventana
                Principal.vtnSim.actualizarConfigActs();
            }
        }
    }
}
