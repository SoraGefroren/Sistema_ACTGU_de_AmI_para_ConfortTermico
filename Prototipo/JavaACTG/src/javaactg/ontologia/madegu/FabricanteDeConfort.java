/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.ontologia.madegu;

import java.util.Enumeration;
import java.util.Hashtable;
import javaactg.ontologia.madegu.utiles.OntoData;
import javaactg.ontologia.madegu.utiles.ResultStrategyACTGU;
import javaactg.ontologia.madegu.utiles.ResultStrategyASHRAE;
import javaactg.ontologia.madegu.utiles.VectorDeTende;
import javaactg.ontologia.madegu.utiles.poractgu.StrategyDataTEMP;
import javaactg.ontologia.madegu.utiles.poractgu.StrategyDataTEND;
import javaactg.ontologia.madegu.utiles.porashrae.StrategyDataPMV;
import javaactg.ontologia.madegu.utiles.porashrae.StrategyDtBMI;
import javaactg.ontologia.madegu.utiles.porashrae.StrategyDtEDAD;
import javaactg.ontologia.madegu.utiles.porashrae.StrategyDtSEXO;

/**
 *
 * @author Jorge
 */
public class FabricanteDeConfort {
    // Variables
    private int noResults = 0;
    private int inEstrategia = 0;
    private String strEstrategia = "";
    private Hashtable<String, OntoData> diccOntoDatas = null;
    
    // Constructor
    public FabricanteDeConfort(int inEstrategia, int noResults, Hashtable<String, OntoData> diccOntoDatas) {
        // Asignar valores
        this.noResults = noResults;
        this.inEstrategia = inEstrategia;
        this.diccOntoDatas = diccOntoDatas;
        // Establer la estrategia a ser aplicada
        switch (inEstrategia) {
            case 5: // Por el más popular (El más votado)
                this.strEstrategia = "MaxV";
                break;
            case 4: // Por el más respetado (El de mayor rango jerarquico)
                this.strEstrategia = "MayPriori";
                break;
            case 3: // El menos miserable (El grupo con la preferencia más baja)
                this.strEstrategia = "MenosM";
                break;
            case 2: // Al mayor placer (El grupo con la preferencia más alta)
                this.strEstrategia = "MayorP";
                break;
            case 1: // Promedio
                this.strEstrategia = "Prome";
                break;
        }
    }
    
    // CALCULA VALORES DE CONFORT GRUPAL
    // --------------------------------- Si la politica es: ACTGU
    // Por estrategia: Determinar los valores de Confot Termico Grupal con base en los datos resultantes
    public Hashtable<String, ResultStrategyACTGU> analizarVlrsACTGUPorEstrategia() {
        // Diccionario que contiene valores de Confort - PMV aceptable - definidos por estrategia
        Hashtable<String, ResultStrategyACTGU> dicResultsPorEstra = new Hashtable<String, ResultStrategyACTGU>();
        // *******************************
        // Variables - Análisis de los Valores de Confort (VC)
        OntoData[] aryOntoDatas = new OntoData[noResults];
        // Inicializar arreglo datos estrategicos
        for (int i = 0; i < noResults; i++) {
            // Asignar dato estrategico
            aryOntoDatas[i] = null;
        }
        // Recorrer diccionario de resultados
        Enumeration enumllaves = diccOntoDatas.keys();
        while (enumllaves.hasMoreElements()) {
            // Recuperar dato resultandode - usuario y su PMV
            String strKey = enumllaves.nextElement().toString();
            OntoData rData = diccOntoDatas.get(strKey);
            // Recorrer arreglo de datos de usuario
            for (int i = 0; i < noResults; i++) {
                // Valida si es un espacio libre para data de usuario
                if (aryOntoDatas[i] == null) {
                    // Reservar data de usuario
                    aryOntoDatas[i] = rData;
                    break;
                }
            }
        }
        // 5: Por el más popular (El más votado)
        dicResultsPorEstra.put("MaxV",
                new ResultStrategyACTGU("MaxV", aryOntoDatas, noResults));
        
        // Por el menos popular (El menos votado)
        dicResultsPorEstra.put("MimV",
                new ResultStrategyACTGU("MimV", aryOntoDatas, noResults));
        // 1: Promedio
        dicResultsPorEstra.put("Prome",
                new ResultStrategyACTGU("Prome", aryOntoDatas, noResults));
        // 3: El menos miserable (El grupo con la preferencia más baja)
        dicResultsPorEstra.put("MenosM",
                new ResultStrategyACTGU("MenosM", aryOntoDatas, noResults));
        // 2: Al mayor placer (El grupo con la preferencia más alta)
        dicResultsPorEstra.put("MayorP",
                new ResultStrategyACTGU("MayorP", aryOntoDatas, noResults));
        // 4: Por el más respetado (El de mayor rango jerarquico)
        dicResultsPorEstra.put("MayPriori",
                new ResultStrategyACTGU("MayPriori", aryOntoDatas, noResults));
        // Regresar resultado
        return dicResultsPorEstra;
    }
    
    // --------------------------------- Si la politica es: ASHRAE
    // Por estrategia: Determinar los valores de Confot Termico Grupal con base en los datos resultantes
    public Hashtable<String, ResultStrategyASHRAE> analizarVlrsASHRAEPorEstrategia() {
        // Diccionario que contiene valores de Confort - PMV aceptable - definidos por estrategia
        Hashtable<String, ResultStrategyASHRAE> dicResultsPorEstra = new Hashtable<String, ResultStrategyASHRAE>();
        // *******************************
        // Variables - Análisis de los Valores de Confort (VC)
        OntoData[] aryOntoDatas = new OntoData[noResults];
        // Inicializar arreglo datos estrategicos
        for (int i = 0; i < noResults; i++) {
            // Asignar dato estrategico
            aryOntoDatas[i] = null;
        }
        // Recorrer diccionario de resultados
        Enumeration enumllaves = diccOntoDatas.keys();
        while (enumllaves.hasMoreElements()) {
            // Recuperar dato resultandode - usuario y su PMV
            String strKey = enumllaves.nextElement().toString();
            OntoData rData = diccOntoDatas.get(strKey);
            // Recorrer arreglo de datos de usuario
            for (int i = 0; i < noResults; i++) {
                // Valida si es un espacio libre para data de usuario
                if (aryOntoDatas[i] == null) {
                    // Reservar data de usuario
                    aryOntoDatas[i] = rData;
                    break;
                }
            }
        }
        // *******************************
        // 5: Por el más popular (El más votado)
        dicResultsPorEstra.put("MaxV",
                new ResultStrategyASHRAE("MaxV", aryOntoDatas, noResults));
        // Por el menos popular (El menos votado)
        dicResultsPorEstra.put("MimV",
                new ResultStrategyASHRAE("MimV", aryOntoDatas, noResults));
        // 1: Promedio
        dicResultsPorEstra.put("Prome",
                new ResultStrategyASHRAE("Prome", aryOntoDatas, noResults));
        // 3: El menos miserable (El grupo con la preferencia más baja)
        dicResultsPorEstra.put("MenosM",
                new ResultStrategyASHRAE("MenosM", aryOntoDatas, noResults));
        // 2: Al mayor placer (El grupo con la preferencia más alta)
        dicResultsPorEstra.put("MayorP",
                new ResultStrategyASHRAE("MayorP", aryOntoDatas, noResults));
        // 4: Por el más respetado (El de mayor rango jerarquico)
        dicResultsPorEstra.put("MayPriori",
                new ResultStrategyASHRAE("MayPriori", aryOntoDatas, noResults));
        // Regresar resultado
        return dicResultsPorEstra;
    }
    
}
