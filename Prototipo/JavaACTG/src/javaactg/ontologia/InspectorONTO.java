/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.ontologia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javaactg.ontologia.modelo.InstanciaJENA;
import javaactg.ontologia.madegu.ControladorConfort;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.util.Map;
import java.util.Set;
import javaactg.ontologia.madegu.utiles.VectorDeTende;

/**
 *
 * @author Jorge
 */
public class InspectorONTO {
    
    // TAGs para consulta de tablas de BD y la ONTOLOGÍA
    public static final String tagBasePersonas = "BaseUsuarios";
    public static final String tagIdActuadores = "IdMecanismosHVAC";
    public static final String tagActuadores = "MecanismosHVAC";
    public static final String tagPersonas = "Usuarios";
    public static final String tagGrpsUsrs = "GrpsUsrs";
    public static final String tagGrupos = "Grupos";
    public static final String tagRoles = "Roles";
    public static final String tagTiposActuadores = "TiposHVAC";
    public static final String tagTiposEquipamientos = "TiposEquipamientos";
    
    // TAGs con campos base
    public static final String cmpTempera = "temperatura";
    public static final String cmpHumedad = "humedad";
    public static final String cmpVelAire = "velocidad_del_aire";
    public static final String cmpConcGas = "concentracion_de_gas";
    
    // Intancia de JENA (La Ontologia)
    private InstanciaJENA instanciaJENA = null;
    
    // Constructor
    public InspectorONTO(InstanciaJENA instaJENA){;
        // Asignar instancia de JENA
        instanciaJENA = instaJENA;
    }
    
    // Función que recupera de la Ontología, la información sobre el ambiente
    public String recuperarDatosAmbientales () {
        // Variables para formar una respuesta con datos ambientales
        Boolean delUltCom = false;
        String strResp = "";
        // Variables para datos sobre el ambiente
        double valTemp = 0.0;
        double valHumd = 0.0;
        double valCGas = 0.0;
        double valVAir = 0.0;
        // Recuperar datos sobre el ambiente
        JsonArray jsAry = getObservationsValueEnOnto().getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
        int tamJsonAry = jsAry.size();
        JsonObject jsObj = null;
        // Recorrer JSON-ARRAY
        for (int x = 0; x < tamJsonAry; x ++) {
            // Crear instancia de objeto JSON OBJECT
            jsObj = jsAry.get(x).getAsJsonObject();
            // Recuperar ID del registro
            valTemp = Double.parseDouble(jsObj.getAsJsonObject("val_temperatura").get("value").toString().replaceAll("\"", ""));
            valHumd = Double.parseDouble(jsObj.getAsJsonObject("val_humedad").get("value").toString().replaceAll("\"", ""));
            valCGas = Double.parseDouble(jsObj.getAsJsonObject("val_congas").get("value").toString().replaceAll("\"", ""));
            valVAir = Double.parseDouble(jsObj.getAsJsonObject("val_velaire").get("value").toString().replaceAll("\"", ""));
        }
        // Armar respuesta con datos sobre el ambiente
        strResp += "\"" + cmpTempera + "\":\"" + valTemp + "\",";
        strResp += "\"" + cmpHumedad + "\":\"" + valHumd + "\",";
        strResp += "\"" + cmpConcGas + "\":\"" + valCGas + "\",";
        strResp += "\"" + cmpVelAire + "\":\"" + valVAir + "\",";
        // Iniciar armado con datos MET de los usuarios
        strResp += "\"sensores_usuarios\":[";
        // Recuperar datos MET de los usuarios
        jsAry = getIdAndMetDeUsuariosEnOnto().getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
        tamJsonAry = jsAry.size();
        // Recorrer JSON-ARRAY
        for (int x = 0; x < tamJsonAry; x ++) {
            // Crear instancia de objeto JSON
            jsObj = jsAry.get(x).getAsJsonObject();
            // Recuperar data de los usuarios
            String strMET = jsObj.getAsJsonObject("val_met").get("value").toString().replaceAll("\"", "");
            String strIdUsr = jsObj.getAsJsonObject("val_persona").get("value").toString().replaceAll("\"", "");
            // Armdar respuesta con datos MET de los usuarios
            strResp += "{";
            strResp += "\"todo\":\"ok\",";
            strResp += "\"met\":\"" + strMET + "\",";
            strResp += "\"id_usuario\":\"" + strIdUsr + "\"";
            strResp += "},";
            // Indicar que se debe eliminar la última coma
            delUltCom = true;
        }
        // Validar si retirar la última coma
        if (delUltCom) {
            // Retirar la última coma
            strResp = strResp.substring(0, strResp.length() - 1);
        }
        // Cerrar armado con datos MET de los usuarios
        strResp += "]";
        // Devolver respuesta
        return strResp;
    }
    
    // Función que recupera de la Ontología, información solo del ambiente
    public JsonObject recuperarDatosDeSensores () {
        // Variables para datos sobre el ambiente
        String valTemp = "";
        String valHumd = "";
        String valCGas = "";
        String valVAir = "";
        // Recuperar datos sobre el ambiente
        JsonArray jsAry = getObservationsValueEnOnto().getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
        int tamJsonAry = jsAry.size();
        JsonObject jsObj = null;
        // Recorrer JSON-ARRAY
        for (int x = 0; x < tamJsonAry; x ++) {
            // Crear instancia de objeto JSON OBJECT
            jsObj = jsAry.get(x).getAsJsonObject();
            // Recuperar ID del registro
            valTemp = jsObj.getAsJsonObject("val_temperatura").get("value").toString().replaceAll("\"", "");
            valHumd = jsObj.getAsJsonObject("val_humedad").get("value").toString().replaceAll("\"", "");
            valCGas = jsObj.getAsJsonObject("val_congas").get("value").toString().replaceAll("\"", "");
            valVAir = jsObj.getAsJsonObject("val_velaire").get("value").toString().replaceAll("\"", "");
        }
        jsObj = new JsonObject();
        // Armar segmentos JSON
        jsObj.addProperty(cmpTempera, valTemp);
        jsObj.addProperty(cmpHumedad, valHumd);
        jsObj.addProperty(cmpConcGas, valCGas);
        jsObj.addProperty(cmpVelAire, valVAir);
        // Devolver respuesta
        return jsObj;
    }
    
    // Función que determina el Confor Térmico Grupal, si es posible, el como mejorarlo
    public String determinarConfortGrupalyAcciones (String idGrupo, String politica, int estrategia) {
        // Se crear manejador de CG para su calculo, mejora y definir como lograrlo.
        ControladorConfort controladorCG = new ControladorConfort(politica, estrategia,
                recuperarDatosDeSensores(), recuperaDataDeConfortPersonal(idGrupo), instanciaJENA);
        controladorCG.calcularConfortTermicoGrupal();
        controladorCG.determinarAccionesParaCTG();
        // Recuperar acciones definidas
        String strResp = controladorCG.generarResultadoGeneral();
        // Devolver respuesta
        return strResp;
    }
    
    // Función que determina el Confor Térmico Grupal, si es posible, el como mejorarlo
    public JsonArray recuperaVectoresDeLaTendencia (String valConfortTrend) {
        // Recupera VECTORES de la TENDENCIA de CONFORT
        JsonArray jsAr = getVectoresDeTendenciaEnOnto(valConfortTrend).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
        // regresar resultados
        return jsAr;
    }
    
