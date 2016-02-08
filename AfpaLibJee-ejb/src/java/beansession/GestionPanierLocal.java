/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansession;

import entites.Ouvrage;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author fafiec107
 */
@Local
public interface GestionPanierLocal {
        
    public void Ajouter( String ref) ;
    
    public void Ajouter( String ref, int qte) ;
    
    public void Augmenter( String ref ) ;
    
    public void Diminuer( String ref ) ;
    
    public void Retirer( String ref ) ;
    
    public int nbArticle() ;
    
    public int prixTotal() ;
        
    public boolean estVide() ;
    
    public Collection<Ouvrage> Liste() ;
    
    public void Vider() ;
}
