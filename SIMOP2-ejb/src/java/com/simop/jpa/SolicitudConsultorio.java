/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 53B45
 */
@Entity
@Table(name = "solicitud_consultorio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SolicitudConsultorio.findAll", query = "SELECT s FROM SolicitudConsultorio s"),
    @NamedQuery(name = "SolicitudConsultorio.findByConsultorioIdconsultorio", query = "SELECT s FROM SolicitudConsultorio s WHERE s.solicitudConsultorioPK.consultorioIdconsultorio = :consultorioIdconsultorio"),
    @NamedQuery(name = "SolicitudConsultorio.findByPacienteNumid", query = "SELECT s FROM SolicitudConsultorio s WHERE s.solicitudConsultorioPK.pacienteNumid = :pacienteNumid"),
    @NamedQuery(name = "SolicitudConsultorio.findByPacienteTipoid", query = "SELECT s FROM SolicitudConsultorio s WHERE s.solicitudConsultorioPK.pacienteTipoid = :pacienteTipoid"),
    @NamedQuery(name = "SolicitudConsultorio.findByEstado", query = "SELECT s FROM SolicitudConsultorio s WHERE s.estado = :estado"),
    @NamedQuery(name = "SolicitudConsultorio.findByFechaSolicitud", query = "SELECT s FROM SolicitudConsultorio s WHERE s.fechaSolicitud = :fechaSolicitud"),
    @NamedQuery(name = "SolicitudConsultorio.findByFechaAprobacionSolicitud", query = "SELECT s FROM SolicitudConsultorio s WHERE s.fechaAprobacionSolicitud = :fechaAprobacionSolicitud")})
public class SolicitudConsultorio implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SolicitudConsultorioPK solicitudConsultorioPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_solicitud")
    @Temporal(TemporalType.DATE)
    private Date fechaSolicitud;
    @Column(name = "fecha_aprobacion_solicitud")
    @Temporal(TemporalType.DATE)
    private Date fechaAprobacionSolicitud;
    @JoinColumns({
        @JoinColumn(name = "paciente_numid", referencedColumnName = "numid", insertable = false, updatable = false),
        @JoinColumn(name = "paciente_tipoid", referencedColumnName = "tipoid", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Paciente paciente;
    @JoinColumn(name = "consultorio_idconsultorio", referencedColumnName = "idconsultorio", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Consultorio consultorio;

    public SolicitudConsultorio() {
    }

    public SolicitudConsultorio(SolicitudConsultorioPK solicitudConsultorioPK) {
        this.solicitudConsultorioPK = solicitudConsultorioPK;
    }

    public SolicitudConsultorio(SolicitudConsultorioPK solicitudConsultorioPK, String estado, Date fechaSolicitud) {
        this.solicitudConsultorioPK = solicitudConsultorioPK;
        this.estado = estado;
        this.fechaSolicitud = fechaSolicitud;
    }

    public SolicitudConsultorio(int consultorioIdconsultorio, int pacienteNumid, String pacienteTipoid) {
        this.solicitudConsultorioPK = new SolicitudConsultorioPK(consultorioIdconsultorio, pacienteNumid, pacienteTipoid);
    }

    public SolicitudConsultorioPK getSolicitudConsultorioPK() {
        return solicitudConsultorioPK;
    }

    public void setSolicitudConsultorioPK(SolicitudConsultorioPK solicitudConsultorioPK) {
        this.solicitudConsultorioPK = solicitudConsultorioPK;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Date getFechaAprobacionSolicitud() {
        return fechaAprobacionSolicitud;
    }

    public void setFechaAprobacionSolicitud(Date fechaAprobacionSolicitud) {
        this.fechaAprobacionSolicitud = fechaAprobacionSolicitud;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Consultorio getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (solicitudConsultorioPK != null ? solicitudConsultorioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SolicitudConsultorio)) {
            return false;
        }
        SolicitudConsultorio other = (SolicitudConsultorio) object;
        if ((this.solicitudConsultorioPK == null && other.solicitudConsultorioPK != null) || (this.solicitudConsultorioPK != null && !this.solicitudConsultorioPK.equals(other.solicitudConsultorioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.simop.jpa.SolicitudConsultorio[ solicitudConsultorioPK=" + solicitudConsultorioPK + " ]";
    }
    
}
