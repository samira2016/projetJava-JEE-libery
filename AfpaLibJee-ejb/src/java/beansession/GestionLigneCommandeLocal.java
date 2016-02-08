/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansession;

import entites.Commande;
import entites.LigneCommande;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author fafiec104
 */
@Local
public interface GestionLigneCommandeLocal {

    public Collection<LigneCommande> getLignes();

    public float getPrixTotal();

    public Collection<String> getAdresseFacturation();

    public Collection<String> getAdresseLivraison();

    public void creerCommande(GestionPanierLocal panier);

    public void validerCommande();

    public boolean estVide();

    public Collection<LigneCommande> Liste();

    public void annulerCommande();

    public Commande getCommande();

    public void generateTransaction(String status);

    public void validateTransaction(String paymentType, String paymentID, int transactionNumber, String status, float amount);
    
}
