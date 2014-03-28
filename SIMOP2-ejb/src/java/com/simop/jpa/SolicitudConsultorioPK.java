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
public class SolicitudConsultorioPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "consultorio_idconsultorio")
    private int consultorioIdconsultorio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paciente_numid")
    private int pacienteNumid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "paciente_tipoid")
    private String pacienteTipoid;

    public SolicitudConsultorioPK() {
    }

    public SolicitudConsultorioPK(int consultorioIdconsultorio, int pacienteNumid, String pacienteTipoid) {
        this.consultorioIdconsultorio = consultorioIdconsultorio;
        this.pacienteNumid = pacienteNumid;
        this.pacienteTipoid = pacienteTipoid;
    }

    public int getConsultorioIdconsultorio() {
        return consultorioIdconsultorio;
    }

    public void setConsultorioIdconsultorio(int consultorioIdconsultorio) {
        this.consultorioIdconsultorio = consultorioIdconsultorio;
    }

    public int getPacienteNumid() {
        return pacienteNumid;
    }

    public void setPacienteNumid(int pacienteNumid) {
        this.pacienteNumid = pacienteNumid;
    }

    public String getPacienteTipoid() {
        return pacienteTipoid;
    }

    public void setPacienteTipoid(String pacienteTipoid) {
        this.pacienteTipoid = pacienteTipoid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) consultorioIdconsultorio;
        hash += (int) pacienteNumid;
        hash += (pacienteTipoid != null ? pacienteTipoid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SolicitudConsultorioPK)) {
            return false;
        }
        SolicitudConsultorioPK other = (SolicitudConsultorioPK) object;
        if (this.consultorioIdconsultorio != other.consultorioIdconsultorio) {
            return false;
        }
        if (this.pacienteNumid != other.pacienteNumid) {
            return false;
        }
        if ((this.pacienteTipoid == null && other.pacienteTipoid != null) || (this.pacienteTipoid != null && !this.pacienteTipoid.equals(other.pacienteTipoid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.simop.jpa.SolicitudConsultorioPK[ consultorioIdconsultorio=" + consultorioIdconsultorio + ", pacienteNumid=" + pacienteNumid + ", pacienteTipoid=" + pacienteTipoid + " ]";
    }
    
}
