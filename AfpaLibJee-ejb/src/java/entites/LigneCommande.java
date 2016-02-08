
package entites;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
public class LigneCommande implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Float prixTTC;
    private Float prixHT;
    private Integer nombreArticle;
   
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Commande commande;
    
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Ouvrage ouvrage;

    public LigneCommande() {
    }

    public LigneCommande(Float prixTTC, Float prixHT, Integer nombreArticle) {
        this.prixTTC = prixTTC;
        this.prixHT = prixHT;
        this.nombreArticle = nombreArticle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPrixTTC() {
        return prixTTC;
    }

    public void setPrixTTC(Float prixTTC) {
        this.prixTTC = prixTTC;
    }

    public Float getPrixHT() {
        return prixHT;
    }

    public void setPrixHT(Float prixHT) {
        this.prixHT = prixHT;
    }

    public Integer getNombreArticle() {
        return nombreArticle;
    }

    public void setNombreArticle(Integer nombreArticle) {
        this.nombreArticle = nombreArticle;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Ouvrage getOuvrage() {
        return ouvrage;
    }

    public void setOuvrage(Ouvrage ouvrage) {
        this.ouvrage = ouvrage;
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
        if (!(object instanceof LigneCommande)) {
            return false;
        }
        LigneCommande other = (LigneCommande) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.LigneCommande[ id=" + id + " ]";
    }
    
}
