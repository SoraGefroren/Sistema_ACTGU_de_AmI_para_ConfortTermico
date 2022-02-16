/*
 * 
 */
package javaactg;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;
import javaactg.basededatos.GestorCRUD;
import javaactg.servidorweb.ServidorWeb;
import javaactg.ontologia.GestorONTO;
import javaactg.mqtt.SuscriptorMQTT;
import javax.swing.JFrame;

/**
 * 
 * 
 *
 * @author Jorge
 */
public class Principal {
    // Variables globales - Ventana
    public static Ventana vtnSim = null;
    public static boolean endApp = false;
    public static boolean usingOnto = false;
    public static boolean srvIniciado = false;
    public static boolean ejecTestInRun = false;
    // Variables globales - De control
    // public static final double rango_de_tolerancia = (100/6)/2; // Aprox. 8.33..
    public static final double rango_de_en_accion = ((100/6)/2)/2; // Aprox. 4.15..
    public static final String ip_servidor_MQTT = "192.168.100.18";
    // Variables estaticas - Base de datos
    public static GestorCRUD gestorCRUD = new GestorCRUD();
    public static GestorONTO gestorONTO = new GestorONTO();
    
    // Arreglo de configuración de Actuadores
    public static JsonArray jsAryConfigAct = new JsonArray();
    // Valores de acelerometro: ID_USR y String [X, Y, Z] --->>> Se usa para calcular el MET
    public static Hashtable<String, String[]> valores_acelerometro = new Hashtable<String, String[]>();
    // Valores del ambiente (Data de sensores del entorno)
    public static double valor_velocidadaire = 0; // 0.1; // 0;
    public static double valor_temperatura = 0; // 24.0; // 0;
    public static double valor_humedad = 0; // 40.0; // 0;
    public static double valor_gas = 0; // 335.0; // 0;
    // TRUE, si existen Acelerometros Reales; FALSE, si no
    public static boolean senAcelerometroHabilitado = false; // Cambiar a TRUE en un contexto real
    public static boolean calcularDataDeAcelerometros = false; // Cambiar a TRUE en un contexto real
    // Variables referentes a topicos
    public static final String topicSensors = "/SENSORS/DATA/";
    public static final String topicActuators = "/CONFIG/HVACS/";
    // CONFIGURACIÓN
    // ------------------------------------------------
    // Valores de información de iteracion
    public static String vlrPolitica = "";
    public static String vlrEstrategia = "";
    public static String vlrGroupInvsg = "";
    public static boolean vlrBandInicio = false;
    
    public static int vlrMaximas_Iteraciones = 0;
    public static int vlrTiempoDeCada_Iteracion = 0;
    
    public static double vlrBase_velocidadaire = 0.0;
    public static double vlrBase_temperatura = 0.0;
    public static double vlrBase_humedad = 0.0;
    public static double vlrBase_gas = 0.0;
    
    public static double vlrIncr_velocidadaire = 0.0;
    public static double vlrIncr_temperatura = 0.0;
    public static double vlrIncr_humedad = 0.0;
    public static double vlrIncr_gas = 0.0;
    
    public static Hashtable<String, String> resultsDeIteracion = new Hashtable<String, String>();
    // ------------------------------------------------
    /**
     * @param args the command line arguments
     */
    public  static void main(String[] args) {
        // Inicializa la configuración para iterar
        startIterationsConfig();
        // Crear ventana
        vtnSim = new Ventana();
        // Configurar ventana
        vtnSim.setResizable(false);
        vtnSim.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        vtnSim.setLocation(dim.width/2 - vtnSim.getPreferredSize().width/2, dim.height/2 - vtnSim.getPreferredSize().height/2);
        // Mostrar ventana
        vtnSim.setVisible(true);
        // Crear y arracar instancias MQTT
        SuscriptorMQTT cliMQTT = new SuscriptorMQTT(topicSensors, "SENSORS");
        // Conectar clientes MQTT
        cliMQTT.conectar();
        // Actualizar Base de Conocimiento
        updateGestorOnto(false, false);
        // Crear y arrancar instancia de Servidor Web
        ServidorWeb srvWeb = new ServidorWeb();
        Thread threadWeb = new Thread(srvWeb);
        threadWeb.start();
        // Se incia con un ciclo infinito para mantener ejecusión del programa
        while (!endApp) {}
    }
    
