/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luisgm.projektgb.controll;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.luisgm.projektgb.model.Competence;

/**
 *
 * @author luisgm
 */
@Stateless
public class CompetenceFacade extends AbstractFacade<Competence> {
    @PersistenceContext(unitName = "org.luisgm_ProjektGB_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompetenceFacade() {
        super(Competence.class);
    }
    
}
