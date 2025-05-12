package org.example.entities;

public class Covoiturage {
    private int id_covoiturage;
    private String lieu_depart;
    private String lieu_arrivee;
    private String date_heure;
    private int id_conducteur;
    private int id_voiture;
    private int places_restantes;
    private String statut;

    public Covoiturage(int id_covoiturage, String lieu_depart, String lieu_arrivee, String date_heure, int id_conducteur, int id_voiture, int places_restantes, String statut) {
        this.id_covoiturage = id_covoiturage;
        this.lieu_depart = lieu_depart;
        this.lieu_arrivee = lieu_arrivee;
        this.date_heure = date_heure;
        this.id_conducteur = id_conducteur;
        this.id_voiture = id_voiture;
        this.places_restantes = places_restantes;
        this.statut = statut;
    }

    public int getId_covoiturage() {
        return id_covoiturage;
    }

    public void setId_covoiturage(int id_covoiturage) {
        this.id_covoiturage = id_covoiturage;
    }

    public String getLieu_depart() {
        return lieu_depart;
    }

    public void setLieu_depart(String lieu_depart) {
        this.lieu_depart = lieu_depart;
    }

    public String getLieu_arrivee() {
        return lieu_arrivee;
    }

    public void setLieu_arrivee(String lieu_arrivee) {
        this.lieu_arrivee = lieu_arrivee;
    }

    public String getDate_heure() {
        return date_heure;
    }

    public void setDate_heure(String date_heure) {
        this.date_heure = date_heure;
    }

    public int getId_conducteur() {
        return id_conducteur;
    }

    public void setId_conducteur(int id_conducteur) {
        this.id_conducteur = id_conducteur;
    }

    public int getId_voiture() {
        return id_voiture;
    }

    public void setId_voiture(int id_voiture) {
        this.id_voiture = id_voiture;
    }

    public int getPlaces_restantes() {
        return places_restantes;
    }

    public void setPlaces_restantes(int places_restantes) {
        this.places_restantes = places_restantes;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}