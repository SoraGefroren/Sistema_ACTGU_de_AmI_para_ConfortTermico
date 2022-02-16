/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.ontologia.madegu.utiles.porashrae;

/**
 *
 * @author Jorge
 */
public class StrategyDataPMV {
    // Variables
    public String pmv = "";
    public int contpmv = 0;
    public int prioridad = 0;
    public int contprioridad = 0;
    
    // Constructor
    public StrategyDataPMV(String pp, int prior) {
        // Asignar valores
        pmv = pp;
        prioridad = prior;
        // Contar valores
        contpmv = 1;
        contprioridad = 1;
        
    }
    
    // Actualizar datos estrategicos
    public void updateData(int prior) {
        // Contar valor
        contpmv += 1;
        // Validar si la prioridad entrante es mayor
        if (prioridad < prior) {
            // Actualizar valores
            prioridad = prior;
            // Reiniciar contador de prioridad
            contprioridad = 1;
        } else {
            // Validar si actualizar contador de prioridad
            if (prioridad == prior) {
                // Actualizar contador de prioridad
                contprioridad += 1;
            }
        }
    }
    
    // Función de determina la frecuencia el valor PMV
    public int getFrequency() {
        // Regresar la frencia del valor PMV
        return contpmv;
    }
    
    // Función de determina la prioridad maxima conocida
    public int getMaxPriority() {
        // Regresar la frencuena de la condición 
        return prioridad;
    }
    
    // Función de determina la frecuencia en que fue contada la prioridad maxima
    public int getNumRepDeMaxPrior() {
        // Regresar la frencuena de la prioridad maxima
        return contprioridad;
    }
}
