package entites;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;


@Entity
public class Ouvrage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150)
    private String titre;
    @Column(nullable = true, length = 150)
    private String sousTitre;

    @Column(nullable = true, length = 22)
    private String isbn;

    @Column(nullable = true, length = 7000)
    private String resume;

    @Column(nullable = true, length = 256)
    private String imageUrl;

    
    
    @Column(nullable = false)
    private Float prixHT;

    private Float poids;
    @Column(nullable = false)
    private Integer stock;

    public String getNomsAuteurs() {
        Auteur[] aut = (Auteur[]) auteurs.toArray();
        
        String nomsAuteurs = "";
        for(int i = 0; i < aut.length;i++){
            nomsAuteurs += aut[i].getNomAuteur()+" ";
        }
        return nomsAuteurs;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private CodePromo codePromo;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Collection<TVA> tvas;

    @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    private Collection<Editeur> editeurs;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Collection<Auteur> auteurs;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Collection<MotCles> motsCle;

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST, CascadeType.REFRESH})
    private Collection<Theme> themes;

    @OneToMany(mappedBy = "ouvrage")//, cascade = {CascadeType.ALL})
    private Collection<Evaluation> evaluations;

    @OneToMany(mappedBy = "ouvrage")
    private Collection<LigneCommande> lignesCommande;

    public Ouvrage() {
        tvas = new ArrayList();
        editeurs = new ArrayList();
        auteurs = new ArrayList();
        motsCle = new ArrayList();
        themes = new ArrayList();
        evaluations = new ArrayList();
        lignesCommande = new ArrayList();
    }

    public Ouvrage(String titre, String sousTitre, String isbn, Float poids) {
        this();
        this.titre = titre;
        this.sousTitre = sousTitre;
        this.isbn = isbn;
        this.poids = poids;
        this.stock=0;
        this.prixHT=0f;
    }

    public Ouvrage(String titre, String isbn, Float prixHT, Integer stock) {
        this();
        this.titre = titre;
        this.isbn = isbn;
        this.prixHT = prixHT;
        this.stock = stock;
    }

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getSousTitre() {
        return sousTitre;
    }

    public void setSousTitre(String sousTitre) {
        this.sousTitre = sousTitre;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Float getPrixHT() {
        return prixHT;
    }

    public void setPrixHT(Float prixHT) {
        this.prixHT = prixHT;
    }
    public Float getPrixTTC(){
        float tva=0f;
        Date d01= new Date();
        // TODO: tri croissant des liste tva par date fin
        for(TVA tv: tvas){
            if(d01.before(tv.getDateFin())){
                tva=tv.getTaux();
            }
        }
        return prixHT*(1+tva);
    }

    public Float getPoids() {
        return poids;
    }

    public void setPoids(Float poids) {
        this.poids = poids;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Collection<TVA> getTvas() {
        return tvas;
    }

    public void setTvas(Collection<TVA> tvas) {
        this.tvas = tvas;
    }

    public Collection<Editeur> getEditeurs() {
        return editeurs;
    }

    public void setEditeurs(Collection<Editeur> editeurs) {
        this.editeurs = editeurs;
    }

    public Collection<Auteur> getAuteurs() {
        return auteurs;
    }

    public void setAuteurs(Collection<Auteur> auteurs) {
        this.auteurs = auteurs;
    }

    public Collection<MotCles> getMotsCle() {
        return motsCle;
    }

    public void setMotsCle(Collection<MotCles> motsCle) {
        this.motsCle = motsCle;
    }

    public Collection<Theme> getThemes() {
        return themes;
    }

    public void setThemes(Collection<Theme> themes) {
        this.themes = themes;
    }

    public CodePromo getCodePromo() {
        return codePromo;
    }

    public void setCodePromo(CodePromo codePromo) {
        this.codePromo = codePromo;
    }

    public Collection<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(Collection<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    public Collection<LigneCommande> getLignesCommande() {
        return lignesCommande;
    }

    public void setLignesCommande(Collection<LigneCommande> lignesCommande) {
        this.lignesCommande = lignesCommande;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.titre);
        hash = 17 * hash + Objects.hashCode(this.isbn);
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
        final Ouvrage other = (Ouvrage) obj;
        if (!Objects.equals(this.titre, other.titre)) {
            return false;
        }
        if (!Objects.equals(this.sousTitre, other.sousTitre)) {
            return false;
        }
        if (!Objects.equals(this.isbn, other.isbn)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ouvrage{" + "id=" + id + ", titre=" + titre + ", sousTitre=" + sousTitre + ", isbn=" + isbn + ", resume=" + resume + ", prixHT=" + prixHT + ", poids=" + poids + ", stock=" + stock + ", editeurs=" + editeurs + ", auteurs=" + auteurs + ", motsCle=" + motsCle + ", themes=" + themes + "}";
    }

    
    
}
