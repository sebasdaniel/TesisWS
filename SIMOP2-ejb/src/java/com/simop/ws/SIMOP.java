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
import com.simop.bean.MedicoPacienteFacadeLocal;
import com.simop.bean.PacienteFacadeLocal;
import com.simop.bean.SolicitudConsultorioFacadeLocal;
import com.simop.bean.SolicitudMedicoFacadeLocal;
import com.simop.bean.TipFacadeLocal;
import com.simop.bean.UsuarioFacadeLocal;
import com.simop.jpa.Antecedente;
import com.simop.jpa.Consultorio;
import com.simop.jpa.Diagnostico;
import com.simop.jpa.MedicoPaciente;
import com.simop.jpa.PacientePK;
import com.simop.jpa.SolicitudConsultorio;
import com.simop.jpa.SolicitudMedico;
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
    private MedicoPacienteFacadeLocal ejbMedicoPaciente;
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
    public String listaPacientes(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "soloConsultorio") boolean soloConsultorio, @WebParam(name = "cedulaMedico") int cedulaMedico) {
        
        //System.out.println("entro al metodo");
        for (Usuario user : ejbUsuario.findAll()) {
            //System.out.println("recorriendo usuarios");

            if (user.getCorreo().equals(correo) && user.getContraseña().equals(clave)
                    && (user.getRoll().equals("medico") || user.getRoll().equals("consultorio"))) {

                //System.out.println("encontro usuario");
                if(user.getRoll().equals("medico")){

                    //System.out.println("encontro que es medico");
                    List<Medico> med = user.getMedicoList();

                    for (Medico m : med) {

                        if (m.getUsuarioID().getCorreo().equals(correo) && m.getUsuarioID().getContraseña().equals(clave)) {
                            //System.out.println("encontro al medico");
                            List<MedicoPaciente> relacion = ejbMedicoPaciente.findAll();
                            
                            String salida = "";
                            
                            for(MedicoPaciente temp : relacion){
                                
                                if(temp.getMedico().getCedulaMedico() == m.getCedulaMedico()){
                                    
                                    Paciente p = temp.getPaciente();
                                    
                                    salida += p.getPacientePK().getTipoid() + ";"
                                            + p.getPacientePK().getNumid() + ";"
                                            + p.getUsuarioID().getNombres() + ";"
                                            + p.getApellidos() + ";"
                                            + p.getUsuarioID().getTelefono() + ";"
                                            + p.getSexo() + ";"
                                            + p.getGruposan() + ";"
                                            + (p.getRh() ? "+" : "-") + ";"
                                            + p.getEdad() + "\n";
                                }
                            }
                            
                            return salida;
                        }
                    }
                } else if(user.getRoll().equals("consultorio")){

                    //System.out.println("encontro que es consultorio");
                    List<Consultorio> con = user.getConsultorioList();

                    for (Consultorio c : con) {

                        if (c.getUsuarioID().getCorreo().equals(correo) && c.getUsuarioID().getContraseña().equals(clave)) {
                            //System.out.println("encontro consultorio");
                            if(soloConsultorio){
                                
                                List<SolicitudConsultorio> solicitud = ejbSolicitudConsultorio.findAll();

                                String salida = "";

                                for(SolicitudConsultorio temp : solicitud){
                                    
                                    // NOTA: falta validar que no tengan medico asignado
                                    if(temp.getConsultorio().getIdconsultorio() == c.getIdconsultorio()
                                            && temp.getEstado().equals("aprobado")){

                                        Paciente p = temp.getPaciente();
                                        
                                        boolean tieneMedico = false;
                                        
                                        for(MedicoPaciente mp : p.getMedicoPacienteList()){
                                            
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
                                                    + p.getSexo() + ";"
                                                    + p.getGruposan() + ";"
                                                    + (p.getRh() ? "+" : "-") + ";"
                                                    + p.getEdad() + "\n";
                                        }
                                        
                                    }
                                }

                                return salida;
                                
                            } else {
                                
                                Medico medico = ejbMedico.find(cedulaMedico);
                                
                                if(medico == null){
                                    return null;
                                }
                                
                                List<MedicoPaciente> relacion = ejbMedicoPaciente.findAll();

                                String salida = "";

                                for(MedicoPaciente temp : relacion){

                                    if(temp.getMedico().getCedulaMedico() == medico.getCedulaMedico()){

                                        Paciente p = temp.getPaciente();

                                        salida += p.getPacientePK().getTipoid() + ";"
                                                + p.getPacientePK().getNumid() + ";"
                                                + p.getUsuarioID().getNombres() + ";"
                                                + p.getApellidos() + ";"
                                                + p.getUsuarioID().getTelefono() + ";"
                                                + p.getSexo() + ";"
                                                + p.getGruposan() + ";"
                                                + (p.getRh() ? "+" : "-") + ";"
                                                + p.getEdad() + "\n";
                                    }
                                }

                                return salida;
                                
                            }
                        }
                    }
                }

            }
        }
        
        return null;
    }

    /**
     * Operacion para listar los medicos para un paciente o consultorio
     */
    
    @WebMethod(operationName = "listaMedicos")
    public String listaMedicos(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave) {
        
        for (Usuario user : ejbUsuario.findAll()) {

            if (user.getCorreo().equals(correo) && user.getContraseña().equals(clave)) {
                
                List<Medico> mlist = ejbMedico.findAll();
        
                String salida = "";

                for(Medico m : mlist){

                    salida += m.getCedulaMedico() + ";"
                            + m.getUsuarioID().getNombres() + ";"
                            + m.getApellidos() + ";"
                            + m.getSexo() + ";"
                            + m.getNacionalidad() + "\n";
                }

                return salida;
            }
        }
        
        return null;
    }

    /**
     * Metodo para obtener las solicitudes pendientes hechas a un medico o consultorio
     */
    @WebMethod(operationName = "listarSolicitudes")
    public String listarSolicitudes(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getCorreo().equals(correo) && usuario.getContraseña().equals(clave)
                    && (usuario.getRoll().equals("medico") || usuario.getRoll().equals("consultorio"))){
                
                switch (usuario.getRoll()) {
                    
                    case "medico":
                        List<Medico> medicos = usuario.getMedicoList();
                        for(Medico medico : medicos){
                            if(medico.getUsuarioID().getId() == usuario.getId()){
                                
                                List<SolicitudMedico> solicitudes = medico.getSolicitudMedicoList();//ejbSolicitudMedico.findAll();
                                String salida = "";
                                for(SolicitudMedico solicitud : solicitudes){
                                    if(solicitud.getEstado().equals("pendiente")){
                                        
                                        Paciente p = solicitud.getPaciente();

                                        salida += p.getPacientePK().getTipoid() + ";"
                                                + p.getPacientePK().getNumid() + ";"
                                                + p.getUsuarioID().getNombres() + ";"
                                                + p.getApellidos() + ";"
                                                + p.getUsuarioID().getTelefono() + ";"
                                                + p.getSexo() + ";"
                                                + p.getGruposan() + ";"
                                                + (p.getRh() ? "+" : "-") + ";"
                                                + p.getEdad() + "\n";
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
                                                + p.getSexo() + ";"
                                                + p.getGruposan() + ";"
                                                + (p.getRh() ? "+" : "-") + ";"
                                                + p.getEdad() + "\n";
                                    }
                                }
                                
                                return salida;
                            }
                        }
                        break;
                }
            }
        }
        
        return null;
    }

    /**
     * Operacion Web Service para buscar medicos por nombres y/o apellidos
     */
    @WebMethod(operationName = "buscarMedico")
    public String buscarMedico(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "nombre") String nombre) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getCorreo().equals(correo) && usuario.getContraseña().equals(clave)){
                
                List<Medico> medicos = ejbMedico.findAll();
                
                String salida = "";
                
                for(Medico medico : medicos){
                    
                    // Usar expresiones regulares para mejorar la busqueda
                    if(medico.getUsuarioID().getNombres().contains(nombre) || medico.getApellidos().contains(nombre)){
                        
                        salida += medico.getCedulaMedico() + ";"
                                + medico.getUsuarioID().getNombres() + ";"
                                + medico.getApellidos() + ";"
                                + medico.getSexo() + ";"
                                + medico.getNacionalidad() + "\n";
                    }
                }
                
                return salida;
            }
        }
        
        return null;
    }

    /**
     * Operacion para buscar consultorios por nombre
     */
    @WebMethod(operationName = "buscarConsultorio")
    public String buscarConsultorio(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "nombre") String nombre) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getCorreo().equals(correo) && usuario.getContraseña().equals(clave)){
                
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
            
            if(usuario.getCorreo().equals(correo) && usuario.getContraseña().equals(clave)
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
            
            if(usuario.getCorreo().equals(correo) && usuario.getContraseña().equals(clave)
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
     * Web service operation
     */
    @WebMethod(operationName = "obtenerHistorial")
    public String obtenerHistorial(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave,
            @WebParam(name = "modo") int modo, @WebParam(name = "pacienteTipoId") String pacienteTipoId,
            @WebParam(name = "pacienteNumId") String pacienteNumId) {
        
        XmlHl7 historial = new XmlHl7();
        
        return historial.getXmlInString();
    }

    /**
     * Operacion para que un paciente obtenga los diagnosticos hechos por su medico
     */
    @WebMethod(operationName = "obtenerDiagnosticos")
    public String obtenerDiagnosticos(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave) {
        
        for(Usuario usuario : ejbUsuario.findAll()){
            
            if(usuario.getCorreo().equals(correo) && usuario.getContraseña().equals(clave)
                    && usuario.getRoll().equals("paciente")){
                
                List<Paciente> pacientes = usuario.getPacienteList();
                
                for(Paciente paciente : pacientes){
                    
                    if(paciente.getUsuarioID().getId() == usuario.getId()){
                        
                        List<Diagnostico> diagnosticos = ejbDiagnostico.findAll();
                        
                        String salida = "";
                        
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
                        
                        for(Diagnostico diagnostico : diagnosticos){
                            
                            PacientePK cheqPacientePK = diagnostico.getChequeoIdchequeo().getPaciente().getPacientePK();
                            
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
            
            if(usuario.getCorreo().equals(correo) && usuario.getContraseña().equals(clave)
                    && usuario.getRoll().equals("medico")){
                
                List<Medico> medicos = usuario.getMedicoList();
                
                for(Medico medico : medicos){
                    
                    if(medico.getUsuarioID().getId() == usuario.getId()){
                        
                        List<MedicoPaciente> atenciones = ejbMedicoPaciente.findAll();
                        
                        List<Paciente> misPacientes = new ArrayList<>();
                        
                        for(MedicoPaciente atencion : atenciones){
                            
                            if(atencion.getMedico().getCedulaMedico() == medico.getCedulaMedico()){
                                
                                misPacientes.add(atencion.getPaciente());
                            }
                        }
                        
                        if(misPacientes.isEmpty()){
                            return "";
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
                                    
                                    salida += temp.getPacientePK().getTipoid() + ";"
                                            + temp.getPacientePK().getNumid() + ";"
                                            + temp.getUsuarioID().getNombres() + " "
                                            + temp.getApellidos() + ";"
                                            + formatoFecha.format(c.getFecha()) + ";"
                                            + formatoHora.format(c.getHora()) + ";"
                                            + c.getTipochequeo() + ";"
                                            + c.getValor() + ";"
                                            + c.getUnidades() + ";"
                                            + c.getTipIdtip().getEstado() + "\n";
                                }
                            }
                        }
                        
                        return salida;
                    }
                }
                
            }
        }
        
        return "";
    }
    
}
