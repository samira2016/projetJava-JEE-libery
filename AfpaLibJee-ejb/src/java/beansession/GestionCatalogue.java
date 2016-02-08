package beansession;

//import com.sun.faces.util.CollectionsUtils;
import entites.Auteur;
import entites.Editeur;
import entites.MotCles;
import entites.Ouvrage;
import entites.TVA;
import entites.Theme;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class GestionCatalogue implements GestionCatalogueLocal {

    @PersistenceContext(unitName = "AfpaLibJee-ejbPU")
    private EntityManager em;

    @Override
    public void supprimerDonnees() {
        String req = "delete from Ouvrage o";
        Query qr = em.createQuery(req);
        qr.executeUpdate();
    }

    @Override
    public void creerDonneesEli() {
        Ouvrage p01 = new Ouvrage("a", "ref45001111", 2.5F, 10);
        Ouvrage p02 = new Ouvrage("b", "ref45002222", 1.4F, 8);
        Ouvrage p03 = new Ouvrage("c", "ref45003333", 3.6F, 9);
        Ouvrage p04 = new Ouvrage("d", "ref45004444", 4F, 6);
        Ouvrage p05 = new Ouvrage("e", "ref45005555", 3.65F, 11);
        Ouvrage p06 = new Ouvrage("f", "ref45006666", 5.55F, 13);

        em.persist(p01);
        em.persist(p02);
        em.persist(p03);
        em.persist(p04);
        em.persist(p05);
        em.persist(p06);
    }

    @Override
    public void resetStock() {
        /*
         Ouvrage p01 = em.find(Ouvrage.class, "ref45001111");
         Ouvrage p02 = em.find(Ouvrage.class, "ref45002222");
         Ouvrage p03 = em.find(Ouvrage.class, "ref45003333");
         Ouvrage p04 = em.find(Ouvrage.class, "ref45004444");
         Ouvrage p05 = em.find(Ouvrage.class, "ref45005555");
         Ouvrage p06 = em.find(Ouvrage.class, "ref45006666");
         */
        Ouvrage p01 = selectOuvrage("ref45001111");
        Ouvrage p02 = selectOuvrage("ref45002222");
        Ouvrage p03 = selectOuvrage("ref45003333");
        Ouvrage p04 = selectOuvrage("ref45004444");
        Ouvrage p05 = selectOuvrage("ref45005555");
        Ouvrage p06 = selectOuvrage("ref45006666");

        p01.setStock(10);
        p02.setStock(8);
        p03.setStock(9);
        p04.setStock(6);
        p05.setStock(11);
        p06.setStock(13);

        em.persist(p01);
        em.persist(p02);
        em.persist(p03);
        em.persist(p04);
        em.persist(p05);
        em.persist(p06);
    }

    @Override
    public List<Ouvrage> selectAllOuvrage() {
        String req = "select o from Ouvrage o";
        Query qr = em.createQuery(req);
        List<Ouvrage> lp = qr.getResultList();
        return lp;
    }

    @Override
    public Long countOuvrage(String isbn, String theme, String titre, String motClef, String auteur) {
        String auteur01 = "";
        String titre01 = "";
        String req = "select COUNT(o) from Ouvrage o ";
        // Traitement des espaces 
        if (isbn != null) {
            isbn = isbn.trim();
        }
        if (theme != null) {
            theme = theme.trim();
        }
        if (titre != null && !titre.isEmpty()) {
            for (int i = 0; i < titre.length(); i++) {
                if (titre.charAt(i) == ' ') {
                    titre01 += '%';
                } else {
                    titre01 += titre.charAt(i);
                }
            }
        }
        if (motClef != null) {
            motClef = motClef.trim();
        }
        if (auteur != null) {
            //auteur = auteur.trim();
            for (int i = 0; i < auteur.length(); i++) {
                if (auteur.charAt(i) == ' ') {
                    auteur01 += '%';
                } else {
                    auteur01 += auteur.charAt(i);
                }
            }
        }
        // Les jointures 
        if (!theme.isEmpty()) {
            req += " JOIN o.themes t ";
        }
        if (!auteur.isEmpty()) {
            req += " JOIN o.auteurs a ";
        }
        if (!motClef.isEmpty()) {
            req += " JOIN o.motsCle m ";
        }
        if (!isbn.isEmpty() || !theme.isEmpty() || !auteur.isEmpty() || !motClef.isEmpty() || !titre.isEmpty()) {
            req += " WHERE ";
        }
        //Ajout des conditions WHERE
        if (!isbn.isEmpty()) {
            req += " o.isbn=:paramISBN AND ";

        }
        if (!theme.isEmpty()) {
            req += " t.theme LIKE :paramTheme AND ";
        }

        if (!titre.isEmpty()) {
            req += " o.titre LIKE :paramTitre OR o.titre LIKE :paramTitre02 AND ";
        }
        if (!auteur.isEmpty()) {
            req += " a.nomAuteur LIKE :paramAuteur  OR a.prenomAuteur LIKE :paramAuteur OR a.nomAuteur+a.prenomAuteur LIKE :paramAuteur02 AND ";
        }
        if (!motClef.isEmpty()) {
            req += " m.motCle=:paramMotClef AND ";
        }
        // Enelever le dernier AND 
        if (req.contains("AND")) {
            req = req.substring(0, req.length() - 4);
        }
        Query qr = em.createQuery(req);
        //set des paramètres de la requête
        if (!isbn.isEmpty()) {
            qr.setParameter("paramISBN", isbn);
        }
        if (!theme.isEmpty()) {
            qr.setParameter("paramTheme", "%" + theme + "%");
        }
        if (!titre.isEmpty()) {
            qr.setParameter("paramTitre", "%" + titre + "%");
            qr.setParameter("paramTitre02", "%" + titre01 + "%");
        }
        if (!auteur.isEmpty()) {
            qr.setParameter("paramAuteur", "%" + auteur + "%");
            qr.setParameter("paramAuteur02", "%" + auteur01 + "%");
        }
        if (!motClef.isEmpty()) {
            qr.setParameter("paramMotClef", motClef);
        }
        return (Long) qr.getSingleResult();
    }

    @Override
    public Long countOuvrage(String isbn, String theme, String titre, String motClef, String auteur, String critere) {
        if (critere == null || critere.trim().isEmpty()) {
            return countOuvrage(isbn, theme, titre, motClef, auteur);
        } else {
            return CountrechercheGeneral(critere);

            //return CountrechercheFiltrer(isbn, theme, titre, motClef, auteur, list);
        }
    }
    
    public boolean exist(ArrayList<Ouvrage> entree,String isbnOuvrage){
        boolean result = false; 
        for( Ouvrage ouv : entree){
            if (ouv.getIsbn().equalsIgnoreCase(isbnOuvrage)){
                result=true; 
                return result; 
            }
        }
        return result;
    }

    public Long CountrechercheGeneral(String critere) {
       List<Ouvrage> result = null;
       
        ArrayList<Ouvrage> resultatFinal = new ArrayList();
         String critere01 = "";
        if (critere != null && !critere.isEmpty()) {
            for (int i = 0; i < critere.length(); i++) {
                if (critere.charAt(i) == ' ') {
                    critere01 += '%';
                } else {
                    critere01 += critere.charAt(i);
                }
            }
        }
        //si le critèere est null or vide on retourne la liste de tous les ouvrages 
        if (critere != null && critere.trim().isEmpty()) {
            String req = "select COUNT(o) from Ouvrage o ";
            Query qr = em.createQuery(req);
            return (Long)qr.getSingleResult();
        } else {
            //Appliquer le critères ISBN
            String req = "select DISTINCT(o) from Ouvrage o ";
            req += " WHERE o.isbn=:param ";
            Query qr = em.createQuery(req);
            qr.setParameter("param", critere);
            result = (List<Ouvrage>) qr.getResultList();
            if (result != null) {
                for (Ouvrage ouv : result) {
                    if (!exist(resultatFinal , ouv.getIsbn())) {
                        resultatFinal.add(ouv);
                    }
                }
            }
            result = null;
            // Appliquer le critère sur le titre ou le sous-titre
            req = "SELECT DISTINCT(o) FROM Ouvrage o WHERE o.titre LIKE :param OR o.sousTitre LIKE :param";
            qr = em.createQuery(req);
            qr.setParameter("param", "%" + critere + "%");
            result = (List<Ouvrage>) qr.getResultList();
            if (result != null) {
                for (Ouvrage ouv : result) {
                    if (!exist(resultatFinal , ouv.getIsbn())) {
                        resultatFinal.add(ouv);
                    }
                }
            }
            result = null;
            //Appliquer le critère sur l'auteur
            req = "SELECT DISTINCT(o) FROM Ouvrage o JOIN o.auteurs a WHERE a.nomAuteur LIKE :param OR a.prenomAuteur LIKE :param OR a.nomAuteur+a.prenomAuteur LIKE :param02";
            qr = em.createQuery(req);
            qr.setParameter("param", "%" + critere + "%");
            qr.setParameter("param02", "%" + critere01 + "%");
            result = (List<Ouvrage>) qr.getResultList();
            if (result != null) {
                for (Ouvrage ouv : result) {
                    if (!exist(resultatFinal , ouv.getIsbn())) {
                        resultatFinal.add(ouv);
                    }
                }
            }
            result = null;
            //Appliquer le critere de theme 
            req = "SELECT DISTINCT(o) FROM Ouvrage o JOIN o.themes t WHERE t.theme LIKE :param";
            qr = em.createQuery(req);
            qr.setParameter("param", "%" + critere + "%");
            result = (List<Ouvrage>) qr.getResultList();
            if (result != null) {
                for (Ouvrage ouv : result) {
                    if (!exist(resultatFinal , ouv.getIsbn())) {
                        resultatFinal.add(ouv);
                    }
                }
            }
            result = null;
            //Appliquer sur résumé mot clé et editeur 
            req = "SELECT DISTINCT(o) FROM Ouvrage o JOIN o.motsCle m JOIN o.editeurs e WHERE m.motCle LIKE :param OR e.nomEditeur LIKE :param ";
            qr = em.createQuery(req);
            qr.setParameter("param", "%" + critere + "%");
            result = (List<Ouvrage>) qr.getResultList();
            if (result != null) {
                for (Ouvrage ouv : result) {
                    if (!exist(resultatFinal , ouv.getIsbn())) {
                        resultatFinal.add(ouv);
                    }
                }
            }
            
            Integer count = resultatFinal.size();
            return count.longValue();
        } 
    }

    @Override
    public Ouvrage selectOuvrage(String ref) {
        String req = "select o from Ouvrage o where o.isbn =:paramRef";
        Query qr = em.createQuery(req);
        qr.setParameter("paramRef", ref);
        return (Ouvrage) qr.getSingleResult();
    }

    @Override
    public List<Ouvrage> rechercher(int debut, int nbrArticle, String isbn, String theme, String titre, String motClef, String auteur, String critere) {
        if (critere == null || critere.trim().isEmpty()) {
            return rechercherParCritere(debut, nbrArticle, isbn, theme, titre, motClef, auteur);
        } else {
            return rechercheGeneral(debut, nbrArticle, critere);
            //return rechercheFiltrer(debut, nbrArticle, isbn, theme, titre, motClef, auteur, listOuvrage);
        }
    }

    // Application des filtres(thème, auteur,isbn, ...)sur résultat  Recherche avec critère générale 
    public List<Ouvrage> rechercherParCritere(int debut, int nbrArticle, String isbn, String theme, String titre, String motClef, String auteur) {
        String auteur01 = "";
        String titre01 = "";
        Query qr = null;
        String req = "select o from Ouvrage o ";
        // Traitement des espaces 
        if (isbn != null) {
            isbn = isbn.trim();
        }
        if (theme != null) {
            theme = theme.trim();
        }
        if (titre != null) {
            for (int i = 0; i < titre.length(); i++) {
                if (titre.charAt(i) == ' ') {
                    titre01 += '%';
                } else {
                    titre01 += titre.charAt(i);
                }
            }
        }
        if (motClef != null) {
            motClef = motClef.trim();
        }
        if (auteur != null && !auteur.isEmpty()) {
            //auteur = auteur.trim();
            for (int i = 0; i < auteur.length(); i++) {
                if (auteur.charAt(i) == ' ') {
                    auteur01 += '%';
                } else {
                    auteur01 += auteur.charAt(i);
                }
            }
        }

        // Les jointures 
        if (!theme.isEmpty()) {
            req += " JOIN o.themes t ";
        }
        if (!auteur.isEmpty()) {
            req += " JOIN o.auteurs a ";
        }
        if (!motClef.isEmpty()) {
            req += " JOIN o.motsCle m ";
        }
        if (!isbn.isEmpty() || !theme.isEmpty() || !auteur.isEmpty() || !motClef.isEmpty() || !titre.isEmpty()) {
            req += " WHERE ";
        }
        //Ajout des conditions WHERE
        if (!isbn.isEmpty()) {
            req += " o.isbn=:paramISBN AND ";

        }
        if (!theme.isEmpty()) {
            req += " t.theme LIKE :paramTheme AND ";
        }

        if (!titre.isEmpty()) {
            req += " o.titre LIKE :paramTitre OR o.titre LIKE :paramTitre02 AND ";
        }
        if (!auteur.isEmpty()) {
            req += " a.nomAuteur LIKE :paramAuteur  OR a.prenomAuteur LIKE :paramAuteur OR a.nomAuteur+a.prenomAuteur LIKE :paramAuteur02 AND ";
        }
        if (!motClef.isEmpty()) {
            req += " m.motCle=:paramMotClef AND ";
        }
        // Enelever le dernier AND 
        if (req.contains("AND")) {
            req = req.substring(0, req.length() - 4);
        }
        qr = em.createQuery(req);
        //set des paramètres de la requête
        if (!isbn.isEmpty()) {
            qr.setParameter("paramISBN", isbn);
        }
        if (!theme.isEmpty()) {
            qr.setParameter("paramTheme", "%" + theme + "%");
        }
        if (!titre.isEmpty()) {
            qr.setParameter("paramTitre", "%" + titre + "%");
            qr.setParameter("paramTitre02", "%" + titre01 + "%");
        }
        if (!auteur.isEmpty()) {
            qr.setParameter("paramAuteur", "%" + auteur + "%");
            qr.setParameter("paramAuteur02", "%" + auteur01 + "%");
        }
        if (!motClef.isEmpty()) {
            qr.setParameter("paramMotClef", motClef);
        }

        qr.setFirstResult(debut);
        qr.setMaxResults(nbrArticle);
        return (List<Ouvrage>) qr.getResultList();
    }

    // Recherche pour récupèrer tous les ouvrages
    public List<Ouvrage> rechercheComplete(int debut, int nbrArticle) {
        String req = "select DISTINCT(o) from Ouvrage o ";
        Query qr = em.createQuery(req);
        qr.setFirstResult(debut);
        qr.setMaxResults(nbrArticle);
        return (List<Ouvrage>) qr.getResultList();
    }

    //Recherche pour récupérer les ouvrages en appliquant le critèr de recherche générale
    public List<Ouvrage> rechercheGeneral(int debut, int nbrArticle, String critere) {
        List<Ouvrage> result = null;
        ArrayList resultatFinal = new ArrayList();
        String critere01 = "";
        if (critere != null && !critere.isEmpty()) {
            for (int i = 0; i < critere.length(); i++) {
                if (critere.charAt(i) == ' ') {
                    critere01 += '%';
                } else {
                    critere01 += critere.charAt(i);
                }
            }
        }
        //si le critèere est null or vide on retourne la liste de tous les ouvrages 
        if (critere != null && critere.trim().isEmpty()) {
            
            return rechercheComplete(debut,nbrArticle);
        } else {
            //Appliquer le critères ISBN
            String req = "select DISTINCT(o) from Ouvrage o ";
            req += " WHERE o.isbn=:param ";
            Query qr = em.createQuery(req);
            qr.setParameter("param", critere);
            result = (List<Ouvrage>) qr.getResultList();
            if (result != null) {
                for (Ouvrage ouv : result) {
                    if (!exist(resultatFinal , ouv.getIsbn())) {
                        resultatFinal.add(ouv);
                    }
                }
            }
            result = null;
            // Appliquer le critère sur le titre ou le sous-titre
            req = "SELECT DISTINCT(o) FROM Ouvrage o WHERE o.titre LIKE :param OR o.sousTitre LIKE :param";
            qr = em.createQuery(req);
            qr.setParameter("param", "%" + critere + "%");
            result = (List<Ouvrage>) qr.getResultList();
            if (result != null) {
                for (Ouvrage ouv : result) {
                    if (!exist(resultatFinal , ouv.getIsbn())) {
                        resultatFinal.add(ouv);
                    }
                }
            }
            result = null;
            //Appliquer le critère sur l'auteur
            req = "SELECT DISTINCT(o) FROM Ouvrage o JOIN o.auteurs a WHERE a.nomAuteur LIKE :param OR a.prenomAuteur LIKE :param OR a.nomAuteur+a.prenomAuteur LIKE :param02";
            qr = em.createQuery(req);
            qr.setParameter("param", "%" + critere + "%");
            qr.setParameter("param02", "%" + critere01 + "%");
            result = (List<Ouvrage>) qr.getResultList();
            if (result != null) {
                for (Ouvrage ouv : result) {
                    if (!exist(resultatFinal , ouv.getIsbn())) {
                        resultatFinal.add(ouv);
                    }
                }
            }
            result = null;
            //Appliquer le critere de theme 
            req = "SELECT DISTINCT(o) FROM Ouvrage o JOIN o.themes t WHERE t.theme LIKE :param";
            qr = em.createQuery(req);
            qr.setParameter("param", "%" + critere + "%");
            result = (List<Ouvrage>) qr.getResultList();
            if (result != null) {
                for (Ouvrage ouv : result) {
                    if (!exist(resultatFinal , ouv.getIsbn())) {
                        resultatFinal.add(ouv);
                    }
                }
            }
            result = null;
            //Appliquer sur résumé mot clé et editeur 
            req = "SELECT DISTINCT(o) FROM Ouvrage o JOIN o.motsCle m JOIN o.editeurs e WHERE m.motCle LIKE :param OR e.nomEditeur LIKE :param ";
            qr = em.createQuery(req);
            qr.setParameter("param", "%" + critere + "%");
            result = (List<Ouvrage>) qr.getResultList();
            if (result != null) {
                for (Ouvrage ouv : result) {
                    if (!exist(resultatFinal , ouv.getIsbn())) {
                        resultatFinal.add(ouv);
                    }
                }
            }
            //Application du critere de pagination 
            if (resultatFinal.size() <= nbrArticle) {
                return (List<Ouvrage>)resultatFinal;
            } else {
                if (debut + nbrArticle >= resultatFinal.size()) {
                    return (List<Ouvrage>)resultatFinal.subList(debut, result.size() - 1);
                } else {
                    return (List<Ouvrage>)resultatFinal.subList(debut, debut + nbrArticle);
                }
            }
        }
    }

    @Override
    public List<Theme> selectAllTheme(){
        String req="Select DISTINCT(t) FROM Theme t WHERE t.idParent = NULL";
        Query qr = em.createQuery(req);
        return (List<Theme>)qr.getResultList();  
    }
    
    @Override
    public List<Theme> selectSousthemeByTheme(String theme){
        String req= "SELECT DISTINCT (t) FROM Theme t WHERE t.theme_parent=:param";
        Query qr=em.createQuery(req);
        qr.setParameter("param", theme);
        return (List<Theme>)qr.getResultList();
    }
    @Override
    public void creerDonnees() {

        //Création des auteurs 
        Auteur aut01 = new Auteur("Delannoy", "Claude");
        Auteur aut02 = new Auteur("Burd", "Barry");
        Auteur aut03 = new Auteur("Anne", "Tasso");
        Auteur aut04 = new Auteur("Herby", "Cyrille");
        Auteur aut05 = new Auteur("Gervais", "Luc");
        Auteur aut06 = new Auteur("Groussard", "Thierry");
        Auteur aut07 = new Auteur("Benbourahla", "Nazim");
        Auteur aut08 = new Auteur("Pierre", "Philippe");
        Auteur aut09 = new Auteur("Pinchon", "Philippe");
        Auteur aut10 = new Auteur("LABAT", "Léonard");
        Auteur aut11 = new Auteur("YAFI", "Anna");
        Auteur aut12 = new Auteur("Hugon", "Jérôme");
        Auteur aut13 = new Auteur("Sharp", "John");
        Auteur aut14 = new Auteur("Chatelier", "Pierre Y");
        Auteur aut15 = new Auteur("Kadiri", "Hicham");
        Auteur aut16 = new Auteur("Larsson", "Stieg");
        Auteur aut17 = new Auteur("Lagercrantz", "David");
        Auteur aut18 = new Auteur("de Vigan", "Delphine");
        Auteur aut19 = new Auteur("Nothomb", "Amélie");
        Auteur aut20 = new Auteur("Schmitt", "Eric-Emmanuel");
        Auteur aut21 = new Auteur("Escamilla", "Michèle");
        Auteur aut22 = new Auteur("Kersaudy", "François");
        Auteur aut23 = new Auteur("X", "Malcom");
        Auteur aut24 = new Auteur("Paquin", "Sabine");
        Auteur aut25 = new Auteur("Pittion-Rossillon", "André");
        //création des  Editeurs
        Editeur ed01 = new Editeur("Eyrolles", "61 bd Saint Germain 75240 Paris");
        Editeur ed02 = new Editeur("First Interactive", "12 avenue d’Italie 75013 Paris");
        Editeur ed04 = new Editeur("Editions ENI", "25 Boulevard de la Tour-Maubourg, 75007 Paris");
        Editeur ed05 = new Editeur("Microsoft Press", "USA");
        Editeur ed06 = new Editeur("CreateSpace Independent Publishing Platform", "USA");
        Editeur ed07 = new Editeur("Actes Sud", "18, rue Séguier 75006 Paris");
        Editeur ed08 = new Editeur("Lattes", "17 Rue Jacob, 75006 Paris");
        Editeur ed09 = new Editeur("Albin Michel", "22 rue Huyghens, 75014 Paris");
        Editeur ed10 = new Editeur("TALLANDIER", "2 Rue Rotrou, 75006 Paris");
        Editeur ed11 = new Editeur("LA DECOUVERTE", "9 bis, rue Abel-Hovelacque 75013 Paris ");
        Editeur ed12 = new Editeur("Éditions du Chêne", "10 allée Latécoere 78140 Vélizy ");

        //Creation des thèmes 
        Theme t01 = new Theme("Informatique", null,null);
        Theme t02 = new Theme("Développement", 1l,"Informatique");
        Theme t03 = new Theme("Système", 1l,"Informatique");
        Theme t04 = new Theme("Administration", 1l,"Informatique");
        Theme t05 = new Theme("Roman", null,null);
        Theme t06 = new Theme("Policier", 6l,"Roman");
        Theme t07 = new Theme("thriller", 6l,"Roman");
        Theme t08 = new Theme("thriller psychologique", null,null);
        Theme t09 = new Theme("Fable", null,null);
        Theme t10 = new Theme("pamphlet", 9l,"Fable");
        Theme t11 = new Theme("Histoire", null,null);
        Theme t12 = new Theme("XX Siècle", 11l,"Histoire");
        Theme t13 = new Theme("Amériques", 11l,"Histoire");
        Theme t14 = new Theme("Europe", 11L,"Histoire");
        Theme t15 = new Theme("Animaux", null,null);
        Theme t16 = new Theme("nature", null,null);
        Theme t17 = new Theme("photographie", 15L,"Animaux");

        //Creation de Mot clés 
        MotCles m01 = new MotCles("Informatique");
        MotCles m02 = new MotCles("C#");
        MotCles m03 = new MotCles("Java");
        MotCles m04 = new MotCles("Android");
        MotCles m05 = new MotCles("Objective");
        MotCles m06 = new MotCles("Administration");
        MotCles m07 = new MotCles("Windows");
        MotCles m08 = new MotCles("débutant");
        MotCles m09 = new MotCles("avancée");
        MotCles m10 = new MotCles("infrastructure");
        MotCles m11 = new MotCles("programmation");
        MotCles m12 = new MotCles("orienté Objet");
        MotCles m13 = new MotCles("Web");
        MotCles m14 = new MotCles("concept");
        MotCles m15 = new MotCles("XML");
        MotCles m16 = new MotCles("environnement");
        MotCles m17 = new MotCles("open source");
        MotCles m18 = new MotCles("asynchrone");
        MotCles m19 = new MotCles("synchrone");
        MotCles m20 = new MotCles("IOS");
        MotCles m21 = new MotCles("Lisbeth");
        MotCles m22 = new MotCles("Enquête");
        MotCles m23 = new MotCles("Police");
        MotCles m24 = new MotCles("Journaliste");
        MotCles m25 = new MotCles("Meurtre");
        MotCles m26 = new MotCles("accusation");
        MotCles m27 = new MotCles("fiction");
        MotCles m28 = new MotCles("noblesse");
        MotCles m29 = new MotCles("prédiction");
        MotCles m30 = new MotCles("Espagne");
        MotCles m31 = new MotCles("Europe");
        MotCles m32 = new MotCles("Noblesse");
        MotCles m33 = new MotCles("Crime");
        MotCles m34 = new MotCles("Spirituelle");
        MotCles m35 = new MotCles("Hoggar");
        MotCles m36 = new MotCles("Voyage");
        MotCles m37 = new MotCles("Royaune-Unis");
        MotCles m38 = new MotCles("Président");
        MotCles m39 = new MotCles("guerre");
        MotCles m40 = new MotCles("Racisme");
        MotCles m41 = new MotCles("Noirs");
        MotCles m43 = new MotCles("Oppression");
        MotCles m42 = new MotCles("Chiens");
        MotCles m44 = new MotCles("Chats");
        MotCles m45 = new MotCles("photographie");
        MotCles m46 = new MotCles("Mignon");
        MotCles m47 = new MotCles("Empire");
        //Creation de TVA
        TVA tv01 = new TVA(new GregorianCalendar(2009, 5, 9).getTime(), new GregorianCalendar(2020, 1, 15).getTime(), 0.055F);
        TVA tv02 = new TVA(new GregorianCalendar(2009, 5, 9).getTime(), new GregorianCalendar(2020, 1, 15).getTime(), 0.2F);
        Ouvrage ouv01 = new Ouvrage("Programmer en Java", "Couvre les nouveautés de Java 8, streams, expressions lambda", "9782212140071", 105.5f);
        ouv01.setPrixHT(33.5f);
        ouv01.setStock(10);
        ouv01.setResume("majeures de Java 8 : les streams et les expressions lambda ; la gestion du temps, des dates et des heures. Chaque notion nouvelle et chaque fonction du langage sont illustrées de programmes complets dont le code source est en libre téléchargement sur le site des éditions. A qui s'adresse ce livre ? Aux étudiants de licence et de master, ainsi qu'aux élèves d'écoles d'ingénieurs. A tout programmeur ayant déjà une expérience de la programmation (Python, PHP, C/C++, C#...) et souhaitant s'initier au langage java.");
        ouv01.setImage("images/livres/Java_Claude_Delannoy.jpg");
        ouv01.getAuteurs().add(aut01);
        ouv01.getEditeurs().add(ed01);
        ouv01.getTvas().add(tv01);
        ouv01.getMotsCle().add(m01);
        ouv01.getMotsCle().add(m03);
        ouv01.getMotsCle().add(m08);
        ouv01.getMotsCle().add(m11);
        ouv01.getThemes().add(t01);
        ouv01.getThemes().add(t02);

        Ouvrage ouv02 = new Ouvrage("Java Pour les Nuls", "", "9782754055888", 35.5f);
        //Ouvrage ouv02 = new Ouvrage("Nana", "", "1234555", 100f);
        ouv02.setPrixHT(22.9f);
        ouv02.setStock(50);
        ouv02.setResume("Ce livre vous parle de Java, un langage de programmation puissant et très généraliste. Mais son objet n'est pas de vous faire entrer dans les arcanes, les subtilités et autres excentricités servant juste à essayer de briller dans les soirées mondaines. Non. Ce livre s'appuie sur un processus : le processus qui consiste à créer des instructions qu'un ordinateur doit exécuter. Il existe de nombreux ouvrages qui décrivent les mécanismes de ce type de processus et qui alignent règles, conventions et formalismes divers et variés. Mais ces autres livres ne sont pas écrits pour les vraies gens. Ils ne vous prennent pas là où vous êtes pour vous amener là où vous voulez aller.\n"
                + "\n" + "Dans tout ce qui va suivre, je fais très peu de suppositions sur votre expérience dans le domaine des ordinateurs. En lisant chaque section, vous allez voir ce qui se passe dans ma tête. Vous découvrirez les problèmes et leurs solutions en même temps que moi. Je me souviens avoir affronté certains de ces problèmes lorsque je débutais. Et d'autres continuent à me travailler maintenant que je suis devenu un expert (du moins, au dire de mes étudiants qui cherchent évidemment à se faire bien voir). Je vais vous aider à comprendre, à visualiser, et à créer vos propres solutions. Et je vous raconterai même quelques histoires au passage.\n"
                + "\n" + "Comment utiliser ce livre\n"
                + "\n" + "J'aimerais pouvoir vous dire : «Ouvrez le livre au hasard et commencez à écrire du code Java. Contentez-vous de remplir les vides et ne regardez pas en arrière.» Dans un sens, c'est vrai. Vous ne pouvez rien endommager en écrivant du code Java, et vous avez donc à chaque instant une totale liberté pour tenter vos propres expériences.\n"
                + "\n" + "Mais je me dois d'être honnête avec vous. Si vous ne comprenez pas les idées générales, écrire un programme est difficile. Et c'est vrai pour n'importe quel langage de programmation, pas simplement Java. Si vous tapez du code sans savoir exactement ce que vous faites, et si ce code ne se comporte pas comme vous l'espériez, vous risquez fort de rester en panne sèche.\n"
                + "\n" + "C'est pourquoi j'ai divisé dans ce livre les notions que vous devez connaître en petites parties bien plus gérables. Chacune de ces «parties» correspond (plus ou moins) à un chapitre. Vous pouvez sauter là où vous vous voulez -au Chapitre 5, au Chapitre 10 ou ailleurs. Vous pouvez même commencer par le milieu d'un chapitre. J'ai essayé de vous proposer des exemples concrets, et si possible intéressants, sans que l'un d'entre eux ne vous oblige à avaler tout ce qui le précède. Et si je me sers d'une idée importante qui a été déjà développée ailleurs, je vous en préviens pour vous aider à la retrouver.\n"
                + "\n" + "Ma philosophie est somme toute plutôt simple :\n"
                + "\n" + "- Ne perdez pas votre temps à lire ou relire ce que vous savez déjà.\n"
                + "\n" + "- Si vous êtes du genre curieux, n'hésitez pas à foncer vers l'avant. Vous pourrez toujours reprendre un chapitre antérieur lorsque vous en aurez besoin.\n"
                + "\n" + "- Mais ne vous précipitez tout de même pas tout de suite sur la dernière page du livre : je n'y donne pas le nom de l'assassin. C'est à vous d'écrire le programme qui vous le révélera...");
        ouv02.setImage("images/livres/Java_Barry_Burd.jpg");
        ouv02.getAuteurs().add(aut02);
        ouv02.getEditeurs().add(ed02);
        ouv02.getTvas().add(tv01);
        ouv02.getMotsCle().add(m01);
        ouv02.getMotsCle().add(m03);
        ouv02.getMotsCle().add(m08);
        ouv02.getMotsCle().add(m11);
        ouv02.getThemes().add(t01);
        ouv02.getThemes().add(t02);

        Ouvrage ouv03 = new Ouvrage("Le livre de Java premier langage", "Avec 109 exercices corrigés", "9782212141542", 80.5f);
        ouv03.setPrixHT(33.5f);
        ouv03.setStock(25);
        ouv03.setResume("Vous avez décidé de vous initier à la programmation et souhaitez opter pour un langage largement utilisé dans le monde professionnel ? Java se révèle un choix idéal comme vous le constaterez dans ce livre conçu pour les vrais débutants en programmation. Vous apprendrez d'abord, à travers des exemples simples en Java, à maîtriser les notions communes à tous les langages : variables, types de données, boucles et instructions conditionnelles, etc. Vous franchirez un nouveau pas en découvrant par la pratique les concepts de la programmation orientée objet (classes, objets, héritage), puis le fonctionnement des librairies graphiques AWT et Swing (fenêtres, gestion de la souris, tracé de graphiques). Cet ouvrage vous expliquera aussi comment réaliser des applications Java dotées d'interfaces graphiques conviviales grâce au logiciel libre NetBeans. Enfin, vous vous initierez au développement d'applications pour téléphones mobiles Android. Chaque chapitre est accompagné de deux types de travaux pratiques des exercices, dont le corrigé est fourni sur l'extension web www.annetasso.fr/Java, et un projet développé au fil de l'ouvrage, qui vous montrera comment combiner toutes les techniques de programmation étudiées pour construire une véritable application Java.G6");
        ouv03.setImage("images/livres/Java_Anne_Tasso.jpg");
        ouv03.getAuteurs().add(aut03);
        ouv03.getEditeurs().add(ed01);
        ouv03.getTvas().add(tv01);
        ouv03.getMotsCle().add(m01);
        ouv03.getMotsCle().add(m03);
        ouv03.getMotsCle().add(m08);
        ouv03.getMotsCle().add(m12);
        ouv03.getThemes().add(t01);
        ouv03.getThemes().add(t02);

        Ouvrage ouv04 = new Ouvrage("Apprenez à programmer en Java", null, "9791090085893", 70.5f);
        ouv04.setPrixHT(30f);
        ouv04.setStock(30);
        ouv04.setResume("40 chapitres de difficulté progressive et 5 travaux pratiques sont proposés pour apprendre pas à pas à programmer en Java. Les concepts de la programmation sont expliqués, ainsi que l'installation d'Eclipse, la programmation orientée objet, la construction de fenêtres, la modélisation d'un programme en UML et l'enregistrement sur des bases de données.");
        ouv04.setImage("images/livres/Java_Cyrille_Herby.jpg");
        ouv04.getAuteurs().add(aut04);
        ouv04.getEditeurs().add(ed01);
        ouv04.getTvas().add(tv01);
        ouv04.getMotsCle().add(m01);
        ouv04.getMotsCle().add(m03);
        ouv04.getMotsCle().add(m08);
        ouv04.getMotsCle().add(m12);
        ouv04.getThemes().add(t01);
        ouv04.getThemes().add(t02);

        Ouvrage ouv05 = new Ouvrage("Java 8", "Apprendre la Programmation Orientée Objet et maîtrisez le langage (avec exercices et corrigés)", "9782746095076", 85.5f);
        ouv05.setPrixHT(55f);
        ouv05.setStock(25);
        ouv05.setResume("Ces deux livres offrent au lecteur un maximum d'informations sur l'apprentissage de la Programmation Orientée Objet (POO) avec le langage Java. Chaque livre contient des exercices pratiques avec leurs corrigés. Des éléments sont en téléchargement sur www.editions-eni.fr. Un livre de la collection Ressources Informatiques Apprendre la Programmation Orientée Objet avec le langage Java Extrait du résumé : Ce livre s'adresse aux étudiants et aux développeurs ayant déjà une première expérience de la programmation structurée et qui sont désireux de passer à la Programmation Orientée Objet (POO) avec le langage Java, pour développer des applications portables Les chapitres du livre : Avant-propos - Introduction à la POO - La conception orientée objet - Introduction à la plate-forme Java - Les types en Java - Création de classes - Héritage et polymorphisme - Communication entre objets - Le multithreading Un livre de la collection Ressources Informatiques JAVA 8 - Les fondamentaux du langage Java Extrait du résumé : Ce livre s'adresse à tout informaticien désirant développer sous Java. Que le lecteur soit débutant ou qu'il ait déjà une première expérience avec un autre langage il trouvera dans cet ouvrage toutes les bases nécessaires pour se familiariser rapidement avec un des langages les plus utilisés au monde. Les chapitres du livre : Avant-propos - Présentation - Bases du langage - Programmation objet - Applications graphiques - Les applets - Accès aux bases de données - Déploiement d'applications.");
        ouv05.setImage("images/livres/Java-Luc_Gervais_Thierry_Groussard.jpg");
        ouv05.getAuteurs().add(aut05);
        ouv05.getAuteurs().add(aut06);
        ouv05.getEditeurs().add(ed04);
        ouv05.getTvas().add(tv01);
        ouv05.getMotsCle().add(m01);
        ouv05.getMotsCle().add(m03);
        ouv05.getMotsCle().add(m08);
        ouv05.getMotsCle().add(m11);
        ouv05.getMotsCle().add(m12);
        ouv05.getThemes().add(t01);
        ouv05.getThemes().add(t02);

        Ouvrage ouv06 = new Ouvrage("Android 5", "Les fondamentaux du développement d'applications Java", "9782746094444", 80f);
        ouv06.setPrixHT(27.8f);
        ouv06.setStock(10);
        ouv06.setResume("Ce livre est destiné aux développeurs, même débutants, qui souhaitent connaître et maîtriser le développement d'applications Java sur Android 5 (en versions 5.0.x - alias Lollipop - au moment de l'écriture). Sa lecture nécessite des connaissances basiques en programmation Java et XML mais aucun prérequis particulier sur Android. Après une présentation de la plateforme Android et des principes de programmation qui lui sont spécifiques, vous apprendrez à installer et configurer l'environnement de développement (Android Studio et SDK Android). Vous évoluerez ensuite de façon progressive afin de connaître toutes les briques essentielles à la création d'applications Android. Ainsi, vous apprendrez à créer des interfaces de plus en plus complexes (layouts, ressources, ActionBar, menus, listes, popups, webview, etc.), à découvrir les nouveautés de la version 5 d'Android (Material Design, Toolbar, CardView, Notifications Android Wear ...), à gérer la navigation et la communication entre les différentes interfaces d'une application ou entre plusieurs applications. Vous découvrirez les méthodes de création d'interfaces personnalisées (gestion des thèmes, animations, police) ainsi que la gestion des différents évènements utilisateurs (clic, rotation, etc.). Vous apprendrez à optimiser le code de l'application, ses interfaces et à gérer la fragmentation de la plateforme (versions d'Android, taille et résolution des écrans, différences matérielles, etc.). Vous verrez comment récupérer des données nécessaires à une application (webservice, gestion de la connectivité, parsing Xml / Json), les stocker (sharedPreferences, fichiers, base de données SQLite) et les partager avec d'autres applications (ContentProvider, Intent, etc.). Vous pourrez créer et interagir avec des cartes (Google Map, localisation, conversion position/adresse). Enfin, vous apprendrez à gérer les différents traitements et interactions effectués dans une application et à identifier ceux qui doivent s'exécuter en tâches de fond (AsyncTask, Thread, Service, Broadcast Receiver, Widget, etc.) ainsi que les méthodes d'accès aux différentes fonctionnalités d'un appareil sous Android (appels, sms, caméra, accéléromètre, Bluetooth, etc.). Tous les exemples présentés dans le livre sont disponibles en téléchargement sur le site www.editions-eni.fr.");
        ouv06.setImage("images/livres/Android_Nazim_Benbourahla.jpg");
        ouv06.getAuteurs().add(aut07);
        ouv06.getEditeurs().add(ed04);
        ouv06.getTvas().add(tv01);
        ouv06.getMotsCle().add(m01);
        ouv06.getMotsCle().add(m04);
        ouv06.getMotsCle().add(m09);
        ouv06.getThemes().add(t01);
        ouv06.getThemes().add(t02);

        Ouvrage ouv07 = new Ouvrage("Debian GNU/Linux", "Vers une administration de haute sécurité", "9782746094413", 85f);
        ouv07.setPrixHT(50f);
        ouv07.setStock(5);
        ouv07.setResume("Ce livre sur Debian GNU/Linux s'adresse aux administrateurs d'infrastructures, mais aussi à toute personne en charge d'applications critiques, ou de serveurs nécessitant de la haute disponibilité. Un minimum de connaissances sur les principes d'un système GNU/Linux, du réseau et de la virtualisation est nécessaire pour tirer le meilleur profit possible de la lecture de cet ouvrage. Tout au long de ces pages, les différents aspects d'un système d'exploitation Debian sont traités à la lumière de la sécurité : - Installation et initialisation : phases d'installation d'un système Debian, différents points à sécuriser, importance des mises à niveau de version ou du noyau, méthodes d'ajout de nouveaux logiciels. - Système d'exploitation et environnement : sécurité du système d'exploitation du point de vue de l'administrateur (quelles tâches ? quels moyens à sa disposition ? ) puis du point de vue de l'utilisateur (systèmes de fichiers, utilisateurs et groupes, processus, accès, notifications, menus, gestion des fenêtres). - Mise à jour du système : gérer les paquets sous Debian, dépanner un serveur défaillant en s'aidant des journaux de logs et éventuellement en déclarant de nouveaux bogues. - Réseau : les mécanismes de bonding et de teaming, les aspects de partage (NFS, CIFS, SAMBA) et de transfert de fichiers, sécurisation des ports de services, du Wifi. - Services d'infrastructure : serveur de noms, annuaire, messagerie, adressage dynamique et administration graphique, inter connectivité avec des systèmes externes. - Bases de données : conception d'un modèle sécurisé où le stockage de données reste protégé des attaques. - Sauvegardes : différents outils et leurs spécificités afin de déterminer quel outil choisir pour quel type de sauvegarde, en fonction des applications mises en uvre. - Applications : se prémunir des attaques de type rootkits, force brute ou déni de service, appliquer ces principes sur les services d'authentification, d'impression et de présentation. - Supervision : exploiter toutes les ressources mises à disposition du système, prévoir l'activation de tâches récurrentes de supervision, connaître différents outils, Nagios/Centreon, Shinken, Ganglia. - Sécurité : procéder à des configurations extrêmes en prévision d'une utilisation dédiée à la sécurité \"\"pure\"\": systèmes antiviraux, proxy, serveurs d'audit Les notions de proximité et de wrappers sont traitées en détail. - Haute disponibilité & virtualisation : mise en place de mécanismes de multipathing, au niveau du SAN, des systèmes de réplication, au niveau des bases de données, création d'un cluster de services afin de gérer à la fois la redondance au niveau des services et l'équilibrage de charge. L'essentiel des machines virtuelles utilisées dans le livre ont été créées sous Debian 7.5 et Debian 8.0. Toutefois, afin de vérifier certains mécanismes, plusieurs tests ont été réalisés sur des machines en Debian 6.0.9.");
        ouv07.setImage("images/livres/Linux_Philippe_Pierre.jpg");
        ouv07.getAuteurs().add(aut07);
        ouv07.getEditeurs().add(ed04);
        ouv07.getTvas().add(tv01);
        ouv07.getMotsCle().add(m01);
        ouv07.getMotsCle().add(m06);
        ouv07.getMotsCle().add(m16);
        ouv07.getMotsCle().add(m17);
        ouv07.getMotsCle().add(m10);
        ouv07.getThemes().add(t01);
        ouv07.getThemes().add(t04);
        ouv07.getThemes().add(t03);

        Ouvrage ouv08 = new Ouvrage("Linux", "Administration avancée, maintenance et exploitation de vos serveurs", "9782746085473", 70f);
        ouv08.setPrixHT(36f);
        ouv08.setStock(10);
        ouv08.setResume("Ce livre s'adresse à un public de professionnels de l'informatique soucieux de consolider leurs connaissances de base sur le système d'exploitation Linux afin d'évoluer en compétences sur la maintenance et le support de ce système d'exploitation. Les distributions Linux traitées dans le livre sont Debian 7, Ubuntu Server 12.04 LTS et CentOS 6.4. Il est néanmoins possible d'adapter les sujets du livre à d'autres distributions. L'auteur commence par présenter l'architecture du système puis propose des techniques de dépannage afin d'enrichir votre méthode de diagnostic. Les chapitres suivants traitent chacun un sujet précis : la compilation et l'installation du noyau Linux (en version 3.10 au moment de l'écriture), le chargement et le déchargement de modules de noyau (Loadable Kernel Modules), les pseudos systèmes de fichiers procfs et sysfs, la détection du matériel, la gestion des disques, la séquence d'amorçage, la maintenance des applications, la configuration réseau d'un ordinateur, les paramètres de sécurité et l'analyse des performances. L'auteur détaille les commandes nécessaires et illustre ces dernières au travers d'exemples concrets. Les chapitres du livre : Introduction - Dépannage : techniques et procédures - Architecture du système GNU/Linux - Noyau Linux - Modules - Pseudo-systèmes de fichiers - Dépannage matériel - Maintenance des disques - Séquence d'amorçage - Maintenance des applications - Maintenance de la configuration réseau - Sécurité - Analyse des performances.");
        ouv08.setImage("images/livres/Linux_Philippe_Pinchon.jpg");
        ouv08.getAuteurs().add(aut09);
        ouv08.getEditeurs().add(ed04);
        ouv08.getTvas().add(tv01);
        ouv08.getMotsCle().add(m01);
        ouv08.getMotsCle().add(m06);
        ouv08.getMotsCle().add(m09);
        ouv08.getMotsCle().add(m10);
        ouv08.getMotsCle().add(m17);
        ouv08.getThemes().add(t01);
        ouv08.getThemes().add(t04);
        ouv08.getThemes().add(t03);

        Ouvrage ouv09 = new Ouvrage("ASP.NET MVC 4 ", "Développement d'applications Web en C# - Concepts et bonnes pratiques", "9782746085411", 100f);
        ouv09.setPrixHT(36f);
        ouv09.setStock(15);
        ouv09.setImage("images/livres/ASP_Léonard_LABAT_Anna_YAFI.jpg");
        ouv09.getAuteurs().add(aut10);
        ouv09.getAuteurs().add(aut11);
        ouv09.getEditeurs().add(ed04);
        ouv09.getTvas().add(tv01);
        ouv09.getMotsCle().add(m01);
        ouv09.getMotsCle().add(m02);
        ouv09.getMotsCle().add(m08);
        ouv09.getMotsCle().add(m09);
        ouv09.getMotsCle().add(m12);
        ouv09.getThemes().add(t01);
        ouv09.getThemes().add(t02);

        Ouvrage ouv10 = new Ouvrage("Visual Basic 2012 (VB.NET)", "Les fondamentaux du langage - Développer avec Visual Studio 2012", "9782746078581", 40f);
        ouv10.setPrixHT(37f);
        ouv10.setStock(15);
        ouv10.setResume("Ce livre sur VB.NET s'adresse aux développeurs, même débutants, désireux de maîtriser Visual Basic.NET. Après une description de l'environnement de développement (Visual Studio 2012), le lecteur découvrira les bases de la programmation orientée objet avec VB.NET. Il évoluera de façon progressive vers sa mise en uvre avec le développement d'applications Windows Form. Les nouveautés de ce langage concernant la programmation asynchrone lui permettront d'améliorer les performances et la réactivité des applications. Les nombreux exemples et les conseils sur l'utilisation des outils de débogage lui fourniront une aide précieuse pendant la réalisation d'une application. Un chapitre consacré à l'accès aux bases de données à l'aide de ADO.NET et de SQL permettra d'évoluer vers le développement d'applications client-serveur. Les puissantes fonctionnalités de LINQ sont présentées et détaillées pour faciliter l'accès et la manipulation des données. L'utilisation du langage XML est également présentée, celui-ci permettant de faciliter l'échange d'informations avec d'autres applications. Les utilisateurs des versions précédentes découvriront les nouveautés et améliorations de cette version 2012 pour développer encore plus rapidement et facilement des applications pour le framework .NET 4.5. La distribution d'une application est présentée avec l'utilisation de Windows Installer et de la technologie Click Once. Les exemples cités dans le livre sont en téléchargement sur le site www.editions-eni.fr. Les chapitres du livre : Avant-propos - Présentation de la plate-forme .NET - Présentation de Visual Studio - Organisation d'une application - Bases du langage - Programmation objet - Gestion des erreurs et débogage du code - Applications Windows - Accès aux bases de données - Présentation de LINQ - Utilisation de XML - Déploiement d'applications et de composants.");
        ouv10.setImage("images/livres/Basic_Thierry_Groussard.jpg");
        ouv10.getAuteurs().add(aut06);
        ouv10.getEditeurs().add(ed04);
        ouv10.getTvas().add(tv01);
        ouv10.getMotsCle().add(m01);
        ouv10.getMotsCle().add(m06);
        ouv10.getMotsCle().add(m09);
        ouv10.getMotsCle().add(m08);
        ouv10.getMotsCle().add(m11);
        ouv10.getThemes().add(t01);
        ouv10.getThemes().add(t02);

        Ouvrage ouv11 = new Ouvrage("C# 6", "Développez des applications Windows avec Visual Studio 2015", "9782746097001", 80f);
        ouv11.setPrixHT(36f);
        ouv11.setStock(5);
        ouv11.setResume("Ce livre sur le développement d'applications Windows avec le langage C# et Visual Studio 2015 est destiné aux développeurs qui débutent avec le framework .NET. Il leur permet d'apprendre les bases du langage C# et introduit des concepts plus avancés leur donnant une vue d'ensemble des possibilités offertes par le langage C#, Visual Studio et le framework .NET en général. L'auteur a choisi une approche pas à pas tout en construisant une application fonctionnelle tout au long de l'ouvrage pour illustrer de manière pratique et cohérente les concepts abordés. L'apprentissage commence par la familiarisation avec l'interface de Visual Studio 2015 ainsi qu'avec le concept de l'architecture .NET. Les détails du langage C#, sa syntaxe et ses fonctionnalités comme les classes, l'héritage, les interfaces, les types génériques ou encore les délégués et les évènements sont ensuite expliqués avant d'aborder la conception d'interfaces utilisateur. La conception de l'interface utilisateur couvre toutes les phases utiles pour créer des applications Windows à la fois fonctionnelles et ergonomiques, allant de la création de formulaires à la création de contrôles en passant par l'implémentation de gestionnaire d'évènements et la validation des données saisies. Une introduction à la conception d'application WPF est également incluse. Les outils de Visual Studio qui permettent de réaliser les tests et le débogage des applications sont également détaillés en présentant les techniques de gestion des erreurs mais aussi les concepts permettant de surveiller les applications comme le traçage, l'interaction avec les journaux d'évènements et l'utilisation des compteurs de performance. L'utilisation de Entity Framework est détaillée au sein d'exemples concrets permettant de comprendre rapidement comment créer des modèles de données et comment les utiliser pour communiquer avec une base de données tout en apprenant à utiliser le langage de requête LINQ pour interagir avec des données sous différents formats (objets, SQL ou XML). L'alternative du stockage de données d'une application sur le système de fichiers et l'utilisation du concept de la sérialisation sont également détaillés fournissant ainsi une vision globale des possibilités offertes par le framework .NET concernant la gestion des données. Des concepts plus avancés sont également abordés afin d'exposer une gamme plus large des possibilités offertes par le langage C# et Visual Studio : l'utilisation des expressions régulières, le développement d'applications multi-tâches et asynchrones, la globalisation et la localisation d'une application, la sécurité du code, l'implémentation d'applications client/serveur, le dessin avec GDI+ ainsi que la réflexion font partie des sujets introduits. La dernière partie de l'ouvrage est consacrée à la création d'assemblages ainsi qu'au déploiement des applications. Les outils et techniques mis à disposition par Visual Studio pour créer des installeurs Windows et configurer les applications y sont détaillés. Le code de l'application exemple traitée dans l'ouvrage est en téléchargement sur le site ww.editions-eni.fr.");
        ouv11.setImage("images/livres/C_Jerome_Hugon.jpg");
        ouv11.getAuteurs().add(aut12);
        ouv11.getEditeurs().add(ed04);
        ouv11.getTvas().add(tv01);
        ouv11.getMotsCle().add(m01);
        ouv11.getMotsCle().add(m02);
        ouv11.getMotsCle().add(m11);
        ouv11.getMotsCle().add(m12);
        ouv11.getThemes().add(t01);
        ouv11.getThemes().add(t02);

        Ouvrage ouv13 = new Ouvrage("Visual C# 2010 étape par étape", "Les outils du développeur", "9782100547418", 90f);
        ouv13.setPrixHT(36f);
        ouv13.setStock(10);
        ouv13.setResume("Visual C# est l'un des langages de programmation présents dans Visual Studio 2010 (aux côtés de Visual Basic, Visual C++ et Visual J#). Cet ouvrage vous guidera dans la découverte et l'apprentissage de Visual C# 2010. De la syntaxe à l'écriture et à l'exécution de vos premiers programmes vous apprendrez à votre rythme les techniques essentielles. Les nombreux exercices et exemples concrets vous permettront de tester vos connaissances et de découvrir les meilleures pratiques du développement sous Visual C# 2010.");
        ouv13.setImage("images/livres/Visual_C_John-Sharp.jpg");
        ouv13.getAuteurs().add(aut13);
        ouv13.getEditeurs().add(ed05);
        ouv13.getTvas().add(tv01);
        ouv13.getMotsCle().add(m01);
        ouv13.getMotsCle().add(m02);
        ouv13.getMotsCle().add(m11);
        ouv13.getMotsCle().add(m12);
        ouv13.getThemes().add(t01);
        ouv13.getThemes().add(t02);

        Ouvrage ouv14 = new Ouvrage("Objective-C pour le développeur avancé", "Le langage d'iOS 6 et Mac OS X pour les développeurs C++/ Java/ C#", "9782212136869", 90f);
        ouv14.setPrixHT(29f);
        ouv14.setStock(35);
        ouv14.setResume("face à un C++ puissant, efficace et maîtrisé, Objective-C surprend par sa richesse et sa souplesse. Adressé au développeur confirmé, ce livre dense et érudit guidera les amoureux de la programmation iOS et Mac OS X à travers toutes les subtilités de ce langage. Objective-C, langage objet indispensable pour développer en natif sous Mac OS X et pour l'iPhone et l'iPad Avec le succès de l'iPhone et de l'iPad, la maîtrise d'Objective-C, langage natif des systèmes Apple Mac OS X et IOS, devient un passage obligé pour les professionnels de la programmation - alors que l'engouement pour ces systèmes ne l'a introduit que récemment dans la formation classique des développeurs. Adressé au développeur qui connaît déjà d'autres langages objet, cet ouvrage éclaire toutes les subtilités d'Objective-C en le comparant avec les principaux langages que sont C++, Java, C# : syntaxe et concepts objet (classes, héritage, instanciation), gestion de la mémoire, chaînes de caractères, exceptions, multithreading, concept des propriétés, mécanismes de modifications à l'exécution, sans oublier les récentes évolutions apportées au langage.");
        ouv14.setImage("images/livres/Objective_Pierre_Y_Chatelier.jpg");
        ouv14.getAuteurs().add(aut14);
        ouv14.getEditeurs().add(ed01);
        ouv14.getTvas().add(tv01);
        ouv14.getMotsCle().add(m01);
        ouv14.getMotsCle().add(m05);
        ouv14.getMotsCle().add(m11);
        ouv14.getMotsCle().add(m13);
        ouv14.getMotsCle().add(m20);
        ouv14.getThemes().add(t01);
        ouv14.getThemes().add(t02);

        Ouvrage ouv15 = new Ouvrage("RDS Windows Server 2012 R2", "Deploiement et Administration en Entreprise: Guide du Consultant", "9781517044428", 90f);
        ouv15.setPrixHT(20f);
        ouv15.setStock(20);
        ouv15.setResume("Ce livre contient toutes les informations dont vous avez besoin pour concevoir, preparer, deployer et gerer une infrastructure RDS 2012 R2. Il peut interesser plusieurs populations IT, notamment : DSI, Responsable Technique /Infrastructure, Architecte Infrastructure, Consultant Infrastructure, Chef de Projet Technique, Ingénieur Systèmes /Réseaux, Administrateur Système /Réseau, Technicien de Support /Système /Réseau ou toute personne désirant concevoir, déployer et administrer RDS 2012 R2. De plus, il repose sur la mise en place d'une infrastructure RDS 2012 R2 hautement disponible incluant la redondance des services de rôles suivants : Hôte de session Bureau à distance (RDSH : Remote Desktop Session Host) | Gestionnaire de licences des Services bureau à distance (RDLS : Remote Desktop Licensing Server) | Service Broker pour les connexions Bureau à distance (RDCB : Remote Desktop Connection Broker) | Passerelle des Services Bureau à distance (RDG : Remote Desktop Gateway) | Accès Bureau à distance par le Web (RDWA : Remote Desktop Web Access). L'eBook est organisé en 16 chapitres. L'auteur vous explique comment créer des scripts PowerShell de déploiement du rôle RDS, de création de Collection ou encore de publication de Programme RemoteApps. Les nouveaux concepts comme les nouveaux modes de déploiement de la solution RDS, les Collections ou encore les Disques de profil utilisateur sont détaillés sur cet eBook.Ayant participé à plusieurs projets d'envergure de déploiement RDS en France et en Europe, l'auteur partage à travers cet eBook toute la méthodologie, les techniques, retours d'expérience et \"Best Practices\" pour réussir votre projet RDS sous Windows Server 2012 R2.");
        ouv15.setImage("images/livres/RDS_Hicham_Kadiri.jpg");
        ouv15.getAuteurs().add(aut15);
        ouv15.getEditeurs().add(ed06);
        ouv15.getTvas().add(tv01);
        ouv15.getMotsCle().add(m01);
        ouv15.getMotsCle().add(m06);
        ouv15.getMotsCle().add(m07);
        ouv15.getMotsCle().add(m10);
        ouv15.getMotsCle().add(m16);
        ouv15.getThemes().add(t01);
        ouv15.getThemes().add(t04);

        Ouvrage ouv17 = new Ouvrage("Millenium 1", "Les hommes qui n'aimaient pas les femmes", "9782330004996", 25f);
        ouv17.setPrixHT(20f);
        ouv17.setStock(40);
        ouv17.setResume("Ancien rédacteur de Millénium, revue d'investigations sociales et économiques, Mikael Blomkvist est contacté par un gros industriel pour relancer une enquête abandonnée depuis quarante ans.\n"
                + "Dans le huis clos d'une île, la petite nièce de Henrik Vanger a disparu, probablement assassinée, et quelqu'un se fait un malin plaisir de le lui rappeler à chacun de ses anniversaires. \n"
                + "Secondé par Lisbeth Salander, jeune femme rebelle et perturbée, placée sous contrôle social mais fouineuse hors pair, Mikael Blomkvist, cassé par un procès en diffamation qu'il vient de perdre, se plonge sans espoir dans les documents cent fois examinés, jusqu'au jour où une intuition lui fait reprendre un dossier. \n"
                + "Régulièrement bousculés par de nouvelles informations, suivant les méandres des haines familiales et des scandales financiers. lancés bientôt dans le monde des tueurs psychopathes, le journaliste tenace et l'écorchée vive vont résoudre l'affaire des fleurs séchées et découvrir ce qu'il faudrait peut-être taire. \n"
                + "Stieg Larsson, né en 1954, journaliste auquel on doit des essais sur l'économie et des reportages de guerre en Afrique, était le rédacteur en chef d'Expo, revue suédoise observatoire des manifestations ordinaires du fascisme. Il est décédé brutalement, en 2004, d'une crise cardiaque, juste après avoir remis à son éditeur les trois tomes de la trilogie Millénium.");
        ouv17.setImage("images/livres/Millenium1.jpg");
        ouv17.getAuteurs().add(aut16);
        ouv17.getEditeurs().add(ed07);
        ouv17.getTvas().add(tv01);
        ouv17.getMotsCle().add(m21);
        ouv17.getMotsCle().add(m22);
        ouv17.getMotsCle().add(m23);
        ouv17.getMotsCle().add(m24);
        ouv17.getMotsCle().add(m26);
        ouv17.getThemes().add(t05);
        ouv17.getThemes().add(t06);
        ouv17.getThemes().add(t07);

        Ouvrage ouv18 = new Ouvrage("Millenium 2", "La fille qui rêvait d'un bidon d'essence et d'une allumette", "9782742797875", 22f);
        ouv18.setPrixHT(20f);
        ouv18.setStock(50);
        ouv18.setResume("Tandis que Lisbeth Salander coule des journées supposées tranquilles aux Caraïbes, Mikael Blomkvist, réhabilité, victorieux, est prêt à lancer un numéro spécial de Millenium sur un thème brûlant pour des gens haut placés : une sombre histoire de prostituées exportées des pays de l'Est. Mikael aimerait surtout revoir Lisbeth. Il la retrouve sur son chemin, mais pas vraiment comme prévu : un soir, dans une rue de Stockholm, il la voit échapper clé peu à une agression manifestement très planifiée.\n"
                + "Enquêter sur clés sujets qui fâchent mafieux et politiciens n'est pas ce qu'on souhaite à clé jeunes journalistes amoureux de la vie. Deux meurtres se succèdent, les victimes enquêtaient pour Millenium. Pire que tout, la police et les médias vont bientôt traquer Lisbeth, coupable toute désignée et qu'on a vite fait de qualifier de tueuse en série au passé psychologique lourdement chargé.\n"
                + "Mais qui était cette gamine attachée sur un lit, exposée aux caprices d'un maniaque et qui survivait en rêvant d'un bidon d'essence et d'une allumette ?\n"
                + "S'agissait-il d'une des filles des pays de l'Est, y a-t-il une hypothèse plus compliquée encore ? C'est dans cet univers à cent à l'heure que nous embarque Stieg Larsson qui signe avec ce deuxième volume de la trilogie Millenium un thriller au rythme affolant.");
        ouv18.setImage("images/livres/Millenium2.jpg");
        ouv18.getAuteurs().add(aut16);
        ouv18.getEditeurs().add(ed07);
        ouv18.getTvas().add(tv01);
        ouv18.getMotsCle().add(m21);
        ouv18.getMotsCle().add(m22);
        ouv18.getMotsCle().add(m23);
        ouv18.getMotsCle().add(m24);
        ouv18.getMotsCle().add(m26);
        ouv18.getThemes().add(t05);
        ouv18.getThemes().add(t06);
        ouv18.getThemes().add(t07);

        Ouvrage ouv19 = new Ouvrage("Millenium 3", "La reine dans le palais des courants d'air", "9782330014421", 28f);
        ouv19.setPrixHT(22f);
        ouv19.setStock(50);
        ouv19.setResume("Coincée dans une chambre d'hôpital sous bonne garde policière, Lisbeth est l'enjeu du combat décisif entre Mikael et les forces du bien d'une part, la Säpo et toutes les aberrations d'un système d'autre part. Coincée, oui, inactive, peut-être pas... Le troisième et dernier volet de l'irrésistible série Millenium qui a imposé la nouvelle collection Actes Noirs va encore donner aux lecteurs ses doses de frissons et giclées d'adrénaline !");
        ouv19.setImage("images/livres/Millenium3.jpg");
        ouv19.getAuteurs().add(aut16);
        ouv19.getEditeurs().add(ed07);
        ouv19.getTvas().add(tv01);
        ouv19.getMotsCle().add(m21);
        ouv19.getMotsCle().add(m22);
        ouv19.getMotsCle().add(m23);
        ouv19.getMotsCle().add(m24);
        ouv19.getMotsCle().add(m26);
        ouv19.getThemes().add(t05);
        ouv19.getThemes().add(t06);
        ouv19.getThemes().add(t07);

        Ouvrage ouv20 = new Ouvrage("Millenium 4", "Ce qui ne me tue pas", "9782330053901", 38f);
        ouv20.setPrixHT(40f);
        ouv20.setStock(70);
        ouv20.setResume("Quand Mikael Blomkvist recoit un appel d'un chercheur de pointe dans le domaine de l'intelligence artificielle qui affirme detenir des informations sensibles sur les services de renseignement americains, il se dit qu'il tient le scoop qu'il attendait pour relancer la revue Millenium et sa carriere. Au meme moment, une hackeuse de genie tente de penetrer les serveurs de la NSA... Dix ans apres la publication en Suede du premier volume, la saga Millenium continue.");
        ouv20.setImage("images/livres/Millenium4.jpg");
        ouv20.getAuteurs().add(aut17);
        ouv20.getEditeurs().add(ed07);
        ouv20.getTvas().add(tv01);
        ouv20.getMotsCle().add(m21);
        ouv20.getMotsCle().add(m22);
        ouv20.getMotsCle().add(m23);
        ouv20.getMotsCle().add(m24);
        ouv20.getMotsCle().add(m26);
        ouv20.getThemes().add(t05);
        ouv20.getThemes().add(t06);
        ouv20.getThemes().add(t07);

        Ouvrage ouv21 = new Ouvrage("D'après une histoire vrai", "", "9782709648523", 25f);
        ouv21.setPrixHT(25f);
        ouv21.setStock(10);
        ouv21.setResume("« Ce livre est le recit de ma rencontre avec L.\n"
                + "L. est le cauchemar de tout ecrivain.Ou plutot le genre de personne qu'un ecrivain ne devrait jamais croiser.»\n"
                + "\n Dans ce roman aux allures de thriller psychologique, Delphine de Vigan s'aventure en equilibriste sur la ligne de crete qui separe le reel de la fiction. Ce livre est aussi une plongee au cour d'une epoque fascinee par le Vrai.");
        ouv21.setImage("images/livres/DapresUneHistoireVrai.jpg");
        ouv21.getAuteurs().add(aut18);
        ouv21.getEditeurs().add(ed08);
        ouv21.getTvas().add(tv01);
        ouv21.getMotsCle().add(m27);
        ouv21.getThemes().add(t05);
        ouv21.getThemes().add(t06);
        ouv21.getThemes().add(t08);

        Ouvrage ouv22 = new Ouvrage("Le crime du comte Neville", "", "9782226381118", 15f);
        ouv22.setPrixHT(15f);
        ouv22.setStock(10);
        ouv22.setResume("« Ce qui est monstrueux n'est pas necessairement indigne. » Amelie Nothomb");
        ouv22.setImage("images/livres/LeCrimeDuCompteNeville.jpg");
        ouv22.getAuteurs().add(aut19);
        ouv22.getEditeurs().add(ed09);
        ouv22.getTvas().add(tv01);
        ouv22.getMotsCle().add(m28);
        ouv22.getMotsCle().add(m29);
        ouv22.getThemes().add(t09);

        Ouvrage ouv23 = new Ouvrage("La nuit de feu", "", "9782226384096", 2f);
        ouv23.setPrixHT(25f);
        ouv23.setStock(50);
        ouv23.setResume("À vingt-huit ans, Éric-Emmanuel Schmitt entreprend une randonnée à pied dans le Sahara en 1989. Parti athée, il en reviendra croyant, dix jours plus tard.\n"
                + "\n Loin de ses repères, il découvre une vie réduite à la simplicité, noue des liens avec les Touareg. Mais il va se perdre dans les immenses étendues du Hoggar pendant une trentaine d’heures, sans rien à boire ou à manger, ignorant où il est et si on le retrouvera. Cette nuit-là, sous les étoiles si proches, alors qu’il s’attend à frissonner d’angoisse, une force immense fond sur lui, le rassure, l’éclaire et le conseille.\n"
                + "\n Cette nuit de feu – ainsi que Pascal nommait sa nuit mystique – va le changer à jamais. Qu’est-il arrivé ? Qu’a-t-il entendu ? Que faire d’une irruption aussi brutale et surprenante quand on est un philosophe formé à l’agnosticisme ?Dans ce livre où l’aventure se double d’un immense voyage intérieur, Éric-Emmanuel Schmitt nous dévoile pour la première fois son intimité spirituelle et sentimentale, montrant comment sa vie entière, d’homme autant que d’écrivain, découle de cet instant miraculeux.\n"
                + "\n Dramaturge, romancier, nouvelliste, essayiste, cinéaste, traduit en 50 langues et joué dans autant de pays, Éric-Emmanuel Schmitt est un des auteurs français les plus lus et les plus représentés dans le monde.");
        ouv23.setImage("images/livres/LanuitDeFeu.jpg");
        ouv23.getAuteurs().add(aut20);
        ouv23.getEditeurs().add(ed09);
        ouv23.getTvas().add(tv01);
        ouv23.getMotsCle().add(m34);
        ouv23.getMotsCle().add(m35);
        ouv23.getMotsCle().add(m36);
        ouv23.getThemes().add(t05);

        Ouvrage ouv24 = new Ouvrage("Le Siècle d'or de l'Espagne", "Apogée et déclin, 1492-1598", "9791021005259", 45f);
        ouv24.setPrixHT(40f);
        ouv24.setStock(40);
        ouv24.setResume("Synthèse de référence de l'hégémonie espagnole tant politique que religieuse ou culturelle. Cette suprématie consolidée par les richesses des Amériques durera du XVe au XVIIe siècle. La grandeur de l'Espagne du XVIe siècle n'a jamais fait l'objet de la large synthèse qu'elle mérite amplement. La prépondérance mondiale de cet empire qui s'étendait des Philippines à l'Amérique, son poids en Europe et sa puissance face à l'Angleterre et à la France, son prestige intellectuel et culturel (Cervantès, Lope de Vega, Greco...), son influence religieuse (Thérèse d'Avila, Jean de La croix...), sa fabuleuse richesse (les métaux précieux d'Amérique) ont joué un rôle décisif dans l'histoire du monde. Il faut évidemment comprendre sur quoi a reposé l'émergence rapide d'une péninsule fragmentée depuis des siècles en principautés musulmanes et chrétiennes (ces dernières souvent rivales entre elles). Quelle a été la place des hommes, des institutions, de l'idéologie et de la religion ? A l'inverse, quels sont les éléments de faiblesse internes et externes qui ont fragilisé l'édifice au point de faire de l'Espagne du XVIIIe siècle une puissance de deuxième ordre ? Sans rien négliger ni laisser dans l'ombre les aspects tenus à tort pour secondaires, Michèle Escamilla, historienne de l'Inquisition et de Charles Quint, dresse un tableau aussi grandiose que passionnant de l'Espagne du Siècle d'or.");
        ouv24.setImage("images/livres/LeSiecleDORESPAGNE.jpg");
        ouv24.getAuteurs().add(aut21);
        ouv24.getEditeurs().add(ed10);
        ouv24.getTvas().add(tv01);
        ouv24.getMotsCle().add(m47);
        ouv24.getThemes().add(t11);
        ouv24.getThemes().add(t14);

        Ouvrage ouv25 = new Ouvrage("Winston Churchill", "", "9791021008403", 50f);
        ouv25.setPrixHT(42f);
        ouv25.setStock(25);
        ouv25.setResume("NOUVELLE ÉDITION REVUE ET AUGMENTÉE « Nous sommes tous des vers », avait modestement confi é le jeune Winston à une amie, « mais je crois que moi, je suis un ver luisant ! » Le mot n est pas trop fort : Alexandre Dumas aurait pu inventer un personnage de ce genre, mais dans le cas de Winston Leonard Spencer-Churchill, la stricte réalité dépasse de très loin la fiction. Jusqu à 26 ans, les aventures du jeune officier et du reporter évoquent immanquablement celles de Tintin ; mais ensuite, le personnage devient une synthèse de Clemenceau et de De Gaulle, l humour et l alcool en plus... ainsi qu une imagination sans limites : « Winston, disait le président Roosevelt, a cent idées par jour, dont quatre seulement sont bonnes... mais il ne sait jamais lesquelles ! » C est pourtant le général de Gaulle qui l a le mieux jugé : « Il fut le grand artiste d une grande histoire. » Cette vie a été un roman ; elle est racontée comme tel, sans un mot de fiction. Se fondant sur des recherches dans les archives de huit pays, la consultation de quelque quatre cents ouvrages et l interview de nombreux acteurs et témoins, ce récit épique montre comment un homme solitaire, longuement façonné par d exceptionnels talents et de singulières faiblesses, a pu infléchir le cours de notre siècle, avec la complicité d un destin qui s est radicalement départi de son impartialité.");
        ouv25.setImage("images/livres/WinstonChUrchill.jpg");
        ouv25.getAuteurs().add(aut22);
        ouv25.getEditeurs().add(ed10);
        ouv25.getTvas().add(tv01);
        ouv25.getMotsCle().add(m37);
        ouv25.getMotsCle().add(m38);
        ouv25.getMotsCle().add(m39);
        ouv25.getThemes().add(t11);
        ouv25.getThemes().add(t12);

        Ouvrage ouv26 = new Ouvrage("Le pouvoir noir", "", "9782707154408", 40f);
        ouv26.setPrixHT(45f);
        ouv26.setStock(70);
        ouv26.setResume("Malcolm X demeure l'un des plus célèbres militants noirs américains. Devenu l'un des chefs de file du mouvement des Black Muslims, il quitta celui-ci en 1964 pour créer une organisation non religieuse qu'il voulait plus politiquement engagée encore, l'Organisation de l'unité afro-américaine (OUA). Il avait découvert l'importance qu'il y avait à relier le mouvement noir américain à ceux qui ailleurs combattaient la même forme de racisme et d'oppression. À partir de cette période, les prises de position de Malcolm X, ses analyses et ses réflexions, peuvent évoluer très rapidement : ainsi en est-il de ses idées de former une nation noire séparée ou d'organiser le retour en Afrique. En avril 1964, il débute ses grandes tournées en Afrique et au Moyen-Orient dans le but de préparer l'unité des Noirs et d'internationaliser leur lutte pour la liberté. Ce recueil retrace l'itinéraire politique de Malcolm X à partir de sa rupture d'avec les Black Muslims. Il éclaire l'évolution d'un homme profondément sensible, marqué par l'amère condition des siens, mais décidé à en finir –; par tous les moyens –; avec la ségrégation, la misère et le racisme.");
        ouv26.setImage("images/livres/LePouvoirNoir.jpg");
        ouv26.getAuteurs().add(aut22);
        ouv26.getEditeurs().add(ed11);
        ouv26.getTvas().add(tv01);
        ouv26.getMotsCle().add(m41);
        ouv26.getMotsCle().add(m40);
        ouv26.getMotsCle().add(m43);
        ouv26.getThemes().add(t11);
        ouv26.getThemes().add(t13);

        Ouvrage ouv27 = new Ouvrage("Les Chats", "", "9782851087881", 75f);
        ouv27.setPrixHT(35f);
        ouv27.setStock(10);
        ouv27.setResume("Yann Arthus-Bertrand, amoureux des animaux et photographe de renommée internationale, présente une galerie de portraits de chats, photographiés avec ou sans leurs maîtres. L'histoire du chat et sa mythologie accompagnent une description précise de chaque espèce. Une somme pour tout savoir sur les chats.");
        ouv27.setImage("images/livres/Leschats.jpg");
        ouv27.getAuteurs().add(aut24);
        ouv27.getEditeurs().add(ed12);
        ouv27.getTvas().add(tv01);
        ouv27.getMotsCle().add(m44);
        ouv27.getMotsCle().add(m45);
        ouv27.getMotsCle().add(m46);
        ouv27.getThemes().add(t15);
        ouv27.getThemes().add(t16);
        ouv27.getThemes().add(t17);

        Ouvrage ouv28 = new Ouvrage("Les Chiens", "", "9782851087713", 45f);
        ouv28.setPrixHT(45f);
        ouv28.setStock(10);
        ouv28.setResume("Yann Arthus-Bertrand présente une galerie de portraits de toutes les races de chiens actuellement élevées en France, photographiés avec ou sans leurs maîtres. Des images tour à tour humoristiques ou touchantes, accompagnées d'informations précises et d'anecdotes amusantes signées par la Société centrale canine. Un véritable trésor pour tout savoir sur les chiens.");
        ouv28.setImage("images/livres/LesChiens.jpg");
        ouv28.getAuteurs().add(aut25);
        ouv28.getEditeurs().add(ed12);
        ouv28.getTvas().add(tv01);
        ouv28.getMotsCle().add(m42);
        ouv28.getMotsCle().add(m45);
        ouv28.getMotsCle().add(m46);
        ouv28.getThemes().add(t15);
        ouv28.getThemes().add(t16);
        ouv28.getThemes().add(t17);
        //Persisiter les Objets 
        em.persist(ouv01);
        em.persist(ouv02);
        em.persist(ouv03);
        em.persist(ouv04);
        em.persist(ouv05);
        em.persist(ouv06);
        em.persist(ouv07);
        em.persist(ouv08);
        em.persist(ouv09);
        em.persist(ouv10);
        em.persist(ouv11);
        em.persist(ouv13);
        em.persist(ouv14);
        em.persist(ouv15);
        em.persist(ouv17);
        em.persist(ouv18);
        em.persist(ouv19);
        em.persist(ouv20);
        em.persist(ouv21);
        em.persist(ouv22);
        em.persist(ouv23);
        em.persist(ouv24);
        em.persist(ouv25);
        em.persist(ouv26);
        em.persist(ouv27);
        em.persist(ouv28);
    }

    @Override
    public void update(Ouvrage ouvrage) {
        em.persist(ouvrage);
    }
}
