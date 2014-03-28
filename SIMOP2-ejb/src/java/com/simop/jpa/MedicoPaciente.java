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
@Table(name = "medico_paciente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MedicoPaciente.findAll", query = "SELECT m FROM MedicoPaciente m"),
    @NamedQuery(name = "MedicoPaciente.findByMedicoCedulaMedico", query = "SELECT m FROM MedicoPaciente m WHERE m.medicoPacientePK.medicoCedulaMedico = :medicoCedulaMedico"),
    @NamedQuery(name = "MedicoPaciente.findByPacienteNumid", query = "SELECT m FROM MedicoPaciente m WHERE m.medicoPacientePK.pacienteNumid = :pacienteNumid"),
    @NamedQuery(name = "MedicoPaciente.findByPacienteTipoid", query = "SELECT m FROM MedicoPaciente m WHERE m.medicoPacientePK.pacienteTipoid = :pacienteTipoid"),
    @NamedQuery(name = "MedicoPaciente.findByFechaInicioAtencion", query = "SELECT m FROM MedicoPaciente m WHERE m.fechaInicioAtencion = :fechaInicioAtencion")})
public class MedicoPaciente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MedicoPacientePK medicoPacientePK;
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

    public MedicoPaciente() {
    }

    public MedicoPaciente(MedicoPacientePK medicoPacientePK) {
        this.medicoPacientePK = medicoPacientePK;
    }

    public MedicoPaciente(MedicoPacientePK medicoPacientePK, Date fechaInicioAtencion) {
        this.medicoPacientePK = medicoPacientePK;
        this.fechaInicioAtencion = fechaInicioAtencion;
    }

    public MedicoPaciente(int medicoCedulaMedico, int pacienteNumid, String pacienteTipoid) {
        this.medicoPacientePK = new MedicoPacientePK(medicoCedulaMedico, pacienteNumid, pacienteTipoid);
    }

    public MedicoPacientePK getMedicoPacientePK() {
        return medicoPacientePK;
    }

    public void setMedicoPacientePK(MedicoPacientePK medicoPacientePK) {
        this.medicoPacientePK = medicoPacientePK;
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
        hash += (medicoPacientePK != null ? medicoPacientePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedicoPaciente)) {
            return false;
        }
        MedicoPaciente other = (MedicoPaciente) object;
        if ((this.medicoPacientePK == null && other.medicoPacientePK != null) || (this.medicoPacientePK != null && !this.medicoPacientePK.equals(other.medicoPacientePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.simop.jpa.MedicoPaciente[ medicoPacientePK=" + medicoPacientePK + " ]";
    }
    
}
