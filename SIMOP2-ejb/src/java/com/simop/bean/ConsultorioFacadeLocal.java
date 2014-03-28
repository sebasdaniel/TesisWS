/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.bean;

import com.simop.jpa.Consultorio;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 53B45
 */
@Local
public interface ConsultorioFacadeLocal {

    void create(Consultorio consultorio);

    void edit(Consultorio consultorio);

    void remove(Consultorio consultorio);

    Consultorio find(Object id);

    List<Consultorio> findAll();

    List<Consultorio> findRange(int[] range);

    int count();
    
}
