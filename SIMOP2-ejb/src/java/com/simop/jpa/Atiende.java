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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 53B45
 */
@Entity
@Table(name = "atiende")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Atiende.findAll", query = "SELECT a FROM Atiende a"),
    @NamedQuery(name = "Atiende.findByMedicoCedulaMedico", query = "SELECT a FROM Atiende a WHERE a.atiendePK.medicoCedulaMedico = :medicoCedulaMedico"),
    @NamedQuery(name = "Atiende.findByPacienteNumid", query = "SELECT a FROM Atiende a WHERE a.atiendePK.pacienteNumid = :pacienteNumid"),
    @NamedQuery(name = "Atiende.findByPacienteTipoid", query = "SELECT a FROM Atiende a WHERE a.atiendePK.pacienteTipoid = :pacienteTipoid"),
    @NamedQuery(name = "Atiende.findByFechaInicioAtencion", query = "SELECT a FROM Atiende a WHERE a.fechaInicioAtencion = :fechaInicioAtencion")})
public class Atiende implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AtiendePK atiendePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio_atencion")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioAtencion;
    @JoinColumns({
        @JoinColumn(name = "paciente_numid", referencedColumnName = "numid", insertable = false, updatable = false),
        @JoinColumn(name = "paciente_tipoid", referencedColumnName = "tipoid", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Paciente paciente;
    @JoinColumn(name = "medico_cedula_medico", referencedColumnName = "cedula_medico", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Medico medico;

    public Atiende() {
    }

    public Atiende(AtiendePK atiendePK) {
        this.atiendePK = atiendePK;
    }

    public Atiende(AtiendePK atiendePK, Date fechaInicioAtencion) {
        this.atiendePK = atiendePK;
        this.fechaInicioAtencion = fechaInicioAtencion;
    }

    public Atiende(int medicoCedulaMedico, int pacienteNumid, String pacienteTipoid) {
        this.atiendePK = new AtiendePK(medicoCedulaMedico, pacienteNumid, pacienteTipoid);
    }

    public AtiendePK getAtiendePK() {
        return atiendePK;
    }

    public void setAtiendePK(AtiendePK atiendePK) {
        this.atiendePK = atiendePK;
    }

    public Date getFechaInicioAtencion() {
        return fechaInicioAtencion;
    }

    public void setFechaInicioAtencion(Date fechaInicioAtencion) {
        this.fechaInicioAtencion = fechaInicioAtencion;
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
        hash += (atiendePK != null ? atiendePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Atiende)) {
            return false;
        }
        Atiende other = (Atiende) object;
        if ((this.atiendePK == null && other.atiendePK != null) || (this.atiendePK != null && !this.atiendePK.equals(other.atiendePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.simop.jpa.Atiende[ atiendePK=" + atiendePK + " ]";
    }
    
}
