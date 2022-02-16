/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.ontologia.madegu.utiles;

import javaactg.conforttermico.PMV;
import javaactg.ontologia.madegu.ControladorConfort;

/**
 *
 * @author Jorge
 */
public class OntoData {
    // Variables base
    public int vPersona = 0;
    public int vPrioridad = 0;
    public String vSexo = "";
    public int vEdad = 0;
    public double vBMI = 0;
    public double vMET = 0;
    public double vCLO = 0;
    public String vPMV = "";
    public double vTemperatura = 0;
    public double vConcentraGas = 0;
    
    public double vSlopeConfort = 0;
    public VectorDeTende vVectorTop = null;
    public VectorDeTende vVectorBottom = null;
    
    // Variables adicionales
    public String[] vRangValiPMV = new String[] {"", ""};
    
    // Constructor versión ACTGU
    public OntoData(int pPersona,
            double pMET, double pCLO,
            double pBMI, String pPMV, String pSexo, int pEdad,
            int pPrioridad, double pConcentraGas, double pTemperatura,
            double slopeConfort, VectorDeTende vectorTop, VectorDeTende vectorBottom) {
        // Asignar valores
        vPrioridad = pPrioridad;
        vPersona = pPersona;
        vSexo = pSexo;
        vEdad = pEdad;
        vPMV = pPMV;
        vBMI = pBMI;
        vMET = pMET;
        vCLO = pCLO;
        vTemperatura = pTemperatura;
        vConcentraGas = pConcentraGas;
        vSlopeConfort = slopeConfort;
        vVectorBottom = vectorBottom;
        vVectorTop = vectorTop;
        // Se ejecuta función para generar la data que permita validar el Confort
        analizarDataDeTendencia();
    }
    
    // Variables asignadas de forma interna
    public double tenCTP = 0;
    public double dblTenPMV = 0;
    public String strTenPMV = "";
    
    // Función para generar la data que permita validar el Confort
    public void analizarDataDeTendencia() {
        // Calcular tendencia de confort termico personal
        tenCTP = ControladorConfort.calcularTendeciaM(vSlopeConfort, vTemperatura, vVectorTop);
        // Convertir tendencia en una representación PMV
        dblTenPMV = ControladorConfort.convertirTENDaPMV(tenCTP);
        // Convertir valor a cadena PMV
        strTenPMV = PMV.convertA55IndexToStr(dblTenPMV);
    }
    
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    
    // Función que determina si el confort es aceptable
    public boolean isAceptByNormsACTGU () {
        // Variables
        boolean esAceptable = false;
        // Validar concentración de gas
        if (vConcentraGas < 1000) {
            // Se indica que el confort el aceptable
            esAceptable = strTenPMV.equals("0");
        }
        // Devolver resultado
        return esAceptable;
    }
    
    // Función que devuelve el valor de Confort Térmico de la persona
    public String getConfortACTGU () {
        // Devolver Tendecia convertida en un valor PMV
        return strTenPMV;
    }
    
    // Función que devuelve el estado de confort de la persona
    public String getEstadoACTGU () {
        // Valor de tendencia de confort
        String tenEstado = "";
        // Comprobar si es un estado valido
        if (isAceptByNormsACTGU()) {
            tenEstado = "Comodo";
        } else {
            tenEstado = "Incomodo";
        }
        // Devolver resultado
        return tenEstado;
    }
    
    // Función que devuelve la sensación de confort de la persona
    public String getSensacionACTGU () {
        // Determinar la sensación con respecto del valor PMV
        String r = PMV.convertA55Sensacion(dblTenPMV);
        // Devolver resultado
        return r;
    }
    
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    
    // Función que determina si el confort es aceptable
    public boolean isAceptByNormsASHRAE (int inEstr) {
        // Variables
        boolean esAceptable = false;
        // Validar estrategia
        if (inEstr == 6) {
            // ----------------------------            
            // Ver si valor PMV es 0
            esAceptable = vPMV.equals("0");
            // ----------------------------
        } else {
            // ----------------------------
            // Validar concentración de gas
            if (vConcentraGas < 1000) {
                // Variables que marcan el valor de aceptabilidad de tendencias, por EDAD
                String pmv_by_edad = "0";
                // Ajustar valor de tolerancia por EDAD
                if ((vEdad > 50) && vSexo.equals("h")) {
                    // En PMV es = "-1";
                    pmv_by_edad = "-1";
                } else if ((vEdad <= 50) && vSexo.equals("h")) {
                    // En PMV es = "-1";
                    pmv_by_edad = "-1";
                } else if ((vEdad > 50) && vSexo.equals("m")) {
                    // En PMV es =  "0";
                    pmv_by_edad = "0";
                } else if ((vEdad <= 50) && vSexo.equals("m")) {
                    // En PMV es = "+1";
                    pmv_by_edad = "+1";
                }
                // Variables que marcan el valor de aceptabilidad de tendencias, por BMI
                String pmv_by_bmi = "0";
                // Determinar PMV valido por BMI
                if ((vBMI >= 35.0) && vSexo.equals("h")) {
                    // En PMV es = "-1";
                    pmv_by_bmi = "-1";
                } else if ((vBMI >= 25.0) && vSexo.equals("h")) {
                    // En PMV es = "-1";
                    pmv_by_bmi = "-1";
                } else if ((vBMI >= 18.5) && vSexo.equals("h")) {
                    // En PMV es = "-1";
                    pmv_by_bmi = "-1";
                } else if ((vBMI >= 35.0) && vSexo.equals("m")) {
                    // En PMV es = "-1";
                    pmv_by_bmi = "-1";
                } else if ((vBMI >= 25.0) && vSexo.equals("m")) {
                    // En PMV es = "0";
                    pmv_by_bmi = "0";
                } else if ((vBMI >= 18.5) && vSexo.equals("m")) {
                    // En PMV es = "+1";
                    pmv_by_bmi = "+1";
                }
                // Contar si es o no aceptado de forma normal (Preferencias y Reglas)
                if (vPMV.equals("0") || pmv_by_edad.equals(vPMV) || pmv_by_bmi.equals(vPMV)) {
                    // Se indica que el confort el aceptable
                    esAceptable = true;
                }
            }
            // ----------------------------
        }
        // Devolver resultado
        return esAceptable;
    }
    
    // Función que devuelve el valor de Confort Térmico de la persona
    public String getConfortASHRAE () {
        // Devolver valor PMV cálculado con ASHRAE
        return vPMV;
    }
    
    // Función que devuelve el estado de confort de la persona
    public String getEstadoASHRAE (int inEstr) {
        // Variable
        String strIndex = "";
        // Validar estado
        if (isAceptByNormsASHRAE(inEstr)) {
            strIndex = "Comodo";
        } else {
            strIndex = "Incomodo";
        }
        // Devolver resultado
        return strIndex;
    }
    
    // Función que devuelve la sensación de confort de la persona
    public String getSensacionASHRAE (int inEstr) {
        // Variable
        double db = Double.parseDouble(vPMV);
        String r = PMV.convertA55Sensacion(db);
        // Devolver resultado
        return r;
    }
    
}
