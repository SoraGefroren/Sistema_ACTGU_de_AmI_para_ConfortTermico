/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.ontologia.madegu.utiles;

import javaactg.ontologia.madegu.utiles.porashrae.StrategyDataPMV;
import javaactg.conforttermico.PMV;
import javaactg.ontologia.madegu.utiles.porashrae.StrategyDtBMI;
import javaactg.ontologia.madegu.utiles.porashrae.StrategyDtEDAD;
import javaactg.ontologia.madegu.utiles.porashrae.StrategyDtSEXO;

/**
 *
 * @author Jorge
 */
public class ResultStrategyASHRAE {
    // Variables de trabajo
    public OntoData[] aryOntoDatas = null;
    public String estrategia = "";
    public int noResults = 0;
    // Constructor
    public ResultStrategyASHRAE (String estrategia, OntoData[] aryOntoDatas, int noResults) {
        // Asignar valores
        this.aryOntoDatas = aryOntoDatas;
        this.estrategia = estrategia;
        this.noResults = noResults;
        // Elegir estrategia
        // Establer la estrategia a ser aplicada
        switch (estrategia) {
            case "MaxV": // Por el más popular (El más votado)
                analisis_de5_MaxV();
                break;
            case "MayPriori": // Por el más respetado (El de mayor rango jerarquico)
                analisis_de4_MayPriori();
                break;
            case "MenosM": // El menos miserable (El grupo con la preferencia más baja)
                analisis_de3_MenosM();
                break;
            case "MayorP": // Al mayor placer (El grupo con la preferencia más alta)
                analisis_de2_MayorP();
                break;
            case "Prome": // Promedio
                analisis_de1_Prome();
                break;
        }
    }
    // Variables de resultado
    public String pmvGrupal_str = "";
    public double pmvGrupal_dbl = 0;
    public double bmiGrupal = 0;
    public double edadGrupal = 0;
    public String sexoGrupal = "";
    // "MaxV": // Por el más popular (El más votado)
    public void analisis_de5_MaxV() {
        // 1.-
        // --------------------------------------------------
        // Variables
        OntoData myOntoData = null;
        StrategyDataPMV[] myStraDataPMV = new StrategyDataPMV[noResults];
        StrategyDtSEXO[] myStraDtSEXO = new StrategyDtSEXO[noResults];
        StrategyDtEDAD[] myStraDtEDAD = new StrategyDtEDAD[noResults];
        StrategyDtBMI[] myStraDtBMI = new StrategyDtBMI[noResults];
        // 1.1.- Inicializar arreglo datos estrategicos
        for (int i = 0; i < noResults; i++) {
            // Asignar dato estrategico
            myStraDataPMV[i] = null;
            myStraDtSEXO[i] = null;
            myStraDtEDAD[i] = null;
            myStraDtBMI[i] = null;
        }
        // 1.2.- Contar a los elementos
        for (int x = 0; x < noResults; x++) {
            // Recuperar data para analisis
            myOntoData = aryOntoDatas[x];
            // Recuperar valores extendidos
            int intEDAD = myOntoData.vEdad;
            Double dblBMI = myOntoData.vBMI;
            String strSEX = myOntoData.vSexo;
            String strPMV = myOntoData.vPMV;
            // Recorrer arreglo de datos estrategicos
            for (int i = 0; i < noResults; i++) {
                // Validar si existe una data
                if ((myStraDataPMV[i] != null) && (myStraDataPMV[i].pmv.equals(strPMV))) {
                    // Actualizar dato estrategico
                    myStraDataPMV[i].updateData(myOntoData.vPrioridad);
                    break;
                } else {
                    // Valida si es un espacio de data estrategica libre
                    if (myStraDataPMV[i] == null) {
                        // Crear nueva instancia de dato estrategico
                        myStraDataPMV[i] = new StrategyDataPMV(strPMV, myOntoData.vPrioridad);
                        break;
                    }
                }
            }
            // Recorrer arreglo de datos estrategicos
            for (int i = 0; i < noResults; i++) {
                // Validar si existe una data
                if ((myStraDtSEXO[i] != null) && (myStraDtSEXO[i].valor.equals(strSEX))) {
                    // Actualizar dato estrategico
                    myStraDtSEXO[i].updateData(myOntoData.vPrioridad);
                    break;
                } else {
                    // Valida si es un espacio de data estrategica libre
                    if (myStraDtSEXO[i] == null) {
                        // Crear nueva instancia de dato estrategico
                        myStraDtSEXO[i] = new StrategyDtSEXO(strSEX, myOntoData.vPrioridad);
                        break;
                    }
                }
            }
            // Recorrer arreglo de datos estrategicos
            for (int i = 0; i < noResults; i++) {
                // Validar si existe una data
                if ((myStraDtBMI[i] != null) && (myStraDtBMI[i].valor == dblBMI)) {
                    // Actualizar dato estrategico
                    myStraDtBMI[i].updateData(myOntoData.vPrioridad);
                    break;
                } else {
                    // Valida si es un espacio de data estrategica libre
                    if (myStraDtBMI[i] == null) {
                        // Crear nueva instancia de dato estrategico
                        myStraDtBMI[i] = new StrategyDtBMI(dblBMI, myOntoData.vPrioridad);
                        break;
                    }
                }
            }
            // Recorrer arreglo de datos estrategicos
            for (int i = 0; i < noResults; i++) {
                // Validar si existe una data
                if ((myStraDtEDAD[i] != null) && (myStraDtEDAD[i].valor == intEDAD)) {
                    // Actualizar dato estrategico
                    myStraDtEDAD[i].updateData(myOntoData.vPrioridad);
                    break;
                } else {
                    // Valida si es un espacio de data estrategica libre
                    if (myStraDtEDAD[i] == null) {
                        // Crear nueva instancia de dato estrategico
                        myStraDtEDAD[i] = new StrategyDtEDAD(intEDAD, myOntoData.vPrioridad);
                        break;
                    }
                }
            }
        }
        // 2.-
        // --------------------------------------------------
        // Variables: numero maximo de votos
        int myNumMaxVtsPMV = 0;
        int myNumMaxVtsBMI = 0;
        int myNumMaxVtsSEXO = 0;
        int myNumMaxVtsEDAD = 0;
        // 2.2.- Elegir al mayor número de votos
        for (int x = 0; x < noResults; x++) {
            // PMV
            // Validar Data estregica
            if (myStraDataPMV[x] != null) {
                // Variables de frencuencia
                int frecuencia = myStraDataPMV[x].getFrequency();
                // Si la frecuencia de i es mayor al voto maximo
                if (frecuencia > myNumMaxVtsPMV) {
                    // Asignar nuevo voto maximo
                    myNumMaxVtsPMV = frecuencia;
                }
            }
            // BMI
            // Validar Data estregica
            if (myStraDtBMI[x] != null) {
                // Variables de frencuencia
                int frecuencia = myStraDtBMI[x].getFrequency();
                // Si la frecuencia de i es mayor al voto maximo
                if (frecuencia > myNumMaxVtsBMI) {
                    // Asignar nuevo voto maximo
                    myNumMaxVtsBMI = frecuencia;
                }
            }
            // SEXO
            // Validar Data estregica
            if (myStraDtSEXO[x] != null) {
                // Variables de frencuencia
                int frecuencia = myStraDtSEXO[x].getFrequency();
                // Si la frecuencia de i es mayor al voto maximo
                if (frecuencia > myNumMaxVtsSEXO) {
                    // Asignar nuevo voto maximo
                    myNumMaxVtsSEXO = frecuencia;
                }
            }
            // EDAD
            // Validar Data estregica
            if (myStraDtEDAD[x] != null) {
                // Variables de frencuencia
                int frecuencia = myStraDtEDAD[x].getFrequency();
                // Si la frecuencia de i es mayor al voto maximo
                if (frecuencia > myNumMaxVtsEDAD) {
                    // Asignar nuevo voto maximo
                    myNumMaxVtsEDAD = frecuencia;
                }
            }
        }
        // 3.-
        // --------------------------------------------------
        // Variables: de usuario
        pmvGrupal_str = "";
        pmvGrupal_dbl = 0;
        bmiGrupal = 0;
        edadGrupal = 0;
        sexoGrupal = "";
        // Variables: numero de elementos
        int myNmInPMV = 0;
        int myNmInBMI = 0;
        int myNmIn_H = 0;
        int myNmIn_M = 0;
        int myNmInEDAD = 0;
        // 3.1.- Sumar elementos más votados
        for (int x = 0; x < noResults; x++) {
            // Validar Data estregica
            if (myStraDataPMV[x] != null) {
                // Variables de frencuencia
                int frecuencia = myStraDataPMV[x].getFrequency();
                // Si la frecuencia i es mayor que el voto maximo
                if (frecuencia == myNumMaxVtsPMV){
                    // Incrementar conteo condiciones
                    myNmInPMV += frecuencia;
                    // Incrementa valor de condición
                    // >> Se suma +4 para tener un valor de 1 a 7
                    // >> Y se multiplica por la frecuencia para evitar sumarlo tantas veces como la frecuencia indique
                    pmvGrupal_dbl += ((Double.parseDouble(myStraDataPMV[x].pmv) + 4) * frecuencia);
                }
            }
            // Validar Data estregica
            if (myStraDtBMI[x] != null) {
                // Variables de frencuencia
                int frecuencia = myStraDtBMI[x].getFrequency();
                // Si la frecuencia i es mayor que el voto maximo
                if (frecuencia == myNumMaxVtsBMI){
                    // Incrementar conteo condiciones
                    myNmInBMI += frecuencia;
                    // Incrementa valor de condición
                    // >> Se multiplica por la frecuencia para evitar sumarlo tantas veces como la frecuencia indique
                    bmiGrupal += (myStraDtBMI[x].valor * frecuencia);
                }
            }
            // Validar Data estregica
            if (myStraDtSEXO[x] != null) {
                // Variables de frencuencia
                int frecuencia = myStraDtSEXO[x].getFrequency();
                // Si la frecuencia i es mayor que el voto maximo
                if (frecuencia == myNumMaxVtsSEXO){
                    // Validar el tipo de valor
                    if (myStraDtSEXO[x].valor.equals("h")) {
                        // Incrementar conteo condiciones
                        myNmIn_H += frecuencia;
                    } else {
                        // Incrementar conteo condiciones
                        myNmIn_M += frecuencia;
                    }
                }
            }
            // Validar Data estregica
            if (myStraDtEDAD[x] != null) {
                // Variables de frencuencia
                int frecuencia = myStraDtEDAD[x].getFrequency();
                // Si la frecuencia i es mayor que el voto maximo
                if (frecuencia == myNumMaxVtsEDAD){
                    // Incrementar conteo condiciones
                    myNmInEDAD += frecuencia;
                    // Incrementa valor de condición
                    // >> Se multiplica por la frecuencia para evitar sumarlo tantas veces como la frecuencia indique
                    edadGrupal += (myStraDtEDAD[x].valor * frecuencia);
                }
            }
        }
        // Definir datos
        pmvGrupal_dbl = (pmvGrupal_dbl/myNmInPMV) - 4;
        // Generar un valor PMV genera como String
        pmvGrupal_str = PMV.convertA55IndexToStr(pmvGrupal_dbl);
        // Calculos extraordinarios
        edadGrupal = edadGrupal/myNmInEDAD;
        bmiGrupal = bmiGrupal/myNmInBMI;
        if (myNmIn_H > myNmIn_M) {
            // Definir dato
            sexoGrupal = "h";
        } else {
            // Definir dato
            sexoGrupal = "m";
        }
        // Validar por error
        if ((pmvGrupal_dbl + "").equals("NaN") || (edadGrupal + "").equals("NaN") || (bmiGrupal + "").equals("NaN")) {
            // Lanzar error
            System.err.println("ASHRAE: Ocurrio un error en los datos de \"analisis_de5_MaxV\"");
        }
    }
    // "MayPriori": // Por el más respetado (El de mayor rango jerarquico)
    public void analisis_de4_MayPriori() {
        // Variables
        OntoData myOntoData = null;
        int myPriorMasAlta = 0;
        int myEdadMasAlta = 0;
        // Buscar prioridad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // Recuperar data
                int intPRIOR = aryOntoDatas[x].vPrioridad;
                // Buscar edad mas alta
                if (myPriorMasAlta < intPRIOR) {
                    // Actualziar edad
                    myPriorMasAlta = intPRIOR;
                }
            }
        }
        // Recorrer usuarios
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // Recuperar valor PMV
                int intPRIOR = aryOntoDatas[x].vPrioridad;
                // Validar data
                if (intPRIOR == myPriorMasAlta) {
                    // Recuperar data
                    int intEDAD = aryOntoDatas[x].vEdad;
                    // Buscar edad mas alta
                    if (myEdadMasAlta < intEDAD) {
                        // Actualziar edad
                        myEdadMasAlta = intEDAD;
                    }
                }
            }
        }
        // Recorrer usuarios
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // Recuperar valor PMV
                int intPRIOR = aryOntoDatas[x].vPrioridad;
                // Validar data
                if (intPRIOR == myPriorMasAlta) {
                    // Recuperar data
                    int intEDAD = aryOntoDatas[x].vEdad;
                    // Buscar edad mas alta
                    if (myEdadMasAlta == intEDAD) {
                        // Actualizar OntoData
                        myOntoData = aryOntoDatas[x];
                        break;
                    }
                }
            }
        }
        // Validar selección de OntoData
        if (myOntoData != null) {
            // Extraer datos
            bmiGrupal = myOntoData.vBMI;
            sexoGrupal = myOntoData.vSexo;
            edadGrupal = myOntoData.vEdad;
            pmvGrupal_str = myOntoData.vPMV;
            // Generar un valor PMV del String
            pmvGrupal_dbl = Double.parseDouble(pmvGrupal_str);
        } else {
            // Lanzar error
            System.err.println("ASHRAE: Ocurrio un error en los datos de \"analisis_de4_MayPriori\"");
        }
    }
    // "MenosM": // El menos miserable (El grupo con la preferencia más baja)
    public void analisis_de3_MenosM() {
        // Variables
        OntoData myOntoData = null;
        // Variable de pmv más alejado de 0
        double myValPmvMenosAceptable = 0;
        double myDistPMVMenosAceptable = 3;
        // Buscar prioridad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // Recuperar valor PMV
                String strPMV = aryOntoDatas[x].vPMV;
                double dblPMV = Double.parseDouble(strPMV);
                double dstPMVCn03 = (3 - Math.abs(dblPMV));
                // Validar seleccion de PMV
                if (dstPMVCn03 <= myDistPMVMenosAceptable) {
                    // La distancia mas chica es la mas cercana a 3
                    myValPmvMenosAceptable = dblPMV;
                    myDistPMVMenosAceptable = dstPMVCn03;
                }
            }
        }
        // Ajuste de tendencia
        myValPmvMenosAceptable = Math.abs(myValPmvMenosAceptable);
        // Variables de lo mas frecuente
        int myPMVPosi = 0;
        int myPMVNega = 0;
        // Buscar la edad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // Recuperar valor PMV
                String strPMV = aryOntoDatas[x].vPMV;
                double dblPMV = Double.parseDouble(strPMV);
                // Validar si coincide la tendencia con el menos conforme
                if (Math.abs(dblPMV) == myValPmvMenosAceptable) {
                    // Validar dirección de la tendencia
                    if (dblPMV == 0) {
                        myPMVPosi++;
                        myPMVNega++;
                    } else if (dblPMV > 0) {
                        myPMVPosi++;
                    } else if (dblPMV < 0) {
                        myPMVNega++;
                    }
                }
            }
        }
        // Buscar la edad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                /// Recuperar valor PMV
                String strPMV = aryOntoDatas[x].vPMV;
                double dblPMV = Double.parseDouble(strPMV);
                // Validar si coincide la tendencia con el menos conforme
                if (Math.abs(dblPMV) == myValPmvMenosAceptable) {
                    // Validar dirección de tendencia
                    if ((myPMVPosi > myPMVNega) || (myPMVPosi < myPMVNega)) {
                        // Validar dirección de la tendencia
                        if ((myPMVPosi > myPMVNega) && (dblPMV > 0)) {
                            // Elegir OntoData
                            myOntoData = aryOntoDatas[x];
                            break;
                        } else if ((myPMVPosi < myPMVNega) && (dblPMV < 0)) {
                            // Elegir OntoData
                            myOntoData = aryOntoDatas[x];
                            break;
                        }
                    } else {
                        /*
                            Si hay igual número de personas con "-30" y "+30",
                            ¿Como decido?
                            - Por edad,
                                si con "-30" hay uno de 25 años y
                                con "+30" tambien hay uno de 25 años
                        */
                        // Elegir OntoData
                        myOntoData = aryOntoDatas[x];
                        break;
                    }
                }
            }
        }
        // Validar selección de OntoData
        if (myOntoData != null) {
            // Extraer datos
            bmiGrupal = myOntoData.vBMI;
            sexoGrupal = myOntoData.vSexo;
            edadGrupal = myOntoData.vEdad;
            pmvGrupal_str = myOntoData.vPMV;
            // Generar un valor PMV del String
            pmvGrupal_dbl = Double.parseDouble(pmvGrupal_str);
        } else {
            // Lanzar error
            System.err.println("ASHRAE: Ocurrio un error en los datos de \"analisis_de3_MenosM\"");
        }
    }
    // "MayorP": // Al mayor placer (El grupo con la preferencia más alta)
    public void analisis_de2_MayorP() {
        // Variables
        OntoData myOntoData = null;
        // Variable de pmv más cercano de 0
        double myValPmvMasAceptable = 0;
        double myDistPMVMasAceptable = 0;
        // Buscar prioridad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // Recuperar valor PMV
                String strPMV = aryOntoDatas[x].vPMV;
                double dblPMV = Double.parseDouble(strPMV);
                double dstPMVCn03 = (3 - Math.abs(dblPMV));
                // Validar seleccion de PMV
                if (dstPMVCn03 >= myDistPMVMasAceptable) {
                    // La distancia mas grande es la mas cercana a 0
                    myValPmvMasAceptable = dblPMV;
                    myDistPMVMasAceptable = dstPMVCn03;
                }
            }
        }
        // Ajuste de tendencia
        myValPmvMasAceptable = Math.abs(myValPmvMasAceptable);
        // Variables de lo mas frecuente
        int myPMVPosi = 0;
        int myPMVNega = 0;
        // Buscar la edad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // Recuperar valor PMV
                String strPMV = aryOntoDatas[x].vPMV;
                double dblPMV = Double.parseDouble(strPMV);
                // Validar si coincide la tendencia con el menos conforme
                if (Math.abs(dblPMV) == myValPmvMasAceptable) {
                    // Validar dirección de la tendencia
                    if (dblPMV == 0) {
                        myPMVPosi++;
                        myPMVNega++;
                    } else if (dblPMV > 0) {
                        myPMVPosi++;
                    } else if (dblPMV < 0) {
                        myPMVNega++;
                    }
                }
            }
        }
        // Buscar la edad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                /// Recuperar valor PMV
                String strPMV = aryOntoDatas[x].vPMV;
                double dblPMV = Double.parseDouble(strPMV);
                // Validar si coincide la tendencia con el menos conforme
                if (Math.abs(dblPMV) == myValPmvMasAceptable) {
                    // Validar dirección de tendencia
                    if ((myPMVPosi > myPMVNega) || (myPMVPosi < myPMVNega)) {
                        // Validar dirección de la tendencia
                        if ((myPMVPosi > myPMVNega) && (dblPMV > 0)) {
                            // Elegir OntoData
                            myOntoData = aryOntoDatas[x];
                            break;
                        } else if ((myPMVPosi < myPMVNega) && (dblPMV < 0)) {
                            // Elegir OntoData
                            myOntoData = aryOntoDatas[x];
                            break;
                        }
                    } else {
                        /*
                            Si hay igual número de personas con "-30" y "+30",
                            ¿Como decido?
                            - Por edad,
                                si con "-30" hay uno de 25 años y
                                con "+30" tambien hay uno de 25 años
                        */
                        // Elegir OntoData
                        myOntoData = aryOntoDatas[x];
                        break;
                    }
                }
            }
        }
        // Validar selección de OntoData
        if (myOntoData != null) {
            // Extraer datos
            bmiGrupal = myOntoData.vBMI;
            sexoGrupal = myOntoData.vSexo;
            edadGrupal = myOntoData.vEdad;
            pmvGrupal_str = myOntoData.vPMV;
            // Generar un valor PMV del String
            pmvGrupal_dbl = Double.parseDouble(pmvGrupal_str);
        } else {
            // Lanzar error
            System.err.println("ASHRAE: Ocurrio un error en los datos de \"analisis_de2_MayorP\"");
        }
    }
    // "Prome": // Promedio
    public void analisis_de1_Prome() {
        // Variables
        OntoData myOntoData = null;
        // Variables: de usuario
        pmvGrupal_str = "";
        pmvGrupal_dbl = 0;
        bmiGrupal = 0;
        edadGrupal = 0;
        sexoGrupal = "";
        // *******************************
        // Variables - Auxiliares
        int myNumH = 0;
        int myNumM = 0;
         // Buscar prioridad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // PROMEDIO
                myOntoData = aryOntoDatas[x];
                // Sumar al valor PMV "4" para tener números del 1 al 7
                pmvGrupal_dbl += (Double.parseDouble(myOntoData.vPMV) + 4);
                // Sumar valores de extencion
                edadGrupal += (myOntoData.vEdad);
                bmiGrupal += (myOntoData.vBMI);
                // Contar mujeres y hombres
                if (myOntoData.vSexo.equals("h")) {
                    myNumH += 1;
                } else {
                    myNumM += 1;
                }
            }
        }
        // Se resta "4" para tener un valor de -3 a +3
        pmvGrupal_dbl = (pmvGrupal_dbl/noResults) - 4;
        // Generar un valor PMV genera como String
        pmvGrupal_str = PMV.convertA55IndexToStr(pmvGrupal_dbl);
        // Calculos extraordinarios
        edadGrupal = (edadGrupal/noResults);
        bmiGrupal = (bmiGrupal/noResults);
        if (myNumH > myNumM) {
            sexoGrupal = "h";
        } else {
            sexoGrupal = "m";
        }
        // Validar por error
        if ((pmvGrupal_dbl + "").equals("NaN") || (edadGrupal + "").equals("NaN") || (bmiGrupal + "").equals("NaN")) {
            // Lanzar error
            System.err.println("ASHRAE: Ocurrio un error en los datos de \"analisis_de1_Prome\"");
        }
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Función que determina si el confort es aceptable
    public boolean isAceptByNormsASHRAE (double dblConcentraGas) {
        // Variables
        boolean esAceptable = false;
        // ----------------------------
        // Validar concentración de gas
        if (dblConcentraGas < 1000) {
            // Variables que marcan el valor de aceptabilidad de PMV, por EDAD
            String pmv_by_edad = "0";
            // Ajustar valor de tolerancia por EDAD
            if ((edadGrupal > 50) && sexoGrupal.equals("h")) {
                // En PMV es = "-1";
                pmv_by_edad = "-1";
            } else if ((edadGrupal <= 50) && sexoGrupal.equals("h")) {
                // En PMV es = "-1";
                pmv_by_edad = "-1";
            } else if ((edadGrupal > 50) && sexoGrupal.equals("m")) {
                // En PMV es =  "0";
                pmv_by_edad = "0";
            } else if ((edadGrupal <= 50) && sexoGrupal.equals("m")) {
                // En PMV es = "+1";
                pmv_by_edad = "+1";
            }
            // Variables que marcan el valor de aceptabilidad de PMV, por BMI
            String pmv_by_bmi = "0";
            // Determinar PMV valido por BMI
            if ((bmiGrupal >= 35.0) && sexoGrupal.equals("h")) {
                // En PMV es = "-1";
                pmv_by_bmi = "-1";
            } else if ((bmiGrupal >= 25.0) && sexoGrupal.equals("h")) {
                // En PMV es = "-1";
                pmv_by_bmi = "-1";
            } else if ((bmiGrupal >= 18.5) && sexoGrupal.equals("h")) {
                // En PMV es = "-1";
                pmv_by_bmi = "-1";
            } else if ((bmiGrupal >= 35.0) && sexoGrupal.equals("m")) {
                // En PMV es = "-1";
                pmv_by_bmi = "-1";
            } else if ((bmiGrupal >= 25.0) && sexoGrupal.equals("m")) {
                // En PMV es = "0";
                pmv_by_bmi = "0";
            } else if ((bmiGrupal >= 18.5) && sexoGrupal.equals("m")) {
                // En PMV es = "+1";
                pmv_by_bmi = "+1";
            }
            // Contar si es o no aceptado de forma normal (Preferencias y Reglas)
            if (pmvGrupal_str.equals("0") || pmv_by_edad.equals(pmvGrupal_str) || pmv_by_bmi.equals(pmvGrupal_str)) {
                // Se indica que el confort el aceptable
                esAceptable = true;
            }
        }
        // ----------------------------
        // Devolver resultado
        return esAceptable;
    }
}
