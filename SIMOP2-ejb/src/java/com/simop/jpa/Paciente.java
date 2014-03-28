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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "paciente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paciente.findAll", query = "SELECT p FROM Paciente p"),
    @NamedQuery(name = "Paciente.findByNumid", query = "SELECT p FROM Paciente p WHERE p.pacientePK.numid = :numid"),
    @NamedQuery(name = "Paciente.findByTipoid", query = "SELECT p FROM Paciente p WHERE p.pacientePK.tipoid = :tipoid"),
    @NamedQuery(name = "Paciente.findBySexo", query = "SELECT p FROM Paciente p WHERE p.sexo = :sexo"),
    @NamedQuery(name = "Paciente.findByEstatura", query = "SELECT p FROM Paciente p WHERE p.estatura = :estatura"),
    @NamedQuery(name = "Paciente.findByImc", query = "SELECT p FROM Paciente p WHERE p.imc = :imc"),
    @NamedQuery(name = "Paciente.findByGruposan", query = "SELECT p FROM Paciente p WHERE p.gruposan = :gruposan"),
    @NamedQuery(name = "Paciente.findByRh", query = "SELECT p FROM Paciente p WHERE p.rh = :rh"),
    @NamedQuery(name = "Paciente.findByEdad", query = "SELECT p FROM Paciente p WHERE p.edad = :edad"),
    @NamedQuery(name = "Paciente.findByApellidos", query = "SELECT p FROM Paciente p WHERE p.apellidos = :apellidos")})
public class Paciente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PacientePK pacientePK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "sexo")
    private String sexo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "estatura")
    private String estatura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "imc")
    private String imc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "gruposan")
    private String gruposan;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rh")
    private boolean rh;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "edad")
    private String edad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "apellidos")
    private String apellidos;
    @JoinColumn(name = "usuario_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario usuarioID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private List<Antecedente> antecedenteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private List<SolicitudMedico> solicitudMedicoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private List<Chequeo> chequeoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private List<SolicitudConsultorio> solicitudConsultorioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private List<MedicoPaciente> medicoPacienteList;

    public Paciente() {
    }

    public Paciente(PacientePK pacientePK) {
        this.pacientePK = pacientePK;
    }

    public Paciente(PacientePK pacientePK, String sexo, String estatura, String imc, String gruposan, boolean rh, String edad, String apellidos) {
        this.pacientePK = pacientePK;
        this.sexo = sexo;
        this.estatura = estatura;
        this.imc = imc;
        this.gruposan = gruposan;
        this.rh = rh;
        this.edad = edad;
        this.apellidos = apellidos;
    }

    public Paciente(int numid, String tipoid) {
        this.pacientePK = new PacientePK(numid, tipoid);
    }

    public PacientePK getPacientePK() {
        return pacientePK;
    }

    public void setPacientePK(PacientePK pacientePK) {
        this.pacientePK = pacientePK;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEstatura() {
        return estatura;
    }

    public void setEstatura(String estatura) {
        this.estatura = estatura;
    }

    public String getImc() {
        return imc;
    }

    public void setImc(String imc) {
        this.imc = imc;
    }

    public String getGruposan() {
        return gruposan;
    }

    public void setGruposan(String gruposan) {
        this.gruposan = gruposan;
    }

    public boolean getRh() {
        return rh;
    }

    public void setRh(boolean rh) {
        this.rh = rh;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Usuario getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Usuario usuarioID) {
        this.usuarioID = usuarioID;
    }

    @XmlTransient
    public List<Antecedente> getAntecedenteList() {
        return antecedenteList;
    }

    public void setAntecedenteList(List<Antecedente> antecedenteList) {
        this.antecedenteList = antecedenteList;
    }

    @XmlTransient
    public List<SolicitudMedico> getSolicitudMedicoList() {
        return solicitudMedicoList;
    }

    public void setSolicitudMedicoList(List<SolicitudMedico> solicitudMedicoList) {
        this.solicitudMedicoList = solicitudMedicoList;
    }

    @XmlTransient
    public List<Chequeo> getChequeoList() {
        return chequeoList;
    }

    public void setChequeoList(List<Chequeo> chequeoList) {
        this.chequeoList = chequeoList;
    }

    @XmlTransient
    public List<SolicitudConsultorio> getSolicitudConsultorioList() {
        return solicitudConsultorioList;
    }

    public void setSolicitudConsultorioList(List<SolicitudConsultorio> solicitudConsultorioList) {
        this.solicitudConsultorioList = solicitudConsultorioList;
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
        hash += (pacientePK != null ? pacientePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paciente)) {
            return false;
        }
        Paciente other = (Paciente) object;
        if ((this.pacientePK == null && other.pacientePK != null) || (this.pacientePK != null && !this.pacientePK.equals(other.pacientePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.simop.jpa.Paciente[ pacientePK=" + pacientePK + " ]";
    }
    
}
