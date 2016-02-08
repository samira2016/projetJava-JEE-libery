
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
public class MotCles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String motCle;
    
    @ManyToMany(mappedBy = "motsCle")
    private Collection<Ouvrage> ouvrages;

    public MotCles() {
        ouvrages = new ArrayList();
    }

    public MotCles(String motCle) {
        this();
        this.motCle = motCle;
        this.ouvrages = ouvrages;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotCle() {
        return motCle;
    }

    public void setMotCle(String motCle) {
        this.motCle = motCle;
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
        if (!(object instanceof MotCles)) {
            return false;
        }
        MotCles other = (MotCles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mot Cle{" + "id=" + id + ", mot=" + motCle+"}";
    }
    
}
