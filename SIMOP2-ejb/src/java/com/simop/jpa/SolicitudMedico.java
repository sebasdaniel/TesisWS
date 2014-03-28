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
@Table(name = "solicitud_medico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SolicitudMedico.findAll", query = "SELECT s FROM SolicitudMedico s"),
    @NamedQuery(name = "SolicitudMedico.findByMedicoCedulaMedico", query = "SELECT s FROM SolicitudMedico s WHERE s.solicitudMedicoPK.medicoCedulaMedico = :medicoCedulaMedico"),
    @NamedQuery(name = "SolicitudMedico.findByPacienteNumid", query = "SELECT s FROM SolicitudMedico s WHERE s.solicitudMedicoPK.pacienteNumid = :pacienteNumid"),
    @NamedQuery(name = "SolicitudMedico.findByPacienteTipoid", query = "SELECT s FROM SolicitudMedico s WHERE s.solicitudMedicoPK.pacienteTipoid = :pacienteTipoid"),
    @NamedQuery(name = "SolicitudMedico.findByEstado", query = "SELECT s FROM SolicitudMedico s WHERE s.estado = :estado"),
    @NamedQuery(name = "SolicitudMedico.findByFechaSolicitud", query = "SELECT s FROM SolicitudMedico s WHERE s.fechaSolicitud = :fechaSolicitud"),
    @NamedQuery(name = "SolicitudMedico.findByFechaAprobacionSolicitud", query = "SELECT s FROM SolicitudMedico s WHERE s.fechaAprobacionSolicitud = :fechaAprobacionSolicitud")})
public class SolicitudMedico implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SolicitudMedicoPK solicitudMedicoPK;
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
    @JoinColumn(name = "medico_cedula_medico", referencedColumnName = "cedula_medico", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Medico medico;

    public SolicitudMedico() {
    }

    public SolicitudMedico(SolicitudMedicoPK solicitudMedicoPK) {
        this.solicitudMedicoPK = solicitudMedicoPK;
    }

    public SolicitudMedico(SolicitudMedicoPK solicitudMedicoPK, String estado, Date fechaSolicitud) {
        this.solicitudMedicoPK = solicitudMedicoPK;
        this.estado = estado;
        this.fechaSolicitud = fechaSolicitud;
    }

    public SolicitudMedico(int medicoCedulaMedico, int pacienteNumid, String pacienteTipoid) {
        this.solicitudMedicoPK = new SolicitudMedicoPK(medicoCedulaMedico, pacienteNumid, pacienteTipoid);
    }

    public SolicitudMedicoPK getSolicitudMedicoPK() {
        return solicitudMedicoPK;
    }

    public void setSolicitudMedicoPK(SolicitudMedicoPK solicitudMedicoPK) {
        this.solicitudMedicoPK = solicitudMedicoPK;
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

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (solicitudMedicoPK != null ? solicitudMedicoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SolicitudMedico)) {
            return false;
        }
        SolicitudMedico other = (SolicitudMedico) object;
        if ((this.solicitudMedicoPK == null && other.solicitudMedicoPK != null) || (this.solicitudMedicoPK != null && !this.solicitudMedicoPK.equals(other.solicitudMedicoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.simop.jpa.SolicitudMedico[ solicitudMedicoPK=" + solicitudMedicoPK + " ]";
    }
    
}
