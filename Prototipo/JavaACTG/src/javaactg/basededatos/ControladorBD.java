/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.basededatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Jorge
 */
public class ControladorBD {
    // Variables
    public final Lock monitorBD = new ReentrantLock();
    // Variables - Para conexión
    private String dirIP = "";
    private String port = "";
    private String database = "";
    private String usr = "";
    private String pass = "";
    // Variables - Base
    private final static String drvSQL = "com.mysql.cj.jdbc.Driver";
    private String dirDrv = "jdbc:mysql://";
    // Variables auxiliares
    private Connection ct;
    private Statement st;
    private boolean conectado = false;	
    
    // Constructor del manejador
    public ControladorBD(){
        this.database = "sistema_actgu";
        this.dirIP = "127.0.0.1";
        this.port = "3306";            
        this.usr = "root";
        this.pass = ""; // << Ponerla
        this.dirDrv += this.dirIP + ":" + this.port + "/" + this.database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    }
    
    // Método devuelve el estado de la conexión
    public boolean estaConectado(){
        return this.conectado;
    }
    
    // Método para iniciar conexión con MySQL
    public boolean startConnMySQL(){
        try{
            if (monitorBD.tryLock(1, TimeUnit.SECONDS)) {
                while(this.conectado) {
                    System.out.println("Con.SQL ocupada.");
                }
                if (!this.conectado) {
                    try{
                        Class.forName(this.drvSQL);
                        this.ct = DriverManager.getConnection(this.dirDrv, this.usr, this.pass);
                        this.st = this.ct.createStatement(); // Aqui se crea la conexion a MySQL tras tener los valores necesarios
                        this.conectado = true;
                    }catch(Exception w){
                        w.printStackTrace();
                        this.conectado = false;
                    }
                }
            }
            // System.out.println("Con.SQL iniciada.");
        }catch(Exception e){
            System.out.println("Con.SQL se intento iniciar.");
            e.printStackTrace();
        }
        return this.conectado;
    }
    
    // Método para terminar conexión con MySQL
    public boolean closeConnMySQL(){
        boolean isTheProcessOk = false; 
        try{
            if (this.conectado) {
                try{
                    this.ct.close();
                    this.st.close();
                    this.conectado = false;
                    isTheProcessOk = true;
                } catch(Exception w){
                    w.printStackTrace();
                    this.conectado = true;
                    isTheProcessOk = false;
                }
            } else {
                isTheProcessOk = true;
            }
            monitorBD.unlock();
            // System.out.println("Con.SQL cerrada.");
        }catch (IllegalMonitorStateException ime) {
            System.out.println("Con.SQL cerrada, el candado ya habia sido liberado.");
        }catch(Exception e){
            e.printStackTrace();
        }
        return isTheProcessOk;
    }
    
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // Obtener n filas de x campos de una tabla donde...
    public ResultSet doSelectQuery (String campos, String tabla, String where, boolean soloUno){
        // Variables
        ResultSet resultado = null;
        String consulta = "SELECT "+campos+" FROM "+tabla; 
        // Validaciones
        if (!where.equals("")) {
            consulta += " WHERE " + where;
        }
        if (soloUno) {
            consulta += " LIMIT 1";
        }
        // Por si hay error
        try{
            if (this.conectado) {
                resultado = this.st.executeQuery(consulta);
            }
        } catch(Exception e){
            // Mostrar consulta
            System.out.println("Consulta: " + consulta);
            // Mostrar error
            e.printStackTrace();
        }
        return resultado;
    }
    
    // Registrar datos
    public Boolean doInsertQuery (String tabla, String campos, String datos) {
        Boolean isOk = true;
        try{
            if (this.conectado) {
                String insertar = "INSERT INTO " + tabla + " (" + campos + ") VALUES (" + datos + ")"; 
                int result = this.st.executeUpdate(insertar);
                /*
                    result 1 row without duplicates -> 1
                    result 3 rows without duplicates -> 3
                    result 3 rows with all duplicates and none updating a value -> 0
                    result 3 rows with 2 no-duplicates and 1 duplicate that updates a value -> 3
                    result 3 rows with 2 no-duplicates and 1 duplicate that doesn't update a value -> 2
                */
                if (result > 1) {
                    isOk = false;
                }
            }
        } catch(Exception e){
            isOk = false;
            // Mostrar error
            e.printStackTrace();
        }
        return isOk;
    }
    
    // Actualiar datos
    public Boolean doUpdateQuery (String tabla, String sets, String where) {
        Boolean isOk = true;
        try{
            if (this.conectado) {
                String insertar = "UPDATE " + tabla + " SET " + sets + " WHERE " + where + ""; 
                int result = this.st.executeUpdate(insertar);
                /*
                    result 1 row without duplicates -> 1
                    result 3 rows without duplicates -> 3
                    result 3 rows with all duplicates and none updating a value -> 0
                    result 3 rows with 2 no-duplicates and 1 duplicate that updates a value -> 3
                    result 3 rows with 2 no-duplicates and 1 duplicate that doesn't update a value -> 2
                */
                if (result > 1) {
                    isOk = false;
                }
            }
        } catch(Exception e){
            isOk = false;
            // Mostrar error
            e.printStackTrace();
        }
        return isOk;
    }
    
    // Borrar datos
    public Boolean doDeleteQuery (String tabla, String where) {
        Boolean isOk = true;
        try{
            if (this.conectado) {
                String insertar = "DELETE FROM " + tabla + " WHERE " + where + ""; 
                int result = this.st.executeUpdate(insertar);
                /*
                    result 1 row without duplicates -> 1
                    result 3 rows without duplicates -> 3
                    result 3 rows with all duplicates and none updating a value -> 0
                    result 3 rows with 2 no-duplicates and 1 duplicate that updates a value -> 3
                    result 3 rows with 2 no-duplicates and 1 duplicate that doesn't update a value -> 2
                */
                if (result > 1) {
                    isOk = false;
                }
            }
        } catch(Exception e){
            isOk = false;
            // Mostrar error
            e.printStackTrace();
        }
        return isOk;
    }
}
