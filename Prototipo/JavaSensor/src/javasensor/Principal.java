/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensor;

// Librerias
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javasensor.elements.SensorPersona;
import javasensor.mqtt.PublicadorMQTT;
import javax.swing.JFrame;

/**
 *
 * @author Jorge
 */
public class Principal {
    // VARIABLES DEL SISTEMA
    // ------------------------------------------------
    // Variables finales
    public static final int numElemts_P = 10;
    public static final String ip_servidor_MQTT = "192.168.100.18";
    // Variables estaticas de control
    public static Ventana vtnSim = null;
    public static boolean endApp = false;
    public static SensorPersona[] arySensoresPersona = new SensorPersona[numElemts_P];
    // Valores de acelerometro: ID_USR y (X, Y, Z, MET)
    public static Hashtable<String, double[][]> valores_acelerometro = new Hashtable<String, double[][]>();
    // Valores del ambiente (ACTUALES)
    public static double vlrActl_velocidadaire = 0.1;
    public static double vlrActl_temperatura = 24.0;
    public static double vlrActl_humedad = 40.0;
    public static double vlrActl_gas = 335.0;
    // ------------------------------------------------
    
    // CONFIGURACIÓN
    // ------------------------------------------------
    // Valores de información de iteracion - de control
    public static String vlrPolitica = "";
    public static String vlrEstrategia = "";
    public static String vlrGroupInvsg = "";
    public static boolean vlrBandInicio = false;
    
    public static int vlrMaximas_Iteraciones = 0;
    public static int vlrTiempoDeCada_Iteracion = 0;
    
    // Valores de información de iteracion - de acción 
    public static int vlrNumero_Iteracion = 0;
    public static int vlrDuracion_Iteracion = 0;
    
    public static double vlrBase_velocidadaire = 0.0;
    public static double vlrBase_temperatura = 0.0;
    public static double vlrBase_humedad = 0.0;
    public static double vlrBase_gas = 0.0;
    
