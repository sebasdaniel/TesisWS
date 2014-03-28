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
import javax.persistence.Lob;
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
@Table(name = "tip")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tip.findAll", query = "SELECT t FROM Tip t"),
    @NamedQuery(name = "Tip.findByIdtip", query = "SELECT t FROM Tip t WHERE t.idtip = :idtip"),
    @NamedQuery(name = "Tip.findByTipochequeo", query = "SELECT t FROM Tip t WHERE t.tipochequeo = :tipochequeo"),
    @NamedQuery(name = "Tip.findByMax", query = "SELECT t FROM Tip t WHERE t.max = :max"),
    @NamedQuery(name = "Tip.findByMin", query = "SELECT t FROM Tip t WHERE t.min = :min"),
    @NamedQuery(name = "Tip.findByDescripcion", query = "SELECT t FROM Tip t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "Tip.findByEstado", query = "SELECT t FROM Tip t WHERE t.estado = :estado")})
public class Tip implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtip")
    private Integer idtip;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tipochequeo")
    private String tipochequeo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "max")
    private String max;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "min")
    private String min;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "contenido")
    private String contenido;
    @Size(max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 45)
    @Column(name = "estado")
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipIdtip")
    private List<Chequeo> chequeoList;

    public Tip() {
    }

    public Tip(Integer idtip) {
        this.idtip = idtip;
    }

    public Tip(Integer idtip, String tipochequeo, String max, String min, String contenido) {
        this.idtip = idtip;
        this.tipochequeo = tipochequeo;
        this.max = max;
        this.min = min;
        this.contenido = contenido;
    }

    public Integer getIdtip() {
        return idtip;
    }

    public void setIdtip(Integer idtip) {
        this.idtip = idtip;
    }

    public String getTipochequeo() {
        return tipochequeo;
    }

    public void setTipochequeo(String tipochequeo) {
        this.tipochequeo = tipochequeo;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Chequeo> getChequeoList() {
        return chequeoList;
    }

    public void setChequeoList(List<Chequeo> chequeoList) {
        this.chequeoList = chequeoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtip != null ? idtip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tip)) {
            return false;
        }
        Tip other = (Tip) object;
        if ((this.idtip == null && other.idtip != null) || (this.idtip != null && !this.idtip.equals(other.idtip))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.simop.jpa.Tip[ idtip=" + idtip + " ]";
    }
    
}
