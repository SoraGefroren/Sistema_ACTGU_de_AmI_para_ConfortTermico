/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.ontologia.modelo;

// Librerias
import javaactg.ontologia.modelo.utiles.ManejadorDeLabel;
import javaactg.ontologia.modelo.utiles.ManejadorDeLabelList;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jorge
 */
public class ModeloACTGU {
    // Variables
    private String omDocURI = "";
    private Document docOM = null;
    private Element rootRDF = null;
    
    private Map<String, XSDDatatype> listTypesXSD = null;
    private Map<String, String> listEquivaSASML = null;
    
    // Prefijos
    private String v_xmlns = "xmlns:";
    private String v_base = "base:";
    private String v_ssn = "ssn:";
    private String v_dul = "DUL:";
    private String v_dul2 = "DUL2:";
    private String v_owl = "owl:";
    private String v_rdf = "rdf:";
    private String v_rdfs = "rdfs:";
    private String v_xml = "xml:";
    private String v_xsd = "xsd:";
    private String v_sosa = "sosa:";
    private String v_actgu = "actgu:";
    private String v_foaf = "foaf:";
    
    // URI - Prefijos
    private String u_base = "http://purl.oclc.org/NET/ssnx/ssn";
    private String u_ssn = "http://purl.oclc.org/NET/ssnx/ssn#";
    private String u_dul = "http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#";
    private String u_dul2 = "http://www.loa-cnr.it/ontologies/DUL.owl#";
    private String u_owl = "http://www.w3.org/2002/07/owl#";
    private String u_rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private String u_rdfs = "http://www.w3.org/2000/01/rdf-schema#";
    private String u_xml = "http://www.w3.org/XML/1998/namespace";
    private String u_xsd = "http://www.w3.org/2001/XMLSchema#";
    private String u_sosa = "http://www.w3.org/ns/sosa/";
    private String u_actgu = "http://www.msicu.onto/actgu#";
    private String u_foaf = "http://xmlns.com/foaf/0.1/";
    
    // Función para la URI del documento
    public String getURIofDoc () {
        return this.omDocURI + "#";
    }
    
    // Función para devolver prefijos
    public String getPrefix (String p) {
        // Variable
        String r = "";
        // Selección
        switch (p) {
            case "xmls":
                r = this.v_xmlns;
                break;
            case "base":
                r = this.v_base;
                break;
            case "ssn":
                r = this.v_ssn;
                break;
            case "dul":
                r = this.v_dul;
                break;
            case "dul2":
                r = this.v_dul2;
                break;
            case "owl":
                r = this.v_owl;
                break;
            case "rdf":
                r = this.v_rdf;
                break;
            case "rdfs":
                r = this.v_rdfs;
                break;
            case "xml":
                r = this.v_xml;
                break;
            case "xsd":
                r = this.v_xsd;
                break;
            case "sosa":
                r = this.v_sosa;
                break;
            case "actgu":
                r = this.v_actgu;
                break;
            case "foaf":
                r = this.v_foaf;
                break;
        }
        // Retorno
        return r;
    }
    
    // Función para devolver el valor URI de prefijos
    public String getURIofPrefix (String u) {
        // Variable
        String r = "";
        // Selección
        switch (u) {
            case "base":
                r = this.u_base;
                break;
            case "ssn":
                r = this.u_ssn;
                break;
            case "dul":
                r = this.u_dul;
                break;
            case "dul2":
                r = this.u_dul2;
                break;
            case "owl":
                r = this.u_owl;
                break;
            case "rdf":
                r = this.u_rdf;
                break;
            case "rdfs":
                r = this.u_rdfs;
                break;
            case "xml":
                r = this.u_xml;
                break;
            case "xsd":
                r = this.u_xsd;
                break;
            case "sosa":
                r = this.u_sosa;
                break;
            case "actgu":
                r = this.u_actgu;
                break;
            case "foaf":
                r = this.u_foaf;
                break;
        }
        // Retorno
        return r;
    }
    
