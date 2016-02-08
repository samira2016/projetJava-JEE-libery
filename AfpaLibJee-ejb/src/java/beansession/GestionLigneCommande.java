/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansession;

import constants.StatutCommande;
import constants.StatutPaiement;
import entites.Adresse;
import entites.Auteur;
import entites.CodePromo;
import entites.Commande;
import entites.LigneCommande;
import entites.Operation;
import entites.Ouvrage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author fafiec104
 */
@Stateful
public class GestionLigneCommande implements GestionLigneCommandeLocal {
         
    @EJB
    private GestionUtilisateurLocal utilisateur;
     
    @EJB
    private GestionCatalogueLocal catalogue;
    
    @EJB
    private GestionPanierLocal panier;
     
    @PersistenceContext(unitName = "AfpaLibJee-ejbPU")
    private EntityManager em;
        
    Commande maCommande;
    HashMap<String, LigneCommande> map;
    
    @PostConstruct
    public void init() {
        this.map = new HashMap();
    }

    public GestionPanierLocal getPanier() {
        return panier;
    }

    public void setPanier(GestionPanierLocal panier) {
        this.panier = panier;
    }
    
    @Override
    public void generateTransaction(String status )
    {
        if(maCommande != null)
        {
            //Long randomTransationNumber = new Long( (long) Math.random() * 10000000000L );
            int randomTransationNumber = (int) Math.random() * 1000000000;
            validateTransaction("Card", "542178XXXXXX3500",
                                randomTransationNumber, status, maCommande.getPrixTotal());
        }
    }
    
    @Override
    public void validateTransaction(String paymentType, String paymentID, 
                    int transactionNumber, String status, float amount )
    {
        if( maCommande != null && maCommande.getStatus().equals(StatutCommande.COMPLETE) )
        {
            Operation newOperation = new Operation( paymentType, paymentID, 
                                                    transactionNumber, // Devrait être un string?
                                                    status, amount, new Date());
            
            // Il faut completer les methodes toString des constantes pour les utiliser ici sans besoin de test
            if("Validé".equals(status))
            {
                newOperation.setStatus(StatutPaiement.VALIDATED.toString());
            }
            else if("Echoué".equals(status)) {
                newOperation.setStatus(StatutPaiement.REJECTED.toString());
            }
            else {
                newOperation.setStatus(StatutPaiement.WAITING.toString());
            }
            
            newOperation.setCommande(maCommande);
            maCommande.getTransactions().add(newOperation);
            
            update(newOperation);
        }
    }
    
    @Override
    public void creerCommande(GestionPanierLocal panier) // TODO : faire if(panier.changed)
    {
        if( maCommande != null && maCommande.getStatus().equals(StatutCommande.COMPLETE) )
        {
            annulerCommande();
        }
        
        float prixTotal = 0f; 
        maCommande = new Commande(new Date(), 0f, StatutCommande.INCOMPLETE.ordinal());   
        
        if(!panier.estVide())
        {
            Collection<Ouvrage> listeAchats = panier.Liste(); 
                   
            for(Ouvrage achat : listeAchats)
            {
                Ouvrage ouvrageCommande = catalogue.selectOuvrage(achat.getIsbn());
                
                if( ouvrageCommande != null )
                {
                    LigneCommande ligneCommande = new LigneCommande(ouvrageCommande.getPrixTTC(), ouvrageCommande.getPrixHT(), achat.getStock());
                    ligneCommande.setOuvrage(ouvrageCommande);
                    
                    float prixTotalAchat = applyReduction(ouvrageCommande.getCodePromo(), achat.getPrixTTC());
                    prixTotal += prixTotalAchat; // getPrixTTC doit prendre ne compte la TVA et le codePromo associé
                    
                    map.put(achat.getIsbn(), ligneCommande);
                    ligneCommande.setCommande(maCommande);
                    // update(ligneCommande); 
                    // Il faudra persister ca peut-etre après la validation une fois qu'on aura le prixTotal definitif
                }
            }
            maCommande.setLigneCommande(map.values()); // Necessaire selon sens de cascade?
        }
        maCommande.setPrixTotal(prixTotal);
    }
    
