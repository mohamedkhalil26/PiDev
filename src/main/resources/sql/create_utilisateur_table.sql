-- Script de création de la table utilisateur
CREATE TABLE IF NOT EXISTS utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    roles VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    telephone INT NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    date_naissance DATE NOT NULL,
    sexe VARCHAR(10) NOT NULL,
    taille DOUBLE NULL,
    poids INT NULL,
    image VARCHAR(255) NULL,
    status INT NULL,
    diplome VARCHAR(100) NULL,
    specialite VARCHAR(100) NULL,
    face_encoding TEXT NULL
);

-- Commentaires sur les colonnes
-- id: Identifiant unique de l'utilisateur
-- nom: Nom de l'utilisateur
-- prenom: Prénom de l'utilisateur
-- email: Email de l'utilisateur (unique)
-- roles: Rôles de l'utilisateur (admin, user, etc.)
-- password: Mot de passe de l'utilisateur
-- telephone: Numéro de téléphone de l'utilisateur
-- adresse: Adresse de l'utilisateur
-- date_naissance: Date de naissance de l'utilisateur
-- sexe: Sexe de l'utilisateur
-- taille: Taille de l'utilisateur (optionnel)
-- poids: Poids de l'utilisateur (optionnel)
-- image: Chemin vers l'image de profil de l'utilisateur (optionnel)
-- status: Statut de l'utilisateur (optionnel)
-- diplome: Diplôme de l'utilisateur (optionnel)
-- specialite: Spécialité de l'utilisateur (optionnel)
-- face_encoding: Encodage facial de l'utilisateur (optionnel)

-- Ajouter des utilisateurs d'exemple
INSERT INTO utilisateur (nom, prenom, email, roles, password, telephone, adresse, date_naissance, sexe)
VALUES 
('Dupont', 'Jean', 'jean.dupont@example.com', 'ROLE_USER', 'password123', 123456789, '123 Rue de Paris, Paris', '1990-01-15', 'Homme'),
('Martin', 'Sophie', 'sophie.martin@example.com', 'ROLE_USER', 'password123', 987654321, '456 Avenue des Champs, Lyon', '1985-05-20', 'Femme'),
('Dubois', 'Pierre', 'pierre.dubois@example.com', 'ROLE_ADMIN', 'admin123', 555123456, '789 Boulevard Saint-Michel, Marseille', '1982-11-10', 'Homme');
