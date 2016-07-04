package org.duckdns.spacedock.upengine.libupsystem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumMap;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

//TODO : vu la taille de cette classe il pourrait s'avérer judicieux de la splitter en plusieurs (peut être système, armes et armures) et, si ses méthodes deviennent publiques (probable), de carrément la mettre dans son propre sous package
//TODO : pour plus de propreté objet, rendre privée getPtsArmureEffectifs et l'appeler depuis les méthodes finales que sont getArmureRedDegats et getArmureBonusND (comme pour getRang) : le calcul devrait être fait ici et pas dans la classe armure (dont le code devrait du coup être importé ici)
/**
 * Classe permetant l'accès aux éléments de référence du UP!System
 *
 * @author iconoctopus
 */
final class UPReference
{

    /**
     * instance unique de cet objet
     */
    private static UPReference m_instance;

    /**
     * table des réductions de dégâts, indexée par le rang d'armure (issu du
     * tableau des rangs)
     */
    private final JsonArray m_tableArmureRedDegats;
    /**
     * table des bonus au ND, indexée par le rang d'armure (issu du tableau des
     * rangs)
     */
    private final JsonArray m_tableArmureBonusND;
    /**
     * table des rangs d'armure, à chaque rang (indice) correspond un seuil de
     * points d'armure à atteindre
     */
    private final JsonArray m_tableArmureRangs;
    /**
     * table des modificateurs à l'init issus de la coordination
     */
    private final JsonArray m_tableInitCoord;
    /**
     * table des modificateurs à l'init issus du mental
     */
    private final JsonArray m_tableInitMental;
    /**
     * table des libellés des traits
     */
    private final JsonArray m_tableTraits;
    /**
     * table des autres libellés
     */
    public final CollectionLibelles libelles;
    /**
     * préfixe des libellés des compétences d'attaque
     */
    private final String m_lblCompAttaque;
    /**
     * préfixe des libellés des compétences de parade
     */
    private final String m_lblCompParade;
    /**
     * préfixe des libellés des compétences d'arts et métiers
     */
    private final String m_lblCompMetier;
    /**
     * tableau contenant tous les domaines et leurs compétences non free form,
     */
    final JsonArray m_arbreDomaines;
    /**
     * tableau contenant toutes les armes existant en jeu
     */
    final JsonArray m_tabArmes;
    /**
     * table des ajustemnts à effectuer sur les points d'armure pour adapter un
     * type d'armure à un type d'arme. C'est un tableau de tableaux (un pour
     * chaque type d'armure), chaque tableau interne est indicé par les types
     * d'armes
     */
    private final JsonArray m_tableAjustementArmure;
    /**
     * liste des libellés des types d'armes
     */
    private final JsonArray m_listLblTypArm;
    /**
     * liste des libellés des catégories d'armes de corps à corps
     */
    private final JsonArray m_listLblCatArmCaC;
    /**
     * liste des libellés des catégories d'armes à distance
     */
    private final JsonArray m_listLblCatArmDist;
    /**
     * liste des libellés des modes d'attaque des armes
     */
    private final JsonArray m_listLblModArm;
    /**
     * tableau des pièces d'armure
     */
    private final JsonArray m_tabPiecesArmures;
    /**
     * list des libellés des matériaux d'armures
     */
    private final JsonArray m_listLblMatArmures;
    /**
     * liste de libellés des types d'armures
     */
    private final JsonArray m_listLblTypArmures;
    /**
     * table de tous les boucliers
     */
    private final JsonArray m_tabBoucliers;
    /**
     * liste des libellés des matériaux des boucliers
     */
    private final JsonArray m_listLblMatBoucliers;

    /**
     * pseudo constructeur statique renvoyant l'instance unique et la
     * construisant si absente
     *
     * @return
     */
    static UPReference getInstance()
    {
	if (m_instance == null)
	{
	    m_instance = new UPReference();
	}
	return (m_instance);
    }