    // Función que recupera los datos de confort personal de los usuarios de un grupo
    public JsonArray recuperaDataDeConfortPersonal (String idGrupo) {
        // Variables de análisis y respuesta
        JsonArray jsAryObj = getIdDeUsuariosEnOnto(idGrupo).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings"); 
        JsonArray jsAryRsp = new JsonArray();
        // TRY - CATCH
        try{
            // Recuperar tamaño de Arreglo json
            int tamJsonAry = jsAryObj.size();
            JsonObject jsData = null;
            JsonObject objTmp = null;
            // Preparar variables de análisis
            JsonArray jsArTemp = null;
            JsonObject jsTemp = null;
            // Recorrer data de Arreglo json
            for (int x = 0; x < tamJsonAry; x ++) {
                // Crear instancia de objeto JSON OBJECT
                jsData = jsAryObj.get(x).getAsJsonObject();
                // Crear objeto json temporal
                objTmp = new JsonObject();
               
                // Recuperar valor de PERSONA
                String idUsuario = jsData.getAsJsonObject("val_persona").get("value").toString().replaceAll("\"", "");
                // Armar segmentos JSON
                objTmp.addProperty("val_persona", idUsuario);
                
                // Recupera CLO de PERSONA
                jsArTemp = getCloMetDeUsuarioEnOnto(idUsuario).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                jsTemp = jsArTemp.get(0).getAsJsonObject();
                // Armar segmentos JSON
                objTmp.addProperty("val_met", jsTemp.getAsJsonObject("val_vmet").get("value").toString().replaceAll("\"", ""));
                objTmp.addProperty("val_clo", jsTemp.getAsJsonObject("val_vclo").get("value").toString().replaceAll("\"", ""));
                
                // Recupera SEXO, EDAD e ID de TENDENCIA de CONFORT
                jsArTemp = getInfoDeUsuarioEnOnto(idUsuario).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                jsTemp = jsArTemp.get(0).getAsJsonObject();
                // Recupera SEXO y EDAD
                String[] vlsSexoEdad = jsTemp.getAsJsonObject("val_sexoedad").get("value").toString().replaceAll("\"", "").split("_");
                // Armar segmentos JSON con SEXO y EDAD
                String valorSEXO = vlsSexoEdad[0];
                objTmp.addProperty("val_sexo", valorSEXO);
                String valorEDAD = vlsSexoEdad[1];
                objTmp.addProperty("val_edad", valorEDAD);
                // Recupera ID de TENDENCIA de CONFORT
                objTmp.addProperty("val_confortren", jsTemp.getAsJsonObject("val_confortrend").get("value").toString().replaceAll("\"", ""));
                 
                // Recupera valor de PMV
                jsArTemp = getPmvDeUsuarioEnOnto(idUsuario).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                jsTemp = jsArTemp.get(0).getAsJsonObject();
                // Armar segmentos JSON
                String valorPMV = jsTemp.getAsJsonObject("val_pmv").get("value").toString().replaceAll("\"", "");
                objTmp.addProperty("val_pmv", valorPMV);
                
                // Recupera PRIORIDAD
                jsArTemp = getRolDeUsuarioEnOnto(idUsuario).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                jsTemp = jsArTemp.get(0).getAsJsonObject();
                // Armar segmentos JSON
                objTmp.addProperty("val_numpriori", jsTemp.getAsJsonObject("val_numpriori").get("value").toString().replaceAll("\"", ""));

                // Recupera valor de BMI
                jsArTemp = getBmiDeUsuarioEnOnto(idUsuario).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                jsTemp = jsArTemp.get(0).getAsJsonObject();
                // Armar segmentos JSON
                String valorBMI = jsTemp.getAsJsonObject("val_bmi").get("value").toString().replaceAll("\"", "");
                objTmp.addProperty("val_bmi", valorBMI);

                // Recupera valor de CONCENTRACIÓN DE GAS
                jsArTemp = getConcentracionDeGasEnOnto().getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                jsTemp = jsArTemp.get(0).getAsJsonObject();
                // Armar segmentos JSON
                String valorConGAS = jsTemp.getAsJsonObject("val_congas").get("value").toString().replaceAll("\"", "");
                objTmp.addProperty("val_congas", valorConGAS);
                
                // Agregar segmento JSON
                jsAryRsp.add(objTmp);
            }
        } catch (Exception ex) {
            // Mostrar error
            ex.printStackTrace();
        }
        // Variabla de repuesta
        return jsAryRsp;
    }

    // Función que inicializa la Onto, con información estandar
    public String ejecutarConsulta(String strQuery) {
        // Variable
        String strResp = "";
        // Ejecutar sentencia sobre un a replica de la ontología
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(strQuery);
        // Validar resultado
        if (jsonElement != null) {
            // Tranformar resultado a un String Json
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            strResp = gson.toJson(jsonElement);
        }
        // Devolver respuesta
        return strResp;
    }
    
    // Función que inicializa la Onto, con información estandar
    public boolean esUnaOntoValida (String idGrupo) {
        // Variables de respuesta
        boolean boolRespAmbt = false;
        boolean boolRespHVACs = false;
        boolean boolRespUsers = false;
        // Realizar consulta de prueba
        JsonElement jsElement = getObservationsValueEnOnto();
        // Validar respuesta
        if (jsElement != null) {
            // Recupera datos de la respuesta
            JsonArray jsAry = jsElement.getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
            // Valida si hay datos en la respuesta
            if (jsAry.size() > 0) {
                // Se declara a la Ontología como valida
                boolRespAmbt = true;
            }
        }
        // Realizar consulta de prueba
        jsElement = getIdDeActuadoresEnOnto();
        // Validar respuesta
        if (jsElement != null) {
            // Recupera datos de la respuesta
            JsonArray jsAry = jsElement.getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
            // Valida si hay datos en la respuesta
            if (jsAry.size() == 5) {
                // Se declara a la Ontología como valida
                boolRespHVACs = true;
            }
        }
        // Validar necesidad de revisar a usuarios
        if (!idGrupo.equals("")) {
            // Realizar consulta de prueba
            jsElement = getIdDeUsuariosEnOnto(idGrupo);
            // Validar respuesta
            if (jsElement != null) {
                // Recupera datos de la respuesta
                JsonArray jsAry = jsElement.getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                // Valida si hay datos en la respuesta
                if (jsAry.size() >= 0) {
                    // Se declara a la Ontología como valida
                    boolRespUsers = true;
                }
            }
        } else {
            // Se declara a la Ontología como valida
            boolRespUsers = true;
        }
        // Devolver respuesta
        return boolRespAmbt && boolRespHVACs && boolRespUsers;
    }
    
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    
    // Función que administra la recuperación de un conjuto especifico de datos
    public JsonArray recuperarDatos(String conjunto) {
        // Variable de consulta
        JsonArray jsAryResp = null;
        // Seleccionar conjunto solicitado
        if (conjunto.equals(InspectorONTO.tagPersonas)) {
            // USUARIOS
            jsAryResp = recuperarDataDeUsuarios();
        } else if (conjunto.equals(InspectorONTO.tagBasePersonas)) {
            // DATA BASE DE USUARIOS
            jsAryResp = recuperarDataDeUsuarios(); // jsAryResp = recuperarDataBaseDeUsuarios();
        } else if (conjunto.equals(InspectorONTO.tagIdActuadores)) {
            // ID de MECANISMOS HVAC
            jsAryResp = recuperarIdDeActuadores();
        } else if (conjunto.equals(InspectorONTO.tagActuadores)) {
            // MECANISMOS HVACs
            jsAryResp = recuperarDataDeActuadores();
        } else if (conjunto.equals(InspectorONTO.tagRoles)) {
            // PRIORIDADES
            jsAryResp = recuperarDataDeRoles();
        } else if (conjunto.equals(InspectorONTO.tagTiposEquipamientos)) {
            // TIPOS DE EQUIPAMIENTO
            jsAryResp = recuperarDataDeTiposEquipamientos();
        } else if (conjunto.equals(InspectorONTO.tagTiposActuadores)) {
            // TIPOS DE HVACs
            jsAryResp = recuperarDataDeTiposDeActuadores();
        } else if (conjunto.equals(InspectorONTO.tagGrupos)) {
            // GRUPOS
            jsAryResp = recuperarDataDeGrupos();
        } else if (conjunto.equals(InspectorONTO.tagGrpsUsrs)) {
            // GRUPOS - USUARIOS
            jsAryResp = recuperarDataDeGruposUsuarios();
        }
        // Devolver datos
        return jsAryResp;
    }
    
