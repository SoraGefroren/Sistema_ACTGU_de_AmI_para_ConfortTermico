/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.basededatos;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Set;
import javaactg.Principal;
import javaactg.ontologia.InspectorONTO;

/**
 *
 * @author Jorge
 */
public class GestorCRUD {
    // Controlador de conexión con BD
    private ControladorBD control_BD = new ControladorBD();
    // Reserva de resultados de consulta de BD
    private JsonArray jsUsuarios = new JsonArray();
    private JsonArray jsMecHVACs = new JsonArray();
    private JsonArray jsGrupos = new JsonArray();
    private JsonArray jsRoles = new JsonArray();
    private JsonArray jsTiposHVAC = new JsonArray();
    private JsonArray jsTiposEqpmts = new JsonArray();
    
    // Constructor
    public GestorCRUD() {
        // Inicializar Arreglos Json
        cargarJsonArray();
    }
    
    // Funcion carga el los Arreglos Json de tablas especificas
    public void cargarJsonArray () {
        // Consultar tablas de BD y cargar Arreglos Json
        jsUsuarios = obtenerDataDeTablaEnBD(InspectorONTO.tagPersonas);
        jsMecHVACs = obtenerDataDeTablaEnBD(InspectorONTO.tagActuadores);
        jsGrupos = obtenerDataDeTablaEnBD(InspectorONTO.tagGrupos);
        jsRoles = obtenerDataDeTablaEnBD(InspectorONTO.tagRoles);
        jsTiposHVAC = obtenerDataDeTablaEnBD(InspectorONTO.tagTiposActuadores);
        jsTiposEqpmts = obtenerDataDeTablaEnBD(InspectorONTO.tagTiposEquipamientos);
    }
    
