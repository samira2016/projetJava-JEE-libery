/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansession;

import entites.Ouvrage;
import entites.Theme;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author pham
 */
@Local
public interface GestionCatalogueLocal {

    public void creerDonnees();
    
    public void resetStock();

    public List<Ouvrage> selectAllOuvrage();
    
    public Ouvrage selectOuvrage(String ref);

    public void creerDonneesEli();
    
    public void update(Ouvrage ouvrage);

    public void supprimerDonnees();
    
   // public List<Ouvrage> rechercher(int debut, int nbrArticle, String isbn, String theme, String titre, String motClef, String auteur,String critere);

    public Long countOuvrage(String isbn, String theme, String titre, String motClef, String auteur);

    public List<Ouvrage> rechercher(int debut, int nbrArticle, String isbn, String theme, String titre, String motClef, String auteur, String critere);

    public Long countOuvrage(String isbn, String theme, String titre, String motClef, String auteur, String critere);

    public List<Theme> selectAllTheme();

    public List<Theme> selectSousthemeByTheme(String theme);
    
}
