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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 53B45
 */
@Entity
@Table(name = "consultorio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Consultorio.findAll", query = "SELECT c FROM Consultorio c"),
    @NamedQuery(name = "Consultorio.findByIdconsultorio", query = "SELECT c FROM Consultorio c WHERE c.idconsultorio = :idconsultorio")})
public class Consultorio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idconsultorio")
    private Integer idconsultorio;
    @ManyToMany(mappedBy = "consultorioList")
    private List<Medico> medicoList;
    @JoinColumn(name = "usuario_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario usuarioID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consultorio")
    private List<SolicitudConsultorio> solicitudConsultorioList;

    public Consultorio() {
    }

    public Consultorio(Integer idconsultorio) {
        this.idconsultorio = idconsultorio;
    }

    public Integer getIdconsultorio() {
        return idconsultorio;
    }

    public void setIdconsultorio(Integer idconsultorio) {
        this.idconsultorio = idconsultorio;
    }

    @XmlTransient
    public List<Medico> getMedicoList() {
        return medicoList;
    }

    public void setMedicoList(List<Medico> medicoList) {
        this.medicoList = medicoList;
    }

    public Usuario getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Usuario usuarioID) {
        this.usuarioID = usuarioID;
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
        hash += (idconsultorio != null ? idconsultorio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Consultorio)) {
            return false;
        }
        Consultorio other = (Consultorio) object;
        if ((this.idconsultorio == null && other.idconsultorio != null) || (this.idconsultorio != null && !this.idconsultorio.equals(other.idconsultorio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.simop.jpa.Consultorio[ idconsultorio=" + idconsultorio + " ]";
    }
    
}
