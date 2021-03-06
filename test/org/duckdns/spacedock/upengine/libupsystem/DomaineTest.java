/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author iconoctopus
 */
public class DomaineTest
{

    @Test
    public void testDomaine()
    {
	Domaine domaine1 = new Domaine(0, 1);
	Domaine domaine2 = new Domaine(8, 5);
	Domaine domaineCac = new Domaine(3, 3);

	Assert.assertEquals(1, domaine1.getRang());
	domaine1.getRangCompetence(2);
	try
	{
	    domaine1.getRangCompetence(3);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	Assert.assertEquals(5, domaine2.getRang());
	domaine2.getRangCompetence(3);
	try
	{
	    domaine2.getRangCompetence(4);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	Assert.assertEquals(3, domaineCac.getRang());
	domaineCac.getRangCompetence(15);
	try
	{
	    domaine2.getRangCompetence(16);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	Domaine domaine3;
	try
	{
	    domaine3 = new Domaine(-1, 1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:indice:-1", e.getMessage());
	}

    }

    //cas nominaux et d'erreur sur paramétres
    @Test
    public void testGetSetRang()
    {
	Domaine domaine = new Domaine(0, 4);

	Assert.assertEquals(4, domaine.getRang());

	domaine.setRang(3);
	Assert.assertEquals(3, domaine.getRang());
	try
	{
	    domaine.setRang(0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:0", e.getMessage());
	}

    }

    //cas d'erreur sur paramétres
    @Test
    public void testEffectuerJetComp()
    {
	Domaine domaine = new Domaine(0, 1);
	//cas d'erreur sur paramétre de compétence
	try
	{
	    domaine.effectuerJetComp(-1, 1, 0, 0, 0, 0, false);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:trait:1 domaine:1 indice compétence:-1", e.getMessage());
	}

	//cas d'erreur sur paramétre de trait
	try
	{
	    domaine.effectuerJetComp(1, -1, 0, 0, 0, 0, false);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:trait:-1 domaine:1 indice compétence:1", e.getMessage());
	}
    }
}
