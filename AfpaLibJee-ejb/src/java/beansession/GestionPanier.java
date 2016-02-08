/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansession;

import entites.Ouvrage;
import java.util.Collection;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Stateful;

/**
 *
 * @author fafiec107
 */
@Stateful
public class GestionPanier implements GestionPanierLocal {

    HashMap<String, Ouvrage> map;
    @EJB
    private GestionCatalogueLocal catalogue;
    
    
    @PostConstruct
    public void init() {
        this.map = new HashMap();
    }
        
    @Override
    public void Ajouter( String ref) {
        Ajouter(ref, +1);
    }
    
    @Override
    public void Ajouter( String ref, int qte)
    {               
        Ouvrage item = null;

        if( map.containsKey(ref) ) {
            item = map.get(ref);
        }
        else
        {
            item = new Ouvrage ("", ref, 0f, 0);
            updateItemFromEntity(ref, item);
            map.put(ref, item );
        }            
        Update(item, item.getStock() + qte);
        
        if( item.getStock() <= 0)
        {
            map.remove(ref, item);
        }
    }
    
    private void Update( Ouvrage item, int qte)
    {
        Ouvrage p = catalogue.selectOuvrage(item.getIsbn());
        
        if( p != null )
        {            
            int oldQte = item.getStock();
            int stockAfter = p.getStock() - (qte - oldQte);
            
            if( stockAfter >= 0 )
            {
                item.setPrixHT( p.getPrixHT() * qte );
                item.setStock(qte);

                p.setStock( stockAfter );
                catalogue.update(p);
            }
        }
    }
    
    @Override
    public void Augmenter( String ref ) {
        Ajouter(ref, +1);
    }
    
    @Override
    public void Diminuer( String ref ) {
        Ajouter(ref, -1);        
    }
    
    @Override
    public void Retirer( String ref )
    {
        if( map.containsKey(ref) ) {
            Ouvrage itemToRemove = map.get(ref);
            Retirer(itemToRemove);
        }
    }
    
    private void Retirer( Ouvrage itemToRemove ) {
        Update( itemToRemove, 0 );
        map.remove(itemToRemove.getIsbn(), itemToRemove);
    }
    
    @Override
    public int nbArticle() {
        return map.size();
    }
    
    @Override
    public int prixTotal() {
        return -1;
    }
        
    @Override
    public boolean estVide() {
        return map.isEmpty();
    }
    
    @Override
    public Collection<Ouvrage> Liste() {
        return map.values();
    }
    
    @Override
    //@PreDestroy
    public void Vider()
    {
        for(Ouvrage itemToRemove : map.values())
        {
            Update( itemToRemove, 0 );
        }
        map.clear();
    }

    private void updateItemFromEntity(String ref, Ouvrage item) {
        Ouvrage p = catalogue.selectOuvrage(item.getIsbn());
        
        if( p != null )
        {
            item.setTitre(p.getTitre());
            item.setSousTitre(p.getSousTitre());
            item.setImage(p.getImage());
        }
    }
}
