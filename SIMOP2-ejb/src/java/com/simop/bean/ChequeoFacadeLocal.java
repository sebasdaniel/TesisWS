/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.bean;

import com.simop.jpa.Chequeo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 53B45
 */
@Local
public interface ChequeoFacadeLocal {

    void create(Chequeo chequeo);

    void edit(Chequeo chequeo);

    void remove(Chequeo chequeo);

    Chequeo find(Object id);

    List<Chequeo> findAll();

    List<Chequeo> findRange(int[] range);

    int count();
    
}
