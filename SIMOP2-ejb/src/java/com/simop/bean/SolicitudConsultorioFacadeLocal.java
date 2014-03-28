/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.bean;

import com.simop.jpa.SolicitudConsultorio;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 53B45
 */
@Local
public interface SolicitudConsultorioFacadeLocal {

    void create(SolicitudConsultorio solicitudConsultorio);

    void edit(SolicitudConsultorio solicitudConsultorio);

    void remove(SolicitudConsultorio solicitudConsultorio);

    SolicitudConsultorio find(Object id);

    List<SolicitudConsultorio> findAll();

    List<SolicitudConsultorio> findRange(int[] range);

    int count();
    
}
