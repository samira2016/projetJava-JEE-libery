/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansession;

import entites.Adresse;
import entites.Utilisateur;
import java.util.Collection;
import javax.ejb.Local;
import outils.ErreurMessage;

/**
 *
 * @author fafiec103
 */
@Local
public interface GestionUtilisateurLocal {

    

    public Utilisateur ajouterUtilisateur(String login, String password) throws ErreurMessage;

    public Utilisateur seconnecter(String login, String password) throws ErreurMessage;

   
    public Utilisateur findUtilisateur(String login, String password)throws ErreurMessage;

    public boolean findLogin(String login)throws ErreurMessage;

    public Utilisateur  modifierInformation(Utilisateur user,Adresse adresse) throws ErreurMessage;;

    public Utilisateur searchByLogin(String login)throws ErreurMessage;;

    public void desinscrire(long id);

    public Utilisateur findById(long id) throws ErreurMessage;

    public Adresse recupererAdrFact(Utilisateur user) throws ErreurMessage;

    public Collection recupererAdresses(Utilisateur user) throws ErreurMessage;

    

    
    
}