    // Función que consulta la BD y regresa un Arreglo Json de los datos consultados
    private JsonArray obtenerDataDeTablaEnBD(String tablaName) {
        // Variable para respuesta
        JsonArray jsResp = null;
        // Variable de resultado
        ResultSet rsDeBD = null;
        String[] aryNomCols = null;
        // Iniciar conexión con base de datos
        if (control_BD.startConnMySQL()) {
            // -----------------------------------------------------
            if (tablaName.equals(InspectorONTO.tagPersonas)) {
                // USUARIOS
                // Columnas consultadas
                aryNomCols = new String[] {"id_usuario", "etiqueta_usuario", "sexo", "edad", "altura", "peso", "id_grupo", "nombre_grupo", "id_rol", "rol",
                    "condicion_top", "tendencia_top", "condicion_bottom", "tendencia_bottom", 
                    "id_equipamiento",
                    "valor_clo_equipado", "descripcion_clo_equipado",
                    "suma_clo_para_h_equipado", "suma_clo_para_m_equipado",
                    "descripcion_clo_para_h_equipado", "descripcion_clo_para_m_equipado"};
                // Recuperar información sobre usuarios
                rsDeBD = control_BD.doSelectQuery(
                    "`sistema_actgu`.`Usuarios`.`id_usuario`,\n" + 
                    "`sistema_actgu`.`Usuarios`.`etiqueta` AS `etiqueta_usuario`,\n" +
                    "`sistema_actgu`.`Usuarios`.`sexo`,\n" + "`sistema_actgu`.`Usuarios`.`edad`,\n" +
                    "`sistema_actgu`.`Usuarios`.`altura`,\n" + "`sistema_actgu`.`Usuarios`.`peso`,\n" +
                    "`sistema_actgu`.`Grupos`.`id_grupo`,\n" + "`sistema_actgu`.`Grupos`.`nombre` AS `nombre_grupo`,\n" +
                    "`sistema_actgu`.`Roles`.`id_rol`,\n" + "`sistema_actgu`.`Roles`.`rol`,\n" +
                    "`sistema_actgu`.`Usuarios`.`condicion_top`, `sistema_actgu`.`Usuarios`.`tendencia_top`, \n" +
                    "`sistema_actgu`.`Usuarios`.`condicion_bottom`, `sistema_actgu`.`Usuarios`.`tendencia_bottom`, \n" +
                    "`sistema_actgu`.`Usuarios`.`id_equipamiento`,\n" +
                    "`Tabla_Equipamientos_Actuales`.`valor_clo` AS `valor_clo_equipado`,\n" +
                    "`Tabla_Equipamientos_Actuales`.`descripcion` AS `descripcion_clo_equipado`,\n" +
                    "`Tabla_Equipamientos_Actuales`.`suma_clo_para_h` AS `suma_clo_para_h_equipado`,\n" +
                    "`Tabla_Equipamientos_Actuales`.`suma_clo_para_m` AS `suma_clo_para_m_equipado`,\n" +
                    "`Tabla_Equipamientos_Actuales`.`descripcion_para_h` AS `descripcion_clo_para_h_equipado`,\n" +
                    "`Tabla_Equipamientos_Actuales`.`descripcion_para_m` AS `descripcion_clo_para_m_equipado`\n",
                    "(((`sistema_actgu`.`Usuarios` JOIN (`sistema_actgu`.`Tipos_de_Equipamientos` AS `Tabla_Equipamientos_Actuales`))\n" +
                    "JOIN `sistema_actgu`.`Roles`)\n" + "JOIN `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`)\n" + "JOIN `sistema_actgu`.`Grupos`\n" +
                    "	ON `sistema_actgu`.`Usuarios`.`id_rol` = `sistema_actgu`.`Roles`.`id_rol`\n" +
                    "	AND `sistema_actgu`.`Usuarios`.`id_equipamiento` = `Tabla_Equipamientos_Actuales`.`id_tipo_de_equipamiento`\n" +
                    "	AND `sistema_actgu`.`Usuarios`.`id_usuario` = `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`.`id_usuario`\n" +
                    "	AND `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`.`id_grupo` = `sistema_actgu`.`Grupos`.`id_grupo`",
                    "" , false);
            } else if (tablaName.equals(InspectorONTO.tagActuadores)) {
                // MECANISMO HVAC
                // Columnas consultadas
                aryNomCols = new String[] {"id_mecanismo_hvac", "id_tipo_de_mecanismo_hvac", "tipo_de_mecanismo",
                    "estado", "temperatura", "humedad", "concentracion_de_gas", "velocidad_del_aire"};
                // Recuperar información sobre usuarios
                rsDeBD = control_BD.doSelectQuery(
                    "`sistema_actgu`.`Mecanismos_HVAC`.`id_mecanismo_hvac`,\n" +
                    "`sistema_actgu`.`Mecanismos_HVAC`.`estado`,\n" +
                    "`sistema_actgu`.`Mecanismos_HVAC`.`id_tipo_de_mecanismo_hvac`,\n" +
                    "`sistema_actgu`.`Tipos_de_Mecanismos_HVAC`.`tipo_de_mecanismo`,\n" +
                    "`sistema_actgu`.`Mecanismos_HVAC`.`temperatura`,\n" + "`sistema_actgu`.`Mecanismos_HVAC`.`humedad`,\n" +
                    "`sistema_actgu`.`Mecanismos_HVAC`.`concentracion_de_gas`,\n" + "`sistema_actgu`.`Mecanismos_HVAC`.`velocidad_del_aire`\n",
                    "`sistema_actgu`.`Mecanismos_HVAC` JOIN `sistema_actgu`.`Tipos_de_Mecanismos_HVAC`\n" +
                    "   ON `sistema_actgu`.`Mecanismos_HVAC`.`id_tipo_de_mecanismo_hvac` = `sistema_actgu`.`Tipos_de_Mecanismos_HVAC`.`id_tipo_de_mecanismo_hvac`",
                    "", false);
            } else if (tablaName.equals(InspectorONTO.tagRoles)) {
                // ROLES
                // Columnas consultadas
                aryNomCols = new String[] {"id_rol", "rol"};
                // Recuperar información sobre usuarios
                rsDeBD = control_BD.doSelectQuery(
                    "`sistema_actgu`.`Roles`.`id_rol`, `sistema_actgu`.`Roles`.`rol`",
                    "`sistema_actgu`.`Roles`", "", false);
            } else if (tablaName.equals(InspectorONTO.tagTiposEquipamientos)) {
                // TIPOS DE EQUIPAMIENTO
                // Columnas consultadas
                aryNomCols = new String[] {"id_tipo_de_equipamiento", "nombre_equipamiento", "valor_clo", "descripcion", "suma_clo_para_m", "descripcion_para_m", "suma_clo_para_h", "descripcion_para_h"};
                // Recuperar información sobre usuarios
                rsDeBD = control_BD.doSelectQuery(
                    "`sistema_actgu`.`Tipos_de_Equipamientos`.`id_tipo_de_equipamiento`,\n" +
                    "`sistema_actgu`.`Tipos_de_Equipamientos`.`nombre` AS `nombre_equipamiento`,\n" +
                    "`sistema_actgu`.`Tipos_de_Equipamientos`.`valor_clo`, `sistema_actgu`.`Tipos_de_Equipamientos`.`descripcion`,\n" +
                    "`sistema_actgu`.`Tipos_de_Equipamientos`.`suma_clo_para_m`, `sistema_actgu`.`Tipos_de_Equipamientos`.`descripcion_para_m`,\n" +
                    "`sistema_actgu`.`Tipos_de_Equipamientos`.`suma_clo_para_h`, `sistema_actgu`.`Tipos_de_Equipamientos`.`descripcion_para_h`",
                    "`sistema_actgu`.`Tipos_de_Equipamientos`", "", false);
            } else if (tablaName.equals(InspectorONTO.tagTiposActuadores)) {
                // TIPOS DE HVAC
                // Columnas consultadas
                aryNomCols = new String[] {"id_tipo_de_mecanismo_hvac", "tipo_de_mecanismo"};
                // Recuperar información sobre usuarios
                rsDeBD = control_BD.doSelectQuery(
                    "`sistema_actgu`.`Tipos_de_Mecanismos_HVAC`.`id_tipo_de_mecanismo_hvac`,\n" + "`sistema_actgu`.`Tipos_de_Mecanismos_HVAC`.`tipo_de_mecanismo`\n",
                    "`sistema_actgu`.`Tipos_de_Mecanismos_HVAC`", "", false);
            } else if (tablaName.equals(InspectorONTO.tagGrupos)) {
                // GRUPOS
                // Columnas consultadas
                aryNomCols = new String[] {"id_grupo", "nombre"};
                // Recuperar información sobre usuarios
                rsDeBD = control_BD.doSelectQuery(
                    "`sistema_actgu`.`Grupos`.`id_grupo`, `sistema_actgu`.`Grupos`.`nombre`",
                    "`sistema_actgu`.`Grupos`", "", false);
            }
            // Validar resultado
            if (rsDeBD != null) {
                jsResp = resultSet_to_JsonArray(rsDeBD, aryNomCols);
            } else {
                jsResp = new JsonArray();
            }
            // Cerrar conexión con base de datos
            control_BD.closeConnMySQL();
        } else {
            jsResp = new JsonArray();
        }
        // Retorno
        return jsResp;
    }

