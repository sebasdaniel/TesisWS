/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.ws;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 *
 * @author 53B45
 */
public class XmlHl7 {
    
    private String salida = null;
    
    public XmlHl7(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementaction = builder.getDOMImplementation();
            
            Document document = implementaction.createDocument(null, "documento", null);
            document.setXmlVersion("1.0");
            
            Element raiz = document.getDocumentElement();
            
            Element nodoNombreCampo = document.createElement("ElementoHijoDeLaRaiz");
            Text nodoValorCampo = document.createTextNode("Contenido del elemento hijo");
            
            nodoNombreCampo.setAttribute("ejAtributo", "si funciona");
            nodoNombreCampo.appendChild(nodoValorCampo);
            
            raiz.appendChild(nodoNombreCampo);
            
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer;
            try {
                transformer = tf.newTransformer();
                // below code to remove XML declaration
                // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                StringWriter writer = new StringWriter();
                transformer.transform(new DOMSource(document), new StreamResult(writer));
                salida = writer.getBuffer().toString();
                
            } catch (TransformerException e) {
                System.err.println(e);
            }
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XmlHl7.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String getXmlInString(){
        return salida;
    }
}
