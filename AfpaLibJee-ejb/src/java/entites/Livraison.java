
package entites;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Livraison implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String livreur;
    private Float montant;
    private Float poidsMax;
    private Float poidsMin;
    private Integer delai;
    private String zonePays;
    
    @OneToMany(mappedBy = "livraison")
    private Collection<Commande> commande;

    public Livraison() {
        commande = new ArrayList();
    }

    public Livraison(String livreur, Float montant, Float poidsMax, Float poidsMin, Integer delai, String zonePays) {
        this();
        this.livreur = livreur;
        this.montant = montant;
        this.poidsMax = poidsMax;
        this.poidsMin = poidsMin;
        this.delai = delai;
        this.zonePays = zonePays;
    }

    public String getLivreur() {
        return livreur;
    }

    public void setLivreur(String livreur) {
        this.livreur = livreur;
    }

    public Float getMontant() {
        return montant;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public Float getPoidsMax() {
        return poidsMax;
    }

    public void setPoidsMax(Float poidsMax) {
        this.poidsMax = poidsMax;
    }

    public Float getPoidsMin() {
        return poidsMin;
    }

    public void setPoidsMin(Float poidsMin) {
        this.poidsMin = poidsMin;
    }

    public Integer getDelai() {
        return delai;
    }

    public void setDelai(Integer delai) {
        this.delai = delai;
    }

    public String getZonePays() {
        return zonePays;
    }

    public void setZonePays(String zonePays) {
        this.zonePays = zonePays;
    }

    public Collection<Commande> getCommande() {
        return commande;
    }

    public void setCommande(Collection<Commande> commande) {
        this.commande = commande;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Livraison)) {
            return false;
        }
        Livraison other = (Livraison) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entites.Livraison[ id=" + id + " ]";
    }
    
}
