/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outilsWar;

import beansession.GestionLigneCommandeLocal;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class CommandeWar implements Serializable{
    GestionLigneCommandeLocal gestionLigneCommande;

    public CommandeWar() {
        gestionLigneCommande = lookupGestionLigneCommandeLocal();
    }

    public GestionLigneCommandeLocal getGestionLigneCommande() {
        return gestionLigneCommande;
    }

    private GestionLigneCommandeLocal lookupGestionLigneCommandeLocal() {
        try {
            Context c = new InitialContext();
            return (GestionLigneCommandeLocal) c.lookup("java:global/AfpaLibJee/AfpaLibJee-ejb/GestionLigneCommande!beansession.GestionLigneCommandeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
