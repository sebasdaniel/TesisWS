/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.bean;

import com.simop.jpa.Antecedente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 53B45
 */
@Local
public interface AntecedenteFacadeLocal {

    void create(Antecedente antecedente);

    void edit(Antecedente antecedente);

    void remove(Antecedente antecedente);

    Antecedente find(Object id);

    List<Antecedente> findAll();

    List<Antecedente> findRange(int[] range);

    int count();
    
}
