package tn.esprit.entites;

public class Reclamation {
    private int id;
    private String sujet;
    private String description;
    private int reservationId;
    private String statut;

    public enum Statut {
        EN_ATTENTE, TRAITEE, REJETEE
    }

    public Reclamation() {}

    public Reclamation(String sujet, String description, int reservationId) {
        this.sujet = sujet;
        this.description = description;
        this.reservationId = reservationId;
        this.statut = Statut.EN_ATTENTE.name();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) {
        try {
            Statut newStatut = Statut.valueOf(statut);
            if (this.statut == null) {
                this.statut = statut;
                return;
            }
            Statut currentStatut = Statut.valueOf(this.statut);
            if (currentStatut == Statut.EN_ATTENTE && (newStatut == Statut.TRAITEE || newStatut == Statut.REJETEE)) {
                this.statut = statut;
            } else if (currentStatut == Statut.EN_ATTENTE && newStatut == Statut.EN_ATTENTE) {
                this.statut = statut;
            } else {
                throw new IllegalStateException("Transition de statut non autorisée : de " + currentStatut + " à " + newStatut);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut invalide : " + statut);
        }
    }
}