/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.ontologia.modelo;

// JENA LIBs
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ConcurrentModificationException;
import java.util.List;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.impl.OntModelImpl;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFWriter;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.iterator.ExtendedIterator;

/**
 *
 * @author Jorge
 */
public class InstanciaJENA {
    // Variables
    private Model model = null;
    private OntModel onmo = null;
    // URI clave de la instancia
    private String omDocURI = "";
    
    // Constructor
    public InstanciaJENA (String strO, String nomspc) {
        // Ajuste para la liberia JENA
        org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        // Inicializar Modelo de Ontológia atravez de un String
        onmo = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF);
        onmo.read(new StringReader(strO), null, "RDF/XML");
        // Abstraer la base del modelo
        model = onmo.getBaseModel();
        // Asignar variables
        omDocURI = nomspc;
    }
    
    // Constructor
    public InstanciaJENA (OntModel nwOM, String nomspc) {
        // Asignar Modelo de Ontológia
        onmo = nwOM;
        // Asignar variables
        omDocURI = nomspc;
        // Abstraer la base del modelo
        model = onmo.getBaseModel();
    }
    
    // Función para realizar consultas SPARQL
    public JsonElement doSparqlQuery(String querySparql) {
        // Variables
        QueryExecution queryExec = null;
        JsonElement jsonElement = null;
        ResultSet resulado = null;
        // Caso de excepción
        try {
            // Inciar consulta
            Query query = QueryFactory.create(querySparql);
            queryExec = QueryExecutionFactory.create(query, this.onmo);
            // Caso de excepción
            try {
                // Ejecutar consulta
                resulado = queryExec.execSelect();
                // Convertir en JSON
                ByteArrayOutputStream osJson = new ByteArrayOutputStream();
                ResultSetFormatter.outputAsJSON(osJson, resulado);
                String strJsonQry = osJson.toString("UTF-8");
                // Generar JSON
                jsonElement = new JsonParser().parse(strJsonQry);
            } catch (ConcurrentModificationException cme) {
                System.err.println("Error de concurrencia, se intento consultar a un elementos en modificación");
            } catch (Exception err) {
                err.printStackTrace();
            } finally {
                queryExec.close();
                queryExec = null;
            }
        } catch (Exception ero) {
            ero.printStackTrace();
        } finally {
            if (queryExec != null) {
                queryExec.close();
            }
        }
        // Retorno
        return jsonElement;
    }
    
    // Función que agrega un elemento clase a la ontología
    public void addOntoClass(String strUriCls) {
        // Caso de excepción
        try {
            // Crear elemento nueva clase en la ontología
            OntClass nwOntClas = onmo.createClass(strUriCls);
            // Actualizar el modelo base
            model = onmo.getBaseModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Función que agrega un elemento individual no relacionado a ontologia
    public void addIndividual(String strUriCls, XSDDatatype xsdDataTyp, String valOfIndv) {;
        // Caso de excepción
        try {
            // Recuperar clase contenida en ontologia
            Resource resorClass = onmo.getResource(strUriCls);
            // Crear elemento individual
            Individual nwIndvl = onmo.createIndividual(strUriCls + "_" + valOfIndv, resorClass);
            // Crear partes para elemento individual
            Property properTopData = onmo.createProperty("http://www.w3.org/2002/07/owl#topDataProperty");
            Literal literalConte = onmo.createTypedLiteral(valOfIndv, xsdDataTyp);
            // Asignar contenido a elementos individual
            nwIndvl.addProperty(properTopData, literalConte);
            // Actualizar el modelo base
            model = onmo.getBaseModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Función que agrega un elemento individual con relaciones a ontologia
    public void addIndividualRelated(String strUriCls, XSDDatatype xsdDataTyp, String valOfIndv, String[] aryReltns, String[] aryUriCls, String[] aryValRels) {;
        // Caso de excepción
        try {
            // Recuperar clase contenida en ontologia
            Resource resorClass = onmo.getResource(strUriCls);
            // Crear elemento individual
            Individual nwIndvl = onmo.createIndividual(strUriCls + "_" + valOfIndv, resorClass);
            // Crear partes para elemento individual
            Literal literalConte = onmo.createTypedLiteral(valOfIndv, xsdDataTyp);
            Property properTopData = onmo.createProperty("http://www.w3.org/2002/07/owl#topDataProperty");
            // Asignar contenido a elementos individual
            nwIndvl.addProperty(properTopData, literalConte);
            // Cálcular número de elementos relacionados
            int numElsRls = aryValRels.length;
            // Recorrer elementos relacionados
            for (int i = 0; i < numElsRls; i++) {
                // Recuperar elemento contenido en ontologia
                Resource subResElmnt = onmo.getResource(aryUriCls[i] + "_" + aryValRels[i]);
                // Crear partes para elemento individual
                Property subPropData = onmo.createProperty(aryReltns[i]);
                // Asignar contenido a elementos individual
                nwIndvl.addProperty(subPropData, subResElmnt);
            }
            // Actualizar el modelo base
            model = onmo.getBaseModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Función que agrega relaciones a un elemento individual de la ontologia
    public void addRelsInIndividual(String strUriCls, String valOfIndv, String[] aryReltns, String[] aryUriCls, String[] aryValRels) {;
        // Caso de excepción
        try {
            // Recuperar elemento individual
            Individual nwIndvl = onmo.getIndividual(strUriCls + "_" + valOfIndv);
            // Validar existencia de individual
            if (nwIndvl != null) {
                // Cálcular número de elementos relacionados
                int numElsRls = aryValRels.length;
                // Recorrer elementos relacionados
                for (int i = 0; i < numElsRls; i++) {
                    // Recuperar elemento contenido en ontologia
                    Resource subResElmnt = onmo.getResource(aryUriCls[i] + "_" + aryValRels[i]);
                    // Crear partes para elemento individual
                    Property subPropData = onmo.createProperty(aryReltns[i]);
                    // Validar si asignar propiedad
                    if (!nwIndvl.hasProperty(subPropData, subResElmnt)) {
                        // Asignar contenido a elementos individual
                        nwIndvl.addProperty(subPropData, subResElmnt);
                    }
                }
                // Actualizar el modelo base
                model = onmo.getBaseModel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Remover todos los elementos individuales de una clase en particular
    public void removeIndividuals(String strUriCls) {
        // Caso de excepción
        try {
            // Recuperar clase contenida en ontologia
            Resource resorClass = onmo.getResource(strUriCls);
            // Validar existencia de recurso a borrar
            if (resorClass != null) {
                // Crear elementos individual en Ontologia
                onmo.removeAll(resorClass, null, null);
                onmo.removeAll(null, null, resorClass);
                // Actualizar el modelo base
                model = onmo.getBaseModel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Remover todas las relaciones en un elemento individual en particular, si la relación es una en especifico
    public void removeRelsInIndividuals(String strUriCls, String[] aryReltns, String[] aryUriCls) {
        // Caso de excepción
        try {
            // Cálcular número elementos relacionados
            int numElsRls = aryReltns.length;
            // Recuperar clase contenida en ontologia
            OntClass ontoClass = onmo.getOntClass(strUriCls);
            // Obtener instancias de la clase antes recuperada
            ExtendedIterator instances = ontoClass.listInstances();
            List<Individual> lisInsta = instances.toList();
            // Recorrer instancias individuales obtenidas
            for (Individual myInsta: lisInsta) {
                // Obtener propiedades de la instancia individual
                StmtIterator staPropers = myInsta.listProperties();
                // Volver al STAMT una lista
                List<Statement> lisPropers = null;
                try {
                    // Generar lista de propiedades
                    lisPropers = staPropers.toList();
                } catch (Exception e) {}
                // Valida si el STAMT es adecuado para volverse lista
                if (lisPropers != null) {
                    // Recorrer propiedades obtenidas
                    for (Statement stamt: lisPropers) {
                        // Recorrer RELACIONES
                        for (int i = 0; i < numElsRls; i++) {
                            // Comparar la propiedad obtenida con una RELACIÓN
                            if (aryReltns[i].equals(stamt.getPredicate().getURI()) ||
                                aryReltns[i].equals(stamt.getPredicate().toString())) {
                                // Recuperar elemento individual al cual apunta
                                Individual indToPointer  = onmo.getIndividual(stamt.getObject().toString());
                                String strUrrClsPointed = "";
                                // Validar si elemento cuanta como individual
                                if (indToPointer.isIndividual()) {
                                    // Recupera URI de la Clase
                                    strUrrClsPointed = indToPointer.getOntClass().getURI();
                                } else {
                                    // Recuperar elemento individual al cual apunta
                                    Resource resToPointer  = onmo.getResource(aryUriCls[i]);
                                    // Validar existencia de recurso
                                    if (resToPointer != null) {
                                        // Recupera URI de la Clase
                                        strUrrClsPointed = resToPointer.getURI();
                                    }
                                }
                                // Validar que se apuntes a la clase objetivo
                                if (aryUriCls[i].equals(strUrrClsPointed)) {
                                    // Remover predicados
                                    myInsta.removeAll(stamt.getPredicate());
                                }
                            }
                        }
                    }
                }
            }
            // Actualizar el modelo base
            model = onmo.getBaseModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
                
    // Convertir modelos a String RDF/XML
    public String getStrModelRDF () {
        // Variables
        StringWriter out = new StringWriter();
        // Imprimir
        onmo.write(out, "RDF/XML");
        // Imprimir
        onmo.write(System.out, "RDF/XML");
        // Retorno
        return out.toString();
    }
    
    // Función para generar documento OWL
    public void generateOwlFile (String nomFile) {
        //*/
        try{
            // Generar archivo donde salvar la Ontología
            FileWriter fileWriter = new FileWriter("_ArchivosAdicionales/" + nomFile + ".owl");
            //*/
            try{
                // Escribir en el archivo generado para la Ontología
                model.write(fileWriter, "RDF/XML");
            } finally {
                // Cerrar archivo
                fileWriter.close();
            }
        } catch(Exception ero){
            // Mostrar error
            ero.printStackTrace();
        }   
    }
    
    // Función para unir este modelo con otro
    public void unionModelWith (Model otherM) {
        // Unir modelos
        onmo.union(otherM);
        // Actualizar el modelo base
        model = onmo.getBaseModel();
    }
    
    // Función para recuperar modelo
    public Model getModel() {
        // Regresar modelo
        return model;
    }
    
    // Función para recuperar modelo
    public OntModel getOntoModel() {
        // Regresar modelo
        return onmo;
    }
}
