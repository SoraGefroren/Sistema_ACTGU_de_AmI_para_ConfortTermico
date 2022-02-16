/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.conforttermico;

/**
 *
 * @author Jorge
 */
public class PMV {
    public double pmv = 0;
    public double ppd = 0;
    public double hl1 = 0;
    public double hl2 = 0;
    public double hl3 = 0;
    public double hl4 = 0;
    public double hl5 = 0;
    public double hl6 = 0;
    public double set = 0;
    public double ta_adj = 0;
    public double tr_adj = 0;
    public double cooling_effect = 0;
    
    // Función que convierte los un valor de TC a String
    public String getTCIndexAsStr () {
        // Recuperar valor contenido para Individual
        String strIndex = "";
        // Componer tipo
        strIndex = convertA55IndexToStr(pmv);
        // Devolver resultado
        return strIndex;
    }
    
    // Función que convierte los un valor de TC a String
    public int getTCIndexAsInt () {
        // Recuperar valor contenido para Individual
        int intIndex = 0;
        // Componer tipo
        intIndex = Integer.parseInt(convertA55IndexToStr(pmv));
        // Devolver resultado
        return intIndex;
    }
    
    // Función que convierte en confort segun ASHRAE55
    public String getConfortASHRAE55 () {
        // Recuperar valor contenido para Individual
        String strIndex = "";
        // Componer tipo
        strIndex = convertA55ToConfort(pmv);
        // Devolver resultado
        return strIndex;
    }
    
    // Función que convierte en su sensación segun ASHRAE55
    public String getSensacionASHRAE55 () {
        String r = convertA55Sensacion(pmv);
        return r;
    }
    
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    // -------------------------------------------------------------
    
    // Función que convierte los un valor de TC a String
    public static String convertTCIndexToStr (double valIntTC) {
        // Recuperar valor contenido para Individual
        String strIndex = "";
        // Componer tipo
        switch ((int) valIntTC) {
            case 1:
                strIndex = "-3";
                break;
            case 2:
                strIndex = "-2";
                break;
            case 3:
                strIndex = "-1";
                break;
            case 5:
                strIndex = "+1";
                break;
            case 6:
                strIndex = "+2";
                break;
            case 7:
                strIndex = "+3";
                break;
            default:
                strIndex = "0";
                break;
        }
        // Devolver resultado
        return strIndex;
    }
    
    // Función que posiciona el valor PMV en una escala del 1 al 7
    public static String convertIndexToStrPos (double dbPmv) {
        // Recuperar valor contenido para Individual
        String strIndex = "";
        // Componer tipo
        if (dbPmv < -2.5)
            strIndex = "1";
        else if (dbPmv < -1.5)
            strIndex = "2";
        else if (dbPmv < -0.5)
            strIndex = "3";
        else if (dbPmv < 0.5)
            strIndex = "4";
        else if (dbPmv < 1.5)
            strIndex = "5";
        else if (dbPmv < 2.5)
            strIndex = "6";
        else
            strIndex = "7";
        // Devolver resultado
        return strIndex;
    }
    
    // Función que convierte los un valor de TC a String
    public static String convertA55IndexToStr (double dbPmv) {
        // Recuperar valor contenido para Individual
        String strIndex = "";
        // Componer tipo
        if (dbPmv < -2.5)
            strIndex = "-3";
        else if (dbPmv < -1.5)
            strIndex = "-2";
        else if (dbPmv < -0.5)
            strIndex = "-1";
        else if (dbPmv < 0.5)
            strIndex = "0";
        else if (dbPmv < 1.5)
            strIndex = "+1";
        else if (dbPmv < 2.5)
            strIndex = "+2";
        else
            strIndex = "+3";
        // Devolver resultado
        return strIndex;
    }
    
    // Función que convierte en confort segun ASHRAE55
    public static String convertA55ToConfort (double dbPmv) {
        // Recuperar valor contenido para Individual
        String strIndex = "";
        // Componer tipo
        if (dbPmv < -2.5)
            strIndex = "Incomodo";
        else if (dbPmv < -1.5)
            strIndex = "Incomodo";
        else if (dbPmv < -0.5)
            strIndex = "Incomodo";
        else if (dbPmv < 0.5)
            strIndex = "Comodo";
        else if (dbPmv < 1.5)
            strIndex = "Incomodo";
        else if (dbPmv < 2.5)
            strIndex= "Incomodo";
        else
            strIndex = "Incomodo";
        // Devolver resultado
        return strIndex;
    }
    
    // Función que convierte en su sensación segun ASHRAE55
    public static String convertA55Sensacion (double dbPmv) {
        String r = "";
        if (dbPmv < -2.5)
            r = "Con mucho frío"; // "Muy frío";
        else if (dbPmv < -1.5)
            r = "Con frío"; // "Frío";
        else if (dbPmv < -0.5)
            r = "Con un poco de frío"; // "Ligeramente frío";
        else if (dbPmv < 0.5)
            r = "Neutral";
        else if (dbPmv < 1.5)
            r = "Con un poco de calor"; // "Ligeramente caliente";
        else if (dbPmv < 2.5)
            r = "Con calor"; // "Caliente";
        else
            r = "Con mucho calor"; // "Muy caliente";
        return r;
    }
    
    // Función que determina un rango de Confort, con base en el valor PMV
    public static String[] determinarResultsAcepts (double nvPMV) {
        // Variable respuesta
        String[] rangPMV = new String[] {"0", "0"};
        // Convertir valor a cadena
        if (nvPMV < -2.5) {
            // Asignar nuevo minimo
            rangPMV[0] = "-3";
        } else if (nvPMV < -1.5) {
            // Asignar nuevo minimo
            rangPMV[0] = "-2";
        } else if (nvPMV < -0.5) {
            // Asignar nuevo minimo
            rangPMV[0] = "-1";
        } else if (nvPMV < 0.5) {
            // Poner en 0
            rangPMV[0] = "0";
            rangPMV[1] = "0";
        } else if (nvPMV < 1.5) {
            // Asignar nuevo maximo
            rangPMV[1] = "+1";
        } else if (nvPMV < 2.5) {
            // Asignar nuevo maximo
            rangPMV[1] = "+2";
        } else {
            // Asignar nuevo maximo
            rangPMV[1] = "+3";
        }
        // Devolver respuesta
        return rangPMV;
    }
}
