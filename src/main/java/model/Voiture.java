package model;

public class Voiture {
    private int id;
    private int utilisateurId;
    private int nbPlaces;
    private String couleur;
    private String typeVoiture;
    private String categorie;
    private String typeCarburant;
public Voiture() {
        // Constructeur par défaut
    }
    public Voiture( int utilisateurId, int nbPlaces, String couleur, String typeVoiture, String categorie , String typeCarburant) {

        this.utilisateurId = utilisateurId;
        this.nbPlaces = nbPlaces;
        this.couleur = couleur;
        this.typeVoiture = typeVoiture;
        this.categorie = categorie;
        this.typeCarburant = typeCarburant;
    }

    public Voiture(int id, int utilisateurId, int nbPlaces, String couleur, String typeVoiture, String categorie , String typeCarburant) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.nbPlaces = nbPlaces;
        this.couleur = couleur;
        this.typeVoiture = typeVoiture;
        this.categorie = categorie;
        this.typeCarburant = typeCarburant;
    }

    // Getters et Setters
    public int getId() { return id; }
    public int getUtilisateurId() { return utilisateurId; }
    public int getNbPlaces() { return nbPlaces; }
    public String getCouleur() { return couleur; }
    public String getTypeVoiture() { return typeVoiture; }
    public String getCategorie() { return categorie; }

    public String getTypeCarburant() {
        return typeCarburant;
    }

    public void setTypeCarburant(String typeCarburant) {
        this.typeCarburant = typeCarburant;
    }

    public void setId(int id) { this.id = id; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    public void setNbPlaces(int nbPlaces) { this.nbPlaces = nbPlaces; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
    public void setTypeVoiture(String typeVoiture) { this.typeVoiture = typeVoiture; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    @Override
    public String toString() {
        return "Voiture{" +
                "id=" + id +
                ", utilisateurId=" + utilisateurId +
                ", nbPlaces=" + nbPlaces +
                ", couleur='" + couleur + '\'' +
                ", typeVoiture='" + typeVoiture + '\'' +
                ", categorie='" + categorie + '\'' +
                '}';
    }
}
