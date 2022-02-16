/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.ontologia.madegu;

import javaactg.Principal;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javaactg.ontologia.InspectorONTO;
import javaactg.ontologia.madegu.utiles.ResultStrategyACTGU;
import javaactg.ontologia.madegu.utiles.ResultStrategyASHRAE;

/**
 *
 * @author Jorge
 */
public class GeneAcciones {
    
    // Variables con resultados
    private boolean usarSoloASHRAE = false;
    
    // Información de Confort Grupal Resultante
    private int inPMV = 0;
    private String ctgPMV = "";
    private String ctgConfort = "";
    private String ctgSensacion = "";
    
    // Variables para la entrega de ACCIONES
    private String strAcciones = "";
    private String strConfiguracion = "";
    private double tendenciaConfortGrupal = 0.0;
    
    // Variable para información de HVACs
    private JsonArray jsAryHvac = null;
    
    // Valores ambientales originales
    private double orVelAir = 0;
    private double orTemp = 0;
    private double orHume = 0;
    private double orCGas = 0;
    
    // Valores ambientales mejorados
    private double nwTemp = 0;
    private double nwCGas = 0;
    
    // Variable a ser calculadas
    private double nwHumCAL = 0;
    private double nwVAiCAL = 0;
    
    // Constructor - Si la politica es ACTGU
    public GeneAcciones(int estra, String politi,
            ResultStrategyASHRAE resuStraASHRAE, ResultStrategyACTGU resuStraACTGU,
            double tenConfortGrupal, String strPmv, String confort, JsonArray jsAry,
            double valTemp, double valVAir, double valHumd, double valCGas) {
        // ***************************************************************
        // ***************************************************************
        // Variables
        // Asignaciones de CG resultante
        ctgPMV = strPmv;
        ctgConfort = confort;
        inPMV = Integer.parseInt(ctgPMV);
        
        /// Asignación data sobre los HVACs
        jsAryHvac = jsAry;
        
        // Asignar valores ambientales
        orTemp = valTemp;
        orHume = valHumd;
        orVelAir = valVAir;
        orCGas = valCGas;
        
        // Asignar valor de mejora igual al original
        nwTemp = valTemp;
        nwHumCAL = valHumd;
        nwVAiCAL = valVAir;
        nwCGas = valCGas;
        
        // Validar estrategia
        if (estra == 6) {
            // Indica el uso de ASHRAE
            usarSoloASHRAE = true;
        } else {
            // Validar la politica a ser utilizada
            // -- Si es ASHRAE
            if (politi.equals("ASHRAE")) {
                // Indica el uso de ASHRAE
                usarSoloASHRAE = true;
            // -- Si es ACTGU
            } else {
                // Indica el uso de ACTGU
                usarSoloASHRAE = false;
            }
        }
        
        // Si se usa ASHRAE
        if (usarSoloASHRAE) {
            // Asignación de Ontologia resultante
            // >> Implica: Calcular valor Tendencia a partir de PMV
            // >> NOTA: Si (inPMV > 0) entonces (tenTC < 0); pero Si (inPMV < 0) entonces (tenTC > 0)
            tendenciaConfortGrupal = ControladorConfort.convertirPMVaTEND((double) inPMV);
            // Si originalmente era comodo
            if (!ctgConfort.toLowerCase().equals("comodo")) {
                // Modificar condiciones ambientales
                // Validar si se tiene frio
                if (inPMV < 0) {
                    // Incrementar temperatura en 1 grado
                    nwTemp += 1;
                // Validar si se tiene calor
                } else if (inPMV > 0) {
                    // Disminuir temperatura en 1 grado
                    nwTemp -= 1;
                }
                // Validar si cambiar cocentracion de gas
                if (orCGas >= 1000) {
                    // Modificar cocentracion de gas deseable
                    nwCGas = 300;
                }
            }
        // Sino, si se se usa ACTGU
        } else {
            // Asignación de Ontologia resultante
            tendenciaConfortGrupal = tenConfortGrupal;
            // Si originalmente era comodo
            if (!ctgConfort.toLowerCase().equals("comodo")) {
                // Ajusta valor de aceptabilidad de tendencias
                double tendencia_min_en_accion = 0 - Principal.rango_de_en_accion;
                double tendencia_max_en_accion = 0 + Principal.rango_de_en_accion;
                // Datos de validación
                double condicion = 0;
                double tendencia = 0;
                boolean contAceptable = false;
                // Validar si se encontro mejores condiciones
                if (!contAceptable) {
                    // Recorrer valores seleccionados DE 10°C a 40°C
                    for (double xcn = 10; xcn < 40; xcn++) {
                        // Determina el valor de la condicion
                        condicion = xcn;
                        // Calcular tendencia de confort termico personal
                        tendencia = ControladorConfort.calcularTendeciaM(resuStraACTGU.pendiente, condicion, resuStraACTGU.vectorTop);
                        // Contar si es o no aceptado de forma normal (Preferencias y Reglas)
                        if ((tendencia_min_en_accion <= tendencia) && (tendencia <= tendencia_max_en_accion)) {
                            contAceptable = true;
                            break;
                        }
                    }
                }
                // Validar si se encontro una condicion aceptable
                if (contAceptable) {
                    // Se Asigna un valor de condicion aceptable
                    nwTemp = condicion; 
                } else {
                    // Modificar condiciones ambientales
                    if (tenConfortGrupal > 0) {
                        // Incrementar temperatura en 1 grado
                        nwTemp += 1;
                    } else if (tenConfortGrupal < 0) {
                        // Disminuir temperatura en 1 grado
                        nwTemp -= 1;
                    }
                }
                // Validar si cambiar cocentracion de gas
                if (orCGas >= 1000) {
                    // Modificar cocentracion de gas deseable
                    nwCGas = 300;
                }
            }
        }
    }
    