    /**
     * véritable constructeur privé effectuant tous les accès fichiers à
     * l'instanciation afin de limiter les temps de latence à une grosse fois
     */
    private UPReference()
    {
	JsonObject object;

	//chargement des règles d'armure
	object = loadJsonFile("JSON/equipement/caracs_armures.json");
	m_tableArmureBonusND = object.getJsonArray("bonusND");
	m_tableArmureRedDegats = object.getJsonArray("red_degats");
	m_tableArmureRangs = object.getJsonArray("rangs");
	m_tableAjustementArmure = object.getJsonArray("ajustements");
	m_tabPiecesArmures = object.getJsonArray("pieces");
	m_listLblMatArmures = object.getJsonArray("materiaux_armures");
	m_listLblTypArmures = object.getJsonArray("types_armures");
	m_tabBoucliers = object.getJsonArray("boucliers");
	m_listLblMatBoucliers = object.getJsonArray("materiaux_boucliers");

	//chargement des règles de calcul de l'initiative
	object = loadJsonFile("JSON/tables_systeme/tab_init.json");
	m_tableInitCoord = object.getJsonArray("coordination");
	m_tableInitMental = object.getJsonArray("mental");

	//chargement des libellés des caractéristiques particulières
	object = loadJsonFile("JSON/tables_systeme/tab_caracs.json");
	m_tableTraits = object.getJsonArray("traits");
	m_lblCompAttaque = object.getString("lbl_attaque");
	m_lblCompParade = object.getString("lbl_parade");
	m_lblCompMetier = object.getString("lbl_metier");

	//chargement de l'arbre des domaines et competences
	m_arbreDomaines = object.getJsonArray("arbre_domaines");

	//chargement des libellés divers
	object = loadJsonFile("JSON/tables_systeme/tab_libelles.json");
	libelles = new CollectionLibelles(object);

	//chargement ded règles des armes
	object = loadJsonFile("JSON/equipement/caracs_armes.json");
	m_tabArmes = object.getJsonArray("armes");
	m_listLblTypArm = object.getJsonArray("types_armes");
	m_listLblCatArmCaC = object.getJsonArray("cat_armes_cac");
	m_listLblCatArmDist = object.getJsonArray("cat_armes_dist");
	m_listLblModArm = object.getJsonArray("mod_armes");
    }

    /**
     *
     * @param p_filePath chemin de package vers le fichier JSON
     * @return un objet JSON lu dans le fichier
     */
    private JsonObject loadJsonFile(String p_filePath)
    {
	InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(p_filePath);//on utilise le classloader du thread et pas de la classe pour plus de sûreté
	JsonReader reader = Json.createReader(in);
	return (reader.readObject());
    }

    /**
     *
     * @param p_coordination la valeur de coordination
     * @return le modificateur de coordination à l'initiative
     */
    int getInitModCoord(int p_coordination)
    {

//le tableau est indexé à partir de 0, pas la coordination
	return m_tableInitCoord.getInt(p_coordination);

    }

    /**
     *
     * @param p_mental la valeur de mental
     * @return le modificateur à l'initiative issu du mental
     */
    int getInitModMental(int p_mental)
    {

	//le tableau est indexé à partir de 0, pas le mental
	return m_tableInitMental.getInt(p_mental);

    }

    /**
     *
     * @param p_points les points d'armure
     * @return le rang d'armure
     */
    private int getRang(int p_points)
    {
	int res = 0;

	int i = -1;//on commence avec i en dehors du tableau (0 points d'armure, pas de bonus) et l'on teste si on peut l'augmenter, quand on ne peut plus l'augmenter on le renvoie.
	while (i <= 4 && p_points >= m_tableArmureRangs.getInt(i + 1))
	{
	    ++i;
	}
	res = i;

	return (res);
    }

    /**
     *
     * @param p_points les points d'armure
     * @return la réduction de dégâts offerte par l'armure
     */
    int getArmureRedDegats(int p_points)
    {
	int resultat = 0;

	int rang = getRang(p_points);
	if (rang >= 0)//impossible si l'on est arrivé jusque là mais on ne sait jamais
	{
	    resultat = m_tableArmureRedDegats.getInt(rang);
	}

	return resultat;
    }

    /**
     *
     * @param p_points les points d'armure
     * @return le bonus au ND offert par l'armure
     */
    int getArmureBonusND(int p_points)
    {
	int resultat = 0;

	int rang = getRang(p_points);
	if (rang >= 0)//impossible si l'on est arrivé jusque là mais on ne sait jamais
	{
	    resultat = m_tableArmureBonusND.getInt(rang);
	}
	return resultat;
    }

