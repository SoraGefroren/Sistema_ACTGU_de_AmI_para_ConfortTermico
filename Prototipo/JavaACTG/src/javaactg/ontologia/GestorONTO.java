/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.ontologia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.FileWriter;
import javaactg.Principal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javaactg.conforttermico.PMV;
import javaactg.conforttermico.ConfortTermico;
import javaactg.ontologia.madegu.ControladorConfort;
import javaactg.ontologia.modelo.InstanciaJENA;
import javaactg.ontologia.modelo.ModeloACTGU;

/**
 *
 * @author Jorge
 */
public class GestorONTO {
    // Elementos para crear la Ontología ACTGU
    private ModeloACTGU ontoMDL = new ModeloACTGU();
    // Intancia de JENA (La Ontologia)
    private InstanciaJENA instanciaJENA = null;
    // Propiedades
    public static final String properHumedad = "humidity";
    public static final String properTempera = "temperature";
    public static final String properVelAire = "air velocity";
    public static final String properConcGas = "gas concentration";
    public static final String properCLO = "insulation of clothes";
    public static final String properMET = "metabolic rate";
    public static final String properWeight = "weight";
    public static final String properHeight = "height";
    public static final String properBMI = "body mass index";
    // Unidades de medida
    public static final String unidadVelAire = "metre per second";
    public static final String unidadHumedad = "percentage";
    public static final String unidadTempera = "celsius";
    public static final String unidadConcGas = "parts per million";
    public static final String unidadMET = "met";
    public static final String unidadCLO = "clo";
    public static final String unidadEdad = "years";
    public static final String unidadWeight = "kilograms";
    public static final String unidadHeight = "meters";
    public static final String unidadBMI = "bmi";
    // Sensores
    public static final String sensorHumedad = "3";
    public static final String sensorTempera = "2";
    public static final String sensorVelAire = "4";
    public static final String sensorConcGas = "1";
    
    // Variable que describe al lugar de estudio
    public static final String noumEnviront = "class room";
    
    // Constructor
    public GestorONTO() {
        // Inicia la ontologia
        reconstruirOntologia();
    }
    
