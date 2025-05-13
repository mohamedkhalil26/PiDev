-- Script de création de la table trajet (version simplifiée sans contraintes de clé étrangère)
CREATE TABLE IF NOT EXISTS trajet (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ville_depart VARCHAR(100) NOT NULL,
    ville_arrivee VARCHAR(100) NOT NULL,
    date_heure_depart DATETIME NOT NULL,
    nb_places_disponibles INT NOT NULL,
    prix_par_place DECIMAL(10, 2) NOT NULL,
    conducteur_id INT NOT NULL,
    voiture_id INT NOT NULL,
    statut VARCHAR(50) NOT NULL
);

-- Commentaires sur les colonnes
-- id: Identifiant unique du trajet
-- ville_depart: Ville de départ du trajet
-- ville_arrivee: Ville d'arrivée du trajet
-- date_heure_depart: Date et heure de départ du trajet
-- nb_places_disponibles: Nombre de places disponibles dans le véhicule
-- prix_par_place: Prix par place en euros
-- conducteur_id: Référence à l'ID de l'utilisateur qui est le conducteur
-- voiture_id: Référence à l'ID de la voiture utilisée pour le trajet
-- statut: État actuel du trajet (planifié, en cours, terminé, annulé)

-- Note: Cette version du script ne contient pas de contraintes de clé étrangère
-- pour faciliter les tests et le développement.
