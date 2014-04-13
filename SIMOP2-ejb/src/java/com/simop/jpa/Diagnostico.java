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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "diagnostico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Diagnostico.findAll", query = "SELECT d FROM Diagnostico d"),
    @NamedQuery(name = "Diagnostico.findByIddiagnostico", query = "SELECT d FROM Diagnostico d WHERE d.iddiagnostico = :iddiagnostico"),
    @NamedQuery(name = "Diagnostico.findByFecha", query = "SELECT d FROM Diagnostico d WHERE d.fecha = :fecha"),
    @NamedQuery(name = "Diagnostico.findByHora", query = "SELECT d FROM Diagnostico d WHERE d.hora = :hora"),
    @NamedQuery(name = "Diagnostico.findByVista", query = "SELECT d FROM Diagnostico d WHERE d.vista = :vista")})
public class Diagnostico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddiagnostico")
    private Integer iddiagnostico;
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
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "contenido")
    private String contenido;
    @Column(name = "vista")
    private Boolean vista;
    @JoinColumn(name = "antecedente_idantecedente", referencedColumnName = "idantecedente")
    @ManyToOne(optional = false)
    private Antecedente antecedenteIdantecedente;
    @JoinColumn(name = "medico_cedula_medico", referencedColumnName = "cedula_medico")
    @ManyToOne(optional = false)
    private Medico medicoCedulaMedico;

    public Diagnostico() {
    }

    public Diagnostico(Integer iddiagnostico) {
        this.iddiagnostico = iddiagnostico;
    }

    public Diagnostico(Integer iddiagnostico, Date fecha, Date hora, String contenido) {
        this.iddiagnostico = iddiagnostico;
        this.fecha = fecha;
        this.hora = hora;
        this.contenido = contenido;
    }

    public Integer getIddiagnostico() {
        return iddiagnostico;
    }

    public void setIddiagnostico(Integer iddiagnostico) {
        this.iddiagnostico = iddiagnostico;
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

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Boolean getVista() {
        return vista;
    }

    public void setVista(Boolean vista) {
        this.vista = vista;
    }

    public Antecedente getAntecedenteIdantecedente() {
        return antecedenteIdantecedente;
    }

    public void setAntecedenteIdantecedente(Antecedente antecedenteIdantecedente) {
        this.antecedenteIdantecedente = antecedenteIdantecedente;
    }

    public Medico getMedicoCedulaMedico() {
        return medicoCedulaMedico;
    }

    public void setMedicoCedulaMedico(Medico medicoCedulaMedico) {
        this.medicoCedulaMedico = medicoCedulaMedico;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddiagnostico != null ? iddiagnostico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Diagnostico)) {
            return false;
        }
        Diagnostico other = (Diagnostico) object;
        if ((this.iddiagnostico == null && other.iddiagnostico != null) || (this.iddiagnostico != null && !this.iddiagnostico.equals(other.iddiagnostico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.simop.jpa.Diagnostico[ iddiagnostico=" + iddiagnostico + " ]";
    }
    
}
