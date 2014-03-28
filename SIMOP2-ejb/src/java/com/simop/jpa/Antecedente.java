/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 53B45
 */
@Entity
@Table(name = "antecedente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Antecedente.findAll", query = "SELECT a FROM Antecedente a"),
    @NamedQuery(name = "Antecedente.findByIdantecedente", query = "SELECT a FROM Antecedente a WHERE a.idantecedente = :idantecedente")})
public class Antecedente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idantecedente")
    private Integer idantecedente;
    @JoinColumn(name = "chequeo_idchequeo", referencedColumnName = "idchequeo")
    @ManyToOne(optional = false)
    private Chequeo chequeoIdchequeo;
    @JoinColumns({
        @JoinColumn(name = "paciente_numid", referencedColumnName = "numid"),
        @JoinColumn(name = "paciente_tipoid", referencedColumnName = "tipoid")})
    @ManyToOne(optional = false)
    private Paciente paciente;

    public Antecedente() {
    }

    public Antecedente(Integer idantecedente) {
        this.idantecedente = idantecedente;
    }

    public Integer getIdantecedente() {
        return idantecedente;
    }

    public void setIdantecedente(Integer idantecedente) {
        this.idantecedente = idantecedente;
    }

    public Chequeo getChequeoIdchequeo() {
        return chequeoIdchequeo;
    }

    public void setChequeoIdchequeo(Chequeo chequeoIdchequeo) {
        this.chequeoIdchequeo = chequeoIdchequeo;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idantecedente != null ? idantecedente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Antecedente)) {
            return false;
        }
        Antecedente other = (Antecedente) object;
        if ((this.idantecedente == null && other.idantecedente != null) || (this.idantecedente != null && !this.idantecedente.equals(other.idantecedente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.simop.jpa.Antecedente[ idantecedente=" + idantecedente + " ]";
    }
    
}
