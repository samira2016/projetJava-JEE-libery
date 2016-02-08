package entites;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Adresse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rue;
    private String ville;
    private String codePostal;
    private String pays;
    private String  type;//valeur possible livraison /facturation
    private String nom;
    

    
    @OneToMany(mappedBy = "adresseLivraison")
    private Collection<Commande> listeLivraisonCommande;
    @OneToMany(mappedBy = "adresseFacturation")
    private Collection<Commande> listeFacturationCommande;

    @ManyToMany(mappedBy = "adresses")
    private Collection<Utilisateur> users;

    public Adresse() {
        listeLivraisonCommande = new ArrayList();
        listeFacturationCommande = new ArrayList();
    }

    public Adresse(String numRue, String nomRue, String ville, String codePostal, String type, String nom) {
        this();
        this.rue = numRue;
        
        this.ville = ville;
        this.codePostal = codePostal;
        this.type = type;
        this.nom = nom;
    }

    public Collection<Commande> getListeLivraisonCommande() {
        return listeLivraisonCommande;
    }

    public void setListeLivraisonCommande(Collection<Commande> listeLivraisonCommande) {
        this.listeLivraisonCommande = listeLivraisonCommande;
    }

    public Collection<Commande> getListeFacturationCommande() {
        return listeFacturationCommande;
    }

    public void setListeFacturationCommande(Collection<Commande> listeFacturationCommande) {
        this.listeFacturationCommande = listeFacturationCommande;
    }

    public Collection<Utilisateur> getUsers() {
        return users;
    }

    public void setUsers(Collection<Utilisateur> users) {
        this.users = users;
    }

    public String getNumRue() {
        return rue;
    }

    public void setNumRue(String numRue) {
        this.rue = numRue;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
       
        if (!(object instanceof Adresse)) {
            return false;
        }
        Adresse other = (Adresse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))
                
                ||(this.ville.equals(other.getVille()))||(this.codePostal.equals(other.getCodePostal()))
                ||(this.rue.equals(other.getRue()))||(this.ville.equals(other.getVille()))
                
                ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "afpalib.Adresse[ id=" + id + " ]";
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }
    
    
    

}
