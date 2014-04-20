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
            
            Document document = implementaction.createDocument(null, "ClinicalDocument", null);
            document.setXmlVersion("1.0");
            
            Element raiz = document.getDocumentElement();
            
            raiz.setAttribute("xmlns", "urn:hl7-org:v3");
            raiz.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            raiz.setAttribute("xsi:schemaLocation", "urn:hl7-org:v3 file:/D:/Documentos/HL7_V3_NormativeEdition2013_2CDset/Edition2013_CD1/Edition2013/infrastructure/cda/CDA.xsd");
            raiz.setAttribute("classCode", "DOCCLIN");
            raiz.setAttribute("moodCode", "EVN");
            
            Element nodoRealmCode = document.createElement("realmCode");
            nodoRealmCode.setAttribute("code", "CO");
            
            raiz.appendChild(nodoRealmCode);
            
            Element nodoTypeId = document.createElement("typeId");
            nodoTypeId.setAttribute("root", "2.16.840.1.113883.1.3");
            nodoTypeId.setAttribute("extension", "POCD_HD000040");
            
            raiz.appendChild(nodoTypeId);
            
            Element nodoId = document.createElement("id");
            nodoId.setAttribute("root", "2.16.840.1.113883.20.57.2.3.4.234170024801.1.5.2.10");
            nodoId.setAttribute("extension", "7f1e5d73-9331-422e-a92a-59a0f5958398");
            
            raiz.appendChild(nodoId);
            
            Element nodoCode = document.createElement("code");
            nodoCode.setAttribute("code", "34765-8");
            nodoCode.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
            nodoCode.setAttribute("codeSystemName", "LOINC");
            nodoCode.setAttribute("displayName", "EVALUATION AND MANAGEMENT NOTE");
            
            raiz.appendChild(nodoCode);
            
            Element nodoTitle = document.createElement("title");
            Text nodoValorTitle = document.createTextNode("HOJA DE EVOLUCION");
            nodoTitle.appendChild(nodoValorTitle);
            
            raiz.appendChild(nodoTitle);
            
            Element nodoEffectiveTime = document.createElement("effectiveTime");
            nodoEffectiveTime.setAttribute("value", "20140311195200");
            
            raiz.appendChild(nodoEffectiveTime);
            
            Element nodoConfidentialityCode = document.createElement("confidentialityCode");
            nodoConfidentialityCode.setAttribute("code", "R");
            nodoConfidentialityCode.setAttribute("codeSystem", "2.16.840.1.113883.5.25");
            nodoConfidentialityCode.setAttribute("codeSystemName", "confidentiality");
            nodoConfidentialityCode.setAttribute("displayName", "Restingido");
            
            raiz.appendChild(nodoConfidentialityCode);
            
            Element nodoRecordTarget = document.createElement("recordTarget");
            nodoRecordTarget.setAttribute("typeCode", "RCT");
            nodoRecordTarget.setAttribute("contextControlCode", "OP");
            
            Element nodoPatientRole = document.createElement("patientRole");
            nodoPatientRole.setAttribute("classCode", "PAT");
            
            nodoId = document.createElement("id");
            nodoId.setAttribute("id", "2.16.840.1.113883.19.57.2.3.4.234170024801.1.1");
            nodoId.setAttribute("extension", "107230099");
            
            nodoPatientRole.appendChild(nodoId);
            
            Element nodoAddr = document.createElement("addr");
            
            Element nodoCity = document.createElement("city");
            
            Element nodoState = document.createElement("state");
            
            nodoAddr.appendChild(nodoCity);
            nodoAddr.appendChild(nodoState);
            
            nodoPatientRole.appendChild(nodoAddr);
            
            Element nodoPatient = document.createElement("patient");
            nodoPatient.setAttribute("classCode", "PSN");
            nodoPatient.setAttribute("determinerCode", "INSTANCE");
            
            nodoId = document.createElement("id");
            nodoId.setAttribute("root", "2.16.840.1.113883.19.57.1.1.1.1.1");
            nodoId.setAttribute("extension", "1067564321");
            nodoId.setAttribute("assigningAuthorityName", "RNEC");
            
            nodoPatient.appendChild(nodoId);
            
            Element nodoName = document.createElement("name");
            
            Element nodoGiven = document.createElement("given");
            Text nodoValorGiven = document.createTextNode("Sebastian"); // Nombre
            nodoGiven.appendChild(nodoValorGiven);
            
            nodoName.appendChild(nodoGiven);
            
            nodoGiven = document.createElement("given");
            nodoValorGiven = document.createTextNode("Daniel"); // Segujndo nombre
            nodoGiven.appendChild(nodoValorGiven);
            
            nodoName.appendChild(nodoGiven);
            
            Element nodoFamily = document.createElement("family");
            Text nodoValorFamily = document.createTextNode("Peña"); // Apellido
            nodoGiven.appendChild(nodoValorFamily);
            
            nodoName.appendChild(nodoFamily);
            
            nodoFamily = document.createElement("family");
            nodoValorFamily = document.createTextNode("Peña"); // Apellido
            nodoGiven.appendChild(nodoValorFamily);
            
            nodoName.appendChild(nodoFamily);
            
            nodoPatient.appendChild(nodoName);
            
            Element nodoAdministrativeGenderCode = document.createElement("administrativeGenderCode");
            nodoAdministrativeGenderCode.setAttribute("code", "F");
            nodoAdministrativeGenderCode.setAttribute("codeSystem", "2.16.840.1.113883.5.1");
            nodoAdministrativeGenderCode.setAttribute("codeSystemName", "AdministrativeGender");
            nodoAdministrativeGenderCode.setAttribute("displayName", "MASCULINO");
            
            nodoPatient.appendChild(nodoAdministrativeGenderCode);
            
            Element nodoBirthTime = document.createElement("birthTime");
            nodoBirthTime.setAttribute("value", "19800720");
            
            nodoPatient.appendChild(nodoBirthTime);
            
            nodoPatientRole.appendChild(nodoPatient);
            
//            Text nodoValorCampo = document.createTextNode("Contenido del elemento hijo");
//            
//            nodoNombreCampo.setAttribute("ejAtributo", "si funciona");
//            nodoNombreCampo.appendChild(nodoValorCampo);
//            
//            raiz.appendChild(nodoNombreCampo);
            
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
