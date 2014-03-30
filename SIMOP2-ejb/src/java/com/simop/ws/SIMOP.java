/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.ws;

import com.simop.bean.AntecedenteFacadeLocal;
import com.simop.jpa.Chequeo;
import com.simop.jpa.Medico;
import com.simop.jpa.Paciente;
import com.simop.jpa.Tip;
import com.simop.bean.ChequeoFacadeLocal;
import com.simop.bean.MedicoFacadeLocal;
import com.simop.bean.PacienteFacadeLocal;
import com.simop.bean.TipFacadeLocal;
import com.simop.bean.UsuarioFacadeLocal;
import com.simop.jpa.Antecedente;
import com.simop.jpa.Consultorio;
import com.simop.jpa.PacientePK;
import com.simop.jpa.Usuario;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author 53B45
 */
@WebService(serviceName = "SIMOP")
@Stateless()
public class SIMOP {
    
    @EJB
    private UsuarioFacadeLocal ejbUsuario;
    @EJB
    private ChequeoFacadeLocal ejbChequeo;
    @EJB
    private PacienteFacadeLocal ejbPaciente;
    @EJB
    private TipFacadeLocal ejbTip;
    @EJB
    private MedicoFacadeLocal ejbMedico;
    @EJB
    private AntecedenteFacadeLocal ejbAntecedente;

    /**
     * Operacion para guardar cualquier tipo de monitoreo que se haga un paciente previamente registrado
     */
    
