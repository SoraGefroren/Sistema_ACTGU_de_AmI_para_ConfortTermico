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
public class StrategyDtEDAD {
    // Variables
    public int valor = 0;
    public int contador = 0;
    public int prioridad = 0;
    public int contprioridad = 0;
    
    // Constructor
    public StrategyDtEDAD(int vll, int prior) {
        // Asignar valores
        valor = vll;
        prioridad = prior;
        // Contar valores
        contador = 1;
        contprioridad = 1;
        
    }
    
    // Actualizar datos estrategicos
    public void updateData(int prior) {
        // Contar valor
        contador += 1;
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
    
    // Función de determina la frecuencia el valor
    public int getFrequency() {
        // Regresar la frencia del valor
        return contador;
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
