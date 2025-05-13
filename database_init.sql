-- Script d'initialisation de la base de données covoiturage
-- Exécuter ce script dans phpMyAdmin pour créer les tables nécessaires

-- Supprimer les tables si elles existent déjà (optionnel)
-- DROP TABLE IF EXISTS trajet;
-- DROP TABLE IF EXISTS voiture;

-- Création de la table voiture
CREATE TABLE IF NOT EXISTS voiture (
    id INT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    nb_places INT NOT NULL,
    couleur VARCHAR(50) NOT NULL,
    type_voiture VARCHAR(50) NOT NULL,
    categorie VARCHAR(50) NOT NULL,
    type_carburant VARCHAR(20) DEFAULT 'essence'
);

-- Création de la table trajet
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

-- Vérifier si la table voiture est vide
SET @count_voiture = (SELECT COUNT(*) FROM voiture);

-- Ajouter des données d'exemple pour les voitures si la table est vide
INSERT INTO voiture (utilisateur_id, nb_places, couleur, type_voiture, categorie, type_carburant)
SELECT * FROM (
    SELECT 1, 5, 'Noir', 'Berline', 'Confort', 'essence' UNION ALL
    SELECT 1, 4, 'Blanc', 'SUV', 'Économique', 'diesel' UNION ALL
    SELECT 2, 2, 'Rouge', 'Coupé', 'Sport', 'hybride' UNION ALL
    SELECT 2, 7, 'Gris', 'Monospace', 'Familiale', 'electrique'
) AS tmp
WHERE @count_voiture = 0;

-- Vérifier si la table trajet est vide
SET @count_trajet = (SELECT COUNT(*) FROM trajet);

-- Ajouter des données d'exemple pour les trajets si la table est vide
INSERT INTO trajet (ville_depart, ville_arrivee, date_heure_depart, nb_places_disponibles, prix_par_place, conducteur_id, voiture_id, statut)
SELECT * FROM (
    SELECT 'Paris', 'Lyon', DATE_ADD(NOW(), INTERVAL 1 DAY), 3, 25.50, 1, 1, 'planifié' UNION ALL
    SELECT 'Lyon', 'Marseille', DATE_ADD(NOW(), INTERVAL 2 DAY), 2, 15.75, 2, 2, 'planifié' UNION ALL
    SELECT 'Marseille', 'Nice', DATE_ADD(NOW(), INTERVAL 3 DAY), 4, 10.00, 1, 1, 'planifié'
) AS tmp
WHERE @count_trajet = 0;
