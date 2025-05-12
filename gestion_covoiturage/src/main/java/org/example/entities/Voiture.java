package org.example.entities;

public class Voiture {
    private int id_voiture;
    private String immatriculation;
    private String marque;
    private String modele;
    private int nombre_places;
    private int id_conducteur;

    public Voiture() {}

    public Voiture(int id_voiture, String immatriculation, String marque, String modele, int nombre_places, int id_conducteur) {
        this.id_voiture = id_voiture;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.nombre_places = nombre_places;
        this.id_conducteur = id_conducteur;
    }

    public int getId_voiture() { return id_voiture; }
    public void setId_voiture(int id_voiture) { this.id_voiture = id_voiture; }
    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    public int getNombre_places() { return nombre_places; }
    public void setNombre_places(int nombre_places) { this.nombre_places = nombre_places; }
    public int getId_conducteur() { return id_conducteur; }
    public void setId_conducteur(int id_conducteur) { this.id_conducteur = id_conducteur; }

    @Override
    public String toString() {
        return "Voiture{id=" + id_voiture + ", immatriculation='" + immatriculation + "', marque='" + marque + "', modele='" + modele + "', nombre_places=" + nombre_places + ", id_conducteur=" + id_conducteur + "}";
    }
}