    // Función que actualiza la Ontología
    public static void updateGestorOnto(boolean reiniciarKB, boolean reconstruirKB) {
        // Validar si actualizar o reiniciar Ontología
        if (reiniciarKB) {
            // Validar si solo reiniciar o reconstruir
            if (reconstruirKB) {
                // Inicializar ontologia
                gestorONTO.reconstruirOntologia();
            } else {
                // Inicializar ontologia
                gestorONTO.reiniciarDataDeBD();
            }
        }
        // Actualizar datos ambientales y MET de usuarios en la ontología
        gestorONTO.actualizarDataDeAmbiente();
        // Actualizar configuración de actuadores en la ontología
        gestorONTO.actualizarDataDeHVACs();
        // Generar Archivo OWL de la Ontología
        gestorONTO.generarArchivoOWL();
    }
    
    // Función que recupera los resultados obtenidos en las iteraciones
    public static String getIterationsResults() {
        // Variables
        String strResp = "";
        // Recorrer diccionario de resultados
        Enumeration enumllaves = Principal.resultsDeIteracion.keys();
        while (enumllaves.hasMoreElements()) {
            // Recuperar llave y resultado de confort
            String strKey = enumllaves.nextElement().toString();
            String strRsl = Principal.resultsDeIteracion.get(strKey);
            // Armar resultado:
            strResp += "{";
            strResp += "\"iteracion\":" + "\"" + strKey + "\",";
            strResp += "\"resultado\":" + "{" + strRsl + "}";
            strResp += "},";
        }
        // Ajustar Data StrJSON
        if (!strResp.equals("")) {
            // Remover la última coma de la cadena
            strResp = strResp.substring(0, strResp.length() - 1);
        }
        strResp = "[" + strResp + "]";
        // Devolver respuesta
        return strResp;
    }
    
