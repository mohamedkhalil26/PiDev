package tn.esprit.entites;


import tn.esprit.outils.MyDatabase;

import java.sql.*;
import java.util.*;

public class Avis  {
    private int  id_avis;
    private int id_utilisateur;
    private int id_covoiturage;
    private int note;
    private String commentaire;
    private String sentiment;

    public Avis(int id_avis, int id_utilisateur, int id_covoiturage, int note, String commentaire, String sentiment) {
        this.id_avis = id_avis;
        this.id_utilisateur = id_utilisateur;
        this.id_covoiturage = id_covoiturage;
        this.note = note;
        this.commentaire = commentaire;
        this.sentiment = sentiment;
    }

    public Avis(int id_utilisateur, int id_covoiturage, int note, String commentaire, String sentiment) {
        this.id_utilisateur = id_utilisateur;
        this.id_covoiturage = id_covoiturage;
        this.note = note;
        this.commentaire = commentaire;
        this.sentiment = sentiment;
    }

    public Avis() {
    }

    public int getId_avis() {
        return id_avis;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public int getId_covoiturage() {
        return id_covoiturage;
    }

    public int getNote() {
        return note;
    }

    public String getCommentaire() {
        return commentaire;
    }
    public String getSentiment() {
        return sentiment;
    }

    public void setId_avis(int id_avis) {
        this.id_avis = id_avis;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public void setId_covoiturage(int id_covoiturage) {
        this.id_covoiturage = id_covoiturage;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Avis avis = (Avis) o;
        return id_avis == avis.id_avis && id_utilisateur == avis.id_utilisateur && id_covoiturage == avis.id_covoiturage && note == avis.note && Objects.equals(commentaire, avis.commentaire) && Objects.equals(sentiment, avis.sentiment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_avis, id_utilisateur, id_covoiturage, note, commentaire, sentiment);
    }
}