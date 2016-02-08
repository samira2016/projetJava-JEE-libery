/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansession;

import entites.Adresse;
import entites.Utilisateur;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import outils.Criptage;
import outils.ErreurMessage;
import outils.Validator;

/**
 *
 * @author fafiec103 fait le 08/09/2015 gestion d'inscription de modification
 * d'un utilisateur
 */
@Stateful
public class GestionUtilisateur implements GestionUtilisateurLocal {

    @PersistenceContext(unitName = "AfpaLibJee-ejbPU")
    private EntityManager em;
    //collection des erreurs

    //--------------recherche utilisateur par id 
    @Override
    public Utilisateur findById(long id) throws ErreurMessage {
        HashMap<String, String> mgErr = new HashMap();
        Utilisateur user = null;
        String req = "select u from Utilisateur u "
                + "where u.id=:paramId ";
        Query qr = em.createQuery(req);
        qr.setParameter("paramId", id);

        try {

            user = (Utilisateur) qr.getSingleResult();
            return user;

        } catch (NoResultException ex) {
            mgErr.put("ErrSearch", "Login introuvable!");
            throw new ErreurMessage(mgErr, "Erreur recherche.........");

        }

    }

//---------------recherche utilisateur par login -------------------
    @Override
    public boolean findLogin(String login) throws ErreurMessage {
        HashMap<String, String> mgErr = new HashMap();
        String req = "select COUNT(u) from Utilisateur u "
                + "where u.login=:paramLogin and u.actif=1";
        Query qr = em.createQuery(req);
        qr.setParameter("paramLogin", login);
        try {

            Long lg = (Long) qr.getSingleResult();
            if (lg > 0) {
                return true;

            }

        } catch (NoResultException ex) {
            mgErr.put("ErrSearch", "Login ou mot de passe introuvable!");
            throw new ErreurMessage(mgErr, "Erreur connexion.........");
        }

        return false;
    }

    //---------------recherche utilisateur par login -et retourne un utilisateur------------------
    @Override
    public Utilisateur searchByLogin(String login) throws ErreurMessage {

        HashMap<String, String> mgErr = new HashMap();
        String req = "select u from Utilisateur u "
                + "where u.login=:paramLogin and u.actif=1";
        Query qr = em.createQuery(req);
        qr.setParameter("paramLogin", login);

        try {

            Utilisateur user = (Utilisateur) qr.getSingleResult();
            return user;

        } catch (NoResultException ex) {
            mgErr.put("ErrSearch", "Login introuvable!");
            throw new ErreurMessage(mgErr, "Erreur recherche.........");

        }

    }

    //---------------recherche utilisateur par login et password------------------
    @Override
    public Utilisateur findUtilisateur(String login, String password) throws ErreurMessage {
        HashMap<String, String> mgConn = new HashMap();
        Utilisateur user = null;
        String req = "select u from Utilisateur u "
                + "where u.login=:paramLogin and u.password=:paramPass and u.actif=1";
        Query qr = em.createQuery(req);
        qr.setParameter("paramLogin", login);
        qr.setParameter("paramPass", password);

        try {

            user = (Utilisateur) qr.getSingleResult();

        } catch (NoResultException ex) {
            mgConn.put("ErrConn", "Login ou mot de passe invalide!");
            throw new ErreurMessage(mgConn, "Erreur connexion.........");

        }

        return user;
    }
//-----------inscription d'un nouveau utilisateur ----------------------

    @Override
    public Utilisateur ajouterUtilisateur(String login, String password) throws ErreurMessage {
        HashMap<String, String> mgInscp = new HashMap();
        // boolean result;

        Utilisateur user = new Utilisateur();
        if (login != null) {
            login = login.trim();
            login = login.toLowerCase();
        }

        if (findLogin(login)) {
            mgInscp.put("errLogin", "Login existe deja!");

        } else {

            //Validator.validLogin(login);
            if (!Validator.validLogin(login)) {

                mgInscp.put("errLogin", "Login invalide!");

            }

            if (!Validator.validPassword(password)) {
                mgInscp.put("errPassword", "Votre mot de passe doit etre compris entre 6 et 12 caracteres!");
            }

        }
        if (!mgInscp.isEmpty()) {
            throw new ErreurMessage(mgInscp, "Erreur d'inscription............");
        }
        //login et password sont valides
        //persister le nouveau utilisateur
        //cripter le mot de passe 

        try {
            password = Criptage.Passe(password);
            System.err.println("mot cripter---------->>" + password);
        } catch (Exception e) {

            throw new ErreurMessage(mgInscp, "Erreur d'inscription..........");
        }

        user.setLogin(login);
        user.setPassword(password);
        user.setActif(1);
        user.setDateInscription(new Date());
        em.persist(user);
        return user;
    }

