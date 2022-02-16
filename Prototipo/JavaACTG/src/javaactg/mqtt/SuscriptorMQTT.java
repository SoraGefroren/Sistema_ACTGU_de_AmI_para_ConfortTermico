/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.mqtt;

import javaactg.Principal;
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
    private MqttClient client;
    private String temaSuscripcion;
    
    // Constructor
    public SuscriptorMQTT(String temaSuscripcion, String nomDemonio) {
        // Atrapar errores
        try {
            // Establecer tema al cual suscribirse
            this.temaSuscripcion = temaSuscripcion;
            // Crear cliente (Escuchador de servidor MQTT)
            client = new MqttClient("tcp://" + Principal.ip_servidor_MQTT + ":1883" /*Broker URL*/, "demonMQTT_R_" + nomDemonio /*ID de Cliente*/);
            
        } catch (MqttException e) {
            // Marcar error en la creación de Escuchador
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    // Iniciar escuchador
    public void conectar() {
        // Atrapar errores
        try {
            // Establecer modo para tratar lectura de datos MQTT
            client.setCallback(new RetornoMQTT());
            // Conectarse a servidor MQTT
            client.connect();
            client.subscribe(temaSuscripcion);
        } catch (MqttException e) {
            // Marcar error en la creación de Escuchador
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    // Clase de retorno
    public class RetornoMQTT implements MqttCallback {
    
        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            //To change body of generated methods, choose Tools | Templates.
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void connectionLost(Throwable cause) {
            //To change body of generated methods, choose Tools | Templates.
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            // Mensaje MQTT a String
            String msjMQTT = message.toString();
            // Revisar el topico del mensaje
            if (topic.equals(Principal.topicSensors)) {
                // Dividir data en secciones (se esperan 5)
                String[] strSensorData = msjMQTT.split("~");
                // ACELEROMETROS
                // Validar el tipo de data
                if (strSensorData.length >= 8) {
                    // Extraer y dividir data por usuarios
                    // bandIniciar ~ numeroIteracion ~ maxDeIteraciones ~ "IdUsr_1:1.0,2.0,3.0|1.0,2.0,3.0^IdUsr_2:1.0,2.0,3.0|1.0,2.0,3.0" ~ Temperatura ~ Humedad ~ CGas ~ VelocidadAire
                    boolean banIni = Boolean.parseBoolean(strSensorData[0]);
                    int numeroIteracion = Integer.parseInt(strSensorData[1]);
                    int maxDeIteraciones = Integer.parseInt(strSensorData[2]);
                    // Separar data de acelerometro
                    String[] strAcelData = strSensorData[3].split("\\^");
                    int numAcelsData = strAcelData.length;
                    // Recorrer data de acelerometros
                    for (int i = 0; i < numAcelsData; i++) {
                        // Extraer data con formato = IdUsr_N:0,0,0|0,0,0|0,0,0
                        String[] strAcelerometroInfo = strAcelData[i].split(":");
                        // Extra er ID del Usuario que envia los datos
                        String nv_id_usuario = strAcelerometroInfo[0];
                        // Recoger datos acumulados en 1 minuto, para poder aplicar el método de Pancardo, 2016. 
                        String[] strAcelerometroData = strAcelerometroInfo[1].split("\\|");
                        // Validar si crear o actualizar diccionario de datos
                        if (Principal.valores_acelerometro.containsKey(nv_id_usuario)) {
                            Principal.valores_acelerometro.replace(nv_id_usuario, strAcelerometroData);
                        } else {
                            Principal.valores_acelerometro.put(nv_id_usuario, strAcelerometroData);
                        }
                    }
                    // TEMPERATURA
                    Principal.valor_temperatura = Double.parseDouble(String.format("%.2f", Double.parseDouble(strSensorData[4])));
                    // HUMEDAD
                    Principal.valor_humedad = Double.parseDouble(String.format("%.2f", Double.parseDouble(strSensorData[5])));
                    // GAS
                    Principal.valor_gas = Double.parseDouble(String.format("%.2f", Double.parseDouble(strSensorData[6])));
                    // VELOCIDADAIRE
                    Principal.valor_velocidadaire = Double.parseDouble(String.format("%.2f", Double.parseDouble(strSensorData[7])));
                    // Actualizar ventana
                    Principal.vtnSim.actualizar();
                    // ONTOLOGÍA
                    // Validar si se debe o no calcular el Confort Termico ("0" a "n" < "n")
                    if ((Principal.ejecTestInRun && Principal.srvIniciado) ||
                       (Principal.vlrBandInicio && banIni &&
                       ((numeroIteracion < maxDeIteraciones) ||
                       ((maxDeIteraciones == 0) && (Principal.resultsDeIteracion.size() == 0))))) {
                        // ---------------------------------------
                        // ---------------------------------------
                        // ---------------------------------------
                        // Impide ejecutar otra prueba
                        Principal.ejecTestInRun = false;
                        // ---------------------------------------
                        // ---------------------------------------
                        // ---------------------------------------
                        // Espera a que no se este usando la Ontología
                        while (Principal.usingOnto) {
                            // Validar TRY-CATCH
                            try {
                                // Dormir hilo (1 seg)
                                Thread.sleep(1000);
                            } catch(InterruptedException ex) {
                                // Lanzar mensaje de error (1 seg)
                                System.out.println("Suscritor - Ontología - 1 seg:" + ex.getMessage());
                            }
                        }
                        // Indicar que se usara la Ontología
                        Principal.usingOnto = true;
                        boolean seCalculo = false;
                        // Ciclo que asegura el calculo del confort
                        while (!seCalculo) {
                            // Validar TRY-CATCH
                            try {
                                // ****************************************************
                                // La iteración va de "0" a "n"
                                String idIteracion = numeroIteracion + "";
                                // Validar si ya se trabajo con este Ito
                                if (!Principal.resultsDeIteracion.containsKey(idIteracion)) {
                                    // Reconstruir Base de Conocimiento
                                    Principal.updateGestorOnto(true, true);
                                    String strResult = Principal.gestorONTO.determinarConfortTermicoGrupal();
                                    // Agregar matriz de dobles
                                    Principal.resultsDeIteracion.put(idIteracion, strResult);
                                }
                                // Indicar fin del calculo
                                seCalculo = true;
                                // ****************************************************
                            } catch(Exception eoo) {
                                // Indicar que no se calculo
                                seCalculo = false;
                                // Lanzar mensaje de error (1 seg)
                                System.out.println("Error al calcular el confort:" + eoo.getMessage());
                            }
                            // Esperar para volver a calcular
                            if (!seCalculo) {
                                // Validar TRY-CATCH
                                try {
                                    // Dormir hilo (1 seg)
                                    Thread.sleep(1000);
                                } catch(InterruptedException ex) {
                                    // Lanzar mensaje de error (1 seg)
                                    System.out.println("Suscritor - Espera - 1 seg:" + ex.getMessage());
                                }
                            }
                        }
                        // Indicar que se dejo de usar la Ontología
                        Principal.usingOnto = false;
                        // ---------------------------------------
                        // ---------------------------------------
                        // ---------------------------------------
                    }
                }
            }
        }
    }
}
