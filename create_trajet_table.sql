-- Script de création de la table trajet
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

-- Insertion de quelques exemples de trajets
INSERT INTO trajet (ville_depart, ville_arrivee, date_heure_depart, nb_places_disponibles, prix_par_place, conducteur_id, voiture_id, statut) VALUES
('Paris', 'Lyon', '2025-05-15 08:30:00', 3, 25.50, 1, 1, 'planifié'),
('Marseille', 'Nice', '2025-05-16 10:00:00', 2, 15.00, 1, 1, 'planifié'),
('Toulouse', 'Bordeaux', '2025-05-17 14:45:00', 4, 18.75, 2, 2, 'planifié'),
('Lille', 'Paris', '2025-05-18 07:15:00', 1, 22.00, 3, 3, 'planifié'),
('Lyon', 'Grenoble', '2025-05-19 16:30:00', 3, 12.50, 2, 2, 'planifié');
