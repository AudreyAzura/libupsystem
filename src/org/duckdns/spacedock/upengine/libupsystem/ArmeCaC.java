/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.duckdns.spacedock.commonutils.ErrorHandler;
import org.duckdns.spacedock.commonutils.PropertiesHandler;

/**
 *
 * @author iconoctopus
 */
public class ArmeCaC extends Arme
{

    /**
     * constructeur identique à celui de la superclasse
     *
     * @param p_indice
     * @param p_qualite
     * @param p_equilibrage
     */
    public ArmeCaC(int p_indice, QualiteArme p_qualite, EquilibrageArme p_equilibrage)
    {
	super(p_indice, p_qualite, p_equilibrage);
    }

    /**
     * génère les dégâts infligés avec cette arme en combat au corps à corps
     *
     * @param p_nbIncrements
     * @param p_physique le physique du personnage
     * @param p_isSonne
     * @return
     */
    Degats genererDegats(int p_nbIncrements, int p_physique, boolean p_isSonne)
    {
	int degatsBruts = 0;
	if (p_nbIncrements >= 0 && p_physique >= 0)
	{
	    degatsBruts = (RollUtils.lancer(super.getDesLances() + p_nbIncrements + p_physique, super.getDesGardes(), p_isSonne));
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("increments") + ":" + p_nbIncrements + " " + PropertiesHandler.getInstance("libupsystem").getString("physique") + ":" + p_physique);
	}
	return new Degats(degatsBruts, super.getTypeArme());
    }
}
