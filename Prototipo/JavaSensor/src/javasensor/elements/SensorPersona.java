/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensor.elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Hashtable;
import javasensor.Principal;

/**
 *
 * @author Jorge
 */
public class SensorPersona {
    // Variables
    private int num = 0;
    // Constructor
    public SensorPersona (int  num) {
        // Asignaciones
        this.num = num;
        ArrayList<String[]> mtxTemp = new ArrayList<String[]>();
        // Inializar data de acelerometro
        for (int i = 0; i < 120; i++) {
            // Agregar data a la matriz temporal
            mtxTemp.add(genDataAcelerometros());
        }
        // Renovar matriz de datos de acelerometros
        actualizarAcelData(mtxTemp);
    }
    // Generar data de acelerometro
    public String[] genDataAcelerometros () {
        // Variable
        String[] acelData = new String[3];
        Random rand = new Random();
        // Asignar valor de data
        acelData[0] = "" + ((rand.nextDouble() * 10.00) + (rand.nextDouble() * 100.00));
        acelData[1] = "" + ((rand.nextDouble() * 10.00) + (rand.nextDouble() * 100.00));
        acelData[2] = "" + ((rand.nextDouble() * 10.00) + (rand.nextDouble() * 100.00));
        // Devolver data 
        return acelData;
    }
    // Actualizar datos de acelerometro
    public void actualizarAcelData(ArrayList<String[]> mtxAcls) {
        // Variables
        int numAcelData = mtxAcls.size();
        double[][] matrizDBLs = new double[numAcelData][3];
        // Recorrer espacio de datos
        for (int i = 0; i < numAcelData; i++) {
            // Extraer acels data 
            String[] acelData = mtxAcls.get(i);
            // Asignar a matriz de dobles
            matrizDBLs[i][0] = Double.parseDouble(acelData[0]);
            matrizDBLs[i][1] = Double.parseDouble(acelData[1]);
            matrizDBLs[i][2] = Double.parseDouble(acelData[2]);
        }
        // Validar en Principal
        String idePerson = this.num + "";
        // Validar existencia de id
        if (Principal.valores_acelerometro.containsKey(idePerson)) {
            // Reemplazar matriz de dobles
            Principal.valores_acelerometro.replace(idePerson, matrizDBLs);
        } else {
            // Agregar matriz de dobles
            Principal.valores_acelerometro.put(idePerson, matrizDBLs);
        }
    }
}