    // Función para devolver el prefijo de una URI
    public String getPrefixofURI (String u) {
        // Variable
        String r = "";
        // Selección
        if (u.equals(this.u_base)) {
            r = this.v_base;
        } else if (u.equals(this.u_ssn)) {
            r = this.v_ssn;
        } else if (u.equals(this.u_dul)) {
            r = this.v_dul;
        } else if (u.equals(this.u_dul2)) {
            r = this.v_dul2;
        } else if (u.equals(this.u_owl)) {
            r = this.v_owl;
        } else if (u.equals(this.u_rdf)) {
            r = this.v_rdf;
        } else if (u.equals(this.u_rdfs)) {
            r = this.v_rdfs;
        } else if (u.equals(this.u_xml)) {
            r = this.v_xml;
        } else if (u.equals(this.u_xsd)) {
            r = this.v_xsd;
        } else if (u.equals(this.u_sosa)) {
            r = this.v_sosa;
        } else if (u.equals(this.u_actgu)) {
            r = this.v_actgu;
        } else if (u.equals(this.u_foaf)) {
            r = this.v_foaf;
        }
        // Retorno
        return r;
    }
            
    // Constructor
    public ModeloACTGU () {
        // Asignar URI
        this.omDocURI = "http://www.msicu.onto/";
        // TRY - CATCH
        try {
            // Crear documento
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            this.docOM = documentBuilder.newDocument();
            
            // Crear elemento|nodo principal
            this.rootRDF = this.docOM.createElement(this.v_rdf + "RDF");
            this.docOM.appendChild(this.rootRDF);

            // Crear atributos iniciales
            this.setAttributeInRoot(this.v_xmlns.substring(0, (this.v_xmlns.length() - 1)), this.u_ssn);
            this.setAttributeInRoot(this.v_xml + "base", this.u_base);
            this.setAttributeInRoot(this.v_xmlns + "ssn", this.u_ssn);
            this.setAttributeInRoot(this.v_xmlns + "DUL", this.u_dul);
            this.setAttributeInRoot(this.v_xmlns + "DUL2", this.u_dul2);
            this.setAttributeInRoot(this.v_xmlns + "owl", this.u_owl);
            this.setAttributeInRoot(this.v_xmlns + "rdf", this.u_rdf);
            this.setAttributeInRoot(this.v_xmlns + "rdfs", this.u_rdfs);
            this.setAttributeInRoot(this.v_xmlns + "xml", this.u_xml);
            this.setAttributeInRoot(this.v_xmlns + "xsd", this.u_xsd);
            this.setAttributeInRoot(this.v_xmlns + "sosa", this.u_sosa);
            this.setAttributeInRoot(this.v_xmlns + "actgu", this.u_actgu);
            this.setAttributeInRoot(this.v_xmlns + "foaf", this.u_foaf);
            
            // Generar como ontologia
            // Asignar elemento informativo
            this.setElementInRoot(
                    v_owl + "Ontology",
                    new Attr[] {this.makeAttribute(v_rdf + "about", this.omDocURI)},
                    new Element[] {}
            );
            // Asignar: Object Properties
            this.setSSNProperties();
            this.setFOAFProperties();
            this.setACTGUProperties();
            
            // Asignar: Clases
            this.setSSNClasses();
            this.setFOAFClasses();
            this.setACTGUClasses();
            
            // Generar lista de relaciones SASML - SSN
            this.initializeEquivalsSASML();
            this.initializeTypesXSD();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Establecer Propiedades de Objeto para la Ontologia
    private void setSSNProperties() {
        // Asignar elementos
        // <!-- http://purl.oclc.org/NET/ssnx/ssn#hasDeployment -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_ssn + "hasDeployment")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_ssn + "Sensor")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_ssn + "Deployment")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://purl.oclc.org/NET/ssnx/ssn#isProducedBy --> 
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_ssn + "isProducedBy")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_ssn + "SensorOutput")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_ssn + "Sensor")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://purl.oclc.org/NET/ssnx/ssn#hasValue -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_ssn + "hasValue")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_ssn + "SensorOutput")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_ssn + "ObservationValue")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://purl.oclc.org/NET/ssnx/ssn#observes -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_ssn + "observes")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_ssn + "Sensor")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_ssn + "Property")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#isClassifiedBy -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_dul + "isClassifiedBy")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_ssn + "ObservationValue")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_dul2 + "UnitOfMeasure")},
                    new Element[] {}
                )
            }
        );
    }
    private void setFOAFProperties() {
        // Asignar elementos
        // <!-- http://xmlns.com/foaf/spec/#member -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_foaf + "member")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_foaf + "Group")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_foaf + "Person")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://xmlns.com/foaf/spec/#name -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_foaf + "name")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_foaf + "Person")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Noun")},
                    new Element[] {}
                )
            }
        );
    }
    private void setACTGUProperties(){
        // Asignar elementos
        // <!-- http://www.msicu.onto/actgu#numerateType -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "numerateType")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "NumType")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Type")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#numerateGroup -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "numerateGroup")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "NumGroup")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_foaf + "Group")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#numeratePriority -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "numeratePriority")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "NumPriority")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Priority")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#suscribe -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "suscribe")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "NumClothes")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Label")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#numerateClothes -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "numerateClothes")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "NumClothes")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Clothes")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#equippeWith -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "equippeWith")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Clothes")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "CloValue")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#configured -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "configured")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "ConfigurationValue")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Actuator")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#isOccupiedBy -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "isOccupiedBy")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_ssn + "Deployment")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Actuator")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_foaf + "Group")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#has --> //*************************************************
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "has")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_foaf + "Person")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "ThermalComfort")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#isSortOf -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "isSortOf")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_foaf + "Person")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "NumPriority")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#characterizes -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "characterizes")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Demographic")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Anthropometry")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_foaf + "Person")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#present -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "present")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Actuator")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "State")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#kind -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "kind")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Actuator")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Type")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#is -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "is")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Demographic")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Gender")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Age")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#dressed -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "dressed")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_foaf + "Person")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "NumClothes")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#equippeWith -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "equippeWith")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Clothes")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "CloValue")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#isEquippedBy -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "isEquippedBy")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "InsulationValue")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_foaf + "Person")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#isPhysicalWearOf -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "isPhysicalWearOf")},
            new Element[] {
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "MetabolicRate")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_foaf + "Person")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#usedTo -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "usedTo")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_foaf + "Person")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "ComfortTrend")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#take -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "take")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Vector")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Tendency")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Condition")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#form -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "form")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "ComfortTrend")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "SlopeOfComfort")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#utilize -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "utilize")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "ComfortTrend")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Vector")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#isDescribedBy -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "isDescribedBy")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "ConfigurationValue")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Condition")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "CloValue")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "InsulationValue")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "MetabolicRate")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "AnthropometricValue")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_ssn + "Property")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#isClassifiedBy -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "isClassifiedBy")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "ConfigurationValue")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Condition")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "CloValue")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "InsulationValue")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "MetabolicRate")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "AnthropometricValue")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Age")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_dul2 + "UnitOfMeasure")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#hasValue -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "hasValue")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Anthropometric")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "AnthropometricValue")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#produce -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "produce")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Actuator")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_ssn + "ObservationValue")},
                    new Element[] {}
                )
            }
        );
        // <!-- http://www.msicu.onto/actgu#influencedIn -->
        this.setElementInRoot(
            v_owl + "ObjectProperty",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "influencedIn")},
            new Element[] {
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_ssn + "ObservationValue")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "InsulationValue")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "MetabolicRate")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Anthropometry")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "domain",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "Demographic")},
                    new Element[] {}
                ),
                this.makeElement(v_rdfs + "range",
                    new Attr[] {this.makeAttribute(v_rdf + "resource", this.u_actgu + "ThermalComfort")},
                    new Element[] {}
                )
            }
        );
    }
            
    // Establecer Clases para la Ontologia
    private void setSSNClasses() {
        // Asignar elementos
        // <!-- http://purl.oclc.org/NET/ssnx/ssn#Deployment -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_ssn + "Deployment")},
            new Element[] {}
        );
        // <!-- http://purl.oclc.org/NET/ssnx/ssn#Sensor -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_ssn + "Sensor")},
            new Element[] {}
        );
        // <!-- http://purl.oclc.org/NET/ssnx/ssn#SensorOutput -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_ssn + "SensorOutput")},
            new Element[] {}
        );
        // <!-- http://purl.oclc.org/NET/ssnx/ssn#ObservationValue -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_ssn + "ObservationValue")},
            new Element[] {}
        );
        // <!-- http://purl.oclc.org/NET/ssnx/ssn#Property -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_ssn + "Property")},
            new Element[] {}
        );
        // <!-- http://www.loa-cnr.it/ontologies/DUL.owl#UnitOfMeasure -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_dul2 + "UnitOfMeasure")},
            new Element[] {}
        );     
    }
    private void setFOAFClasses() {
        // Asignar elementos
        // <!-- http://xmlns.com/foaf/spec/#term_Person -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_foaf + "Person")},
            new Element[] {}
        );
        // <!-- http://xmlns.com/foaf/spec/#term_Group -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_foaf + "Group")},
            new Element[] {}
        );
    }
    private void setACTGUClasses(){
        // Asignar elementos Adicionales
        // <!-- http://www.msicu.onto/actgu#Noun -->
        this.setElementInRoot(
            v_rdfs + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "Label")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#Noun -->
        this.setElementInRoot(
            v_rdfs + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "Noun")},
            new Element[] {}
        );
        // <!-- http://www.w3.org/1999/02/22-rdf-syntax-ns#Description -->
        this.setElementInRoot(
            v_rdfs + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_rdf + "Description")},
            new Element[] {}
        );
        // Asignar elementos ACTGU
        // <!-- http://www.msicu.onto/actgu#NumPriority -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "NumPriority")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#Priority -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "Priority")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#NumGroup -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "NumGroup")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#NumClothes -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "NumClothes")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#Clothes -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "Clothes")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#CloValue -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "CloValue")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#ComfortTrend -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "ComfortTrend")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#SlopeOfComfort -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "SlopeOfComfort")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#Vector -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "Vector")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#Tendency -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "Tendency")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#Condition -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "Condition")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#ConfigurationValue -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "ConfigurationValue")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#ThermalComfort -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "ThermalComfort")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#Demographic -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "Demographic")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#Gender -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "Gender")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#Age -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "Age")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#Anthropometry -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "Anthropometry")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#AnthropometricValue -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "AnthropometricValue")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#MetabolicRate -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "MetabolicRate")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#InsulationValue -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "InsulationValue")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#Actuator -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "Actuator")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#State -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "State")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#NumType -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "NumType")},
            new Element[] {}
        );
        // <!-- http://www.msicu.onto/actgu#Type -->
        this.setElementInRoot(
            v_owl + "Class",
            new Attr[] {this.makeAttribute(v_rdf + "about", this.u_actgu + "Type")},
            new Element[] {}
        );
    }
    
    // Función para iniciar la lista de relaciones SASML - SSN
    private void initializeEquivalsSASML() {
        // Inicializar lista de relaciones
        this.listEquivaSASML = new HashMap<String, String>();
        // ** Adicionales
        this.listEquivaSASML.put("Description", this.u_rdf + "Description");
        // ** Equivalencias - SSN
        this.listEquivaSASML.put("Deployment", this.u_ssn + "Deployment");
        this.listEquivaSASML.put("Sensor", this.u_ssn + "Sensor");
        this.listEquivaSASML.put("SensorOutput", this.u_ssn + "SensorOutput");
        this.listEquivaSASML.put("ObservationValue", this.u_ssn + "ObservationValue");
        this.listEquivaSASML.put("UnitOfMeasure", this.u_dul2 + "UnitOfMeasure");
        this.listEquivaSASML.put("Property", this.u_ssn + "Property");
        // ** Equivalencias - FOAF
        this.listEquivaSASML.put("Person", this.u_foaf + "Person");
        this.listEquivaSASML.put("Group", this.u_foaf + "Group");
        // ** Equivalencias - ACTGU
        this.listEquivaSASML.put("NumPriority", this.u_actgu + "NumPriority");
        this.listEquivaSASML.put("Priority", this.u_actgu + "Priority");
        this.listEquivaSASML.put("NumGroup", this.u_actgu + "NumGroup");
        this.listEquivaSASML.put("Demographic", this.u_actgu + "Demographic");
        this.listEquivaSASML.put("Gender", this.u_actgu + "Gender");
        this.listEquivaSASML.put("Age", this.u_actgu + "Age");
        this.listEquivaSASML.put("Anthropometry", this.u_actgu + "Anthropometry");
        this.listEquivaSASML.put("AnthropometricValue", this.u_actgu + "AnthropometricValue");
        this.listEquivaSASML.put("MetabolicRate", this.u_actgu + "MetabolicRate");
        this.listEquivaSASML.put("InsulationValue", this.u_actgu + "InsulationValue");
        this.listEquivaSASML.put("NumClothes", this.u_actgu + "NumClothes");
        this.listEquivaSASML.put("Clothes", this.u_actgu + "Clothes");
        this.listEquivaSASML.put("CloValue", this.u_actgu + "CloValue");
        this.listEquivaSASML.put("Actuator", this.u_actgu + "Actuator");
        this.listEquivaSASML.put("State", this.u_actgu + "State");
        this.listEquivaSASML.put("NumType", this.u_actgu + "NumType");
        this.listEquivaSASML.put("Type", this.u_actgu + "Type");
        this.listEquivaSASML.put("ThermalComfort", this.u_actgu + "ThermalComfort");
        this.listEquivaSASML.put("ConfigurationValue", this.u_actgu + "ConfigurationValue");
        this.listEquivaSASML.put("ComfortTrend", this.u_actgu + "ComfortTrend");
        this.listEquivaSASML.put("SlopeOfComfort", this.u_actgu + "SlopeOfComfort");
        this.listEquivaSASML.put("Vector", this.u_actgu + "Vector");
        this.listEquivaSASML.put("Tendency", this.u_actgu + "Tendency");
        this.listEquivaSASML.put("Condition", this.u_actgu + "Condition");
        this.listEquivaSASML.put("Noun", this.u_actgu + "Noun");
        this.listEquivaSASML.put("Label", this.u_actgu + "Label");
    }
    
    // Función para iniciar la lista de relaciones SASML - SSN
    private void initializeTypesXSD() {
        // Inicializar lista de relaciones
        this.listTypesXSD = new HashMap<String, XSDDatatype>();
        // Tipos de datos ACTGU
        this.listTypesXSD.put("Person", XSDDatatype.XSDnonNegativeInteger);
        this.listTypesXSD.put("NumPriority", XSDDatatype.XSDnonNegativeInteger);
        this.listTypesXSD.put("Priority", XSDDatatype.XSDstring);
        this.listTypesXSD.put("NumGroup", XSDDatatype.XSDnonNegativeInteger);
        this.listTypesXSD.put("Group", XSDDatatype.XSDstring);
        this.listTypesXSD.put("Demographic", XSDDatatype.XSDstring);
        this.listTypesXSD.put("Gender", XSDDatatype.XSDstring);
        this.listTypesXSD.put("Age", XSDDatatype.XSDnonNegativeInteger);
        this.listTypesXSD.put("Anthropometry", XSDDatatype.XSDstring);
        this.listTypesXSD.put("AnthropometricValue", XSDDatatype.XSDdouble);
        this.listTypesXSD.put("MetabolicRate", XSDDatatype.XSDdouble);
        this.listTypesXSD.put("InsulationValue", XSDDatatype.XSDdouble);
        this.listTypesXSD.put("NumClothes", XSDDatatype.XSDnonNegativeInteger);
        this.listTypesXSD.put("Clothes", XSDDatatype.XSDstring);
        this.listTypesXSD.put("CloValue", XSDDatatype.XSDdouble);
        this.listTypesXSD.put("Actuator", XSDDatatype.XSDnonNegativeInteger);
        this.listTypesXSD.put("State", XSDDatatype.XSDstring);
        this.listTypesXSD.put("NumType", XSDDatatype.XSDnonNegativeInteger);
        this.listTypesXSD.put("Type", XSDDatatype.XSDstring);
        this.listTypesXSD.put("ThermalComfort", XSDDatatype.XSDstring);
        this.listTypesXSD.put("ConfigurationValue", XSDDatatype.XSDdouble);
        this.listTypesXSD.put("Deployment", XSDDatatype.XSDstring); 
        this.listTypesXSD.put("Sensor", XSDDatatype.XSDnonNegativeInteger);
        this.listTypesXSD.put("SensorOutput", XSDDatatype.XSDstring);
        this.listTypesXSD.put("ObservationValue", XSDDatatype.XSDdouble);
        this.listTypesXSD.put("Property", XSDDatatype.XSDstring);
        this.listTypesXSD.put("UnitOfMeasure", XSDDatatype.XSDstring);
        this.listTypesXSD.put("ComfortTrend", XSDDatatype.XSDstring);
        this.listTypesXSD.put("SlopeOfComfort", XSDDatatype.XSDdouble);
        this.listTypesXSD.put("Vector", XSDDatatype.XSDstring);
        this.listTypesXSD.put("Tendency", XSDDatatype.XSDdouble);
        this.listTypesXSD.put("Condition", XSDDatatype.XSDdouble);
        this.listTypesXSD.put("Noun", XSDDatatype.XSDstring);
        this.listTypesXSD.put("Label", XSDDatatype.XSDstring);
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // Funcion para asignar atributos en Root
    public void setAttributeInRoot(String property, String value) {
        // Crear atributps Prefijo
        Attr attr = this.makeAttribute(property, value);
        // Asignar atributo en Nodo principal (ROOT)
        this.rootRDF.setAttributeNode(attr);
    }
    
    // Funcion para crear atributo
    public Attr makeAttribute(String property, String value) {
        // Crear atributps Prefijo
        Attr attr = this.docOM.createAttribute(property);
        attr.setValue(value);
        return attr;
    }
    
    // Funcion para asignar elementos en Root
    public void setElementInRoot(Element e) {
        // Asignar elementos principal a Nodo principal (ROOT)
        this.rootRDF.appendChild(e);
    }
    
    // Funcion para asignar elementos en Root
    public void setElementInRoot(String typeTag, Attr[] listAttributes, Element[] listSubElements) {
        // Crear elemento "Nodo"
        Element element = this.makeElement(typeTag, listAttributes, listSubElements);
        // Asignar elementos principal a Nodo principal (ROOT)
        this.rootRDF.appendChild(element);
    }
    
    // Funcion para crear elemento
    public Element makeElement(String typeTag, Attr[] listAttributes, Element[] listSubElements) {
        // Crear elemento "Nodo"
        Element element = this.docOM.createElement(typeTag);
        // Asignar atributos a elementos principal
        for (Attr attr: listAttributes) {
            element.setAttributeNode(attr);
        }
        // Asignar subelementos a elementos principal
        for (Element subelement: listSubElements) {
            element.appendChild(subelement);
        }
        // Devolver elemento creado
        return element;
    }
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    // Función que devuelve la URI de una clase
    public String getUriClass(String clase) {
        // Devolver clase
        return listEquivaSASML.get(clase);
    }
    
    // Función que devuelve el objeto XSD de una clase
    public XSDDatatype getXSDType(String clase) {
        // Devolver clase
        return listTypesXSD.get(clase);
    }
    
    // Función para generar documento OWL
    public void makeOWLFile (String origen) {
        //*/
        try{
            // Convertir objeto DOM a documento XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            DOMSource domSource = new DOMSource(this.docOM);
            StreamResult streamResult = new StreamResult(new File("_ArchivosAdicionales/ontoModel" + origen + ".owl"));
            
            // Finalizar proceso
            transformer.transform(domSource, streamResult); // Para volver Documento
        } catch(Exception e){
            // Mostrar error
            // e.printStackTrace();
        }   
    }
    
    // Función para generar documento OWL como String
    public String makeOWLString () {
        //*/
        String cadena = "";
        try{
            // Crear archivo XML
            StringWriter swResult = new StringWriter();
            
            // Convertir objeto DOM a documento XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            DOMSource domSource = new DOMSource(this.docOM);
            StreamResult streamResult = new StreamResult(swResult);
            
            // Finalizar proceso
            transformer.transform(domSource, streamResult); // Para volver Documento
            cadena = swResult.toString();
        } catch(Exception e){
            // Mostrar error
            // e.printStackTrace();
        }
        return cadena;
    }
}
