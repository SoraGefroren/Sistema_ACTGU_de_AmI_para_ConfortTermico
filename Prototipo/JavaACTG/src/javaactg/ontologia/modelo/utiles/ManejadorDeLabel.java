/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.ontologia.modelo.utiles;

// Librerias
import java.util.ArrayList;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jorge
 */
public class ManejadorDeLabel {
    // Variables
    private Node node = null;
    
    // Constructor
    public ManejadorDeLabel(Node n) {
        // Asignar
        this.node = n;
    }
    
    // Recuperar una SubLabel por su TagName
    public ManejadorDeLabel getLabelByTagName(String tagName) {
        // Variables
        ManejadorDeLabel nLabel = null;
        // Validar nodo
        if (this.node.getNodeType() == Node.ELEMENT_NODE) {
            // Convertir nodo en elemento
            Element element = (Element) this.node;
            NodeList nodeList = element.getElementsByTagName(tagName);
            int numNL = nodeList.getLength();
            // Validar número de nodos
            if (0 < numNL) {
                // Obtener nodo
                Node nodeTemp = null;
                for (int i = 0; i < numNL; i++) {
                    nodeTemp = nodeList.item(i);
                    // Validar nodo
                    if (nodeTemp.getNodeType() == Node.ELEMENT_NODE) {
                        nLabel = new ManejadorDeLabel(nodeTemp);
                        break;
                    }
                }
            }
        }
        return nLabel;
    }
    
    // Recuperar una SubLabel por su TagName
    public ManejadorDeLabelList getLabelsByTagName(String tagName) {
        // Variables
        ManejadorDeLabelList nLL = null;
        // Validar nodo
        if (this.node.getNodeType() == Node.ELEMENT_NODE) {
            // Convertir nodo en elemento
            Element element = (Element) this.node;
            NodeList nodeList = element.getElementsByTagName(tagName);
            int numNL = nodeList.getLength();
            // Validar número de nodos
            if (0 < numNL) {
                // Obtener nodo
                nLL = new ManejadorDeLabelList(nodeList);
            }
        }
        return nLL;
    }
    
    // Recuperar valor del Atributo de la Label Actual
    public String getAttribute(String attrName) {
        // Variable
        String strTemp = "";
        // Validar nodo
        if (this.node.getNodeType() == Node.ELEMENT_NODE) {
            // Convertir nodo en elemento
            Element element = (Element) this.node;    
            strTemp = element.getAttribute(attrName);
        }
        return strTemp;
    }
    
    // Recuperar valor del Atributo de la Label Actual
    public String getAttributeWDP(String attrName) {
        // Variable
        String strTemp = "";
        // Validar nodo
        if (this.node.getNodeType() == Node.ELEMENT_NODE) {
            // Extraer atributos
            NamedNodeMap attrs = this.node.getAttributes();  
            int numA = attrs.getLength();
            for(int i = 0 ; i < numA; i++) {
                Attr attribute = (Attr) attrs.item(i);
                // Validar
                if (attribute.getName().equals(attrName)){
                    strTemp = attribute.getValue();
                }
            }
        }
        return strTemp;
    }
    
    // Recuperar texto contenido en la Label Actual
    public String getTextContent() {
        // Variable
        String strTC = "";
        // Validar nodo
        if (this.node.getNodeType() == Node.ELEMENT_NODE) {
            // Convertir nodo en elemento
            Element element = (Element) this.node;
            strTC = element.getTextContent();
        }
        return strTC;
    }
    
    // Devolver lista de SubLabels contenidas en la Label actual
    public ManejadorDeLabelList getChildLabels() {
        // Convertir nodo en elemento
        Element element = (Element) this.node;
        return new ManejadorDeLabelList(element.getChildNodes());
    }
    
    // Devolver lista de SubLabels contenidas en la Label actual
    public ManejadorDeLabelList getChildLabelsByN() {
        // Recuperar
        return new ManejadorDeLabelList(this.node.getChildNodes());
    }
    
    // Recuperar texto contenido en la Label Actual
    public String getNodeName() {
        // Variable
        String tagN = "";
        // Validar nodo
        if (this.node.getNodeType() == Node.ELEMENT_NODE) {
            tagN = this.node.getNodeName();
        }
        return tagN;
    }
    
    public ArrayList<ManejadorDeLabel> getChilds() {
        // Variables
        ArrayList<ManejadorDeLabel> aryC = new ArrayList<ManejadorDeLabel>();
        // Validar nodo
        if (this.node.getNodeType() == Node.ELEMENT_NODE) {
            // Convertir nodo en elemento
            Element element = (Element) this.node;
            NodeList nL = element.getElementsByTagName("*");
            int nn = nL.getLength();
            // Recorrer lista
            for (int i = 0; i < nn; i++) {
                Node nodeTemp = nL.item(i);
                if (nodeTemp.getNodeType() == Node.ELEMENT_NODE) {
                    aryC.add(new ManejadorDeLabel(nodeTemp));
                }
            }
        }
        // Retorno
        return aryC;
    }
}
