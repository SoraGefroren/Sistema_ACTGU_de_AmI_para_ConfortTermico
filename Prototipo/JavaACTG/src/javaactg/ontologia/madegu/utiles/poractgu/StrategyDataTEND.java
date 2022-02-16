/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.ontologia.madegu.utiles.poractgu;

/**
 *
 * @author Jorge
 */
public class StrategyDataTEND {
    // Variables
    public double tendencia = 0;
    public int conttend = 0;
    public int prioridad = 0;
    public int contprioridad = 0;
    
    // Constructor
    public StrategyDataTEND(double val, int prior) {
        // Asignar valores
        tendencia = val;
        prioridad = prior;
        // Contar valores
        conttend = 1;
        contprioridad = 1;
        
    }
    
    // Actualizar datos estrategicos
    public void updateData(int prior) {
        // Contar valor
        conttend += 1;
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
        return conttend;
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