    public void completerCommande()
    {
        // Il faut verifier la completude de la commande (adresseLivraison, adresseFacturation)
        // appliquer les codepromo et calculer le total final
        if( maCommande.getCodePromo() != null ) // && isValid => Interface validator Samira
        {
            // Appliquer reduction via code promo
            maCommande.setPrixTotal( applyReduction( maCommande.getCodePromo(), 
                                                     maCommande.getPrixTotal() ) );
        }
        
        if(   maCommande.getAdresseLivraison() != null // && isValid => Interface validator Samira
           && maCommande.getAdresseFacturation() != null // && isValid => Interface validator Samira
           )
        {
            maCommande.setStatus(StatutCommande.COMPLETE.ordinal());
            update(maCommande);   
        }
            
    }
    
    @Override
    public void validerCommande()
    {
        // Après paiement
        if(maCommande.getLastSuccesfulTransaction() != null)
        {
            // Update statut commande
            maCommande.setStatus(StatutCommande.CONFIRMED.ordinal());
            update(maCommande);
            
            // Detruire panier
            if(panier != null)
            {
                panier.Vider();
            }
            // Declencher livraison / afficher recapitulatif
            
            // TODO : penser a renouveller commande?
        }
        else
        {   
            // TODO : Try > 3 => releaseStock / Lock commande
            // Timer for release
            if( maCommande.getTransactions().size() >= 3 )
            {
                annulerCommande();
            }
        }
        
    }
    
    @Override
    public void annulerCommande()
    {
        map.clear();
        releaseStock(maCommande);
        // Le panier doit être vidé aussi
        updateAll(maCommande);
    }
    
    @Override
    public boolean estVide() {
        return map.isEmpty();
    }
    
     @Override
    public Collection<LigneCommande> Liste() {
        return map.values();
    }
    
    @Override
    public Commande getCommande() {
        return maCommande;
    }

    // A mettre dans GestionCatalogue plutot?
    private float applyReduction(CodePromo codePromoOuvrage, float prixTotalAchat) {
        if( codePromoOuvrage != null ) // && isValid => Interface validator Samira
            // penser à methode hasCodePromo ?
        {
            Date today = new Date();
            float montantReduction = codePromoOuvrage.getReduction();
            if(   codePromoOuvrage.isTypeReduc() // TODO add TypeReduc as Enum of applicable items (COmmande / Ouvrage)
               && today.after( codePromoOuvrage.getDateDebut() )
               && today.before(codePromoOuvrage.getDateFin() )
               && ( montantReduction < prixTotalAchat ) )
            {
                prixTotalAchat -= montantReduction;
            }
            else
            {
                prixTotalAchat = (1 - codePromoOuvrage.getReduction()) * prixTotalAchat;
            }
        }
        return prixTotalAchat;
    }
    
    private void releaseStock(Commande commandeToClear)
    {
        for( LigneCommande ligneComToUpdate : commandeToClear.getLigneCommande() )
        {
            Ouvrage achat = ligneComToUpdate.getOuvrage();
            Ouvrage ouvrageCommande = catalogue.selectOuvrage(achat.getIsbn());
                
            if( ouvrageCommande != null )
            {
                ouvrageCommande.setStock( ouvrageCommande.getStock() + achat.getStock() );
                catalogue.update(ouvrageCommande);
            }
        }        
    }
    
    private void updateAll(Commande commandeToUpdate)
    {
        for( Operation opToUpdate : commandeToUpdate.getTransactions() )
        {
            update(opToUpdate);
        }
        // ? Moyen d'Optimiser la sauvegarde en detectant les opérations et lignes qui déjà été enregistrées? 
        for( LigneCommande ligneComToUpdate : commandeToUpdate.getLigneCommande() )
        {
            update(ligneComToUpdate);
        }
        
        if(   commandeToUpdate.getTransactions().isEmpty() 
           && commandeToUpdate.getLigneCommande().isEmpty() )
        {
            update(commandeToUpdate);
        }
    }
    
    private void update(Commande commandeToUpdate)
    {
        em.persist(commandeToUpdate);
    }
    
    private void update(LigneCommande ligneCommandeToUpdate)
    {        
        em.persist(ligneCommandeToUpdate);
    }
    
