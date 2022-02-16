/*
 * 
 */
package javaactg.servidorweb;

import java.io.File;
import java.net.ServerSocket;
import javaactg.Principal;

/**
 *
 * @author Jorge
 * Basado en el "Java HTTP Server" construido por "SSaurel",
 * * Sitio: "https://medium.com/@ssaurel/create-a-simple-http-web-server-in-java-3fc12b29d5fd"
 * 
 */
// ------------------------------------------------
// ------------------------------------------------
// Clase que describe al Servicio Web
public class ServidorWeb implements Runnable{
    // Ruta de archivos base del servidor
    public static final File WEB_ROOT = new File("_ArchivosWeb/");
    
    // Archivos base por defecto
    public static final String DEFAULT_FILE = "index.html";
    public static final String FILE_NOT_FOUND = "404.html";
    public static final String METHOD_NOT_SUPPORTED = "not_supported.html";
    
    // Modo detallado
    public static final boolean verbose = true;
    
    // Puerto de escucha
    public static final int PORT = 8088;
    private ServerSocket srvSocket = null;
    
    // Función de construcción
    public ServidorWeb() {
        // Se imprime mensaje de incialización del Servidor
        System.out.println("Servidor iniciado.");    
    }
    
    // Función que ejecuta un hilo de comunicación
    @Override
    public void run() {
        // Indicar que el servidor se ha inicado
        Principal.srvIniciado = true;
         // Para atrapar los errores
        try {
            // unLockSrvWeb();
            // Se crea Socket en un determinado Puerto
            srvSocket = new ServerSocket(PORT);
            // System.out.println("Esperando conexiones en el puerto: " + PORT + " ...");
            // Se imprime mensaje de incialización del Servidor
            Principal.vtnSim.cambiarEdoSrv("Servidor iniciado.");
        } catch (Exception es) {
            // Se imprime mensaje de error
            System.err.println("Error al iniciar el servidor web: " + es.getMessage());
        }
        // Ciclo para mantener al servidor vivo
        while (true) {
            // Para atrapar los errores
            try {
                // Se inicia un ciclo infinito para escuchar las solicitudes de los usuarios
                while (true) {
                    // Se crea un nuevo Servicio HTTP para cada conexión aceptada
                    ServicioHTTP servicioHttp = new ServicioHTTP(srvSocket.accept());
                    // Iniciar proceso de Servicio HTTP
                    servicioHttp.beginProcess();
                }
            } catch (Exception es) {
                // Se imprime mensaje de error
                // es.printStackTrace();
                System.err.println("Error al conectar el servidor web: " + es.getMessage());
            }
            // Validar TRY-CATCH
            try {
                // Dormir hilo (1 seg)
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                // Lanzar mensaje de error (1 seg)
                System.err.println("Servidor - 1 seg:" + ex.getMessage());
            }
        }
    }
}