    // Devuelve la configuración de confort calculada
    public String getAcciones() {
        // Devolver valor
        return strAcciones;
    }
    
    // Devuelve el JSON de recomendación, como un STRING
    public String getConfiguracion() {
        // Devolver valor
        return strConfiguracion;
    }
    
    // Genera una recomendación basada en la última ontología generada
    //      > Para esto, consulta los elementos HVACs de la onología base
    public void construirRecomendacion () {
        // >> Banderas
        boolean usandoAireAcondi = false;
        boolean usandoCalentador = false;
        boolean usandoVentilador = false;
        boolean usandoPuerta = false;
        boolean usandoVentan = false;
        // **********************
        // **********************
        // >>>>>>>>>>>> Respuesta
        String strResp = "{";
        // **********************
        // Escribir configuración
        strAcciones = "";
        // **********************
        // **********************
        // **********************
        // Recuperar número de HVACs
        int numAryHvac = jsAryHvac.size();
        JsonObject jsObjHvac = null;
        // **********************
        // **********************
        // Recorrer Elementos HVAC
        for (int i = 0; i < numAryHvac; i++) {
            // Recuperar instancia
            jsObjHvac = jsAryHvac.get(i).getAsJsonObject();
            // Construir cadena de configuración
            String[] aryAccToHVAC = makeConfigOfMecanismo(jsObjHvac.get("id_mecanismo_hvac").toString().replaceAll("\"", "").toLowerCase(),
                    jsObjHvac.get("tipo_de_mecanismo").toString().replaceAll("\"", "").toLowerCase(),
                    jsObjHvac.get("estado").toString().replaceAll("\"", "").toLowerCase())
                    .split("~");
            // Si el elemento esta encendido >> a = Apagado, e = Encendido
            if (aryAccToHVAC[0].equals("e")) {
                // Configurar parametros
                switch (aryAccToHVAC[1]) {
                    // TIPOS DE HVACs
                    case "aire acondicionado":
                        // Indicar que se usa el elemento
                        usandoAireAcondi = true;
                        break;
                    case "calefactor":
                        // Indicar que se usa el elemento
                        usandoCalentador = true;
                        break;
                    case "ventilador":
                        // Indicar que se usa el elemento
                        usandoVentilador = true;
                        break;
                    case "ventanas":
                        // Indicar que se usa el elemento
                        usandoVentan = true;
                        break;
                    case "puerta":
                        // Indicar que se usa el elemento
                        usandoPuerta = true;
                        break;
                }
            } else {
                // Configurar parametros
                switch (aryAccToHVAC[1]) {
                    // TIPOS DE HVACs
                    case "aire acondicionado":
                        // Indicar que se usa el elemento
                        usandoAireAcondi = false;
                        break;
                    case "calefactor":
                        // Indicar que se usa el elemento
                        usandoCalentador = false;
                        break;
                    case "ventilador":
                        // Indicar que se usa el elemento
                        usandoVentilador = false;
                        break;
                    case "ventanas":
                        // Indicar que se usa el elemento
                        usandoVentan = false;
                        break;
                    case "puerta":
                        // Indicar que se usa el elemento
                        usandoPuerta = false;
                        break;
                }
            }
            // Armar string de acciones
            strAcciones += aryAccToHVAC[2]  + "|";
        }
        // Validar contenido de cadena
        if (!strAcciones.equals("")) {
            // Ajuste de cadena
            strAcciones = strAcciones.substring(0, strAcciones.length() - 1);
        }
        // **********************
        strResp += makeTextResult(usandoAireAcondi, usandoCalentador, usandoVentilador, usandoPuerta, usandoVentan);
        // **********************
        // **********************
        // **********************
        // Cerrar respuesta
        strResp += "}";
        // Asignar resultado
        strConfiguracion = strResp;
    }
    
