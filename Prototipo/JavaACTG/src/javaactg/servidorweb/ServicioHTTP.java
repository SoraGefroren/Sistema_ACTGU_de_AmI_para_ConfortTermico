/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.servidorweb;

import javaactg.Principal;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Jorge
 * Basado en el "Java HTTP Server" construido por "SSaurel",
 * * Sitio: "https://medium.com/@ssaurel/create-a-simple-http-web-server-in-java-3fc12b29d5fd"
 * 
 */
// ------------------------------------------------
// ------------------------------------------------
// Clase que describe al Servicio HTTP
public class ServicioHTTP {
    
    // Socket para la conexión el cliente
    private Socket scktConnect;

    // Función de construcción
    public ServicioHTTP(Socket sckcont) {
        // Asignar variable Socket
        scktConnect = sckcont;
        // Validar si dar más detalle de la conexión
        if (ServidorWeb.verbose) {
            // Preparar y dar formato al idioma Español de la fecha y hora actuales
            // SimpleDateFormat sdFormat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy 'a las' hh.mm.ss aa", new Locale("es_ES"));
            // String strDate = sdFormat.format(new Date());
            // Se imprime la fecha y hora de la conexión
            // System.out.println("Conexión (" + strDate + ")");
        }
    }

    // Función que ejecuta el hilo
    public void beginProcess() {
        // Variables para la gestión de la conexión con el cliente
        // ** Variable "dataOut"
        BufferedOutputStream dataDeSalida = null;
        // ** Variable "in"
        BufferedReader bEntrada = null;
        // ** Variable "out"
        PrintWriter pwSalida = null;
        // ** Variable "fileRequested"
        String strArchSolicitado = null;
        // Para atrapar los errores
        try {
            // Se leen los caracteres que envia el cliente a través del Socket
            // ** Se lee la URL solicitada en BYTES
            // ** Variable "in"
            bEntrada = new BufferedReader(new InputStreamReader(scktConnect.getInputStream()));
            // Se prepara la variable para responder al cliente (Aqui va el encabezado de la petición)
            // ** Variable "out"
            pwSalida = new PrintWriter(scktConnect.getOutputStream());
            // Se prepara la variables para responder al cliente (Aqui van los verdaderos datos solicitados [El cuerpo])
            // ** Variable "dataOut"
            dataDeSalida = new BufferedOutputStream(scktConnect.getOutputStream());

            // Se obtiene y tokeniza la primera línea de la solicitud del cliente
            String strEntrada = bEntrada.readLine();
            // System.out.println("Entrada: " + strEntrada);
            StringTokenizer parse = new StringTokenizer(strEntrada);
            // Se obtiene el método HTTP solicitado y eleva a mayusculas
            String method = parse.nextToken().toUpperCase();
            // Se obtiene el archivo solicitado
            strArchSolicitado = parse.nextToken().toLowerCase();

            // Se valida el tipo de método utilizado en la solicitud
            if (!method.equals("GET")  &&  !method.equals("HEAD")) {
            // SI ES UNA SOLICITUD POST
                // Validar el método utilizado
                if (method.equals("POST")) {
                    // Respuesta de datos de POST
                     JsonObject jsonObjDat = getHeadPostData(strArchSolicitado, bEntrada);
                    String strJsonResp = makeStrResp(strArchSolicitado, jsonObjDat);
                    // Enviar respuesta para usuario
                    webBytesResp(
                            strJsonResp,
                            pwSalida, dataDeSalida,
                            "application/json; charset=utf-8",
                            "HTTP/1.1 200 OK");
                } else {
                    // Validar si dar más detalle de la conexión
                    if (ServidorWeb.verbose) {
                        // Imprimir error de en los métodos
                        System.out.println("Método \"501\" no implementado: " + method + ".");
                    }
                    // Enviar respuesta para usuario
                    webResponse(
                            ServidorWeb.WEB_ROOT,
                            ServidorWeb.METHOD_NOT_SUPPORTED,
                            pwSalida, dataDeSalida,
                            "text/html; charset=utf-8",
                            "HTTP/1.1 501 Not Implemented");
                }
            } else {
            // SI ES UNA SOLICITUD GET
                // Validar si el archivo solicitado es indicado
                if (strArchSolicitado.endsWith("/")) {
                    // Concatenar nombre de archivo por defecto
                    strArchSolicitado += ServidorWeb.DEFAULT_FILE;
                }
                // Se cacula el tipo del archivo solicitado
                String strContentType = getContentType(strArchSolicitado);
                // Si es el método GET se emitira una respuesta
                if (method.equals("GET")) {
                    // Enviar respuesta para usuario
                    webResponse(
                            ServidorWeb.WEB_ROOT,
                            strArchSolicitado,
                            pwSalida, dataDeSalida,
                            strContentType,
                            "HTTP/1.1 200 OK");
                    // Validar si dar más detalle de la conexión
                    if (ServidorWeb.verbose) {
                        // Imprimir mensaje
                        // System.out.println("El archivo \"" + strArchSolicitado + "\" de tipo \"" + strContentType + "\" ha sido enviado.");
                    }
                } else {
                    // Validar si dar más detalle de la conexión
                    if (ServidorWeb.verbose) {
                        // Imprimir mensaje
                        System.err.println("El archivo \"" + strArchSolicitado + "\" de tipo \"" + strContentType + "\" no ha sido enviado - No se solicita por GET.");
                    }
                }
            }
        // -----------
        // EXCEPCIONES
        } catch (FileNotFoundException fnfe) {
            // Para atrapar los errores
            try {
                // Enviar respuesta para usuario
                webResponse(
                        ServidorWeb.WEB_ROOT,
                        ServidorWeb.FILE_NOT_FOUND,
                        pwSalida, dataDeSalida,
                        "text/html; charset=utf-8",
                        "HTTP/1.1 404 File Not Found");
                // Validar si dar más detalle de la conexión
                if (ServidorWeb.verbose) {
                    // Imprimir mensaje
                    System.err.println("El archivo \"" + strArchSolicitado + "\" no ha sido encontrado.");
                }
            } catch (IOException ioe) {
                // Imprimir mensaje de error
                System.err.println("Error con el archivo \"" + ServidorWeb.FILE_NOT_FOUND + "\" al ser enviado: " + ioe.getMessage());
            }
        } catch (IOException ioe) {
                // Imprimir mensaje de error
                System.err.println("Error en el servidor: " + ioe);
        } finally {
            // Para atrapar los errores
            try {
                // Se cierran los medios de comunicación
                bEntrada.close();
                pwSalida.close();
                dataDeSalida.close();
                // Se cierra el Socket (la comunicación)
                scktConnect.close();
            } catch (Exception e) {
                // Imprimir mensaje de error
                System.err.println("Error al cerrar los medios de comunicación: " + e.getMessage());
            } 
            // Validar si dar más detalle de la conexión
            if (ServidorWeb.verbose) {
                // Imprimir mensaje
                // System.out.println("Conexión finalizada.\n");
            }
        }
    }

