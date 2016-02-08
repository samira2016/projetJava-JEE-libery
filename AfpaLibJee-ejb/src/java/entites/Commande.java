package entites;

import constants.StatutPaiement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Commande implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCommande;
    private Float prixTotal;
    private Integer status;
    @OneToMany(mappedBy = "commande")
    private Collection<LigneCommande> ligneCommande;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Adresse adresseLivraison;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Adresse adresseFacturation;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Livraison livraison;
    @OneToMany(mappedBy = "commande")
    private Collection<Operation> transactions; 
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Utilisateur user;
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private CodePromo codePromo;
   
    public Commande() {
        ligneCommande = new ArrayList();
        transactions = new ArrayList();
    }

    public Commande(Date dateCommande, Float prixTotal, Integer status) {
        this();
        this.dateCommande = dateCommande;
        this.prixTotal = prixTotal;
        this.status = status;
    }

    public CodePromo getCodePromo() {
        return codePromo;
    }

    public void setCodePromo(CodePromo codePromo) {
        this.codePromo = codePromo;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Float getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Float prixTotal) {
        this.prixTotal = prixTotal;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Collection<LigneCommande> getLigneCommande() {
        return ligneCommande;
    }

    public void setLigneCommande(Collection<LigneCommande> ligneCommande) {
        this.ligneCommande = ligneCommande;
    }

    public Adresse getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(Adresse adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public Adresse getAdresseFacturation() {
        return adresseFacturation;
    }

    public void setAdresseFacturation(Adresse adresseFacturation) {
        this.adresseFacturation = adresseFacturation;
    }

    public Livraison getLivraison() {
        return livraison;
    }

    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
    } 

    public Collection<Operation> getTransactions() {
        return transactions;
    }
    
    public Operation getLastSuccesfulTransaction() {
        for( Operation op : transactions )
        {
            if ( op.getStatus().equals(StatutPaiement.VALIDATED) )
            {
                return op;
            }
        }
        return null;
    }

    public void setTransactions(Collection<Operation> transactions) {
        this.transactions = transactions;
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
        if (!(object instanceof Commande)) {
            return false;
        }
        Commande other = (Commande) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Commande[ id=" + id + " ]";
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }

}