    // Función que define la configuración del mecanismo HVAC
    public String makeConfigOfMecanismo (String idActuador, String tipoAct, String edoAct) {
        // Variables internas de ambiente
        double nwVeAiIntera = orVelAir;
        double nwHumIntera = orHume;
        // ----------------------------
        // ----------------------------
        // >> Valida si el ambiente no es comodo
        if (!ctgConfort.toLowerCase().equals("comodo")) {
            // SI la temperatura incremento
            if (orTemp < nwTemp) { // TEMPERATURA: INCREMENTA
                // Calcular diferencia
                double diffTem = nwTemp - orTemp;
                // Validar tipo a revisar
                // CALEFACTOR
                if (tipoAct.equals("calefactor")) {
                    // ENCENDER
                    edoAct = "e";
                    // Asigna valores por default
                    nwVeAiIntera = 0.5;
                    nwVAiCAL = nwVeAiIntera;
                    // Valida si reducir la humedad
                    if (nwHumIntera >= 10) {
                        // Validar como reducir la humedad
                        if ((nwHumIntera - (diffTem * 0.75)) >= 10) {
                            // Calcular nueva humedad
                            nwHumIntera = nwHumIntera - (diffTem * 0.75);
                            nwHumCAL = nwHumIntera;
                        } else {
                            // Calcular nueva humedad
                            nwHumIntera = 10;
                            nwHumCAL = 10;
                        }
                    } else {
                        // Calcular nueva humedad
                        nwHumIntera = orHume;
                        nwHumCAL = orHume;
                    }
                // AIRE ACONDICIONADO || VENTILADOR || PUERTA || VENTANAS
                } else {
                    // APAGAR
                    edoAct = "a";
                    // Asigna valores por default
                    nwVeAiIntera = 0;
                    nwHumIntera = 0;
                }
            // ----------------------------
            // ----------------------------
            // SI la temperatura disminuyo
            } else if (orTemp > nwTemp) { // TEMPERATURA: DISMINUYE
                // Calcular diferencia
                double diffTem = orTemp - nwTemp;
                // Validar tipo a revisar
                // AIRE ACONDICIONADO
                if (tipoAct.equals("aire acondicionado")) {
                    // ENCENDER
                    edoAct = "e";
                    // Asigna valores por default
                    nwVeAiIntera = 1.5;
                    nwVAiCAL = nwVeAiIntera;
                    // Calcular nueva humedad
                    nwHumIntera = nwHumIntera + (diffTem * 1.75);
                    nwHumCAL = nwHumIntera;
                // CALEFACTOR || VENTILADOR || PUERTA || VENTANAS
                } else {
                    // APAGAR
                    edoAct = "a";
                    // Asigna valores por default
                    nwVeAiIntera = 0;
                    nwHumIntera = 0;
                }
            // ----------------------------
            // ----------------------------
            // SINO, la temperatura es igual (Original y Nueva) 
            } else {
                // SI la temperatura no cambia
                if (orTemp == nwTemp) { // TEMPERATURA: NO CAMBIA
                    // APAGAR
                    edoAct = "a";
                    // Asigna valores por default
                    nwVeAiIntera = 0;
                    nwHumIntera = 0;
                } else {
                    // *********
                    // *********
                    // SI (Se tiene frio) || (Se tiende a MAYORES CONDICIONES)
                    if ((usarSoloASHRAE && (inPMV < 0)) ||
                       (!usarSoloASHRAE && (tendenciaConfortGrupal > 0))) {
                        // Si se incrementa temperatura en 1 grado
                        if (tipoAct.equals("calefactor")) {
                            // ENCENDER
                            edoAct = "e";
                            // Asigna valores por default
                            nwVeAiIntera = 0.5;
                            nwVAiCAL = nwVeAiIntera;
                            // Valida si reducir la humedad
                            if (nwHumIntera >= 10) {
                                // Validar como reducir la humedad
                                if ((nwHumIntera - 1.75) >= 10) {
                                    // Calcular nueva humedad
                                    nwHumIntera = nwHumIntera - 1.75;
                                    nwHumCAL = nwHumIntera;
                                } else {
                                    // Calcular nueva humedad
                                    nwHumIntera = 10;
                                    nwHumCAL = 10;
                                }
                            } else {
                                // Calcular nueva humedad
                                nwHumIntera = orHume;
                                nwHumCAL = orHume;
                            }
                        // AIRE ACONDICIONADO || VENTILADOR || PUERTA || VENTANAS
                        } else {
                            // APAGAR
                            edoAct = "a";
                            // Asigna valores por default
                            nwVeAiIntera = 0;
                            nwHumIntera = 0;
                        }
                    // *********
                    // *********
                    // SI (Se tiene calor) || (Se tiende a MENOR CONDICIONES)
                    } else if ((usarSoloASHRAE && (inPMV > 0)) ||
                              (!usarSoloASHRAE && (tendenciaConfortGrupal < 0))) {
                        // Si se disminuye temperatura en 1 grado
                        // double tendIndexAjuste = 50/3;
                        // AIRE ACONDICIONADO || CALEFACTOR
                        if (tipoAct.equals("aire acondicionado") || tipoAct.equals("calefactor")) {
                            // APAGAR
                            edoAct = "a";
                            // Asigna valores por default
                            nwVeAiIntera = 0;
                            nwHumIntera = 0;
                        // VENTILADOR --------------------------
                        } else  if (tipoAct.equals("ventilador")) {
                            // APAGAR
                            edoAct = "a";
                            // Asigna valores por default
                            nwVeAiIntera = 0;
                            nwHumIntera = 0;
                            /*
                            // Elegir acción a tomar
                            if (tendenciaConfortGrupal <= tendIndexAjuste) {
                                // ENCENDER
                                edoAct = "e";
                                // Asigna valores por default
                                nwVeAiIntera = 3.0;
                                nwVAiCAL = nwVeAiIntera;
                                // Calcular nueva humedad
                                nwHumIntera = orHume;
                                nwHumCAL = orHume;
                            } else {
                                // APAGAR
                                edoAct = "a";
                                // Asigna valores por default
                                nwVeAiIntera = 0;
                                nwHumIntera = 0;
                            }
                            */
                        // PUERTA --------------------------
                        } else if (tipoAct.equals("puerta")) {
                            // APAGAR
                            edoAct = "a";
                            // Asigna valores por default
                            nwVeAiIntera = 0;
                            nwHumIntera = 0;
                            /*
                            // Elegir acción a tomar
                            if ((tendIndexAjuste < tendenciaConfortGrupal) && (tendenciaConfortGrupal <= (tendIndexAjuste * 2))) {
                                // ENCENDER
                                edoAct = "e";
                                // Asigna valores por default
                                nwVeAiIntera = 0.25;
                                nwVAiCAL = nwVeAiIntera;
                                // Calcular nueva humedad
                                nwHumIntera = orHume;
                                nwHumCAL = orHume;
                            } else {
                                // APAGAR
                                edoAct = "a";
                                // Asigna valores por default
                                nwVeAiIntera = 0;
                                nwHumIntera = 0;
                            }
                            */
                        // VENTANAS --------------------------
                        } else {
                            // APAGAR
                            edoAct = "a";
                            // Asigna valores por default
                            nwVeAiIntera = 0;
                            nwHumIntera = 0;
                            /*
                            // Elegir acción a tomar
                            if (((tendIndexAjuste * 2) < tendenciaConfortGrupal) && (tendenciaConfortGrupal <= (tendIndexAjuste * 3))) {
                                // ENCENDER
                                edoAct = "e";
                                // Asigna valores por default
                                nwVeAiIntera = 0.2;
                                nwVAiCAL = nwVeAiIntera;
                                // Calcular nueva humedad
                                nwHumIntera = orHume;
                                nwHumCAL = orHume;
                            } else {
                                // APAGAR
                                edoAct = "a";
                                // Asigna valores por default
                                nwVeAiIntera = 0;
                                nwHumIntera = 0;
                            }
                            */
                        }
                    // *********
                    // *********
                    } else {
                        // APAGAR
                        edoAct = "a";
                        // Asigna valores por default
                        nwVeAiIntera = 0;
                        nwHumIntera = 0;
                        /*
                        // Validar ajustes por diferencias en la concentración de gas
                        if ((orCGas - nwCGas) != 0) {
                            // AIRE ACONDICIONADO || CALEFACTOR || VENTILADOR || PUERTA
                            if (tipoAct.equals("aire acondicionado") || tipoAct.equals("calefactor") || tipoAct.equals("ventilador") || tipoAct.equals("puerta")) {
                                // APAGAR
                                edoAct = "a";
                                // Asigna valores por default
                                nwVeAiIntera = 0;
                                nwHumIntera = 0;
                            // VENTANAS
                            } else {
                                // ENCENDER
                                edoAct = "e";
                                // Asigna valores por default
                                nwVeAiIntera = 0.2;
                                nwVAiCAL = nwVeAiIntera;
                                // Calcular nueva humedad
                                nwHumIntera = orHume;
                                nwHumCAL = orHume;
                            }
                        } else {
                            // APAGAR
                            edoAct = "a";
                            // Asigna valores por default
                            nwVeAiIntera = 0;
                            nwHumIntera = 0;
                        }
                        */
                    }
                }
            }
        } else {
            // APAGAR
            edoAct = "a";
            // Asigna valores por default
            nwVeAiIntera = 0;
            nwHumIntera = 0;
            // y se conservan los valores ambientales originales
        }
        // Construir y devolver cadena de configuración
        return edoAct + "~" + tipoAct + "~" + "id:" + idActuador + ",edo:" + edoAct + ",tipo:" + tipoAct +
                ",temp:" + nwTemp + ",aire:" + nwVeAiIntera + ",hum:" + nwHumIntera + ",gas:" + nwCGas;
    }
    
