/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.jpa;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    @NamedQuery(name = "Paciente.findByApellidos", query = "SELECT p FROM Paciente p WHERE p.apellidos = :apellidos"),
    @NamedQuery(name = "Paciente.findBySexo", query = "SELECT p FROM Paciente p WHERE p.sexo = :sexo"),
    @NamedQuery(name = "Paciente.findByFechanac", query = "SELECT p FROM Paciente p WHERE p.fechanac = :fechanac")})
public class Paciente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PacientePK pacientePK;
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
    @Column(name = "fechanac")
    @Temporal(TemporalType.DATE)
    private Date fechanac;
    @JoinColumn(name = "usuario_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario usuarioID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private List<Antecedente> antecedenteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private List<Atiende> atiendeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private List<SolicitudMedico> solicitudMedicoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private List<Chequeo> chequeoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private List<SolicitudConsultorio> solicitudConsultorioList;

    public Paciente() {
    }

    public Paciente(PacientePK pacientePK) {
        this.pacientePK = pacientePK;
    }

    public Paciente(PacientePK pacientePK, String apellidos, String sexo, Date fechanac) {
        this.pacientePK = pacientePK;
        this.apellidos = apellidos;
        this.sexo = sexo;
        this.fechanac = fechanac;
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

    public Date getFechanac() {
        return fechanac;
    }

    public void setFechanac(Date fechanac) {
        this.fechanac = fechanac;
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
    public List<Atiende> getAtiendeList() {
        return atiendeList;
    }

    public void setAtiendeList(List<Atiende> atiendeList) {
        this.atiendeList = atiendeList;
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