    @WebMethod(operationName = "guardarChequeo")
    public String guardarChequeo(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "tipo") String tipo, @WebParam(name = "valor") String valor,
            @WebParam(name = "unidades") String unidades, @WebParam(name = "fecha") String fecha,
            @WebParam(name = "hora") String hora, @WebParam(name = "descripcion") String descripcion,
            @WebParam(name = "latitud") String latitud, @WebParam(name = "longitud") String longitud,
            @WebParam(name = "nota") String nota) {
        
        Paciente usuarioPaciente = null;
        
        loggin:{
            for(Usuario user : ejbUsuario.findAll()){

                if(user.getCorreo().equals(correo) && user.getContraseña().equals(clave)){
                    
                    List<Paciente> pac = user.getPacienteList();

                    for(Paciente p : pac){

                        if(p.getUsuarioID().getCorreo().equals(correo) && p.getUsuarioID().getContraseña().equals(clave)){
                            usuarioPaciente = p;
                            break loggin;
                        }
                    }
                }
            }
        }
        
        if(usuarioPaciente == null){
            return "no se encontro el usuario, los datos no se guardaron";
        }
        
        Date dFecha = null;
        Date dHora = null;
        
        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            dFecha = formato.parse(fecha);
            formato = new SimpleDateFormat("HH:mm:ss");
            dHora = formato.parse(hora);
        } catch (ParseException ex) {
            Logger.getLogger(SIMOP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Chequeo chequeo = new Chequeo();
        
        Tip tip = null;
        
        List<Tip> tipList = ejbTip.findAll();
        
        // obtener el tip correspondiente a cada tipo de prueba
        switch (tipo) {
            case "glucosa": // buscar tip para glucosa
                for(Tip tempTip : tipList){

                    if(tempTip.getTipochequeo().equals("glucosa") && tempTip.getDescripcion().equals(descripcion)){

                        int min = Integer.parseInt(tempTip.getMin());
                        int max = Integer.parseInt(tempTip.getMax());
                        int val = Integer.parseInt(valor);

                        if(val >= min && val <= max){
                            tip = tempTip;
                            break;
                        }
                    }
                }
                break;
            case "presion": // buscar tip para presion
                int sistolica, diastolica, index;
                index = valor.indexOf('/');
                sistolica = Integer.parseInt(valor.substring(0, index));
                diastolica = Integer.parseInt(valor.substring(index+1, valor.length()));

                String tip1 = null, tip2 = null;
                int id1 = 0, id2 = 0;

                for(Tip temTip : tipList){

                    if(temTip.getTipochequeo().equals("presion")){

                        int min = Integer.parseInt(temTip.getMin());
                        int max = Integer.parseInt(temTip.getMax());

                        if(temTip.getDescripcion().equals("sistolica") && sistolica >= min && sistolica <= max){
                            tip1 = temTip.getEstado();
                            id1 = temTip.getIdtip();
                        } else {
                            if(temTip.getDescripcion().equals("diastolica") && diastolica >= min && diastolica <= max){
                                tip2 = temTip.getEstado();
                                id2 = temTip.getIdtip();
                            }
                        }
                        
                    }
                    
                    if(tip1 != null && tip2 != null){
                        break;
                    }
                }

                if(tip1 != null && tip2!= null){
                    //System.out.println("no son nulos");
                    if(tip1.equals(tip2) && id1 != 0){
                        tip = ejbTip.find(id1);
                    } else {
                        if(!tip1.equals("normal")){
                            tip = ejbTip.find(id1);
                        } else if(!tip2.equals("normal")){
                            tip = ejbTip.find(id2);
                        }
                    }
                }
                break;
            case "arritmia": // tip para ritmo cardiaco
                for(Tip tempTip : tipList){

                    if(tempTip.getTipochequeo().equals("ritmo_cardiaco") && tempTip.getDescripcion().equals(descripcion)){

                        int min = Integer.parseInt(tempTip.getMin());
                        int max = Integer.parseInt(tempTip.getMax());
                        int val = Integer.parseInt(valor);

                        if(val >= min && val <= max){
                            tip = tempTip;
                            break;
                        }
                    }
                }
                break;
        }
        // fin obtener tip
        
        chequeo.setDescripcion(descripcion);
        chequeo.setFecha(dFecha);
        chequeo.setHora(dHora);
        chequeo.setLatitud(latitud);
        chequeo.setLongitud(longitud);
        chequeo.setPaciente(usuarioPaciente);
        chequeo.setTipIdtip(tip != null ? tip : ejbTip.find(21));
        chequeo.setTipochequeo(tipo);
        chequeo.setUnidades(unidades);
        chequeo.setValor(valor);
        chequeo.setNota(nota);
        
        ejbChequeo.create(chequeo);
        
        // guardar antecedente si lo hay
        if(tip != null && !tip.getEstado().equals("normal")){
            
            Antecedente antecedente = new Antecedente();
            antecedente.setChequeoIdchequeo(chequeo);
            antecedente.setPaciente(usuarioPaciente);
            ejbAntecedente.create(antecedente);
        }
        
        return tip != null ? tip.getContenido() : chequeo.getTipIdtip().getContenido();
        
    }

    /**
     * Operacion para extraer datos estadisticos sea de un paciente o el total del sistema
     */
    
    @WebMethod(operationName = "obtenerEstadisticas")
    public String obtenerEstadisticas(@WebParam(name = "correo") String correo,
            @WebParam(name = "clave") String clave, @WebParam(name = "fechaInicio") String fechaInicio,
            @WebParam(name = "fechaFin") String fechaFin, @WebParam(name = "tipoChequeo") String tipoChequeo,
            @WebParam(name = "modo") int modo, @WebParam(name = "numeroId") String numeroId,
            @WebParam(name = "tipoId") String tipoId) {
        
        // modo:
        // 0 -> el paciente revisa sus propias estadisticas
        // 1 -> el medico o consultorio revisa las estadisticas de un paciente
        // 2 -> se puede revisar las estadisticas de todos los pacientes
        
        String salida = "";
        
        SimpleDateFormat formato1, formato2;
        
        formato1 = new SimpleDateFormat("yyyy-MM-dd");
        formato2 = new SimpleDateFormat("HH:mm:ss");
        
        Date fInicio;
        Date fFin;

        try {
            fInicio = formato1.parse(fechaInicio);
            fFin = formato1.parse(fechaFin);
        } catch (ParseException ex) {
            System.err.println("error al convertir fecha");
            Logger.getLogger(SIMOP.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        switch(modo){
            
            case 0:
                
                Paciente usuarioPaciente = null;

                loggin:
                {
                    for (Usuario user : ejbUsuario.findAll()) {

                        if (user.getCorreo().equals(correo) && user.getContraseña().equals(clave) && user.getRoll().equals("paciente")) {
                            
                            List<Paciente> pac = user.getPacienteList();

                            for (Paciente p : pac) {

                                if (p.getUsuarioID().getCorreo().equals(correo) && p.getUsuarioID().getContraseña().equals(clave)) {
                                    
                                    usuarioPaciente = p;
                                    break loggin;
                                }
                            }
                        }
                    }
                }

                //System.out.println("verifica para retornar null o no");
                if (usuarioPaciente == null) {
                    return null;
                }
                
                //System.out.println("paso retorno null");
                List<Chequeo> cList = ejbChequeo.findAll();
                
                for (Chequeo temp : cList) {

                    if (temp.getTipochequeo().equals(tipoChequeo) && fInicio.before(temp.getFecha()) && fFin.after(temp.getFecha())
                            && temp.getPaciente().getPacientePK() == usuarioPaciente.getPacientePK()) {
                        
                        salida += temp.getValor() + ";" + temp.getUnidades() + ";" + formato1.format(temp.getFecha())
                                + ";" + formato2.format(temp.getHora()) + ";" + temp.getDescripcion() + "\n";
                    }
                }
                
                break;
                
            case 1:
                
                Medico usuarioMedico = null;
                Consultorio usuarioConsultorio = null;

                loggin:
                {
                    for (Usuario user : ejbUsuario.findAll()) {

                        if (user.getCorreo().equals(correo) && user.getContraseña().equals(clave)
                                && (user.getRoll().equals("medico") || user.getRoll().equals("consultorio"))) {
                            
                            if(user.getRoll().equals("medico")){
                                
                                List<Medico> med = user.getMedicoList();

                                for (Medico m : med) {

                                    if (m.getUsuarioID().getCorreo().equals(correo) && m.getUsuarioID().getContraseña().equals(clave)) {

                                        usuarioMedico = m;
                                        break loggin;
                                    }
                                }
                            } else if(user.getRoll().equals("consultorio")){
                                
                                List<Consultorio> con = user.getConsultorioList();

                                for (Consultorio c : con) {

                                    if (c.getUsuarioID().getCorreo().equals(correo) && c.getUsuarioID().getContraseña().equals(clave)) {

                                        usuarioConsultorio = c;
                                        break loggin;
                                    }
                                }
                            }
                            
                        }
                    }
                }
                
                if(usuarioMedico == null && usuarioConsultorio == null){
                    //System.out.println("usuario nulo");
                    return null;
                }
                
                //Paciente usuarioPaciente2 = new Paciente(Integer.parseInt(numeroId), tipoId);
                Paciente usuarioPaciente2 = ejbPaciente.find(new PacientePK(Integer.parseInt(numeroId), tipoId));
                
                if(usuarioPaciente2 == null){
                    //System.out.println("el paciente existe: " + usuarioPaciente2.getApellidos());
                    return null;
                }
                
                List<Chequeo> cList2 = ejbChequeo.findAll();
                
                for (Chequeo temp : cList2) {

                    if (temp.getTipochequeo().equals(tipoChequeo) && fInicio.before(temp.getFecha()) && fFin.after(temp.getFecha())
                            && temp.getPaciente().getPacientePK() == usuarioPaciente2.getPacientePK()) {
                        
                        salida += temp.getValor() + ";" + temp.getUnidades() + ";" + formato1.format(temp.getFecha())
                                + ";" + formato2.format(temp.getHora()) + ";" + temp.getDescripcion() + ";" + temp.getNota()
                                + "\n";
                    }
                }
                
                break;
                
            case 2:
                
                boolean usuario = false;

                loggin:
                {
                    for (Usuario user : ejbUsuario.findAll()) {

                        if (user.getCorreo().equals(correo) && user.getContraseña().equals(clave)) {
                            usuario = true;
                        }
                    }
                }
                
                if(!usuario){
                    return null;
                }
                
//                Paciente usuarioPaciente3 = ejbPaciente.find(new PacientePK(Integer.parseInt(numeroId), tipoId));
//                
//                if(usuarioPaciente3 == null){
//                    //System.out.println("el paciente existe: " + usuarioPaciente2.getApellidos());
//                    return null;
//                }
                
                List<Chequeo> cList3 = ejbChequeo.findAll();
                
                for (Chequeo temp : cList3) {

                    if (temp.getTipochequeo().equals(tipoChequeo) && fInicio.before(temp.getFecha()) 
                            && fFin.after(temp.getFecha())) {
                        
                        salida += temp.getValor() + ";" + temp.getUnidades() + ";" + formato1.format(temp.getFecha())
                                + ";" + formato2.format(temp.getHora()) + ";" + temp.getDescripcion() + ";" 
                                + temp.getLatitud() + ";" + temp.getLongitud() + "\n";
                    }
                }
                
                break;
                
            default:
                break;
        }
        
        return salida;
    }

    /**
     * Operacion para obtener el listado de paciente de un medico o consultorio
     */
    
    @WebMethod(operationName = "listaPacientes")
    public ArrayList<RegistroPaciente> listaPacientes() {
        
        List<Paciente> pList = ejbPaciente.findAll();
        
        ArrayList<RegistroPaciente> pacientes = new ArrayList<>();
        
        for(Paciente p : pList){
            
            RegistroPaciente rp = new RegistroPaciente();
            
            rp.setNombres(p.getUsuarioID().getNombres());
            rp.setApellidos(p.getApellidos());
            rp.setDireccion(p.getUsuarioID().getDireccion());
            rp.setTelefono(String.valueOf(p.getUsuarioID().getTelefono()));
            rp.setCorreo(p.getUsuarioID().getCorreo());
            rp.setSexo(p.getSexo());
            rp.setEstatura(p.getEstatura());
            rp.setImc(p.getImc());
            rp.setGrupoSanguineo(p.getGruposan());
            rp.setRh(p.getRh() ? "+" : "-");
            rp.setEdad(p.getEdad());
            
            pacientes.add(rp);
        }
        
        return pacientes;
    }

    /**
     * Operacion para listar los medicos para un paciente o consultorio
     */
    
    @WebMethod(operationName = "listaMedicos")
    public ArrayList<RegistroMedico> listaMedicos() {
        
        List<Medico> mlist = ejbMedico.findAll();
        
        ArrayList<RegistroMedico> medicos = new ArrayList<>();
        
        for(Medico m : mlist){
            
            RegistroMedico rm = new RegistroMedico();
            
            rm.setNombres(m.getUsuarioID().getNombres());
            rm.setApellidos(m.getApellidos());
            rm.setSexo(m.getSexo());
            rm.setNacionalidad(m.getNacionalidad());
            
            medicos.add(rm);
        }
        
        return medicos;
    }
}