    // Construir/Generar recomendacion descriptiva
    public String makeTextResult(boolean usandoAireAcondi, boolean usandoCalentador, boolean usandoVentilador, boolean usandoPuerta, boolean usandoVentan) {
        // Variables
        boolean uboCamb = false;
        // Recupera diferencia entre los valores ambientales
        double dfCGas = orCGas - nwCGas;
        // *******************************
        // *******************************
        // *******************************
        // 1.-
        // >> Variables de leyenda de recomendación
        String strResp = "\"acciones\":\"" + strAcciones.replaceAll("\\|", "*") + "\",";
        // *******************************
        // *******************************
        // *******************************
        // 2.-
        // >> Sumar confort detectado a leyenda
        strResp += "\"descripcion\":\"";
        // >> Valida si el ambiente es comodo
        if (ctgConfort.toLowerCase().equals("comodo")) {
            // --------------------------------------------
            strResp += "El Confort Grupal describe una sensación ";
            if (inPMV == 0) {
                strResp += "neutral";
            } else if (inPMV < 0) {
                strResp += "de frío";
            } else if (inPMV > 0) {
                strResp += "de calor";
            }
            strResp += " y resulta favorable para los usuarios";
            // --------------------------------------------
        } else {
            // --------------------------------------------
            strResp += "El Confort Grupal describe una sensación ";
            if (inPMV == 0) {
                strResp += "neutral";
            } else if (inPMV < -2) {
                strResp += "de mucho frío";
            } else if (inPMV < -1) {
                strResp += "de frío";
            } else if (inPMV < 0) {
                strResp += "de un poco de frío";
            } else if (inPMV > 2) {
                strResp += "de mucho calor";
            } else if (inPMV > 1) {
                strResp += "de calor";
            } else if (inPMV > 0) {
                strResp += "de un poco de calor";
            }
            strResp += " y no resulta favorable para los usuarios";
            // --------------------------------------------
        }
        strResp += ".\",";
        // *******************************
        // *******************************
        // *******************************
        // 3.-
        // Iniciar leyenda de recomendación
        strResp += "\"decision\":\"";
        // Si originalmente era comodo
        if (ctgConfort.toLowerCase().equals("comodo")) {
        // ---------------------------------------------------
            strResp += "Las condiciones actuales del ambiente ofrecen un estado de Confort Térmico aceptable para el grupo";
            uboCamb = false;
        // ---------------------------------------------------
        // Si es originalmente incomodo
        } else {
            // Variables de apoyo
            boolean addY = false;
            uboCamb = true;
            // A.-
            // Escribir leyenda
            strResp += "Las condiciones actuales del ambiente no ofrecen un estado de Confort Térmico aceptable para el grupo. ";
            // B.-
            // Indicar condiciones buscadas
            strResp += "No obstante, se determinó que una ";
            strResp += "temperatura de " + nwTemp + "°C, humedad de " + nwHumCAL + "%, velocidad del aire de " + nwVAiCAL + " m/s y concentración de gas de " + nwCGas + " ppm, ";
            strResp += "permitirá generar un mejor estado de Confort Grupal. ";
            // C.-
            // ** Extraer estado de configuración
            String[] aryDataStr = strAcciones.split("\\|");
            // ** Preparar recorrido de JSON-ARRAY de HVACs
            int tamJsonAry = aryDataStr.length;
            String strComplento = "";
            // ** Recorrer JSON-ARRAY
            for (int x = 0; x < tamJsonAry; x ++) {
                // Extraer data
                String[] arySubDat = aryDataStr[x].split(",");
                String strTipoElement = "";
                String strEdoElement = "";
                // Recorrer subdata
                int tamDataAry = arySubDat.length;
                // Recorrer JSON-ARRAY
                for (int w = 0; w < tamDataAry; w ++) {
                    // Campos
                    String[] aryFields = arySubDat[w].split(":");
                    // Validar tipo
                    switch (aryFields[0]) {
                        case "tipo":
                            strTipoElement = aryFields[1];
                            break;
                        case "edo":
                            strEdoElement = aryFields[1];
                            break;
                    }
                }
                // Validar datas
                if (!strTipoElement.equals("") && !strEdoElement.equals("")) {
                    // Ajuste de estado
                    switch (strTipoElement) {
                        case "aire acondicionado":
                            if (strEdoElement.equals("e")) {
                                strEdoElement = "encendido";
                            } else {
                                strEdoElement = "apagado";
                            }
                            break;
                        case "calefactor":
                            if (strEdoElement.equals("e")) {
                                strEdoElement = "encendido";
                            } else {
                                strEdoElement = "apagado";
                            }
                            break;
                        case "ventilador":
                            if (strEdoElement.equals("e")) {
                                strEdoElement = "encendido";
                            } else {
                                strEdoElement = "apagado";
                            }
                            break;
                        case "ventana": case "ventanas":
                            if (strEdoElement.equals("e")) {
                                strEdoElement = "abiertas";
                            } else {
                                strEdoElement = "cerradas";
                            }
                            break;
                        case "puerta":
                            if (strEdoElement.equals("e")) {
                                strEdoElement = "abierta";
                            } else {
                                strEdoElement = "cerrada";
                            }
                            break;
                    }
                    // Concatenar data
                    strComplento += " " + strTipoElement + " " + strEdoElement;
                    // Validar coma
                    if ((x + 2) < tamJsonAry) {
                        strComplento += ",";
                    } else if ((x + 1) < tamJsonAry) {
                        strComplento += " y ";
                    }
                }
            }
            // Validar complemento
            if (!strComplento.equals("")) {
                // Complemento
                strResp += "Así que, se instruyó a los actuadores adoptar la siguiente configuración: " + strComplento + ". ";
            }
            
            // Validar si sumar a leyenda
            if ((usandoAireAcondi || usandoCalentador || usandoVentilador) ||
                (usandoPuerta || usandoVentan || ((dfCGas > 0) || (orCGas >= 1000)))) {
                // Sumar a leyenda
                strResp += "De manera que, fuera posible ";
                // Indicar objetivo de la configuración
                if (usandoAireAcondi) {
                    strResp += "generar frio ";
                    addY = true;
                } else if (usandoCalentador) {
                    strResp += "generar calor ";
                    addY = true;
                } else if (usandoVentilador) {
                    if (orVelAir < nwVAiCAL) {
                        strResp += "aumentar la sensación de frescura ";
                    } else if (orVelAir > nwVAiCAL) {
                        strResp += "reducir la sensación de frescura ";
                    } else {
                        strResp += "generar una sensación de frescura ";
                    }
                    addY = true;
                }
                // Sumar a leyenda
                if (usandoPuerta || usandoVentan || ((dfCGas > 0) || (orCGas >= 1000))) {
                    if (addY) {
                        strResp += "y ";
                    }
                    strResp += "mejorar la ventilación ";
                }
                // Sumar a leyenda
                strResp += "en el ambiente y conseguir condiciones favorables para el grupo";
            }
        }
        // Cerrar leyenda de recomendación
        strResp += ".\",";
        // *******************************
        // *******************************
        // *******************************
        // 4.-
        // Iniciar armado de indicaciones
        strResp += "\"condiciones_buscadas\":{";
        // Validar si se cambiaron las condiciones
        if (uboCamb) {
            // Armar indicaciones
            strResp += "\"cambio\":\"SI\",";
        } else {
            // Armar indicaciones
            strResp += "\"cambio\":\"NO\",";
        }
        // Armar indicaciones
        strResp += "\"" + InspectorONTO.cmpTempera + "\":\"" + nwTemp + "\",";
        strResp += "\"" + InspectorONTO.cmpHumedad + "\":\"" + nwHumCAL + "\",";
        strResp += "\"" + InspectorONTO.cmpConcGas + "\":\"" + nwCGas + "\",";
        strResp += "\"" + InspectorONTO.cmpVelAire + "\":\"" + nwVAiCAL + "\"";
        // Terminar armado de indicaciones
        strResp += "}";
        // Devolver recomendacion
        return strResp;
    }
}
                









