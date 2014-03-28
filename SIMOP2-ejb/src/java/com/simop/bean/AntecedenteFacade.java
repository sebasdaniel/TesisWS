/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.bean;

import com.simop.jpa.Antecedente;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author 53B45
 */
@Stateless
public class AntecedenteFacade extends AbstractFacade<Antecedente> implements AntecedenteFacadeLocal {
    @PersistenceContext(unitName = "SIMOP2-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AntecedenteFacade() {
        super(Antecedente.class);
    }
    
}
