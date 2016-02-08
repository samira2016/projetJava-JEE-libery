package entites;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Editeur implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomEditeur;
    private String adresseEditeur;
    
    @ManyToMany(mappedBy = "editeurs")
    private Collection<Ouvrage> ouvrages;

    public Editeur() {
        ouvrages = new ArrayList();
    }

    public Editeur(String nomEditeur, String adresseEditeur) {
        this();
        this.nomEditeur = nomEditeur;
        this.adresseEditeur = adresseEditeur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEditeur() {
        return nomEditeur;
    }

    public void setNomEditeur(String nomEditeur) {
        this.nomEditeur = nomEditeur;
    }

    public String getAdresseEditeur() {
        return adresseEditeur;
    }

    public void setAdresseEditeur(String adresseEditeur) {
        this.adresseEditeur = adresseEditeur;
    }

    public Collection<Ouvrage> getOuvrages() {
        return ouvrages;
    }

    public void setOuvrages(Collection<Ouvrage> ouvrages) {
        this.ouvrages = ouvrages;
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
        if (!(object instanceof Editeur)) {
            return false;
        }
        Editeur other = (Editeur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Editeur{" + "id=" + id + ", nom=" + nomEditeur + ", adresse=" + adresseEditeur+"}";
    }
    
}