    //---------------------seconnecter-------------
    @Override
    public Utilisateur seconnecter(String login, String password) throws ErreurMessage {
        HashMap<String, String> mgConn = new HashMap();
        Utilisateur user = null;

        if (login != null) {
            login = login.trim();
        }
        if (password != null) {

            password = Criptage.Passe(password);
        }
        if (findUtilisateur(login, password) != null) {
            user = findUtilisateur(login, password);
           
            //recuperer l'adresse par default de l'utilisateur si elle existe
             Adresse adresse=new Adresse();
            
                Collection<Adresse> adresses = new ArrayList();

                if (user.getAdresses()!=null) {
                    adresses = user.getAdresses();

                    for (Adresse ad : adresses) {
                        if (ad.getType().equals("default")) {
                            adresse = ad;
                        }
                    }
                   // user.setDefaut(adresse);
                }
            
            //return user;
        }
        return user;
    }

    //-----------recuperer les adresses un utilisateur
    @Override
    public Collection recupererAdresses(Utilisateur user) throws ErreurMessage {
        
        System.err.println("Dans recuperAdress=================>>>");
        
        Collection<Adresse> adresses=new ArrayList();
        System.out.println("user nom>>>>>>>>>>>>>>>>><"+ user.getNom());
                Utilisateur userRecup = searchByLogin(user.getLogin());
           // Utilisateur userRecup = findById(user.getId());
            String nom = userRecup.getNom();
            System.out.println("nom >>>>>>>>>>>>>>>>>>>>>>>>"+ nom);
            adresses = userRecup.getAdresses();
             System.err.println("taille adresses==========>>>>"+adresses.size());
            return adresses;
       
       

    }
    //recuperer l'adresse de facturation (adresse par defaut)

    @Override
    public Adresse recupererAdrFact(Utilisateur user) throws ErreurMessage {
        System.err.println("Dans recuperAdressFact=================>>>");
        HashMap<String, String> mgInscp = new HashMap();
        Adresse adresse = null;
        if (user != null) {

            Collection<Adresse> adresses = new ArrayList();
            try {
                adresses = recupererAdresses(user);
                
                if (user.getAdresses().size() != 0) {
                    for (Adresse ad : adresses) {
                        if (ad.getType().equals("facturation")) {
                            adresse = ad;
                            System.err.println("if adresse fact========>>>"+adresse);
                        }
                    }
                }

            } catch (ErreurMessage ex) {

                 throw new ErreurMessage(mgInscp, "Erreur de recherche..........");
            }

        }
        System.err.println("return  adresse fact========>>>"+adresse);
        return adresse;
    }

    @Override
    public Utilisateur  modifierInformation(Utilisateur user,Adresse adresse) throws ErreurMessage {
        System.err.println("premier ---------->>>>"+adresse.getId());

        HashMap<String, String> mpModif = new HashMap();

        Utilisateur userToUpdate = null;

        userToUpdate = searchByLogin(user.getLogin());

        if (userToUpdate != null) {
            if (userToUpdate.getId() == user.getId()) {
                System.err.println("uti------------>>" + user.getId());
                //modifier le nom
                if (user.getNom() != null) {
                    userToUpdate.setNom(user.getNom());

                }
                //modifier le prenom
                if (user.getPrenom() != null) {
                    userToUpdate.setPrenom(user.getPrenom());

                }

                //modifier le numero de telephone
                if (user.getTelephone() != null) {
                    userToUpdate.setTelephone(user.getTelephone());

                }

                
               if(userToUpdate.getAdresses().size()==0){
                    System.err.println("dans if taille 0");
                   userToUpdate.getAdresses().add(adresse);
                   
                }else{
                    
                    for(Adresse ad:userToUpdate.getAdresses()){
                        if(ad.getId()==adresse.getId()){                       
                            ad.setCodePostal(adresse.getCodePostal());
                            ad.setRue(adresse.getRue());
                            ad.setPays(adresse.getPays());
                            ad.setVille(adresse.getVille());                        
                        }
                    }
                }
                
                em.persist(userToUpdate);
                return userToUpdate;
            }
        }
      
        return null;

    }

    //------------------------------se desinscrire du site en gardant l'utilisateur dans la base de donnée avec 
    //champs actif =0
    @Override
    public void desinscrire(long id) {
        System.err.println("id------>>" + 1);
        String query = "select u from Utilisateur u where u.id=:paramId";
        Query qr = em.createQuery(query);
        qr.setParameter("paramId", id);
        Utilisateur user = (Utilisateur) qr.getSingleResult();
        user.setActif(0);
        em.persist(user);

    }
//    public void persist(Object object) {
//        em.persist(object);
//    }
}
