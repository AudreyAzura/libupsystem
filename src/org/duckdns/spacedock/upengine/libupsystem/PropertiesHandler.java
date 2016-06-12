package org.duckdns.spacedock.upengine.libupsystem;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe chargeant les fichiers de propriétés (notamment strings) et rendant
 * accessibles les informations de configuration au reste du package
 *
 * @author iconoctopus
 */
final class PropertiesHandler
{

    /**
     * message d'erreur par défaut pour les paramétres inférieurs à 0
     */
    private final static String m_propertiesErrorMessage = "erreur d'accès à un fichier de propriétés";

    /**
     * si les propriétés des exceptions ont pu être récupérées
     */
    private boolean m_exceptionsRecovered;

    /**
     * instance statique unique
     */
    private static PropertiesHandler m_instance;

    /**
     * Textes des exceptions
     */
    private final Properties m_exceptionProperties;

    /**
     * pseudo-constructeur statique permettant d'accéder à l'instance (la crée
     * si elle n'existe pas)
     *
     * @return l'instance unique
     * @throws FileNotFoundException
     * @throws IOException
     */
    static PropertiesHandler getInstance()
    {
	if(m_instance == null)
	{
	    m_instance = new PropertiesHandler();
	}

	return (m_instance);
    }

    /**
     * véritable contructeur, appelé par getInstance() si l'instance n'existe
     * pas. Construit les objets properties à partir des fichiers idoines afin
     * qu'il ne soit pas nécessaire de le faire de façon synchrone durant le
     * reste de l'exécution
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private PropertiesHandler()
    {
	m_exceptionProperties = new Properties();

	//InputStream in = PropertiesHandler.class.getClassLoader().getResourceAsStream("strings/exceptions.properties");
	/*on utilise le classloader pour récupérer le fichier de propriétés ailleurs que dans le même package : il utilise le classpath.
	 *On utilise le classloader du thread afin d'être davantage sur qu'il explorera tout le classpath, contrairement au classloader
	 *de la classe (utilisé dans le bout de code commenté ci-dessus). J'ignore si cela marche bien avec les threads android.
	 */
	InputStream in;

	try
	{

	    in = Thread.currentThread().getContextClassLoader().getResourceAsStream("strings/exceptions.properties");
	    m_exceptionProperties.load(in);
	    in.close();
	    m_exceptionsRecovered = true;
	}
	catch(IOException e)
	{
	    m_exceptionsRecovered = false;
	}

    }

    /**
     *
     * @param p_property
     * @return le message d'erreur idoine
     */
    String getErrorMessage(String p_property)
    {
	String result;
	if(m_exceptionsRecovered)
	{
	    result = m_exceptionProperties.getProperty(p_property);
	}
	else
	{
	    result = m_propertiesErrorMessage;
	}
	return result;
    }
}