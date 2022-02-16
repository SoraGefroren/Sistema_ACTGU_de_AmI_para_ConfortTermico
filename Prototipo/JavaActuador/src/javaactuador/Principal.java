/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactuador;

import java.awt.Dimension;
import java.awt.Toolkit;
import javaactuador.mqtt.SuscriptorMQTT;
import javaactuador.elementos.Actuador;
import javax.swing.JFrame;

/**
 *
 * @author Jorge
 */
public class Principal {
    // Variables finales
    public static final int numElemts_H = 2; // 5;
    public static final String ip_servidor_MQTT = "192.168.100.18";
    // Variables estaticas de control
    public static Ventana vtnSim = null;
    public static boolean endApp = false;
    public static Actuador[] aryActuadores = new Actuador[numElemts_H];
    // Variables referentes a topicos
    public static final String topicConfHVACS = "/CONFIG/HVACS/";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // 1.-
        // Crear actuadores
        aryActuadores[0] = new Actuador(1, "aire acondicionado"); 
        aryActuadores[1] = new Actuador(2, "calefactor"); 
        //aryActuadores[2] = new Actuador(3, "ventilador"); 
        //aryActuadores[3] = new Actuador(4, "ventanas"); 
        //aryActuadores[4] = new Actuador(5, "puerta");
        // 2.-
        // Crear ventana
        vtnSim = new Ventana();
        // Configurar ventana
        vtnSim.setSize(380, 120);
        vtnSim.setResizable(false);
        vtnSim.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        vtnSim.setLocation(dim.width/2 - vtnSim.getPreferredSize().width/2, dim.height/2 - vtnSim.getPreferredSize().height/2);
        // Actualizar valores en ventana de datos
        vtnSim.actualizarConfigActs();
        // 3.-
        // Conectar medios MQTT
        // Crear y arracar instancias MQTT
        SuscriptorMQTT cliMQTT_HVACS = new SuscriptorMQTT("HVACS", topicConfHVACS);
        cliMQTT_HVACS.conectar();
        // Mostrar ventana
        vtnSim.setVisible(true);
        // 4.-
        // Iniciar ciclo de envío de mensajes
        while (!endApp) {
            // Actualizar valores en ventana de datos
            vtnSim.actualizarConfigActs();
            // Validar TRY-CATCH
            try {
                // Dormir hilo (cada 5 segs)
                Thread.sleep(5000);
            } catch(InterruptedException ex) {
                // Lanzar mensaje de error (cada 5 segs)
                System.err.println("Principal - 5 segs:" + ex.getMessage());
            }
        }
    }
}
