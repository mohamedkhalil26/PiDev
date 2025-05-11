package tn.esprit.entites;

public class Reservation {
    private int idReservation;
    private int idCovoiturage;
    private int idPassager;
    private double prix;
    private int nbPlacesReservees;

    public Reservation(int idCovoiturage, int idPassager, double prix, int nbPlacesReservees) {
        this.idCovoiturage = idCovoiturage;
        this.idPassager = idPassager;
        this.prix = prix;
        this.nbPlacesReservees = nbPlacesReservees;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdCovoiturage() {
        return idCovoiturage;
    }

    public int getIdPassager() {
        return idPassager;
    }

    public double getPrix() {
        return prix;
    }

    public int getNbPlacesReservees() {
        return nbPlacesReservees;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public void setNbPlacesReservees(int nbPlacesReservees) {
        this.nbPlacesReservees = nbPlacesReservees;
    }

    @Override
    public String toString() {
        return "Reservation [id=" + idReservation + ", idCovoiturage=" + idCovoiturage + ", idPassager=" + idPassager +
                ", prix=" + prix + ", nbPlacesReservees=" + nbPlacesReservees + "]";
    }
}

