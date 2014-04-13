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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
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
@Table(name = "chequeo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Chequeo.findAll", query = "SELECT c FROM Chequeo c"),
    @NamedQuery(name = "Chequeo.findByIdchequeo", query = "SELECT c FROM Chequeo c WHERE c.idchequeo = :idchequeo"),
    @NamedQuery(name = "Chequeo.findByTipochequeo", query = "SELECT c FROM Chequeo c WHERE c.tipochequeo = :tipochequeo"),
    @NamedQuery(name = "Chequeo.findByValor", query = "SELECT c FROM Chequeo c WHERE c.valor = :valor"),
    @NamedQuery(name = "Chequeo.findByUnidades", query = "SELECT c FROM Chequeo c WHERE c.unidades = :unidades"),
    @NamedQuery(name = "Chequeo.findByFecha", query = "SELECT c FROM Chequeo c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "Chequeo.findByHora", query = "SELECT c FROM Chequeo c WHERE c.hora = :hora"),
    @NamedQuery(name = "Chequeo.findByDescripcion", query = "SELECT c FROM Chequeo c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "Chequeo.findByLatitud", query = "SELECT c FROM Chequeo c WHERE c.latitud = :latitud"),
    @NamedQuery(name = "Chequeo.findByLongitud", query = "SELECT c FROM Chequeo c WHERE c.longitud = :longitud")})
public class Chequeo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idchequeo")
    private Integer idchequeo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tipochequeo")
    private String tipochequeo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "valor")
    private String valor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "unidades")
    private String unidades;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Size(max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @Lob
    @Size(max = 65535)
    @Column(name = "nota")
    private String nota;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "latitud")
    private String latitud;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "longitud")
    private String longitud;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chequeoIdchequeo")
    private List<Antecedente> antecedenteList;
    @JoinColumns({
        @JoinColumn(name = "paciente_numid", referencedColumnName = "numid"),
        @JoinColumn(name = "paciente_tipoid", referencedColumnName = "tipoid")})
    @ManyToOne(optional = false)
    private Paciente paciente;
    @JoinColumn(name = "tip_idtip", referencedColumnName = "idtip")
    @ManyToOne(optional = false)
    private Tip tipIdtip;

    public Chequeo() {
    }

    public Chequeo(Integer idchequeo) {
        this.idchequeo = idchequeo;
    }

    public Chequeo(Integer idchequeo, String tipochequeo, String valor, String unidades, Date fecha, Date hora, String latitud, String longitud) {
        this.idchequeo = idchequeo;
        this.tipochequeo = tipochequeo;
        this.valor = valor;
        this.unidades = unidades;
        this.fecha = fecha;
        this.hora = hora;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Integer getIdchequeo() {
        return idchequeo;
    }

    public void setIdchequeo(Integer idchequeo) {
        this.idchequeo = idchequeo;
    }

    public String getTipochequeo() {
        return tipochequeo;
    }

    public void setTipochequeo(String tipochequeo) {
        this.tipochequeo = tipochequeo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    @XmlTransient
    public List<Antecedente> getAntecedenteList() {
        return antecedenteList;
    }

    public void setAntecedenteList(List<Antecedente> antecedenteList) {
        this.antecedenteList = antecedenteList;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Tip getTipIdtip() {
        return tipIdtip;
    }

    public void setTipIdtip(Tip tipIdtip) {
        this.tipIdtip = tipIdtip;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idchequeo != null ? idchequeo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Chequeo)) {
            return false;
        }
        Chequeo other = (Chequeo) object;
        if ((this.idchequeo == null && other.idchequeo != null) || (this.idchequeo != null && !this.idchequeo.equals(other.idchequeo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.simop.jpa.Chequeo[ idchequeo=" + idchequeo + " ]";
    }
    
}
