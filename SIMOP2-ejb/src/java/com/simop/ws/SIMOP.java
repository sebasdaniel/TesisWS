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
import com.simop.bean.ConsultorioFacadeLocal;
import com.simop.bean.DiagnosticoFacadeLocal;
import com.simop.bean.MedicoFacadeLocal;
import com.simop.bean.AtiendeFacadeLocal;
import com.simop.bean.DepartamentoFacadeLocal;
import com.simop.bean.EspecialidadFacadeLocal;
import com.simop.bean.MunicipioFacadeLocal;
import com.simop.bean.PacienteFacadeLocal;
import com.simop.bean.SolicitudConsultorioFacadeLocal;
import com.simop.bean.SolicitudMedicoFacadeLocal;
import com.simop.bean.TipFacadeLocal;
import com.simop.bean.UsuarioFacadeLocal;
import com.simop.jpa.Antecedente;
import com.simop.jpa.Consultorio;
import com.simop.jpa.Diagnostico;
import com.simop.jpa.Atiende;
import com.simop.jpa.Departamento;
import com.simop.jpa.Especialidad;
import com.simop.jpa.Municipio;
import com.simop.jpa.PacientePK;
import com.simop.jpa.SolicitudConsultorio;
import com.simop.jpa.SolicitudConsultorioPK;
import com.simop.jpa.SolicitudMedico;
import com.simop.jpa.SolicitudMedicoPK;
import com.simop.jpa.Usuario;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private AtiendeFacadeLocal ejbAtiende;
    @EJB
    private AntecedenteFacadeLocal ejbAntecedente;
    @EJB
    private SolicitudConsultorioFacadeLocal ejbSolicitudConsultorio;
    @EJB
    private SolicitudMedicoFacadeLocal ejbSolicitudMedico;
    @EJB
    private ConsultorioFacadeLocal ejbConsultorio;
    @EJB
    private DiagnosticoFacadeLocal ejbDiagnostico;
    @EJB
    private MunicipioFacadeLocal ejbMunicipio;
    @EJB
    private EspecialidadFacadeLocal ejbEspecialidad;
    @EJB
    private DepartamentoFacadeLocal ejbDepartamento;

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

                if(user.getEmail().equals(correo) && user.getClave().equals(clave)){
                    
                    List<Paciente> pac = user.getPacienteList();

                    for(Paciente p : pac){

                        if(p.getUsuarioID().getEmail().equals(correo) && p.getUsuarioID().getClave().equals(clave)){
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
        
        if(nota != null && !nota.isEmpty()){
            
            nota = nota.replaceAll("\n", " ");
            nota = nota.trim();
        }
        
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
            antecedente.setVista(Boolean.FALSE);
            ejbAntecedente.create(antecedente);
            
            for(Atiende atiende : usuarioPaciente.getAtiendeList()){
                
                if(atiende.getPaciente().getUsuarioID().getId() == usuarioPaciente.getUsuarioID().getId()){
                    
                    String regId = atiende.getMedico().getUsuarioID().getGcmRegId();
                    
                    if(regId != null && !regId.isEmpty()){
                        
                        GCM.send("alerta", regId, usuarioPaciente.getUsuarioID().getNombres() + " "
                                + usuarioPaciente.getApellidos() + "\n" + chequeo.getTipochequeo() + " "
                                + chequeo.getValor() + " " + chequeo.getUnidades());
                    }
                    
                }
            }
            
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

                        if (user.getEmail().equals(correo) && user.getClave().equals(clave) && user.getRoll().equals("paciente")) {
                            
                            List<Paciente> pac = user.getPacienteList();

                            for (Paciente p : pac) {

                                if (p.getUsuarioID().getEmail().equals(correo) && p.getUsuarioID().getClave().equals(clave)) {
                                    
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

                        if (user.getEmail().equals(correo) && user.getClave().equals(clave)
                                && (user.getRoll().equals("medico") || user.getRoll().equals("consultorio"))) {
                            
                            if(user.getRoll().equals("medico")){
                                
                                List<Medico> med = user.getMedicoList();

                                for (Medico m : med) {

                                    if (m.getUsuarioID().getEmail().equals(correo) && m.getUsuarioID().getClave().equals(clave)) {

                                        usuarioMedico = m;
                                        break loggin;
                                    }
                                }
                            } else if(user.getRoll().equals("consultorio")){
                                
                                List<Consultorio> con = user.getConsultorioList();

                                for (Consultorio c : con) {

                                    if (c.getUsuarioID().getEmail().equals(correo) && c.getUsuarioID().getClave().equals(clave)) {

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

                        if (user.getEmail().equals(correo) && user.getClave().equals(clave)) {
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
    public String listaPacientes(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "soloConsultorio") boolean soloConsultorio, @WebParam(name = "cedulaMedico") int cedulaMedico) {
        
        // Un medico obtiene la lista de todos sus pacientes, solo se nesesita correo y clave, pero los otros
        // parametros no pueden ser nulos, se recomienda 'soloConsultorio = false' y 'cedulaMedico = 0'.
        // Un consultorio puede obtener la lista de todos los pacientes asociados a este, para ello ademas
        // de los datos de sesion de la cuenta se nesesita como parametro:
        //  - 'soloConsultorio = true'
        //  - 'cedulaMedico = 0'
        // El consultorio tambien puede consultar la lista de pacientes de un medico asociado a dicho consultorio,
        // para ellos se modifican los anteriores parametros, quedando 'soloConsultorio = false' y la correspondiente
        // cedula del medico al que se le quiere consultar la lista de pacientes.
        
        //System.out.println("entro al metodo");
        for (Usuario user : ejbUsuario.findAll()) {
            //System.out.println("recorriendo usuarios");

            if (user.getEmail().equals(correo) && user.getClave().equals(clave)
                    && (user.getRoll().equals("medico") || user.getRoll().equals("consultorio"))) {

                //System.out.println("encontro usuario");
                
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                
                if(user.getRoll().equals("medico")){

                    //System.out.println("encontro que es medico");
                    List<Medico> med = user.getMedicoList();

                    for (Medico m : med) {

                        if (m.getUsuarioID().getEmail().equals(correo) && m.getUsuarioID().getClave().equals(clave)) {
                            //System.out.println("encontro al medico");
                            List<Atiende> relacion = ejbAtiende.findAll();
                            
                            String salida = "";
                            
                            for(Atiende temp : relacion){
                                
                                if(temp.getMedico().getCedulaMedico() == m.getCedulaMedico()){
                                    
                                    Paciente p = temp.getPaciente();
                                    
                                    salida += p.getPacientePK().getTipoid() + ";"
                                            + p.getPacientePK().getNumid() + ";"
                                            + p.getUsuarioID().getNombres() + ";"
                                            + p.getApellidos() + ";"
                                            + p.getUsuarioID().getTelefono() + ";"
                                            + p.getUsuarioID().getEmail() + ";"
                                            + p.getSexo() + ";"
                                            + Edad.calcularEdad(formato.format(p.getFechanac())) + "\n";
                                }
                            }
                            
                            return salida;
                        }
                    }
                } else if(user.getRoll().equals("consultorio")){

                    //System.out.println("encontro que es consultorio");
                    List<Consultorio> con = user.getConsultorioList();

                    for (Consultorio c : con) {

                        if (c.getUsuarioID().getEmail().equals(correo) && c.getUsuarioID().getClave().equals(clave)) {
                            //System.out.println("encontro consultorio");
                            if(soloConsultorio){
                                
                                List<SolicitudConsultorio> solicitud = ejbSolicitudConsultorio.findAll();

                                String salida = "";

                                for(SolicitudConsultorio temp : solicitud){
                                    
                                    // NOTA: falta validar que no tengan medico asignado
                                    if(temp.getConsultorio().getIdconsultorio() == c.getIdconsultorio()
                                            && temp.getEstado().equals("aprobado")){

                                        Paciente p = temp.getPaciente();
                                        
//                                        boolean tieneMedico = false;
//                                        
//                                        for(Atiende mp : p.getAtiendeList()){
//                                            
//                                            if(mp.getMedico() != null){
//                                                tieneMedico = true;
//                                            }
//                                        }
                                        
//                                        if(!tieneMedico){
                                            
                                            salida += p.getPacientePK().getTipoid() + ";"
                                                    + p.getPacientePK().getNumid() + ";"
                                                    + p.getUsuarioID().getNombres() + ";"
                                                    + p.getApellidos() + ";"
                                                    + p.getUsuarioID().getTelefono() + ";"
                                                    + p.getUsuarioID().getEmail() + ";"
                                                    + p.getSexo() + ";"
                                                    + Edad.calcularEdad(formato.format(p.getFechanac())) + "\n";
//                                        }
                                        
                                    }
                                }

                                return salida;
                                
                            } else {
                                
                                Medico medico = ejbMedico.find(cedulaMedico);
                                
                                if(medico == null){
                                    return null;
                                }
                                
                                List<Atiende> relacion = ejbAtiende.findAll();

                                String salida = "";

                                for(Atiende temp : relacion){

                                    if(temp.getMedico().getCedulaMedico() == medico.getCedulaMedico()){

                                        Paciente p = temp.getPaciente();

                                        salida += p.getPacientePK().getTipoid() + ";"
                                                    + p.getPacientePK().getNumid() + ";"
                                                    + p.getUsuarioID().getNombres() + ";"
                                                    + p.getApellidos() + ";"
                                                    + p.getUsuarioID().getTelefono() + ";"
                                                    + p.getUsuarioID().getEmail() + ";"
                                                    + p.getSexo() + ";"
                                                    + Edad.calcularEdad(formato.format(p.getFechanac())) + "\n";
                                    }
                                }

                                return salida;
                                
                            }
                        }
                    }
                }

            }
        }
        
        return "fail";
    }

    /**
     * Operacion para obtener el listado de paciente de un consultorio que no tengan medicos asignados
     */
    
    @WebMethod(operationName = "listaPacientesNoMedico")
    public String listaPacientesNoMedico(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave) {
        
        for (Usuario user : ejbUsuario.findAll()) {

            if (user.getEmail().equals(correo) && user.getClave().equals(clave)
                    && user.getRoll().equals("consultorio")) {
                
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                
//                if(user.getRoll().equals("consultorio")){
                    
                List<Consultorio> con = user.getConsultorioList();

                for (Consultorio c : con) {

                    if (c.getUsuarioID().getId() == user.getId()) {
                        
//                            if(soloConsultorio){

                        List<SolicitudConsultorio> solicitud = ejbSolicitudConsultorio.findAll();

                        String salida = "";

                        for(SolicitudConsultorio temp : solicitud){
                            
                            if(temp.getConsultorio().getIdconsultorio() == c.getIdconsultorio()
                                    && temp.getEstado().equals("aprobado")){

                                Paciente p = temp.getPaciente();

                                boolean tieneMedico = false;

                                for(Atiende mp : p.getAtiendeList()){

                                    if(mp.getMedico() != null){
                                        tieneMedico = true;
                                    }
                                }

                                if(!tieneMedico){

                                    salida += p.getPacientePK().getTipoid() + ";"
                                            + p.getPacientePK().getNumid() + ";"
                                            + p.getUsuarioID().getNombres() + ";"
                                            + p.getApellidos() + ";"
                                            + p.getUsuarioID().getTelefono() + ";"
                                            + p.getUsuarioID().getEmail() + ";"
                                            + p.getSexo() + ";"
                                            + Edad.calcularEdad(formato.format(p.getFechanac())) + "\n";
                                }

                            }
                        }

                        return salida;

//                            }
                    }
                }
//                }

            }
        }
        
        return "fail";
    }

    /**
     * Operacion para listar los medicos para un paciente o consultorio
     */
    
    @WebMethod(operationName = "listaMedicos")
    public String listaMedicos(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave) {
        
        for (Usuario user : ejbUsuario.findAll()) {

            if (user.getEmail().equals(correo) && user.getClave().equals(clave)) {
                
                List<Medico> mlist = ejbMedico.findAll();
        
                String salida = "";

                for(Medico m : mlist){

                    salida += m.getCedulaMedico() + ";"
                            + m.getUsuarioID().getNombres() + ";"
                            + m.getApellidos() + ";"
                            + m.getUsuarioID().getTelefono() + ";"
                            + m.getUsuarioID().getEmail() + ";"
                            + m.getSexo() + "\n";
                }

                return salida;
            }
        }
        
        return "fail";
    }

    /**
     * Metodo para obtener las solicitudes pendientes hechas a un medico o consultorio
     */
    @WebMethod(operationName = "listarSolicitudes")
    public String listarSolicitudes(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && (usuario.getRoll().equals("medico") || usuario.getRoll().equals("consultorio"))){
                
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                
                switch (usuario.getRoll()) {
                    
                    case "medico":
                        List<Medico> medicos = usuario.getMedicoList();
                        for(Medico medico : medicos){
                            if(medico.getUsuarioID().getId() == usuario.getId()){
                                
                                List<SolicitudMedico> solicitudes = medico.getSolicitudMedicoList();
                                String salida = "";
                                
                                for(SolicitudMedico solicitud : solicitudes){
                                    if(solicitud.getEstado().equals("pendiente")){
                                        
                                        Paciente p = solicitud.getPaciente();

                                        salida += p.getPacientePK().getTipoid() + ";"
                                                + p.getPacientePK().getNumid() + ";"
                                                + p.getUsuarioID().getNombres() + ";"
                                                + p.getApellidos() + ";"
                                                + p.getUsuarioID().getTelefono() + ";"
                                                + p.getUsuarioID().getEmail() + ";"
                                                + p.getSexo() + ";"
                                                + Edad.calcularEdad(formato.format(p.getFechanac())) + ";"
                                                + formato.format(solicitud.getFechaSolicitud()) + "\n";
                                    }
                                }
                                
                                return salida;
                            }
                        }
                        break;
                        
                    case "consultorio":
                        List<Consultorio> consultorios = usuario.getConsultorioList();
                        for(Consultorio consultorio : consultorios){
                            if(consultorio.getUsuarioID().getId() == usuario.getId()){
                                
                                List<SolicitudConsultorio> solicitudes = ejbSolicitudConsultorio.findAll();
                                String salida = "";
                                
                                for(SolicitudConsultorio solicitud : solicitudes){
                                    if(solicitud.getEstado().equals("pendiente")){
                                        
                                        Paciente p = solicitud.getPaciente();

                                        salida += p.getPacientePK().getTipoid() + ";"
                                                + p.getPacientePK().getNumid() + ";"
                                                + p.getUsuarioID().getNombres() + ";"
                                                + p.getApellidos() + ";"
                                                + p.getUsuarioID().getTelefono() + ";"
                                                + p.getUsuarioID().getEmail() + ";"
                                                + p.getSexo() + ";"
                                                + Edad.calcularEdad(formato.format(p.getFechanac())) + ";"
                                                + formato.format(solicitud.getFechaSolicitud()) + "\n";
                                    }
                                }
                                
                                return salida;
                            }
                        }
                        break;
                }
            }
        }
        
        return "fail";
    }

    /**
     * Operacion Web Service para buscar medicos por nombres y/o apellidos o cedula
     */
    @WebMethod(operationName = "buscarMedico")
    public String buscarMedico(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "tipoBusqueda") int tipoBusqueda, @WebParam(name = "criterio") String criterio) {
        
        // tipoBusqueda:
        // 0 -> por nombre o apellido
        // 1 -> por nuemero de cedula
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)){
                
                switch(tipoBusqueda){
                    
                    case 0:
                        List<Medico> medicos = ejbMedico.findAll();
                        
                        String salida = "";

                        for(Medico medico : medicos){

                            // Usar expresiones regulares para mejorar la busqueda
                            if(medico.getUsuarioID().getNombres().contains(criterio)
                                    || medico.getApellidos().contains(criterio)){

                                salida += medico.getCedulaMedico() + ";"
                                        + medico.getUsuarioID().getNombres() + ";"
                                        + medico.getApellidos() + ";"
                                        + medico.getUsuarioID().getTelefono() + ";"
                                        + medico.getUsuarioID().getEmail() + ";"
                                        + medico.getSexo() + "\n";
                            }
                        }

                        return salida;
//                        break;
                        
                    case 1:
                        Medico medico = ejbMedico.find(Integer.parseInt(criterio));
                        
                        if(medico == null){
                            return "fail";
                        }
                        
                        String salida2 = medico.getCedulaMedico() + ";"
                                + medico.getUsuarioID().getNombres() + ";"
                                + medico.getApellidos() + ";"
                                + medico.getUsuarioID().getTelefono() + ";"
                                + medico.getUsuarioID().getEmail() + ";"
                                + medico.getSexo();
                        
                        return salida2;
//                        break;
                }
                
            }
        }
        
        return "fail";
    }

    /**
     * Operacion para buscar consultorios por nombre
     */
    @WebMethod(operationName = "buscarConsultorio")
    public String buscarConsultorio(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "nombre") String nombre) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)){
                
                List<Consultorio> consultorios = ejbConsultorio.findAll();
                
                String salida = "";
                
                for(Consultorio consultorio : consultorios){
                    
                    // Usar expresiones regulares para mejorar la busqueda
                    if(consultorio.getUsuarioID().getNombres().contains(nombre)){
                        
                        salida += consultorio.getIdconsultorio() + ";"
                                + consultorio.getUsuarioID().getNombres() + ";"
                                + consultorio.getUsuarioID().getDireccion() + ";"
                                + consultorio.getUsuarioID().getTelefono() + "\n";
                    }
                }
                
                return salida;
            }
        }
        
        return null;
    }

    /**
     * Metodo para validar si un usuario existe o no
     */
    @WebMethod(operationName = "validarUsuario")
    public String validarUsuario(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "roll") int roll) {
        
        // roll:
        // 0 -> paciente
        // 1 -> medico
        // 2 -> consultorio
        
        String rollUsuario;
        
        switch(roll){
            case 0:
                rollUsuario = "paciente";
                break;
            case 1:
                rollUsuario = "medico";
                break;
            case 2:
                rollUsuario = "consultorio";
                break;
            default:
                return "none";
        }
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && usuario.getRoll().equals(rollUsuario)){
                
                return usuario.getNombres();
            }
        }
        
        return "none";
    }

    /**
     * Operacion que permite al peciente enviarle solicitud al medico o consultorio
     */
    @WebMethod(operationName = "hacerSolicitud")
    public String hacerSolicitud(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "entidad") int entidad, @WebParam(name = "id") int id) {
        
        // entidad:
        // 0 -> al medico
        // 1 -> al consultorio
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && usuario.getRoll().equals("paciente")){
                
                switch(entidad){
                    
                    case 0:
                        Medico medico = ejbMedico.find(id);
                        
                        if(medico != null){
                            
                            List<Paciente> pacientes = usuario.getPacienteList();
                            
                            for(Paciente paciente : pacientes){
                                
                                if(paciente.getUsuarioID().getId() == usuario.getId()){
                                    
                                    SolicitudMedico solicitud = new SolicitudMedico(medico.getCedulaMedico(),
                                            paciente.getPacientePK().getNumid(), paciente.getPacientePK().getTipoid());
                                    
//                                    solicitud.setMedico(medico);
//                                    solicitud.setPaciente(paciente);
                                    solicitud.setEstado("pendiente");
                                    solicitud.setFechaSolicitud(Calendar.getInstance().getTime());
                                    
                                    ejbSolicitudMedico.create(solicitud);
                                    
                                    return "ok";
                                }
                            }
                        }
                        
                        break;
                        
                    case 1:
                        Consultorio consultorio = ejbConsultorio.find(id);
                        
                        if(consultorio != null){
                            
                            List<Paciente> pacientes = usuario.getPacienteList();
                            
                            for(Paciente paciente : pacientes){
                                
                                if(paciente.getUsuarioID().getId() == usuario.getId()){
                                    
                                    SolicitudConsultorio solicitud = new SolicitudConsultorio(consultorio.getIdconsultorio(),
                                            paciente.getPacientePK().getNumid(), paciente.getPacientePK().getTipoid());
                                    
//                                    solicitud.setConsultorio(consultorio);
//                                    solicitud.setPaciente(paciente);
                                    solicitud.setEstado("pendiente");
                                    solicitud.setFechaSolicitud(Calendar.getInstance().getTime());
                                    
                                    ejbSolicitudConsultorio.create(solicitud);
                                    
                                    return "ok";
                                }
                            }
                        }
                        break;
                }
            }
        }
        
        return "fail";
    }

    /**
     * Operacion para obtener el historial de un paciente
     */
    @WebMethod(operationName = "obtenerHistorial")
    public String obtenerHistorial(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "modo") int modo, @WebParam(name = "pacienteTipoId") String pacienteTipoId,
            @WebParam(name = "pacienteNumId") String pacienteNumId) {
        
        // modo:
        //  0 -> un paciente consulta su historial
        //  1 -> un medico consulta el historial de un paciente
        //  2 -> un consultorio obtiene el historial de una paciente
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)){
                
                switch(modo){
                    case 0:
                        for(Paciente paciente : usuario.getPacienteList()){
                            
                            if(paciente.getUsuarioID().getId() == usuario.getId()){
                                
                                XmlHl7 historial = new XmlHl7();
                                
                                historial.setMunicipioPaciente(paciente.getUsuarioID().getMunicipioIdmunicipio().getNombre());
                                historial.setDepartamentoPaciente(paciente.getUsuarioID().getMunicipioIdmunicipio()
                                        .getDepartamentoIddepartamento().getNombre());
                                
                                String nombres = paciente.getUsuarioID().getNombres();
                                
                                int pos = nombres.indexOf(" ");
                                
                                if(pos == -1){
                                    
                                    historial.setNombre1Paciente(nombres);
                                    historial.setNombre2Paciente("");
                                } else {
                                    
                                    String[] temp = nombres.split(" ");
                                    historial.setNombre1Paciente(temp[0]);
                                    historial.setNombre2Paciente(temp[1]);
                                }
                                
                                String apellidos = paciente.getApellidos();
                                
                                int pos2 = apellidos.indexOf(" ");
                                
                                if(pos2 == -1){
                                    
                                    historial.setApellido1Paciente(apellidos);
                                    historial.setApellido2Paciente("");
                                } else {
                                    
                                    String[] temp2 = apellidos.split(" ");
                                    historial.setApellido1Paciente(temp2[0]);
                                    historial.setApellido2Paciente(temp2[1]);
                                }
                                
                                String codGenero = "" + paciente.getSexo().charAt(0);
                                
                                codGenero = codGenero.toUpperCase();
                                
                                if(codGenero.equals("F") || codGenero.equals("M")){
                                    historial.setCodigoGeneroPaciente(codGenero);
                                } else {
                                    historial.setCodigoGeneroPaciente("ND");
                                }
                                
                                historial.setGeneroPaciente(paciente.getSexo());
                                
                                boolean encontroConsultorio = false;
                                
                                historial.setTelefonoConsultorio("");
                                historial.setMunicipioConsultorio("");
                                historial.setDepartamentoConsultorio("");
                                
                                for(Atiende atiende : paciente.getAtiendeList()){
                                    
                                    if(atiende.getPaciente().getPacientePK().getNumid() == paciente.getPacientePK().getNumid()
                                            && atiende.getPaciente().getPacientePK().getTipoid().equals(paciente
                                                    .getPacientePK().getTipoid())){
                                        
                                        Consultorio consultorio = null;
                                        
                                        if(!atiende.getMedico().getConsultorioList().isEmpty()){
                                            
                                            consultorio = atiende.getMedico().getConsultorioList().get(0);
                                        }
                                        
                                        if(consultorio != null){
                                            
                                            historial.setTelefonoConsultorio("" + consultorio.getUsuarioID().getTelefono());
                                            historial.setMunicipioConsultorio(consultorio.getUsuarioID()
                                                    .getMunicipioIdmunicipio().getNombre());
                                            historial.setDepartamentoConsultorio(consultorio.getUsuarioID()
                                                    .getMunicipioIdmunicipio().getDepartamentoIddepartamento().getNombre());
                                            
                                            
                                            encontroConsultorio = true;
                                            break;
                                        }
                                    }
                                }
                                
                                if(!encontroConsultorio){
                                    
                                    for(SolicitudConsultorio solicitud : paciente.getSolicitudConsultorioList()){
                                        
                                        if(solicitud.getPaciente().getPacientePK().getNumid() == paciente.getPacientePK().getNumid()
                                            && solicitud.getPaciente().getPacientePK().getTipoid().equals(paciente
                                                    .getPacientePK().getTipoid()) && solicitud.getEstado().equals("aprobado")){
                                            
                                            Usuario usuarioConsul = solicitud.getConsultorio().getUsuarioID();
                                            
                                            historial.setTelefonoConsultorio("" + usuarioConsul.getTelefono());
                                            historial.setMunicipioConsultorio(usuarioConsul.getMunicipioIdmunicipio()
                                                    .getNombre());
                                            historial.setDepartamentoConsultorio(usuarioConsul.getMunicipioIdmunicipio()
                                                    .getDepartamentoIddepartamento().getNombre());
                                            
                                            break;
                                        }
                                    }
                                }
                                
                                ArrayList<String[]> datosHistorial = new ArrayList<>();
                                
                                for(Chequeo chequeo : paciente.getChequeoList()){
                                    
                                    String[] temp = new String[6];
                                    
                                    temp[0] = chequeo.getTipochequeo();
                                    temp[1] = chequeo.getValor();
                                    temp[2] = chequeo.getUnidades();
                                    temp[3] = "";
                                    
                                    for(Tip tip : ejbTip.findAll()){
                                        
                                        String tipo = temp[0].equals("arritmia") ? "ritmo_cardiaco" : temp[0];
                                        
                                        if(tip.getTipochequeo().equals(tipo) && tip.getEstado().equals("normal")){
                                            
                                            temp[3] = tip.getMin() + " - " + tip.getMax();
                                            break;
                                        }
                                    }
//                                    temp[3] = chequeo
                                    temp[4] = chequeo.getTipIdtip().getEstado();
                                    temp[5] = chequeo.getTipIdtip().getDescripcion();
                                    
                                    datosHistorial.add(temp);
                                }
                                
                                historial.setDatosHistorial(datosHistorial);
                                
                                historial.procesarXmlHl7();
                                
                                return historial.getXmlInString();
                            }
                        }
                        break;
                    case 1:
                        Paciente paciente = ejbPaciente.find(new PacientePK(Integer.parseInt(pacienteNumId), pacienteTipoId));
                        
                        if(paciente == null){
                            return "fail";
                        }
                        
                        // encontramos al paciete
                        XmlHl7 historial = new XmlHl7();

                        historial.setMunicipioPaciente(paciente.getUsuarioID().getMunicipioIdmunicipio().getNombre());
                        historial.setDepartamentoPaciente(paciente.getUsuarioID().getMunicipioIdmunicipio()
                                .getDepartamentoIddepartamento().getNombre());

                        String nombres = paciente.getUsuarioID().getNombres();

                        int pos = nombres.indexOf(" ");

                        if (pos == -1) {

                            historial.setNombre1Paciente(nombres);
                            historial.setNombre2Paciente("");
                        } else {

                            String[] temp = nombres.split(" ");
                            historial.setNombre1Paciente(temp[0]);
                            historial.setNombre2Paciente(temp[1]);
                        }

                        String apellidos = paciente.getApellidos();

                        int pos2 = apellidos.indexOf(" ");

                        if (pos2 == -1) {

                            historial.setApellido1Paciente(apellidos);
                            historial.setApellido2Paciente("");
                        } else {

                            String[] temp2 = apellidos.split(" ");
                            historial.setApellido1Paciente(temp2[0]);
                            historial.setApellido2Paciente(temp2[1]);
                        }

                        String codGenero = "" + paciente.getSexo().charAt(0);

                        codGenero = codGenero.toUpperCase();

                        if (codGenero.equals("F") || codGenero.equals("M")) {
                            historial.setCodigoGeneroPaciente(codGenero);
                        } else {
                            historial.setCodigoGeneroPaciente("ND");
                        }

                        historial.setGeneroPaciente(paciente.getSexo());

                        boolean encontroConsultorio = false;

                        historial.setTelefonoConsultorio("");
                        historial.setMunicipioConsultorio("");
                        historial.setDepartamentoConsultorio("");

                        for (Atiende atiende : paciente.getAtiendeList()) {

                            if (atiende.getPaciente().getPacientePK().getNumid() == paciente.getPacientePK().getNumid()
                                    && atiende.getPaciente().getPacientePK().getTipoid().equals(paciente
                                            .getPacientePK().getTipoid())) {

                                Consultorio consultorio = null;

                                if (!atiende.getMedico().getConsultorioList().isEmpty()) {

                                    consultorio = atiende.getMedico().getConsultorioList().get(0);
                                }

                                if (consultorio != null) {

                                    historial.setTelefonoConsultorio("" + consultorio.getUsuarioID().getTelefono());
                                    historial.setMunicipioConsultorio(consultorio.getUsuarioID()
                                            .getMunicipioIdmunicipio().getNombre());
                                    historial.setDepartamentoConsultorio(consultorio.getUsuarioID()
                                            .getMunicipioIdmunicipio().getDepartamentoIddepartamento().getNombre());

                                    encontroConsultorio = true;
                                    break;
                                }
                            }
                        }

                        if (!encontroConsultorio) {

                            for (SolicitudConsultorio solicitud : paciente.getSolicitudConsultorioList()) {

                                if (solicitud.getPaciente().getPacientePK().getNumid() == paciente.getPacientePK().getNumid()
                                        && solicitud.getPaciente().getPacientePK().getTipoid().equals(paciente
                                                .getPacientePK().getTipoid()) && solicitud.getEstado().equals("aprobado")) {

                                    Usuario usuarioConsul = solicitud.getConsultorio().getUsuarioID();

                                    historial.setTelefonoConsultorio("" + usuarioConsul.getTelefono());
                                    historial.setMunicipioConsultorio(usuarioConsul.getMunicipioIdmunicipio()
                                            .getNombre());
                                    historial.setDepartamentoConsultorio(usuarioConsul.getMunicipioIdmunicipio()
                                            .getDepartamentoIddepartamento().getNombre());

                                    break;
                                }
                            }
                        }

                        ArrayList<String[]> datosHistorial = new ArrayList<>();

                        for (Chequeo chequeo : paciente.getChequeoList()) {

                            String[] temp = new String[6];

                            temp[0] = chequeo.getTipochequeo();
                            temp[1] = chequeo.getValor();
                            temp[2] = chequeo.getUnidades();
                            temp[3] = "";

                            for (Tip tip : ejbTip.findAll()) {

                                String tipo = temp[0].equals("arritmia") ? "ritmo_cardiaco" : temp[0];

                                if (tip.getTipochequeo().equals(tipo) && tip.getEstado().equals("normal")) {

                                    temp[3] = tip.getMin() + " - " + tip.getMax();
                                    break;
                                }
                            }
//                                    temp[3] = chequeo
                            temp[4] = chequeo.getTipIdtip().getEstado();
                            temp[5] = chequeo.getTipIdtip().getDescripcion();

                            datosHistorial.add(temp);
                        }

                        historial.setDatosHistorial(datosHistorial);

                        historial.procesarXmlHl7();

                        return historial.getXmlInString();
                        //break;
                    case 2:
                        Paciente paciente2 = ejbPaciente.find(new PacientePK(Integer.parseInt(pacienteNumId), pacienteTipoId));
                        
                        if(paciente2 == null){
                            return "fail";
                        }
                        
                        XmlHl7 historial2 = new XmlHl7();

                        historial2.setMunicipioPaciente(paciente2.getUsuarioID().getMunicipioIdmunicipio().getNombre());
                        historial2.setDepartamentoPaciente(paciente2.getUsuarioID().getMunicipioIdmunicipio()
                                .getDepartamentoIddepartamento().getNombre());

                        String nombres2 = paciente2.getUsuarioID().getNombres();

                        int pos3 = nombres2.indexOf(" ");

                        if (pos3 == -1) {

                            historial2.setNombre1Paciente(nombres2);
                            historial2.setNombre2Paciente("");
                        } else {

                            String[] temp = nombres2.split(" ");
                            historial2.setNombre1Paciente(temp[0]);
                            historial2.setNombre2Paciente(temp[1]);
                        }

                        String apellidos2 = paciente2.getApellidos();

                        int pos4 = apellidos2.indexOf(" ");

                        if (pos4 == -1) {

                            historial2.setApellido1Paciente(apellidos2);
                            historial2.setApellido2Paciente("");
                        } else {

                            String[] temp2 = apellidos2.split(" ");
                            historial2.setApellido1Paciente(temp2[0]);
                            historial2.setApellido2Paciente(temp2[1]);
                        }

                        String codGenero2 = "" + paciente2.getSexo().charAt(0);

                        codGenero2 = codGenero2.toUpperCase();

                        if (codGenero2.equals("F") || codGenero2.equals("M")) {
                            historial2.setCodigoGeneroPaciente(codGenero2);
                        } else {
                            historial2.setCodigoGeneroPaciente("ND");
                        }

                        historial2.setGeneroPaciente(paciente2.getSexo());

                        boolean encontroConsultorio2 = false;

                        historial2.setTelefonoConsultorio("");
                        historial2.setMunicipioConsultorio("");
                        historial2.setDepartamentoConsultorio("");

                        for (Atiende atiende : paciente2.getAtiendeList()) {

                            if (atiende.getPaciente().getPacientePK().getNumid() == paciente2.getPacientePK().getNumid()
                                    && atiende.getPaciente().getPacientePK().getTipoid().equals(paciente2
                                            .getPacientePK().getTipoid())) {

                                Consultorio consultorio = null;

                                if (!atiende.getMedico().getConsultorioList().isEmpty()) {

                                    consultorio = atiende.getMedico().getConsultorioList().get(0);
                                }

                                if (consultorio != null) {

                                    historial2.setTelefonoConsultorio("" + consultorio.getUsuarioID().getTelefono());
                                    historial2.setMunicipioConsultorio(consultorio.getUsuarioID()
                                            .getMunicipioIdmunicipio().getNombre());
                                    historial2.setDepartamentoConsultorio(consultorio.getUsuarioID()
                                            .getMunicipioIdmunicipio().getDepartamentoIddepartamento().getNombre());

                                    encontroConsultorio2 = true;
                                    break;
                                }
                            }
                        }

                        if (!encontroConsultorio2) {

                            for (SolicitudConsultorio solicitud : paciente2.getSolicitudConsultorioList()) {

                                if (solicitud.getPaciente().getPacientePK().getNumid() == paciente2.getPacientePK().getNumid()
                                        && solicitud.getPaciente().getPacientePK().getTipoid().equals(paciente2
                                                .getPacientePK().getTipoid()) && solicitud.getEstado().equals("aprobado")) {

                                    Usuario usuarioConsul = solicitud.getConsultorio().getUsuarioID();

                                    historial2.setTelefonoConsultorio("" + usuarioConsul.getTelefono());
                                    historial2.setMunicipioConsultorio(usuarioConsul.getMunicipioIdmunicipio()
                                            .getNombre());
                                    historial2.setDepartamentoConsultorio(usuarioConsul.getMunicipioIdmunicipio()
                                            .getDepartamentoIddepartamento().getNombre());

                                    break;
                                }
                            }
                        }

                        ArrayList<String[]> datosHistorial2 = new ArrayList<>();

                        for (Chequeo chequeo : paciente2.getChequeoList()) {

                            String[] temp = new String[6];

                            temp[0] = chequeo.getTipochequeo();
                            temp[1] = chequeo.getValor();
                            temp[2] = chequeo.getUnidades();
                            temp[3] = "";

                            for (Tip tip : ejbTip.findAll()) {

                                String tipo = temp[0].equals("arritmia") ? "ritmo_cardiaco" : temp[0];

                                if (tip.getTipochequeo().equals(tipo) && tip.getEstado().equals("normal")) {

                                    temp[3] = tip.getMin() + " - " + tip.getMax();
                                    break;
                                }
                            }
//                                    temp[3] = chequeo
                            temp[4] = chequeo.getTipIdtip().getEstado();
                            temp[5] = chequeo.getTipIdtip().getDescripcion();

                            datosHistorial2.add(temp);
                        }

                        historial2.setDatosHistorial(datosHistorial2);

                        historial2.procesarXmlHl7();

                        return historial2.getXmlInString();
                        //break;
                }
            }
        }
        
        return "fail";
    }

    /**
     * Operacion para que un paciente obtenga los diagnosticos hechos por su medico
     */
    @WebMethod(operationName = "obtenerDiagnosticos")
    public String obtenerDiagnosticos(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && usuario.getRoll().equals("paciente")){
                
                List<Paciente> pacientes = usuario.getPacienteList();
                
                for(Paciente paciente : pacientes){
                    
                    if(paciente.getUsuarioID().getId() == usuario.getId()){
                        
                        List<Diagnostico> diagnosticos = ejbDiagnostico.findAll();
                        
                        String salida = "";
                        
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
                        
                        for(Diagnostico diagnostico : diagnosticos){
                            
                            PacientePK cheqPacientePK = diagnostico.getAntecedenteIdantecedente().getPaciente().getPacientePK();
                            
                            // comparar si diagnostico ha sido visto o no
                            if(cheqPacientePK.getNumid() == paciente.getPacientePK().getNumid()
                                    && cheqPacientePK.getTipoid().equals(paciente.getPacientePK().getTipoid())){
                                
                                salida += diagnostico.getIddiagnostico() + ";"
                                        + formatoFecha.format(diagnostico.getFecha()) + ";"
                                        + formatoHora.format(diagnostico.getHora()) + ";"
                                        + diagnostico.getContenido() + "\n";
                            }
                        }
                        
                        return salida;
                    }
                }
            }
        }
        
        return "none";
    }

    /**
     * Operacion mediante la cual un medico obtiene una lista de los antecedentes de todos sus pacientes
     */
    @WebMethod(operationName = "obtenerAlertas")
    public String obtenerAlertas(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && usuario.getRoll().equals("medico")){
                
                List<Medico> medicos = usuario.getMedicoList();
                
                for(Medico medico : medicos){
                    
                    if(medico.getUsuarioID().getId() == usuario.getId()){
                        
                        List<Atiende> atenciones = ejbAtiende.findAll();
                        
                        List<Paciente> misPacientes = new ArrayList<>();
                        
                        for(Atiende atencion : atenciones){
                            
                            if(atencion.getMedico().getCedulaMedico() == medico.getCedulaMedico()){
                                
                                misPacientes.add(atencion.getPaciente());
                            }
                        }
                        
                        if(misPacientes.isEmpty()){
                            return "fail";
                        }
                        
                        String salida = "";
                        
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
                        
                        List<Antecedente> antecedentes = ejbAntecedente.findAll();
                        
                        for(Paciente paciente : misPacientes){
                            
                            for(Antecedente antecedente : antecedentes){
                                
                                Paciente temp = antecedente.getPaciente();
                                if(temp.getPacientePK().getNumid() == paciente.getPacientePK().getNumid() &&
                                        temp.getPacientePK().getTipoid().equals(paciente.getPacientePK().getTipoid())){
                                    
                                    Chequeo c = antecedente.getChequeoIdchequeo();
                                    
                                    salida += antecedente.getIdantecedente() + ";"
                                            + temp.getPacientePK().getTipoid() + ";"
                                            + temp.getPacientePK().getNumid() + ";"
                                            + temp.getUsuarioID().getNombres() + " "
                                            + temp.getApellidos() + ";"
                                            + formatoFecha.format(c.getFecha()) + ";"
                                            + formatoHora.format(c.getHora()) + ";"
                                            + c.getTipochequeo() + ";"
                                            + c.getValor() + ";"
                                            + c.getUnidades() + ";"
                                            + c.getTipIdtip().getEstado() + ";"
                                            + c.getLatitud() + ";"
                                            + c.getLongitud() + "\n";
                                }
                            }
                        }
                        
                        return salida;
                    }
                }
                
            }
        }
        
        return "fail";
    }
    
    /**
     * Operacion mediante la cual un medico obtiene una lista de los antecedentes que no haya visto de todos sus pacientes
     */
    @WebMethod(operationName = "obtenerAlertasNoVistas")
    public String obtenerAlertasNoVistas(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && usuario.getRoll().equals("medico")){
                
                List<Medico> medicos = usuario.getMedicoList();
                
                for(Medico medico : medicos){
                    
                    if(medico.getUsuarioID().getId() == usuario.getId()){
                        
                        List<Atiende> atenciones = ejbAtiende.findAll();
                        
                        List<Paciente> misPacientes = new ArrayList<>();
                        
                        for(Atiende atencion : atenciones){
                            
                            if(atencion.getMedico().getCedulaMedico() == medico.getCedulaMedico()){
                                
                                misPacientes.add(atencion.getPaciente());
                            }
                        }
                        
                        if(misPacientes.isEmpty()){
                            return "fail";
                        }
                        
                        String salida = "";
                        
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
                        
                        List<Antecedente> antecedentes = ejbAntecedente.findAll();
                        
                        for(Paciente paciente : misPacientes){
                            
                            for(Antecedente antecedente : antecedentes){
                                
                                Paciente temp = antecedente.getPaciente();
                                
                                if(temp.getPacientePK().getNumid() == paciente.getPacientePK().getNumid() &&
                                        temp.getPacientePK().getTipoid().equals(paciente.getPacientePK().getTipoid())
                                        && antecedente.getVista() == Boolean.FALSE){
                                    
                                    Chequeo c = antecedente.getChequeoIdchequeo();
                                    
                                    salida += antecedente.getIdantecedente() + ";"
                                            + temp.getPacientePK().getTipoid() + ";"
                                            + temp.getPacientePK().getNumid() + ";"
                                            + temp.getUsuarioID().getNombres() + " "
                                            + temp.getApellidos() + ";"
                                            + formatoFecha.format(c.getFecha()) + ";"
                                            + formatoHora.format(c.getHora()) + ";"
                                            + c.getTipochequeo() + ";"
                                            + c.getValor() + ";"
                                            + c.getUnidades() + ";"
                                            + c.getTipIdtip().getEstado() + ";"
                                            + c.getLatitud() + ";"
                                            + c.getLongitud() + "\n";
                                }
                            }
                        }
                        
                        return salida;
                    }
                }
                
            }
        }
        
        return "fail";
    }
    
    /**
     * Metodo a traves del cual se establece una alerta como vista
     * @param correo
     * @param clave
     * @param idAntecedente
     * @return 
     */
    @WebMethod(operationName = "alertaVista")
    public String alertaVista(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "idAntecedente") int idAntecedente){
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && usuario.getRoll().equals("medico")){
                
                Antecedente antecedente = ejbAntecedente.find(idAntecedente);
                
                if(antecedente == null){
                    return "fail";
                }
                
                antecedente.setVista(Boolean.TRUE);
                
                ejbAntecedente.edit(antecedente);
                
                return "ok";
            }
        }
        
        return "fail";
    }
     
    /**
     * Operacion mediante la cual un medico obtiene una lista de los antecedentes de un paciente determinado
     */
    @WebMethod(operationName = "obtenerAlertasPaciente")
    public String obtenerAlertasPaciente(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "tipoId") String tipoId, @WebParam(name = "numeroId") String numeroId) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && usuario.getRoll().equals("medico")){
                
                List<Medico> medicos = usuario.getMedicoList();
                
                for(Medico medico : medicos){
                    
                    if(medico.getUsuarioID().getId() == usuario.getId()){
                        
                        //List<MedicoPaciente> atenciones = ejbAtiende.findAll();
                        
                        Paciente paciente = ejbPaciente.find(new PacientePK(Integer.parseInt(numeroId), tipoId));
                        
                        if(paciente == null){
                            return "";
                        }
                        
//                        if(misPacientes.isEmpty()){
//                            return "";
//                        }
                        
                        String salida = "";
                        
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
                        
                        List<Antecedente> antecedentes = ejbAntecedente.findAll();
                            
                        for(Antecedente antecedente : antecedentes){

                            Paciente temp = antecedente.getPaciente();
                            
                            if(temp.getPacientePK().getNumid() == paciente.getPacientePK().getNumid() &&
                                    temp.getPacientePK().getTipoid().equals(paciente.getPacientePK().getTipoid())){

                                Chequeo c = antecedente.getChequeoIdchequeo();

                                salida += antecedente.getIdantecedente() + ";"
                                        + temp.getPacientePK().getTipoid() + ";"
                                        + temp.getPacientePK().getNumid() + ";"
                                        + temp.getUsuarioID().getNombres() + " "
                                        + temp.getApellidos() + ";"
                                        + formatoFecha.format(c.getFecha()) + ";"
                                        + formatoHora.format(c.getHora()) + ";"
                                        + c.getTipochequeo() + ";"
                                        + c.getValor() + ";"
                                        + c.getUnidades() + ";"
                                        + c.getTipIdtip().getEstado() + ";"
                                        + c.getLatitud() + ";"
                                        + c.getLongitud() + "\n";;
                            }
                        }
                        
                        return salida;
                    }
                }
                
            }
        }
        
        return "";
    }

    /**
     * Operacion mediante la cula un medico le hace un diagnostico a un paciente a traves de sus alertas
     */
    @WebMethod(operationName = "hacerDiagnostico")
    public String hacerDiagnostico(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "idAntecedente") String idAntecedente, @WebParam(name = "contenido") String contenido) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && usuario.getRoll().equals("medico")){
                
                List<Medico> medicos = ejbMedico.findAll();
                
                for(Medico medico :  medicos){
                    
                    if(medico.getUsuarioID().getId() == usuario.getId()){
                        
                        Antecedente antecedente = ejbAntecedente.find(Integer.parseInt(idAntecedente));
                        
                        contenido = contenido.replaceAll("\n", " ");
                        contenido  = contenido.trim();
                        
                        Diagnostico diagnostico = new Diagnostico();
                        
                        diagnostico.setFecha(Calendar.getInstance().getTime());
                        diagnostico.setHora(Calendar.getInstance().getTime());
                        diagnostico.setContenido(contenido);
                        diagnostico.setMedicoCedulaMedico(medico);
                        diagnostico.setAntecedenteIdantecedente(antecedente);
                        
                        ejbDiagnostico.create(diagnostico);
                        
                        String regGcm = antecedente.getPaciente().getUsuarioID().getGcmRegId();
                        
                        if(regGcm != null && !regGcm.equals("")){
                            
                            GCM.send("diagnostico", regGcm,
                                    contenido.substring(0, (contenido.length() < 20 ? contenido.length() : 20)));
                        }
                        
                        return "ok";
                    }
                }
            }
        }
        
        return "fail";
    }

    /**
     * Operacion mediante la cual un medico o consultorio puede aprobar la solicitud hecha por un paciente
     */
    @WebMethod(operationName = "aprobarSolicitud")
    public String aprobarSolicitud(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "tipoId") String tipoId, @WebParam(name = "numeroId") String numeroId) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && (usuario.getRoll().equals("medico") || usuario.getRoll().equals("consultorio"))){
                
                switch (usuario.getRoll()) {
                    
                    case "medico":
                        
                        List<Medico> medicos = usuario.getMedicoList();
                        
                        for(Medico medico : medicos){
                            
                            if(medico.getUsuarioID().getId() == usuario.getId()){
                                
                                SolicitudMedicoPK key = new SolicitudMedicoPK(medico.getCedulaMedico(),
                                        Integer.parseInt(numeroId), tipoId);
                                
                                SolicitudMedico solicitud = ejbSolicitudMedico.find(key);
                                
                                if(solicitud == null){
                                    return "fail";
                                }
                                
                                solicitud.setEstado("aprobado");
                                solicitud.setFechaAprobacionSolicitud(Calendar.getInstance().getTime());
                                
                                ejbSolicitudMedico.edit(solicitud);
                                
                                Paciente paciente = ejbPaciente.find(new PacientePK(Integer.parseInt(numeroId), tipoId));
                                
                                if(paciente == null){
                                    return "fail";
                                }
                                
                                Atiende atiende = new Atiende(medico.getCedulaMedico(), paciente.getPacientePK().getNumid(),
                                        paciente.getPacientePK().getTipoid());
//                                atiende.setMedico(medico);
//                                atiende.setPaciente(paciente);
                                atiende.setFechaInicioAtencion(Calendar.getInstance().getTime());
                                
                                ejbAtiende.create(atiende);
                                
                                return "ok";
                            }
                        }
                        
                        break;
                        
                    case "consultorio":
                        List<Consultorio> consultorios = usuario.getConsultorioList();
                        
                        for(Consultorio consultorio : consultorios){
                            
                            if(consultorio.getUsuarioID().getId() == usuario.getId()){
                                
                                SolicitudConsultorioPK key2 = new SolicitudConsultorioPK(consultorio.getIdconsultorio(),
                                        Integer.parseInt(numeroId), tipoId);
                                
                                SolicitudConsultorio solicitud2 = ejbSolicitudConsultorio.find(key2);
                                
                                if(solicitud2 == null){
                                    return "fail";
                                }
                                
                                solicitud2.setEstado("aprobado");
                                solicitud2.setFechaAprobacionSolicitud(Calendar.getInstance().getTime());
                                
                                ejbSolicitudConsultorio.edit(solicitud2);
                                
                                return "ok";
                            }
                        }
                        
                        break;
                }
            }
        }
        
        return "fail";
    }
    
    /**
     * Operacion mediante la cual un consultorio puede relacionar un paciente relacionado con el conultorio a un medico
     * de dicho consultorio
     */
    @WebMethod(operationName = "aprobarSolicitudCaM")
    public String aprobarSolicitudCaM(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "tipoId") String tipoId, @WebParam(name = "numeroId") String numeroId,
            @WebParam(name = "cedulaMedico") int cedulaMedico){
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && usuario.getRoll().equals("consultorio")){
                
                for(Consultorio consultorio : usuario.getConsultorioList()){
                    
                    if(consultorio.getUsuarioID().getId() == usuario.getId()){
                        
                        PacientePK ppk = new PacientePK(Integer.parseInt(numeroId), tipoId);
                        
                        Paciente paciente = ejbPaciente.find(ppk);
                        
                        if(paciente == null){
                            return "fail";
                        }
                        
                        Medico medico = ejbMedico.find(cedulaMedico);
                        
                        if(medico == null){
                            return "fail";
                        }
                        
                        if(medico.getConsultorioList().get(0).getIdconsultorio() == consultorio.getIdconsultorio()){
                            
                            Atiende atiende = new Atiende(medico.getCedulaMedico(), paciente.getPacientePK().getNumid(),
                                    paciente.getPacientePK().getTipoid());
                            
                            atiende.setFechaInicioAtencion(Calendar.getInstance().getTime());

                            ejbAtiende.create(atiende);
                            
                            return "ok";
                        } else {
                            return "fail";
                        }
                    }
                }
            }
        }
        
        return "fail";
    }
    
    /**
     * Operacion mediante la cual un medico o consultorio puede desaprobar la solicitud hecha por un paciente
     */
    @WebMethod(operationName = "rechazarSolicitud")
    public String rechazarSolicitud(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "tipoId") String tipoId, @WebParam(name = "numeroId") String numeroId) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && (usuario.getRoll().equals("medico") || usuario.getRoll().equals("consultorio"))){
                
                switch (usuario.getRoll()) {
                    
                    case "medico":
                        
                        List<Medico> medicos = usuario.getMedicoList();
                        
                        for(Medico medico : medicos){
                            
                            if(medico.getUsuarioID().getId() == usuario.getId()){
                                
                                SolicitudMedicoPK key = new SolicitudMedicoPK(medico.getCedulaMedico(),
                                        Integer.parseInt(numeroId), tipoId);
                                
                                SolicitudMedico solicitud = ejbSolicitudMedico.find(key);
                                
                                if(solicitud == null){
                                    return "fail";
                                }
                                
                                solicitud.setEstado("desaprobado");
                                solicitud.setFechaAprobacionSolicitud(Calendar.getInstance().getTime());
                                
                                ejbSolicitudMedico.edit(solicitud);
                                
//                                Paciente paciente = ejbPaciente.find(new PacientePK(Integer.parseInt(numeroId), tipoId));
//                                
//                                if(paciente == null){
//                                    return "fail";
//                                }
//                                
//                                Atiende atiende = new Atiende(medico.getCedulaMedico(), paciente.getPacientePK().getNumid(),
//                                        paciente.getPacientePK().getTipoid());
////                                atiende.setMedico(medico);
////                                atiende.setPaciente(paciente);
//                                atiende.setFechaInicioAtencion(Calendar.getInstance().getTime());
//                                
//                                ejbAtiende.create(atiende);
                                
                                return "ok";
                            }
                        }
                        
                        break;
                        
                    case "consultorio":
                        List<Consultorio> consultorios = usuario.getConsultorioList();
                        
                        for(Consultorio consultorio : consultorios){
                            
                            if(consultorio.getUsuarioID().getId() == usuario.getId()){
                                
                                SolicitudConsultorioPK key2 = new SolicitudConsultorioPK(consultorio.getIdconsultorio(),
                                        Integer.parseInt(numeroId), tipoId);
                                
                                SolicitudConsultorio solicitud2 = ejbSolicitudConsultorio.find(key2);
                                
                                if(solicitud2 == null){
                                    return "fail";
                                }
                                
                                solicitud2.setEstado("desaprobado");
                                solicitud2.setFechaAprobacionSolicitud(Calendar.getInstance().getTime());
                                
                                ejbSolicitudConsultorio.edit(solicitud2);
                                
                                return "ok";
                            }
                        }
                        
                        break;
                }
            }
        }
        
        return "fail";
    }

    /**
     * Operacion para registrar consultorio
     */
    @WebMethod(operationName = "registrarConsultorio")
    public String registrarConsultorio(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "nombre") String nombre, @WebParam(name = "direccion") String direccion,
            @WebParam(name = "telefono") String telefono, @WebParam(name = "idMunicipio") int idMunicipio) {
        
        Municipio municipio = ejbMunicipio.find(idMunicipio);
        
        if(municipio == null){
            return "fail";
        }
        
        Usuario usuario = new Usuario();
        
        usuario.setEmail(correo);
        usuario.setClave(clave);
        usuario.setNombres(nombre);
        usuario.setDireccion(direccion);
        usuario.setTelefono(telefono);
        usuario.setMunicipioIdmunicipio(municipio);
        usuario.setRoll("consultorio");
        
        Consultorio consultorio = new Consultorio();
        consultorio.setUsuarioID(usuario);
        
        ejbUsuario.create(usuario);
        ejbConsultorio.create(consultorio);
        
        return "ok";
    }

    /**
     * operacion para registrar paciente
     */
    @WebMethod(operationName = "registrarPaciente")
    public String registrarPaciente(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "tipoId") String tipoId, @WebParam(name = "numeroId") int numeroId,
            @WebParam(name = "nombres") String nombres, @WebParam(name = "apellidos") String apellidos,
            @WebParam(name = "sexo") String sexo, @WebParam(name = "fechaNacimiento") String fechaNacimiento,
            @WebParam(name = "direccion") String direccion, @WebParam(name = "telefono") String telefono,
            @WebParam(name = "idMunicipio") int idMunicipio) {
        
        Municipio municipio = ejbMunicipio.find(idMunicipio);
        
        if(municipio == null){
            return "fail";
        }
        
        Usuario usuario = new Usuario();
        
        usuario.setEmail(correo);
        usuario.setClave(clave);
        usuario.setNombres(nombres);
        usuario.setDireccion(direccion);
        usuario.setTelefono(telefono);
        usuario.setMunicipioIdmunicipio(municipio);
        usuario.setRoll("paciente");
        
        Date fNac;
        try {
            fNac = new SimpleDateFormat("yyyy-MM-dd").parse(fechaNacimiento);
        } catch (ParseException ex) {
            Logger.getLogger(SIMOP.class.getName()).log(Level.SEVERE, null, ex);
            return "fail";
        }
        
        Paciente paciente = new Paciente(numeroId, tipoId);
        
        paciente.setApellidos(apellidos);
        paciente.setSexo(sexo);
        paciente.setFechanac(fNac);
        paciente.setUsuarioID(usuario);
        
        ejbUsuario.create(usuario);
        ejbPaciente.create(paciente);
        
        return "ok";
    }

    /**
     * Operacion para obtener una lista de todos los departamentos
     */
    @WebMethod(operationName = "listaDepartamento")
    public String listaDepartamento() {
        
        List<Departamento> departamentos = ejbDepartamento.findAll();
        
        String salida = "";
        
        for(Departamento departamento : departamentos){
            
            salida += departamento.getIddepartamento() + ";"
                    + departamento.getNombre() + "\n";
        }
        
        return salida;
    }

    /**
     * Operacion medante la cual se obtiene una lista de todos los municipios de una departamento dado
     */
    @WebMethod(operationName = "listaMunicipios")
    public String listaMunicipios(@WebParam(name = "idDepartamento") int idDepartamento) {
        
        Departamento departamento = ejbDepartamento.find(idDepartamento);
        
        if(departamento == null){
            return "fail";
        }
        
        List<Municipio> municipios = departamento.getMunicipioList();
        
        String salida = "";
        
        for(Municipio municipio : municipios){
            
            if(municipio.getDepartamentoIddepartamento().getIddepartamento() == departamento.getIddepartamento()){
                
                salida += municipio.getIdmunicipio() + ";"
                        + municipio.getNombre() + "\n";
            }
        }
        
        return salida;
    }

    /**
     * Metodo para que cualquier usuario recupere su constraseña la cual sera enviada a su correo
     */
    @WebMethod(operationName = "recuperarClave")
    public String recuperarClave(@WebParam(name = "correo") String correo) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo)){
                
                SendMail sender = new SendMail();
        
                sender.send(usuario.getClave());
                
                return "ok";
            }
        }
        
        return "fail";
    }

    /**
     * Operacion para registrar medico
     */
    @WebMethod(operationName = "registrarMedico")
    public String registrarMedico(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "cedula") int cedula, @WebParam(name = "nombres") String nombres,
            @WebParam(name = "apellidos") String apellidos, @WebParam(name = "sexo") String sexo,
            @WebParam(name = "numeroTP") String numeroTP, @WebParam(name = "nacionalidad") String nacionalidad,
            @WebParam(name = "especializacion") String especializacion, @WebParam(name = "direccion") String direccion,
            @WebParam(name = "telefono") String telefono, @WebParam(name = "idMunicipio") int idMunicipio) {
        
        Municipio municipio = ejbMunicipio.find(idMunicipio);
        
        if(municipio == null){
            return "fail";
        }
        
        Usuario usuario = new Usuario();
        
        usuario.setEmail(correo);
        usuario.setClave(clave);
        usuario.setNombres(nombres);
        usuario.setDireccion(direccion);
        usuario.setTelefono(telefono);
        usuario.setMunicipioIdmunicipio(municipio);
        usuario.setRoll("medico");
        
        Medico medico = new Medico();
        
        medico.setCedulaMedico(cedula);
        medico.setApellidos(apellidos);
        medico.setSexo(sexo);
        medico.setNumTP(numeroTP);
        medico.setNummaxpacientes(30);
        medico.setNacionalidad(nacionalidad);
        medico.setUsuarioID(usuario);
        
        Especialidad esp = new Especialidad();
        esp.setNombre(especializacion);
        esp.setMedicoCedulaMedico(medico);
        
        ejbUsuario.create(usuario);
        ejbMedico.create(medico);
        ejbEspecialidad.create(esp);
        
        return "ok";
    }

    /**
     * Metodo para guardar el codigo de registro de GCM de un determidado usuario, sea paciente o medico
     */
    @WebMethod(operationName = "registrarGCM")
    public String registrarGCM(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "regGCM") String regGCM) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)){
                
                if(usuario.getRoll().equals("paciente") || usuario.getRoll().equals("medico")){
                    
                    usuario.setGcmRegId(regGCM);
                    
                    ejbUsuario.edit(usuario);
                    
                    return "ok";
                }
                
                return "fail";
            }
        }
        
        return "fail";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "probarMensajes")
    public String probarMensajes(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "mensaje") String mensaje) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)){
                
                if(usuario.getRoll().equals("paciente") || usuario.getRoll().equals("medico")){
                    
                    return GCM.send("prueba", usuario.getGcmRegId(), mensaje) ? "ok" : "fail";
                }
                
                return "fail";
            }
        }
        return "fail";
    }

    /**
     * Metodo mediante el cual se pueden obtener los datos de una cuenta dada
     */
    @WebMethod(operationName = "obtenerDatosCuenta")
    public String obtenerDatosCuenta(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)){
                
                switch(usuario.getRoll()){
                    
                    case "paciente":
                        for(Paciente paciente : usuario.getPacienteList()){
                            
                            if(paciente.getUsuarioID().getId() == usuario.getId()){
                                
                                SimpleDateFormat formato = new SimpleDateFormat("yyy-MM-dd");
                                
                                String salida = paciente.getPacientePK().getNumid() + ";"
                                        + paciente.getPacientePK().getTipoid() + ";"
                                        + paciente.getUsuarioID().getNombres() + ";"
                                        + paciente.getApellidos() + ";"
                                        + paciente.getSexo() + ";"
                                        + formato.format(paciente.getFechanac()) + ";"
                                        + paciente.getUsuarioID().getDireccion() + ";"
                                        + paciente.getUsuarioID().getTelefono() + ";"
                                        + paciente.getUsuarioID().getEmail() + ";"
                                        + paciente.getUsuarioID().getClave() + ";"
                                        + paciente.getUsuarioID().getMunicipioIdmunicipio().getDepartamentoIddepartamento()
                                                .getIddepartamento() + ";"
                                        + paciente.getUsuarioID().getMunicipioIdmunicipio().getIdmunicipio();
                                        
                                return salida;
                            }
                        }
                        break;
                    case "medico":
                        for(Medico medico : usuario.getMedicoList()){
                            
                            if(medico.getUsuarioID().getId() == usuario.getId()){
                                
                                String salida = medico.getCedulaMedico() + ";"
                                        + medico.getUsuarioID().getNombres() + ";"
                                        + medico.getApellidos() + ";"
                                        + medico.getSexo() + ";"
                                        + medico.getNumTP() + ";"
                                        + medico.getNacionalidad() + ";"
                                        + medico.getEspecialidadList().get(0).getNombre() + ";"
                                        + medico.getUsuarioID().getDireccion() + ";"
                                        + medico.getUsuarioID().getTelefono() + ";"
                                        + medico.getUsuarioID().getEmail() + ";"
                                        + medico.getUsuarioID().getClave() + ";"
                                        + medico.getUsuarioID().getMunicipioIdmunicipio().getDepartamentoIddepartamento()
                                                .getIddepartamento() + ";"
                                        + medico.getUsuarioID().getMunicipioIdmunicipio().getIdmunicipio();
                                        
                                return salida;
                            }
                        }
                        break;
                    case "consultorio":
                        for(Consultorio consultorio : usuario.getConsultorioList()){
                            
                            if(consultorio.getUsuarioID().getId() == usuario.getId()){
                                
                                String salida = consultorio.getIdconsultorio() + ";"
                                        + consultorio.getUsuarioID().getNombres() + ";"
                                        + consultorio.getUsuarioID().getDireccion() + ";"
                                        + consultorio.getUsuarioID().getTelefono() + ";"
                                        + consultorio.getUsuarioID().getEmail() + ";"
                                        + consultorio.getUsuarioID().getClave() + ";"
                                        + consultorio.getUsuarioID().getMunicipioIdmunicipio().getDepartamentoIddepartamento()
                                                .getIddepartamento() + ";"
                                        + consultorio.getUsuarioID().getMunicipioIdmunicipio().getIdmunicipio();
                                
                                return salida;
                            }
                        }
                        break;
                }
            }
        }
        
        return "fail";
    }

    /**
     * Metodo para obtener la lista de medicos de un consultorio
     */
    @WebMethod(operationName = "listaMedicoConsultorio")
    public String listaMedicoConsultorio(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave) {
        
        for (Usuario user : ejbUsuario.findAll()) {

            if (user.getEmail().equals(correo) && user.getClave().equals(clave) && user.getRoll().equals("consultorio")) {
                
                for(Consultorio consultorio : user.getConsultorioList()){
                    
                    if(consultorio.getUsuarioID().getId() == user.getId()){
                        
                        String salida = "";
                        
                        for(Medico medico : consultorio.getMedicoList()){
                            
                            salida += medico.getCedulaMedico() + ";"
                                + medico.getUsuarioID().getNombres() + ";"
                                + medico.getApellidos() + ";"
                                + medico.getUsuarioID().getTelefono() + ";"
                                + medico.getUsuarioID().getEmail() + ";"
                                + medico.getSexo() + "\n";
                        }
                        
                        return salida;
                    }
                    
                }
                
            }
        }
        
        return "fail";
    }

    /**
     * Metodo para actualizar los datos de un consultorio
     */
    @WebMethod(operationName = "actualizarConsultorio")
    public String actualizarConsultorio(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "nuevoCorreo") String nuevoCorreo, @WebParam(name = "nuevaClave") String nuevaClave,
            @WebParam(name = "nombre") String nombre, @WebParam(name = "direccion") String direccion,
            @WebParam(name = "telefono") String telefono, @WebParam(name = "idMunicipio") int idMunicipio) {
        
        Municipio municipio = ejbMunicipio.find(idMunicipio);
        
        if(municipio == null){
            return "fail";
        }
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && usuario.getRoll().equals("consultorio")){
                
                usuario.setEmail(nuevoCorreo);
                usuario.setClave(nuevaClave);
                usuario.setNombres(nombre);
                usuario.setDireccion(direccion);
                usuario.setTelefono(telefono);
                usuario.setMunicipioIdmunicipio(municipio);
                
                ejbUsuario.edit(usuario);
                
                return "ok";
            }
        }
        
        return "fail";
    }
    
    /**
     * operacion para actualizar los datos de un paciente
     */
    @WebMethod(operationName = "actualizarPaciente")
    public String actualizarPaciente(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "nuevoCorreo") String nuevoCorreo, @WebParam(name = "nuevaClave") String nuevaClave,
            @WebParam(name = "tipoId") String tipoId, @WebParam(name = "numeroId") int numeroId,
            @WebParam(name = "nombres") String nombres, @WebParam(name = "apellidos") String apellidos,
            @WebParam(name = "sexo") String sexo, @WebParam(name = "fechaNacimiento") String fechaNacimiento,
            @WebParam(name = "direccion") String direccion, @WebParam(name = "telefono") String telefono,
            @WebParam(name = "idMunicipio") int idMunicipio) {
        
        Municipio municipio = ejbMunicipio.find(idMunicipio);
        
        if(municipio == null){
            return "fail";
        }
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && usuario.getRoll().equals("paciente")){
                
                for(Paciente paciente : usuario.getPacienteList()){
                    
                    System.out.print("nombre de usuario: " + usuario.getNombres());
                    
                    if(paciente.getUsuarioID().getId() == usuario.getId()){
                        
                        usuario.setEmail(nuevoCorreo);
                        usuario.setClave(nuevaClave);
                        usuario.setNombres(nombres);
                        usuario.setDireccion(direccion);
                        usuario.setTelefono(telefono);
                        usuario.setMunicipioIdmunicipio(municipio);

                        Date fNac;

                        try {
                            fNac = new SimpleDateFormat("yyyy-MM-dd").parse(fechaNacimiento);
                        } catch (ParseException ex) {
                            //Logger.getLogger(SIMOP.class.getName()).log(Level.SEVERE, null, ex);
                            return "fail";
                        }

                        paciente.setApellidos(apellidos);
                        paciente.setSexo(sexo);
                        paciente.setFechanac(fNac);
                        
                        ejbUsuario.edit(usuario);
                        ejbPaciente.edit(paciente);
                        
                        return "ok";
                    }
                    
                }
                
            }
        }
        
        return "fail";
    }

    /**
     * Operacion para actualizar los datos de un medico
     */
    @WebMethod(operationName = "actualizarMedico")
    public String actualizarMedico(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "nuevoCorreo") String nuevoCorreo, @WebParam(name = "nuevaClave") String nuevaClave,
            @WebParam(name = "cedula") int cedula, @WebParam(name = "nombres") String nombres,
            @WebParam(name = "apellidos") String apellidos, @WebParam(name = "sexo") String sexo,
            @WebParam(name = "numeroTP") String numeroTP, @WebParam(name = "nacionalidad") String nacionalidad,
            @WebParam(name = "especializacion") String especializacion, @WebParam(name = "direccion") String direccion,
            @WebParam(name = "telefono") String telefono, @WebParam(name = "idMunicipio") int idMunicipio) {
        
        Municipio municipio = ejbMunicipio.find(idMunicipio);
        
        if(municipio == null){
            return "fail";
        }
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && usuario.getRoll().equals("medico")){
                
                for(Medico medico : usuario.getMedicoList()){
                    
                    if(medico.getUsuarioID().getId() == usuario.getId()){
                        
                        usuario.setEmail(correo);
                        usuario.setClave(clave);
                        usuario.setNombres(nombres);
                        usuario.setDireccion(direccion);
                        usuario.setTelefono(telefono);
                        usuario.setMunicipioIdmunicipio(municipio);
                        
                        medico.setCedulaMedico(cedula);
                        medico.setApellidos(apellidos);
                        medico.setSexo(sexo);
                        medico.setNumTP(numeroTP);
//                        medico.setNummaxpacientes(30);
                        medico.setNacionalidad(nacionalidad);
                        
                        Especialidad esp = medico.getEspecialidadList().get(0);
                        
                        if(esp != null){
                            esp.setNombre(especializacion);
                            ejbEspecialidad.edit(esp);
                        }
                        
                        ejbUsuario.edit(usuario);
                        ejbMedico.edit(medico);
                        
                        return "ok";
                    }
                }
            }
        }
        
        return "fail";
    }
    
    /**
     * Metodo mediante el cual un consultorio relaciona a un medico con dicho consultorio
     */
    @WebMethod(operationName = "relacionarMedicoConsultorio")
    public String relacionarMedicoConsultorio(@WebParam(name = "correo") String correo,
            @WebParam(name = "clave") String clave, @WebParam(name = "cedulaMedico") int cedulaMedico) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getEmail().equals(correo) && usuario.getClave().equals(clave)
                    && usuario.getRoll().equals("consultorio")){
                
                for(Consultorio consultorio : ejbConsultorio.findAll()){
                    
                    if(consultorio.getUsuarioID().getId() == usuario.getId()){
                        
                        Medico medico = ejbMedico.find(cedulaMedico);
                        
                        if(medico == null){
                            return "fail";
                        }
                        
                        List<Consultorio> consultorioList = medico.getConsultorioList();
                        consultorioList.add(consultorio);
                        
                        medico.setConsultorioList(consultorioList);
                        
                        ejbMedico.edit(medico);
                        
                        return "ok";
                    }
                }
            }
        }
        
        return "fail";
    }
    
}