    // Función para enviar respuesta a usuarios
    private void webResponse (File responsefile, String fileRequested, PrintWriter pwOut, OutputStream dataOut, String contentType, String responseHead) throws IOException {
        // Se carga el archivo solicitado para responder al usuario
        File file = new File(responsefile, fileRequested);
        int fileLength = (int) file.length();
        // Se convierte el archivo solicitado a BYTES
        byte[] fileDataBytes = readFileData(file, fileLength);
        // Encabezados HTTP con datos para el cliente
        pwOut.println(responseHead);
        pwOut.println("Server: Java HTTP Server: 1.0");
        pwOut.println("Date: " + new Date());
        pwOut.println("Content-type: " + contentType);
        pwOut.println("Content-length: " + fileLength);
        pwOut.println(); // Se escribe una línea en blanco para el encabezado ¡Muy importante!
        pwOut.flush(); // Se cierra el Búfer de salida con caracteres vacíos
        // Se envía el archivo
        dataOut.write(fileDataBytes, 0, fileLength);
        dataOut.flush();
    }

    // Función para enviar respuesta a usuarios
    private void webBytesResp (String dataResponse, PrintWriter pwOut, OutputStream dataOut, String contentType, String responseHead) throws IOException {
        // Se carga el archivo solicitado para responder al usuario
        int dataLength = (int) dataResponse.length();
        // Se convierte el archivo solicitado a BYTES
        byte[] strDataBytes = dataResponse.getBytes();
        // Encabezados HTTP con datos para el cliente
        pwOut.println(responseHead);
        pwOut.println("Server: Java HTTP Server: 1.0");
        pwOut.println("Date: " + new Date());
        pwOut.println("Content-type: " + contentType);
        pwOut.println("Content-length: " + dataLength);
        pwOut.println(); // Se escribe una línea en blanco para el encabezado ¡Muy importante!
        pwOut.flush(); // Se cierra el Búfer de salida con caracteres vacíos
        // Se envía el archivo
        dataOut.write(strDataBytes, 0, dataLength);
        dataOut.flush();
    }

