package model;

import java.time.LocalDateTime;

public class Trajet {
    private int id;
    private String villeDepart;
    private String villeArrivee;
    private LocalDateTime dateHeureDepart;
    private int nbPlacesDisponibles;
    private double prixParPlace;
    private int conducteurId; // Lié à l'entité Utilisateur
    private int voitureId; // Lié à l'entité Voiture
    private String statut; // planifié, en cours, terminé, annulé

    public Trajet(int id, String villeDepart, String villeArrivee, LocalDateTime dateHeureDepart,
                 int nbPlacesDisponibles, double prixParPlace, int conducteurId, int voitureId) {
        this.id = id;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateHeureDepart = dateHeureDepart;
        this.nbPlacesDisponibles = nbPlacesDisponibles;
        this.prixParPlace = prixParPlace;
        this.conducteurId = conducteurId;
        this.voitureId = voitureId;
        this.statut = statut;
    }

    // Getters
    public int getId() { return id; }
    public String getVilleDepart() { return villeDepart; }
    public String getVilleArrivee() { return villeArrivee; }
    public LocalDateTime getDateHeureDepart() { return dateHeureDepart; }
    public int getNbPlacesDisponibles() { return nbPlacesDisponibles; }
    public double getPrixParPlace() { return prixParPlace; }
    public int getConducteurId() { return conducteurId; }
    public int getVoitureId() { return voitureId; }
    public String getStatut() { return statut; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setVilleDepart(String villeDepart) { this.villeDepart = villeDepart; }
    public void setVilleArrivee(String villeArrivee) { this.villeArrivee = villeArrivee; }
    public void setDateHeureDepart(LocalDateTime dateHeureDepart) { this.dateHeureDepart = dateHeureDepart; }
    public void setNbPlacesDisponibles(int nbPlacesDisponibles) { this.nbPlacesDisponibles = nbPlacesDisponibles; }
    public void setPrixParPlace(double prixParPlace) { this.prixParPlace = prixParPlace; }
    public void setConducteurId(int conducteurId) { this.conducteurId = conducteurId; }
    public void setVoitureId(int voitureId) { this.voitureId = voitureId; }
    public void setStatut(String statut) { this.statut = statut; }

    @Override
    public String toString() {
        return "Trajet{" +
                "id=" + id +
                ", villeDepart='" + villeDepart + '\'' +
                ", villeArrivee='" + villeArrivee + '\'' +
                ", dateHeureDepart=" + dateHeureDepart +
                ", nbPlacesDisponibles=" + nbPlacesDisponibles +
                ", prixParPlace=" + prixParPlace +
                ", conducteurId=" + conducteurId +
                ", voitureId=" + voitureId +
                ", statut='" + statut + '\'' +
                '}';
    }
}
