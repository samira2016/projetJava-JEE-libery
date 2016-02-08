/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beansession.GestionCatalogueLocal;
import beansession.GestionLigneCommandeLocal;
import beansession.GestionPanierLocal;
import beansession.GestionUtilisateurLocal;
import entites.Adresse;
import entites.Ouvrage;
import entites.Utilisateur;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import outilsWar.CommandeWar;
import outilsWar.PanierWar;
import outils.ErreurMessage;

/**
 *
 * @author fafiec103
 */
@WebServlet(name = "Controller", urlPatterns = {"/Controller"})
public class Controller extends HttpServlet {

    @EJB
    GestionUtilisateurLocal gestionUtilisateur = lookupGestionUtilisateurLocal();
    @EJB
    private GestionPanierLocal gestionPanier;
    @EJB
    private GestionLigneCommandeLocal gestionCommande;
    @EJB
    private GestionCatalogueLocal gestionCatalogue;

    HashMap<String, String> messageErr = new HashMap();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String section = request.getParameter("section");
        String action = request.getParameter("action");
        String origine = request.getParameter("origine");

        String url = "/WEB-INF/jsp/accueil.jsp";

        if ("modifierCommande".equals(section)) {
            url = "/WEB-INF/magasin/panier.jsp";
        }

        if ("commande".equals(section)) {
            if ("test".equalsIgnoreCase(action)) {
                //session.setAttribute("panierWar", pw);
                if (session.getAttribute("maCommande") == null) {
                    CommandeWar pw = new CommandeWar();
                    session.setAttribute("maCommande", pw.getGestionLigneCommande().getLignes());
                }

                if (session.getAttribute("monAdresseFacturation") == null) {
                    CommandeWar pw = new CommandeWar();
                    session.setAttribute("monAdresseFacturation", pw.getGestionLigneCommande().getAdresseFacturation());
                }

                if (session.getAttribute("monAdresseLivraison") == null) {
                    CommandeWar pw = new CommandeWar();
                    session.setAttribute("monAdresseLivraison", pw.getGestionLigneCommande().getAdresseLivraison());
                }

                if (session.getAttribute("prixTotal") == null) {
                    CommandeWar pw = new CommandeWar();
                    session.setAttribute("prixTotal", pw.getGestionLigneCommande().getPrixTotal());
                }

                url = "/WEB-INF/magasin/commande.jsp";
            } else {
                if (session.getAttribute("commandeWar") == null) {
                    CommandeWar commandeWar = new CommandeWar();
                    session.setAttribute("commandeWar", commandeWar);
                }

                CommandeWar commandeWar = (CommandeWar) session.getAttribute("commandeWar");
                gestionCommande = commandeWar.getGestionLigneCommande();

                if (session.getAttribute("panierWar") != null) {
                    PanierWar panierWar = (PanierWar) session.getAttribute("panierWar");
                    gestionPanier = panierWar.getGestionPanier();
                }

                if ("paiement".equalsIgnoreCase(action)) {
                    url = "/WEB-INF/magasin/paiement.jsp";
                }
                if ("annuler".equalsIgnoreCase(action)) {
                    gestionCommande.annulerCommande();
                    gestionPanier.Vider();
                    request.setAttribute("commandeVerifiee", "annulé");
                    url = "/WEB-INF/magasin/commande.jsp";
                }

                if ("verification".equalsIgnoreCase(action)) {
                    System.out.println("verification transaction : ");
                    String transactionStatus = request.getParameter("transactionStatus");
                    if (transactionStatus != null) {
                        String transactionNumber = request.getParameter("transactionNumber");
                        if (transactionStatus == null) {
                            gestionCommande.generateTransaction(transactionStatus);
                        } else {
                            // Appel à ValidateTransaction avec parametres de payment
                        }

                        System.out.println("Etat transaction : " + transactionStatus);
                        //session.setAttribute( "commandeVerifiee", transactionStatus.equals("Validé") );
                        request.setAttribute("commandeVerifiee", transactionStatus);
                    }
                    url = "/WEB-INF/magasin/commande.jsp";
                }

                if ("afficher".equalsIgnoreCase(action)) {
                    gestionCommande.creerCommande(gestionPanier);
                    url = "/WEB-INF/magasin/commande.jsp";
                }

                request.setAttribute("CommandeVide", gestionCommande.estVide());
                request.setAttribute("maCommande", gestionCommande.Liste());
                session.setAttribute("prixTotal", gestionCommande.getCommande().getPrixTotal());
            }
        }
        //-------------------------------------------------------------------------------------------------//
        //------------------------Partie utilisateur et espace personnel samira---------------------------//

