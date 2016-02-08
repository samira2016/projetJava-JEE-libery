
package entites;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

@Entity
public class Utilisateur implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String login;
    @Column(nullable = false, length = 100)
    private String password;
    
    private int actif;
    private String nom;
    private String prenom;
    private String telephone;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateInscription;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Collection<Adresse> adresses;
    
    @OneToMany(mappedBy = "user")
    private Collection<Evaluation> evaluations;
    
    @OneToMany(mappedBy = "user")
    private Collection<Commande> commandes;
    
    //adresse par default de l'utilisateur
   
    
   // private Adresse defaut;
    public Utilisateur() {
        adresses=new ArrayList<>();
        commandes=new ArrayList<>();
        evaluations=new ArrayList<>();
    }

    public Utilisateur(String login, String password) {
        this();
        setLogin(login);
        this.password = password;
    }

    
    public Utilisateur(String login, String password, int actif, String nom, String prenom, String telephone, Date dateInscription) {
        this();
        setLogin(login);
        setPassword(password);
        setActif(actif);
        setNom(nom);
        setPrenom(prenom);
        setTelephone(telephone);
        setDateInscription(dateInscription);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        
        
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getActif() {
        return actif;
    }

    public void setActif(int actif) {
        this.actif = actif;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Collection <Adresse> getAdresses() {
        return adresses;
    }

    public void setAdresses(Collection <Adresse> adresses) {
        this.adresses = adresses;
    }

    public Collection <Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(Collection <Commande> commandes) {
        this.commandes = commandes;
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
        if (!(object instanceof Utilisateur)) {
            return false;
        }
        Utilisateur other = (Utilisateur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.User[ id=" + id + " ]nom "+nom;
    }

    public Collection<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(Collection<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

//    public Adresse getDefaut() {
//        return defaut;
//    }
//
//    public void setDefaut(Adresse defaut) {
//        this.defaut = defaut;
//    }
//    
}
