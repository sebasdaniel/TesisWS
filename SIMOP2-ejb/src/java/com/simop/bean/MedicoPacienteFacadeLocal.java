/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.bean;

import com.simop.jpa.MedicoPaciente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 53B45
 */
@Local
public interface MedicoPacienteFacadeLocal {

    void create(MedicoPaciente medicoPaciente);

    void edit(MedicoPaciente medicoPaciente);

    void remove(MedicoPaciente medicoPaciente);

    MedicoPaciente find(Object id);

    List<MedicoPaciente> findAll();

    List<MedicoPaciente> findRange(int[] range);

    int count();
    
}
