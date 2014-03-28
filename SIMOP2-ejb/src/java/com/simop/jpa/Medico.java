/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 53B45
 */
@Entity
@Table(name = "medico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Medico.findAll", query = "SELECT m FROM Medico m"),
    @NamedQuery(name = "Medico.findByCedulaMedico", query = "SELECT m FROM Medico m WHERE m.cedulaMedico = :cedulaMedico"),
    @NamedQuery(name = "Medico.findByApellidos", query = "SELECT m FROM Medico m WHERE m.apellidos = :apellidos"),
    @NamedQuery(name = "Medico.findBySexo", query = "SELECT m FROM Medico m WHERE m.sexo = :sexo"),
    @NamedQuery(name = "Medico.findByNumTP", query = "SELECT m FROM Medico m WHERE m.numTP = :numTP"),
    @NamedQuery(name = "Medico.findByNummaxpacientes", query = "SELECT m FROM Medico m WHERE m.nummaxpacientes = :nummaxpacientes"),
    @NamedQuery(name = "Medico.findByNacionalidad", query = "SELECT m FROM Medico m WHERE m.nacionalidad = :nacionalidad")})
public class Medico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cedula_medico")
    private Integer cedulaMedico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "apellidos")
    private String apellidos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "sexo")
    private String sexo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numTP")
    private int numTP;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nummaxpacientes")
    private int nummaxpacientes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nacionalidad")
    private String nacionalidad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicoCedulaMedico")
    private List<Especialidad> especialidadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medico")
    private List<SolicitudMedico> solicitudMedicoList;
    @JoinColumn(name = "usuario_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario usuarioID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicoCedulaMedico")
    private List<Diagnostico> diagnosticoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medico")
    private List<MedicoPaciente> medicoPacienteList;

    public Medico() {
    }

    public Medico(Integer cedulaMedico) {
        this.cedulaMedico = cedulaMedico;
    }

    public Medico(Integer cedulaMedico, String apellidos, String sexo, int numTP, int nummaxpacientes, String nacionalidad) {
        this.cedulaMedico = cedulaMedico;
        this.apellidos = apellidos;
        this.sexo = sexo;
        this.numTP = numTP;
        this.nummaxpacientes = nummaxpacientes;
        this.nacionalidad = nacionalidad;
    }

    public Integer getCedulaMedico() {
        return cedulaMedico;
    }

    public void setCedulaMedico(Integer cedulaMedico) {
        this.cedulaMedico = cedulaMedico;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getNumTP() {
        return numTP;
    }

    public void setNumTP(int numTP) {
        this.numTP = numTP;
    }

    public int getNummaxpacientes() {
        return nummaxpacientes;
    }

    public void setNummaxpacientes(int nummaxpacientes) {
        this.nummaxpacientes = nummaxpacientes;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    @XmlTransient
    public List<Especialidad> getEspecialidadList() {
        return especialidadList;
    }

    public void setEspecialidadList(List<Especialidad> especialidadList) {
        this.especialidadList = especialidadList;
    }

    @XmlTransient
    public List<SolicitudMedico> getSolicitudMedicoList() {
        return solicitudMedicoList;
    }

    public void setSolicitudMedicoList(List<SolicitudMedico> solicitudMedicoList) {
        this.solicitudMedicoList = solicitudMedicoList;
    }

    public Usuario getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Usuario usuarioID) {
        this.usuarioID = usuarioID;
    }

    @XmlTransient
    public List<Diagnostico> getDiagnosticoList() {
        return diagnosticoList;
    }

    public void setDiagnosticoList(List<Diagnostico> diagnosticoList) {
        this.diagnosticoList = diagnosticoList;
    }

    @XmlTransient
    public List<MedicoPaciente> getMedicoPacienteList() {
        return medicoPacienteList;
    }

    public void setMedicoPacienteList(List<MedicoPaciente> medicoPacienteList) {
        this.medicoPacienteList = medicoPacienteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cedulaMedico != null ? cedulaMedico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medico)) {
            return false;
        }
        Medico other = (Medico) object;
        if ((this.cedulaMedico == null && other.cedulaMedico != null) || (this.cedulaMedico != null && !this.cedulaMedico.equals(other.cedulaMedico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.simop.jpa.Medico[ cedulaMedico=" + cedulaMedico + " ]";
    }
    
}