        //--------1)inscription utilisateur
        //--traitement formulaire inscription
        if ("utilisateur".equals(section)) {

            //afficher la page d'inscription
            if ("inscription".equals(action)) {

                if (request.getParameter("login") != null && request.getParameter("password") != null) {

                    String login = request.getParameter("login");
                    String password = request.getParameter("password");
                    try {

                        Utilisateur user = gestionUtilisateur.ajouterUtilisateur(login, password);
                        request.setAttribute("success", "Inscription effectuÃ©e");

                    } catch (ErreurMessage ex) {

                        request.setAttribute("login", login);
                        String mg = ex.getMessage();
                        messageErr = ex.getMapErr();
                        Set<String> keys = messageErr.keySet();
                        for (String k : keys) {
                            request.setAttribute(k, messageErr.get(k));
                        }
                    }
                    url = "/WEB-INF/jsp/inscription.jsp";
                }
            }

            //2 ---traitement connection----------------
            //afficher la page de connexion
            if ("connexion".equals(action)) {

                if (request.getParameter("login") != null && request.getParameter("password") != null) {
                    Utilisateur user = null;
                    String login = request.getParameter("login");
                    String password = request.getParameter("password");
                    try {

                        user = gestionUtilisateur.seconnecter(login, password);
                        //espace personnel
                        //recuperer l'adresse de facturation de l'utilisateur est
                        //l'afficher dans le formulaire de modification
//                       
                        Adresse adresse = gestionUtilisateur.recupererAdrFact(user);
                        session.setAttribute("adresse", adresse);
                        System.err.println("adresses-------->>" + adresse);
                        session.setAttribute("user", user);
                        url = "/WEB-INF/jsp/espace/index.jsp";
                    } catch (ErreurMessage ex) {
                        request.setAttribute("echec", "erreur de connexion");
                        request.setAttribute("login", login);
                        //String mg = ex.getMessage();
                        messageErr = ex.getMapErr();
                        Set<String> keys = messageErr.keySet();
                        for (String k : keys) {
                            request.setAttribute(k, messageErr.get(k));
                        }
                        url = "/WEB-INF/jsp/connection.jsp";
                    }
                }
            }
        }