    // Función para actualiza la configuración de las iteraciones
    public static String updateIterationsConfig(JsonObject jsonObj) {
        // Variables
        String mjsError = "";
        
        // 1.-
        // Recupera grupo a actualizar en configuración de iteraciones
        String grupo_investigado = jsonObj.get("grupo_investigado").toString().replaceAll("\"", "").trim();
        // Validar si el formato del grupo es correcto
        if (grupo_investigado.equals("NaN") || grupo_investigado.equals("null") ||
            grupo_investigado.equals("") || !gestorCRUD.existeGrupo(grupo_investigado)) {
            // Devolver error encontrado
            return "El grupo seleccionado para trabajar es invalido, por favor, eliga otro grupo";
        }
        
        // Validar si que el grupo seleccionado tenga usuarios
        if (!gestorCRUD.tieneUsuariosElGrupo(grupo_investigado)) {
            // Devolver error encontrado
            return "El grupo seleccionado no cuenta con usuarios asignados a él, por favor, eliga otro grupo";
        }
        
        // 2.-
        // Recupera politica a actualizar en configuración de iteraciones
        String politica = jsonObj.get("politica").toString().replaceAll("\"", "").trim();
         // Validar si la politica es correcta
        if (!(politica.equals("ACTGU") || politica.equals("ASHRAE"))) {
            // Devolver error encontrado
            return "Por favor, indique si se aplicaran los métodos tradicionales descritos en el estándar ASHRAE 55";
        }
        
        // 3.-
        // Recupera estrategia a actualizar en configuración de iteraciones
        int vlaInEstra = 0;
        String estrategia = jsonObj.get("estrategia").toString().replaceAll("\"", "").trim();
        // TRY - CATCH
        try {
            // Validar formato del dato
            vlaInEstra = Integer.parseInt(estrategia);
            // Validar rango de estrategia
            if ((vlaInEstra < 1) || (6 < vlaInEstra)) {
                // Indicar error
                mjsError = "La estrategia elegida es inválida, por favor seleccione otra estrategia";
            }
        } catch (Exception e) {
            // Indicar error
            mjsError = "La estrategia elegida tiene un formato inválido, por favor seleccione otra estrategia";
        }
        // Validar si existe un error
        if (!mjsError.equals("")) {
            // Devolver error encontrado
            return mjsError;
        }
        
        // 4 .-
        // Recupera bandera sobre inicio del proceso de iteraciones
        boolean baniniciar = false;
        String iniciar = jsonObj.get("iniciar").toString().replaceAll("\"", "").toLowerCase().trim();
        // TRY - CATCH
        try {
            // Validar formato del dato
            baniniciar = Boolean.parseBoolean(iniciar);
        } catch (Exception e) {
            // Indicar error
            mjsError = "La orden para dar inicio con el proceso de iteración ha sido ingresada de forma incorrecta, por favor, inténtelo de nuevo";
        }
        // Validar si existe un error
        if (!mjsError.equals("")) {
            // Devolver error encontrado
            return mjsError;
        }
        
        // 5.-
        // Recupera data para actualizar configuración de iteraciones
        int num_de_iteraciones = 0;
        int dur_de_iteracion = 0;
        String numero_de_iteraciones = jsonObj.get("numero_de_iteraciones").toString().replaceAll("\"", "").trim();
        String duracion_de_iteracion = jsonObj.get("duracion_de_iteracion").toString().replaceAll("\"", "").trim();
        // TRY - CATCH
        try {
            // Validar formato del dato
            num_de_iteraciones = Integer.parseInt(numero_de_iteraciones);
            dur_de_iteracion = Integer.parseInt(duracion_de_iteracion);
        } catch (Exception e) {
            // Indicar error
            mjsError = "El número de iteraciones o su duración tienen un formato invalido, por favor, revise estos datos";
        }
        // Validar si existe un error
        if (!mjsError.equals("")) {
            // Devolver error encontrado
            return mjsError;
        }
        
        // 6.-
        // Recupera condiciones base y su incremento a actualizar en configuración de iteraciones
        double vlaTemperatura_inicial = 0;
        double vlaTemperatura_incremento = 0;
        JsonObject jsonAux = jsonObj.get("temperatura").getAsJsonObject();
        String temperatura_inicial = jsonAux.get("valor_inicial").toString().replaceAll("\"", "").trim();
        String temperatura_incremento = jsonAux.get("valor_incremento").toString().replaceAll("\"", "").trim();
        
        double vlaHumedad_inicial = 0;
        double vlaHumedad_incremento = 0;
        jsonAux = jsonObj.get("humedad").getAsJsonObject();
        String humedad_inicial = jsonAux.get("valor_inicial").toString().replaceAll("\"", "").trim();
        String humedad_incremento = jsonAux.get("valor_incremento").toString().replaceAll("\"", "").trim();
        
        double vlaConGas_inicial = 0;
        double vlaConGas_incremento = 0;
        jsonAux = jsonObj.get("concentracion_gas").getAsJsonObject();
        String concentracion_gas_inicial = jsonAux.get("valor_inicial").toString().replaceAll("\"", "").trim();
        String concentracion_gas_incremento = jsonAux.get("valor_incremento").toString().replaceAll("\"", "").trim();
        
        double vlaVelAire_inicial = 0;
        double vlaVelAire_incremento = 0;
        jsonAux = jsonObj.get("velocidad_aire").getAsJsonObject();
        String velocidad_aire_inicial = jsonAux.get("valor_inicial").toString().replaceAll("\"", "").trim();
        String velocidad_aire_incremento = jsonAux.get("valor_incremento").toString().replaceAll("\"", "").trim();
        // TRY - CATCH
        try {
            // Validar formato del dato
            vlaTemperatura_inicial = Double.parseDouble(temperatura_inicial);
            vlaTemperatura_incremento = Double.parseDouble(temperatura_incremento);
            vlaHumedad_inicial = Double.parseDouble(humedad_inicial);
            vlaHumedad_incremento = Double.parseDouble(humedad_incremento);
            vlaConGas_inicial = Double.parseDouble(concentracion_gas_inicial);
            vlaConGas_incremento = Double.parseDouble(concentracion_gas_incremento);
            vlaVelAire_inicial = Double.parseDouble(velocidad_aire_inicial);
            vlaVelAire_incremento = Double.parseDouble(velocidad_aire_incremento);
            // Ajuste numerico
            temperatura_inicial = String.format("%.2f", vlaTemperatura_inicial);
            temperatura_incremento = String.format("%.2f", vlaTemperatura_incremento);
            humedad_inicial = String.format("%.2f", vlaHumedad_inicial);
            humedad_incremento = String.format("%.2f", vlaHumedad_incremento);
            concentracion_gas_inicial = String.format("%.2f", vlaConGas_inicial);
            concentracion_gas_incremento = String.format("%.2f", vlaConGas_incremento);
            velocidad_aire_inicial = String.format("%.2f", vlaVelAire_inicial);
            velocidad_aire_incremento = String.format("%.2f", vlaVelAire_incremento);
            
        } catch (Exception e) {
            // Indicar error
            mjsError = "Las condiciones iniciales ingresadas y su incremento para cada iteración tienen un formato invalido, por favor, revise estos datos";
        }
        // Validar si existe un error
        if (!mjsError.equals("")) {
            // Devolver error encontrado
            return mjsError;
        }
        
        // 7.-
        // Actualizar valores locales
        // TRY - CATCH
        try {
            // Actualizar data local
            vlrBandInicio = baniniciar;
            vlrPolitica = politica;
            vlrEstrategia = estrategia;
            vlrGroupInvsg = grupo_investigado;

            vlrMaximas_Iteraciones = num_de_iteraciones;
            vlrTiempoDeCada_Iteracion = dur_de_iteracion;

            vlrBase_velocidadaire = Double.parseDouble(velocidad_aire_inicial);
            vlrBase_temperatura = Double.parseDouble(temperatura_inicial);
            vlrBase_humedad = Double.parseDouble(humedad_inicial);
            vlrBase_gas = Double.parseDouble(concentracion_gas_inicial);

            vlrIncr_velocidadaire = Double.parseDouble(velocidad_aire_incremento);
            vlrIncr_temperatura = Double.parseDouble(temperatura_incremento);
            vlrIncr_humedad = Double.parseDouble(humedad_incremento);
            vlrIncr_gas = Double.parseDouble(concentracion_gas_incremento);
            
            // Validar si la bandera de inicio indica FALSE
            if (!vlrBandInicio) {
                // Limpiar Tabla
                Principal.resultsDeIteracion.clear();
            }
                    
        } catch (Exception e) {
            // Indicar error
            mjsError = "Ocurrio un error al intentar actualizar la configuración o reiniciar el calculo de Confort";
        }
        
        // Validar si existe un error
        if (!mjsError.equals("")) {
            // Devolver error encontrado
            return mjsError;
        }
        
        // Construir string de datos JSON para archivo de configuración
        String configJson = "{" + "\n";
        // Validar si es TRUE o FALSE
        if (vlrBandInicio) {
            configJson += "\"iniciar\":\"" + "true" + "\"," + "\n";
        } else {
            configJson += "\"iniciar\":\"" + "false" + "\"," + "\n";
        }
	
	configJson += "\"politica\":\"" + vlrPolitica + "\"," + "\n";
	configJson += "\"estrategia\":\"" + vlrEstrategia + "\"," + "\n";
        
	configJson += "\"numero_de_iteraciones\":\"" + vlrMaximas_Iteraciones + "\"," + "\n";
	configJson += "\"duracion_de_iteracion\":\"" + vlrTiempoDeCada_Iteracion + "\"," + "\n";
        
	configJson += "\"temperatura\":{" + "\n";
	configJson += "\"valor_inicial\":\"" + vlrBase_temperatura + "\"," + "\n";
	configJson += "\"valor_incremento\":\"" + vlrIncr_temperatura + "\"" + "\n";
	configJson += "}," + "\n";
	configJson += "\"humedad\":{" + "\n";
	configJson += "\"valor_inicial\":\"" + vlrBase_humedad + "\"," + "\n";
	configJson += "\"valor_incremento\":\"" + vlrIncr_humedad + "\"" + "\n";
	configJson += "}," + "\n";
	configJson += "\"concentracion_gas\":{" + "\n";
	configJson += "\"valor_inicial\":\"" + vlrBase_gas + "\"," + "\n";
	configJson += "\"valor_incremento\":\"" + vlrIncr_gas + "\"" + "\n";
	configJson += "}," + "\n";
	configJson += "\"velocidad_aire\":{" + "\n";
	configJson += "\"valor_inicial\":\"" + vlrBase_velocidadaire + "\"," + "\n";
	configJson += "\"valor_incremento\":\"" + vlrIncr_velocidadaire + "\"" + "\n";
	configJson += "}," + "\n";
	configJson += "\"grupo_investigado\":\"" + vlrGroupInvsg + "\"" + "\n";
        
        configJson += "}";
        // Variable para recuperar instancia de archivo
        boolean isFileOK = false;
        // Recuperar instancia de archivo
        while (!isFileOK) {
            // TRY - CATCH
            try {
                // Recupera data de archivo y parsear como JSON
                File myFileObj = new File("../configsimulacion.json");
                // Validar que exista el archivo
                if (!myFileObj.exists()) {
                    // Crear archivo si no existe
                    myFileObj.createNewFile();
                }
                // Crear escritor de datos en archivo
                FileWriter fileWriter = new FileWriter(myFileObj);
                fileWriter.write(configJson);
                fileWriter.close();
                isFileOK = true;
            // Excepción
            } catch (Exception ess) {
                // Mostrar error, si ocurre
                System.out.println("Ocurrio un error al abrir el archivo: " + ess.getMessage());
                isFileOK = false;
            }
        }
        // Devolver estado del proceso
        return mjsError;
    }
    
