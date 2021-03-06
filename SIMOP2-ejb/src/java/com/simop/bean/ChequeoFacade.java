/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.bean;

import com.simop.jpa.Chequeo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author 53B45
 */
@Stateless
public class ChequeoFacade extends AbstractFacade<Chequeo> implements ChequeoFacadeLocal {
    @PersistenceContext(unitName = "SIMOP2-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChequeoFacade() {
        super(Chequeo.class);
    }
    
}