    private JsonArray recuperarDataDeUsuarios() {
        // Variables de análisis y respuesta
        JsonArray jsAryObj = getIdDeUsuariosEnOnto().getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings"); 
        JsonArray jsAryRsp = new JsonArray();
        // TRY - CATCH
        try{
            // Recuperar tamaño de Arreglo json
            int tamJsonAry = jsAryObj.size();
            JsonObject jsData = null;
            JsonObject objTmp = null;
            // Preparar variables de análisis
            JsonArray jsArTemp = null;
            JsonObject jsTemp = null;
            int tamArTemp = 0;
            // Recorrer data de Arreglo json
            for (int x = 0; x < tamJsonAry; x ++) {
                // Crear instancia de objeto JSON OBJECT
                jsData = jsAryObj.get(x).getAsJsonObject();
                // Crear objeto json temporal
                objTmp = new JsonObject();
                
                // Recuperar valor de PERSONA
                String idUsuario = jsData.getAsJsonObject("val_persona").get("value").toString().replaceAll("\"", "");
                // Armar segmentos JSON
                objTmp.addProperty("id_usuario", idUsuario);
                
                // Recuperar NOMBRE de PERSONA
                jsArTemp = getNounDeUsuarioEnOnto(idUsuario).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                jsTemp = jsArTemp.get(0).getAsJsonObject();
                // Armar segmentos JSON
                objTmp.addProperty("etiqueta_usuario", jsTemp.getAsJsonObject("val_noun").get("value").toString().replaceAll("\"", ""));
                
                // Recupera PRIORIDAD
                jsArTemp = getRolDeUsuarioEnOnto(idUsuario).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                jsTemp = jsArTemp.get(0).getAsJsonObject();
                // Armar segmentos JSON
                objTmp.addProperty("id_rol", jsTemp.getAsJsonObject("val_numpriori").get("value").toString().replaceAll("\"", ""));
                objTmp.addProperty("rol", jsTemp.getAsJsonObject("val_prioridad").get("value").toString().replaceAll("\"", ""));
                
                // Recupera SEXO, EDAD, PESO, ALTURA e ID de ROPA
                jsArTemp = getInfoDeUsuarioEnOnto(idUsuario).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                jsTemp = jsArTemp.get(0).getAsJsonObject();
                // Recupera valor de PESO y ALTURA
                String[] vlsSexoEdad = jsTemp.getAsJsonObject("val_sexoedad").get("value").toString().replaceAll("\"", "").split("_");
                // Armar segmentos JSON con SEXO y EDAD
                objTmp.addProperty("sexo", vlsSexoEdad[0]);
                objTmp.addProperty("edad", vlsSexoEdad[1]);
                // Recupera valor de PESO y ALTURA
                String[] vlsPesoAltura = jsTemp.getAsJsonObject("val_pesoaltura").get("value").toString().replaceAll("\"", "").split("_");
                // Armar segmentos JSON con PESO y ALTURA
                objTmp.addProperty("peso", vlsPesoAltura[0]);
                objTmp.addProperty("altura", vlsPesoAltura[1]);
                // Recupera valor ID de ROPA
                String valNumClo = jsTemp.getAsJsonObject("val_numclo").get("value").toString().replaceAll("\"", "");
                // Armar segmentos JSON con ID del EQUIPAMIENTO
                objTmp.addProperty("id_equipamiento", valNumClo);
                // Recupera ID de TENDENCIA de CONFORT
                String valCnfrTrnd = jsTemp.getAsJsonObject("val_confortrend").get("value").toString().replaceAll("\"", "");
                
                // Recupera DESCRIPCIÓN de la ROPA y VALOR CLO
                jsArTemp = getInfoDeRopaEnOnto(valNumClo).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                tamArTemp = jsArTemp.size();
                jsTemp = null;
                // Recorrer data de Arreglo json
                for (int z = 0; z < tamArTemp; z ++) {
                    // Recupera DESCRIPCIÓN de la ROPA y VALOR CLO
                    jsTemp = jsArTemp.get(z).getAsJsonObject();
                    String valValClo = jsTemp.getAsJsonObject("val_valclo").get("value").toString().replaceAll("\"", "");
                    String[] valsDescrip = jsTemp.getAsJsonObject("val_desclo").get("value").toString().replaceAll("\"", "").split(":");
                    // Armar segmentos JSON
                    if (valsDescrip[0].equals("")) {
                        objTmp.addProperty("valor_clo_equipado", valValClo);
                        objTmp.addProperty("descripcion_clo_equipado", valsDescrip[1]);
                    } else if (valsDescrip[0].equals("m")) {
                        objTmp.addProperty("suma_clo_para_m_equipado", valValClo);
                        objTmp.addProperty("descripcion_clo_para_m_equipado", valsDescrip[1]);
                    } else if (valsDescrip[0].equals("h")) {
                        objTmp.addProperty("suma_clo_para_h_equipado", valValClo);
                        objTmp.addProperty("descripcion_clo_para_h_equipado", valsDescrip[1]);
                    }
                }
                // Recupera VECTORES de la TENDENCIA de CONFORT
                jsArTemp = getVectoresDeTendenciaEnOnto(valCnfrTrnd).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                tamArTemp = jsArTemp.size();
                // Validar existencia de dos Vectores
                if (tamArTemp >= 2) {
                    // Recuperar y elegir condiciones
                    jsTemp = jsArTemp.get(0).getAsJsonObject();
                    double conOne = Double.parseDouble(jsTemp.getAsJsonObject("val_condi").get("value").toString().replaceAll("\"", ""));
                    double tenOne = Double.parseDouble(jsTemp.getAsJsonObject("val_tende").get("value").toString().replaceAll("\"", ""));
                    // Recuperar y elegir condiciones
                    jsTemp = jsArTemp.get(1).getAsJsonObject();
                    double conTwo = Double.parseDouble(jsTemp.getAsJsonObject("val_condi").get("value").toString().replaceAll("\"", ""));
                    double tenTwo = Double.parseDouble(jsTemp.getAsJsonObject("val_tende").get("value").toString().replaceAll("\"", ""));
                    // Generar VECTOR de la TENDENCIA de CONFORT
                    VectorDeTende vTop = ControladorConfort.determinarVectorTop(conOne, tenOne, conTwo, tenTwo);
                    objTmp.addProperty("condicion_top", vTop.condition);
                    objTmp.addProperty("tendencia_top", vTop.tendency);
                    // Generar VECTOR de la TENDENCIA de CONFORT
                    VectorDeTende vBot = ControladorConfort.determinarVectorBottom(conOne, tenOne, conTwo, tenTwo);
                    objTmp.addProperty("condicion_bottom", vBot.condition);
                    objTmp.addProperty("tendencia_bottom", vBot.tendency);
                }
                
                // Recupera GRUPO
                jsArTemp = getDataGrupoDeUsuarioEnOnto(idUsuario).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                tamArTemp = jsArTemp.size();
                jsTemp = null;
                // Recorrer data de Arreglo json
                for (int z = 0; z < tamArTemp; z ++) {
                    // Crear objeto json temporal
                    JsonObject objXXX = cloneJsonObject(objTmp);
                    // Recupera valores de grupo
                    jsTemp = jsArTemp.get(z).getAsJsonObject();
                    // Armar segmentos JSON
                    objXXX.addProperty("id_grupo", jsTemp.getAsJsonObject("val_numgrp").get("value").toString().replaceAll("\"", ""));
                    objXXX.addProperty("nombre_grupo", jsTemp.getAsJsonObject("val_grupo").get("value").toString().replaceAll("\"", ""));
                    // Agregar segmento JSON
                    jsAryRsp.add(objXXX);
                }
            }
        } catch (Exception ex) {
            // Mostrar error
            ex.printStackTrace();
        }
        // Variabla de repuesta
        return jsAryRsp;
    }
    
    private JsonArray recuperarDataDeActuadores() {
        // Variables de análisis y respuesta
        JsonArray jsAryObj = getIdDeActuadoresEnOnto().getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
        JsonArray jsAryRsp = new JsonArray();
        // TRY - CATCH
        try{
            // Recuperar tamaño de Arreglo json
            int tamJsonAry = jsAryObj.size();
            JsonObject jsData = null;
            JsonObject objTmp = null;
            // Preparar variables de análisis
            JsonArray jsArTemp = null;
            JsonObject jsTemp = null;
            // Recorrer data de Arreglo json
            for (int x = 0; x < tamJsonAry; x ++) {
                // Crear instancia de objeto JSON OBJECT
                jsData = jsAryObj.get(x).getAsJsonObject();
                // Crear objeto json temporal
                objTmp = new JsonObject();
                // Recupera ID de Actuador
                String isActuador = jsData.getAsJsonObject("val_actuador").get("value").toString().replaceAll("\"", "");
                // Armar segmentos JSON
                objTmp.addProperty("id_mecanismo_hvac", isActuador);
                
                // Recupera INFORMACIÓN DE ACTUADOR
                jsArTemp = getInfoActuadorEnOnto(isActuador).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                jsTemp = jsArTemp.get(0).getAsJsonObject();
                // Armar segmentos JSON
                objTmp.addProperty("estado", jsTemp.getAsJsonObject("val_estado").get("value").toString().replaceAll("\"", ""));
                String tipoActuador = jsTemp.getAsJsonObject("val_tipoact").get("value").toString().replaceAll("\"", "");
                objTmp.addProperty("tipo_de_mecanismo", tipoActuador);
                
                // Recupera ID del TIPO DE ACTUADOR
                jsArTemp = getIdDeTipoActuadorEnOnto(tipoActuador).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                jsTemp = jsArTemp.get(0).getAsJsonObject();
                // Armar segmentos JSON
                objTmp.addProperty("id_tipo_de_mecanismo_hvac", jsTemp.getAsJsonObject("val_numtipoact").get("value").toString().replaceAll("\"", ""));
                
                // Recupera CONFIGURACIÓN DE ACTUADOR
                jsArTemp = getConfigActuadorEnOnto(isActuador).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                // Validar datos
                if (jsArTemp.size() > 0) {
                    // Recuperar objeto y armar JSON
                    jsTemp = jsArTemp.get(0).getAsJsonObject();
                    // Armar segmentos JSON
                    objTmp.addProperty("temperatura", jsTemp.getAsJsonObject("val_temp").get("value").toString().replaceAll("\"", ""));
                    objTmp.addProperty("humedad", jsTemp.getAsJsonObject("val_hume").get("value").toString().replaceAll("\"", ""));
                    objTmp.addProperty("velocidad_del_aire", jsTemp.getAsJsonObject("val_vaire").get("value").toString().replaceAll("\"", ""));
                    objTmp.addProperty("concentracion_de_gas", jsTemp.getAsJsonObject("val_cgas").get("value").toString().replaceAll("\"", ""));
                } else {
                    // Armar segmentos JSON
                    objTmp.addProperty("temperatura", "0");
                    objTmp.addProperty("humedad", "0");
                    objTmp.addProperty("velocidad_del_aire", "0");
                    objTmp.addProperty("concentracion_de_gas", "0");
                }
                // Agregar segmento JSON
                jsAryRsp.add(objTmp);
            }
        } catch (Exception ex) {
            // Mostrar error
            ex.printStackTrace();
        }
        // Regresar resultado
        return jsAryRsp;
    }
    
