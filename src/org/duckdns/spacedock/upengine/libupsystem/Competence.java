package org.duckdns.spacedock.upengine.libupsystem;

/**
 * classe représentant une compétence
 *
 * @author iconoctopus
 */
class Competence
{//TODO : gérer les spécialités via un tableau les contenant

    /**
     * le rang de la compétence, déclenche des jets spéciaux à 3 et 5
     */
    private int m_rang;

    int getRang()
    {
	return m_rang;
    }

    /**
     * compte comme rang d'attaque dans les CompCac
     *
     * @param p_rang
     */
    final void setRang(int p_rang)
    {
	if (p_rang >= 0)
	{
	    m_rang = p_rang;
	}
	else
	{
	    ErrorHandler.paramAberrant("rang:" + p_rang);
	}
    }

    /**
     * pour l'instant la compétence ignore son nom car il est fourni par les
     * classes plus élevées en fonction de son indice. Pour l'instant cette
     * classe est inutile : elle n'encapsule qu'un entier, mais à terme elle
     * pourra contenir un tableau de spécalités. Elle est déjà utilisée pour
     * dériver CompCac qui encapsule l'attaque et la parade avec une catégorie
     * d'arme donnée
     *
     *
     * @param p_rang
     */
    Competence(int p_rang)
    {
	setRang(p_rang);
    }
}
