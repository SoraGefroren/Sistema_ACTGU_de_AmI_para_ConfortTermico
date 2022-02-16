/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensor;

import java.awt.Font;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javasensor.Principal;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
/**
 *
 * @author Jorge
 */
public class Ventana extends JFrame{
    // Variables - PANELES
    private JPanel panelMain;
    private JPanel panelData;
    private JPanel panelBtns;
    // Variables - BOTONES
    private JButton btnClose;
    // Variables - LABELS
    // private JLabel lbl_vlrActl_a;
    private JLabel lbl_vlrActl_b; // Temperatura
    private JTextField txt_vlrActl_c; // Temp
    private JLabel lbl_vlrActl_d; // °C
    private JLabel lbl_vlrActl_e; // Humedad
    private JTextField txt_vlrActl_f; // Hum
    private JLabel lbl_vlrActl_g; // %
    private JLabel lbl_vlrActl_h; // Velocidad del aire
    private JTextField txt_vlrActl_i; // Aire
    private JLabel lbl_vlrActl_j; // m/s
    private JLabel lbl_vlrActl_k; // Concentración de gas
    private JTextField txt_vlrActl_l; // CGas
    private JLabel lbl_vlrActl_m; // ppm
    // Variables - LABELS
    private JLabel lbl_vlrItera_a; // Número de iteración
    private JTextField txt_vlrItera_b; // Número de iteración
    private JLabel lbl_vlrItera_c; // Tiempo para el cambio
    private JTextField txt_vlrItera_d; // Tiempo para el cambio
    private JLabel lbl_vlrItera_e; // Tiempo para el cambio
    
