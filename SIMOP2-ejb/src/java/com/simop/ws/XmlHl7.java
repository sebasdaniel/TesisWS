/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.ws;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    
    private String municipioPaciente;
    private String departamentoPaciente;
    private String nombre1Paciente;
    private String nombre2Paciente;
    private String apellido1Paciente;
    private String apellido2Paciente;
    private String codigoGeneroPaciente; // F, M, ND
    private String generoPaciente;
    private String telefonoConsultorio; // 123.45.67
    private String municipioConsultorio;
    private String departamentoConsultorio;
    private ArrayList<String[]> datosHistorial;
    
    private String salida = null;
    
    public XmlHl7(){
        
    }
    
    public void procesarXmlHl7(){
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
            
            // cabecera
            Element nodoTitle = document.createElement("title");
            Text nodoValorTitle = document.createTextNode("HISTORIAL");
            nodoTitle.appendChild(nodoValorTitle);
            
            raiz.appendChild(nodoTitle);
            
            Element nodoEffectiveTime = document.createElement("effectiveTime");
            nodoEffectiveTime.setAttribute("value", fechaActual());
            
            raiz.appendChild(nodoEffectiveTime);
            
            Element nodoConfidentialityCode = document.createElement("confidentialityCode");
            nodoConfidentialityCode.setAttribute("code", "R");
            nodoConfidentialityCode.setAttribute("codeSystem", "2.16.840.1.113883.5.25");
            nodoConfidentialityCode.setAttribute("codeSystemName", "confidentiality");
            nodoConfidentialityCode.setAttribute("displayName", "Restringido");
            
            raiz.appendChild(nodoConfidentialityCode);
            // fin cabecera
            
            // registro clinico
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
            nodoCity.appendChild(document.createTextNode(municipioPaciente));
            
            Element nodoState = document.createElement("state");
            nodoState.appendChild(document.createTextNode(departamentoPaciente));
            
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
            //Text nodoValorGiven = document.createTextNode(nombre1Paciente); // Nombre
            //nodoGiven.appendChild(nodoValorGiven);
            nodoGiven.appendChild(document.createTextNode(nombre1Paciente));
            
            nodoName.appendChild(nodoGiven);
            
            if(!nombre2Paciente.isEmpty()){
                
                nodoGiven = document.createElement("given");
//            nodoValorGiven = document.createTextNode(nombre2Paciente); // Segundo nombre
//            nodoGiven.appendChild(nodoValorGiven);
                nodoGiven.appendChild(document.createTextNode(nombre2Paciente));

                nodoName.appendChild(nodoGiven);
            }
            
            
            Element nodoFamily = document.createElement("family");
//            Text nodoValorFamily = document.createTextNode(apellido1Paciente); // Apellido
//            nodoGiven.appendChild(nodoValorFamily);
            nodoFamily.appendChild(document.createTextNode(apellido1Paciente));
            
            nodoName.appendChild(nodoFamily);
            
            if(!apellido2Paciente.isEmpty()){
                
                nodoFamily = document.createElement("family");
    //            nodoValorFamily = document.createTextNode(apellido2Paciente); // Segundo Apellido
    //            nodoGiven.appendChild(nodoValorFamily);
                nodoFamily.appendChild(document.createTextNode(apellido2Paciente));

                nodoName.appendChild(nodoFamily);
            }
            
            nodoPatient.appendChild(nodoName);
            
            Element nodoAdministrativeGenderCode = document.createElement("administrativeGenderCode");
            nodoAdministrativeGenderCode.setAttribute("code", codigoGeneroPaciente);
            nodoAdministrativeGenderCode.setAttribute("codeSystem", "2.16.840.1.113883.5.1");
            nodoAdministrativeGenderCode.setAttribute("codeSystemName", "AdministrativeGender");
            nodoAdministrativeGenderCode.setAttribute("displayName", generoPaciente);
            
            nodoPatient.appendChild(nodoAdministrativeGenderCode);
            
//            Element nodoBirthTime = document.createElement("birthTime");
//            nodoBirthTime.setAttribute("value", "19800720");
//            
//            nodoPatient.appendChild(nodoBirthTime);
            
            nodoPatientRole.appendChild(nodoPatient);
            
            Element nodoProviderOrganization = document.createElement("providerOrganization");
            nodoProviderOrganization.setAttribute("classCode", "ORG");
            nodoProviderOrganization.setAttribute("determinerCode", "INSTANCE");
            
            nodoId = document.createElement("id");
            nodoId.setAttribute("root", "2.16.840.1.113883.19.57.2.1.2.1");
            nodoId.setAttribute("extension", "234170024801");
            nodoId.setAttribute("assigningAuthorityName", "MinSalud");
            
            nodoProviderOrganization.appendChild(nodoId);
            
            nodoName = document.createElement("name");
            Text nodoValorName = document.createTextNode("CONSULTORIO");
            nodoName.appendChild(nodoValorName);
            
            nodoProviderOrganization.appendChild(nodoName);
            
            Element nodoTelecom = document.createElement("telecom");
            nodoTelecom.setAttribute("value", "tel: +57(4) " + telefonoConsultorio);
            
            nodoProviderOrganization.appendChild(nodoTelecom);
            
//            nodoTelecom = document.createElement("telecom");
//            nodoTelecom.setAttribute("value", "fax: +57(4) 773.51.61");
//            
//            nodoProviderOrganization.appendChild(nodoTelecom);
            
            nodoAddr = document.createElement("addr");
            
            nodoCity = document.createElement("city");
            nodoCity.appendChild(document.createTextNode(municipioConsultorio));
            
            nodoAddr.appendChild(nodoCity);
            
            nodoState = document.createElement("state");
            nodoState.appendChild(document.createTextNode(departamentoConsultorio));
            
            nodoAddr.appendChild(nodoState);
            
            Element nodoCountry = document.createElement("country");
            nodoCountry.appendChild(document.createTextNode("Colombia"));
            
            nodoAddr.appendChild(nodoCountry);
            
            nodoProviderOrganization.appendChild(nodoAddr);
            nodoPatientRole.appendChild(nodoProviderOrganization);
            nodoRecordTarget.appendChild(nodoPatientRole);
            
            raiz.appendChild(nodoRecordTarget);
            // fin registro clinico
            
            // datos del autor
            Element nodoAuthor = document.createElement("author");
            nodoAuthor.setAttribute("typeCode", "AUT");
            nodoAuthor.setAttribute("contextControlCode", "OP");
            
            Element nodoTime = document.createElement("time");
            nodoTime.setAttribute("value", fechaActual());
            
            nodoAuthor.appendChild(nodoTime);
            
            Element nodoAssignedAuthor = document.createElement("assignedAuthor");
            
            nodoId = document.createElement("id");
            nodoId.setAttribute("root", "2.16.840.1.113883.19.57.2.1.1.1.1");
            nodoId.setAttribute("extension", "123456321");
            nodoId.setAttribute("assigningAuthorityName", "MinSalud");
            
            nodoAssignedAuthor.appendChild(nodoId);
            
            Element nodoAssignedPerson = document.createElement("assignedPerson");
            nodoAssignedPerson.setAttribute("classCode", "PSN");
            nodoAssignedPerson.setAttribute("determinerCode", "INSTANCE");
            
            nodoName = document.createElement("name");
            
            nodoGiven = document.createElement("given");
            nodoGiven.appendChild(document.createTextNode("SIMOP"));
            nodoName.appendChild(nodoGiven);
            
//            nodoFamily = document.createElement("family");
//            nodoName.appendChild(nodoFamily);
            
            nodoAssignedPerson.appendChild(nodoName);
            nodoAssignedAuthor.appendChild(nodoAssignedPerson);
            nodoAuthor.appendChild(nodoAssignedAuthor);
            
            raiz.appendChild(nodoAuthor);
            // fin datos autor
            
            // datos encargado
            Element nodoCustodian = document.createElement("custodian");
            nodoCustodian.setAttribute("typeCode", "CST");
            
            Element nodoAssignedCustodian = document.createElement("assignedCustodian");
            nodoAssignedCustodian.setAttribute("classCode", "ASSIGNED");
            
            Element nodoRepresentedCustodianOrganization = document.createElement("representedCustodianOrganization");
            nodoRepresentedCustodianOrganization.setAttribute("classCode", "ORG");
            nodoRepresentedCustodianOrganization.setAttribute("determinerCode", "INSTANCE");
            
            nodoId = document.createElement("id");
            nodoId.setAttribute("root", "2.16.840.1.113883.19.57.2.1.2.1");
            nodoId.setAttribute("extension", "234170024801");
            nodoId.setAttribute("assigningAuthorityName", "MinSalud");
            
            nodoRepresentedCustodianOrganization.appendChild(nodoId);
            
            nodoName = document.createElement("name");
            nodoName.appendChild(document.createTextNode("CONSULTORIO"));
            
            nodoRepresentedCustodianOrganization.appendChild(nodoName);
            nodoAssignedCustodian.appendChild(nodoRepresentedCustodianOrganization);
            nodoCustodian.appendChild(nodoAssignedCustodian);
            
            raiz.appendChild(nodoCustodian);
            // fin datos encargado
            
            // cuerpo
            Element nodoComponent = document.createElement("component");
            nodoComponent.setAttribute("typeCode", "COMP");
            nodoComponent.setAttribute("contextConductionInd", "true");
            
            Element nodoStructuredBody = document.createElement("structuredBody");
            nodoStructuredBody.setAttribute("classCode", "DOCBODY");
            nodoStructuredBody.setAttribute("moodCode", "EVN");
            
            Element nodoComponent2 = document.createElement("component");
            nodoComponent2.setAttribute("typeCode", "COMP");
            nodoComponent2.setAttribute("contextConductionInd", "true");
            
            Element nodoSection = document.createElement("section");
            nodoSection.setAttribute("classCode", "DOCSECT");
            nodoSection.setAttribute("moodCode", "EVN");
            
            nodoCode = document.createElement("code");
            nodoCode.setAttribute("code", "72231-4");
            nodoCode.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
            nodoCode.setAttribute("codeSystemName", "LOINC");
            nodoCode.setAttribute("displayName", "CONSULTATION NOTE");
            
            nodoSection.appendChild(nodoCode);
            
            nodoTitle = document.createElement("title");
            nodoTitle.appendChild(document.createTextNode("NOTA DE EVOLUCIÓN"));
            
            nodoSection.appendChild(nodoTitle);
            
            // el core:
            Element nodoText = document.createElement("text");
            Element nodoTable = document.createElement("table");
            Element nodoThead = document.createElement("thead");
            Element nodoTr = document.createElement("tr");
            
            Element nodoTh = document.createElement("th");
            nodoTh.appendChild(document.createTextNode("Tipo de Prueba"));
            nodoTr.appendChild(nodoTh);
            
            nodoTh = document.createElement("th");
            nodoTh.appendChild(document.createTextNode("Valor Resultado"));
            nodoTr.appendChild(nodoTh);
            
            nodoTh = document.createElement("th");
            nodoTh.appendChild(document.createTextNode("Unidades Resultado"));
            nodoTr.appendChild(nodoTh);
            
            nodoTh = document.createElement("th");
            nodoTh.appendChild(document.createTextNode("Rango"));
            nodoTr.appendChild(nodoTh);
            
            nodoTh = document.createElement("th");
            nodoTh.appendChild(document.createTextNode("Estado"));
            nodoTr.appendChild(nodoTh);
            
            nodoTh = document.createElement("th");
            nodoTh.appendChild(document.createTextNode("Descripción"));
            nodoTr.appendChild(nodoTh);
            
            nodoThead.appendChild(nodoTr);
            
            nodoTable.appendChild(nodoThead);
            
            Element nodoTbody = document.createElement("tbody");
            
            for(String[] registro : datosHistorial){
                
                nodoTr = document.createElement("tr");
            
                Element nodoTd = document.createElement("td");
                nodoTd.appendChild(document.createTextNode(registro[0]));
                nodoTr.appendChild(nodoTd);

                nodoTd = document.createElement("td");
                nodoTd.appendChild(document.createTextNode(registro[1]));
                nodoTr.appendChild(nodoTd);

                nodoTd = document.createElement("td");
                nodoTd.appendChild(document.createTextNode(registro[2]));
                nodoTr.appendChild(nodoTd);

                nodoTd = document.createElement("td");
                nodoTd.appendChild(document.createTextNode(registro[3]));
                nodoTr.appendChild(nodoTd);

                nodoTd = document.createElement("td");
                nodoTd.appendChild(document.createTextNode(registro[4]));
                nodoTr.appendChild(nodoTd);

                nodoTd = document.createElement("td");
                nodoTd.appendChild(document.createTextNode(registro[5]));
                nodoTr.appendChild(nodoTd);

                nodoTbody.appendChild(nodoTr);
                
            }
            
//            nodoTr = document.createElement("tr");
//            
//            Element nodoTd = document.createElement("td");
//            nodoTd.appendChild(document.createTextNode("Presión"));
//            nodoTr.appendChild(nodoTd);
//            
//            nodoTd = document.createElement("td");
//            nodoTd.appendChild(document.createTextNode("100/60"));
//            nodoTr.appendChild(nodoTd);
//            
//            nodoTd = document.createElement("td");
//            nodoTd.appendChild(document.createTextNode("mmHg"));
//            nodoTr.appendChild(nodoTd);
//            
//            nodoTd = document.createElement("td");
//            nodoTd.appendChild(document.createTextNode("120/80"));
//            nodoTr.appendChild(nodoTd);
//            
//            nodoTd = document.createElement("td");
//            nodoTd.appendChild(document.createTextNode("Bajo"));
//            nodoTr.appendChild(nodoTd);
//            
//            nodoTd = document.createElement("td");
//            nodoTd.appendChild(document.createTextNode("prueba realizada en reposo"));
//            nodoTr.appendChild(nodoTd);
//            
//            nodoTbody.appendChild(nodoTr);
            
            nodoTable.appendChild(nodoTbody);
            nodoText.appendChild(nodoTable);
            
//            Element nodoParagraph = document.createElement("paragraph");
//            nodoParagraph.appendChild(document.createTextNode("Se le recomienda ingerir de manera urgente un alimento"
//                    + " con alto contenido en azúcar"));
//            
//            nodoText.appendChild(nodoParagraph);
            
            nodoSection.appendChild(nodoText);
            nodoComponent2.appendChild(nodoSection);
            nodoStructuredBody.appendChild(nodoComponent2);
            nodoComponent.appendChild(nodoStructuredBody);
            
            raiz.appendChild(nodoComponent);
            
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer;
            try {
                transformer = tf.newTransformer();
                // below code to remove XML declaration
                // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                StringWriter writer = new StringWriter();
                transformer.transform(new DOMSource(document), new StreamResult(writer));
                setSalida(writer.getBuffer().toString());
                
            } catch (TransformerException e) {
                System.err.println("Ha ocurrido una exception:\n" + e);
            }
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XmlHl7.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String getXmlInString(){
        return salida;
    }

    private String fechaActual() {
        
        Date fecha = Calendar.getInstance().getTime();
        
        SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmss");
        
        return formato.format(fecha);
    }

    /**
     * @param municipioPaciente the municipioPaciente to set
     */
    public void setMunicipioPaciente(String municipioPaciente) {
        this.municipioPaciente = municipioPaciente;
    }

    /**
     * @param departamentoPaciente the departamentoPaciente to set
     */
    public void setDepartamentoPaciente(String departamentoPaciente) {
        this.departamentoPaciente = departamentoPaciente;
    }

    /**
     * @param codigoGeneroPaciente the codigoGeneroPaciente to set
     */
    public void setCodigoGeneroPaciente(String codigoGeneroPaciente) {
        this.codigoGeneroPaciente = codigoGeneroPaciente;
    }

    /**
     * @param generoPaciente the generoPaciente to set
     */
    public void setGeneroPaciente(String generoPaciente) {
        this.generoPaciente = generoPaciente;
    }

    /**
     * @param telefonoConsultorio the telefonoConsultorio to set
     */
    public void setTelefonoConsultorio(String telefonoConsultorio) {
        this.telefonoConsultorio = telefonoConsultorio;
    }

    /**
     * @param municipioConsultorio the municipioConsultorio to set
     */
    public void setMunicipioConsultorio(String municipioConsultorio) {
        this.municipioConsultorio = municipioConsultorio;
    }

    /**
     * @param departamentoConsultorio the departamentoConsultorio to set
     */
    public void setDepartamentoConsultorio(String departamentoConsultorio) {
        this.departamentoConsultorio = departamentoConsultorio;
    }

    /**
     * @param datosHistorial the datosHistorial to set
     */
    public void setDatosHistorial(ArrayList<String[]> datosHistorial) {
        this.datosHistorial = datosHistorial;
    }

    /**
     * @param salida the salida to set
     */
    public void setSalida(String salida) {
        this.salida = salida;
    }

    /**
     * @param nombre1Paciente the nombre1Paciente to set
     */
    public void setNombre1Paciente(String nombre1Paciente) {
        this.nombre1Paciente = nombre1Paciente;
    }

    /**
     * @param nombre2Paciente the nombre2Paciente to set
     */
    public void setNombre2Paciente(String nombre2Paciente) {
        this.nombre2Paciente = nombre2Paciente;
    }

    /**
     * @param apellido1Paciente the apellido1Paciente to set
     */
    public void setApellido1Paciente(String apellido1Paciente) {
        this.apellido1Paciente = apellido1Paciente;
    }

    /**
     * @param apellido2Paciente the apellido2Paciente to set
     */
    public void setApellido2Paciente(String apellido2Paciente) {
        this.apellido2Paciente = apellido2Paciente;
    }
}