        //--------------------------------------------------------------//
        //----------------------Les affichages--------------------------//
        //-----------espace personnel
        if ("espace".equals(section)) {
            if (session.getAttribute("user") == null) {
                url = "/WEB-INF/jsp/connection.jsp";

            } else {
                Utilisateur user = (Utilisateur) session.getAttribute("user");
                Adresse adresse = new Adresse();

                Collection<Adresse> adresses = new ArrayList();

                if (user.getAdresses() != null) {
                    adresses = user.getAdresses();

                    for (Adresse ad : adresses) {
                        if (ad.getType().equals("default")) {

                            adresse = ad;
                        }
                    }
                    session.setAttribute("adresse", adresse);
                }

                //afficher la page index de l'espace personnel
                if ("afficher".equals(action)) {
                    url = "/WEB-INF/jsp/espace/index.jsp";
                }
                //afficher le formulaire informations personnelles
                if ("afficherModif".equals(action)) {

                    url = "/WEB-INF/jsp/espace/informations.jsp";
                }

                //si action modifier les données
                if ("modifier".equals(action)) {

                    Adresse monAdresse = new Adresse();

                    if (request.getParameter("nom") != null) {
                        String nom = request.getParameter("nom").toLowerCase();
                        user.setNom(nom);

                    }
                    if (request.getParameter("prenom") != null) {
                        String prenom = request.getParameter("prenom");
                        user.setPrenom(prenom);

                    }
                    if (request.getParameter("telephone") != null) {
                        String telephone = request.getParameter("telephone");
                        user.setTelephone(telephone);

                    }
                    if (request.getParameter("rue") != null) {
                        String rue = request.getParameter("rue");
                        monAdresse.setRue(rue);
                    }
                    if (request.getParameter("cp") != null) {
                        String cp = request.getParameter("cp");
                        monAdresse.setCodePostal(cp);
                    }
                    if (request.getParameter("ville") != null) {
                        String ville = request.getParameter("ville");
                        monAdresse.setVille(ville);
                    }
                    if (request.getParameter("pays") != null) {
                        String pays = request.getParameter("pays");
                        monAdresse.setPays(pays);
                    }
                    if (request.getParameter("nomAdresse") != null) {
                        String nomAdresse = request.getParameter("nomAdresse");
                        monAdresse.setNom(nomAdresse);
                    }

                    if (request.getParameter("idAdresse") != null && !request.getParameter("idAdresse").isEmpty()  ) {

                        monAdresse.setId(Long.valueOf(request.getParameter("idAdresse")).longValue());

                    }
                    if (monAdresse != null) {
                        monAdresse.setType("default");
                    }

                    try {
                        Utilisateur userUpDate = gestionUtilisateur.modifierInformation(user, monAdresse);
                        session.setAttribute("adresse", monAdresse);
                        session.setAttribute("user", userUpDate);

                    } catch (ErreurMessage ex) {

                        //String mg = ex.getMessage();
                        messageErr = ex.getMapErr();
                        Set<String> keys = messageErr.keySet();
                        for (String k : keys) {
                            request.setAttribute(k, messageErr.get(k));
                        }

                        request.setAttribute("echec", "modifiaction echouée");
                    }

                    //session.setAttribute("user", user);
                    url = "/WEB-INF/jsp/espace/informations.jsp";

                }

                //------------------desinscription 
                if ("desinscrire".equals(action)) {
                    System.err.println("id dans controller---------" + user.getId());
                    gestionUtilisateur.desinscrire(user.getId());
                    session.removeAttribute("user");
                    url = "/WEB-INF/jsp/accueil.jsp";

                }

                //-----------------deconnexion--------------------------------
                if ("deconnexion".equals(action)) {

                    session.removeAttribute("user");
                    url = "/WEB-INF/jsp/connection.jsp";

                }

            }

        }

        //-affichage page inscription 
        if ("inscription".equals(section)) {

            if ("afficher".equals(action)) {

                url = "/WEB-INF/jsp/inscription.jsp";

            }

        }
        //------------------------------------------------------------------------------//
        //----------------affichage formulaire de connexion-----------------------------//
        //------------------------------------------------------------------------------//
        if ("connexion".equals(section)) {
            //afficher la page d'inscription
            if ("afficher".equals(action)) {
                url = "/WEB-INF/jsp/connection.jsp";
            }
        }

        //--------------------------------------------------------------------------------//
        //------------------------fin bloc samira-----------------------------------------//
        //--------------------------------------------------------------------------------//
        if ("donnees".equalsIgnoreCase(section)) {
            /*if ("creer".equalsIgnoreCase(action)) {
             //gestionCatalogue.creerDonnees();
             //gestionMembre.creerDonnees();
             gestionCatalogue.creerDonneesEli();
             updateCatalogue();
             request.setAttribute("message", "Creation du jeu de test terminÃ©e!");
             }*/
            if ("supprimer".equalsIgnoreCase(action)) {
                //gestionCatalogue.creerDonnees();
                //gestionMembre.creerDonnees();
                gestionCatalogue.supprimerDonnees();
                getServletContext().removeAttribute("monCatalogue");
                request.setAttribute("message", "Suppresion du jeu de test terminÃ©e!");
            }
            if ("restock".equalsIgnoreCase(action)) {
                gestionCatalogue.resetStock();
                request.setAttribute("message", "Repeupler le jeu de test terminÃ©e!");
            }
        }

        if ("achat".equalsIgnoreCase(section)) {
            if ("afficher".equalsIgnoreCase(action)) {
                String themeSearch=(String)request.getParameter("theme");
                //Envoie de la liste des thèmes vers la JSP
                request.setAttribute("themes", gestionCatalogue.selectAllTheme());
                request.setAttribute("sousTheme", gestionCatalogue.selectSousthemeByTheme(request.getParameter(themeSearch)));
                String page = request.getParameter("page");
                request.setAttribute("page", page);
                url = "/WEB-INF/jsp/achat.jsp";
            }
        }

