/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactuador.elementos;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import javaactuador.Principal;

/**
 *
 * @author Jorge
 */
public class Actuador {
    // Variables
    private int num = 0;
    private int pos = 0;
    private String tipo = "";
    // Config: a = Apagado, e = Encendido
    private String estado = "";
    // Aspectos del entornos
    private double valorconfig_temp = 0.0;
    private double valorconfig_hume = 0.0;
    private double valorconfig_vair = 0.0;
    private double valorconfig_cgas = 0.0;
    // Constructor
    public Actuador (int numIde, String tipo) {
        // Variables de configuración
        this.num = numIde;
        this.pos = numIde - 1;
        this.tipo = tipo.toLowerCase();
        // Se inicializa como apagado
        this.estado = "a";
    }
    // Actualizar configuración
    public void actualizarConfig(String[] mjsDiv) {
        // MSJ = "id:ideAct, edo:a, tipo:tipoAct, temp:nwTemp, aire:nwVelAir, hum:nwHume, gas:nwCGas"
        // >> Recuperar y actualizar valor de ESTADO
        String vVale = mjsDiv[1].split(":")[1];
        this.estado = vVale.toLowerCase();
        // >> Si el actuador esta APAGADO
        if (this.estado.equals("a")) {
            // Indicar valor configura en 0
            this.valorconfig_temp = 0;
            this.valorconfig_hume = 0;
            this.valorconfig_vair = 0;
            this.valorconfig_cgas = 0;
        } else {
            // >> Si el actuador esta ENCENDIDO
            // >> De acuerdo al tipo de actuador
            switch (this.tipo){
                case "aire acondicionado":
                    // >> Recuperar y actualizar valor configurado de temperatura
                    vVale = mjsDiv[3].split(":")[1].toLowerCase();
                    this.valorconfig_temp = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de humedas
                    vVale = mjsDiv[5].split(":")[1].toLowerCase();
                    this.valorconfig_hume = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de vel_aire
                    vVale = mjsDiv[4].split(":")[1].toLowerCase();
                    this.valorconfig_vair = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de conce_de_gas
                    vVale = mjsDiv[6].split(":")[1].toLowerCase();
                    this.valorconfig_cgas = Double.parseDouble(vVale);
                    break;
                case "calefactor":
                    // >> Recuperar y actualizar valor configurado de temperatura
                    vVale = mjsDiv[3].split(":")[1].toLowerCase();
                    this.valorconfig_temp = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de humedas
                    vVale = mjsDiv[5].split(":")[1].toLowerCase();
                    this.valorconfig_hume = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de vel_aire
                    vVale = mjsDiv[4].split(":")[1].toLowerCase();
                    this.valorconfig_vair = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de conce_de_gas
                    vVale = mjsDiv[6].split(":")[1].toLowerCase();
                    this.valorconfig_cgas = Double.parseDouble(vVale);
                    break;
                case "ventilador":
                    // >> Recuperar y actualizar valor configurado de temperatura
                    vVale = mjsDiv[3].split(":")[1].toLowerCase();
                    this.valorconfig_temp = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de humedas
                    vVale = mjsDiv[5].split(":")[1].toLowerCase();
                    this.valorconfig_hume = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de vel_aire
                    vVale = mjsDiv[4].split(":")[1].toLowerCase();
                    this.valorconfig_vair = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de conce_de_gas
                    vVale = mjsDiv[6].split(":")[1].toLowerCase();
                    this.valorconfig_cgas = Double.parseDouble(vVale);
                    break;
                case "ventana":
                    // >> Recuperar y actualizar valor configurado de temperatura
                    vVale = mjsDiv[3].split(":")[1].toLowerCase();
                    this.valorconfig_temp = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de humedas
                    vVale = mjsDiv[5].split(":")[1].toLowerCase();
                    this.valorconfig_hume = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de vel_aire
                    vVale = mjsDiv[4].split(":")[1].toLowerCase();
                    this.valorconfig_vair = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de conce_de_gas
                    vVale = mjsDiv[6].split(":")[1].toLowerCase();
                    this.valorconfig_cgas = Double.parseDouble(vVale);
                    break;
                case "puerta":
                    // >> Recuperar y actualizar valor configurado de temperatura
                    vVale = mjsDiv[3].split(":")[1].toLowerCase();
                    this.valorconfig_temp = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de humedas
                    vVale = mjsDiv[5].split(":")[1].toLowerCase();
                    this.valorconfig_hume = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de vel_aire
                    vVale = mjsDiv[4].split(":")[1].toLowerCase();
                    this.valorconfig_vair = Double.parseDouble(vVale);
                    // >> Recuperar y actualizar valor configurado de conce_de_gas
                    vVale = mjsDiv[6].split(":")[1].toLowerCase();
                    this.valorconfig_cgas = Double.parseDouble(vVale);
                    break;
            }
        }
    }
    
    // ------------------------------------------------------------------------
    // Métodos Get and Set
    public String getEstado() {
        // Devolver valor
        return this.estado;
    }
    public String getTipo() {
        // Validar
        if (this.tipo.equals("ventana")) {
            // Devolver valor
            return "ventanas";
        } else {
            // Devolver valor
            return this.tipo;
        }
    }
    // Recuperar los valores configurador
    public double getValorTemperatura() {
        // Devolver valor
        return this.valorconfig_temp;
    }
    public double getValorHumedad() {
        // Devolver valor
        return this.valorconfig_hume;
    }
    public double getValorVelAir() {
        // Devolver valor
        return this.valorconfig_vair;
    }
    public double getValorCGas() {
        // Devolver valor
        return this.valorconfig_cgas;
    }
}
