package tn.esprit.entites;

import java.time.LocalDateTime;

public class ReclamationHistorique {
    private int id;
    private int reclamationId;
    private String champModifie;
    private String ancienneValeur;
    private String nouvelleValeur;
    private LocalDateTime dateModification;
    private String modifiePar;

    public ReclamationHistorique() {}

    public ReclamationHistorique(int reclamationId, String champModifie, String ancienneValeur, String nouvelleValeur, LocalDateTime dateModification, String modifiePar) {
        this.reclamationId = reclamationId;
        this.champModifie = champModifie;
        this.ancienneValeur = ancienneValeur;
        this.nouvelleValeur = nouvelleValeur;
        this.dateModification = dateModification;
        this.modifiePar = modifiePar;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getReclamationId() { return reclamationId; }
    public void setReclamationId(int reclamationId) { this.reclamationId = reclamationId; }
    public String getChampModifie() { return champModifie; }
    public void setChampModifie(String champModifie) { this.champModifie = champModifie; }
    public String getAncienneValeur() { return ancienneValeur; }
    public void setAncienneValeur(String ancienneValeur) { this.ancienneValeur = ancienneValeur; }
    public String getNouvelleValeur() { return nouvelleValeur; }
    public void setNouvelleValeur(String nouvelleValeur) { this.nouvelleValeur = nouvelleValeur; }
    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }
    public String getModifiePar() { return modifiePar; }
    public void setModifiePar(String modifiePar) { this.modifiePar = modifiePar; }
}