        if ("panier".equalsIgnoreCase(section)) {
            if (session.getAttribute("panierWar") == null) {
                PanierWar panierWar = new PanierWar();
                session.setAttribute("panierWar", panierWar);
            }

            PanierWar panierWar = (PanierWar) session.getAttribute("panierWar");
            gestionPanier = panierWar.getGestionPanier();

            if (session.getAttribute("monPanier") == null) {
                // à  eviter .... c'est très pourri
                session.setAttribute("monPanier", gestionPanier.Liste());
            }

            if ("vider".equalsIgnoreCase(action)) {
                gestionPanier.Vider();
                updateCatalogue();
            }

            if ("afficher".equalsIgnoreCase(action)) {
                url = "/WEB-INF/magasin/panier.jsp";
            } else {
                String ref = request.getParameter("ref");
                if (ref != null) {
                    if ("ajouter".equalsIgnoreCase(action)) {
                        Integer quantite = 1;
                        if (request.getParameter("quantity") != null) {
                            try {
                                quantite = Integer.parseInt(request.getParameter("quantity"));
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                            }
                        }
                        gestionPanier.Ajouter(ref, quantite);
                    }

                    if ("inc".equalsIgnoreCase(action)) {
                        gestionPanier.Augmenter(ref);
                    }

                    if ("dec".equalsIgnoreCase(action)) {
                        gestionPanier.Diminuer(ref);
                    }

                    if ("sup".equalsIgnoreCase(action)) {
                        gestionPanier.Retirer(ref);
                    }
                    updateCatalogue();
                }

                if ("ajax".equalsIgnoreCase(origine)) {
                    url = "/WEB-INF/jsp/panier.jsp";
                } else {
                    response.sendRedirect("Controller?section=panier&action=afficher");
                    return;
                }
            }
            request.setAttribute("PanierVide", gestionPanier.estVide());
            request.setAttribute("monPanier", gestionPanier.Liste());
        }

