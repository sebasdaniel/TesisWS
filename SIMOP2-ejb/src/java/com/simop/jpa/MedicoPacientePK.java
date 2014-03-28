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
public class MedicoPacientePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "medico_cedula_medico")
    private int medicoCedulaMedico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paciente_numid")
    private int pacienteNumid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "paciente_tipoid")
    private String pacienteTipoid;

    public MedicoPacientePK() {
    }

    public MedicoPacientePK(int medicoCedulaMedico, int pacienteNumid, String pacienteTipoid) {
        this.medicoCedulaMedico = medicoCedulaMedico;
        this.pacienteNumid = pacienteNumid;
        this.pacienteTipoid = pacienteTipoid;
    }

    public int getMedicoCedulaMedico() {
        return medicoCedulaMedico;
    }

    public void setMedicoCedulaMedico(int medicoCedulaMedico) {
        this.medicoCedulaMedico = medicoCedulaMedico;
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
        hash += (int) medicoCedulaMedico;
        hash += (int) pacienteNumid;
        hash += (pacienteTipoid != null ? pacienteTipoid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedicoPacientePK)) {
            return false;
        }
        MedicoPacientePK other = (MedicoPacientePK) object;
        if (this.medicoCedulaMedico != other.medicoCedulaMedico) {
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
        return "com.simop.jpa.MedicoPacientePK[ medicoCedulaMedico=" + medicoCedulaMedico + ", pacienteNumid=" + pacienteNumid + ", pacienteTipoid=" + pacienteTipoid + " ]";
    }
    
}
