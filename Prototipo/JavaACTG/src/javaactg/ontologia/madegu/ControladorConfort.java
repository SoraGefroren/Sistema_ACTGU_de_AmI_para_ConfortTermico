/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.ontologia.madegu;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.util.Enumeration;
import java.util.Hashtable;
import javaactg.Principal;
import javaactg.conforttermico.ConfortTermico;
import javaactg.conforttermico.PMV;
import javaactg.mqtt.PublicadorMQTT;
import javaactg.ontologia.InspectorONTO;
import javaactg.ontologia.modelo.InstanciaJENA;
import javaactg.ontologia.madegu.utiles.OntoData;
import javaactg.ontologia.madegu.utiles.VectorDeTende;
import javaactg.ontologia.madegu.utiles.ResultStrategyACTGU;
import javaactg.ontologia.madegu.utiles.ResultStrategyASHRAE;
import javaactg.ontologia.madegu.utiles.poractgu.StrategyDataTEMP;
import javaactg.ontologia.madegu.utiles.porashrae.StrategyDataPMV;
import javaactg.ontologia.madegu.utiles.poractgu.StrategyDataTEND;
import javaactg.ontologia.madegu.utiles.porashrae.StrategyDtBMI;
import javaactg.ontologia.madegu.utiles.porashrae.StrategyDtEDAD;
import javaactg.ontologia.madegu.utiles.porashrae.StrategyDtSEXO;

/**
 *
 * @author Jorge
 */
public class ControladorConfort {
    
    // Variables: Estrategia, Grupo, Intancia de JENA y Diccionario de Resultados
    private int inEstrategia = 0;
    private String strPolitica = "";
    private InstanciaJENA instanciaJENA = null;
    private Hashtable<String, OntoData> diccOntoDatas = null;
    private int noResults = 0;
    
    // Variables para datos sobre el ambiente
    private double valTemp = 0.0;
    private double valVAir = 0.0;
    private double valHumd = 0.0;
    private double valCGas = 0.0;
    
    // Constructor
    public ControladorConfort(String strPoli, int inEstr, JsonObject jsnObjAmbt, JsonArray jsonDataResul, InstanciaJENA insJENA) {
        // Asignar instancia JENA
        instanciaJENA = insJENA;        
        // Asignar estrategia y grupo a utilizar
        strPolitica = strPoli;
        inEstrategia = inEstr;
        // Recuperar valores del ambiente
        valTemp = Double.parseDouble(jsnObjAmbt.get(InspectorONTO.cmpTempera).toString().replaceAll("\"", ""));
        valVAir = Double.parseDouble(jsnObjAmbt.get(InspectorONTO.cmpVelAire).toString().replaceAll("\"", ""));
        valHumd = Double.parseDouble(jsnObjAmbt.get(InspectorONTO.cmpHumedad).toString().replaceAll("\"", ""));
        valCGas = Double.parseDouble(jsnObjAmbt.get(InspectorONTO.cmpConcGas).toString().replaceAll("\"", ""));
        // Generar diccionario con datos resultantes
        diccOntoDatas = generarDiccOntoDatas(jsonDataResul, valTemp);
    }
    