    // Función que recupera la configuración de las iteraciones
    public static String getIterationsConfig() {
        // Leer archivo de configuración
        String strFileDat = "";
        // Variable para recuperar instancia de archivo
        boolean isFileOK = false;
        File myFileObj = null;
        // Recuperar instancia de archivo
        while (!isFileOK) {
            // TRY - CATCH
            try {
                // Recupera data de archivo y parsear como JSON
                myFileObj = new File("../configsimulacion.json");
                Scanner myFileReader = new Scanner(myFileObj);
                while (myFileReader.hasNextLine()) {
                    strFileDat += myFileReader.nextLine() + "\n";
                }
                myFileReader.close();
                isFileOK = true;
            // Excepción
            } catch (Exception ess) {
                // Mostrar error, si ocurre
                System.out.println("Ocurrio un error al abrir el archivo: " + ess.getMessage());
                isFileOK = false;
            }
        }
        // Devolver datos leidos de archivo
        return strFileDat;
    }
    
    // Función que recupera e inicializa la configuración de iteraciones
    public static void startIterationsConfig() {
        // Recuperar STRING de configuración
        String strJson = getIterationsConfig();
        // Convertir cadena JSON a Objeto JSON
        JsonElement jsonElm = new JsonParser().parse(strJson);
        JsonObject jsonObj = jsonElm.getAsJsonObject();
        // Recuperar elementos de configuración
        
        // 1.-
        // Recupera grupo a actualizar en configuración de iteraciones
        String grupo_investigado = jsonObj.get("grupo_investigado").toString().replaceAll("\"", "").trim();
        
         // Validar si el formato del grupo es correcto
        if (grupo_investigado.equals("NaN") || grupo_investigado.equals("null") ||
            grupo_investigado.equals("") || !gestorCRUD.existeGrupo(grupo_investigado)) {
            // Devolver error encontrado
            System.err.println("Ocurrio un error en relación al grupo elegido en la configuración de iteración");
        }
        
        // 2.-
        // Recupera politica a actualizar en configuración de iteraciones
        String politica = jsonObj.get("politica").toString().replaceAll("\"", "").trim();
         // Validar si la politica es correcta
        if (!(politica.equals("ACTGU") || politica.equals("ASHRAE"))) {
            // Devolver error encontrado
            System.err.println("Ocurrio un error en relación a la politica de iteración utilizada");
        }
        
        // 3.-
        // Recupera estrategia a actualizar en configuración de iteraciones
        int vlaInEstra = 0;
        String estrategia = jsonObj.get("estrategia").toString().replaceAll("\"", "").trim();
        // TRY - CATCH
        try {
            // Validar formato del dato
            vlaInEstra = Integer.parseInt(estrategia);
            // Validar rango de estrategia
            if ((vlaInEstra < 1) || (6 < vlaInEstra)) {
                // Indicar error
                System.err.println("Ocurrio un error en relación a la estrategia de iteración elegida");
            }
        } catch (Exception e) {
            // Indicar error
            System.err.println("Ocurrio un error en relación a la estrategia de iteración seleccionada");
        }
        
        // 4 .-
        // Recupera bandera sobre inicio del proceso de iteraciones
        boolean baniniciar = false;
        String iniciar = jsonObj.get("iniciar").toString().replaceAll("\"", "").toLowerCase().trim();
        // TRY - CATCH
        try {
            // Validar formato del dato
            baniniciar = Boolean.parseBoolean(iniciar);
        } catch (Exception e) {
            // Indicar error
            System.err.println("Ocurrio un error en relación al indicador de inicio del proceso de iteración");
        }
        
        // 5.-
        // Recupera data para actualizar configuración de iteraciones
        int num_de_iteraciones = 0;
        int dur_de_iteracion = 0;
        String numero_de_iteraciones = jsonObj.get("numero_de_iteraciones").toString().replaceAll("\"", "").trim();
        String duracion_de_iteracion = jsonObj.get("duracion_de_iteracion").toString().replaceAll("\"", "").trim();
        // TRY - CATCH
        try {
            // Validar formato del dato
            num_de_iteraciones = Integer.parseInt(numero_de_iteraciones);
            dur_de_iteracion = Integer.parseInt(duracion_de_iteracion);
        } catch (Exception e) {
            // Indicar error
            System.err.println("Ocurrio un error en relación al número o duración de las iteraciones configuradas");
        }
        
        // 6.-
        // Recupera condiciones base y su incremento a actualizar en configuración de iteraciones
        double vlaTemperatura_inicial = 0;
        double vlaTemperatura_incremento = 0;
        JsonObject jsonAux = jsonObj.get("temperatura").getAsJsonObject();
        String temperatura_inicial = jsonAux.get("valor_inicial").toString().replaceAll("\"", "").trim();
        String temperatura_incremento = jsonAux.get("valor_incremento").toString().replaceAll("\"", "").trim();
        
        double vlaHumedad_inicial = 0;
        double vlaHumedad_incremento = 0;
        jsonAux = jsonObj.get("humedad").getAsJsonObject();
        String humedad_inicial = jsonAux.get("valor_inicial").toString().replaceAll("\"", "").trim();
        String humedad_incremento = jsonAux.get("valor_incremento").toString().replaceAll("\"", "").trim();
        
        double vlaConGas_inicial = 0;
        double vlaConGas_incremento = 0;
        jsonAux = jsonObj.get("concentracion_gas").getAsJsonObject();
        String concentracion_gas_inicial = jsonAux.get("valor_inicial").toString().replaceAll("\"", "").trim();
        String concentracion_gas_incremento = jsonAux.get("valor_incremento").toString().replaceAll("\"", "").trim();
        
        double vlaVelAire_inicial = 0;
        double vlaVelAire_incremento = 0;
        jsonAux = jsonObj.get("velocidad_aire").getAsJsonObject();
        String velocidad_aire_inicial = jsonAux.get("valor_inicial").toString().replaceAll("\"", "").trim();
        String velocidad_aire_incremento = jsonAux.get("valor_incremento").toString().replaceAll("\"", "").trim();
        // TRY - CATCH
        try {
            // Validar formato del dato
            vlaTemperatura_inicial = Double.parseDouble(temperatura_inicial);
            vlaTemperatura_incremento = Double.parseDouble(temperatura_incremento);
            vlaHumedad_inicial = Double.parseDouble(humedad_inicial);
            vlaHumedad_incremento = Double.parseDouble(humedad_incremento);
            vlaConGas_inicial = Double.parseDouble(concentracion_gas_inicial);
            vlaConGas_incremento = Double.parseDouble(concentracion_gas_incremento);
            vlaVelAire_inicial = Double.parseDouble(velocidad_aire_inicial);
            vlaVelAire_incremento = Double.parseDouble(velocidad_aire_incremento);
            // Ajuste numerico
            temperatura_inicial = String.format("%.2f", vlaTemperatura_inicial);
            temperatura_incremento = String.format("%.2f", vlaTemperatura_incremento);
            humedad_inicial = String.format("%.2f", vlaHumedad_inicial);
            humedad_incremento = String.format("%.2f", vlaHumedad_incremento);
            concentracion_gas_inicial = String.format("%.2f", vlaConGas_inicial);
            concentracion_gas_incremento = String.format("%.2f", vlaConGas_incremento);
            velocidad_aire_inicial = String.format("%.2f", vlaVelAire_inicial);
            velocidad_aire_incremento = String.format("%.2f", vlaVelAire_incremento);
            
        } catch (Exception e) {
            // Indicar error
            System.err.println("Ocurrio un error en relación a las condiciones de la configuración de iteraciones");
        }
        
        // 7.-
        // Actualizar valores locales
        // TRY - CATCH
        try {
            // Actualizar data local
            vlrBandInicio = baniniciar;
            vlrPolitica = politica;
            vlrEstrategia = estrategia;
            vlrGroupInvsg = grupo_investigado;

            vlrMaximas_Iteraciones = num_de_iteraciones;
            vlrTiempoDeCada_Iteracion = dur_de_iteracion;

            vlrBase_velocidadaire = Double.parseDouble(velocidad_aire_inicial);
            vlrBase_temperatura = Double.parseDouble(temperatura_inicial);
            vlrBase_humedad = Double.parseDouble(humedad_inicial);
            vlrBase_gas = Double.parseDouble(concentracion_gas_inicial);

            vlrIncr_velocidadaire = Double.parseDouble(velocidad_aire_incremento);
            vlrIncr_temperatura = Double.parseDouble(temperatura_incremento);
            vlrIncr_humedad = Double.parseDouble(humedad_incremento);
            vlrIncr_gas = Double.parseDouble(concentracion_gas_incremento);
            
            // Limpiar Tabla de resultado de iteraciones
            Principal.resultsDeIteracion.clear();
                    
        } catch (Exception e) {
            // Indicar error
            System.err.println("Ocurrio un error al intentar inicializar la configuración de las iteraciones");
        }
    }
    
}
