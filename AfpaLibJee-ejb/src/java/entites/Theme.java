package entites;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Theme implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String theme;
    private String theme_parent;
    private Long idParent;

    @ManyToMany(mappedBy = "themes")
    private Collection<Ouvrage> ouvrages;

    public Theme() {
        ouvrages = new ArrayList();
    }

    public Theme(String theme, Long idParent, String theme_parent) {
        this();
        this.theme = theme;
        this.idParent = idParent;
        this.theme_parent = theme_parent;
    }

    public Collection<Ouvrage> getOuvrages() {
        return ouvrages;
    }

    public String getTheme_parent() {
        return theme_parent;
    }

    public void setTheme_parent(String theme_parent) {
        this.theme_parent = theme_parent;
    }

    public void setOuvrages(Collection<Ouvrage> ouvrages) {
        this.ouvrages = ouvrages;
    }

    public Long getIdParent() {
        return idParent;
    }

    public void setIdParent(Long idParent) {
        this.idParent = idParent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
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
        if (!(object instanceof Theme)) {
            return false;
        }
        Theme other = (Theme) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Theme : valeur = " + theme + " (" + id + ")";
    }

}