    private JsonArray recuperarIdDeActuadores() {
        // Variables de análisis y respuesta
        JsonArray jsAryObj = getIdDeActuadoresEnOnto().getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
        JsonArray jsAryRsp = new JsonArray();
        // TRY - CATCH
        try{
            // Recuperar tamaño de Arreglo json
            int tamJsonAry = jsAryObj.size();
            JsonObject jsData = null;
            JsonObject objTmp = null;
            // Recorrer data de Arreglo json
            for (int x = 0; x < tamJsonAry; x ++) {
                // Crear instancia de objeto JSON OBJECT
                jsData = jsAryObj.get(x).getAsJsonObject();
                // Crear objeto json temporal
                objTmp = new JsonObject();
                // Armar segmentos JSON
                objTmp.addProperty("id_mecanismo_hvac", jsData.getAsJsonObject("val_actuador").get("value").toString().replaceAll("\"", ""));
                // Agregar segmento JSON
                jsAryRsp.add(objTmp);
            }
        } catch (Exception ex) {
            // Mostrar error
            ex.printStackTrace();
        }
        // Regresar resultado
        return jsAryRsp;
    }
    
    private JsonArray recuperarDataDeRoles() {
        // Variables de análisis y respuesta
        JsonArray jsAryObj = getRolesEnOnto().getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
        JsonArray jsAryRsp = new JsonArray();
        // TRY - CATCH
        try{
            // Recuperar tamaño de Arreglo json
            int tamJsonAry = jsAryObj.size();
            JsonObject jsData = null;
            JsonObject objTmp = null;
            // Recorrer data de Arreglo json
            for (int x = 0; x < tamJsonAry; x ++) {
                // Crear instancia de objeto JSON OBJECT
                jsData = jsAryObj.get(x).getAsJsonObject();
                // Crear objeto json temporal
                objTmp = new JsonObject();
                
                // Armar segmentos JSON
                objTmp.addProperty("id_rol", jsData.getAsJsonObject("val_numprioridad").get("value").toString().replaceAll("\"", ""));
                objTmp.addProperty("rol", jsData.getAsJsonObject("val_prioridad").get("value").toString().replaceAll("\"", ""));
                
                // Agregar segmento JSON
                jsAryRsp.add(objTmp);
            }
        } catch (Exception ex) {
            // Mostrar error
            ex.printStackTrace();
        }
        // Regresar resultado
        return jsAryRsp;
    }
    
    private JsonArray recuperarDataDeTiposEquipamientos() {
        // Variables de análisis y respuesta
        JsonArray jsAryObj = getIdDeRopaEnOnto().getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
        JsonArray jsAryRsp = new JsonArray();
        // TRY - CATCH
        try{
            // Recuperar tamaño de Arreglo json
            int tamJsonAry = jsAryObj.size();
            JsonObject jsData = null;
            JsonObject objTmp = null;
            // Preparar variables de análisis
            JsonArray jsArTemp = null;
            JsonObject jsTemp = null;
            int tamArTemp = 0;
            // Recorrer data de Arreglo json
            for (int x = 0; x < tamJsonAry; x ++) {
                // Crear instancia de objeto JSON OBJECT
                jsData = jsAryObj.get(x).getAsJsonObject();
                // Crear objeto json temporal
                objTmp = new JsonObject();
                // Recupera valor ID de ROPA
                String valNumClo = jsData.getAsJsonObject("val_numclo").get("value").toString().replaceAll("\"", "");
                objTmp.addProperty("id_tipo_de_equipamiento", valNumClo);
                // Recupera DESCRIPCIÓN de la ROPA y VALOR CLO
                jsArTemp = getLabelDeRopaEnOnto(valNumClo).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                jsTemp = jsArTemp.get(0).getAsJsonObject();
                objTmp.addProperty("nombre_equipamiento", jsTemp.getAsJsonObject("val_noun").get("value").toString().replaceAll("\"", ""));
                // Recupera DESCRIPCIÓN de la ROPA y VALOR CLO
                jsArTemp = getInfoDeRopaEnOnto(valNumClo).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                tamArTemp = jsArTemp.size();
                jsTemp = null;
                // Recorrer data de Arreglo json
                for (int z = 0; z < tamArTemp; z ++) {
                    // Recupera DESCRIPCIÓN de la ROPA y VALOR CLO
                    jsTemp = jsArTemp.get(z).getAsJsonObject();
                    String valValClo = jsTemp.getAsJsonObject("val_valclo").get("value").toString().replaceAll("\"", "");
                    String[] valsDescrip = jsTemp.getAsJsonObject("val_desclo").get("value").toString().replaceAll("\"", "").split(":");
                    // Armar segmentos JSON
                    if (valsDescrip[0].equals("")) {
                        objTmp.addProperty("valor_clo", valValClo);
                        objTmp.addProperty("descripcion", valsDescrip[1]);
                    } else if (valsDescrip[0].equals("m")) {
                        objTmp.addProperty("suma_clo_para_m", valValClo);
                        objTmp.addProperty("descripcion_para_m", valsDescrip[1]);
                    } else if (valsDescrip[0].equals("h")) {
                        objTmp.addProperty("suma_clo_para_h", valValClo);
                        objTmp.addProperty("descripcion_para_h", valsDescrip[1]);
                    }
                }
                // Agregar segmento JSON
                jsAryRsp.add(objTmp);
            }
        } catch (Exception ex) {
            // Mostrar error
            ex.printStackTrace();
        }
        // Regresar resultado
        return jsAryRsp;
    }
    
    private JsonArray recuperarDataDeTiposDeActuadores() {
        // Variables de análisis y respuesta
        JsonArray jsAryObj = getTiposDeActuadoresEnOnto().getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
        JsonArray jsAryRsp = new JsonArray();
        // TRY - CATCH
        try{
            // Recuperar tamaño de Arreglo json
            int tamJsonAry = jsAryObj.size();
            JsonObject jsData = null;
            JsonObject objTmp = null;
            // Preparar variables de análisis
            JsonArray jsArTemp = null;
            JsonObject jsTemp = null;
            int tamArTemp = 0;
            // Recorrer data de Arreglo json
            for (int x = 0; x < tamJsonAry; x ++) {
                // Crear instancia de objeto JSON OBJECT
                jsData = jsAryObj.get(x).getAsJsonObject();
                // Crear objeto json temporal
                objTmp = new JsonObject();
                // Armar segmentos JSON
                String tipoActuador = jsData.getAsJsonObject("val_tipomeca").get("value").toString().replaceAll("\"", "");
                // Recupera ID del TIPO DE ACTUADOR
                jsArTemp = getIdDeTipoActuadorEnOnto(tipoActuador).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                // Validar existencia de Tipo de HVACs
                if (jsArTemp.size() > 0) {
                    // Armar segmentos JSON
                    objTmp.addProperty("tipo_de_mecanismo", tipoActuador);
                    // Recupera intancia del ID de un HVAC
                    jsTemp = jsArTemp.get(0).getAsJsonObject();
                    // Armar segmentos JSON
                    objTmp.addProperty("id_tipo_de_mecanismo_hvac", jsTemp.getAsJsonObject("val_numtipoact").get("value").toString().replaceAll("\"", ""));
                    // Agregar segmento JSON
                    jsAryRsp.add(objTmp);
                }
            }
        } catch (Exception ex) {
            // Mostrar error
            ex.printStackTrace();
        }
        // Regresar resultado
        return jsAryRsp;
    }
    
