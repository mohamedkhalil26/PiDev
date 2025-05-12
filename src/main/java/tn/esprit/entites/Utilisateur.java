package tn.esprit.entites;

import java.util.Objects;

public class Utilisateur {
    private int id_utilisateur;
    private String nom;
    private String prenom;
    private int age;
    private Role role; // Changed to enum
    private String email;
    private String numero_tel;
    private String username;
    private String mot_de_passe;


    public Utilisateur(int id_utilisateur, String nom, String prenom, int age, Role role, String email, String numero_tel, String username, String mot_de_passe) {
        this.id_utilisateur = id_utilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.role = role;
        this.email = email;
        this.numero_tel = numero_tel;
        this.username = username;
        this.mot_de_passe = mot_de_passe;
    }

    public Utilisateur(String nom, String prenom, int age, Role role, String email, String numero_tel, String username, String mot_de_passe) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.role = role;
        this.email = email;
        this.numero_tel = numero_tel;
        this.username = username;
        this.mot_de_passe = mot_de_passe;
    }

    public Utilisateur() {
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public String getNumero_tel() {
        return numero_tel;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public int getAge() {
        return age;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNumero_tel(String numero_tel) {
        this.numero_tel = numero_tel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id_utilisateur=" + id_utilisateur +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", age=" + age +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", numero_tel='" + numero_tel + '\'' +
                ", username='" + username + '\'' +
                ", mot_de_passe='" + mot_de_passe + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return id_utilisateur == that.id_utilisateur && age == that.age && Objects.equals(nom, that.nom) && Objects.equals(prenom, that.prenom) && role == that.role && Objects.equals(email, that.email) && Objects.equals(numero_tel, that.numero_tel) && Objects.equals(username, that.username) && Objects.equals(mot_de_passe, that.mot_de_passe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_utilisateur, nom, prenom, age, role, email, numero_tel, username, mot_de_passe);
    }
}
