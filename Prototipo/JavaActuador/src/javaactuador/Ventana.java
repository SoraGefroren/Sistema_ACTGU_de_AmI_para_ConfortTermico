/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactuador;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javaactuador.Principal;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

/**
 *
 * @author Jorge
 */
public class Ventana extends JFrame {
    // Variables - PANELES
    private JPanel panelMain;
    private JPanel panelData;
    private JPanel panelBtns;
    private JButton btnClose;
    private int verLbls = 2;
    
    // Variables - ACTUADORES []
    private JLabel[][] lbls_Actuadores = new JLabel[Principal.numElemts_H][14];
    // private JLabel lbl_Actuador_1_a; // Tipo
    // private JLabel lbl_Actuador_1_b; // Edo
    // private JLabel lbl_Actuador_1_c; // Temperatura
    // private JLabel lbl_Actuador_1_d; // Temp
    // private JLabel lbl_Actuador_1_e; // °C
    // private JLabel lbl_Actuador_1_f; // Humedad
    // private JLabel lbl_Actuador_1_g; // Hum
    // private JLabel lbl_Actuador_1_h; // %
    // private JLabel lbl_Actuador_1_i; // Velocidad del aire
    // private JLabel lbl_Actuador_1_j; // Aire
    // private JLabel lbl_Actuador_1_k; // m/s
    // private JLabel lbl_Actuador_1_l; // Concentración de gas
    // private JLabel lbl_Actuador_1_m; // CGas
    // private JLabel lbl_Actuador_1_n; // ppm
    