    private JsonArray recuperarDataDeGrupos() {
        // Variables de análisis y respuesta
        JsonArray jsAryObj = getGruposEnOnto().getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
        JsonArray jsAryRsp = new JsonArray();
        // TRY - CATCH
        try{
            // Recuperar tamaño de Arreglo json
            int tamJsonAry = jsAryObj.size();
            JsonObject jsData = null;
            JsonObject objTmp = null;
            // Preparar variables de análisis
            JsonArray jsArTemp = null;
            JsonObject jsTemp = null;
            int tamArTemp = 0;
            // Recorrer data de Arreglo json
            for (int x = 0; x < tamJsonAry; x ++) {
                // Crear instancia de objeto JSON OBJECT
                jsData = jsAryObj.get(x).getAsJsonObject();
                // Crear objeto json temporal
                objTmp = new JsonObject();
                // Armar segmentos JSON
                objTmp.addProperty("id_grupo", jsData.getAsJsonObject("val_numgrupo").get("value").toString().replaceAll("\"", ""));
                objTmp.addProperty("nombre", jsData.getAsJsonObject("val_grupo").get("value").toString().replaceAll("\"", ""));
                // Agregar segmento JSON
                jsAryRsp.add(objTmp);
            }
        } catch (Exception ex) {
            // Mostrar error
            ex.printStackTrace();
        }
        // Regresar resultado
        return jsAryRsp;
    }
    
