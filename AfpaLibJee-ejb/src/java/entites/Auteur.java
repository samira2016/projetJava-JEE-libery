
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
public class Auteur implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomAuteur;
    private String prenomAuteur;
    
    @ManyToMany(mappedBy = "auteurs")
    private Collection<Ouvrage> ouvrages;

    public Auteur() {
        ouvrages = new ArrayList();
    }

    public Auteur(String nomAuteur, String prenomAuteur) {
        this();
        this.nomAuteur = nomAuteur;
        this.prenomAuteur = prenomAuteur;
    }
    
    

    public String getNomAuteur() {
        return nomAuteur;
    }

    public void setNomAuteur(String nomAuteur) {
        this.nomAuteur = nomAuteur;
    }

    public String getPrenomAuteur() {
        return prenomAuteur;
    }

    public void setPrenomAuteur(String prenomAuteur) {
        this.prenomAuteur = prenomAuteur;
    }

    public Collection<Ouvrage> getOuvrages() {
        return ouvrages;
    }

    public void setOuvrages(Collection<Ouvrage> ouvrages) {
        this.ouvrages = ouvrages;
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
        if (!(object instanceof Auteur)) {
            return false;
        }
        Auteur other = (Auteur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Auteur{" + "id=" + id + ", nom=" + nomAuteur + ", prenom=" + prenomAuteur+"}";
    }
    
}