    /**
     *
     * @param p_typeArme type d'arme considéré
     * @param p_typeArmure type d'armure considéré
     * @return les points d'armure à effectivement utiliser face à un type
     * d'arme donné en fonction du type d'armure
     */
    int getPtsArmureEffectifs(int p_typeArme, int p_typeArmure, int p_nbPts)
    {
	double coeff = 0;

	JsonArray tabPourType = m_tableAjustementArmure.getJsonArray(p_typeArmure);
	coeff = tabPourType.getJsonNumber(p_typeArme).doubleValue();

	double preResult = (((double) p_nbPts) * coeff);
	long preIntResult = Math.round(preResult);//TODO partout ailleurs le simple cast vers int ne fait que tronquer, remplacer toutes ses occurences par un appel à math.round() comme ici
	return (int) preIntResult;
    }

    /**
     *
     * @param p_indice l'indice du trait tel que défini dan le fichier JSON
     * @return le libelle du trait indicé
     */
    public String getLibelleTrait(int p_indice)
    {
	return m_tableTraits.getString(p_indice);

    }

    /**
     *
     * @param p_indice
     * @return le libellé d'une arme
     */
    String getLblArme(int p_indice)
    {

	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getString("nom");
    }

    /**
     *
     * @param p_indice
     * @return le nb de dés lancés par une arme
     */
    int getNbLancesArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("lance");

    }

    /**
     *
     * @param p_indice
     * @return le nombre de dés gardés d'une arme
     */
    int getNbGardesArme(int p_indice)
    {

	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("garde");

    }

    /**
     *
     * @param p_indice
     * @return le bonus d'init de l'arme
     */
    int getBonusInitArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("bonus_init");
    }

    /**
     *
     * @param p_indice
     * @return le malus aux jets d'attaque de l'arme
     */
    int getMalusAttaqueArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("malus_attaque");
    }

    /**
     *
     * @param p_indice
     * @return le physique minimal de l'arme
     */
    int getPhysMinArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("physique_minimal");
    }

    /**
     *
     * @param p_indice
     * @return l'indice de catégorie de l'arme
     */
    int getCategorieArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("categorie");
    }

    /**
     *
     * @param p_indice
     * @return l'indice du type de l'arme
     */
    int getTypeArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("type");
    }

    /**
     *
     * @param p_indice
     * @return le libellé d'un type d'arme représenté par son index
     */
    String getLblTypeArme(int p_indice)
    {
	return m_listLblTypArm.getString(p_indice);
    }

    /**
     *
     * @param p_indice
     * @return le mode d'attaque d'une arme représentée par son index
     */
    int getModArme(int p_indice)
    {
	return m_tabArmes.getJsonObject(p_indice).getInt("mode");
    }

    /**
     *
     * @param p_indice
     * @return le libellé du mode d'attaque représenté par son index
     */
    String getLblModArme(int p_indice)
    {
	return m_listLblModArm.getString(p_indice);
    }

    /**
     * renvoie le libellé d'une catégorie d'arme de corps à corps
     *
     * @param p_indice
     * @return
     */
    String getLblCatArmeCaC(int p_indice)
    {
	return m_listLblCatArmCaC.getString(p_indice);
    }

    /**
     * renvoie le libellé d'une catégorie d'arme à distance
     *
     * @param p_indice
     * @return
     */
    String getLblCatArmeDist(int p_indice)
    {
	return m_listLblCatArmDist.getString(p_indice);
    }

    /**
     * renvoie le malus à l'attaque à courte portée de l'arme
     *
     * @param p_indice
     * @return
     */
    int getMalusCourtArme(int p_indice)
    {
	return m_tabArmes.getJsonObject(p_indice).getInt("malus_court");
    }

    /**
     * renvoie le malus à l'attaque à longue portée de l'arme
     *
     * @param p_indice
     * @return
     */
    int getMalusLongArme(int p_indice)
    {
	return m_tabArmes.getJsonObject(p_indice).getInt("malus_long");
    }

    /**
     * renvoie la portée de l'arme
     *
     * @param p_indice
     * @return
     */
    int getPorteeArme(int p_indice)
    {
	return m_tabArmes.getJsonObject(p_indice).getInt("portee");
    }

    /**
     * renvoie le nombre d'actions nécessaire au rechargement de l'arme
     *
     * @param p_indice
     * @return
     */
    int getNbActionsRechargeArme(int p_indice)
    {
	return m_tabArmes.getJsonObject(p_indice).getInt("actions_recharge");
    }

    /**
     * renvoie le max de munitions au sein de l'arme
     *
     * @param p_indice
     * @return
     */
    int getMagasinArme(int p_indice)
    {
	return m_tabArmes.getJsonObject(p_indice).getInt("magasin");
    }

    /**
     *
     * @param p_bouclier
     * @param p_materiau
     * @return les points d'armure du bouclier de type et matériau spécifiés
     */
    int getNbPointsBouclier(int p_bouclier, int p_materiau)
    {
	JsonObject objetIntermediaire = m_tabBoucliers.getJsonObject(p_bouclier);
	JsonArray tabIntermediaire = objetIntermediaire.getJsonArray("points");
	return tabIntermediaire.getInt(p_materiau);
    }

    /**
     *
     * @param p_indice
     * @return le label du matériau de bouclier de type spécifié
     */
    String getLblMatBouclier(int p_indice)
    {
	return m_listLblMatBoucliers.getString(p_indice);

    }

    /**
     * On utilise plutôt getLblComp pour récupérer un libellé de comp. Cette
     * compétence sert surtout à récupérer le nombre de comps d'un domaine (avec
     * la size de la liste). Mais autant renvoyer un libellé au passage.
     *
     * @param p_indice
     * @return la liste des libellés des compétences d'un domaine donné par son
     * indice avec un traitement spécial pour le corps à corps où il y a juste
     * la liste des catégories d'armes
     */
    ArrayList<String> getListComp(int p_indice)
    {
	ArrayList<String> res = new ArrayList<>();

	if (p_indice == 3)//domaine corps à corps : on renvoie la liste des catégories d'armes de corps à corps
	{
	    for (int i = 0; i < m_listLblCatArmCaC.size(); ++i)
	    {
		res.add(m_listLblCatArmCaC.getString(i));
	    }
	}
	else
	{
	    if (p_indice == 4)//domaine combat à distance : on renvoie la liste des catégories d'armes à distance
	    {
		for (int i = 0; i < m_listLblCatArmDist.size(); ++i)
		{
		    res.add(m_listLblCatArmDist.getString(i));
		}
	    }
	    else
	    {
		JsonObject domaine = m_arbreDomaines.getJsonObject(p_indice);
		JsonArray tabComp = domaine.getJsonArray("comps");

		for (int i = 0; i < tabComp.size(); ++i)
		{
		    res.add(tabComp.getString(i));
		}
	    }
	}
	return res;
    }

    /**
     *
     * @param p_indice
     * @return le libellé d'un domaine
     */
    String getLblDomaine(int p_indice)
    {
	JsonObject domaine = m_arbreDomaines.getJsonObject(p_indice);
	return domaine.getString("lbl");
    }

    /**
     * préférer cette méthode à getListComp pour la récupération d'un libellé de
     * comp : on pourrait très bien refactorer getListComp pour renvoyer un
     * simple entier dans le futur et aucune classe ne devrait se baser dessus
     *
     *
     * @param p_indiceDomaine
     * @param p_indiceComp
     * @return le libellé d'une compétence
     */
    String getLblComp(int p_indiceDomaine, int p_indiceComp)
    {//TODO si domaine CaC ou CaD appeler la liste des catégories
	JsonObject domaine = m_arbreDomaines.getJsonObject(p_indiceDomaine);
	JsonArray tabComp = domaine.getJsonArray("comps");
	return tabComp.getString(p_indiceComp);
    }

    /**
     *
     * @param p_idPiece
     * @param p_materiau
     * @return le nombre de points d'armure d'une pièce d'un matériau spécifiés
     * par leurs indices
     */
    int getPtsArmure(int p_idPiece, int p_materiau)
    {
	JsonObject piece = m_tabPiecesArmures.getJsonObject(p_idPiece);
	JsonArray tabPoints = piece.getJsonArray("points");
	return tabPoints.getInt(p_materiau);
    }

    /**
     *
     * @param p_indice
     * @return le libellé d'un matériau d'armure
     */
    String getLblMateriauArmure(int p_indice)
    {
	return m_listLblMatArmures.getString(p_indice);
    }

    /**
     *
     * @param p_indice
     * @return le libllé d'un type d'armure
     */
    String getLblTypeArmure(int p_indice)
    {

	return m_listLblTypArmures.getString(p_indice);

    }

    /**
     *
     * @param p_indice
     * @return le libellé d'une pièce d'armure
     */
    String getLblPiece(int p_indice)
    {
	JsonObject piece = m_tabPiecesArmures.getJsonObject(p_indice);
	return piece.getString("lbl");
    }

    /**
     *
     * @param p_indicePiece
     * @param p_materiau
     * @return le malus d'esquive d'une pièce d'un matériau donné
     */
    int getMalusEsquive(int p_indicePiece, int p_materiau)
    {
	JsonObject piece = m_tabPiecesArmures.getJsonObject(p_indicePiece);
	JsonArray tabMalus = piece.getJsonArray("malus_esquive");
	return tabMalus.getInt(p_materiau);
    }

    /**
     *
     * @param p_indicePiece
     * @param p_materiau
     * @return le malus de parade d'une pièce d'un matériau donné
     */
    int getMalusParade(int p_indicePiece, int p_materiau)
    {
	JsonObject piece = m_tabPiecesArmures.getJsonObject(p_indicePiece);
	JsonArray tabMalus = piece.getJsonArray("malus_parade");
	return tabMalus.getInt(p_materiau);

    }

    /**
     *
     * @param p_indice
     * @return le nb max de pièces d'une sorte
     */
    int getNbMaxPieces(int p_indice)
    {
	JsonObject piece = m_tabPiecesArmures.getJsonObject(p_indice);
	return piece.getInt("nbmax");
    }

    /**
     * Classe encapsulant les libellés autres que ceux utilisés dans les
     * caractéristiques et l'équipement (surtout utilisé pour l'entrée sortie)
     */
    final class CollectionLibelles
    {

	public final String typarm;
	public final String typarmure;
	public final String ptsarmure;
	public final String trait;
	public final String interArme;
	public final String liaison;
	public final String addition;
	public final String qualite;
	public final String equilibrage;

	public final EnumMap libQualite;
	public final EnumMap libEquilibrage;

	CollectionLibelles(JsonObject p_libelles)
	{
	    if (p_libelles != null)
	    {
		typarm = p_libelles.getString("typarm");
		typarmure = p_libelles.getString("typarmure");
		ptsarmure = p_libelles.getString("ptsarmure");
		trait = p_libelles.getString("trait");
		interArme = p_libelles.getString("interArme");
		liaison = p_libelles.getString("liaison_standard");
		addition = p_libelles.getString("liaison_addition");
		qualite = p_libelles.getString("qualite");
		equilibrage = p_libelles.getString("equilibrage");
		JsonArray tabQualite = p_libelles.getJsonArray("lib_qualite");
		libQualite = new EnumMap(Arme.QualiteArme.class);
		libQualite.put(Arme.QualiteArme.inferieure, tabQualite.getString(0));
		libQualite.put(Arme.QualiteArme.moyenne, tabQualite.getString(1));
		libQualite.put(Arme.QualiteArme.superieure, tabQualite.getString(2));
		libQualite.put(Arme.QualiteArme.maitre, tabQualite.getString(3));

		JsonArray tabEquilibrage = p_libelles.getJsonArray("lib_equilibrage");
		libEquilibrage = new EnumMap(Arme.EquilibrageArme.class);
		libEquilibrage.put(Arme.EquilibrageArme.mauvais, tabEquilibrage.getString(0));
		libEquilibrage.put(Arme.EquilibrageArme.normal, tabEquilibrage.getString(1));
		libEquilibrage.put(Arme.EquilibrageArme.bon, tabEquilibrage.getString(2));
	    }
	    else
	    {
		typarm = "typarm";
		typarmure = "typarmure";
		ptsarmure = "ptsarmure";
		trait = "trait";
		interArme = "interArme";
		liaison = "de";
		addition = "et";
		qualite = "qualite";
		equilibrage = "equilibrage";
		libQualite = new EnumMap(Arme.QualiteArme.class);
		libQualite.put(Arme.QualiteArme.inferieure, "inf");
		libQualite.put(Arme.QualiteArme.moyenne, "moy");
		libQualite.put(Arme.QualiteArme.superieure, "sup");
		libQualite.put(Arme.QualiteArme.maitre, "maitre");
		libEquilibrage = new EnumMap(Arme.EquilibrageArme.class);
		libEquilibrage.put(Arme.EquilibrageArme.mauvais, "mauvais");
		libEquilibrage.put(Arme.EquilibrageArme.normal, "normal");
		libEquilibrage.put(Arme.EquilibrageArme.bon, "bon");

	    }
	}
    }
}