    // Función que convierte a un ResultSet en un Arreglo Json
    private JsonArray resultSet_to_JsonArray (ResultSet rsDeBD, String[] aryNomCols) {
        // Variables
        String strKeyRS = "";
        String strValRS = "";
        JsonArray jsResp = new JsonArray();
        int numColsConsu = aryNomCols.length;
        // TRY - CATCH
        try {
            // Recorrer datos recuperados
            while (rsDeBD.next()){
                // Crear objeto json temporal
                JsonObject objTemp = new JsonObject();
                // Recorrer columnas de ResultSet
                for (int irs = 0; irs < numColsConsu; irs++) {
                    // Recuperar columna para ResultSet
                    strKeyRS = aryNomCols[irs];
                    // TRY - CATCH
                    try {
                        // Recuperar data de ResultSet
                        strValRS = rsDeBD.getString(strKeyRS);
                    } catch (Exception ei) {
                        // Ajustar data para ResultSet
                        strValRS = "";
                    }
                    // Armar segmentos JSON
                    objTemp.addProperty(strKeyRS, strValRS);
                }
                jsResp.add(objTemp);
            }
        } catch (Exception ex) {
            // Mostrar error
            ex.printStackTrace();
        }
        // Variabla de repuesta
        return jsResp;
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // RECUPERAR una copia de un Arreglo Json con datos de una Tabla
    
    // Función que devuelve datos de Tabla USUARIOS
    public JsonArray recuperarDataUsuarios() {
        // Devolver Arreglo Json
        return jsUsuarios;
    }
    
    // Función que devuelve datos de Tabla HVACs
    public JsonArray recuperarDataHVACs() {
        // Devolver Arreglo Json
        return jsMecHVACs;
    }
    
    // Función que devuelve datos de Tabla GRUPOS
    public JsonArray recuperarDataGrupos() {
        // Devolver Arreglo Json
        return jsGrupos;
    }
    
    // Función que devuelve datos de Tabla ROLES
    public JsonArray recuperarDataRoles() {
        // Devolver Arreglo Json
        return jsRoles;
    }
    
    // Función que devuelve datos de Tabla TIPO DE EQUIPAMIENTOS
    public JsonArray recuperarDataTiposEqpmts() {
        // Devolver Arreglo Json
        return jsTiposEqpmts;
    }
    
    // Función que devuelve datos de Tabla TIPO DE HVAC
    public JsonArray recuperarDataTiposHVAC() {
        // Devolver Arreglo Json
        return jsTiposHVAC;
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Función que ejecuta cambios en un tabla especifica de la BD, segun correspónda
    public String afectarTablaEnBD (String strSol /* Grupo o Usuario */, String strAcc , JsonObject jsonObj) {
        // Variablesgender swap
        String strResp = "";
        // Iniciar conexión con base de datos
        if (control_BD.startConnMySQL()) {
            // ---------------------------
            // Funciones para el "GRUPO"
            if (strSol.equals("grupo")) {
                /* Registrar, actualizar y eliminar */
                if (strAcc.equals("registrar")) {
                    strResp = ejecutarRegistroDeGrupo(jsonObj);
                } else if (strAcc.equals("actualizar")) {
                    strResp = ejecutarActualizacionDeGrupo(jsonObj);
                } else if (strAcc.equals("eliminar")) {
                    strResp = ejecutarEliminacionDeGrupo(jsonObj);
                } else {
                    strResp = "proceso invalido";
                }
            // Funciones para el "USUARIO"
            } else if (strSol.equals("usuario")) {
                /* Registrar, actualizar y eliminar */
                if (strAcc.equals("registrar")) {
                    strResp = ejecutarRegistroDeUsuario(jsonObj);
                } else if (strAcc.equals("actualizar")) {
                    strResp = ejecutarActualizacionDeUsuario(jsonObj);
                } else if (strAcc.equals("eliminar")) {
                    strResp = ejecutarEliminacionDeUsuario(jsonObj);
                } else {
                    strResp = "proceso invalido";
                }
            } else {
                strResp = "proceso invalido";
            }
            // ---------------------------
            // Cerrar conexión con base de datos
            control_BD.closeConnMySQL();
        }
        // Devolver respuesta
        return strResp;
    }
    
    // ACCIONES SOBRE TABLA << GRUPO >>
    
    // INSERTAR
    /* Data JSON: { id_grupo: "", nombre: "", usuarios: [{id_usuario: ""}, ... ] } */
    public String ejecutarRegistroDeGrupo(JsonObject jsonObj) {
        // Recuperar nombre en objeto JSON
        String nombre = jsonObj.get("nombre").toString().replaceAll("\"", "").trim();
        // Validar nombre
        if (nombre.equals("") || nombre.equals("null") || nombre.equals("NaN")) {
            // Regresar error
            return "El nombre del grupo no es un dato correcto, por favor revíselo";
        }
        // ---------------------------
        // Variables
        String strResp = "";
        Boolean datosOk = true;
        // Recuperar datos de objeto JSON
        JsonArray jsAry = jsonObj.getAsJsonArray("usuarios");
        int tamJsonAry = jsAry.size();
        // Recorrer JSON-ARRAY
        for (int x = 0; x < tamJsonAry; x ++) {
            // Recupera datos de sub-objeto JSON
            String idUsr = jsAry.get(x).getAsJsonObject().get("id_usuario").toString().replaceAll("\"", "").trim();
            // Validar sub-datos
            if (idUsr.equals("")|| idUsr.equals("null") || idUsr.equals("NaN") || idUsr.equals("0")) {
                datosOk = false;
                break;
            }
        }
        // Validar ID de Usuario de ocupación
        if (!datosOk) {
            strResp = "Existe un problema con los usuarios seleccionados, por favor revíselos";
            return strResp;
        }
        // ---------------------------
        // Recuperar el ID del último Grupo
        ResultSet rsDeBD = control_BD.doSelectQuery(
            " `sistema_actgu`.`Grupos`.`id_grupo`, `sistema_actgu`.`Grupos`.`nombre` ",
            " `sistema_actgu`.`Grupos` ORDER BY `sistema_actgu`.`Grupos`.`id_grupo` DESC ", "", true);
        // Recuperar el ID
        int ultimoID = 0;
        boolean nomLibre = true;
        // Recorrer datos recuperados
        try {
            while (rsDeBD.next()) {
                // Contar usuarios
                String strValRS = rsDeBD.getString("nombre");
                if (strValRS.equals(nombre)) {
                    nomLibre = false;
                }
                // Tomar ID del grupo
                strValRS = rsDeBD.getString("id_grupo");
                ultimoID = Integer.parseInt(strValRS);
            }
            ultimoID += 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Validar
        if (!nomLibre) {
            strResp = "El nombre registrado, ya se encuentra en uso";
            return strResp;
        }
        // Registrar grupo
        control_BD.doInsertQuery(
            " `sistema_actgu`.`Grupos` ",
            " `id_grupo`, `nombre` ",
            ultimoID + ", '" + nombre + "'");
        // Recorrer JSON-ARRAY
        for (int x = 0; x < tamJsonAry; x ++) {
            // Recupera datos de sub-objeto JSON
            String idUsr = jsAry.get(x).getAsJsonObject().get("id_usuario").toString().replaceAll("\"", "").trim();
            // Registrar sub-registros
            control_BD.doInsertQuery(
                " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos` ",
                " `id_grupo`, `id_usuario` ",
                ultimoID + ", " + idUsr);
        }
        // Devolver respuesta
        return strResp;
    }
    
    // ACTUALIZAR
    /* Data JSON: { id_grupo: "", nombre: "", usuarios: [{id_usuario: ""}, ... ] } */
    public String ejecutarActualizacionDeGrupo(JsonObject jsonObj) {
        // Recuperar nombre en objeto JSON
        String nombre = jsonObj.get("nombre").toString().replaceAll("\"", "").trim();
        String idGrupo = jsonObj.get("id_grupo").toString().replaceAll("\"", "").trim();
        // Validar nombre
        if (nombre.equals("") || nombre.equals("null") || nombre.equals("NaN")) {
            // Regresar error
            return "El nombre del grupo no es un dato correcto, por favor, revíselo";
        }
        // Validar id del grupo
        if (idGrupo.equals("") || idGrupo.equals("null") || idGrupo.equals("NaN") || idGrupo.equals("0")) {
            // Regresar error
            return "El identificador del grupo no es correcto, por favor revíselo";
        }
        // ---------------------------
        // Variables
        String strResp = "";
        Boolean datosOk = true;
        // Recuperar datos de objeto JSON
        JsonArray jsAry = jsonObj.getAsJsonArray("usuarios");
        int tamJsonAry = jsAry.size();
        // Recorrer JSON-ARRAY
        for (int x = 0; x < tamJsonAry; x ++) {
            // Recupera datos de sub-objeto JSON
            String idUsr = jsAry.get(x).getAsJsonObject().get("id_usuario").toString().replaceAll("\"", "").trim();
            // Validar sub-datos
            if (idUsr.equals("")|| idUsr.equals("null") || idUsr.equals("NaN") || idUsr.equals("0")) {
                datosOk = false;
                break;
            }
        }
        // Validar ID de Usuario de ocupación
        if (!datosOk) {
            strResp = "Existe un problema con los usuarios seleccionados, por favor revíselos";
            return strResp;
        }
        // ---------------------------
        // Recuperar el ID de usuarios
        ResultSet rsDeBD = control_BD.doSelectQuery(
            "`sistema_actgu`.`Usuarios_pertenecen_a_Grupos`.`id_usuario` ",
            " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`",
            "`sistema_actgu`.`Usuarios_pertenecen_a_Grupos`.`id_grupo` = " + idGrupo, false);
        // Recuperar el ID
        String strIdesUsrs = "";
        // Recorrer datos recuperados
        try {
            while (rsDeBD.next()) {
                // Recupera ID de usuario
                strIdesUsrs += rsDeBD.getString("id_usuario") + "|";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Validar si se actualiza un grupo con usuarios
        if (!strIdesUsrs.equals("")) {
            // Sacar arreglo con ID de usuarios
            String[] aryIdesUsrs = strIdesUsrs.split("\\|");
            // Recorrer ID de usuarios
            for (int w = 0; w < aryIdesUsrs.length; w++) {
                // Recupera ID de usuario
                String strIdUsr = aryIdesUsrs[w].trim();
                // Validar existencia de ID de usuario
                if (!strIdUsr.equals("")) {
                    // Variables de control
                    boolean permaneceEnGrup = false;
                    // Recorrer JSON-ARRAY
                    for (int x = 0; x < tamJsonAry; x ++) {
                        // Recupera datos de sub-objeto JSON
                        String idUsr = jsAry.get(x).getAsJsonObject().get("id_usuario").toString().replaceAll("\"", "").trim();
                        // Valida si el ID de usuario permanecera en el grupo
                        if (idUsr.equals(strIdUsr)) {
                            // Confirma que permanecera en el grupo
                            permaneceEnGrup = true;
                            break;
                        }
                    }
                    // Valida si no permanecera en el grupo
                    if (!permaneceEnGrup) {
                        // Recuperar el ID de grupos
                        rsDeBD = control_BD.doSelectQuery(
                            "`sistema_actgu`.`Usuarios_pertenecen_a_Grupos`.`id_grupo` ",
                            " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`",
                            "`sistema_actgu`.`Usuarios_pertenecen_a_Grupos`.`id_usuario` = " + strIdUsr, false);
                        // Contator de IDs
                        int numIdesGrps = 0;
                        // Recorrer datos recuperados
                        try {
                            while (rsDeBD.next()) {
                                // Recupera ID de grupo
                                String strIdGrp = rsDeBD.getString("id_grupo");
                                // Validar ID de grupo
                                if (!strIdGrp.equals("")) {
                                    // Contar grupos
                                    numIdesGrps += 1;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // Validar número de grupos
                        if (numIdesGrps <= 1) {
                            // Recuperar el ID de grupos
                            rsDeBD = control_BD.doSelectQuery(
                                "`sistema_actgu`.`Usuarios`.`etiqueta` ",
                                " `sistema_actgu`.`Usuarios`",
                                "`sistema_actgu`.`Usuarios`.`id_usuario` = " + strIdUsr, true);
                            // Contator de IDs
                            String etiqDeUsr = "";
                            // Recorrer datos recuperados
                            try {
                                while (rsDeBD.next()) {
                                    // Recupera etiqueta de usuario
                                    etiqDeUsr = rsDeBD.getString("etiqueta");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // Escribir error y regresarlo
                            strResp = "No se puede retirar al usuario '" + etiqDeUsr + "' de este grupo, ya que no está asignado a algún otro y es necesario que todo usuario pertenezca a al menos un grupo";
                            return strResp;
                        }
                    }
                }
            }
        }
        // ---------------------------
        // Actualizar registro
        control_BD.doUpdateQuery(
            " `sistema_actgu`.`Grupos` ",
            " `nombre` = '" + nombre + "' ",
            " `id_grupo` = " + idGrupo + " ");
        
        // Borrar relaciones del grupo
        control_BD.doDeleteQuery(
            " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos` ",
            " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`.`id_grupo` = " + idGrupo);
        
        // Recorrer JSON-ARRAY
        for (int x = 0; x < tamJsonAry; x ++) {
            // Recupera datos de sub-objeto JSON
            String idUsr = jsAry.get(x).getAsJsonObject().get("id_usuario").toString().replaceAll("\"", "").trim();
            // Registrar sub-registros
            control_BD.doInsertQuery(
                " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos` ",
                " `id_grupo`, `id_usuario` ",
                idGrupo + ", " + idUsr);
        }
        // Devolver respuesta
        return strResp;
    }
    
    // BORRAR
    /* Data JSON: { id_grupo: "" } */
    public String ejecutarEliminacionDeGrupo(JsonObject jsonObj) {
        // Recupera identificador en objeto JSON
        String idGrupo = jsonObj.get("id_grupo").toString().replaceAll("\"", "").trim();
        // Validar id del grupo
        if (idGrupo.equals("") || idGrupo.equals("null") || idGrupo.equals("NaN")) {
            // Regresar error
            return "El identificador del grupo no es correcto, por favor revíselo";
        }
        // ---------------------------
        // Variable
        String strResp = "";
        // Buscar relación USUARIO-GRUPO
        ResultSet rsDeBD = control_BD.doSelectQuery(
            " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`.`id_grupo` ",
            " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos` ",
            " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`.`id_grupo` = " + idGrupo + " ", true);
        // Recuperar el ID
        int numRegistros = 0;
        // Recorrer datos recuperados
        try {
            while (rsDeBD.next()) {
                // Contar usuarios
                String strValRS = rsDeBD.getString("id_grupo");
                numRegistros += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Validar si borrar
        if (numRegistros == 0) {
            // Borrar registro
            control_BD.doDeleteQuery(
                " `sistema_actgu`.`Grupos` ",
                " `sistema_actgu`.`Grupos`.`id_grupo` = " + idGrupo);
        } else {
            strResp = "No es posible eliminar al grupo, ya que existen usuarios relacionados a él";
        }
        // Devolver respuesta
        return strResp;
    }
    
    // ACCIONES SOBRE TABLA << USUARIO >>
    
    // INSERTAR
    /* Data JSON: { id_usuario: "", sexo: "", edad: "", peso: "", altura: "", id_rol: "",
        etiqueta: "", id_equipamiento: "", grupos: [{ id_grupo: "" }, ...] } */
    public String ejecutarRegistroDeUsuario(JsonObject jsonObj) {
        // Recupera datos de objeto JSON
        String etiqueta = jsonObj.get("etiqueta").toString().replaceAll("\"", "").trim();
        String sexo = jsonObj.get("sexo").toString().replaceAll("\"", "").trim();
        String edad = jsonObj.get("edad").toString().replaceAll("\"", "").trim();
        String peso = jsonObj.get("peso").toString().replaceAll("\"", "").trim();
        String altura = jsonObj.get("altura").toString().replaceAll("\"", "").trim();
        String id_rol = jsonObj.get("id_rol").toString().replaceAll("\"", "").trim();
        String id_equipamiento = jsonObj.get("id_equipamiento").toString().replaceAll("\"", "").trim();
        // Recupera datos de objeto JSON - De modelo sencillo
        String condicion_top = jsonObj.get("condicion_top").toString().replaceAll("\"", "").trim();
        String tendencia_top = jsonObj.get("tendencia_top").toString().replaceAll("\"", "").trim();
        String condicion_bottom = jsonObj.get("condicion_bottom").toString().replaceAll("\"", "").trim();
        String tendencia_bottom = jsonObj.get("tendencia_bottom").toString().replaceAll("\"", "").trim();

        // Validar datos recuperados
        if (etiqueta.equals("") || etiqueta.equals("null") || etiqueta.equals("NaN")) {
            return "La etiqueta del usuario no es un dato correcto, por favor revíselo";
        }
        if (sexo.equals("") || sexo.equals("null") || sexo.equals("NaN")) {
            return "El sexo no es un dato correcto, por favor revíselo";
        }
        if (edad.equals("") || edad.equals("null") || edad.equals("NaN") || edad.equals("0")) {
            return "La edad no es un dato correcto, por favor revísela";
        }
        if (peso.equals("") || peso.equals("null") || peso.equals("NaN") || peso.equals("0")) {
            return "El peso no es un dato correcto, por favor revíselo";
        }
        if (altura.equals("") || altura.equals("null") || altura.equals("NaN") || altura.equals("0")) {
            return "La altura no es un dato correcto, por favor revíselo";
        }
        if (id_rol.equals("") || id_rol.equals("null") || id_rol.equals("NaN") || id_rol.equals("0")) {
            return "El rol no es un dato correcto, por favor revísela";
        }
        if (id_equipamiento.equals("") || id_equipamiento.equals("null") || id_equipamiento.equals("NaN") || id_equipamiento.equals("0")) {
            return "La ropa vestida no es un dato correcto, por favor revísela";
        }
        if (condicion_top.equals("") || condicion_top.equals("null") || condicion_top.equals("NaN")) {
            return "La primer temperatura revisada, no es un dato correcto, por favor revísela";
        }
        if (tendencia_top.equals("") || tendencia_top.equals("null") || tendencia_top.equals("NaN")) {
            return "La tendecia de confort respecto de la primer temperatura, no es un dato correcto, por favor revísela";
        }
        if (condicion_bottom.equals("") || condicion_bottom.equals("null") || condicion_bottom.equals("NaN")) {
            return "La segunda temperatura revisada, no es un dato correcto, por favor revísela";
        }
        if (tendencia_bottom.equals("") || tendencia_bottom.equals("null") || tendencia_bottom.equals("NaN")) {
            return "La tendecia de confort respecto de la segunda temperatura, no es un dato correcto, por favor revísela";
        }

        // Atrapar error
        try {
            // Valores de temperatura
            condicion_top = Double.parseDouble(condicion_top) + "";
            condicion_bottom = Double.parseDouble(condicion_bottom) + "" ;
            // Ajustar tendencia, se espera un valor entre 0 y 100
            tendencia_top = Double.parseDouble(tendencia_top) + "";
            tendencia_bottom = Double.parseDouble(tendencia_bottom) + "" ;
            // Validar rango de tendencia
            if ((Double.parseDouble(tendencia_top) < 0) || (50 < Double.parseDouble(tendencia_top))) {
                return "La tendecia de confort respecto de la primer temperatura, se encuentra fuera del rango de 0 a 50, por favor revísela";
            }
            if ((Double.parseDouble(tendencia_bottom) < -50) || (0 < Double.parseDouble(tendencia_bottom))) {
                return "La tendecia de confort respecto de la segunda temperatura, se encuentra fuera del rango de -50 a 0, por favor revísela";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "La temperatura y tendencia se encuentra en un formato inesperado, por favor revíselos";
        }

        // ---------------------------
        // Variable
        Boolean datosOk = true;
        // Recuperar grupos a los que pertenece el usuario
        JsonArray jsAry = jsonObj.getAsJsonArray("grupos");
        int tamJsonAry = jsAry.size();
        int numGrupsAsig = 0;
        // Recorrer JSON-ARRAY
        for (int x = 0; x < tamJsonAry; x ++) {
            // Recupera datos de sub-objeto JSON
            String id_grupo = jsAry.get(x).getAsJsonObject().get("id_grupo").toString().replaceAll("\"", "").trim();
            // Validar sub-datos
            if (id_grupo.equals("") || id_grupo.equals("null") || id_grupo.equals("NaN") || id_grupo.equals("0")) {
                datosOk = false;
                break;
            }
            numGrupsAsig += 1;
        }
        
        // Validar
        if (!datosOk || (numGrupsAsig == 0)) {
            return "Los grupos relacionados con este usuarios no son correctos, por favor, revise estos datos";
        }
        // ---------------------------
        // Recuperar el ID del último Grupo
        ResultSet rsDeBD_Etq = control_BD.doSelectQuery(
            " `sistema_actgu`.`Usuarios`.`id_usuario`, `sistema_actgu`.`Usuarios`.`etiqueta` ",
            " `sistema_actgu`.`Usuarios` ",
            "", false);
        // Valida si se repite la etiqueta
        boolean seRepite = false;
        // Recorrer datos recuperados
        try {
            while (rsDeBD_Etq.next()) {
                // Recupera valor de etiqueta
                String strValRS = rsDeBD_Etq.getString("etiqueta").trim();
                // Validar si la etiqueta no se repite
                if (strValRS.equals(etiqueta)) {
                    // Indicar que se repite la etiqueta
                    seRepite = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Validar si se repirte la etiqueta
        if (seRepite) {
            return "La etiqueta de usuario que intenta ingresar ya existe, por favor, ingrese otra etiqueta";
        }
        // ---------------------------
        // Recuperar el ID del último Grupo
        ResultSet rsDeBD_Ide = control_BD.doSelectQuery(
            " `sistema_actgu`.`Usuarios`.`id_usuario` ",
            " `sistema_actgu`.`Usuarios` ORDER BY `sistema_actgu`.`Usuarios`.`id_usuario` DESC ",
            "", true);
        // Recuperar el ID
        int ultimoID = 0;
        // Recorrer datos recuperados
        try {
            while (rsDeBD_Ide.next()) {
                // Contar usuarios
                String strValRS = rsDeBD_Ide.getString("id_usuario");
                ultimoID = Integer.parseInt(strValRS);
            }
            ultimoID += 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Registrar registro
        control_BD.doInsertQuery(
            " `sistema_actgu`.`Usuarios` ",
            " `id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, " +
            " `id_equipamiento`, `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom` ",
            ultimoID + ", '" + etiqueta + "', '" + sexo + "', " + edad + ", " + altura + ", " + peso + ", " + id_rol + ", " +
            id_equipamiento + ", " + condicion_top + ", " + tendencia_top + ", " + condicion_bottom + ", " + tendencia_bottom);
        
        // Recorrer JSON-ARRAY
        for (int x = 0; x < tamJsonAry; x ++) {
            // Recupera datos de sub-objeto JSON
            String id_grupo = jsAry.get(x).getAsJsonObject().get("id_grupo").toString().replaceAll("\"", "").trim();
            // Validar
            if (!id_grupo.equals("")) {
                // Registrar sub-registros
                control_BD.doInsertQuery(
                    " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos` ",
                    " `id_usuario`, `id_grupo` ",
                    ultimoID + ", " + id_grupo);
            }
        }
        // Devolver respuesta
        return "";
    }
    
    // ACTUALIZAR
    /* Data JSON: { id_usuario: "", sexo: "", edad: "", peso: "", altura: "", id_rol: "",
        etiqueta: "", id_equipamiento: "", grupos: [{ id_grupo: "" }, ...] } */
    public String ejecutarActualizacionDeUsuario(JsonObject jsonObj) {
        // Recupera identificador en objeto JSON
        String id_usuario = jsonObj.get("id_usuario").toString().replaceAll("\"", "").trim();
        // Validar datos
        if (id_usuario.equals("") || id_usuario.equals("null") || id_usuario.equals("NaN") || id_usuario.equals("0")) {
            return "El identificador del usuario no es un dato correcto, por favor revíselo";
        }
        // Recupera datos de objeto JSON
        String etiqueta = jsonObj.get("etiqueta").toString().replaceAll("\"", "").trim();
        String sexo = jsonObj.get("sexo").toString().replaceAll("\"", "").trim();
        String edad = jsonObj.get("edad").toString().replaceAll("\"", "").trim();
        String peso = jsonObj.get("peso").toString().replaceAll("\"", "").trim();
        String altura = jsonObj.get("altura").toString().replaceAll("\"", "").trim();
        String id_rol = jsonObj.get("id_rol").toString().replaceAll("\"", "").trim();
        String id_equipamiento = jsonObj.get("id_equipamiento").toString().replaceAll("\"", "").trim();
        // Recupera datos de objeto JSON - De modelo sencillo
        String condicion_top = jsonObj.get("condicion_top").toString().replaceAll("\"", "").trim();
        String tendencia_top = jsonObj.get("tendencia_top").toString().replaceAll("\"", "").trim();
        String condicion_bottom = jsonObj.get("condicion_bottom").toString().replaceAll("\"", "").trim();
        String tendencia_bottom = jsonObj.get("tendencia_bottom").toString().replaceAll("\"", "").trim();
        // Validar datos recuperados
        if (etiqueta.equals("") || etiqueta.equals("null") || etiqueta.equals("NaN")) {
            return "La etiqueta del usuario no es un dato correcto, por favor revíselo";
        }
        if (sexo.equals("") || sexo.equals("null") || sexo.equals("NaN")) {
            return "El sexo no es un dato correcto, por favor revíselo";
        }
        if (edad.equals("") || edad.equals("null") || edad.equals("NaN") || edad.equals("0")) {
            return "La edad no es un dato correcto, por favor revísela";
        }
        if (peso.equals("") || peso.equals("null") || peso.equals("NaN") || peso.equals("0")) {
            return "El peso no es un dato correcto, por favor revíselo";
        }
        if (altura.equals("") || altura.equals("null") || altura.equals("NaN") || altura.equals("0")) {
            return "La altura no es un dato correcto, por favor revíselo";
        }
        if (id_rol.equals("") || id_rol.equals("null") || id_rol.equals("NaN") || id_rol.equals("0")) {
            return "El rol no es un dato correcto, por favor revísela";
        }
        if (id_equipamiento.equals("") || id_equipamiento.equals("null") || id_equipamiento.equals("NaN") || id_equipamiento.equals("0")) {
            return "La ropa vestida no es un dato correcto, por favor revísela";
        }
        if (condicion_top.equals("") || condicion_top.equals("null") || condicion_top.equals("NaN")) {
            return "La primer temperatura revisada, no es un dato correcto, por favor revísela";
        }
        if (tendencia_top.equals("") || tendencia_top.equals("null") || tendencia_top.equals("NaN")) {
            return "La tendecia de confort respecto de la primer temperatura, no es un dato correcto, por favor revísela";
        }
        if (condicion_bottom.equals("") || condicion_bottom.equals("null") || condicion_bottom.equals("NaN")) {
            return "La segunda temperatura revisada, no es un dato correcto, por favor revísela";
        }
        if (tendencia_bottom.equals("") || tendencia_bottom.equals("null") || tendencia_bottom.equals("NaN")) {
            return "La tendecia de confort respecto de la segunda temperatura, no es un dato correcto, por favor revísela";
        }
        // Atrapar error
        try {
            // Valores de temperatura
            condicion_top = Double.parseDouble(condicion_top) + "";
            condicion_bottom = Double.parseDouble(condicion_bottom) + "" ;
            // Ajustar tendencia, se espera un valor entre 0 y 100
            tendencia_top = Double.parseDouble(tendencia_top) + "";
            tendencia_bottom = Double.parseDouble(tendencia_bottom) + "" ;
            // Validar rango de tendencia
            if ((Double.parseDouble(tendencia_top) < 0) || (50 < Double.parseDouble(tendencia_top))) {
                return "La tendecia de confort respecto de la primer temperatura, se encuentra fuera del rango de 0 a 50, por favor revísela";
            }
            if ((Double.parseDouble(tendencia_bottom) < -50) || (0 < Double.parseDouble(tendencia_bottom))) {
                return "La tendecia de confort respecto de la segunda temperatura, se encuentra fuera del rango de -50 a 0, por favor revísela";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "La temperatura y tendencia se encuentra en un formato inesperado, por favor revíselos";
        }
        // ---------------------------
        // Variable
        Boolean datosOk = true;
        // Recuperar grupos a los que pertenece el usuario
        JsonArray jsAry = jsonObj.getAsJsonArray("grupos");
        int tamJsonAry = jsAry.size();
        int numGrupsAsig = 0;
        // Recorrer JSON-ARRAY
        for (int x = 0; x < tamJsonAry; x ++) {
            // Recupera datos de sub-objeto JSON
            String id_grupo = jsAry.get(x).getAsJsonObject().get("id_grupo").toString().replaceAll("\"", "").trim();
            // Validar sub-datos
            if (id_grupo.equals("") || id_grupo.equals("null") || id_grupo.equals("NaN")) {
                datosOk = false;
                break;
            }
            numGrupsAsig += 1;
        }
        // Validar
        if (!datosOk || (numGrupsAsig == 0)) {
            return "Los grupos relacionados con este usuarios no son correctos, por favor, revise estos datos";
        }
        // ---------------------------
        // Recuperar el ID del último Grupo
        ResultSet rsDeBD_Etq = control_BD.doSelectQuery(
            " `sistema_actgu`.`Usuarios`.`id_usuario`, `sistema_actgu`.`Usuarios`.`etiqueta` ",
            " `sistema_actgu`.`Usuarios` ",
            "", false);
        // Valida si se repite la etiqueta
        boolean seRepite = false;
        // Recorrer datos recuperados
        try {
            while (rsDeBD_Etq.next()) {
                // Recupera valor de etiqueta
                String strValRS = rsDeBD_Etq.getString("etiqueta").trim();
                // Validar si la etiqueta no se repite
                if (strValRS.equals(etiqueta)) {
                    // Recupera valor de ID de usuario
                    String strValID = rsDeBD_Etq.getString("id_usuario").trim();
                    // Validar que el ID de usuario no sea el del usuario actual
                    if (!id_usuario.equals(strValID)) {
                        // Indicar que se repite la etiqueta
                        seRepite = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Validar si se repirte la etiqueta
        if (seRepite) {
            return "La etiqueta de usuario que intenta ingresar ya existe, por favor, ingrese otra etiqueta";
        }
        // ---------------------------
        // Actualizar registro
        control_BD.doUpdateQuery(
            " `sistema_actgu`.`Usuarios` ",
            " `etiqueta` = '" + etiqueta + "', " +
            " `sexo` = '" + sexo + "', " +
            " `edad` = " + edad + ", " +
            " `altura` = " + altura + ", " +
            " `peso` = " + peso + ", " +
            " `id_rol` = " + id_rol + ", " +
            " `id_equipamiento` = " + id_equipamiento + ", " +
            " `condicion_top` = " + condicion_top + ", " +
            " `tendencia_top` = " + tendencia_top + ", " +
            " `condicion_bottom` = " + condicion_bottom + ", " +
            " `tendencia_bottom` = " + tendencia_bottom + " ",
            " `id_usuario` = " + id_usuario + " ");
        // Borrar relaciones
        control_BD.doDeleteQuery(
            " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos` ",
            " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`.`id_usuario` = " + id_usuario);
        // Recorrer JSON-ARRAY
        for (int x = 0; x < tamJsonAry; x ++) {
            // Recupera datos de sub-objeto JSON
            String id_grupo = jsAry.get(x).getAsJsonObject().get("id_grupo").toString().replaceAll("\"", "").trim();
            // Validar
            if (!id_grupo.equals("")) {
                // Registrar sub-registros
                control_BD.doInsertQuery(
                    " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos` ",
                    " `id_usuario`, `id_grupo` ",
                    " " + id_usuario + ", " + id_grupo + " ");
            }
        }
        // Devolver respuesta
        return "";
    }
    
    // ELIMINAR
    /* Data JSON: { id_usuario: "" } */
    public String ejecutarEliminacionDeUsuario(JsonObject jsonObj) {
       // Recupera identificador en objeto JSON
        String id_usuario = jsonObj.get("id_usuario").toString().replaceAll("\"", "").trim();
        // Validar datos
        if (id_usuario.equals("") || id_usuario.equals("null") || id_usuario.equals("NaN") || id_usuario.equals("0")) {
            return "El identificador del usuario no es un dato correcto, por favor revíselo";
        }
        // ---------------------------
        // Borrar relaciones
        control_BD.doDeleteQuery(
            " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos` ",
            " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`.`id_usuario` = " + id_usuario);
        
        // Borrar relaciones
        control_BD.doDeleteQuery(
            " `sistema_actgu`.`Usuarios` ",
            " `sistema_actgu`.`Usuarios`.`id_usuario` = " + id_usuario);
        
        // Devolver respuesta
        return "";
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Función que valida la existencia de un grupo
    public boolean existeGrupo(String idGrupo) {
        // ---------------------------
        // Iniciar conexión con base de datos
        if (control_BD.startConnMySQL()) {
            // Buscar GRUPO
            ResultSet rsDeBD = control_BD.doSelectQuery(
                " `sistema_actgu`.`Grupos`.`id_grupo` ", " `sistema_actgu`.`Grupos` ",
                " `sistema_actgu`.`Grupos`.`id_grupo` = " + idGrupo + " ",
                true);
            // Recuperar el ID
            int numRegistros = 0;
            // Recorrer datos recuperados
            try {
                while (rsDeBD.next()) {
                    // Contar usuarios
                    String strValRS = rsDeBD.getString("id_grupo");
                    numRegistros += 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // ---------------------------
            // Cerrar conexión con base de datos
            control_BD.closeConnMySQL();
            // ---------------------------
            // Validar si existe el grupo
            if (numRegistros == 0) {
                // No existe el grupo
                return false;
            } else {
                // Existe el grupo
                return true;
            }
        }
        // No existe el grupo
        return false;
    }

    public boolean tieneUsuariosElGrupo(String idGrupo) {
        // ---------------------------
        // Iniciar conexión con base de datos
        if (control_BD.startConnMySQL()) {
            // ---------------------------
            // Recuperar el ID de usuarios
            ResultSet rsDeBD = control_BD.doSelectQuery(
                "`sistema_actgu`.`Usuarios_pertenecen_a_Grupos`.`id_usuario` ",
                " `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`",
                "`sistema_actgu`.`Usuarios_pertenecen_a_Grupos`.`id_grupo` = " + idGrupo, false);
            // Recuperar el ID
            int numUsrsInGrp = 0;
            // Recorrer datos recuperados
            try {
                while (rsDeBD.next()) {
                    // Recupera ID de usuario
                    String ideUsr = rsDeBD.getString("id_usuario").trim();
                    // Validar id de usuario
                    if (!ideUsr.equals("")) {
                        // Contar usuario
                        numUsrsInGrp += 1;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // ---------------------------
            // Cerrar conexión con base de datos
            control_BD.closeConnMySQL();
            // ---------------------------
            // Validar si existen usuarios en el grupo
            if (numUsrsInGrp > 0) {
                // Existen usuarios en el grupo
                return true;
            } else {
                // No existen usuarios en el grupo
                return false;
            }
        }
        // No existe el grupo
        return false;
    }
}