    // Función que prepara los datos resultantes para sus análisis
    public Hashtable<String, OntoData> generarDiccOntoDatas (JsonArray jsonArray, double vpTemp) {
        // Preparar variables
        JsonObject jsResultObj = null;
        Hashtable<String, OntoData> dicResu = new Hashtable<String, OntoData>();
        // Calcular tamaño de arreglo
        int tamJsonAry = jsonArray.size();
        // Recorrer JSON-ARRAY
        for (int x = 0; x < tamJsonAry; x ++) {
            // Recuperar instancia de objeto JSON OBJECT
            jsResultObj = jsonArray.get(x).getAsJsonObject();
            // Obtener ID de registro de instancia JSON
            String strIDPerson = jsResultObj.get("val_persona").toString().replaceAll("\"", "");
            // Validar si se esta revisando un nuevo registro
            if (dicResu.get(strIDPerson) == null) {
                // Obtener datos de instancia JSON a ser resguardados
                String nmllConfTrnd = jsResultObj.get("val_confortren").toString().replaceAll("\"", "");
                String nmllPMV = jsResultObj.get("val_pmv").toString().replaceAll("\"", "");
                // Para validar datos
                boolean isOk = true;
                // TRY - CATCH
                try {
                    int testPMV_Act = Integer.parseInt(nmllPMV);
                    // Validar
                    if ((testPMV_Act > 3) || (testPMV_Act < -3)) {
                        isOk = false;
                    }
                } catch (Exception e) {
                    isOk = false;
                }
                // Crear inspector de Ontología
                InspectorONTO inspOnto = new InspectorONTO(instanciaJENA);
                double slopeConfort = 0;
                VectorDeTende vectorTop = null;
                VectorDeTende vectorBottom = null;
                // --- >> ?val_slopeconfort ?val_vector ?val_condi ?val_tende ?val_confortren
                // Consultar Ontologia y devolver vectores de la tendencia de confort
                JsonArray jsAryTm = inspOnto.recuperaVectoresDeLaTendencia(nmllConfTrnd);
                int tamAry = jsAryTm.size();
                // Validar existencia de dos Vectores
                if (tamAry >= 2) {
                    // Recupera PENDIENTE "m" de CONFORT
                    JsonObject jsTemp = jsAryTm.get(0).getAsJsonObject();
                    slopeConfort = Double.parseDouble(jsTemp.getAsJsonObject("val_slopeconfort").get("value").toString().replaceAll("\"", ""));
                    // Recupera VECTOR de la TENDENCIA de CONFORT
                    double conOne = Double.parseDouble(jsTemp.getAsJsonObject("val_condi").get("value").toString().replaceAll("\"", ""));
                    double tenOne = Double.parseDouble(jsTemp.getAsJsonObject("val_tende").get("value").toString().replaceAll("\"", ""));
                    // Recupera VECTOR de la TENDENCIA de CONFORT
                    jsTemp = jsAryTm.get(1).getAsJsonObject();
                    double conTwo = Double.parseDouble(jsTemp.getAsJsonObject("val_condi").get("value").toString().replaceAll("\"", ""));
                    double tenTwo = Double.parseDouble(jsTemp.getAsJsonObject("val_tende").get("value").toString().replaceAll("\"", ""));
                    // Determinar cual es el vector TOP y BOTTOM
                    vectorTop = determinarVectorTop(conOne, tenOne, conTwo, tenTwo);
                    vectorBottom = determinarVectorBottom(conOne, tenOne, conTwo, tenTwo);
                }
                // Obtener datos de instancia JSON a ser resguardados
                double nmllMET = Double.parseDouble(jsResultObj.get("val_met").toString().replaceAll("\"", ""));
                double nmllClo = Double.parseDouble(jsResultObj.get("val_clo").toString().replaceAll("\"", ""));
                double nmllBMI = Double.parseDouble(jsResultObj.get("val_bmi").toString().replaceAll("\"", ""));
                // --- >> ?val_persona ?val_numpriori ?val_sexo ?val_edad ?val_bmi ?val_met ?val_clo
                // --- >> ?val_pmv ?val_confortren ?val_congas
                // Obtener datos de instancia JSON a ser resguardados
                int nmllPrioridad = Integer.parseInt(jsResultObj.get("val_numpriori").toString().replaceAll("\"", ""));
                int nmllEdad = Integer.parseInt(jsResultObj.get("val_edad").toString().replaceAll("\"", ""));
                String nmllSexo = jsResultObj.get("val_sexo").toString().replaceAll("\"", "");
                double nmllConcentraGas = Double.parseDouble(jsResultObj.get("val_congas").toString().replaceAll("\"", ""));
                // Validar
                if (isOk) {
                    // Agregar resultado a diccionario
                    dicResu.put(strIDPerson,
                        new OntoData(Integer.parseInt(strIDPerson), nmllMET, nmllClo, nmllBMI, nmllPMV, nmllSexo, nmllEdad,
                            nmllPrioridad, nmllConcentraGas, vpTemp, slopeConfort, vectorTop, vectorBottom));
                    // Incrementar el número de resultados
                    noResults += 1;
                }
                
            }
        }
        // Regresar diccionario de resultados
        return dicResu;
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Función determinar cual par de valores forman al vector top
    public static VectorDeTende determinarVectorTop(double conOne, double tenOne, double conTwo, double tenTwo) {
        // Preparar vector de tendencia
        VectorDeTende vector = null;
        // Validar cual conjunto es el top
        if (conOne < conTwo) {
            // Formar vector de tendencia
            vector = new VectorDeTende(conOne, tenOne);
        } else {
            // Formar vector de tendencia
            vector = new VectorDeTende(conTwo, tenTwo);
        }
        // Devolver pendiente "m" calculada
        return vector;
    }
    
    // Función determinar cual par de valores forman al vector bottom
    public static VectorDeTende determinarVectorBottom(double conOne, double tenOne, double conTwo, double tenTwo) {
        // Preparar vector de tendencia
        VectorDeTende vector = null;
        // Validar cual conjunto es el top
        if (conOne < conTwo) {
            // Formar vector de tendencia
            vector = new VectorDeTende(conTwo, tenTwo);
        } else {
            // Formar vector de tendencia
            vector = new VectorDeTende(conOne, tenOne);
        }
        // Devolver pendiente "m" calculada
        return vector;
    }
    
    // Función que calcula la pendiente "m" de confort
    public static double calcularPendienteM(double conOne, double tenOne, double conTwo, double tenTwo) {
        // Variables
        double m = 0;
        // Determinar cual es el vector TOP y BOTTOM
        VectorDeTende vector1_Top = determinarVectorTop(conOne, tenOne, conTwo, tenTwo);
        VectorDeTende vector2_Bot = determinarVectorBottom(conOne, tenOne, conTwo, tenTwo);
        // TRY - CATCH
        try {
            // Calcular valor de "m" pendiente de confort
            m = ((vector2_Bot.tendency - vector1_Top.tendency)/(vector2_Bot.condition - vector1_Top.condition));
            // Validar por NaN
            if ((m + "").equals("NaN")) {
                // Asginar cero
                m = 0;
            }
        } catch (Exception e) {
            m = 0;
        }
        // Devolver pendiente "m" calculada
        return m;
    }
    
    // Función que calcula la tendencia de confort con base en una condición
    public static double calcularTendeciaM(double mSCT, double conBot, VectorDeTende vectorTop) {
        // Variables
        double tenBot = 0;
        // TRY - CATCH
        try {
            // Calcular el valor de la tendencia del confort
            tenBot = (mSCT * (conBot - vectorTop.condition)) + vectorTop.tendency;
        } catch (Exception e) {
            tenBot = 0;
        }
        // Devolver pendiente "m" calculada
        return tenBot;
    }
    
    // Función que determina si la tendencia esta en un rango aceptable
    /*
    public static boolean validarTEND(double tenTC) {
        // Variables
        double segment = (100/6); // 16.666666666666666666666666666667
        double rangMax = 3.5/segment; // en PMV es -0.5
        double rangMin = 2.5/segment; // en PMV es +0.5
        // Se valida si la tendencia esta en un rango aceptable de Confort
        // ** Donde: (-0.5 <= x) es "0"
        // ** Donde: (x < +0.5) es "0"
        return (rangMin < tenTC) && (tenTC <= rangMax);
    }
    */
    
    // Función que convertir la tendencia de Confort a una representación PMV
    public static double convertirTENDaPMV(double tenTC /* Supone un valor de -50 a +50 */) {
        // Variables
        double segment = (100/6); // 16.666666666666666666666666666667
        // Calcular valor PMV
        // * Se multiplica por "-1" porque se interpreta la tendencia al contrario del PMV
        // * Si quiero frio (de -50 a 0), es porque tengo calor
        // * Si quiero calor (de 0 a 50), es porque tengo frio
        return (tenTC/segment) * -1;
    }
    
    // Función que convertir la tendencia de Confort a una representación PMV
    public static double convertirPMVaTEND(double inPMV) {
        // Variables
        double segment = (100/6);
        // Calcular valor de tendencia
        return (inPMV/-1) * segment;
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // CALCULA VALORES DE CONFORT GRUPAL
    
    // --------------------------------- Si la politica es: ACTGU
    // Por estrategia: Determinar los valores de Confot Termico Grupal con base en los datos resultantes
    public Hashtable<String, ResultStrategyACTGU> determinarVlrsACTGUPorEstrategia() {
        // *******************************
        // Diccionario que contiene valores de Confort - PMV aceptable - definidos por estrategia
        Hashtable<String, ResultStrategyACTGU> dicResultsPorEstra =
                (new FabricanteDeConfort(inEstrategia, noResults, diccOntoDatas)).analizarVlrsACTGUPorEstrategia();
        // Regresar resultado
        return dicResultsPorEstra;
    }

    // --------------------------------- Si la politica es: ASHRAE
    // Por estrategia: Determinar los valores de Confot Termico Grupal con base en los datos resultantes
    public Hashtable<String, ResultStrategyASHRAE> determinarVlrsASHRAEPorEstrategia() {
        // *******************************
        // Diccionario que contiene valores de Confort - PMV aceptable - definidos por estrategia
        Hashtable<String, ResultStrategyASHRAE> dicResultsPorEstra =
                (new FabricanteDeConfort(inEstrategia, noResults, diccOntoDatas)).analizarVlrsASHRAEPorEstrategia();
        // Regresar resultado
        return dicResultsPorEstra;
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Variables de caculo de Confort Grupal
    private String ctgPMV = "";
    private String ctgConfort = "";
    private String ctgSensacion = "";
    // -- Para estrategias 1 a 5, con politica ASHRAE
    private ResultStrategyASHRAE resultStrategyASHRAE = null;
    // -- Para estrategias 1 a 5, con politica ACTGU
    private double tenConfortGrupal = 0;
    private ResultStrategyACTGU resultStrategyACTGU = null;
    
    
    // Por estrategia: Determinar Confort Térmico Grupal
    public void calcularConfortTermicoGrupal() {
        // Validar estrategia
        if (inEstrategia == 6) {
            // ----------------------------------------------------
            // ----------------------------------------------------
            // ----------------------------------------------------
            // Calculo de Confort Grupal ASHRAE 55
            // Variables
            double metprom = 0;
            double cloprom = 0;
            // RECORRER DATA RESULTANTE
            Enumeration enumllaves = diccOntoDatas.keys();
            while (enumllaves.hasMoreElements()) {
                // Recuperar datos en conjunto
                String strKey = enumllaves.nextElement().toString();
                OntoData rData = diccOntoDatas.get(strKey);
                // Sumar valores de confort del usuario
                metprom += rData.vMET;
                cloprom += rData.vCLO;
            }
            // Calcular promedio de los valores de confort
            metprom /= noResults;
            cloprom /= noResults;
            // Calcular Confort Térmico
            ConfortTermico objTC_ByProUsrs = new ConfortTermico(
                    valTemp /*Temperatura*/, valVAir /*Velocidad del Aire*/,
                    valHumd /*Humedad Relativa*/, metprom /*Valor MET*/, cloprom /*Valor CLO*/);
            PMV objPMV_ByProUsrs = objTC_ByProUsrs.calcularValorPMV();
            // 6: CONFORT TERMICO DEL PROMEDIO DEL MET Y CLO DE LOS USUARIOS
            ctgPMV = objPMV_ByProUsrs.getTCIndexAsStr();
            ctgConfort = objPMV_ByProUsrs.getConfortASHRAE55();
            ctgSensacion = objPMV_ByProUsrs.getSensacionASHRAE55();
            // ----------------------------------------------------
            // ----------------------------------------------------
            // ----------------------------------------------------
        } else {
            // ----------------------------------------------------
            // ----------------------------------------------------
            // ----------------------------------------------------
            //  Calculo de Confort Grupal por estrategias para GRUPOS
            // ***********************************
            // ***********************************
            // ***********************************
            // Validar la politica a ser utilizada
            // -- Si es ASHRAE
            if (strPolitica.equals("ASHRAE")) {
                // Determinar el PMV valido por estrategia de manejo de grupos
                Hashtable<String, ResultStrategyASHRAE> dicPorEstragASHRAE = determinarVlrsASHRAEPorEstrategia();
                // Recuperar el resultado de la estrategia aplicada
                switch (inEstrategia) {
                    case 5: // "MaxV" // Por el más popular (El más votado)
                        resultStrategyASHRAE = dicPorEstragASHRAE.get("MaxV");
                        break;
                    case 4: // "MayPriori" // Por el más respetado (El de mayor rango jerarquico)
                        resultStrategyASHRAE = dicPorEstragASHRAE.get("MayPriori");
                        break;
                    case 3: // "MenosM" // El menos miserable (El grupo con la preferencia más baja)
                        resultStrategyASHRAE = dicPorEstragASHRAE.get("MenosM");
                        break;
                    case 2: // "MayorP" // Al mayor placer (El grupo con la preferencia más alta)
                        resultStrategyASHRAE = dicPorEstragASHRAE.get("MayorP");
                        break;
                    case 1: // "Prome" // Promedio
                        resultStrategyASHRAE = dicPorEstragASHRAE.get("Prome");
                        break;
                }
            // ***********************************
            // -- Si es ACTGU
            } else {
                /// Determinar el PMV valido por estrategia de manejo de grupos
                Hashtable<String, ResultStrategyACTGU> dicPorEstragACTGU = determinarVlrsACTGUPorEstrategia();
                // Recuperar el resultado de la estrategia aplicada
                switch (inEstrategia) {
                    case 5: // "MaxV" // Por el más popular (El más votado)
                        resultStrategyACTGU = dicPorEstragACTGU.get("MaxV");
                        break;
                    case 4: // "MayPriori" // Por el más respetado (El de mayor rango jerarquico)
                        resultStrategyACTGU = dicPorEstragACTGU.get("MayPriori");
                        break;
                    case 3: // "MenosM" // El menos miserable (El grupo con la preferencia más baja)
                        resultStrategyACTGU = dicPorEstragACTGU.get("MenosM");
                        break;
                    case 2: // "MayorP" // Al mayor placer (El grupo con la preferencia más alta)
                        resultStrategyACTGU = dicPorEstragACTGU.get("MayorP");
                        break;
                    case 1: // "Prome" // Promedio
                        resultStrategyACTGU = dicPorEstragACTGU.get("Prome");
                        break;
                }
            }
            // ***********************************
            // ***********************************
            // ***********************************
            // Validar la politica a ser utilizada
            // -- Si es ASHRAE
            if (strPolitica.equals("ASHRAE")) {
                // Convertir tendencia en una representación PMV
                double dblGralPMV = resultStrategyASHRAE.pmvGrupal_dbl;
                // Convertir valor a cadena PMV
                ctgPMV = PMV.convertA55IndexToStr(dblGralPMV);
                // Validar estado de confort grupal: METODO ASHRAE NORMAL
                // if (Integer.parseInt(ctgPMV) == 0) { ctgConfort = "Comodo"; } else { ctgConfort = "Incomodo"; }
                // Validar estado de confort grupal: METODO ASHRAE EXTENDIDO
                if (resultStrategyASHRAE.isAceptByNormsASHRAE(valCGas)) {
                    ctgConfort = "Comodo";
                } else {
                    ctgConfort = "Incomodo";
                }
                ctgSensacion = PMV.convertA55Sensacion(dblGralPMV);
            // -- Si es ACTGU
            } else {
                // Calcular la tendencia de Confort Termico Grupal resultante (dando un valor entre 0 y 100)
                tenConfortGrupal = calcularTendeciaM(resultStrategyACTGU.pendiente, valTemp, resultStrategyACTGU.vectorTop);
                // Convertir tendencia en una representación PMV
                double dblGralPMV = convertirTENDaPMV(tenConfortGrupal);
                // Convertir valor a cadena PMV
                ctgPMV = PMV.convertA55IndexToStr(dblGralPMV);
                // Validar estado de confort grupal
                if (Integer.parseInt(ctgPMV) == 0) {
                    ctgConfort = "Comodo";
                } else {
                    ctgConfort = "Incomodo";
                }
                ctgSensacion = PMV.convertA55Sensacion(dblGralPMV);
            }
            // ----------------------------------------------------
            // ----------------------------------------------------
            // ----------------------------------------------------
        }
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Variables para resguardar las acciones a tomar
    private String strHVACAcciones = "";
    private String strConfigDescrp = "";
            
    // Función que determina las acciones a tomar con los actuadores
    public void determinarAccionesParaCTG() {
        // Solo si no es comodo, es necesario generar resultados
        // Crear inspector de Ontología
        InspectorONTO inspOnto = new InspectorONTO(instanciaJENA);
        // Recupera la información de los Medios HVAC
        JsonArray jsAry = inspOnto.recuperarDatos(InspectorONTO.tagActuadores);
        // Inicializar recomendación
        GeneAcciones geneAcciones = null;
        // Construir generación de acciones
        geneAcciones = new GeneAcciones(inEstrategia, strPolitica,
            resultStrategyASHRAE, resultStrategyACTGU,
            tenConfortGrupal, ctgPMV, ctgConfort,
            jsAry, valTemp, valVAir, valHumd, valCGas);
        // Construir recomendacion
        geneAcciones.construirRecomendacion();
        // >> Respuesta
        strHVACAcciones = geneAcciones.getAcciones();
        strConfigDescrp = geneAcciones.getConfiguracion();
        // Validar y enviar acciones
        if (!strHVACAcciones.equals("")) {
            // Actualizar disposición de actualización de HVACs
            analizarNuevaConfigDeHVACs(strHVACAcciones);
            // Informar sobre nueva configuración a HVACs
            PublicadorMQTT cliMQTT_HVAC = new PublicadorMQTT(strHVACAcciones);
            cliMQTT_HVAC.publicarConfiguracion();
        }
    }
    
    // Función que analiza y actualiza una disposición para actualizar el estado de HVACs
    private void analizarNuevaConfigDeHVACs(String strNwConfig) {
        // Dividir configuración
        String[] aryNwConfig = strNwConfig.split("\\|");
        JsonArray jsNwConfig = new JsonArray();
        // Recorrer nueva configuración
        for (String nwConff: aryNwConfig) {
            // Recuperar segmentos de configuración
            String[] arySegConfig = nwConff.split(",");
            String ideHVAC = "";
            String edoHVAC = "";
            String val_Temp = "";
            String val_Hume = "";
            String val_VAir = "";
            String val_CGas = "";
            // Recorrer segmentos
            for (String segConff: arySegConfig) {
                // Dividir segmentos en sus partes
                String[] aryPartData = segConff.split(":");
                // Validar partes del segmento
                if (aryPartData.length > 1) {
                    // Validar que sea uno de los datos buscado
                    if (aryPartData[0].trim().equals("id")) {
                        // Asignar valor de parte
                        ideHVAC = aryPartData[1].trim();
                    } else if (aryPartData[0].trim().equals("edo")) {
                        // Asignar valor de parte
                        edoHVAC = aryPartData[1].trim();
                    } else if (aryPartData[0].trim().equals("temp")) {
                        // Asignar valor de parte
                        val_Temp = aryPartData[1].trim();
                    } else if (aryPartData[0].trim().equals("aire")) {
                        // Asignar valor de parte
                        val_VAir = aryPartData[1].trim();
                    } else if (aryPartData[0].trim().equals("hum")) {
                        // Asignar valor de parte
                        val_Hume = aryPartData[1].trim();
                    } else if (aryPartData[0].trim().equals("gas")) {
                        // Asignar valor de parte
                        val_CGas = aryPartData[1].trim();
                    }
                }
            }
            // Crear Objeto Json Temporal
            JsonObject jsObjTemp = new JsonObject();
            // Asignar valores a objeto JSON
            jsObjTemp.addProperty("val_actuador", ideHVAC);
            jsObjTemp.addProperty("val_estado", edoHVAC);
            
            jsObjTemp.addProperty("val_temperatura", val_Temp);
            jsObjTemp.addProperty("val_humedad", val_Hume);
            jsObjTemp.addProperty("val_vel_aire", val_VAir);
            jsObjTemp.addProperty("val_con_gas", val_CGas);
            // Asignar Objeto JSON a Arreglo JSON
            jsNwConfig.add(jsObjTemp);
        }
        // Modificar configurar global de HVACs
        Principal.jsAryConfigAct = jsNwConfig;
    }
    
    // Funcion que genera un STRING con los resultados
    public String generarResultadoGeneral() {
        // Variables
        String strEdoUsrs = "";
        String strCondGral = "";
        String strConfigRem = "";
        // Iniciar JSON de usuarios
        strEdoUsrs = "\"usuarios\":[ ";
        // Recorrer diccionario de resultados
        Enumeration enumllaves = diccOntoDatas.keys();
        // Validar la politica a ser utilizada
        // -- Si es ASHRAE
        if (strPolitica.equals("ASHRAE")) {
            // RECORRER DATA RESULTANTE
            while (enumllaves.hasMoreElements()) {
                // Se recuperan datos de un diccionario de datos ontologicos
                String strKey = enumllaves.nextElement().toString();
                OntoData rData = diccOntoDatas.get(strKey);
                // Armar JSON con data de usuarios
                strEdoUsrs += "{";
                // Resultados de Confort Térmico
                strEdoUsrs += "\"pmv\":\"" + rData.getConfortASHRAE() + "\",";
                strEdoUsrs += "\"confort\":\"" + rData.getEstadoASHRAE(inEstrategia) + "\",";
                strEdoUsrs += "\"sensacion\":\"" + rData.getSensacionASHRAE(inEstrategia) + "\",";
                // ID DE USUARIO
                strEdoUsrs += "\"id_usuario\":\"" + strKey + "\"";
                strEdoUsrs += "},";
            }
        // -- Si es ACTGU
        } else {
            // RECORRER DATA RESULTANTE
            while (enumllaves.hasMoreElements()) {
                // Se recuperan datos de un diccionario de datos ontologicos
                String strKey = enumllaves.nextElement().toString();
                OntoData rData = diccOntoDatas.get(strKey);
                // Armar JSON con data de usuarios
                strEdoUsrs += "{";
                // Resultados de Confort Térmico
                strEdoUsrs += "\"pmv\":\"" + rData.getConfortACTGU() + "\",";
                strEdoUsrs += "\"confort\":\"" + rData.getEstadoACTGU() + "\",";
                strEdoUsrs += "\"sensacion\":\"" + rData.getSensacionACTGU() + "\",";
                // ID DE USUARIO
                strEdoUsrs += "\"id_usuario\":\"" + strKey + "\"";
                strEdoUsrs += "},";
            }
        }
        
        // Ajustar Data StrJSON
        if (!strEdoUsrs.equals("")) {
            // Remover la última coma de la cadena
            strEdoUsrs = strEdoUsrs.substring(0, strEdoUsrs.length() - 1);
        }
        strEdoUsrs += " ]";
        // Validar si se indica una estrategia
        if (!strConfigDescrp.equals("")) {
            // Asignar configuración y descripción
            strConfigRem = strConfigDescrp;
        } else {
            // Asignar una representación de cadena vacia
            strConfigRem = "\"\"";
        }
        // Indicar condiciones revisadas
        // "\"ambiente_revisado\":\" temperatura de " + valTemp + "°C, humedad de " + valHumd + "%, velocidad del aire de " + valVAir + " m/s y concentración de gas de  " + valCGas + " ppm.\",";
        strCondGral += "\"condiciones_enfrentadas\":{";
        strCondGral += "\"" + InspectorONTO.cmpTempera + "\":\"" + valTemp + "\",";
        strCondGral += "\"" + InspectorONTO.cmpHumedad + "\":\"" + valHumd + "\",";
        strCondGral += "\"" + InspectorONTO.cmpVelAire + "\":\"" + valVAir + "\",";
        strCondGral += "\"" + InspectorONTO.cmpConcGas + "\":\"" + valCGas + "\"";
        strCondGral += "},";
        // Armar JSON con data de Confort Grupal
        strCondGral += "\"confort_grupal\":{";
        strCondGral += "\"valor_pmv\":\"" + ctgPMV + "\",";
        strCondGral += "\"valor_confort\":\"" + ctgConfort + "\",";
        strCondGral += "\"valor_sensacion\":\"" + ctgSensacion + "\",";
        strCondGral += "\"actgu_configuracion\":" + strConfigRem + "";
        strCondGral += "}";
        // Regresar resultado
        return strEdoUsrs + "," + strCondGral;
    }
    
}