    private void update(Operation operationToUpdate)
    {        
        em.persist(operationToUpdate);
    }  
     
     
    @Override
     public Collection<LigneCommande> getLignes(){
        //Collection<Ouvrage> liste1 = panier.Liste(); 
        Collection<Ouvrage> liste1 = new ArrayList();
        Ouvrage ouv1 = new Ouvrage("Titre01","SousTitre01","1234",23.45f);
        Ouvrage ouv2 = new Ouvrage("Titre02","SousTitre02","1234",23.45f);
        Ouvrage ouv3 = new Ouvrage("Titre03","SousTitre03","1234",23.45f);
        Ouvrage ouv4 = new Ouvrage("Titre04","SousTitre04","1234",23.45f);
        Ouvrage ouv5 = new Ouvrage("Titre05","SousTitre05","1234",23.45f);
        Auteur auteur01 = new Auteur("NOM01","prenom01");
        Auteur auteur02 = new Auteur("NOM02","prenom02");
        Auteur auteur03 = new Auteur("NOM03","prenom03");
        Auteur auteur04 = new Auteur("NOM04","prenom04");
        Auteur auteur05 = new Auteur("NOM05","prenom05");
        Collection<Auteur> a1 = new ArrayList();
        a1.add(auteur01);
        Collection<Auteur> a2 = new ArrayList();
        a2.add(auteur02);
        Collection<Auteur> a3 = new ArrayList();
        a3.add(auteur03);
        Collection<Auteur> a4 = new ArrayList();
        a4.add(auteur04);
        Collection<Auteur> a5 = new ArrayList();
        a5.add(auteur05);
        ouv1.setAuteurs(a1);
        ouv2.setAuteurs(a2);
        ouv3.setAuteurs(a3);
        ouv4.setAuteurs(a4);
        ouv5.setAuteurs(a5);
        

        LigneCommande ligne1 = new LigneCommande(23.5f,20.1f,3);
        ligne1.setOuvrage(ouv1);
        LigneCommande ligne2 = new LigneCommande(22f,17f,2);
        ligne2.setOuvrage(ouv2);
        LigneCommande ligne3 = new LigneCommande(24f,25f,1);
        ligne3.setOuvrage(ouv3);
        LigneCommande ligne4 = new LigneCommande(27f,28f,2);
        ligne4.setOuvrage(ouv4);
        LigneCommande ligne5 = new LigneCommande(29f,21f,20);
        ligne5.setOuvrage(ouv5);
        
        Collection<LigneCommande> liste = new ArrayList();
        liste.add(ligne1);
        liste.add(ligne2);
        liste.add(ligne3);
        liste.add(ligne4);
        liste.add(ligne5);
        
        return liste;
    }
    
     @Override
    public float getPrixTotal(){
        ArrayList<LigneCommande> lignesCommande = (ArrayList<LigneCommande>) getLignes();
        float prixTotal=0;
        
        for (LigneCommande achat : lignesCommande) {
            prixTotal += (achat.getPrixTTC() * achat.getNombreArticle());
        }
        return prixTotal;
    }
    
     @Override
    public Collection<String> getAdresseFacturation(){
        
        Adresse adFacturation = new Adresse("50","JeanJaures","Cergy","92530","avenue","SCHAFF");

        ArrayList<String> adresse = new ArrayList();
        String ligne1 = adFacturation.getNom();
        String ligne2 = adFacturation.getRue()+" "+adFacturation.getType()+" "+adFacturation.getRue();
        String ligne3 = adFacturation.getCodePostal()+" "+adFacturation.getVille();
        
        adresse.add(ligne1);
        adresse.add(ligne2);
        adresse.add(ligne3);
        
        return adresse;
    }
    
     @Override
    public Collection<String> getAdresseLivraison(){
        Adresse adLivraison = new Adresse("11","de Paris","Boulogne Billancourt","92100","rue","CONCAS");

        ArrayList<String> adresse = new ArrayList();
        String ligne1 = adLivraison.getNom();
        String ligne2 = adLivraison.getRue()+" "+adLivraison.getType()+" "+adLivraison.getRue();
        String ligne3 = adLivraison.getCodePostal()+" "+adLivraison.getVille();
        
        adresse.add(ligne1);
        adresse.add(ligne2);
        adresse.add(ligne3);
        
        return adresse;
    }

     
}
