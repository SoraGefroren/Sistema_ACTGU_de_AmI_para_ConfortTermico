/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
    
    // Variables - LABELS
    private JLabel lbl_MQTT_a; // Temperatura
    private JLabel lbl_MQTT_b; // Temp
    private JLabel lbl_MQTT_c; // °C
    private JLabel lbl_MQTT_d; // Humedad
    private JLabel lbl_MQTT_e; // Hum
    private JLabel lbl_MQTT_f; // %
    private JLabel lbl_MQTT_g; // Velocidad del aire
    private JLabel lbl_MQTT_h; // Aire
    private JLabel lbl_MQTT_i; // m/s
    private JLabel lbl_MQTT_j; // Concentración de gas
    private JLabel lbl_MQTT_k; // CGas
    private JLabel lbl_MQTT_l; // ppm
    
    private JLabel lbl_SrvEdo; // ppm
    
    // Variables - PARA JENA
    private JPanel panelQueryBox;
    private JPanel panelQueryBtn;
    private JPanel panelQueryRsul;
    
    private JButton btnJENA;
    private JTextArea txa_JENA_Q;
    private JTextArea txa_JENA_R;
    
    // Variables - Tamaño de ventana
    private int vimAncho = 800;
    // private int vimAltura = 600;
    private int vimAltura = 125;
    
    // Constructor
    public Ventana(){
        // Inicializar
        super("Servidor");
        setSize(vimAncho, vimAltura);
        
        // Inicializar Labels para valores actuales
        lbl_MQTT_a = new JLabel("temperatura");
        lbl_MQTT_b = new JLabel("00.00");
        lbl_MQTT_b.setFont(lbl_MQTT_b.getFont().deriveFont(Font.ITALIC));
        lbl_MQTT_c = new JLabel("°C");
        lbl_MQTT_d = new JLabel("humedad");
        lbl_MQTT_e = new JLabel("00.00");
        lbl_MQTT_e.setFont(lbl_MQTT_e.getFont().deriveFont(Font.ITALIC));
        lbl_MQTT_f = new JLabel("%");
        lbl_MQTT_g = new JLabel("velocidad del aire");
        lbl_MQTT_h = new JLabel("00.00");
        lbl_MQTT_h.setFont(lbl_MQTT_h.getFont().deriveFont(Font.ITALIC));
        lbl_MQTT_i = new JLabel("m/s");
        lbl_MQTT_j = new JLabel("concentración de gas");
        lbl_MQTT_k = new JLabel("00.00");
        lbl_MQTT_k.setFont(lbl_MQTT_k.getFont().deriveFont(Font.ITALIC));
        lbl_MQTT_l = new JLabel("ppm");
        
        lbl_SrvEdo = new JLabel("---");
        
        // Inicializar panel para labels de datos
        GridBagLayout lyt = new GridBagLayout();
        GridBagConstraints consGrd = new GridBagConstraints();
        panelData = new JPanel();
        panelData.setLayout(lyt);
        
        // DATOS RECIBIDOS
        // Agregar y agregar labels a panel de datos
        consGrd.gridx = 0;
        consGrd.gridwidth = 2;
        consGrd.gridy = 0;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
        panelData.add(lbl_MQTT_a, consGrd); // Temperatura
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 2;
        consGrd.gridwidth = 1;
        consGrd.gridy = 0;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.CENTER; // Centro
        panelData.add(lbl_MQTT_b, consGrd); // Temp
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 3;
        consGrd.gridwidth = 1;
        consGrd.gridy = 0;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHWEST; // Izquierda
        panelData.add(lbl_MQTT_c, consGrd); // °C,
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 4;
        consGrd.gridwidth = 2;
        consGrd.gridy = 0;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
        panelData.add(lbl_MQTT_d, consGrd); // Humedad
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 6;
        consGrd.gridwidth = 1;
        consGrd.gridy = 0;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.CENTER; // Centro
        panelData.add(lbl_MQTT_e, consGrd); // Hum
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 7;
        consGrd.gridwidth = 1;
        consGrd.gridy = 0;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHWEST; // Izquierda
        panelData.add(lbl_MQTT_f, consGrd); // %,
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 8;
        consGrd.gridwidth = 2;
        consGrd.gridy = 0;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
        panelData.add(lbl_MQTT_g, consGrd); // Velocidad del aire
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 10;
        consGrd.gridwidth = 1;
        consGrd.gridy = 0;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.CENTER; // Centro
        panelData.add(lbl_MQTT_h, consGrd); // vel aire
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 11;
        consGrd.gridwidth = 1;
        consGrd.gridy = 0;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHWEST; // Izquierda
        panelData.add(lbl_MQTT_i, consGrd); // m/s y
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 12;
        consGrd.gridwidth = 2;
        consGrd.gridy = 0;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
        panelData.add(lbl_MQTT_j, consGrd); // Concentración de gas
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 14;
        consGrd.gridwidth = 1;
        consGrd.gridy = 0;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.CENTER; // Centro
        panelData.add(lbl_MQTT_k, consGrd); // cgas
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 15;
        consGrd.gridwidth = 1;
        consGrd.gridy = 0;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHWEST; // Izquierda
        panelData.add(lbl_MQTT_l, consGrd); // ppm
        consGrd.weightx = 0.0;
        
        
        // SERVIDOR
        // Agregar y agregar labels a panel de datos
        consGrd.gridx = 0;
        consGrd.gridwidth = 16;
        consGrd.gridy = 1;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.CENTER; // Derecha
        panelData.add(lbl_SrvEdo, consGrd); // Temperatura
        consGrd.weightx = 0.0;
        
        
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

        // *******************************************************
        // *******************************************************
        
        // Agregar elementos para JENA
        
        // Configurar seccion donde va la query
        txa_JENA_Q = new JTextArea ("");
        JScrollPane scrollTA_Q = new JScrollPane (txa_JENA_Q, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        panelQueryBox = new JPanel();
        panelQueryBox.setLayout(new FlowLayout());
        
        panelQueryBox.add(scrollTA_Q);
        
        // Configurar seccion de controles
        
        btnJENA = new JButton("Ejecutar consulta");
        btnJENA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // Ejecutar consulta en Ontología
                String strResul = Principal.gestorONTO.ejecutarConsultaPARQL(txa_JENA_Q.getText().trim());
                txa_JENA_R.setText(strResul);
                // Actualizar
                update(getGraphics());
            }
        });
        
        panelQueryBtn = new JPanel();
        panelQueryBtn.setLayout(new FlowLayout());
        
        panelQueryBtn.add(btnJENA);
        
        // Configurar seccion de resultado
        txa_JENA_R = new JTextArea ("");
        JScrollPane scrollTA_R = new JScrollPane (txa_JENA_R, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        panelQueryRsul = new JPanel();
        panelQueryRsul.setLayout(new FlowLayout());
        
        panelQueryRsul.add(scrollTA_R);
        
        // *******************************************************
        // *******************************************************
        
        // Crear panel principal
        panelMain = new JPanel();

        // Dimensionar contenido
        panelData.setPreferredSize(new Dimension(vimAncho, 35));
        panelBtns.setPreferredSize(new Dimension(vimAncho, 35));
        panelQueryBtn.setPreferredSize(new Dimension(vimAncho, 35));
        scrollTA_Q.setPreferredSize(new Dimension(vimAncho - 10, 210));
        scrollTA_R.setPreferredSize(new Dimension(vimAncho - 10, 210));
        
        // Agregar elementos al panel principal
        panelMain.add(panelData);
        panelMain.add(panelBtns);
        // panelMain.add(panelQueryBox);
        // panelMain.add(panelQueryBtn);
        // panelMain.add(panelQueryRsul);

        // Crear contenedor y agregar elementos a él
        Container contenedor = getContentPane();
        contenedor.add(panelMain);
    }
    
    // Funcion que actualiza la Velocidad del aire
    public void actualizar() {
        // Asignar Temp
        lbl_MQTT_b.setText(Principal.valor_temperatura + "");
        // Asignar Hume
        lbl_MQTT_e.setText(Principal.valor_humedad + "");
        // Asignar CGas
        lbl_MQTT_k.setText(Principal.valor_gas + "");
        // Asignar VAir
        lbl_MQTT_h.setText(Principal.valor_velocidadaire + "");
        // Actualizar
        super.update(this.getGraphics());
    }
    
    // Funcion que actualiza la Info del servidor
    public void cambiarEdoSrv(String valor) {
        // Asignar valores del ambiente
        lbl_SrvEdo.setText(valor.trim());
        // Actualizar
        super.update(this.getGraphics());
    }
}