    // Función privada para leer archivo de datos
    private byte[] readFileData(File file, int fileLength) throws IOException {
        // Variables para leer archivo
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        // Para atrapar los errores
        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null) 
                fileIn.close();
        }
        // Devolver Bytes leidos
        return fileData;
    }

    // Función que devuelve el tipo de contenido en un archivo
    private String getContentType(String fileRequested) {
        // Variables
        String strCT = "";
        // Validar respuesta
        if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html")) {
            strCT = "text/html; charset=utf-8";
        } else if (fileRequested.endsWith(".css")) {
            strCT = "text/css; charset=utf-8";
        } else if (fileRequested.endsWith(".js")) {
            strCT = "text/javascript; charset=utf-8";
        } else if (fileRequested.endsWith(".json")) {
            strCT = "application/json; charset=utf-8";
        } else if (fileRequested.endsWith(".png")) {
            strCT = "image/png; charset=utf-8";
        } else {
            strCT = "text/plain; charset=utf-8";
        }
        // Devolver datos
        return strCT;
    }

    // Función que extrae los datos de la cabecera POST, a los cuales, se espera como JSON
    private JsonObject getHeadPostData(String strArSoldo, BufferedReader brHeadData) throws IOException {
        // Recuperar todos los datos de cabezara
        StringBuilder strBuildr = new StringBuilder();
        while(brHeadData.ready()){
            // Guardar datos en el String Builder
            strBuildr.append((char) brHeadData.read());
        }                        
        // Convertir StringBuilder en un Array String
        String[] aryDataHead = strBuildr.toString().split("\n");
        int intAryDH = aryDataHead.length;
        String strDataHead = "";
        Boolean boolRD = false;
        // Recorrer Array String y armar String de datos
        for (int i = 0; i < intAryDH; i++) {
            // Validar si ya se deben de leer datos
            if (boolRD) {
                strDataHead += aryDataHead[i];
                // Validar si agregar o no un salto de linea
                if ((i + 1) < intAryDH) {
                    strDataHead += "\n";
                }
            } else {
                // Validar la linea actual
                if (aryDataHead[i].equals("") || aryDataHead[i].equals("\n") || aryDataHead[i].equals("\r")) {
                    boolRD = true;
                }
            }
        }
        //Extrar caden JSON de datos Head
        String[] aryJsonHead = strDataHead.split("jsonData=");
        String strJsonHead = aryJsonHead[aryJsonHead.length - 1];
        // Elementos para resultado de Objeto JSON
        JsonElement jsonElement = null;
        JsonObject jsonObject = null;
        try {
            // Convertir cadena JSON a Objeto JSON
            jsonElement = new JsonParser().parse(strJsonHead);
            jsonObject = jsonElement.getAsJsonObject();
        } catch (Exception exx) {
            // Indicar error
            // exx.printStackTrace();
            System.err.println("Error con el JSON enviado para \"" + strArSoldo + "\": " + exx.getMessage());
        }
        // Regresar datos 
        return jsonObject;
    }

    // ------------------------------------------------
    // ------------------------------------------------
    // ------------------------------------------------
    // ------------------------------------------------

    // Función SINCRONA que analiza el JSON POST y emite una respuesta como JSON-STRING
    private String makeStrResp(String strArcSol, JsonObject jsonObj) {
        // Variables
        String strResp = "";
        String strSolicitud = "";
        String strAccionSol = "";
        String[] arySol = strArcSol.split("/");
        // Selección de acciones
        if (arySol.length > 2) {
            strSolicitud = arySol[arySol.length - 2];
            strAccionSol = arySol[arySol.length - 1];
        } else {
            strSolicitud = arySol[arySol.length - 1];
            strAccionSol = arySol[arySol.length - 1];
        }
        // Tratar de bloquear hilo
        try{
            // ---------------------------------------
            // ---------------------------------------
            // ---------------------------------------
            // Espera a que no se este usando la Ontología
            while (Principal.usingOnto) {
                // Validar TRY-CATCH
                try {
                    // Dormir hilo (1 seg)
                    Thread.sleep(1000);
                } catch(InterruptedException ex) {
                    // Lanzar mensaje de error (1 seg)
                    System.out.println("Suscritor - 1 seg:" + ex.getMessage());
                }
            }
            // Indicar que se usara la Ontología
            Principal.usingOnto = true;
            // Validar TRY-CATCH
            try {
                // Reconstruir Base de Conocimiento
                Principal.updateGestorOnto(true, true);
                // ---------------------------------------
                // ---------------------------------------
                // ---------------------------------------
                // Validar elemento solicitado
                if (strSolicitud.equals("sistema")) {
                    // Validar por data
                    if (jsonObj == null) {
                        // Indicar cadena vacia
                        strResp = "";
                    } else {
                        // Validación acción solicitada
                        if (strAccionSol.equals("informacion")) {
                            // Recupera de la ontología, información especifica sobre una tabla en particular
                            strResp = Principal.gestorONTO.recuperarTabla(jsonObj.get("Tabla").toString().replaceAll("\"", ""));
                            strResp = strResp + "|" + strResp;
                        // Valida si recupera informacióin de sensores
                        } else if (strAccionSol.equals("datos_de_sensor")) {
                            // Recupera información de sensores ambientales
                            strResp = Principal.gestorONTO.recuperarDatosAmbientales(jsonObj);
                            // System.out.println("Datos de sensores... " + strResp);
                            strResp = strResp + "|" + strResp;
                        }
                    }
                // Validar elemento solicitado
                } else if (strSolicitud.equals("configuracion")) {
                    // Valida si recuperar el resultado de las iteraciones
                    if (strAccionSol.equals("resultados")) {
                        // Recuperar los resultados de las iteraciones
                        strResp = Principal.getIterationsResults();
                        strResp = strResp + "|" + strResp;
                    // Valida si actualizar la configuración actual
                    } else if (strAccionSol.equals("actualizar")) {
                        // Validar por data
                        if (jsonObj == null) {
                            // Indicar cadena vacia
                            strResp = "{\"todo\":\"\", \"mensaje\":\"No se logró procesar la solicitud\"}";
                            // Ajustar respuesta resultante del proceso
                            strResp = strResp + "|" + strResp;
                        } else {
                            // Actualiza la configuración de las iteraciones
                            String mjsError = Principal.updateIterationsConfig(jsonObj);
                            // Validar mensaje de error
                            if (mjsError.equals("")) {
                                // Armar respuesta exitosa
                                strResp = "{\"todo\":\"ok\", \"mensaje\":\"\"}";
                            } else {
                                // Armar respuesta con errores
                                strResp = "{\"todo\":\"\", \"mensaje\":\"" + mjsError + "\"}";
                            }
                            // Ajustar respuesta resultante del proceso
                            strResp = strResp + "|" + strResp;
                        }
                    // Valida si recuperar la configuración actual
                    } else if (strAccionSol.equals("esquema")) {
                        // Recupera la configuración de las iteraciones
                        strResp = Principal.getIterationsConfig();
                        // Ajustar respuesta resultante del proceso
                        strResp = strResp + "|" + strResp;
                    }
                // Validar elemento solicitado
                } else if (strSolicitud.equals("usuario") || strSolicitud.equals("grupo")) {
                    // Validar por data
                    if (jsonObj == null) {
                        // Indicar cadena vacia
                        strResp = "{\"todo\":\"\", \"mensaje\":\"No se logró procesar la solicitud\"}";
                        // Ajustar respuesta resultante del proceso
                        strResp = strResp + "|" + strResp;
                    } else {
                        // Validación acción solicitada
                        if (strAccionSol.equals("registrar") || strAccionSol.equals("actualizar") || strAccionSol.equals("eliminar")) {
                            // Invoca mecanismo que ejecuta cambios en la Base de Datos
                            String mjsError = Principal.gestorCRUD.afectarTablaEnBD(strSolicitud, strAccionSol, jsonObj);
                            // Reiniciar Datos de BD retenidos en Memoria como Arreglos JSON
                            Principal.gestorCRUD.cargarJsonArray();
                            // Reconstruir Base de Conocimiento
                            Principal.updateGestorOnto(true, true);
                            // Validar mensaje de error
                            if (mjsError.equals("")) {
                                // Armar respuesta exitosa
                                strResp = "{\"todo\":\"ok\", \"mensaje\":\"\"}";
                            } else {
                                // Armar respuesta con errores
                                strResp = "{\"todo\":\"\", \"mensaje\":\"" + mjsError + "\"}";
                            }
                            // Ajustar respuesta resultante del proceso
                            strResp = strResp + "|" + strResp;
                        }
                    }
                }
                // ---------------------------------------
                // ---------------------------------------
                // ---------------------------------------
            } catch(Exception eoo) {
                // Ajustar variable de respuesta
                strResp = "";
                // Mostrar error al hacer uso de la ontologia
                System.out.println("Error al hacer uso de la ontología:" + eoo.getMessage());
            }
            // Indicar que se dejo de usar la Ontología
            Principal.usingOnto = false;
            // ---------------------------------------
            // ---------------------------------------
            // ---------------------------------------
        // EXCEPCIONES
        } catch(Exception e){
            System.err.println("Error durante la solicitud de << " + strSolicitud + "/" + strAccionSol + " >>");
            e.printStackTrace();
        }
        // Regresar respuesta
        return strResp;
    }

}