    // Constructor
    public Ventana(){
        // Inicializar simulador
        super("Sensores");
        Font fuentSaSef = new Font("SansSerif", Font.BOLD, 15);
        Dimension dimTxtBox = new Dimension(60, 25);
        
        // Inicializar Labels para valores actuales
        // lbl_vlrActl_a = new JLabel("Valores censados:");
        lbl_vlrActl_b = new JLabel("temperatura");
        txt_vlrActl_c = new JTextField("0.0");
        txt_vlrActl_c.setEditable(false);
        txt_vlrActl_c.setFont(fuentSaSef);
        txt_vlrActl_c.setPreferredSize(dimTxtBox);
        txt_vlrActl_c.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_vlrActl_d = new JLabel("°C");
        lbl_vlrActl_e = new JLabel("humedad");
        txt_vlrActl_f = new JTextField("0.0");
        txt_vlrActl_f.setEditable(false);
        txt_vlrActl_f.setFont(fuentSaSef);
        txt_vlrActl_f.setPreferredSize(dimTxtBox);
        txt_vlrActl_f.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_vlrActl_g = new JLabel("%");
        lbl_vlrActl_h = new JLabel("velocidad del aire");
        txt_vlrActl_i = new JTextField("0.0");
        txt_vlrActl_i.setEditable(false);
        txt_vlrActl_i.setFont(fuentSaSef);
        txt_vlrActl_i.setPreferredSize(dimTxtBox);
        txt_vlrActl_i.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_vlrActl_j = new JLabel("m/s");
        lbl_vlrActl_k = new JLabel("concentración de gas");
        txt_vlrActl_l = new JTextField("0.0");
        txt_vlrActl_l.setEditable(false);
        txt_vlrActl_l.setFont(fuentSaSef);
        txt_vlrActl_l.setPreferredSize(dimTxtBox);
        txt_vlrActl_l.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_vlrActl_m = new JLabel("ppm");
        // Inicializar Labels de configuración
        lbl_vlrItera_a = new JLabel("Iteración:");
        txt_vlrItera_b = new JTextField("0");
        txt_vlrItera_b.setEditable(false);
        txt_vlrItera_b.setFont(fuentSaSef);
        txt_vlrItera_b.setPreferredSize(dimTxtBox);
        txt_vlrItera_b.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_vlrItera_c = new JLabel("Duración:");
        txt_vlrItera_d = new JTextField("0");
        txt_vlrItera_d.setEditable(false);
        txt_vlrItera_d.setFont(fuentSaSef);
        txt_vlrItera_d.setPreferredSize(dimTxtBox);
        txt_vlrItera_d.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_vlrItera_e = new JLabel("segundos");
        
        // Inicializar panel para labels de datos
        GridBagConstraints consGrd = new GridBagConstraints();
        GridBagLayout lyt = new GridBagLayout();
        panelData = new JPanel();
        panelData.setLayout(lyt);
        
        // DATOS ACTUALES
        // Agregar y agregar labels a panel de datos
        // ------------------------
        // ------------------------
        /*
        consGrd.gridx = 0;
        consGrd.gridwidth = 7;
        consGrd.gridy = 1;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
        panelData.add(lbl_vlrItera_a, consGrd); // Iteración
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 7;
        consGrd.gridwidth = 1;
        consGrd.gridy = 1;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.CENTER; // Centro
        panelData.add(txt_vlrItera_b, consGrd); // Número iteración
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 8;
        consGrd.gridwidth = 1;
        consGrd.gridy = 1;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
        panelData.add(lbl_vlrItera_c, consGrd); // Duración de iteración
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 9;
        consGrd.gridwidth = 1;
        consGrd.gridy = 1;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.CENTER; // Centro
        panelData.add(txt_vlrItera_d, consGrd); // Duración
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 10;
        consGrd.gridwidth = 1;
        consGrd.gridy = 1;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHWEST; // Izquierda
        panelData.add(lbl_vlrItera_e, consGrd); // Segundos
        consGrd.weightx = 0.0;
        // */
        
        // ------------------------
        
        // consGrd.gridx = 0;
        // consGrd.gridwidth = 4;
        // consGrd.gridy = 2;
        // consGrd.gridheight = 1;
        // consGrd.weightx = 1.0;
        // consGrd.anchor = GridBagConstraints.NORTHWEST; // Izquierda
        // panelData.add(lbl_vlrActl_a, consGrd); // Titulo
        // consGrd.weightx = 0.0;
        
        consGrd.gridx = 0;
        consGrd.gridwidth = 2;
        consGrd.gridy = 2;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
        panelData.add(lbl_vlrActl_b, consGrd); // Temperatura
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 2;
        consGrd.gridwidth = 1;
        consGrd.gridy = 2;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.CENTER; // Centro
        panelData.add(txt_vlrActl_c, consGrd); // Temp
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 3;
        consGrd.gridwidth = 1;
        consGrd.gridy = 2;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHWEST; // Izquierda
        panelData.add(lbl_vlrActl_d, consGrd); // °C,
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 4;
        consGrd.gridwidth = 2;
        consGrd.gridy = 2;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
        panelData.add(lbl_vlrActl_e, consGrd); // Humedad
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 6;
        consGrd.gridwidth = 1;
        consGrd.gridy = 2;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.CENTER; // Centro
        panelData.add(txt_vlrActl_f, consGrd); // Hum
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 7;
        consGrd.gridwidth = 1;
        consGrd.gridy = 2;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHWEST; // Izquierda
        panelData.add(lbl_vlrActl_g, consGrd); // %,
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 8;
        consGrd.gridwidth = 2;
        consGrd.gridy = 2;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
        panelData.add(lbl_vlrActl_h, consGrd); // Velocidad del aire
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 10;
        consGrd.gridwidth = 1;
        consGrd.gridy = 2;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.CENTER; // Centro
        panelData.add(txt_vlrActl_i, consGrd); // vel aire
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 11;
        consGrd.gridwidth = 1;
        consGrd.gridy = 2;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHWEST; // Izquierda
        panelData.add(lbl_vlrActl_j, consGrd); // m/s y
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 12;
        consGrd.gridwidth = 2;
        consGrd.gridy = 2;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHEAST; // Derecha
        panelData.add(lbl_vlrActl_k, consGrd); // Concentración de gas
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 14;
        consGrd.gridwidth = 1;
        consGrd.gridy = 2;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.CENTER; // Centro
        panelData.add(txt_vlrActl_l, consGrd); // cgas
        consGrd.weightx = 0.0;
        
        consGrd.gridx = 15;
        consGrd.gridwidth = 1;
        consGrd.gridy = 2;
        consGrd.gridheight = 1;
        consGrd.weightx = 1.0;
        consGrd.anchor = GridBagConstraints.NORTHWEST; // Izquierda
        panelData.add(lbl_vlrActl_m, consGrd); // ppm
        consGrd.weightx = 0.0;
        
        // ------------------------
        // ------------------------
        
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
    
    // Actualizar información ambiental
    public void actualizarAmbtInfo() {
        // DATOS ACTUALES
        // Asignar información de iteraciones
        txt_vlrItera_b.setText(Principal.vlrNumero_Iteracion + "/" + Principal.vlrMaximas_Iteraciones);
        txt_vlrItera_d.setText(Principal.vlrDuracion_Iteracion + "");
        // Asignar valores del ambiente
        txt_vlrActl_c.setText(String.format("%.2f", Principal.vlrActl_temperatura));
        txt_vlrActl_f.setText(String.format("%.2f", Principal.vlrActl_humedad));
        txt_vlrActl_i.setText(String.format("%.2f", Principal.vlrActl_velocidadaire));
        txt_vlrActl_l.setText(String.format("%.2f", Principal.vlrActl_gas));
        // Actualizar
        super.update(this.getGraphics());
    }
}
