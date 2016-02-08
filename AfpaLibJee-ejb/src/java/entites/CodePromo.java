
package entites;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;


@Entity
public class CodePromo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String codePromo;
    
    @Column(nullable = false)
    private Float reduction;
    @Column(nullable = false)
    private boolean typeReduc;
    @Column(nullable = false)
    private boolean cummulable;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateDebut;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateFin;
    
    @OneToMany(mappedBy = "codePromo")
    private Collection<Commande> commandes;
    
    @OneToMany(mappedBy = "codePromo")//, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Collection<Ouvrage> ouvrages;
        
    public CodePromo() {
        ouvrages = new ArrayList();
        commandes = new ArrayList();
    }

    public CodePromo(String codePromo, Float reduction, boolean typeReduc, boolean cummulable) {
        this();
        this.codePromo = codePromo;
        this.reduction = reduction;
        this.typeReduc = typeReduc;
        this.cummulable = cummulable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodePromo() {
        return codePromo;
    }

    public void setCodePromo(String codePromo) {
        this.codePromo = codePromo;
    }

    public Float getReduction() {
        return reduction;
    }

    public void setReduction(Float reduction) {
        this.reduction = reduction;
    }

    public boolean isTypeReduc() {
        return typeReduc;
    }

    public void setTypeReduc(boolean typeReduc) {
        this.typeReduc = typeReduc;
    }

    public boolean isCummulable() {
        return cummulable;
    }

    public void setCummulable(boolean cummulable) {
        this.cummulable = cummulable;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Collection<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(Collection<Commande> commandes) {
        this.commandes = commandes;
    }

    public Collection<Ouvrage> getOuvrages() {
        return ouvrages;
    }

    public void setOuvrages(Collection<Ouvrage> ouvrages) {
        this.ouvrages = ouvrages;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.codePromo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CodePromo other = (CodePromo) obj;
        if (!Objects.equals(this.codePromo, other.codePromo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CodePromo{" + "id=" + id + ", codePromo=" + codePromo + ", reduction=" + reduction + ", typeReduc=" + typeReduc + ", cummulable=" + cummulable + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + '}';
    }
    
    
}
