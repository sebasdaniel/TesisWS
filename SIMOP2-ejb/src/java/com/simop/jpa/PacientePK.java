/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author 53B45
 */
@Embeddable
public class PacientePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "numid")
    private int numid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tipoid")
    private String tipoid;

    public PacientePK() {
    }

    public PacientePK(int numid, String tipoid) {
        this.numid = numid;
        this.tipoid = tipoid;
    }

    public int getNumid() {
        return numid;
    }

    public void setNumid(int numid) {
        this.numid = numid;
    }

    public String getTipoid() {
        return tipoid;
    }

    public void setTipoid(String tipoid) {
        this.tipoid = tipoid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) numid;
        hash += (tipoid != null ? tipoid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PacientePK)) {
            return false;
        }
        PacientePK other = (PacientePK) object;
        if (this.numid != other.numid) {
            return false;
        }
        if ((this.tipoid == null && other.tipoid != null) || (this.tipoid != null && !this.tipoid.equals(other.tipoid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.simop.jpa.PacientePK[ numid=" + numid + ", tipoid=" + tipoid + " ]";
    }
    
}