    private JsonArray recuperarDataDeGruposUsuarios () {
        // Variables de análisis y respuesta
        JsonArray jsAryObj = getGruposEnOnto().getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
        JsonArray jsAryRsp = new JsonArray();
        // TRY - CATCH
        try{
            // Recuperar tamaño de Arreglo json
            int tamJsonAry = jsAryObj.size();
            JsonObject jsData = null;
            JsonObject objTmp = null;
            // Preparar variables de análisis
            JsonArray jsArTemp = null;
            JsonObject jsTemp = null;
            int tamArTemp = 0;
            // Recorrer data de Arreglo json
            for (int x = 0; x < tamJsonAry; x ++) {
                // Crear instancia de objeto JSON OBJECT
                jsData = jsAryObj.get(x).getAsJsonObject();
                // Crear objeto json temporal
                objTmp = new JsonObject();
                // Armar segmentos JSON
                objTmp.addProperty("nombre", jsData.getAsJsonObject("val_grupo").get("value").toString().replaceAll("\"", ""));
                String idGrupo = jsData.getAsJsonObject("val_numgrupo").get("value").toString().replaceAll("\"", "");
                objTmp.addProperty("id_grupo", idGrupo);
                
                // Recupera GRUPO
                jsArTemp = getDataUsuarioDeGrupoEnOnto(idGrupo).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                tamArTemp = jsArTemp.size();
                jsTemp = null;
                // Valida número de usuarios en el grupo
                if (tamArTemp > 0) {
                    // Recorrer data de Arreglo json
                    for (int z = 0; z < tamArTemp; z ++) {
                        // Crear objeto json temporal
                        JsonObject objXXX = cloneJsonObject(objTmp);
                        // Recupera valores de grupo
                        jsTemp = jsArTemp.get(z).getAsJsonObject();
                        // Armar segmentos JSON
                        String idUsuario = jsTemp.getAsJsonObject("val_persona").get("value").toString().replaceAll("\"", "");
                        objXXX.addProperty("id_usuario", idUsuario);
                        
                        // Recuperar NOMBRE de PERSONA
                        JsonArray jsATmp2 = getNounDeUsuarioEnOnto(idUsuario).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
                        JsonObject jsTmp2 = jsATmp2.get(0).getAsJsonObject();
                        
                        // Armar segmentos JSON
                        objXXX.addProperty("etiqueta_usuario", jsTmp2.getAsJsonObject("val_noun").get("value").toString().replaceAll("\"", ""));
                        
                        // Agregar segmento JSON
                        jsAryRsp.add(objXXX);
                    }
                } else {
                    // Agrega datos vacios
                    objTmp.addProperty("id_usuario", "");
                    objTmp.addProperty("etiqueta_usuario", "");
                    // Agregar segmento JSON
                    jsAryRsp.add(objTmp);
                }
            }
        } catch (Exception ex) {
            // Mostrar error
            ex.printStackTrace();
        }
        // Regresar resultado
        return jsAryRsp;
    }
    
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    
    private JsonElement getIdDeUsuariosEnOnto() {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED \n" +
                "?val_persona \n" +
                "WHERE {\n" +
                "	# >> Recupera a todas las personas\n" +
                "	?persona rdf:type foaf:Person .\n" +
                "	# << Recuperar el dato de las variables consultadas\n" +
                "	?persona owl:topDataProperty ?val_persona .\n" +
                "	FILTER ((datatype(?val_persona)) = xsd:nonNegativeInteger) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getRolDeUsuarioEnOnto(String idUsuario) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED \n" +
                "?val_numpriori ?val_prioridad \n" +
                "WHERE {\n" +
                "	# >> Recupera a todas las personas\n" +
                "	?persona rdf:type foaf:Person .\n" +
                "	?persona owl:topDataProperty " + idUsuario + " .\n" +
                "	# >> Recuperar instancias de priroridad\n" +
                "	?numpriori rdf:type actgu:NumPriority .\n" +
                "	?prioridad rdf:type actgu:Priority .\n" +
                "	?numpriori actgu:numeratePriority ?prioridad .\n" +
                "	?persona actgu:isSortOf ?numpriori .\n" +
                "	# << Recuperar el dato de las variables consultadas\n" +
                "	?prioridad owl:topDataProperty ?val_prioridad .\n" +
                "	?numpriori owl:topDataProperty ?val_numpriori .\n" +
                "	FILTER ((datatype(?val_prioridad)) = xsd:string) .\n" +
                "	FILTER ((datatype(?val_numpriori)) = xsd:nonNegativeInteger) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getNounDeUsuarioEnOnto(String idUsuario) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED \n" +
                "?val_noun \n" +
                "WHERE {\n" +
                "	# >> Recupera a todas las personas\n" +
                "	?persona rdf:type foaf:Person .\n" +
                "	?persona owl:topDataProperty " + idUsuario + " .\n" +
                "	# >> Recuperar instancias de priroridad\n" +
                "	?noun rdf:type actgu:Noun .\n" +
                "	?persona foaf:name ?noun .\n" +
                "	# << Recuperar el dato de las variables consultadas\n" +
                "	?noun owl:topDataProperty ?val_noun .\n" +
                "	FILTER ((datatype(?val_noun)) = xsd:string) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getDataGrupoDeUsuarioEnOnto(String idUsuario) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED \n" +
                "?val_numgrp ?val_grupo \n" +
                "WHERE {\n" +
                "	# >> Recupera a todas las personas\n" +
                "	?persona rdf:type foaf:Person .\n" +
                "	?persona owl:topDataProperty " + idUsuario + " .\n" +
                "	# >> Recuperar instancias de grupo\n" +
                "	?grupo rdf:type foaf:Group .\n" +
                "	?grupo foaf:member ?persona .\n" +
                "	?numgrp rdf:type actgu:NumGroup .\n" +
                "	?numgrp actgu:numerateGroup ?grupo .\n" +
                "	# << Recuperar el dato de las variables consultadas\n" +
                "	?grupo owl:topDataProperty ?val_grupo .\n" +
                "	?numgrp owl:topDataProperty ?val_numgrp .\n" +
                "	FILTER ((datatype(?val_grupo)) = xsd:string) .\n" +
                "	FILTER ((datatype(?val_numgrp)) = xsd:nonNegativeInteger) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getInfoDeUsuarioEnOnto(String idUsuario) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED \n" +
                "?val_sexoedad ?val_pesoaltura ?val_numclo ?val_confortrend \n" +
                "WHERE {\n" +
                "	# >> Recupera a todas las personas\n" +
                "	?persona rdf:type foaf:Person .\n" +
                "	?persona owl:topDataProperty " + idUsuario + " .\n" +
                "	# >> Recuperar la información demografica de cada persona\n" +
                "	?sexoedad rdf:type actgu:Demographic .\n" +
                "	?sexoedad actgu:characterizes ?persona .\n" +
                "	# >> Recuperar la información antropometrica de cada persona\n" +
                "	?pesoaltura rdf:type actgu:Anthropometry .\n" +
                "	?pesoaltura actgu:characterizes ?persona .\n" +
                "	# >> Recuperar el CLO que viste actualmente cada persona\n" +
                "	?numclo rdf:type actgu:NumClothes .\n" +
                "	?persona actgu:dressed ?numclo .\n" +
                "	# >> Recuperar la tendencia de confort\n" +
                "	?confortrend rdf:type actgu:ComfortTrend .\n" +
                "	?persona actgu:usedTo ?confortrend .\n" +
                "	# << Recuperar el dato de las variables consultadas\n" +
                "	?sexoedad owl:topDataProperty ?val_sexoedad .\n" +
                "	?pesoaltura owl:topDataProperty ?val_pesoaltura .\n" +
                "	?numclo owl:topDataProperty ?val_numclo .\n" +
                "	?confortrend owl:topDataProperty ?val_confortrend .\n" +
                "	FILTER ((datatype(?val_sexoedad)) = xsd:string) .\n" +
                "	FILTER ((datatype(?val_pesoaltura)) = xsd:string) .\n" +
                "	FILTER ((datatype(?val_numclo)) = xsd:nonNegativeInteger) .\n" +
                "	FILTER ((datatype(?val_confortrend)) = xsd:string) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getInfoDeRopaEnOnto(String valNumClo) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED ?val_desclo ?val_valclo \n" +
                "WHERE {\n" +
                "	# >> Recuperar el CLO que viste actualmente cada persona\n" +
                "	?numclo rdf:type actgu:NumClothes .\n" +
                "	?numclo owl:topDataProperty " + valNumClo + " .\n" +
                "	# >> Recuperar CLO Value\n" +
                "	?desclo rdf:type actgu:Clothes .\n" +
                "	?numclo actgu:numerateClothes ?desclo .\n" +
                "	# >> Recuperar CLO Descripcion\n" +
                "	?valclo rdf:type actgu:CloValue .\n" +
                "	?desclo actgu:equippeWith ?valclo .\n" +
                "	# << Recuperar valor de CLO\n" +
                "	?desclo owl:topDataProperty ?val_desclo .\n" +
                "	?valclo owl:topDataProperty ?val_valclo .\n" +
                "	FILTER ((datatype(?val_desclo)) = xsd:string) .\n" +
                "	FILTER ((datatype(?val_valclo)) = xsd:double) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getLabelDeRopaEnOnto(String valNumClo) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED ?val_noun \n" +
                "WHERE {\n" +
                "	# >> Recuperar el LABEL que viste actualmente cada persona\n" +
                "	?numclo rdf:type actgu:NumClothes .\n" +
                "	?numclo owl:topDataProperty " + valNumClo + " .\n" +
                "	# >> Recuperar LABEL Value\n" +
                "	?noun rdf:type actgu:Label .\n" +
                "	?numclo actgu:subscribe ?noun .\n" +
                "	# << Recuperar valor de LABEL \n" +
                "	?noun owl:topDataProperty ?val_noun .\n" +
                "	FILTER ((datatype(?val_noun)) = xsd:string) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getIdDeUsuariosEnOnto(String idGrupo) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED \n" +
                "?val_persona \n" +
                "WHERE {\n" +
                "        # >> Recuperar instancias de grupo \n" +
                "        ?numgrp rdf:type actgu:NumGroup . \n" +
                "        ?numgrp owl:topDataProperty " + idGrupo + " . \n" +
                "        ?nomgrp rdf:type foaf:Group .\n" +
                "        ?numgrp actgu:numerateGroup ?nomgrp . \n" +
                "        # >> Recupera a todas las personas \n" +
                "        ?persona rdf:type foaf:Person . \n" +
                "        ?nomgrp foaf:member ?persona . \n" +
                "	# << Recuperar el dato de las variables consultadas \n" +
                "	?persona owl:topDataProperty ?val_persona . \n" +
                "	FILTER ((datatype(?val_persona)) = xsd:nonNegativeInteger) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getIdDeGrupoEnOnto (String grupo) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED \n" +
                "?val_numgrp \n" +
                "WHERE {\n" +
                "	# >> Recuperar instancias de grupo\n" +
                "	?grupo rdf:type foaf:Group .\n" +
                "       ?grupo owl:topDataProperty \"" + grupo + "\" .\n" +
                "	?numgrp rdf:type actgu:NumGroup .\n" +
                "	?numgrp actgu:numerateGroup ?grupo .\n" +
                "	# << Recuperar el dato de las variables consultadas\n" +
                "	?numgrp owl:topDataProperty ?val_numgrp .\n" +
                "	FILTER ((datatype(?val_numgrp)) = xsd:nonNegativeInteger) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getCloMetDeUsuarioEnOnto(String idUsuario) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED \n" +
                "?val_vclo ?val_vmet \n" +
                "WHERE {\n" +
                "	# >> Recupera a todas las personas\n" +
                "	?persona rdf:type foaf:Person .\n" +
                "	?persona owl:topDataProperty " + idUsuario + " .\n" +
                "	# >> Recuperar el MET que tiene actualmente cada persona\n" +
                "	?vmet rdf:type actgu:MetabolicRate .\n" +
                "	?vmet actgu:isPhysicalWearOf ?persona .\n" +
                "	# >> Recuperar el CLO que viste actualmente cada persona\n" +
                "	?vclo rdf:type actgu:InsulationValue .\n" +
                "	?vclo actgu:isEquippedBy ?persona .\n" +
                "	# << Recuperar el dato de las variables consultadas\n" +
                "	?vmet owl:topDataProperty ?val_vmet .\n" +
                "	?vclo owl:topDataProperty ?val_vclo .\n" +
                "	FILTER ((datatype(?val_vmet)) = xsd:double) .\n" +
                "	FILTER ((datatype(?val_vclo)) = xsd:double) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
   
    private JsonElement getVectoresDeTendenciaEnOnto(String valConfortTrend) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED ?val_slopeconfort ?val_vector ?val_condi ?val_tende \n" +
                "WHERE {\n" +
                "	# >> Recuperar objeto de tendencia de confort\n" +
                "	?confortrend rdf:type actgu:ComfortTrend .\n" +
                "	?confortrend owl:topDataProperty \"" + valConfortTrend + "\" .\n" +
                "	# >> Recuperar vector\n" +
                "	?slopeconfort rdf:type actgu:SlopeOfComfort .\n" +
                "	?confortrend actgu:form ?slopeconfort .\n" +
                "	# >> Recuperar vector\n" +
                "	?vector rdf:type actgu:Vector .\n" +
                "	?confortrend actgu:utilize ?vector .\n" +
                "	# >> Recuperar condición\n" +
                "	?condi rdf:type actgu:Condition .\n" +
                "	?vector actgu:take ?condi .\n" +
                "	# >> Recuperar tendencia\n" +
                "	?tende rdf:type actgu:Tendency .\n" +
                "	?vector actgu:take ?tende .\n" +
                "	# << Recuperar valor de CLO\n" +
                "	?slopeconfort owl:topDataProperty ?val_slopeconfort .\n" +
                "	?vector owl:topDataProperty ?val_vector .\n" +
                "	?condi owl:topDataProperty ?val_condi .\n" +
                "	?tende owl:topDataProperty ?val_tende .\n" +
                "	FILTER ((datatype(?val_slopeconfort)) = xsd:double) .\n" +
                "	FILTER ((datatype(?val_vector)) = xsd:string) .\n" +
                "	FILTER ((datatype(?val_condi)) = xsd:double) .\n" +
                "	FILTER ((datatype(?val_tende)) = xsd:double) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getInfoActuadorEnOnto(String idActuador) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
            "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
            "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
            "SELECT REDUCED \n" +
            "?val_estado ?val_tipoact \n" +
            "WHERE {\n" +
            "        # Recuperar instancia de actuador\n" +
            "        ?actuador rdf:type actgu:Actuator .\n" +
            "        ?actuador owl:topDataProperty " + idActuador + " .\n" +
            "        # Recuperar instancia de Estado\n" +
            "        ?estado rdf:type actgu:State .\n" +
            "        ?actuador actgu:present ?estado .\n" +
            "        # Recuperar instancia de Tipo\n" +
            "        ?tipoact rdf:type actgu:Type .\n" +
            "        ?actuador actgu:kind ?tipoact .\n" +
            "        # Extraer valor exacto\n" +
            "        ?estado owl:topDataProperty ?val_estado .\n" +
            "        ?tipoact owl:topDataProperty ?val_tipoact .\n" +
            "        FILTER ((datatype(?val_estado)) = xsd:string) .\n" +
            "        FILTER (?val_estado = \"a\" || ?val_estado = \"e\") .\n" +
            "        FILTER ((datatype(?val_tipoact)) = xsd:string) .\n" +
            "        FILTER (?val_tipoact = \"puerta\" || ?val_tipoact = \"ventanas\" || ?val_tipoact = \"ventilador\" || ?val_tipoact = \"calefactor\" || ?val_tipoact = \"aire acondicionado\") \n" +
            "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getIdDeTipoActuadorEnOnto(String tipoActuador) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
            "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
            "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
            "SELECT REDUCED \n" +
            "?val_numtipoact \n" +
            "WHERE {\n" +
            "        # Recuperar instancia de Tipo\n" +
            "        ?tipoact rdf:type actgu:Type .\n" +
            "        ?tipoact owl:topDataProperty \"" + tipoActuador.toLowerCase() + "\" .\n" +
            "        ?numtipoact rdf:type actgu:NumType .\n" +
            "        ?numtipoact actgu:numerateType ?tipoact .\n" +
            "        # Extraer valor exacto\n" +
            "        ?numtipoact owl:topDataProperty ?val_numtipoact .\n" +
            "        FILTER ((datatype(?val_numtipoact)) = xsd:nonNegativeInteger) \n" +
            "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }

    private JsonElement getConfigActuadorEnOnto(String idActuador) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
            "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
            "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
            "SELECT REDUCED \n" +
            "?val_temp ?val_hume ?val_vaire ?val_cgas \n" +
            "WHERE {\n" +
            "        # Recuperar instancia de actuador\n" +
            "        ?actuador rdf:type actgu:Actuator .\n" +
            "        ?actuador owl:topDataProperty " + idActuador + " .\n" +
            "        # >> Recuperar la configuracion de TEMPERATURA\n" +
            "        ?tempproper rdf:type ssn:Property .\n" +
            "        ?tempproper owl:topDataProperty \"" + GestorONTO.properTempera + "\" .\n" +
            "        ?tempconfig rdf:type actgu:ConfigurationValue .\n" +
            "        ?tempconfig actgu:isDescribedBy ?tempproper .\n" +
            "        ?tempconfig actgu:configured ?actuador .\n" +
            "        # >> Recuperar la configuracion de HUMEDAD\n" +
            "        ?humeproper rdf:type ssn:Property .\n" +
            "        ?humeproper owl:topDataProperty \"" + GestorONTO.properHumedad + "\" .\n" +
            "        ?humeconfig rdf:type actgu:ConfigurationValue .\n" +
            "        ?humeconfig actgu:isDescribedBy ?humeproper .\n" +
            "        ?humeconfig actgu:configured ?actuador .\n" +
            "        # >> Recuperar la configuracion de CONCENTRACIÓN DE GAS\n" +
            "        ?cgasproper rdf:type ssn:Property .\n" +
            "        ?cgasproper owl:topDataProperty \"" + GestorONTO.properConcGas + "\" .\n" +
            "        ?cgasconfig rdf:type actgu:ConfigurationValue .\n" +
            "        ?cgasconfig actgu:isDescribedBy ?cgasproper .\n" +
            "        ?cgasconfig actgu:configured ?actuador .\n" + 
            "        # >> Recuperar la configuracion de VELOCIDAD DEL AIRE\n" +
            "        ?vairproper rdf:type ssn:Property .\n" +
            "        ?vairproper owl:topDataProperty \"" + GestorONTO.properVelAire + "\" .\n" +
            "        ?vairconfig rdf:type actgu:ConfigurationValue .\n" +
            "        ?vairconfig actgu:isDescribedBy ?vairproper .\n" +
            "        ?vairconfig actgu:configured ?actuador .\n" + 
            "        # Extraer valor exacto\n" +
            "        ?tempconfig owl:topDataProperty ?val_temp .\n" +
            "        ?humeconfig owl:topDataProperty ?val_hume .\n" +
            "        ?vairconfig owl:topDataProperty ?val_vaire .\n" +
            "        ?cgasconfig owl:topDataProperty ?val_cgas .\n" +
            "        FILTER ((datatype(?val_temp)) = xsd:double) .\n" +
            "        FILTER ((datatype(?val_hume)) = xsd:double) .\n" +
            "        FILTER ((datatype(?val_vaire)) = xsd:double) .\n" +
            "        FILTER ((datatype(?val_cgas)) = xsd:double) \n" +
            "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getIdDeActuadoresEnOnto() {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
            "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
            "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
            "SELECT REDUCED ?val_actuador \n" +
            "WHERE {\n" +
            "        # Recuperar instancia de actuador\n" +
            "        ?actuador rdf:type actgu:Actuator .\n" +
            "        # Extraer valor exacto\n" +
            "        ?actuador owl:topDataProperty ?val_actuador .\n" +
            "        FILTER ((datatype(?val_actuador)) = xsd:nonNegativeInteger) \n" +
            "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getRolesEnOnto () {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
            "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
            "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
            "SELECT REDUCED ?val_numprioridad ?val_prioridad \n" +
            "WHERE {\n" +
            "        # Recuperar prioridades\n" +
            "        ?prioridad rdf:type actgu:Priority .\n" +
            "        ?numprioridad rdf:type actgu:NumPriority .\n" +
            "        ?numprioridad actgu:numeratePriority ?prioridad .\n" +
            "        # Extraer valor exacto\n" +
            "        ?numprioridad owl:topDataProperty ?val_numprioridad .\n" +
            "        ?prioridad owl:topDataProperty ?val_prioridad .\n" +
            "        FILTER ((datatype(?val_prioridad)) = xsd:string) .\n" +
            "        FILTER ((datatype(?val_numprioridad)) = xsd:nonNegativeInteger) \n" +
            "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getIdDeRopaEnOnto() {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED ?val_numclo\n" +
                "WHERE {\n" +
                "	# >> Recuperar el CLO que viste actualmente cada persona\n" +
                "	?numclo rdf:type actgu:NumClothes .\n" +
                "	# >> Recuperar valor de CLO\n" +
                "	?numclo owl:topDataProperty ?val_numclo .\n" +
                "	FILTER ((datatype(?val_numclo)) = xsd:nonNegativeInteger) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getTiposDeActuadoresEnOnto() {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
            "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
            "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
            "SELECT REDUCED ?val_tipomeca \n" +
            "WHERE {\n" +
            "        # Recuperar instancia de tipo de actuador\n" +
            "        ?tipomeca rdf:type actgu:Type .\n" +
            "        # Extraer valor exacto\n" +
            "        ?tipomeca owl:topDataProperty ?val_tipomeca .\n" +
            "        FILTER ((datatype(?val_tipomeca)) = xsd:string) \n" +
            "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getGruposEnOnto() {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
            "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
            "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
            "SELECT REDUCED ?val_numgrupo ?val_grupo \n" +
            "WHERE {\n" +
            "        # Recuperar instancia de grupo\n" +
            "        ?grupo rdf:type foaf:Group .\n" +
            "        ?numgrupo rdf:type actgu:NumGroup .\n" +
            "        ?numgrupo actgu:numerateGroup ?grupo .\n" +
            "        # Extraer valor exacto\n" +
            "        ?numgrupo owl:topDataProperty ?val_numgrupo .\n" +
            "        ?grupo owl:topDataProperty ?val_grupo .\n" +
            "        FILTER ((datatype(?val_numgrupo)) = xsd:nonNegativeInteger) .\n" +
            "        FILTER ((datatype(?val_grupo)) = xsd:string) \n" +
            "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    
    // Obtener observaciones del ambiente como un Elemento JSON
    private JsonElement getObservationsValueEnOnto() {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
            "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
            "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
            "SELECT REDUCED ?val_temperatura ?val_humedad ?val_congas ?val_velaire\n" +
            "WHERE {\n" +
            "        # >> Recuperar la información sobre el sensor de TEMPERATURA\n" +
            "        ?tempproper rdf:type ssn:Property .\n" +
            "        ?tempproper owl:topDataProperty \"" + GestorONTO.properTempera + "\" .\n" +
            "        ?tempsensor rdf:type ssn:Sensor .\n" +
            "        ?tempsensor ssn:observes ?tempproper .\n" +
            "        ?tempsenout rdf:type ssn:SensorOutput .\n" +
            "        ?tempsenout ssn:isProducedBy ?tempsensor .\n" +
            "        ?temperatura rdf:type ssn:ObservationValue .\n" +
            "        ?tempsenout ssn:hasValue ?temperatura .\n" +
            "        # >> Recuperar la información sobre el sensor de HUMEDAD\n" +
            "        ?humdproper rdf:type ssn:Property .\n" +
            "        ?humdproper owl:topDataProperty \"" + GestorONTO.properHumedad + "\" .\n" +
            "        ?humdsensor rdf:type ssn:Sensor .\n" +
            "        ?humdsensor ssn:observes ?humdproper .   \n" +
            "        ?humdsenout rdf:type ssn:SensorOutput .\n" +
            "        ?humdsenout ssn:isProducedBy ?humdsensor .\n" +
            "        ?humedad rdf:type ssn:ObservationValue .\n" +
            "        ?humdsenout ssn:hasValue ?humedad .\n" +
            "        # >>  Recuperar la información sobre el sensor de CONCENTRACIÓN DE GAS\n" +
            "        ?cgasproper rdf:type ssn:Property .\n" +
            "        ?cgasproper owl:topDataProperty \"" + GestorONTO.properConcGas + "\" .\n" +
            "        ?cgassensor rdf:type ssn:Sensor .\n" +
            "        ?cgassensor ssn:observes ?cgasproper .   \n" +
            "        ?cgassenout rdf:type ssn:SensorOutput .\n" +
            "        ?cgassenout ssn:isProducedBy ?cgassensor .\n" +
            "        ?congas rdf:type ssn:ObservationValue .\n" +
            "        ?cgassenout ssn:hasValue ?congas .\n" +
            "        # >> Recuperar la información sobre el sensor de VELOCIDAD DEL AIRE\n" +
            "        ?vaireproper rdf:type ssn:Property .\n" +
            "        ?vaireproper owl:topDataProperty \"" + GestorONTO.properVelAire + "\" .\n" +
            "        ?vairesensor rdf:type ssn:Sensor .\n" +
            "        ?vairesensor ssn:observes ?vaireproper .   \n" +
            "        ?vairesenout rdf:type ssn:SensorOutput .\n" +
            "        ?vairesenout ssn:isProducedBy ?vairesensor .\n" +
            "        ?velaire rdf:type ssn:ObservationValue .\n" +
            "        ?vairesenout ssn:hasValue ?velaire .\n" +
            "        # >> Datos sobre condiciones ambientales actuales\n" +
            "        ?temperatura owl:topDataProperty ?val_temperatura .\n" +
            "        ?humedad owl:topDataProperty ?val_humedad .\n" +
            "        ?congas owl:topDataProperty ?val_congas .\n" +
            "        ?velaire owl:topDataProperty ?val_velaire .\n" +
            "        FILTER ((datatype(?val_temperatura)) = xsd:double) .\n" +
            "        FILTER ((datatype(?val_humedad)) = xsd:double) .\n" +
            "        FILTER ((datatype(?val_congas)) = xsd:double) .\n" +
            "        FILTER ((datatype(?val_velaire)) = xsd:double)\n" +
            "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    // Obtener ID y MET data de los usuarios como un Elemento JSON
    private JsonElement getIdAndMetDeUsuariosEnOnto() {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
            "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
            "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
            "SELECT REDUCED ?val_persona ?val_met\n" +
            "WHERE {\n" +
            "        # >> Recuperar instancias de las personas\n" +
            "        ?persona rdf:type foaf:Person .\n" +
            "        # Recuperar el valor MET que tiene actualmente cada persona\n" +
            "        ?met rdf:type actgu:MetabolicRate .\n" +
            "        ?met actgu:isPhysicalWearOf ?persona .\n" +
            "        # >> Recuperar el dato de las variables consultadas\n" +
            "        ?persona owl:topDataProperty ?val_persona .\n" +
            "        ?met owl:topDataProperty ?val_met .\n" +
            "        FILTER ((datatype(?val_persona)) = xsd:nonNegativeInteger) .\n" +
            "        FILTER ((datatype(?val_met)) = xsd:double)\n" +
            "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    // Obtener ID y MET data de los usuarios como un Elemento JSON
    private JsonElement getPmvDeUsuarioEnOnto (String idUsuario) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED ?val_pmv \n" +
                "WHERE {\n" +
                "       # >> Recuperar instancias de las personas relacionadas al grupo\n" +
                "       ?persona rdf:type foaf:Person .\n" +
                "	?persona owl:topDataProperty " + idUsuario + " .\n" +
                "	# >> Recuperar el PMV que tiene actualmente cada persona\n" +
                "	?pmv rdf:type actgu:ThermalComfort .\n" +
                "	?persona actgu:has ?pmv .\n" +
                "	# >> Datos de Confort Térmico\n" +
                "	?pmv owl:topDataProperty ?val_pmv .\n" +
                "	FILTER ((datatype(?val_pmv)) = xsd:string) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    // Obtener ID y MET data de los usuarios como un Elemento JSON
    private JsonElement getBmiDeUsuarioEnOnto (String idUsuario) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED ?val_bmi \n" +
                "WHERE {\n" +
                "       # >> Recuperar instancias de las personas relacionadas al grupo\n" +
                "       ?persona rdf:type foaf:Person .\n" +
                "	?persona owl:topDataProperty " + idUsuario + " .\n" +
                "	# >> Recuperar la información antropometrica de cada persona\n" +
                "	?atropometrico rdf:type actgu:Anthropometry .\n" +
                "	?atropometrico actgu:characterizes ?persona .\n" +
                "	?unidadmed rdf:type dul2:UnitOfMeasure .\n" +
                "	?unidadmed owl:topDataProperty \"" + GestorONTO.unidadBMI + "\" .\n" +
                "	?bmi rdf:type actgu:AnthropometricValue .\n" +
                "	?atropometrico actgu:hasValue ?bmi .\n" +
                "	?bmi actgu:isClassifiedBy ?unidadmed .\n" +
                "	# >> Datos sobre la persona\n" +
                "	?bmi owl:topDataProperty ?val_bmi  .\n" +
                "	FILTER ((datatype(?val_bmi)) = xsd:double) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    // Obtener ID y MET data de los usuarios como un Elemento JSON
    private JsonElement getConcentracionDeGasEnOnto () {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED ?val_congas \n" +
                "WHERE {\n" +
                "	# >> Recuperar la información sobre el sentor de CONCENTRACIÓN DE GAS\n" +
                "	?cgasproper rdf:type ssn:Property .\n" +
                "	?cgasproper owl:topDataProperty \"" + GestorONTO.properConcGas + "\" .\n" +
                "	?cgassensor rdf:type ssn:Sensor .\n" +
                "	?cgassensor ssn:observes ?cgasproper .\n" +
                "	?cgassenout rdf:type ssn:SensorOutput .\n" +
                "	?cgassenout ssn:isProducedBy ?cgassensor .\n" +
                "	?congas rdf:type ssn:ObservationValue .\n" +
                "	?cgassenout ssn:hasValue ?congas .\n" +
                "	# >> Recuperar el dato de las variables consultadas\n" +
                "	?congas owl:topDataProperty ?val_congas .\n" +
                "	FILTER ((datatype(?val_congas)) = xsd:double) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    
    private JsonElement getDataUsuarioDeGrupoEnOnto (String idGrupo) {
        // Ejecutar consulta SPARQL
        JsonElement jsonElement = instanciaJENA.doSparqlQuery(
                "PREFIX base: <http://purl.oclc.org/NET/ssnx/ssn>\n" +
                "PREFIX actgu: <http://www.msicu.onto/actgu#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dul1: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" + "PREFIX dul2: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
                "SELECT REDUCED ?val_persona \n" +
                "WHERE {\n" +
                "	# >> Recuperar instancia de id de grupo\n" +
                "	?numgrp rdf:type actgu:NumGroup .\n" +
                "	?numgrp owl:topDataProperty " + idGrupo + " .\n" +
                "	# >> Recuperar instancia de nombre de grupo\n" +
                "	?grupo rdf:type foaf:Group .\n" +
                "	?numgrp actgu:numerateGroup ?grupo .\n" +
                "	# >> Recupera a toda persona del grupo\n" +
                "	?persona rdf:type foaf:Person .\n" +
                "	?grupo foaf:member ?persona .\n" +
                "	# << Recupera el valor de la persona\n" +
                "	?persona owl:topDataProperty ?val_persona .\n" +
                "	FILTER ((datatype(?val_persona)) = xsd:nonNegativeInteger) \n" +
                "}");
        // Regresar Elemento JSON resultante
        return jsonElement;
    }
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    
    private JsonObject cloneJsonObject(JsonObject jsObj) {
        // Variable
        JsonObject jsResu = new JsonObject();
        // Recuperar llaves de instancia JSON
        Set<Map.Entry<String, JsonElement>> entries = jsObj.entrySet();
        // Recorrer llaves de instancia JSON
        for (Map.Entry<String, JsonElement> entry: entries) {
            // Recuperar llave y valor
            String strKeyRS = entry.getKey().replaceAll("\"", "");
            String strValRS = entry.getValue().toString().replaceAll("\"", "");
            // Armar segmentos JSON
            jsResu.addProperty(strKeyRS, strValRS);
        }
        // Devolver JSON
        return jsResu;
    }
}
