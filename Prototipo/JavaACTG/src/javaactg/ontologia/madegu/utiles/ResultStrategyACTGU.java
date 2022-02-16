/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.ontologia.madegu.utiles;

import javaactg.ontologia.madegu.ControladorConfort;
import javaactg.ontologia.madegu.utiles.poractgu.StrategyDataTEMP;
import javaactg.ontologia.madegu.utiles.poractgu.StrategyDataTEND;

/**
 *
 * @author Jorge
 */
public class ResultStrategyACTGU {
    // Variables de trabajo
    public OntoData[] aryOntoDatas = null;
    public String estrategia = "";
    public int noResults = 0;
    // Constructor
    public ResultStrategyACTGU (String estrategia, OntoData[] aryOntoDatas, int noResults) {
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
    public double pendiente = 0; // Valor "m"
    public VectorDeTende vectorTop = null; // X1, Y1
    public VectorDeTende vectorBot = null; // X2, Y2
    
    // "MaxV": // Por el más popular (El más votado)
    public void analisis_de5_MaxV() {
        // 1.-
        // --------------------------------------------------
        // Variables
        OntoData myOntoData = null;
        VectorDeTende myVectTop = null;
        VectorDeTende myVectBot = null;
        StrategyDataTEMP[] myStraDataTempTop = new StrategyDataTEMP[noResults];
        StrategyDataTEND[] myStraDataTendTop = new StrategyDataTEND[noResults];
        StrategyDataTEMP[] myStraDataTempBot = new StrategyDataTEMP[noResults];
        StrategyDataTEND[] myStraDataTendBot = new StrategyDataTEND[noResults];
        // 1.1.- Inicializar arreglo datos estrategicos
        for (int x = 0; x < noResults; x++) {
            // Asignar dato estrategico
            myStraDataTempTop[x] = null;
            myStraDataTendTop[x] = null;
            // Asignar dato estrategico
            myStraDataTempBot[x] = null;
            myStraDataTendBot[x] = null;
        }
        // 1.2.- Contar a los elementos
        for (int x = 0; x < noResults; x++) {
            // Recuperar data para analisis
            myOntoData = aryOntoDatas[x];
            myVectTop = myOntoData.vVectorTop;
            myVectBot = myOntoData.vVectorBottom;
            // CONDICION TOP
            // Recorrer arreglo de datos estrategicos
            for (int i = 0; i < noResults; i++) {
                // Validar si existe una data
                if ((myStraDataTempTop[i] != null) && (myStraDataTempTop[i].condicion == myVectTop.condition)) {
                    // Actualizar dato estrategico
                    myStraDataTempTop[i].updateData(myOntoData.vPrioridad);
                    break;
                } else {
                    // Valida si es un espacio de data estrategica libre
                    if (myStraDataTempTop[i] == null) {
                        // Crear nueva instancia de dato estrategico
                        myStraDataTempTop[i] = new StrategyDataTEMP(myVectTop.condition, myOntoData.vPrioridad);
                        break;
                    }
                }
            }
            // TENDENCIA TOP
            // Recorrer arreglo de datos estrategicos
            for (int i = 0; i < noResults; i++) {
                // Validar si existe una data
                if ((myStraDataTendTop[i] != null) && (myStraDataTendTop[i].tendencia == myVectTop.tendency)) {
                    // Actualizar dato estrategico
                    myStraDataTendTop[i].updateData(myOntoData.vPrioridad);
                    break;
                } else {
                    // Valida si es un espacio de data estrategica libre
                    if (myStraDataTendTop[i] == null) {
                        // Crear nueva instancia de dato estrategico
                        myStraDataTendTop[i] = new StrategyDataTEND(myVectTop.tendency, myOntoData.vPrioridad);
                        break;
                    }
                }
            }
            // CONDICION BOT
            // Recorrer arreglo de datos estrategicos
            for (int i = 0; i < noResults; i++) {
                // Validar si existe una data
                if ((myStraDataTempBot[i] != null) && (myStraDataTempBot[i].condicion == myVectBot.condition)) {
                    // Actualizar dato estrategico
                    myStraDataTempBot[i].updateData(myOntoData.vPrioridad);
                    break;
                } else {
                    // Valida si es un espacio de data estrategica libre
                    if (myStraDataTempBot[i] == null) {
                        // Crear nueva instancia de dato estrategico
                        myStraDataTempBot[i] = new StrategyDataTEMP(myVectBot.condition, myOntoData.vPrioridad);
                        break;
                    }
                }
            }
            // TENDENCIA BOT
            // Recorrer arreglo de datos estrategicos
            for (int i = 0; i < noResults; i++) {
                // Validar si existe una data
                if ((myStraDataTendBot[i] != null) && (myStraDataTendBot[i].tendencia == myVectBot.tendency)) {
                    // Actualizar dato estrategico
                    myStraDataTendBot[i].updateData(myOntoData.vPrioridad);
                    break;
                } else {
                    // Valida si es un espacio de data estrategica libre
                    if (myStraDataTendBot[i] == null) {
                        // Crear nueva instancia de dato estrategico
                        myStraDataTendBot[i] = new StrategyDataTEND(myVectBot.tendency, myOntoData.vPrioridad);
                        break;
                    }
                }
            }
        }
        // 2.-
        // --------------------------------------------------
        // Variables: numero maximo de votos
        int myNumMaxVtsTempTop = 0;
        int myNumMaxVtsTendTop = 0;
        int myNumMaxVtsTempBot = 0;
        int myNumMaxVtsTendBot = 0;
        // 2.2.- Elegir al mayor número de votos
        for (int x = 0; x < noResults; x++) {
            // TOP
            // Validar Data estregica
            if (myStraDataTempTop[x] != null) {
                // Variables de frencuencia
                int frecuenciaTop = myStraDataTempTop[x].getFrequency();
                // Si la frecuencia de i es mayor al voto maximo
                if (frecuenciaTop > myNumMaxVtsTempTop) {
                    // Asignar nuevo voto maximo
                    myNumMaxVtsTempTop = frecuenciaTop;
                }
            }
            // Validar Data estregica
            if (myStraDataTendTop[x] != null) {
                // Variables de frencuencia
                int frecuenciaTop = myStraDataTendTop[x].getFrequency();
                // Si la frecuencia de i es mayor al voto maximo
                if (frecuenciaTop > myNumMaxVtsTendTop) {
                    // Asignar nuevo voto maximo
                    myNumMaxVtsTendTop = frecuenciaTop;
                }
            }
            // BOT
            // Validar Data estregica
            if (myStraDataTempBot[x] != null) {
                // Variables de frencuencia
                int frecuenciaBot = myStraDataTempBot[x].getFrequency();
                // Si la frecuencia de i es mayor al voto maximo
                if (frecuenciaBot > myNumMaxVtsTempBot) {
                    // Asignar nuevo voto maximo
                    myNumMaxVtsTempBot = frecuenciaBot;
                }
            }
             // Validar Data estregica
            if (myStraDataTendBot[x] != null) {
                // Variables de frencuencia
                int frecuenciaBot = myStraDataTendBot[x].getFrequency();
                // Si la frecuencia de i es mayor al voto maximo
                if (frecuenciaBot > myNumMaxVtsTendBot) {
                    // Asignar nuevo voto maximo
                    myNumMaxVtsTendBot = frecuenciaBot;
                }
            }
        }
        // 3.-
        // --------------------------------------------------
        // Variables: condiciones y tendencias
        double myConTop = 0;
        double myTenTop = 0;
        double myConBot = 0;
        double myTenBot = 0;
        // Variables: numero de elementos top y bot
        int myNmInTempTop = 0;
        int myNmInTendTop = 0;
        int myNmInTempBot = 0;
        int myNmInTendBot = 0;
        // 3.1.- Sumar elementos más votados
        for (int x = 0; x < noResults; x++) {
            // TOP
            // Validar Data estregica
            if (myStraDataTempTop[x] != null) {
                // Variables de frencuencia
                int frecuenciaTop = myStraDataTempTop[x].getFrequency();
                // Si la frecuencia i es mayor que el voto maximo
                if (frecuenciaTop == myNumMaxVtsTempTop){
                    // Incrementar conteo condiciones
                    myNmInTempTop += frecuenciaTop;
                    // Incrementa valor de condición
                    myConTop += (myStraDataTempTop[x].condicion * frecuenciaTop);
                }
            }
            // Validar Data estregica
            if (myStraDataTendTop[x] != null) {
                // Variables de frencuencia
                int frecuenciaTop = myStraDataTendTop[x].getFrequency();
                // Si la frecuencia i es mayor que el voto maximo
                if (frecuenciaTop == myNumMaxVtsTendTop){
                    // Incrementar conteo tendencia
                    myNmInTendTop += frecuenciaTop;
                    // Incrementa valor de tendencia
                    myTenTop += ((myStraDataTendTop[x].tendencia + 51) /* para escala 1 a 101 */ * frecuenciaTop);
                }
            }
            // BOT
            // Validar Data estregica
            if (myStraDataTempBot[x] != null) {
                // Variables de frencuencia
                int frecuenciaBot = myStraDataTempBot[x].getFrequency();
                // Si la frecuencia i es mayor que el voto maximo
                if (frecuenciaBot == myNumMaxVtsTempBot){
                    // Incrementar conteo condiciones
                    myNmInTempBot += frecuenciaBot;
                    // Incrementa valor de condición
                    myConBot += (myStraDataTempBot[x].condicion * frecuenciaBot);
                }
            }
            // Validar Data estregica
            if (myStraDataTendBot[x] != null) {
                // Variables de frencuencia
                int frecuenciaBot = myStraDataTendBot[x].getFrequency();
                // Si la frecuencia i es mayor que el voto maximo
                if (frecuenciaBot == myNumMaxVtsTendBot){
                    // Incrementar conteo tendencia
                    myNmInTendBot += frecuenciaBot;
                    // Incrementa valor de tendencia
                    myTenBot += ((myStraDataTendBot[x].tendencia + 51) /* para escala 1 a 101 */ * frecuenciaBot);
                }
            }
        }
        // 4.-
        // --------------------------------------------------
        // Definir datos
        myConTop = myConTop/myNmInTempTop;
        myTenTop = (myTenTop/myNmInTendTop) - 51; // en escala 1 a 101
        myConBot = myConBot/myNmInTempBot;
        myTenBot = (myTenBot/myNmInTendBot) - 51; // en escala 1 a 101
        // Validar selección de OntoData
        if (!((myConTop + "").equals("NaN") || (myTenTop + "").equals("NaN") ||
           (myConBot + "").equals("NaN") || (myTenBot + "").equals("NaN"))) {
            // Determinar valor de vectores
            vectorTop = ControladorConfort.determinarVectorTop(myConTop, myTenTop, myConBot, myTenBot); // X1, Y1
            vectorBot = ControladorConfort.determinarVectorBottom(myConTop, myTenTop, myConBot, myTenBot); // X2, Y2
            // Calcular pendiente "m" de confort
            pendiente = ControladorConfort.calcularPendienteM(myConTop, myTenTop, myConBot, myTenBot);
        } else {
            // Lanzar error
            System.err.println("ACTGU: Ocurrio un error en los datos de \"analisis_de5_MaxV\"");
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
        // Buscar la edad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // Recuperar valor PMV
                int priorDOD = aryOntoDatas[x].vPrioridad;
                // Validar data
                if (priorDOD == myPriorMasAlta) {
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
                int priorDOD = aryOntoDatas[x].vPrioridad;
                // Validar data
                if (priorDOD == myPriorMasAlta) {
                    // Recuperar data
                    int intEDAD = aryOntoDatas[x].vEdad;
                    // Buscar edad mas alta
                    if (myEdadMasAlta == intEDAD) {
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
            vectorTop = myOntoData.vVectorTop; // X1, Y1
            vectorBot = myOntoData.vVectorBottom; // X2, Y2
            // Calcular pendiente "m" de confort
            pendiente = myOntoData.vSlopeConfort;
        } else {
            // Lanzar error
            System.err.println("ACTGU: Ocurrio un error en los datos de \"analisis_de4_MayPriori\"");
        }
    }
    // "MenosM": // El menos miserable (El grupo con la preferencia más baja)
    public void analisis_de3_MenosM() {
        // Variables
        OntoData myOntoData = null;
        // Variable de tendencia más alejada de 0
        double myValTendMenosAceptable = 0;
        double myDistTendMenosAceptable = 50;
        // Buscar prioridad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // Recuperar tendencia
                double tendDeUsr = aryOntoDatas[x].tenCTP;
                double dstTndCn50 = (50 - Math.abs(tendDeUsr));
                // Validar seleccion de tendencia
                if (dstTndCn50 <= myDistTendMenosAceptable) {
                    // La distancia mas chica es la mas cercana a 50
                    myValTendMenosAceptable = tendDeUsr;
                    myDistTendMenosAceptable = dstTndCn50;
                }
            }
        }
        // Ajuste de tendencia
        myValTendMenosAceptable = Math.abs(myValTendMenosAceptable);
        // Variables de lo mas frecuente
        int myTendPosi = 0;
        int myTendNega = 0;
        // Buscar la edad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // Recuperar tendencia
                double tendDeUsr = aryOntoDatas[x].tenCTP;
                // Validar si coincide la tendencia con el menos conforme
                if (Math.abs(tendDeUsr) == myValTendMenosAceptable) {
                    // Validar dirección de la tendencia
                    if (tendDeUsr == 0) {
                        myTendPosi++;
                        myTendNega++;
                    } else if (tendDeUsr > 0) {
                        myTendPosi++;
                    } else if (tendDeUsr < 0) {
                        myTendNega++;
                    }
                }
            }
        }
        // Buscar la edad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // Recuperar tendencia
                double tendDeUsr = aryOntoDatas[x].tenCTP;
                // Validar si coincide la tendencia con el menos conforme
                if (Math.abs(tendDeUsr) == myValTendMenosAceptable) {
                    // Validar dirección de tendencia
                    if ((myTendPosi > myTendNega) || (myTendPosi < myTendNega)) {
                        // Validar dirección de la tendencia
                        if ((myTendPosi > myTendNega) && (tendDeUsr > 0)) {
                            // Elegir OntoData
                            myOntoData = aryOntoDatas[x];
                            break;
                        } else if ((myTendPosi < myTendNega) && (tendDeUsr < 0)) {
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
            vectorTop = myOntoData.vVectorTop; // X1, Y1
            vectorBot = myOntoData.vVectorBottom; // X2, Y2
            // Calcular pendiente "m" de confort
            pendiente = myOntoData.vSlopeConfort;
        } else {
            // Lanzar error
            System.err.println("ACTGU: Ocurrio un error en los datos de \"analisis_de3_MenosM\"");
        }
    }
    // "MayorP": // Al mayor placer (El grupo con la preferencia más alta)
    public void analisis_de2_MayorP() {
        // Variables
        OntoData myOntoData = null;
        // Variable de tendencia más cercana a 0
        double myValTendMasAceptable = 0;
        double myDistTendMasAceptable = 0;
        // Buscar prioridad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // Recuperar tendencia
                double tendDeUsr = aryOntoDatas[x].tenCTP;
                // double dstTndCn50 = (50 - Math.abs(tendDeUsr));
                double dstTndCn50 = 0;
                // Ajuste de tendencia
                if (Math.abs(tendDeUsr) > 50) {
                    dstTndCn50 = 0;
                } else {
                    dstTndCn50 = (50 - Math.abs(tendDeUsr));
                }
                // Validar seleccion de tendencia
                if (dstTndCn50 >= myDistTendMasAceptable) {
                    // La distancia mas grande es la mas cercana a 0
                    myValTendMasAceptable = tendDeUsr;
                    myDistTendMasAceptable = dstTndCn50;
                }
            }
        }
        // Ajuste de tendencia
        myValTendMasAceptable = Math.abs(myValTendMasAceptable);
        // Variables de lo mas frecuente
        int myTendPosi = 0;
        int myTendNega = 0;
        // Buscar la edad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // Recuperar tendencia
                double tendDeUsr = aryOntoDatas[x].tenCTP;
                // Validar si coincide la tendencia con el menos conforme
                if (Math.abs(tendDeUsr) == myValTendMasAceptable) {
                    // Validar dirección de la tendencia
                    if (tendDeUsr == 0) {
                        myTendPosi++;
                        myTendNega++;
                    } else if (tendDeUsr > 0) {
                        myTendPosi++;
                    } else if (tendDeUsr < 0) {
                        myTendNega++;
                    }
                }
            }
        }
        // Buscar la edad mas alta
        for (int x = 0; x < noResults; x++) {
            // Validar usuario
            if (aryOntoDatas[x] != null) {
                // Recuperar tendencia
                double tendDeUsr = aryOntoDatas[x].tenCTP;
                // Validar si coincide la tendencia con el menos conforme
                if (Math.abs(tendDeUsr) == myValTendMasAceptable) {
                    // Validar dirección de tendencia
                    if ((myTendPosi > myTendNega) || (myTendPosi < myTendNega)) {
                        // Validar dirección de la tendencia
                        if ((myTendPosi > myTendNega) && (tendDeUsr > 0)) {
                            // Elegir OntoData
                            myOntoData = aryOntoDatas[x];
                            break;
                        } else if ((myTendPosi < myTendNega) && (tendDeUsr < 0)) {
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
            vectorTop = myOntoData.vVectorTop; // X1, Y1
            vectorBot = myOntoData.vVectorBottom; // X2, Y2
            // Calcular pendiente "m" de confort
            pendiente = myOntoData.vSlopeConfort;
        } else {
            // Lanzar error
            System.err.println("ACTGU: Ocurrio un error en los datos de \"analisis_de2_MayorP\"");
        }
    }
    // "Prome": // Promedio
    public void analisis_de1_Prome() {
        // Variables
        double myConPromeTop = 0;
        double myTenPromeTop = 0;
        double myConPromeBot = 0;
        double myTenPromeBot = 0;
        OntoData myOntoData = null;
        VectorDeTende myVectTop = null;
        VectorDeTende myVectBot = null;
        // Buscar prioridad mas alta
        for (int x = 0; x < noResults; x++) {
            // Recupera data de trabajo
            myOntoData = aryOntoDatas[x];
            myVectTop = myOntoData.vVectorTop;
            myVectBot = myOntoData.vVectorBottom;
            // Sumar valor de condiciones de vectores
            myConPromeTop += myVectTop.condition;
            myTenPromeTop += myVectTop.tendency + 51; // como escala 1 a 101
            // Sumar valor de condiciones de vectores
            myConPromeBot += myVectBot.condition;
            myTenPromeBot += myVectBot.tendency + 51; // como escala 1 a 101
        }
        // Calculos extraordinarios
        myConPromeTop = myConPromeTop/noResults;
        myTenPromeTop = (myTenPromeTop/noResults) - 51; // poner en escala -50 a 50
        myConPromeBot = myConPromeBot/noResults;
        myTenPromeBot = (myTenPromeBot/noResults) - 51; // poner en escala -50 a 50
        // Validar selección de OntoData
        if (!((myConPromeTop + "").equals("NaN") || (myTenPromeTop + "").equals("NaN") ||
           (myConPromeBot + "").equals("NaN") || (myTenPromeBot + "").equals("NaN"))) {
            // Determinar valor de vectores
            vectorTop = ControladorConfort.determinarVectorTop(myConPromeTop, myTenPromeTop, myConPromeBot, myTenPromeBot); // X1, Y1
            vectorBot = ControladorConfort.determinarVectorBottom(myConPromeTop, myTenPromeTop, myConPromeBot, myTenPromeBot); // X2, Y2
            // Calcular pendiente "m" de confort
            pendiente = ControladorConfort.calcularPendienteM(myConPromeTop, myTenPromeTop, myConPromeBot, myTenPromeBot);
        } else {
            // Lanzar error
            System.err.println("ACTGU: Ocurrio un error en los datos de \"analisis_de1_Prome\"");
        }
    }
}
