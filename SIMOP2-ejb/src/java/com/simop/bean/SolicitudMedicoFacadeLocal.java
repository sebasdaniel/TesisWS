/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.bean;

import com.simop.jpa.SolicitudMedico;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 53B45
 */
@Local
public interface SolicitudMedicoFacadeLocal {

    void create(SolicitudMedico solicitudMedico);

    void edit(SolicitudMedico solicitudMedico);

    void remove(SolicitudMedico solicitudMedico);

    SolicitudMedico find(Object id);

    List<SolicitudMedico> findAll();

    List<SolicitudMedico> findRange(int[] range);

    int count();
    
}