    public static double vlrIncr_velocidadaire = 0.0;
    public static double vlrIncr_temperatura = 0.0;
    public static double vlrIncr_humedad = 0.0;
    public static double vlrIncr_gas = 0.0;
    // ------------------------------------------------
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // 1.-
        // Crear data de sensores de personas
        for (int i = 0; i < numElemts_P; i++) {
            // Crear sensor de persona (ACELEROMETROS)
            arySensoresPersona[i] = new SensorPersona(i);
        }
        // 2.-
        // Crear ventana
        vtnSim = new Ventana();
        // Configurar ventana
        vtnSim.setSize(860, 120);
        vtnSim.setResizable(false);
        vtnSim.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        vtnSim.setLocation(dim.width/2 - vtnSim.getPreferredSize().width/2, dim.height/2 - vtnSim.getPreferredSize().height/2);
        vtnSim.setVisible(true);
        // Para atrapar los errores
        try {
            // Tomar tiempo actual
            Date startDate = new Date();
            Long startTime = startDate.getTime();
            // Tomar tiempo actual - Para escribir
            Date iniDate = new Date();
            Long iniTime = iniDate.getTime();
            // 3.-
            // Iniciar ciclo de envío de mensajes
            while (!endApp) {
                // TRY - CATCH
                try {
                    // 4.-
                    // Leer archivo de configuración
                    String strFileDat = "";
                    // Variable para recuperar instancia de archivo
                    boolean isFileOK = false;
                    File myFileObj = null;
                    // Recuperar instancia de archivo
                    while (!isFileOK) {
                        // TRY - CATCH
                        try {
                            // Recupera data de archivo
                            myFileObj = new File("../configsimulacion.json");
                            // Validar que exista el archivo
                            if (myFileObj.exists()) {
                                // Recuperar data de archivo y parsear como JSON
                                Scanner myFileReader = new Scanner(myFileObj);
                                while (myFileReader.hasNextLine()) {
                                    strFileDat += myFileReader.nextLine() + "\n";
                                }
                                myFileReader.close();
                                isFileOK = true;
                            } else {
                                isFileOK = false;
                            }
                        // Excepción
                        } catch (Exception ess) {
                            // Mostrar error, si ocurre
                            System.err.println("(Nivel 1) Ocurrio un error al abrir el archivo: " + ess.getMessage());
                            isFileOK = false;
                        }
                        // Validar apertura de archivo
                        if (!isFileOK) {
                            // TRY - CATCH
                            try {
                                // Recupera data de archivo y parsear como JSON
                                myFileObj = new File("../../configsimulacion.json");
                                // Validar que exista el archivo
                                if (myFileObj.exists()) {
                                    // Recuperar data de archivo y parsear como JSON
                                    Scanner myFileReader = new Scanner(myFileObj);
                                    while (myFileReader.hasNextLine()) {
                                        strFileDat += myFileReader.nextLine() + "\n";
                                    }
                                    myFileReader.close();
                                    isFileOK = true;
                                } else {
                                    isFileOK = false;
                                }
                            // Excepción
                            } catch (Exception ess) {
                                // Mostrar error, si ocurre
                                System.err.println("(Nivel 2) Ocurrio un error al abrir el archivo: " + ess.getMessage());
                                isFileOK = false;
                            }
                        }
                    }
                    // Convertir cadena JSON a Objeto JSON
                    JsonElement jsonElement = new JsonParser().parse(strFileDat);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    JsonObject jsonAux = null;
                    // Recuperar data de configuración - de control
                    String strPolitica = jsonObject.get("politica").toString().replaceAll("\"", "").trim().toLowerCase();
                    String strEstrategia = jsonObject.get("estrategia").toString().replaceAll("\"", "").trim().toLowerCase();
                    String strGroupInvsg = jsonObject.get("grupo_investigado").toString().replaceAll("\"", "").trim().toLowerCase();
                    boolean bandIniciar = Boolean.parseBoolean(jsonObject.get("iniciar").toString().replaceAll("\"", "").trim().toLowerCase());
                    // Recuperar data de configuración - de acción
                    int inMaxNumItos = Integer.parseInt(jsonObject.get("numero_de_iteraciones").toString().replaceAll("\"", "").trim().toLowerCase());
                    int inTiempDeItos = Integer.parseInt(jsonObject.get("duracion_de_iteracion").toString().replaceAll("\"", "").trim().toLowerCase());
                    jsonAux = jsonObject.get("temperatura").getAsJsonObject();
                    double baseTemp = Double.parseDouble(jsonAux.get("valor_inicial").toString().replaceAll("\"", "").trim().toLowerCase());
                    double incrTemp = Double.parseDouble(jsonAux.get("valor_incremento").toString().replaceAll("\"", "").trim().toLowerCase());
                    jsonAux = jsonObject.get("humedad").getAsJsonObject();
                    double baseHume = Double.parseDouble(jsonAux.get("valor_inicial").toString().replaceAll("\"", "").trim().toLowerCase());
                    double incrHume = Double.parseDouble(jsonAux.get("valor_incremento").toString().replaceAll("\"", "").trim().toLowerCase());
                    jsonAux = jsonObject.get("concentracion_gas").getAsJsonObject();
                    double baseCGas = Double.parseDouble(jsonAux.get("valor_inicial").toString().replaceAll("\"", "").trim().toLowerCase());
                    double incrCGas = Double.parseDouble(jsonAux.get("valor_incremento").toString().replaceAll("\"", "").trim().toLowerCase());
                    jsonAux = jsonObject.get("velocidad_aire").getAsJsonObject();
                    double baseVAir = Double.parseDouble(jsonAux.get("valor_inicial").toString().replaceAll("\"", "").trim().toLowerCase());
                    double incrVAir = Double.parseDouble(jsonAux.get("valor_incremento").toString().replaceAll("\"", "").trim().toLowerCase());
                     // 5.-
                    // Actualizar valores de iteración
                    // Validar si existe algún cambio de configuración
                    if (!vlrPolitica.equals(strPolitica) || !vlrEstrategia.equals(strEstrategia) || !vlrGroupInvsg.equals(strGroupInvsg) ||
                        (vlrBandInicio != bandIniciar) || (vlrMaximas_Iteraciones != inMaxNumItos) || (vlrTiempoDeCada_Iteracion != inTiempDeItos) ||
                        (vlrBase_temperatura != baseTemp) || (vlrIncr_temperatura != incrTemp) || (vlrBase_humedad != baseHume) || (vlrIncr_humedad != incrHume) ||
                        (vlrBase_gas != baseCGas) || (vlrIncr_gas != incrCGas) || (vlrBase_velocidadaire != baseVAir) || (vlrIncr_velocidadaire != incrVAir)) {
                        // Mensaje de control
                        System.out.println("Se detecto un cambio en la configuración");
                        // Se REINICIA el proceso de iteración
                        // >> Asignar data de control
                        vlrBandInicio = bandIniciar;
                        vlrPolitica = strPolitica;
                        vlrEstrategia = strEstrategia;
                        vlrGroupInvsg = strGroupInvsg;
                        vlrMaximas_Iteraciones = inMaxNumItos;
                        vlrTiempoDeCada_Iteracion = inTiempDeItos;
                        // >> Asignar data de acción
                        vlrNumero_Iteracion = 0;
                        vlrDuracion_Iteracion = inTiempDeItos;
                        // >> Asignar data de acción - ambiente
                        vlrBase_velocidadaire = baseVAir;
                        vlrIncr_velocidadaire = incrVAir;
                        vlrBase_temperatura = baseTemp;
                        vlrIncr_temperatura = incrTemp;
                        vlrBase_humedad = baseHume;
                        vlrIncr_humedad = incrHume;
                        vlrBase_gas = baseCGas;
                        vlrIncr_gas = incrCGas;
                        // >> Asignar data de acción - ambiente
                        vlrActl_velocidadaire = baseVAir;
                        vlrActl_temperatura = baseTemp;
                        vlrActl_humedad = baseHume;
                        vlrActl_gas = baseCGas;
                    } else {
                        // Validar si iniciar proceso
                        if (vlrBandInicio) {
                            // Se CONTINUA con el proceso de iteración
                            // >> Validar si  continuar, si hay duración
                            if ((0 < vlrDuracion_Iteracion) && (vlrNumero_Iteracion < vlrMaximas_Iteraciones)) {
                                // Mensaje de control
                                System.out.println("Iteraciones en curso");
                                // Tomar nuevo tiempo actual
                                Date endDate = new Date();
                                Long endTime = endDate.getTime();
                                // Validar paso del tiempo (1 segundo)
                                if (((endTime - startTime)/1000) >= 1) {
                                    // Reducir tiempo de iteración
                                    vlrDuracion_Iteracion -= 1;
                                    // Asignar nuevo tiempo actual
                                    startDate = new Date();
                                    startTime = startDate.getTime();
                                }
                                // Mensaje de control
                                System.out.println("Tiempo transcurrido " + vlrDuracion_Iteracion + " segundos ");
                            } else {
                                // >> Validar, si no se ha alcanzado el maximo de iteraciones
                                if (vlrNumero_Iteracion < vlrMaximas_Iteraciones) {
                                    // Mensaje de control
                                    System.out.println("Iteraciones en curso");
                                    // Cambiar de iteración (Va de "0" a "n")
                                    vlrNumero_Iteracion += 1;
                                    // Valida si reiniciar iteración ("0" a "n" < "n")
                                    if (vlrNumero_Iteracion < vlrMaximas_Iteraciones) {
                                        // Reiniciar tiempo de iteración
                                        vlrDuracion_Iteracion = vlrTiempoDeCada_Iteracion;
                                        // (Cada cambio de iteración) Incrementar valores ambientales
                                        vlrActl_velocidadaire += vlrIncr_velocidadaire;
                                        vlrActl_temperatura += vlrIncr_temperatura;
                                        vlrActl_humedad += vlrIncr_humedad;
                                        vlrActl_gas += vlrIncr_gas;
                                        // Mensaje de control
                                        System.out.println("La iteración va en " + vlrNumero_Iteracion + " de "+ vlrMaximas_Iteraciones);
                                    } else {
                                        // Se detienen el conteo de tiempo
                                        vlrDuracion_Iteracion = 0;
                                        // Mensaje de control
                                        System.out.println("El contador se detuvo en " + vlrNumero_Iteracion + " de "+ vlrMaximas_Iteraciones);
                                    }
                                } else {
                                    // Validar si el maximo de iteraciones es 0
                                    if (vlrMaximas_Iteraciones == 0) {
                                        // Mensaje de control
                                        System.out.println("Iteraciones en curso");
                                        // >> Asignar data de acción - ambiente
                                        vlrActl_velocidadaire = vlrBase_velocidadaire;
                                        vlrActl_temperatura = vlrBase_temperatura;
                                        vlrActl_humedad = vlrBase_humedad;
                                        vlrActl_gas = vlrBase_gas;
                                        // Mensaje de control
                                        System.out.println("Iteración especial " + vlrNumero_Iteracion + " de "+ vlrMaximas_Iteraciones);
                                    } else {
                                        // Mensaje de control
                                        System.out.println("Iteraciones finalizadas");
                                    }
                                }
                            }
                        } else {
                            // Mensaje de control
                            System.out.println("Iteraciones en espera");
                            // >> Asignar data de acción - ambiente
                            vlrActl_velocidadaire = vlrBase_velocidadaire;
                            vlrActl_temperatura = vlrBase_temperatura;
                            vlrActl_humedad = vlrBase_humedad;
                            vlrActl_gas = vlrBase_gas;
                        }
                    }
                } catch (Exception exx) {
                    // Mostrar error, si ocurre
                    System.err.println("Ocurrio un en el proceso de análisis del archivo: " + exx.getMessage());
                }
                // 6.-
                // Actualizar valores en ventana de datos
                vtnSim.actualizarAmbtInfo();
                // 7.-
                // Generar mensaje con data de acelerometro y sensores
                String msjParaSrvMQTT = "";
                // Recuperar llaves de diccionario sobre acelerometros
                Enumeration enumKeys = valores_acelerometro.keys();
                while (enumKeys.hasMoreElements()) {
                    // Tomar llave y recuperar arreglo de datos double
                    String msjAcels = "";
                    String strLlv = enumKeys.nextElement().toString();
                    double[][] dataAcels = valores_acelerometro.get(strLlv);
                    // Tomar número de datos en acelerometros
                    int numAcels = dataAcels.length;
                    // Recorrer arreglo de datos
                    for (int i = 0; i < numAcels; i++) {
                        // Armar mensaje (String = "1.0,2.0,3.0|1.0,2.0,3.0|1.0,2.0,3.0")
                        msjAcels += "" + String.format("%.2f", dataAcels[i][0]) + ",";
                        msjAcels += "" + String.format("%.2f", dataAcels[i][1]) + ",";
                        msjAcels += "" + String.format("%.2f", dataAcels[i][2]);
                        // Validar si usar Pipe
                        if ((i + 1) < numAcels) {
                            // Concatenar pipe
                            msjAcels += "|";
                        }
                    }
                    // Validar cantidad de acelerometros data
                    if (0 < numAcels) {
                        // Validar estado del mensaje
                        // String = "IdUsr_1:1.0,2.0,3.0|1.0,2.0,3.0^IdUsr_2:1.0,2.0,3.0|1.0,2.0,3.0"
                        if (msjParaSrvMQTT.equals("")) {
                            // Crear publicadores MQTT
                            msjParaSrvMQTT += (strLlv + ":" + msjAcels);
                        } else {
                            // Crear publicadores MQTT
                            msjParaSrvMQTT += ("^" + (strLlv + ":" + msjAcels));
                        }
                    }
                }
                // Tomar nuevo tiempo actual
                Date finDate = new Date();
                Long finTime = finDate.getTime();
                // Escribe en MQTT cada 5 segs
                // Validar paso del tiempo (5 segundo)
                if (((finTime - iniTime)/1000) >= 5) {
                    // 8.-
                    // Enviar mensaje (SENSORES)
                    PublicadorMQTT pubMQTT_DATA = new PublicadorMQTT("SENSORS", "/SENSORS/DATA/");
                    pubMQTT_DATA.publicarMensaje(vlrBandInicio + "~" + vlrNumero_Iteracion + "~" + vlrMaximas_Iteraciones +
                        "~" + msjParaSrvMQTT + "~" + vlrActl_temperatura +
                        "~" + vlrActl_humedad + "~" + vlrActl_gas +
                        "~" + vlrActl_velocidadaire);
                    System.out.println("-----------------------------");
                    System.out.println("-----------------------------");
                    System.out.println("Bandera: " + vlrBandInicio);
                    System.out.println("N°Ito: " + vlrNumero_Iteracion);
                    System.out.println("Max: " + vlrMaximas_Iteraciones);
                    System.out.println("Amb: " + vlrActl_temperatura + "°C, " + vlrActl_humedad + "%, " + vlrActl_velocidadaire + " m/s y " + vlrActl_gas + "ppm");
                    System.out.println("-----------------------------");
                    System.out.println("-----------------------------");
                    // Asignar nuevo tiempo actual
                    iniDate = new Date();
                    iniTime = iniDate.getTime();
                }
                // Validar TRY-CATCH
                try {
                    // Dormir hilo (1 seg)
                    Thread.sleep(1000);
                } catch(InterruptedException ex) {
                    // Lanzar mensaje de error (1 seg)
                    System.err.println("Principal - 1 seg:" + ex.getMessage());
                }
            }
        } catch (Exception eee) {
            // Se imprime mensaje de error
            System.err.println("Mientras se calculaba el paso del tiempo: " + eee.getMessage());
        }
    }
}
