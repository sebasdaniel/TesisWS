/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.bean;

import com.simop.jpa.Tip;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 53B45
 */
@Local
public interface TipFacadeLocal {

    void create(Tip tip);

    void edit(Tip tip);

    void remove(Tip tip);

    Tip find(Object id);

    List<Tip> findAll();

    List<Tip> findRange(int[] range);

    int count();
    
}
