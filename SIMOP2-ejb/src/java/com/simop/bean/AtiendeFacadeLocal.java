/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.bean;

import com.simop.jpa.Atiende;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 53B45
 */
@Local
public interface AtiendeFacadeLocal {

    void create(Atiende atiende);

    void edit(Atiende atiende);

    void remove(Atiende atiende);

    Atiende find(Object id);

    List<Atiende> findAll();

    List<Atiende> findRange(int[] range);

    int count();
    
}