    // Constructor
    public Ventana(){
        // Inicializar simulador
        super("Actuadores");
        // Inicializar panel para labels de datos
        panelData = new JPanel();
        panelData.setLayout(new GridBagLayout());
        GridBagConstraints consGrd = new GridBagConstraints();
        Font fuentSaSef = new Font("SansSerif", Font.BOLD, 15);
        // DATOS DE ACTUADORES
        // Cregar y agregar labels de actuadores a panel de datos
        for (int i = 0; i < Principal.numElemts_H; i++) {
            // Recupera tipo del actuador
            String tipo = Principal.aryActuadores[i].getTipo().toLowerCase();
            // Recuperar y generar estado del actuador
            String edto = generarEstadoDeActuador(tipo, Principal.aryActuadores[i].getEstado());
            // Ajustar nombre de tipo de actuador revisado
            tipo = tipo.substring(0, 1).toUpperCase() + tipo.substring(1);
            // Crear labels
            lbls_Actuadores[i][0] = new JLabel(tipo);
            lbls_Actuadores[i][1] = new JLabel(edto);
            lbls_Actuadores[i][1].setFont(fuentSaSef);
            // Validar si se pueden ver las labels
            if (i < verLbls) {
                lbls_Actuadores[i][2] = new JLabel("temperatura");
                lbls_Actuadores[i][3] = new JLabel("" + Principal.aryActuadores[i].getValorTemperatura());
                lbls_Actuadores[i][3].setFont(fuentSaSef);
                lbls_Actuadores[i][4] = new JLabel("°C");
            }
            // lbls_Actuadores[i][5] = new JLabel("humedad");
            // lbls_Actuadores[i][6] = new JLabel("" + Principal.aryActuadores[i].getValorHumedad());
            // lbls_Actuadores[i][6].setFont(fuentSaSef);
            // lbls_Actuadores[i][7] = new JLabel("%");
            // lbls_Actuadores[i][8] = new JLabel("velocidad del aire");
            // lbls_Actuadores[i][9] = new JLabel("" + Principal.aryActuadores[i].getValorVelAir());
            // lbls_Actuadores[i][9].setFont(fuentSaSef);
            // lbls_Actuadores[i][10] = new JLabel("m/s");
            // lbls_Actuadores[i][11] = new JLabel("concentración de gas");
            // lbls_Actuadores[i][12] = new JLabel("" + Principal.aryActuadores[i].getValorCGas());
            // lbls_Actuadores[i][12].setFont(fuentSaSef);
            // lbls_Actuadores[i][13] = new JLabel("ppm");
            // Agregar y agregar labels de actuadores a panel de datos
            consGrd.gridx = 0;
            consGrd.gridwidth = 3;
            consGrd.gridy = i + 3;
            consGrd.gridheight = 1;
            consGrd.weightx = 1.0;
            consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
            panelData.add(lbls_Actuadores[i][0], consGrd); // Tipo
            consGrd.weightx = 0.0;

            consGrd.gridx = 3;
            consGrd.gridwidth = 3;
            consGrd.gridy = i + 3;
            consGrd.gridheight = 1;
            consGrd.weightx = 1.0;
            consGrd.anchor = GridBagConstraints.CENTER; // Centro
            panelData.add(lbls_Actuadores[i][1], consGrd); // Estado
            consGrd.weightx = 0.0;
            
            // Validar si se pueden ver las labels
            if (i < verLbls) {
                consGrd.gridx = 6;
                consGrd.gridwidth = 1;
                consGrd.gridy = i + 3;
                consGrd.gridheight = 1;
                consGrd.weightx = 1.0;
                consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
                panelData.add(lbls_Actuadores[i][2], consGrd); // Temperatura
                consGrd.weightx = 0.0;

                consGrd.gridx = 7;
                consGrd.gridwidth = 1;
                consGrd.gridy = i + 3;
                consGrd.gridheight = 1;
                consGrd.weightx = 1.0;
                consGrd.anchor = GridBagConstraints.CENTER; // Centro
                panelData.add(lbls_Actuadores[i][3], consGrd); // Temp
                consGrd.weightx = 0.0;

                consGrd.gridx = 8;
                consGrd.gridwidth = 1;
                consGrd.gridy = i + 3;
                consGrd.gridheight = 1;
                consGrd.weightx = 1.0;
                consGrd.anchor = GridBagConstraints.NORTHWEST; // Izquierda
                panelData.add(lbls_Actuadores[i][4], consGrd); // °C,
                consGrd.weightx = 0.0;
            }
            
            /*
            consGrd.gridx = 8;
            consGrd.gridwidth = 2;
            consGrd.gridy = i + 3;
            consGrd.gridheight = 1;
            consGrd.weightx = 1.0;
            consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
            panelData.add(lbls_Actuadores[i][5], consGrd); // Humedad
            consGrd.weightx = 0.0;

            consGrd.gridx = 10;
            consGrd.gridwidth = 1;
            consGrd.gridy = i + 3;
            consGrd.gridheight = 1;
            consGrd.weightx = 1.0;
            consGrd.anchor = GridBagConstraints.CENTER; // Centro
            panelData.add(lbls_Actuadores[i][6], consGrd); // Hum
            consGrd.weightx = 0.0;

            consGrd.gridx = 11;
            consGrd.gridwidth = 1;
            consGrd.gridy = i + 3;
            consGrd.gridheight = 1;
            consGrd.weightx = 1.0;
            consGrd.anchor = GridBagConstraints.NORTHWEST; // Izquierda
            panelData.add(lbls_Actuadores[i][7], consGrd); // %,
            consGrd.weightx = 0.0;

            consGrd.gridx = 12;
            consGrd.gridwidth = 2;
            consGrd.gridy = i + 3;
            consGrd.gridheight = 1;
            consGrd.weightx = 1.0;
            consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
            panelData.add(lbls_Actuadores[i][8], consGrd); // Velocidad del aire
            consGrd.weightx = 0.0;

            consGrd.gridx = 14;
            consGrd.gridwidth = 1;
            consGrd.gridy = i + 3;
            consGrd.gridheight = 1;
            consGrd.weightx = 1.0;
            consGrd.anchor = GridBagConstraints.CENTER; // Centro
            panelData.add(lbls_Actuadores[i][9], consGrd); // vel aire
            consGrd.weightx = 0.0;

            consGrd.gridx = 15;
            consGrd.gridwidth = 1;
            consGrd.gridy = i + 3;
            consGrd.gridheight = 1;
            consGrd.weightx = 1.0;
            consGrd.anchor = GridBagConstraints.NORTHWEST; // Izquierda
            panelData.add(lbls_Actuadores[i][10], consGrd); // m/s y
            consGrd.weightx = 0.0;

            consGrd.gridx = 16;
            consGrd.gridwidth = 2;
            consGrd.gridy = i + 3;
            consGrd.gridheight = 1;
            consGrd.weightx = 1.0;
            consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
            panelData.add(lbls_Actuadores[i][11], consGrd); // Concentración de gas
            consGrd.weightx = 0.0;

            consGrd.gridx = 18;
            consGrd.gridwidth = 1;
            consGrd.gridy = i + 3;
            consGrd.gridheight = 1;
            consGrd.weightx = 1.0;
            consGrd.anchor = GridBagConstraints.CENTER; // Centro
            panelData.add(lbls_Actuadores[i][12], consGrd); // cgas
            consGrd.weightx = 0.0;

            consGrd.gridx = 19;
            consGrd.gridwidth = 1;
            consGrd.gridy = i + 3;
            consGrd.gridheight = 1;
            consGrd.weightx = 1.0;
            consGrd.anchor = GridBagConstraints.NORTHWEST; // Izquierda
            panelData.add(lbls_Actuadores[i][13], consGrd); // ppm
            consGrd.weightx = 0.0;
            */
        }
        
        // Crear panel de botones
        panelBtns = new JPanel();
        panelBtns.setLayout(new FlowLayout());
        
        // Crear boton de termino
        btnClose = new JButton("Salir");
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // Cambiar bandera a falso
                Principal.endApp = true;
                // Terminar aplicacion
                System.exit(0);
            }
        });
        
        // Agregar botón a panel
        panelBtns.add(btnClose);
        
        // Crear panel principal
        panelMain = new JPanel();
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        
        // Agregar elementos al panel principal
        panelMain.add(panelData);
        panelMain.add(panelBtns);
        
        // Crear contenedor y agregar elementos a él
        Container contenedor = getContentPane();
        contenedor.add(panelMain);
    }
    
    // Función que genera y devuelve una representación del estado del actuador
    private String generarEstadoDeActuador (String tipo, String edto) {
        // Variable respuesta
        String strRespo = "";
        // Validar tipo de actuador
        switch (tipo.toLowerCase()) {
            case "aire acondicionado": case "calefactor": case "ventilador":
                // Validar estado del actuador
                if (edto.toLowerCase().equals("e")) {
                    strRespo = "Encendido";
                } else {
                    strRespo = "Apagado";
                }
                break;
            case "ventana": case "ventanas":
                // Validar estado del actuador
                if (edto.toLowerCase().equals("e")) {
                    strRespo = "Abiertas";
                } else {
                    strRespo = "Cerradas";
                }
                break;
            case "puerta":
                // Validar estado del actuador
                if (edto.toLowerCase().equals("e")) {
                    strRespo = "Abierta";
                } else {
                    strRespo = "Cerrada";
                }
                break;
        }
        // Regresar resultado
        return strRespo;
    }
    
    // Actualizar información ambiental
    public synchronized void actualizarConfigActs() {
        // DATOS DE ACTUADORES
        // Asignar valores de actuadores
        for (int i = 0; i < Principal.numElemts_H; i++) {
            // Recuperar y generar estado del actuador
            String edto = generarEstadoDeActuador(Principal.aryActuadores[i].getTipo(), Principal.aryActuadores[i].getEstado());
            // Actualizar valores de actuadores
            lbls_Actuadores[i][1].setText(edto);
            // Validar si se pueden ver las labels
            if (i < verLbls) {
                lbls_Actuadores[i][3].setText(String.format("%.2f", Principal.aryActuadores[i].getValorTemperatura()));
            }
            // lbls_Actuadores[i][6].setText(String.format("%.2f", Principal.aryActuadores[i].getValorHumedad()));
            // lbls_Actuadores[i][9].setText(String.format("%.2f", Principal.aryActuadores[i].getValorVelAir()));
            // lbls_Actuadores[i][12].setText(String.format("%.2f", Principal.aryActuadores[i].getValorCGas()));
        }
        // Actualizar
        super.update(this.getGraphics());
    }
}