        //--------------------------------------------------------------------------------
        //-------------------------Gestion du Catalogue-----------------------------------
        //--------------------------------------------------------------------------------
        if ("catalogue".equalsIgnoreCase(section)) {

            int rowsPerPage = 5;
            int nbrPage = 0;
            String isbn = "";
            String theme = "";
            String auteur = "";
            String titre = "";
            String motClef = "";
            String CritereRecherche = "";
            int count = 0;
            //Si on n'a pas de critère de recherche global 
            if (request.getParameter("CritereRecherche") != null) {
                CritereRecherche = request.getParameter("CritereRecherche");
                request.setAttribute("CritereRecherche", CritereRecherche);
            }
            if (request.getParameter("isbn") != null) {
                isbn = request.getParameter("isbn");
                request.setAttribute("isbn", isbn);
            }
            if (request.getParameter("theme") != null) {
                theme = request.getParameter("theme");
                request.setAttribute("theme", theme);
            }
            if (request.getParameter("auteur") != null) {
                auteur = request.getParameter("auteur");
                request.setAttribute("auteur", auteur);
                //session.setAttribute("auteur", auteur);
            }
            if (request.getParameter("titre") != null) {
                titre = request.getParameter("titre");
                request.setAttribute("titre", titre);
                //session.setAttribute("titre", titre);
            }
            if (request.getParameter("motClef") != null) {
                motClef = request.getParameter("motClef");
                request.setAttribute("motClef", motClef);
            }

            //Envoie de la liste des thèmes vers la JSP
            request.setAttribute("themes", gestionCatalogue.selectAllTheme());
            //le nombre de ligne par page
            request.setAttribute("rowsPerPage", rowsPerPage);
            // le nombre des éléments de la page
            request.setAttribute("monCatalogueCount", rowsPerPage);
            //Calcul de nombre de page
            count = ((Long) gestionCatalogue.countOuvrage(isbn, theme, titre, motClef, auteur, CritereRecherche)).intValue();
            //Pas de résultat de recherche 
            if (count == 0) {
                request.setAttribute("message01", "Aucun ouvrage ne correponds à vos critères de recherche!!");
                request.setAttribute("afficherPagination", false);

            } else {
                if (count <= rowsPerPage) {
                    request.setAttribute("afficherPagination", false);
                } else {
                    request.setAttribute("afficherPagination", true);
                }
            }
            int i = count / rowsPerPage;
            if (count % rowsPerPage != 0) {
                i += 1;
            }
            nbrPage = (int) Math.ceil(i);
            request.setAttribute("nbrPage", nbrPage);
            //Set du numéro de la page 
            int page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            //Dans le cas ou on est sur la dernièrre page on reste et on veut y aller à une page après
            if (page > nbrPage) {
                page = nbrPage;
            }
            // Dans le cas ou on est sur le premièrre page et on veut y aller à une page avant
            if (page <= 0) {
                page = 1;
            }
            request.setAttribute("currentPage", page);
            updateCatalogue(page * rowsPerPage - rowsPerPage, rowsPerPage, isbn, theme, titre, motClef, auteur, CritereRecherche);
        if("listSousTheme".equalsIgnoreCase(action)){
            //System.out.println("Je suis passé par là");
            String themeSearch=(String)request.getParameter("theme");
            //System.out.println("theme: "+ themeSearch);
            System.out.println("sousTheme: "+gestionCatalogue.selectSousthemeByTheme(themeSearch)+"size sous theme :"+ gestionCatalogue.selectSousthemeByTheme(themeSearch).size());
            request.setAttribute("sousTheme", gestionCatalogue.selectSousthemeByTheme(request.getParameter(themeSearch)));
            url="/WEB-INF/jsp/achat.jsp";
        }

            if ("afficher".equalsIgnoreCase(action)) {
                url = "/WEB-INF/magasin/catalogue.jsp";
            }
            if ("rechercher".equalsIgnoreCase(action)) {
                //Envoie de la liste des thèmes vers la JSP
                request.setAttribute("themes", gestionCatalogue.selectAllTheme());
                url = "/WEB-INF/jsp/achat.jsp";
            }

            if ("creer".equalsIgnoreCase(action)) {
                gestionCatalogue.creerDonnees();
                request.setAttribute("message", "la création de données a été faites avec succès!!!");
            }
            if ("afficherDetail".equalsIgnoreCase(action)) {
                isbn = request.getParameter("ref");
                Ouvrage ouv = gestionCatalogue.selectOuvrage(isbn);
                request.setAttribute("ouvrage", ouv);
                url = "/WEB-INF/magasin/detailOuvrage.jsp";
            }
        }
        //--------------------------------------------------------------------------------
        //-------------------------Entete - footer -----------------------------------
        //--------------------------------------------------------------------------------
        if ("entete".equalsIgnoreCase(section)) {
            String page = request.getParameter("page");
            request.setAttribute("page", page);
            url = "/WEB-INF/entete_footer/entete.jsp";
        }

        if ("banniere".equalsIgnoreCase(section)) {
            Date d = new Date();
            request.setAttribute("today", d);
            url = "/WEB-INF/entete_footer/banniere.jsp";
        }

        if ("footer".equalsIgnoreCase(section)) {
            String page = request.getParameter("page");
            request.setAttribute("page", page);
            url = "/WEB-INF/entete_footer/footer.jsp";
        }

        //System.out.println(">>>>>>>>>>>>>>>>>>>  URL = " + url);
        url = response.encodeURL(url);
        getServletContext().getRequestDispatcher(url).include(request, response);
    }

    private void updateCatalogue() {
        List<Ouvrage> lp = gestionCatalogue.selectAllOuvrage();
        getServletContext().setAttribute("monCatalogue", lp);
    }

    private void updateCatalogue(int debut, int fin, String isbn, String theme, String titre, String MotClef, String auteur, String CritereRecherche) {
        List<Ouvrage> lp = gestionCatalogue.rechercher(debut, fin, isbn, theme, titre, MotClef, auteur, CritereRecherche);
        //System.out.println("lp size : " + lp.size());
        getServletContext().setAttribute("monCatalogue", lp);
    }

    private GestionUtilisateurLocal lookupGestionUtilisateurLocal() {
        try {
            Context c = new InitialContext();
            return (GestionUtilisateurLocal) c.lookup("java:global/AfpaLibJee/AfpaLibJee-ejb/GestionUtilisateur!beansession.GestionUtilisateurLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
