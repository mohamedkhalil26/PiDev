package org.example.entities;

public class Conducteur {
    private int id_conducteur;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;

    public Conducteur(int id_conducteur, String nom, String prenom, String telephone, String email) {
        this.id_conducteur = id_conducteur;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
    }

    public int getId_conducteur() { return id_conducteur; }
    public void setId_conducteur(int id_conducteur) { this.id_conducteur = id_conducteur; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Conducteur{id=" + id_conducteur + ", nom='" + nom + "', prenom='" + prenom + "', telephone='" + telephone + "', email='" + email + "'}";
    }
}