    // Función que inicializa la Onto, con información estandar
    private void inicializarDataEstandar() {
        // Elementos estandar referentes al ambiente
        instanciaJENA.addIndividual(ontoMDL.getUriClass("Property"), ontoMDL.getXSDType("Property"), properVelAire);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getXSDType("UnitOfMeasure"), unidadVelAire);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("Property"), ontoMDL.getXSDType("Property"), properHumedad);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getXSDType("UnitOfMeasure"), unidadHumedad);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("Property"), ontoMDL.getXSDType("Property"), properTempera);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getXSDType("UnitOfMeasure"), unidadTempera);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("Property"), ontoMDL.getXSDType("Property"), properConcGas);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getXSDType("UnitOfMeasure"), unidadConcGas);
        // Elementos estandar referentes aspectos dinamicos del usuario
        instanciaJENA.addIndividual(ontoMDL.getUriClass("Property"), ontoMDL.getXSDType("Property"), properMET);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getXSDType("UnitOfMeasure"), unidadMET);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("Property"), ontoMDL.getXSDType("Property"), properCLO);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getXSDType("UnitOfMeasure"), unidadCLO);
        // Elementos estandar referentes aspectos estaticos del usuario
        instanciaJENA.addIndividual(ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getXSDType("UnitOfMeasure"), unidadEdad);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("Property"), ontoMDL.getXSDType("Property"), properWeight);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getXSDType("UnitOfMeasure"), unidadWeight);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("Property"), ontoMDL.getXSDType("Property"), properHeight);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getXSDType("UnitOfMeasure"), unidadHeight);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("Property"), ontoMDL.getXSDType("Property"), properBMI);
        instanciaJENA.addIndividual(ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getXSDType("UnitOfMeasure"), unidadBMI);
        // ------------------------------------------------------
        // Agregar posibles índices de CONFORT TÉRMICO
        instanciaJENA.addIndividual(ontoMDL.getUriClass("ThermalComfort"), ontoMDL.getXSDType("ThermalComfort"), "-3");
        instanciaJENA.addIndividual(ontoMDL.getUriClass("ThermalComfort"), ontoMDL.getXSDType("ThermalComfort"), "-2");
        instanciaJENA.addIndividual(ontoMDL.getUriClass("ThermalComfort"), ontoMDL.getXSDType("ThermalComfort"), "-1");
        instanciaJENA.addIndividual(ontoMDL.getUriClass("ThermalComfort"), ontoMDL.getXSDType("ThermalComfort"), "0");
        instanciaJENA.addIndividual(ontoMDL.getUriClass("ThermalComfort"), ontoMDL.getXSDType("ThermalComfort"), "+1");
        instanciaJENA.addIndividual(ontoMDL.getUriClass("ThermalComfort"), ontoMDL.getXSDType("ThermalComfort"), "+2");
        instanciaJENA.addIndividual(ontoMDL.getUriClass("ThermalComfort"), ontoMDL.getXSDType("ThermalComfort"), "+3");
        // ------------------------------------------------------
        // Agregar estado de APAGADO
        instanciaJENA.addIndividual(ontoMDL.getUriClass("State"), ontoMDL.getXSDType("State"), "a");
        // Agregar estado de ENCENDIDO
        instanciaJENA.addIndividual(ontoMDL.getUriClass("State"), ontoMDL.getXSDType("State"), "e");
        // ------------------------------------------------------
        // Agregar Genero de MUJER
        instanciaJENA.addIndividual(ontoMDL.getUriClass("Gender"), ontoMDL.getXSDType("Gender"), "m");
        // Agregar Genero de HOMBRE
        instanciaJENA.addIndividual(ontoMDL.getUriClass("Gender"), ontoMDL.getXSDType("Gender"), "h");
        // ------------------------------------------------------
        // Variable de apoyo para evitar elementos repetidos
        ArrayList<String> aryListElemts = new ArrayList<String>();
        // ------------------------------------------------------
        // Recuperar Arreglo de datos sobre PRIORIDADES
        JsonArray jsAry = Principal.gestorCRUD.recuperarDataRoles();
        int tamJsonAry = jsAry.size();
        JsonObject jsObj = null;
        // Recorrer Arreglo JSON con datos
        for (int x = 0; x < tamJsonAry; x ++) {
            // Crear instancia de objeto JSON
            jsObj = jsAry.get(x).getAsJsonObject();
            // Agregar NOUN  de PRIORIDAD (* Se agrega su forma númerica)
            String priority = jsObj.get("rol").toString().replaceAll("\"", "").trim().trim();
            // Validar que no se agrege un elemento repetido
            if (!aryListElemts.contains("Priority_" + priority)) {
                // Agregar elemento para no repetirlo
                aryListElemts.add("Priority_" + priority);
                // Agregar valor de ELEMENTO
                instanciaJENA.addIndividual(ontoMDL.getUriClass("Priority"), ontoMDL.getXSDType("Priority"), priority);
                // Agregar ID de PRIORIDAD
                instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("NumPriority"), ontoMDL.getXSDType("NumPriority"),
                        jsObj.get("id_rol").toString().replaceAll("\"", "").toLowerCase(),
                        new String[]{
                            // Construye uri para las relaciones actuales
                            ontoMDL.getURIofPrefix("actgu") + "numeratePriority"
                        },
                        new String[]{
                            // Recuperar uri de clase
                            ontoMDL.getUriClass("Priority")
                        },
                        new String[]{
                            // Valores de clase a relacionar
                            priority
                        });
            }
        }
        // ------------------------------------------------------
        // Recuperar Arreglo de datos sobre TIPOS DE EQUIPAMIENTOS
        jsAry = Principal.gestorCRUD.recuperarDataTiposEqpmts();
        tamJsonAry = jsAry.size();
        jsObj = null;
        // Recorrer Arreglo JSON con datos
        for (int x = 0; x < tamJsonAry; x ++) {
            // Crear instancia de objeto JSON
            jsObj = jsAry.get(x).getAsJsonObject();
            // Recuperar datos de instancia de objeto JSON
            String valDeClo = jsObj.get("valor_clo").toString().replaceAll("\"", "").toLowerCase();
            String valClo_M = jsObj.get("suma_clo_para_m").toString().replaceAll("\"", "").toLowerCase();
            String valClo_H = jsObj.get("suma_clo_para_h").toString().replaceAll("\"", "").toLowerCase();
            // Validar que no se agrege un elemento repetido
            if (!aryListElemts.contains("CloValue_" + valDeClo)) {
                // Agregar elemento para no repetirlo
                aryListElemts.add("CloValue_" + valDeClo);
                // Agregar valor de ELEMENTO de CLO
                instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("CloValue"), ontoMDL.getXSDType("CloValue"), valDeClo,
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("actgu") + "isClassifiedBy", ontoMDL.getURIofPrefix("actgu") + "isDescribedBy" },
                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                        ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getUriClass("Property") },
                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                        unidadCLO, properCLO });
            }
            // Validar que no se agrege un elemento repetido
            if (!aryListElemts.contains("CloValue_" + valClo_M)) {
                // Agregar elemento para no repetirlo
                aryListElemts.add("CloValue_" + valClo_M);
                // Agregar valor de ELEMENTO de CLO para MUJER
                instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("CloValue"), ontoMDL.getXSDType("CloValue"), valClo_M,
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("actgu") + "isClassifiedBy", ontoMDL.getURIofPrefix("actgu") + "isDescribedBy" },
                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                        ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getUriClass("Property") },
                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                        unidadCLO, properCLO });
            }
            // Validar que no se agrege un elemento repetido
            if (!aryListElemts.contains("CloValue_" + valClo_H)) {
                // Agregar elemento para no repetirlo
                aryListElemts.add("CloValue_" + valClo_H);
                // Agregar valor de ELEMENTO de CLO para HOMBRE
                instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("CloValue"), ontoMDL.getXSDType("CloValue"), valClo_H,
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("actgu") + "isClassifiedBy", ontoMDL.getURIofPrefix("actgu") + "isDescribedBy" },
                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                        ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getUriClass("Property") },
                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                        unidadCLO, properCLO });
            }
            // Recuperar datos de instancia de objeto JSON
            String descpClo = ":" + jsObj.get("descripcion").toString().replaceAll("\"", "").toLowerCase();
            String descClo_M = "m:" + jsObj.get("descripcion_para_m").toString().replaceAll("\"", "").toLowerCase();
            String descClo_H = "h:" + jsObj.get("descripcion_para_h").toString().replaceAll("\"", "").toLowerCase();
            // Validar que no se agrege un elemento repetido
            if (!aryListElemts.contains("Clothes_" + descpClo)) {
                // Agregar elemento para no repetirlo
                aryListElemts.add("Clothes_" + descpClo);
                // Agregar valor de ELEMENTO de VALOR de CLO
                instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("Clothes"), ontoMDL.getXSDType("Clothes"), descpClo,
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("actgu") + "equippeWith" },
                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                        ontoMDL.getUriClass("CloValue") },
                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                        valDeClo });
            } else {
                // Agregar valor de ELEMENTO de VALOR de CLO
                instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("Clothes"), descpClo,
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("actgu") + "equippeWith" },
                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                        ontoMDL.getUriClass("CloValue") },
                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                        valDeClo });
            }
            // Validar que no se agrege un elemento repetido
            if (!aryListElemts.contains("Clothes_" + descClo_M)) {
                // Agregar elemento para no repetirlo
                aryListElemts.add("Clothes_" + descClo_M);
                // Agregar valor de ELEMENTO de VALOR de CLO
                instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("Clothes"), ontoMDL.getXSDType("Clothes"), descClo_M,
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("actgu") + "equippeWith" },
                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                        ontoMDL.getUriClass("CloValue") },
                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                        valClo_M });
            } else {
                // Agregar elemento para no repetirlo
                aryListElemts.add("Clothes_" + descClo_M);
                // Agregar valor de ELEMENTO de VALOR de CLO
                instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("Clothes"), descClo_M,
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("actgu") + "equippeWith" },
                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                        ontoMDL.getUriClass("CloValue") },
                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                        valClo_M });
            }
            // Validar que no se agrege un elemento repetido
            if (!aryListElemts.contains("Clothes_" + descClo_H)) {
                // Agregar elemento para no repetirlo
                aryListElemts.add("Clothes_" + descClo_H);
                // Agregar valor de ELEMENTO de VALOR de CLO
                instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("Clothes"), ontoMDL.getXSDType("Clothes"), descClo_H,
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("actgu") + "equippeWith" },
                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                        ontoMDL.getUriClass("CloValue") },
                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                        valClo_H });
            } else {
                // Agregar elemento para no repetirlo
                aryListElemts.add("Clothes_" + descClo_H);
                // Agregar valor de ELEMENTO de VALOR de CLO
                instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("Clothes"), descClo_H,
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("actgu") + "equippeWith" },
                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                        ontoMDL.getUriClass("CloValue") },
                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                        valClo_H });
            }
            // Recuperar datos de instancia de objeto JSON
            String valNomEq = jsObj.get("nombre_equipamiento").toString().replaceAll("\"", "").trim();
            // Validar que no se agrege un elemento repetido
            if (!aryListElemts.contains("Label_" + valNomEq)) {
                // Agregar elemento para no repetirlo
                aryListElemts.add("Label_" + valNomEq);
                // Agregar valor de ELEMENTO de VALOR de CLO
                instanciaJENA.addIndividual(ontoMDL.getUriClass("Label"), ontoMDL.getXSDType("Label"), valNomEq);
            }
            // Recuperar valor ID de la ROPA
            String numCloEquip = jsObj.get("id_tipo_de_equipamiento").toString().replaceAll("\"", "").toLowerCase();
            // Validar que no se agrege un elemento repetido
            if (!aryListElemts.contains("NumClothes_" + numCloEquip)) {
                // Agregar elemento para no repetirlo
                aryListElemts.add("NumClothes_" + numCloEquip);
                // Agregar ID de TIPO DE EQUIPAMIENTO
                instanciaJENA.addIndividual(ontoMDL.getUriClass("NumClothes"), ontoMDL.getXSDType("NumClothes"), numCloEquip);
            }
            // Validar que no se agrege un elemento repetido
            if (!aryListElemts.contains("LabelEquip_" + numCloEquip + "_" + valNomEq)) {
                // Agregar elemento para no repetirlo
                aryListElemts.add("LabelEquip_" + numCloEquip + "_" + valNomEq);
                // Agregar ID de TIPO DE EQUIPAMIENTO
                instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("NumClothes"), numCloEquip,
                        new String[]{ // Construye uri para las relaciones actuales
                            ontoMDL.getURIofPrefix("actgu") + "subscribe" },
                        new String[]{ // Recuperar uri de clase
                            ontoMDL.getUriClass("Label") },
                        new String[]{ // Valores de clase a relacionar
                            valNomEq });
            }
            // Validar que no se agrege un elemento repetido
            if (!aryListElemts.contains("NumClothes_" + numCloEquip + "_" + descpClo)) {
                // Agregar elemento para no repetirlo
                aryListElemts.add("NumClothes_" + numCloEquip + "_" + descpClo);
                // Agregar ID de TIPO DE EQUIPAMIENTO
                instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("NumClothes"), numCloEquip,
                        new String[]{ // Construye uri para las relaciones actuales
                            ontoMDL.getURIofPrefix("actgu") + "numerateClothes" },
                        new String[]{ // Recuperar uri de clase
                            ontoMDL.getUriClass("Clothes") },
                        new String[]{ // Valores de clase a relacionar
                            descpClo });
            }
            // Validar que no se agrege un elemento repetido
            if (!aryListElemts.contains("NumClothes_" + numCloEquip + "_" + descClo_M)) {
                // Agregar elemento para no repetirlo
                aryListElemts.add("NumClothes_" + numCloEquip + "_" + descClo_M);
                // Agregar ID de TIPO DE EQUIPAMIENTO
                instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("NumClothes"), numCloEquip,
                        new String[]{ // Construye uri para las relaciones actuales
                            ontoMDL.getURIofPrefix("actgu") + "numerateClothes" },
                        new String[]{ // Recuperar uri de clase
                            ontoMDL.getUriClass("Clothes") },
                        new String[]{ // Valores de clase a relacionar
                            descClo_M });
            }
            // Validar que no se agrege un elemento repetido
            if (!aryListElemts.contains("NumClothes_" + numCloEquip + "_" + descClo_H)) {
                // Agregar elemento para no repetirlo
                aryListElemts.add("NumClothes_" + numCloEquip + "_" + descClo_H);
                // Agregar ID de TIPO DE EQUIPAMIENTO
                instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("NumClothes"), numCloEquip,
                        new String[]{ // Construye uri para las relaciones actuales
                            ontoMDL.getURIofPrefix("actgu") + "numerateClothes" },
                        new String[]{ // Recuperar uri de clase
                            ontoMDL.getUriClass("Clothes") },
                        new String[]{ // Valores de clase a relacionar
                            descClo_H });
            }
        }
        // ------------------------------------------------------
        // Recuperar Arreglo de datos sobre TIPOS DE HVACs
        jsAry = Principal.gestorCRUD.recuperarDataTiposHVAC();
        tamJsonAry = jsAry.size();
        jsObj = null;
        // Recorrer Arreglo JSON con datos
        for (int x = 0; x < tamJsonAry; x ++) {
            // Crear instancia de objeto JSON
            jsObj = jsAry.get(x).getAsJsonObject();
            // Recuperar valor de objeto JSON
            String strTipoHvac = jsObj.get("tipo_de_mecanismo").toString().replaceAll("\"", "").toLowerCase();
            // Validar si el Elemento ya existe
            if (!aryListElemts.contains("Type_" + strTipoHvac)) {
                // Agregar Elemento para no repetir
                aryListElemts.add("Type_" + strTipoHvac);
                // Agregar Elemento de TIPO
                instanciaJENA.addIndividual(ontoMDL.getUriClass("Type"), ontoMDL.getXSDType("Type"), strTipoHvac);
                // Agregar ID del Elemento
                instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("NumType"), ontoMDL.getXSDType("NumType"),
                    jsObj.get("id_tipo_de_mecanismo_hvac").toString().replaceAll("\"", "").toLowerCase(),
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("actgu") + "numerateType" },
                    new String[]{ // Recuperar uri de clase
                        ontoMDL.getUriClass("Type") },
                    new String[]{ // Valores de clase a relacionar
                        strTipoHvac });
            }
        }
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Función que reconstruiye la onto, poniendola en un estado integro
    public void reconstruirOntologia() {
        // Generar documento-base OWL y crear una instancia de JENA
        instanciaJENA = new InstanciaJENA(ontoMDL.makeOWLString(), ontoMDL.getURIofDoc());
        // Inicializar onto con información estandar
        inicializarDataEstandar();
        // Inicializar onto con información de la BD
        reiniciarDataDeBD();
    }
    
    // Función que reinicia la onto, con información de la BD
    public void reiniciarDataDeBD() {
        // Inicializar onto con información de la BD
        reiniciarDataDeHVACs();
        reiniciarDataDeGrps();
        reiniciarDataDeUsrs();
        // Inicializar onto con información del Ambiente
        reiniciarDataDeAmb();
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Función que elimina en la onto, información de la BD sobre HVACs
    private void limpiarDataDeHVACs() {
        // Adicionales
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Description"));
        // Remover todos los elementos individuales de la clase
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Actuator"));
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("ConfigurationValue"));
    }
    
    // Función que agrega a la onto, información de la BD sobre HVACs
    private void reiniciarDataDeHVACs() {
        // Eliminar de la Onto, data que pueda interferir
        limpiarDataDeHVACs();
        // ------------------------------------------------------
        // Variable de apoyo para evitar elementos repetidos
        ArrayList<String> aryListElemts = new ArrayList<String>();
        // ------------------------------------------------------
        // Recuperar Arreglo de datos sobre HVACs
        JsonArray jsAry = Principal.gestorCRUD.recuperarDataHVACs();
        int tamJsonAry = jsAry.size();
        JsonObject jsObj = null;
        // Recorrer Arreglo JSON con datos
        for (int x = 0; x < tamJsonAry; x ++) {
            // Crear instancia de objeto JSON
            jsObj = jsAry.get(x).getAsJsonObject();
            // Recuperar valores de objeto JSON
            String idMcaHVAC = jsObj.get("id_mecanismo_hvac").toString().replaceAll("\"", "").toLowerCase();
            // Agregar HVAC
            instanciaJENA.addIndividualRelated(
                    ontoMDL.getUriClass("Actuator"), ontoMDL.getXSDType("Actuator"), idMcaHVAC,
                    new String[]{
                        // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("actgu") + "present",
                        ontoMDL.getURIofPrefix("actgu") + "kind"
                    },
                    new String[]{
                        // Recuperar uri de clase sobre el ESTADO y TIPO DE HVAC
                        ontoMDL.getUriClass("State"),
                        ontoMDL.getUriClass("Type")
                    },
                    new String[]{
                        // Obtiene valor de ESTADO (a|e) y TIPO DE HVAC
                        jsObj.get("estado").toString().replaceAll("\"", "").toLowerCase(),
                        jsObj.get("tipo_de_mecanismo").toString().replaceAll("\"", "").toLowerCase()
                    });
            // Recuperar valores de objeto JSON
            String[] valsAmbt = new String [] {
                fixDblAsStr(jsObj.get("humedad").toString().replaceAll("\"", "").toLowerCase()),
                fixDblAsStr(jsObj.get("temperatura").toString().replaceAll("\"", "").toLowerCase()),
                fixDblAsStr(jsObj.get("velocidad_del_aire").toString().replaceAll("\"", "").toLowerCase()),
                fixDblAsStr(jsObj.get("concentracion_de_gas").toString().replaceAll("\"", "").toLowerCase()) };
            // Formar datos para análisis
            String[] valsPropers = new String[] {properHumedad, properTempera, properVelAire, properConcGas};
            String[] valsUnidads = new String[] {unidadHumedad, unidadTempera, unidadVelAire, unidadConcGas};
            // Recorrer valores agrupados de Objeto JSON
            for (int i = 0; i < 4; i++) {
                // Validar que no se agrege un elemento repetido
                if (!aryListElemts.contains("ConfigurationValue_" + valsAmbt[i])) {
                    // Agregar elemento para no repetirlo
                    aryListElemts.add("ConfigurationValue_" + valsAmbt[i]);
                    // Agregar valor de configuración
                    instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("ConfigurationValue"), ontoMDL.getXSDType("ConfigurationValue"), valsAmbt[i],
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "isClassifiedBy",
                                ontoMDL.getURIofPrefix("actgu") + "isDescribedBy",
                                ontoMDL.getURIofPrefix("actgu") + "configured"
                            },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("UnitOfMeasure"),
                                ontoMDL.getUriClass("Property"),
                                ontoMDL.getUriClass("Actuator")
                            },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                valsUnidads[i],
                                valsPropers[i],
                                idMcaHVAC
                            });
                } else {
                    // Agregar valor de configuración
                    instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("ConfigurationValue"), valsAmbt[i],
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "isClassifiedBy",
                                ontoMDL.getURIofPrefix("actgu") + "isDescribedBy",
                                ontoMDL.getURIofPrefix("actgu") + "configured"
                            },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("UnitOfMeasure"),
                                ontoMDL.getUriClass("Property"),
                                ontoMDL.getUriClass("Actuator")
                            },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                valsUnidads[i],
                                valsPropers[i],
                                idMcaHVAC
                            });
                }
            }
        }
    }
    
    // Función que elimina en la onto, información de la BD sobre Grupos
    private void limpiarDataDeGrps() {
        // Adicionales
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Description"));
        // Remover todos los elementos individuales de la clase
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("NumGroup"));
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Group"));
    }
    
    // Función que agrega a la onto, información de la BD sobre Grupos
    private void reiniciarDataDeGrps() {
        // Eliminar de la Onto, data que pueda interferir
        limpiarDataDeGrps();
        // ------------------------------------------------------
        // Variable de apoyo para evitar elementos repetidos
        ArrayList<String> aryListElemts = new ArrayList<String>();
        // ------------------------------------------------------
        // Recuperar Arreglo de datos sobre GRUPOS
        JsonArray jsAry = Principal.gestorCRUD.recuperarDataGrupos();
        int tamJsonAry = jsAry.size();
        JsonObject jsObj = null;
        // Recorrer Arreglo JSON con datos
        for (int x = 0; x < tamJsonAry; x ++) {
            // Crear instancia de objeto JSON
            jsObj = jsAry.get(x).getAsJsonObject();
            // Recuperar el nombre del grupo
            String nomGrup = jsObj.get("nombre").toString().replaceAll("\"", "").trim();
            // Validar si es un ELEMENTO repetido
            if (!aryListElemts.contains("Group_" + nomGrup)) {
                aryListElemts.add("Group_" + nomGrup);
                // Agregar GRUPOS (* Se agrega su forma númerica)
                instanciaJENA.addIndividual(ontoMDL.getUriClass("Group"), ontoMDL.getXSDType("Group"), nomGrup);
                // Agregar individual relacionado
                instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("NumGroup"), ontoMDL.getXSDType("NumGroup"),
                        jsObj.get("id_grupo").toString().replaceAll("\"", "").toLowerCase(),
                        new String[]{ // Construye uri para las relaciones actuales
                            ontoMDL.getURIofPrefix("actgu") + "numerateGroup" },
                        new String[]{ // Recuperar uri de clase para las relaciones actuales
                            ontoMDL.getUriClass("Group") },
                        new String[]{ // Establece ELEMENTO al cual relacionarse
                            nomGrup });
            }
        }
    }
    
    // Función que elimina en la onto, información de la BD sobre Usuarios
    private void limpiarDataDeUsrs() {
        // Adicionales
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Description"));
        // Remover todos los elementos individuales de la clase
        // *****************************************************
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Person"));
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Age"));
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Demographic"));
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Anthropometry"));
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("AnthropometricValue"));
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("InsulationValue"));
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("MetabolicRate"));
        // *****************************************************
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Tendency"));
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Condition"));
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("SlopeOfComfort"));
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("ComfortTrend"));
    }
    
    // Función que agrega a la onto, información de la BD sobre Usuarios
    private void reiniciarDataDeUsrs() {
        // Eliminar de la Onto, data que pueda interferir
        limpiarDataDeUsrs();
        // ** Solo agregar valor de BMI para un usuario
        // ** Cuidar que no se asigne más de un MET a un usuario
        // *****************************************************
        // ------------------------------------------------------
        // Variable de apoyo para evitar elementos repetidos
        ArrayList<String> aryListElemts = new ArrayList<String>();
        // ------------------------------------------------------
        // Recuperar Arreglo de datos sobre USUARIOS
        JsonArray jsAry = Principal.gestorCRUD.recuperarDataUsuarios();
        int tamJsonAry = jsAry.size();
        JsonObject jsObj = null;
        // Recorrer Arreglo JSON con datos
        for (int x = 0; x < tamJsonAry; x ++) {
            // Crear instancia de objeto JSON
            jsObj = jsAry.get(x).getAsJsonObject();
            // Recupera valor de USUARIO
            String idPersona = jsObj.get("id_usuario").toString().replaceAll("\"", "").toLowerCase();
            // Validar si es un ELEMENTO repetido
            if (!aryListElemts.contains("Person_" + idPersona)) {
                // Agrega intancia de PERSONA
                aryListElemts.add("Person_" + idPersona);
                instanciaJENA.addIndividual(ontoMDL.getUriClass("Person"), ontoMDL.getXSDType("Person"), idPersona);
                // ************
                // Recupera valores de usuario
                String sexo = jsObj.get("sexo").toString().replaceAll("\"", "").toLowerCase();
                String edad = jsObj.get("edad").toString().replaceAll("\"", "").toLowerCase();
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("Age_" + edad)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("Age_" + edad);
                    instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("Age"), ontoMDL.getXSDType("Age"), edad,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "isClassifiedBy" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("UnitOfMeasure") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                unidadEdad });
                }
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("Demographic_" + sexo + "_" + edad)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("Demographic_" + sexo + "_" + edad);
                    instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("Demographic"), ontoMDL.getXSDType("Demographic"), sexo + "_" + edad,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "is", ontoMDL.getURIofPrefix("actgu") + "is",
                                ontoMDL.getURIofPrefix("actgu") + "characterizes" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("Gender"), ontoMDL.getUriClass("Age"),
                                ontoMDL.getUriClass("Person") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                sexo, edad,
                                idPersona });
                } else {
                    // Actualizar: a instancia de ANTROPOMETRIA con sus relaciones
                    instanciaJENA.addRelsInIndividual(
                            ontoMDL.getUriClass("Demographic"), sexo + "_" + edad,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "characterizes" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("Person") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                idPersona });
                }
                // ************
                // Recupera valores de usuario
                double peso = Double.parseDouble(jsObj.get("peso").toString().replaceAll("\"", "").toLowerCase());
                double altura = Double.parseDouble(jsObj.get("altura").toString().replaceAll("\"", "").toLowerCase());
                String valPesoAltura = String.format("%.2f", peso) + "_" + String.format("%.2f", altura);
                String valorBMI = String.format("%.2f", calcularBMI(peso, altura));
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("AnthropometricValue_" + valorBMI)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("AnthropometricValue_" + valorBMI);
                    instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("AnthropometricValue"), ontoMDL.getXSDType("AnthropometricValue"), valorBMI,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "isClassifiedBy", ontoMDL.getURIofPrefix("actgu") + "isDescribedBy" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getUriClass("Property") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                unidadBMI, properBMI });
                }
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("Anthropometry_" + valPesoAltura)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("Anthropometry_" + valPesoAltura);
                    instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("Anthropometry"), ontoMDL.getXSDType("Anthropometry"), valPesoAltura,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "hasValue", ontoMDL.getURIofPrefix("actgu") + "characterizes" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("AnthropometricValue"), ontoMDL.getUriClass("Person") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                valorBMI, idPersona });
                } else {
                    // Agregar intancia con relaciones
                    aryListElemts.add("Anthropometry_" + valPesoAltura);
                    instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("Anthropometry"), valPesoAltura,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "characterizes" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("Person") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                idPersona });
                }
                // ************
                // Recupera valores de usuario
                String valorCLO = String.format("%.2f", calcularCLO( sexo,
                        Double.parseDouble(jsObj.get("valor_clo_equipado").toString().replaceAll("\"", "").toLowerCase()),
                        Double.parseDouble(jsObj.get("suma_clo_para_h_equipado").toString().replaceAll("\"", "").toLowerCase()),
                        Double.parseDouble(jsObj.get("suma_clo_para_m_equipado").toString().replaceAll("\"", "").toLowerCase())));
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("InsulationValue_" + valorCLO)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("InsulationValue_" + valorCLO);
                    instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("InsulationValue"), ontoMDL.getXSDType("InsulationValue"), valorCLO,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "isClassifiedBy", ontoMDL.getURIofPrefix("actgu") + "isDescribedBy",
                                ontoMDL.getURIofPrefix("actgu") + "isEquippedBy" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getUriClass("Property"),
                                ontoMDL.getUriClass("Person") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                unidadCLO, properCLO,
                                idPersona });
                } else {
                    // Actualizar: a instancia de ANTROPOMETRIA con sus relaciones
                    instanciaJENA.addRelsInIndividual(
                            ontoMDL.getUriClass("InsulationValue"), valorCLO,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "isEquippedBy" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("Person") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                idPersona });
                }
                // ************
                // Recuperar valores TOP
                double condicionTop = Double.parseDouble(jsObj.get("condicion_top").toString().replaceAll("\"", "").toLowerCase());
                // Tendencia - considerando un valor de -50 y a 50
                double tendenciaTop = Double.parseDouble(jsObj.get("tendencia_top").toString().replaceAll("\"", "").toLowerCase());
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("Condition_" + condicionTop)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("Condition_" + condicionTop);
                    instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("Condition"), ontoMDL.getXSDType("Condition"), condicionTop + "",
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "isClassifiedBy", ontoMDL.getURIofPrefix("actgu") + "isDescribedBy" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getUriClass("Property") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                unidadTempera, properTempera });
                }
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("Tendency_" + tendenciaTop)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("Tendency_" + tendenciaTop);
                    instanciaJENA.addIndividual(ontoMDL.getUriClass("Tendency"), ontoMDL.getXSDType("Tendency"), tendenciaTop + "");
                }
                // Formar valor de vector
                String vectrTop = condicionTop + "_" + tendenciaTop;
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("Vector_" + vectrTop)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("Vector_" + vectrTop);
                    instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("Vector"), ontoMDL.getXSDType("Vector"), vectrTop + "",
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "take", ontoMDL.getURIofPrefix("actgu") + "take" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("Condition"), ontoMDL.getUriClass("Tendency") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                condicionTop + "", tendenciaTop + "" });
                }
                // Recuperar valores BOTTOM
                double condicionBottom = Double.parseDouble(jsObj.get("condicion_bottom").toString().replaceAll("\"", "").toLowerCase());
                // Tendencia - considerando un valor de -50 y a 50
                double tendenciaBottom = Double.parseDouble(jsObj.get("tendencia_bottom").toString().replaceAll("\"", "").toLowerCase());
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("Condition_" + condicionBottom)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("Condition_" + condicionBottom);
                    instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("Condition"), ontoMDL.getXSDType("Condition"), condicionBottom + "",
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "isClassifiedBy", ontoMDL.getURIofPrefix("actgu") + "isDescribedBy" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("UnitOfMeasure"), ontoMDL.getUriClass("Property") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                unidadTempera, properTempera });
                }
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("Tendency_" + tendenciaBottom)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("Tendency_" + tendenciaBottom);
                    instanciaJENA.addIndividual(ontoMDL.getUriClass("Tendency"), ontoMDL.getXSDType("Tendency"), tendenciaBottom + "");
                }
                // Formar valor de vector
                String vectrBottom = condicionBottom + "_" + tendenciaBottom;
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("Vector_" + vectrBottom)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("Vector_" + vectrBottom);
                    instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("Vector"), ontoMDL.getXSDType("Vector"), vectrBottom + "",
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "take", ontoMDL.getURIofPrefix("actgu") + "take" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("Condition"), ontoMDL.getUriClass("Tendency") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                condicionBottom + "", tendenciaBottom + "" });
                }
                // Determinar la pendiente "m" de Confort de la persona
                double mSlope = ControladorConfort.calcularPendienteM(condicionTop, tendenciaTop, condicionBottom, tendenciaBottom);
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("SlopeOfComfort_" + mSlope)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("SlopeOfComfort_" + mSlope);
                    instanciaJENA.addIndividual(ontoMDL.getUriClass("SlopeOfComfort"), ontoMDL.getXSDType("SlopeOfComfort"), mSlope + "");
                }
                // Formmar Tendencia de Confort
                String tendeConf = mSlope + "_" + vectrTop + "_" + vectrBottom;
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("ComfortTrend_" + tendeConf)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("ComfortTrend_" + tendeConf);
                    instanciaJENA.addIndividual(ontoMDL.getUriClass("ComfortTrend"), ontoMDL.getXSDType("ComfortTrend"), tendeConf);
                }
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("ComfortTrend_" + tendeConf + "_" + mSlope)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("ComfortTrend_" + tendeConf + "_" + mSlope);
                    instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("ComfortTrend"), tendeConf,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "form" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("SlopeOfComfort") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                mSlope + "" });
                }
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("ComfortTrend_" + tendeConf + "_" + vectrTop)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("ComfortTrend_" + tendeConf + "_" + vectrTop);
                    instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("ComfortTrend"), tendeConf,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "utilize" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("Vector") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                vectrTop + "" });
                }
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("ComfortTrend_" + tendeConf + "_" + vectrBottom)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("ComfortTrend_" + tendeConf + "_" + vectrBottom);
                    instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("ComfortTrend"), tendeConf,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "utilize" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("Vector") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                vectrBottom + "" });
                }
                // Recupera valor de USUARIO
                String nomPersona = jsObj.get("etiqueta_usuario").toString().replaceAll("\"", "").trim();
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("Noun_" + nomPersona)) {
                    // Agregar intancia con relaciones
                    aryListElemts.add("Noun_" + nomPersona);
                    instanciaJENA.addIndividual(ontoMDL.getUriClass("Noun"), ontoMDL.getXSDType("Noun"), nomPersona);
                }
                // ************
                // Actualizar: a instancia de USUARIO con sus relaciones
                instanciaJENA.addRelsInIndividual(
                        ontoMDL.getUriClass("Person"), idPersona,
                        new String[]{ // Construye uri para las relaciones actuales
                            ontoMDL.getURIofPrefix("foaf") + "name",
                            ontoMDL.getURIofPrefix("actgu") + "dressed",
                            ontoMDL.getURIofPrefix("actgu") + "isSortOf",
                            ontoMDL.getURIofPrefix("actgu") + "usedTo"
                        },
                        new String[]{ // Recuperar uri de clase para las relaciones actuales
                            ontoMDL.getUriClass("Noun"),
                            ontoMDL.getUriClass("NumClothes"),
                            ontoMDL.getUriClass("NumPriority"),
                            ontoMDL.getUriClass("ComfortTrend")
                        },
                        new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                            nomPersona,
                            jsObj.get("id_equipamiento").toString().replaceAll("\"", "").toLowerCase(), // CLO
                            jsObj.get("id_rol").toString().replaceAll("\"", "").toLowerCase(), // PRIORIDAD
                            tendeConf
                        });
            }
            // Actualizar: a instancia de GRUPO con su relación a USUARIO
            instanciaJENA.addRelsInIndividual(
                    ontoMDL.getUriClass("Group"), jsObj.get("nombre_grupo").toString().replaceAll("\"", "").trim(),
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("foaf") + "member" },
                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                        ontoMDL.getUriClass("Person") },
                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                        idPersona });
        }
    }
    
    // Función que elimina en la onto, información sobre el Ambiente
    private void limpiarDataDeAmb() {
        // Adicionales
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Description"));
        // Remover todos los elementos individuales de la clase
        // *****************************************************
        // ENTORNO
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Deployment"));
        // AMBIENTE
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Sensor"));
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("SensorOutput"));
    }
    
    // Función que inicializa en la onto, información sobre el Ambiente
    private void reiniciarDataDeAmb() {
        // Eliminar de la Onto, data que pueda interferir
        limpiarDataDeAmb();
        // *****************************************************
        // Elemento que representa al espacio revisado
        instanciaJENA.addIndividual(ontoMDL.getUriClass("Deployment"), ontoMDL.getXSDType("Deployment"), noumEnviront);
        // *****************************************************
        // Elementos que representan a los SENSORES
        // ** Property: gas concentration, UnitOfMeasure: parts per million
        instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("Sensor"), ontoMDL.getXSDType("Sensor"), sensorConcGas,
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("ssn") + "hasDeployment", ontoMDL.getURIofPrefix("ssn") + "observes" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("Deployment"), ontoMDL.getUriClass("Property") },
                new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                    noumEnviront, properConcGas });
        // ** Property: temperature, UnitOfMeasure: celsius
        instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("Sensor"), ontoMDL.getXSDType("Sensor"), sensorTempera,
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("ssn") + "hasDeployment", ontoMDL.getURIofPrefix("ssn") + "observes" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("Deployment"), ontoMDL.getUriClass("Property") },
                new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                    noumEnviront, properTempera });
        // ** Property: humidity, UnitOfMeasure: percentage
        instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("Sensor"), ontoMDL.getXSDType("Sensor"), sensorHumedad,
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("ssn") + "hasDeployment", ontoMDL.getURIofPrefix("ssn") + "observes" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("Deployment"), ontoMDL.getUriClass("Property") },
                new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                    noumEnviront, properHumedad });
        // ** Property: air velocity, UnitOfMeasure: metre per second
        instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("Sensor"), ontoMDL.getXSDType("Sensor"), sensorVelAire,
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("ssn") + "hasDeployment", ontoMDL.getURIofPrefix("ssn") + "observes" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("Deployment"), ontoMDL.getUriClass("Property") },
                new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                    noumEnviront, properVelAire });
        // *****************************************************
        // Elementos que representa a los SENSORs OUTPUT
        // ** Property: gas concentration, UnitOfMeasure: parts per million
        instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("SensorOutput"), ontoMDL.getXSDType("SensorOutput"), sensorConcGas,
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("ssn") + "isProducedBy" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("Sensor") },
                new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                    sensorConcGas });
        // ** Property: temperature, UnitOfMeasure: celsius
        instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("SensorOutput"), ontoMDL.getXSDType("SensorOutput"), sensorTempera,
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("ssn") + "isProducedBy" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("Sensor") },
                new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                    sensorTempera });
        // ** Property: humidity, UnitOfMeasure: percentage
        instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("SensorOutput"), ontoMDL.getXSDType("SensorOutput"), sensorHumedad,
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("ssn") + "isProducedBy" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("Sensor") },
                new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                    sensorHumedad });
        // ** Property: air velocity, UnitOfMeasure: metre per second
        instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("SensorOutput"), ontoMDL.getXSDType("SensorOutput"), sensorVelAire,
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("ssn") + "isProducedBy" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("Sensor") },
                new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                    sensorVelAire });
        // ------------------------------------------------------
        // Variable de apoyo para evitar elementos repetidos
        ArrayList<String> aryListElemts = new ArrayList<String>();
        // ------------------------------------------------------
        // Recuperar Arreglo de datos sobre HVACs
        JsonArray jsAry = Principal.gestorCRUD.recuperarDataHVACs();
        int tamJsonAry = jsAry.size();
        JsonObject jsObj = null;
        // Recorrer Arreglo JSON con datos
        for (int x = 0; x < tamJsonAry; x ++) {
            // Crear instancia de objeto JSON
            jsObj = jsAry.get(x).getAsJsonObject();
            // Recupera valor de HVAC
            String hvac = jsObj.get("id_mecanismo_hvac").toString().replaceAll("\"", "").toLowerCase();
            // Validar si es un ELEMENTO repetido
            if (!aryListElemts.contains("Actuator_" + hvac)) {
                aryListElemts.add("Actuator_" + hvac);
                // Actualizar: a instancia de HVAC con sus relaciones
                instanciaJENA.addRelsInIndividual(
                        ontoMDL.getUriClass("Deployment"), noumEnviront,
                        new String[]{ // Construye uri para las relaciones actuales
                            ontoMDL.getURIofPrefix("actgu") + "isOccupiedBy" },
                        new String[]{ // Recuperar uri de clase para las relaciones actuales
                            ontoMDL.getUriClass("Actuator") },
                        new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                            hvac });
            }
        }
        // ------------------------------------------------------
        // Recuperar Arreglo de datos sobre GRUPOS
        jsAry = Principal.gestorCRUD.recuperarDataGrupos();
        tamJsonAry = jsAry.size();
        jsObj = null;
        // Recorrer Arreglo JSON con datos
        for (int x = 0; x < tamJsonAry; x ++) {
            // Crear instancia de objeto JSON
            jsObj = jsAry.get(x).getAsJsonObject();
            // Recupera valor de GRUPO
            String grupo = jsObj.get("nombre").toString().replaceAll("\"", "").toLowerCase();
            // Validar si es un ELEMENTO repetido
            if (!aryListElemts.contains("Group_" + grupo)) {
                aryListElemts.add("Group_" + grupo);
                // Actualizar: a instancia de GRUPO con sus relaciones
                instanciaJENA.addRelsInIndividual(
                        ontoMDL.getUriClass("Deployment"), noumEnviront,
                        new String[]{ // Construye uri para las relaciones actuales
                            ontoMDL.getURIofPrefix("actgu") + "isOccupiedBy" },
                        new String[]{ // Recuperar uri de clase para las relaciones actuales
                            ontoMDL.getUriClass("Group") },
                        new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                            grupo });
            }
        }
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Función que actualiza en la onto, información sobre el Ambiente
    public void actualizarDataDeAmbiente() {
        // Adicionales: Remover todos los elementos individuales
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Description"));
        // *****************************************************
        // Recuperar data sobre el ambiente
        // ** Property: gas concentration, UnitOfMeasure: parts per million
        String cgas = Principal.valor_gas + "";
        // ** Property: humidity, UnitOfMeasure: percentage
        String humedad = Principal.valor_humedad + "";
        // ** Property: temperature, UnitOfMeasure: celsius
        String temperatura = Principal.valor_temperatura + "";
        // ** Property: air velocity, UnitOfMeasure: metre per second
        String velocidadaire = Principal.valor_velocidadaire + "";
        // ** Data con formato = IDUSR:0,0,0|0,0,0|0,0,0
        Hashtable<String, String[]> acelerometro = (Hashtable<String, String[]>) Principal.valores_acelerometro.clone();
        // ------------------------------------------------------
        // Variable de apoyo para evitar elementos repetidos
        ArrayList<String> aryListElemts = new ArrayList<String>();
        ArrayList<String> aryListObsvsVals = new ArrayList<String>();
        // *****************************************************
        // *****************************************************
        // *****************************************************
        // *****************************************************
        // *****************************************************
        // Remover relaciones en individuales
        instanciaJENA.removeRelsInIndividuals(ontoMDL.getUriClass("Actuator"),
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("actgu") + "produce" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("ObservationValue") });
        // Remover relaciones en individuales
        instanciaJENA.removeRelsInIndividuals(ontoMDL.getUriClass("SensorOutput"),
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("ssn") + "hasValue" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("ObservationValue") });
        // Remover individuales innecesarios
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("ObservationValue"));
        // ------------------------------------------------------
        // CONCENTRACIÓN DE GAS
        // Validar si es un ELEMENTO repetido
        if (!aryListElemts.contains("ObservationValue_" + cgas)) {
            // Agrega intancia
            aryListObsvsVals.add(cgas);
            aryListElemts.add("ObservationValue_" + cgas);
            // Agrega intancia OBSERVATIONVALUE a Ontología - CONCENTRACIÓN DE GAS
            instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("ObservationValue"), ontoMDL.getXSDType("ObservationValue"), cgas,
                    new String[]{ // Construye uri de relaciones
                        ontoMDL.getURIofPrefix("dul") + "isClassifiedBy" },
                    new String[]{ // Recuperar uri de clase
                        ontoMDL.getUriClass("UnitOfMeasure") },
                    new String[]{ // Establece valores
                        unidadConcGas });
        } else {
            // Actualizar: a instancia de OBSERVATIONVALUE con sus relación a UNITOFMEASURE
            instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("ObservationValue"), cgas,
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("dul") + "isClassifiedBy" },
                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                        ontoMDL.getUriClass("UnitOfMeasure")},
                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                        unidadConcGas });
        }
        // TEMPERATURA
        // Validar si es un ELEMENTO repetido
        if (!aryListElemts.contains("ObservationValue_" + temperatura)) {
            // Agrega intancia
            aryListObsvsVals.add(temperatura);
            aryListElemts.add("ObservationValue_" + temperatura);
            // Agrega intancia OBSERVATIONVALUE a Ontología - TEMPERATURA
            instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("ObservationValue"), ontoMDL.getXSDType("ObservationValue"), temperatura,
                    new String[]{ // Construye uri de relaciones
                        ontoMDL.getURIofPrefix("dul") + "isClassifiedBy" },
                    new String[]{ // Recuperar uri de clase
                        ontoMDL.getUriClass("UnitOfMeasure") },
                    new String[]{ // Establece valores
                        unidadTempera });
        } else {
            // Actualizar: a instancia de OBSERVATIONVALUE con sus relación a UNITOFMEASURE
            instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("ObservationValue"), temperatura,
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("dul") + "isClassifiedBy" },
                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                        ontoMDL.getUriClass("UnitOfMeasure")},
                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                        unidadTempera });
        }
        // HUMEDAD
        // Validar si es un ELEMENTO repetido
        if (!aryListElemts.contains("ObservationValue_" + humedad)) {
            // Agrega intancia
            aryListObsvsVals.add(humedad);
            aryListElemts.add("ObservationValue_" + humedad);
            // Agrega intancia OBSERVATIONVALUE a Ontología - HUMEDAD
            instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("ObservationValue"), ontoMDL.getXSDType("ObservationValue"), humedad,
                    new String[]{ // Construye uri de relaciones
                        ontoMDL.getURIofPrefix("dul") + "isClassifiedBy" },
                    new String[]{ // Recuperar uri de clase
                        ontoMDL.getUriClass("UnitOfMeasure") },
                    new String[]{ // Establece valores
                        unidadHumedad });
        } else {
            // Actualizar: a instancia de OBSERVATIONVALUE con sus relación a UNITOFMEASURE
            instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("ObservationValue"), humedad,
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("dul") + "isClassifiedBy" },
                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                        ontoMDL.getUriClass("UnitOfMeasure")},
                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                        unidadHumedad });
        }
        // VELOCIDAD DEL AIRE
        // Validar si es un ELEMENTO repetido
        if (!aryListElemts.contains("ObservationValue_" + velocidadaire)) {
            // Agrega intancia
            aryListObsvsVals.add(velocidadaire);
            aryListElemts.add("ObservationValue_" + velocidadaire);
            // Agrega intancia OBSERVATIONVALUE a Ontología - VELOCIDAD DEL AIRE
            instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("ObservationValue"), ontoMDL.getXSDType("ObservationValue"), velocidadaire,
                    new String[]{ // Construye uri de relaciones
                        ontoMDL.getURIofPrefix("dul") + "isClassifiedBy" },
                    new String[]{ // Recuperar uri de clase
                        ontoMDL.getUriClass("UnitOfMeasure") },
                    new String[]{ // Establece valores
                        unidadVelAire });
        } else {
            // Actualizar: a instancia de OBSERVATIONVALUE con sus relación a UNITOFMEASURE
            instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("ObservationValue"), velocidadaire,
                    new String[]{ // Construye uri para las relaciones actuales
                        ontoMDL.getURIofPrefix("dul") + "isClassifiedBy" },
                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                        ontoMDL.getUriClass("UnitOfMeasure")},
                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                        unidadVelAire });
        }
        // *****************************************************
        // *****************************************************
        // *****************************************************
        // *****************************************************
        // *****************************************************
        // Elementos que representa a los SENSORs OUTPUT
        // ** Property: gas concentration, UnitOfMeasure: parts per million
        instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("SensorOutput"), sensorConcGas,
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("ssn") + "hasValue" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("ObservationValue") },
                new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                    cgas });
        // ** Property: temperature, UnitOfMeasure: celsius
        instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("SensorOutput"), sensorTempera,
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("ssn") + "hasValue" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("ObservationValue") },
                new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                    temperatura });
        // ** Property: humidity, UnitOfMeasure: percentage
        instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("SensorOutput"), sensorHumedad,
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("ssn") + "hasValue" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("ObservationValue") },
                new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                    humedad });
        // ** Property: air velocity, UnitOfMeasure: metre per second
        instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("SensorOutput"), sensorVelAire,
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("ssn") + "hasValue" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("ObservationValue") },
                new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                    velocidadaire });
        // ------------------------------------------------------
        // Recuperar Arreglo de datos sobre los HVACs
        JsonArray jsAry = recuperarDatos(InspectorONTO.tagIdActuadores);
        int tamJsonAry = jsAry.size();
        JsonObject jsObj = null;
        // Recorrer Arreglo JSON con datos
        for (int x = 0; x < tamJsonAry; x ++) {
            // Crear instancia de objeto JSON
            jsObj = jsAry.get(x).getAsJsonObject();
            // Recupera valor de HVAC
            String hvac = jsObj.get("id_mecanismo_hvac").toString().replaceAll("\"", "").toLowerCase();
            // Validar si es un ELEMENTO repetido
            if (!aryListElemts.contains("Actuator_" + hvac)) {
                aryListElemts.add("Actuator_" + hvac);
                // Recorrer OBSERVATIONVALUE
                for (String obsvVal: aryListObsvsVals) {
                    // Actualizar: a instancia de HVAC con sus relaciones
                    instanciaJENA.addRelsInIndividual(
                            ontoMDL.getUriClass("Actuator"), hvac,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "produce" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("ObservationValue") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                obsvVal });
                }
            }
        }
        // ------------------------------------------------------
        // Remover relaciones en individuales
        instanciaJENA.removeRelsInIndividuals(ontoMDL.getUriClass("Person"),
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("actgu") + "has" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("ThermalComfort") });
        // Remover relaciones en individuales
        instanciaJENA.removeRelsInIndividuals(ontoMDL.getUriClass("Demographic"),
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("actgu") + "influencedIn" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("ThermalComfort") });
        // Remover relaciones en individuales
        instanciaJENA.removeRelsInIndividuals(ontoMDL.getUriClass("Anthropometry"),
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("actgu") + "influencedIn" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("ThermalComfort") });
        // Remover relaciones en individuales
        instanciaJENA.removeRelsInIndividuals(ontoMDL.getUriClass("InsulationValue"),
                new String[]{ // Construye uri para las relaciones actuales
                    ontoMDL.getURIofPrefix("actgu") + "influencedIn" },
                new String[]{ // Recuperar uri de clase para las relaciones actuales
                    ontoMDL.getUriClass("ThermalComfort") });
        // Remover individuales innecesarios
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("MetabolicRate"));
        // ------------------------------------------------------
        // Recuperar Arreglo de datos sobre USUARIOS
        jsAry = recuperarDatos(InspectorONTO.tagBasePersonas);
        tamJsonAry = jsAry.size();
        jsObj = null;
        // Recorrer Arreglo JSON con datos
        for (int x = 0; x < tamJsonAry; x ++) {
            // Crear instancia de objeto JSON
            jsObj = jsAry.get(x).getAsJsonObject();
            // Recupera valor de USUARIO
            String persona = jsObj.get("id_usuario").toString().replaceAll("\"", "").toLowerCase();
            // Validar si es un ELEMENTO repetido
            if (!aryListElemts.contains("Person_" + persona)) {
                // Agrega intancia
                aryListElemts.add("Person_" + persona);
                // ************
                // Calcular valor de CLO
                String valorCLO = String.format("%.2f", calcularCLO(
                        jsObj.get("sexo").toString().replaceAll("\"", "").toLowerCase(),
                        Double.parseDouble(jsObj.get("valor_clo_equipado").toString().replaceAll("\"", "").toLowerCase()),
                        Double.parseDouble(jsObj.get("suma_clo_para_h_equipado").toString().replaceAll("\"", "").toLowerCase()),
                        Double.parseDouble(jsObj.get("suma_clo_para_m_equipado").toString().replaceAll("\"", "").toLowerCase())));
                // Calcular valor de MET
                String valorMET = String.format("%.2f", calcularMET(persona, acelerometro));
                // Calcular Confort Térmico Personal
                ConfortTermico confTerm = new ConfortTermico(Double.parseDouble(temperatura) /*Temperatura*/,
                        Double.parseDouble(velocidadaire) /*Velocidad del Aire*/,
                        Double.parseDouble(humedad) /*Humedad Relativa*/,
                        Double.parseDouble(valorMET) /*Valor MET*/,
                        Double.parseDouble(valorCLO) /*Valor CLO*/);
                String valorPMV = confTerm.calcularValorPMV().getTCIndexAsStr();
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("MetabolicRate_" + valorMET)) {
                    // Agrega intancia
                    aryListElemts.add("MetabolicRate_" + valorMET);
                    // Agrega intancia MET a Ontología
                    instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("MetabolicRate"), ontoMDL.getXSDType("MetabolicRate"), valorMET,
                        new String[]{ // Construye uri de relaciones
                            ontoMDL.getURIofPrefix("actgu") + "isDescribedBy", ontoMDL.getURIofPrefix("actgu") + "isClassifiedBy",
                            ontoMDL.getURIofPrefix("actgu") + "isPhysicalWearOf" },
                        new String[]{ // Recuperar uri de clase
                            ontoMDL.getUriClass("Property"), ontoMDL.getUriClass("UnitOfMeasure"),
                            ontoMDL.getUriClass("Person") },
                        new String[]{ // Establece valores
                            properMET, unidadMET,
                            persona });
                } else {
                    // Actualizar intancia MET a Ontología
                    instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("MetabolicRate"), valorMET,
                        new String[]{ // Construye uri de relaciones
                            ontoMDL.getURIofPrefix("actgu") + "isPhysicalWearOf" },
                        new String[]{ // Recuperar uri de clase
                            ontoMDL.getUriClass("Person") },
                        new String[]{ // Establece valores
                            persona });
                }
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("MetabolicRate_" + valorMET + "_" + valorPMV)) {
                    // Agrega intancia
                    aryListElemts.add("MetabolicRate_" + valorMET + "_" + valorPMV);
                    // Actualizar intancia MET a Ontología
                    instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("MetabolicRate"), valorMET,
                        new String[]{ // Construye uri de relaciones
                            ontoMDL.getURIofPrefix("actgu") + "influencedIn" },
                        new String[]{ // Recuperar uri de clase
                            ontoMDL.getUriClass("ThermalComfort") },
                        new String[]{ // Establece valores
                            valorPMV });
                }
                // ************
                // Recupera valores de usuario
                String sexo = jsObj.get("sexo").toString().replaceAll("\"", "").toLowerCase();
                String edad = jsObj.get("edad").toString().replaceAll("\"", "").toLowerCase();
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("Demographic_" + sexo + "_" + edad + "_" + valorPMV)) {
                    // Agrega intancia
                    aryListElemts.add("Demographic_" + sexo + "_" + edad + "_" + valorPMV);
                    // Actualizar: a instancia de ANTROPOMETRIA con sus relaciones
                    instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("Demographic"), sexo + "_" + edad,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "influencedIn" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("ThermalComfort") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                valorPMV });
                }
                // ************
                // Recupera valores de usuario
                double peso = Double.parseDouble(jsObj.get("peso").toString().replaceAll("\"", "").toLowerCase());
                double altura = Double.parseDouble(jsObj.get("altura").toString().replaceAll("\"", "").toLowerCase());
                String valPesoAltura = String.format("%.2f", peso) + "_" + String.format("%.2f", altura);
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("Anthropometry_" + valPesoAltura + "_" + valorPMV)) {
                    // Agrega intancia
                    aryListElemts.add("Anthropometry_" + valPesoAltura + "_" + valorPMV);
                    // Actualizar: a instancia de ANTROPOMETRIA con sus relaciones
                    instanciaJENA.addRelsInIndividual(
                            ontoMDL.getUriClass("Anthropometry"), valPesoAltura,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "influencedIn" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("ThermalComfort") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                valorPMV });
                }
                // ************
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("InsulationValue_" + valorCLO + "_" + valorPMV)) {
                    // Agrega intancia
                    aryListElemts.add("InsulationValue_" + valorCLO + "_" + valorPMV);
                    // Actualizar: a instancia de INSULATIONVALUE con sus relaciones
                    instanciaJENA.addRelsInIndividual(
                            ontoMDL.getUriClass("InsulationValue"), valorCLO,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "influencedIn" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("ThermalComfort") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                valorPMV });
                }
                // ************
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("ThermalComfort_" + valorPMV)) {
                    // Agrega intancia
                    aryListElemts.add("ThermalComfort_" + valorPMV);
                    // Recorrer OBSERVATIONVALUE
                    for (String obsvVal: aryListObsvsVals) {
                        // Actualizar: a instancia de HVAC con sus relaciones
                        instanciaJENA.addRelsInIndividual(
                                ontoMDL.getUriClass("ObservationValue"), obsvVal,
                                new String[]{ // Construye uri para las relaciones actuales
                                    ontoMDL.getURIofPrefix("actgu") + "influencedIn" },
                                new String[]{ // Recuperar uri de clase para las relaciones actuales
                                    ontoMDL.getUriClass("ThermalComfort") },
                                new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                    valorPMV });
                    }
                }
                // ************
                // Validar si es un ELEMENTO repetido
                if (!aryListElemts.contains("Person_" + persona + "_" + valorPMV)) {
                    // Agrega intancia
                    aryListElemts.add("Person_" + persona + "_" + valorPMV);
                    // Actualizar: a instancia de PERSONA con sus relaciones
                    instanciaJENA.addRelsInIndividual(
                            ontoMDL.getUriClass("Person"), persona,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "has" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("ThermalComfort") },
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                valorPMV });
                }
            }
        }
    }
    
    // Función que actualiza en la onto, el estado de los HVAC
    public void actualizarDataDeHVACs() {
        // Adicionales: Remover todos los elementos individuales
        instanciaJENA.removeIndividuals(ontoMDL.getUriClass("Description"));
        // Recupera data de condifiguración de HVACs
        JsonArray jsAryCA = Principal.jsAryConfigAct;
        // Validar existencia de configuración
        if ((jsAryCA != null)) {
            // Recupera el número de elementos a ser actualizados
            int numAryConAct = jsAryCA.size();
            // Validar que existen elementos configurados
            if (numAryConAct > 0) {
                // Variable de apoyo para evitar elementos repetidos
                ArrayList<String> aryListElemts = new ArrayList<String>();
                // Remover relaciones en individuales
                instanciaJENA.removeRelsInIndividuals(ontoMDL.getUriClass("Actuator"),
                        new String[]{ // Construye uri para las relaciones actuales
                            ontoMDL.getURIofPrefix("actgu") + "present" },
                        new String[]{ // Recuperar uri de clase para las relaciones actuales
                            ontoMDL.getUriClass("State") });
                // Remover individuales
                instanciaJENA.removeIndividuals(ontoMDL.getUriClass("ConfigurationValue"));
                // Prepara variables de recorrido
                JsonObject jsConfig = null;
                // Recorrrer configuración de HVACs
                for (int x = 0; x < numAryConAct; x++) {
                    // Recuperar instancia
                    jsConfig = jsAryCA.get(x).getAsJsonObject();
                    // Recuperar valores de instancia
                    String vAct = jsConfig.get("val_actuador").toString().replaceAll("\"", "");
                    String vEdo = jsConfig.get("val_estado").toString().replaceAll("\"", "").toLowerCase();
                    // Actualizar: a instancia de ACTUATOR con su relación a un STATE
                    instanciaJENA.addRelsInIndividual( ontoMDL.getUriClass("Actuator"), vAct,
                            new String[]{ // Construye uri para las relaciones actuales
                                ontoMDL.getURIofPrefix("actgu") + "present" },
                            new String[]{ // Recuperar uri de clase para las relaciones actuales
                                ontoMDL.getUriClass("State")},
                            new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                vEdo });
                    // Recuperar valores de objeto JSON
                    String[] valsAmbt = new String [] {
                        fixDblAsStr(jsConfig.get("val_humedad").toString().replaceAll("\"", "").toLowerCase()),
                        fixDblAsStr(jsConfig.get("val_temperatura").toString().replaceAll("\"", "").toLowerCase()),
                        fixDblAsStr(jsConfig.get("val_vel_aire").toString().replaceAll("\"", "").toLowerCase()),
                        fixDblAsStr(jsConfig.get("val_con_gas").toString().replaceAll("\"", "").toLowerCase()) };
                    // Formar datos para análisis
                    String[] valsPropers = new String[] {properHumedad, properTempera, properVelAire, properConcGas};
                    String[] valsUnidads = new String[] {unidadHumedad, unidadTempera, unidadVelAire, unidadConcGas};
                    // Recorrer valores agrupados de Objeto JSON
                    for (int i = 0; i < 4; i++) {
                        // Validar que no se agrege un elemento repetido
                        if (!aryListElemts.contains("ConfigurationValue_" + valsAmbt[i])) {
                            // Agregar elemento para no repetirlo
                            aryListElemts.add("ConfigurationValue_" + valsAmbt[i]);
                            // Agregar valor de configuración
                            instanciaJENA.addIndividualRelated(ontoMDL.getUriClass("ConfigurationValue"), ontoMDL.getXSDType("ConfigurationValue"), valsAmbt[i],
                                    new String[]{ // Construye uri para las relaciones actuales
                                        ontoMDL.getURIofPrefix("actgu") + "isClassifiedBy",
                                        ontoMDL.getURIofPrefix("actgu") + "isDescribedBy",
                                        ontoMDL.getURIofPrefix("actgu") + "configured"
                                    },
                                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                                        ontoMDL.getUriClass("UnitOfMeasure"),
                                        ontoMDL.getUriClass("Property"),
                                        ontoMDL.getUriClass("Actuator")
                                    },
                                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                        valsUnidads[i],
                                        valsPropers[i],
                                        vAct
                                    });
                        } else {
                            // Agregar valor de configuración
                            instanciaJENA.addRelsInIndividual(ontoMDL.getUriClass("ConfigurationValue"), valsAmbt[i],
                                    new String[]{ // Construye uri para las relaciones actuales
                                        ontoMDL.getURIofPrefix("actgu") + "isClassifiedBy",
                                        ontoMDL.getURIofPrefix("actgu") + "isDescribedBy",
                                        ontoMDL.getURIofPrefix("actgu") + "configured"
                                    },
                                    new String[]{ // Recuperar uri de clase para las relaciones actuales
                                        ontoMDL.getUriClass("UnitOfMeasure"),
                                        ontoMDL.getUriClass("Property"),
                                        ontoMDL.getUriClass("Actuator")
                                    },
                                    new String[]{ // Establece valor de ELEMENTO al cual relacionarse
                                        valsUnidads[i],
                                        valsPropers[i],
                                        vAct
                                    });
                        }
                    }
                }
            }
        }
    }
            
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Función ajustar el valor de un String que deberia mantenerse como Double
    private String fixDblAsStr (String valor) {
        // Variable
        String strDbl = "";
        // Para errores
        try {
            strDbl = Double.parseDouble(valor) + "";
        } catch (Exception e) {
            strDbl = "0";
        }
        // Validar
        if (strDbl.equals("")) {
            strDbl = "0";
        }
        // Devolver respuesta
        return strDbl;
    }
    
    // Función que calcula el índice de masa corporal
    private double calcularBMI(double peso, double altura) {
        double rBMI = peso/altura;
        return rBMI;
    }
    
    // Función que calcula el aislamiento térmico de la ropa del usuario
    private double calcularCLO(String sexo, double valCloBase, double valCloH, double valCloM) {
        // Variable de retorno
        double valorClo = 0;
        // Ajustar valor de CLO
        if (sexo.equals("h")) {
            valorClo = valCloBase + valCloH;
        } else {
            valorClo = valCloBase + valCloM;
        }
        // Devolver valor CLO
        return valorClo;
    }
    
    // Función que calcula el valor de MET 
    private double calcularMET(String strIdUsr, Hashtable<String, String[]> acelerometro) {
        String srIdUsAJ = strIdUsr;
        // Preparar trato de acelerometro
        String[] datAcel = new String[]{};
        // Se inicializa MET como alguien: Sentado y tranquilo o sentado y leyendo o escribiendo
        double metDataCal = 1.0;
        // Valida si existen ACELEROMETROS REALES conectados
        if (!Principal.senAcelerometroHabilitado) {
            // Convertir el ID de Usuario a un Integer
            int numIdUsr = Integer.parseInt(srIdUsAJ);
            // Validar tamaño de ID
            if (numIdUsr >= 10) {
                // Validar reciduo entre 10
                if ((numIdUsr%10) == 0) {
                    // Ajustar Ide
                    srIdUsAJ = "0";
                // Validar reciduo entre 9
                } else if ((numIdUsr%9) == 0) { 
                    // Ajustar Ide
                    srIdUsAJ = "9";
                // Validar reciduo entre 8
                } else if ((numIdUsr%8) == 0) { 
                    // Ajustar Ide
                    srIdUsAJ = "8";
                // Validar reciduo entre 7
                } else if ((numIdUsr%7) == 0) { 
                    // Ajustar Ide
                    srIdUsAJ = "7";
                // Validar reciduo entre 6
                } else if ((numIdUsr%6) == 0) { 
                    // Ajustar Ide
                    srIdUsAJ = "6";
                // Validar reciduo entre 5
                } else if ((numIdUsr%5) == 0) { 
                    // Ajustar Ide
                    srIdUsAJ = "5";
                // Validar reciduo entre 4
                } else if ((numIdUsr%4) == 0) { 
                    // Ajustar Ide
                    srIdUsAJ = "4";
                // Validar reciduo entre 3
                } else if ((numIdUsr%3) == 0) { 
                    // Ajustar Ide
                    srIdUsAJ = "3";
                // Validar reciduo entre 2
                } else if ((numIdUsr%2) == 0) { 
                    // Ajustar Ide
                    srIdUsAJ = "2";
                // Validar reciduo entre 1
                } else {
                    // Ajustar Ide
                    srIdUsAJ = "1";
                }
            }
        }
        // Calcular data de ACELEROMETROS REALES conectados
        if (Principal.calcularDataDeAcelerometros) {
            // Recuperar datos de acelerometro
            if (acelerometro.containsKey(srIdUsAJ)) {
                // Recuperar datos de acelerometros de un usuario
                datAcel = acelerometro.get(srIdUsAJ);
                // -- CALCULAR VALOR MET
                /* De acuerdo a PancardoPablo 2016, para calcular el MET,
                *  Se debe sumar el valor absoluto (postivo) del arreglo de vectores de magnitudes de aceleración o VMA */
                double valorVMA = 0;
                int numDatosAcel = datAcel.length;
                // Recorrer vectores de magnitudes de aceleración o VMA
                for (int ixV = 0; ixV < numDatosAcel; ixV++) {
                    // Dividir en vectores de magnitudes de aceleración o VMA
                    String[] datas_acel = datAcel[ixV].split(",");
                    // Tranformar valores de magnitudes de aceleración
                    double data_acel_x = Double.parseDouble(String.format("%.2f", Double.parseDouble(datas_acel[0])));
                    double data_acel_y = Double.parseDouble(String.format("%.2f", Double.parseDouble(datas_acel[1])));
                    double data_acel_z = Double.parseDouble(String.format("%.2f", Double.parseDouble(datas_acel[2])));
                    // Convertir a su valor positivo
                    if (data_acel_x < 0) {
                        data_acel_x = -1 * data_acel_x;
                    }
                    if (data_acel_y < 0) {
                        data_acel_y = -1 * data_acel_y;
                    }
                    if (data_acel_z < 0) {
                        data_acel_z = -1 * data_acel_z;
                    }
                    // Suma de distancia
                    // https://www.ejemplos.co/calcular-la-aceleracion/
                    // dt = tf – ti // Donde tf será el tiempo final y ti el tiempo inicial del movimiento.
                    // VMA = SUMA_DE_TODO (|ax| + |ay| + |az|) dt = SUMA_DE_TODO (ABSOLUTO_X + ABSOLUTO_Y + ABSOLUTO_Z) * Minutos
                    valorVMA += (data_acel_x + data_acel_y + data_acel_z) * 1 /* 1 minuto*/;
                }
                // MET = 0,005348 * VMA + 0,01402
                // Estimación del valor MET a partir de los datos del acelerómetro
                // Se debe de reducir la dimensión de VMA multiplicando su valor por 10^-2, el calculo del MET requiere un VMA de 3 cifras decimales
                metDataCal = (0.005348 * (valorVMA * 0.01)  + 0.01402);
            }   
        }
        // Devolver valor MET
        return metDataCal;
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Función para generar documento OWL
    public void generarArchivoOWL () {
        // Escribir o sobreescribir archivo OWL
        instanciaJENA.generateOwlFile("ontoACTGU");
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Función que inicializa la Onto, con información estandar
    public String ejecutarConsultaPARQL(String strQuery) {
        // Variable respuesta
        String strResp = "";
        // Loop que busca el trabajo con una Ontologia valida
        while (true) {
            // Crear inspector de Ontología
            InspectorONTO inspOnto = new InspectorONTO(instanciaJENA);
            // Validar si el estado de la Ontologia es o no valido
            if (!inspOnto.esUnaOntoValida("")) {
                // Mensaje que evidencia el proceso de validación
                System.out.println(">>> ejecutarConsultaSPARQL >>> validandoOnto");
                // Dormir proceso
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Regresa al inicio
                continue;
            }
            // Determinar el Confort Térmico del Grupo y generar un JSON String como resultado
            strResp = inspOnto.ejecutarConsulta(strQuery);
            // Salir de Loop
            break;
        }
        // Devolver respuesta
        return strResp;
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Función para el análisis del Confort Térmico y su balance
    public String determinarConfortTermicoGrupal() {
        // Variable respuesta
        String strResp = "";
        String idGrupo = Principal.vlrGroupInvsg;
        String politica = Principal.vlrPolitica;
        int estrategia = Integer.parseInt(Principal.vlrEstrategia);
        // Crear inspector de Ontología
        InspectorONTO inspOnto = new InspectorONTO(instanciaJENA);
        // Determinar el Confort Térmico del Grupo y generar un JSON String como resultado
        strResp = inspOnto.determinarConfortGrupalyAcciones(idGrupo, politica, estrategia);
        // Devolver respuesta
        return strResp;
    }

    // Función que envía los datos de sensor 
    public String recuperarDatosAmbientales(JsonObject jsonObj) {
        // Variable respuesta
        String strResp = "";
        // Loop que busca el trabajo con una Ontologia valida
        while (true) {
            // Crear inspector de Ontología
            InspectorONTO inspOnto = new InspectorONTO(instanciaJENA);
            // Validar si el estado de la Ontologia es o no valido
            if (!inspOnto.esUnaOntoValida("")) {
                // Mensaje que evidencia el proceso de validación
                System.out.println(">>> recuperarDatosAmbientales >>> validandoOnto");
                // Dormir proceso
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Regresa al inicio
                continue;
            }
            // Consultar Ontologia y devolver información sobre sensores
            strResp = inspOnto.recuperarDatosAmbientales();
            // Salir de Loop
            break;
        }
        // Devolver respuesta
        return strResp;
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Función que recupera un conjunto de datos de la ontología
    public JsonArray recuperarDatos (String conjunto) {
        // Variable de consulta
        JsonArray jsAryResp = null;
        // -----------------------------------------------------
        // Crear inspector de Ontología
        InspectorONTO inspOnto = new InspectorONTO(instanciaJENA);
        // Solicitar conjunto de datos de la Ontología
        jsAryResp = inspOnto.recuperarDatos(conjunto);
        // Devolver datos
        return jsAryResp;
    }
    
    // Función que recupera de la ontología o base de datos, los datos de una tabla
    public String recuperarTabla (String tablaName) {
        // Consultar tablas
        String strJsRes = "";
        // -----------------------------------------------------
        // Loop que busca el trabajo con una Ontologia valida
        while (true) {
            // Crear inspector de Ontología
            InspectorONTO inspOnto = new InspectorONTO(instanciaJENA);
            // Validar si el estado de la Ontologia es o no valido
            if (!inspOnto.esUnaOntoValida("")) {
                // Mensaje que evidencia el proceso de validación
                System.out.println(">>> recuperarDatosDeTabla >>> validandoOnto");
                // Dormir proceso
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Regresa al inicio
                continue;
            }
            // Recupera un conjunto de datos de la ontología
            JsonArray jsAry = inspOnto.recuperarDatos(tablaName);
            strJsRes = jsonArray_to_String(jsAry, tablaName);
            // Salir de Loop
            break;
        }
        // Devolver datos en un String
        return strJsRes;
    }
    
    // Función que convierte a un Arreglo Json en un String
    private String jsonArray_to_String (JsonArray jsAry, String tablaName) {
        // Variables
        int irs = 0;
        String strKeyRS = "";
        String strValRS = "";
        String strJsonResp = "";
        JsonObject objTemp = null;
        // TRY - CATCH
        try{
            // Recuperar tamaño de Arreglo json
            int tamJsonAry = jsAry.size();
            // Recorrer data de Arreglo json
            for (int x = 0; x < tamJsonAry; x ++) {
                // Crear instancia JSON
                objTemp = jsAry.get(x).getAsJsonObject();
                // Abrir segmento JSON
                strJsonResp += "{";
                irs = 0;
                // Recuperar llaves de instancia JSON
                Set<Map.Entry<String, JsonElement>> entries = objTemp.entrySet();
                // Recorrer llaves de instancia JSON
                for (Map.Entry<String, JsonElement> entry: entries) {
                    // Recuperar llave y valor
                    strKeyRS = entry.getKey().replaceAll("\"", "");
                    strValRS = entry.getValue().toString().replaceAll("\"", "");
                    // Armar segmentos JSON
                    strJsonResp += "\"" + strKeyRS + "\":\"" + strValRS + "\",";
                    irs += 1;
                }
                // Validar si hay o no una coma de mas
                if (irs > 0) {
                    // Ajustar segmento JSON (quitar coma de más)
                    strJsonResp = strJsonResp.substring(0, strJsonResp.length() - 1);
                }
                // Cerrar segmento JSON
                strJsonResp += "},";
            }
            // Validar si ajustar segmento JSON
            if (!strJsonResp.equals("")) {
                // Ajustar segmento JSON (quitar coma de más)
                strJsonResp = strJsonResp.substring(0, strJsonResp.length() - 1);
            }
        } catch (Exception ex) {
            // Mostrar error
            ex.printStackTrace();
        }
        // Variabla de repuesta
        return "\"" + tablaName + "\":[" + strJsonResp + "]";
    }
}
