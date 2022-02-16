/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.ontologia.modelo.utiles;

// Librerias
import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 *
 * @author Jorge
 */
public class ManejadorDeLabelList {
   // Variables
    private NodeList nodeList = null;
    
    // Constructor
    public ManejadorDeLabelList(NodeList nList) {
        // Asignar
        this.nodeList = nList;
    }
    
    // Devolver número de elementos
    public int getLength() {
        return this.nodeList.getLength();
    }
    
    // Devolver el item de la posición x
    public ManejadorDeLabel getLabel(int x) {
        // Recuperar NODE item
        Node item = this.nodeList.item(x);
        // Recuperar Label de la lista
        ManejadorDeLabel labelItem = null;
        // Validar nodo
        if (item.getNodeType() == Node.ELEMENT_NODE) {
            labelItem = new ManejadorDeLabel(item);
        }
        return labelItem;
    }
    
    // Devolver una lista con Labels que contengan subLabel <tagNameSL> con <oValue>
    public ArrayList<ManejadorDeLabel> getLabelsSLC (String tagNameSL, String oValue) {
        // Variables
        ArrayList<ManejadorDeLabel> aryLM = new ArrayList<ManejadorDeLabel>();
        int numItems = this.nodeList.getLength();
        // Recorrido de nodos
        for (int i = 0; i < numItems; i++) {
            // Recuperar NODE item
            Node item = this.nodeList.item(i);
            // Validar nodo
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                // Recuperar Label de la lista
                ManejadorDeLabel labelItem = new ManejadorDeLabel(item);
                // Recupera sub Label
                ManejadorDeLabel subLabelItem = labelItem.getLabelByTagName(tagNameSL);
                // Validar nodo recuperado
                if (subLabelItem != null) {
                    // Obtener valor 
                    String sValue = subLabelItem.getTextContent();
                    // Validar y arregar label al arreglo
                    if (oValue.equals(sValue)) {
                        aryLM.add(labelItem);
                    }
                }
            }
        }
        //
        int n = aryLM.size();
        for (int i = 0; i < n; i++) {
            ManejadorDeLabel labelAnnon = aryLM.get(i);
            labelAnnon.getChildLabels();
        }
        //
        return aryLM;
    } 
}
