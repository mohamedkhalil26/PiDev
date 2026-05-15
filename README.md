# PIDEV - Application JavaFX de Covoiturage

Application desktop développée en JavaFX dans le cadre d'un projet PIDEV. Elle permet de gérer les utilisateurs, les réservations, les réclamations et les avis autour d'un système de covoiturage.

## Présentation

Ce projet est une application JavaFX connectée à une base de données MySQL.  
L'application démarre sur une interface d'authentification, puis donne accès aux différents modules selon les écrans disponibles.

Le projet suit une architecture en couches :

- `entites` : modèles métier.
- `services` : logique métier et accès aux données.
- `controller` : contrôleurs JavaFX liés aux fichiers FXML.
- `outils` : classes utilitaires, notamment la connexion à la base de données.
- `api` : intégrations externes comme SMS, email, OAuth Google et analyse de sentiment.
- `resources` : vues FXML, fichiers CSS et images.

## Fonctionnalités

- Authentification utilisateur
- Inscription et gestion des profils
- Gestion des utilisateurs côté administration
- Gestion des réservations de covoiturage
- Gestion des réclamations avec statuts
- Gestion des avis et notes
- Analyse de sentiment des commentaires
- Envoi d'emails
- Envoi de SMS via Twilio
- Connexion avec Google OAuth
- Interface JavaFX basée sur FXML et CSS

## Stack technique

- Java 17
- JavaFX 21.0.1
- Maven
- MySQL
- JDBC
- FXML / CSS
- JUnit 5
- Twilio SDK
- Google OAuth Client
- JavaMail
- ZXing
- OkHttp
- Apache HttpClient
- Jackson
- JSON

## Architecture du projet

```text
.
├── pom.xml
├── mvnw
├── mvnw.cmd
├── src
│   └── main
│       ├── java
│       │   └── tn
│       │       └── esprit
│       │           ├── api
│       │           ├── controller
│       │           ├── entites
│       │           ├── main
│       │           ├── outils
│       │           └── services
│       └── resources
│           ├── fichiers FXML
│           ├── fichiers CSS
│           ├── styles
│           └── images
```

Prérequis
Avant de lancer le projet, installer :

JDK 17 ou version compatible
Maven ou le wrapper Maven fourni avec le projet
MySQL Server
Un IDE Java : IntelliJ IDEA, Eclipse ou VS Code
Vérifier Java :
```
java -version
```
Installation
Cloner le projet :
```
git clone <url-du-repository>
cd PiDev-main
```
Installer les dépendances :
```
./mvnw clean install
```
Sous Windows :
```
mvnw.cmd clean install
```
Configuration
Base de données
Le projet utilise MySQL. Les classes de connexion utilisent actuellement les bases suivantes :

. gryffindor
. covoiturage_app
Créer les bases dans MySQL :
```
CREATE DATABASE IF NOT EXISTS gryffindor;
CREATE DATABASE IF NOT EXISTS covoiturage_app;
```
Modifier les paramètres de connexion si nécessaire dans les fichiers :
```
src/main/java/tn/esprit/outils/MyDatabase.java
src/main/java/tn/esprit/outils/DBConnection.java
```
Exemple :
```
private final String URL = "jdbc:mysql://localhost:3306/gryffindor";
private final String USERNAME = "root";
private final String PASSWORD = "";
```
Variables d'environnement
Créer un fichier .env à la racine du projet pour configurer Twilio :
```
TWILIO_ACCOUNT_SID=your_twilio_account_sid
TWILIO_AUTH_TOKEN=your_twilio_auth_token
TWILIO_PHONE_NUMBER=your_twilio_phone_number
```
Google OAuth
Le fichier client_secret.json doit être placé dans :
```
src/main/resources/client_secret.json
```
L'URI de redirection utilisée peut être configurée dans Google Cloud Console :
```
http://localhost:8888/Callback
```
Tables principales
Le projet utilise principalement les tables suivantes :
```
utilisateur
reservations
reclamation
avis
covoiturage
```
Exemple de structure minimale :
```
CREATE TABLE utilisateur (
    id_utilisateur INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    age INT,
    role VARCHAR(50),
    email VARCHAR(150) UNIQUE,
    numero_tel VARCHAR(30),
    username VARCHAR(100),
    mot_de_passe VARCHAR(255)
);

CREATE TABLE reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_covoiturage INT NOT NULL,
    id_passager INT NOT NULL,
    prix DOUBLE NOT NULL,
    nb_places_reservees INT NOT NULL
);

CREATE TABLE reclamation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sujet VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    reservation_id INT NOT NULL,
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE'
);

CREATE TABLE avis (
    id_avis INT AUTO_INCREMENT PRIMARY KEY,
    id_utilisateur INT NOT NULL,
    id_covoiturage INT NOT NULL,
    note INT NOT NULL,
    commentaire TEXT,
    sentiment VARCHAR(100)
);
```
Lancement
Lancer l'application avec Maven :
```
./mvnw javafx:run
```
Sous Windows :
```
mvnw.cmd javafx:run
```
Classe principale configurée dans Maven :
```
tn.esprit.main.mainGui
```
Tests
Lancer les tests :

./mvnw test
Commandes utiles :
```
# Nettoyer et compiler
./mvnw clean compile

# Lancer l'application
./mvnw javafx:run

# Exécuter les tests
./mvnw test

# Générer le package
./mvnw clean package
```
Sécurité

Avant de publier le projet sur GitHub, il est recommandé de :

Ne pas stocker les clés API directement dans le code source.
Déplacer les secrets vers un fichier .env.
Ajouter .env dans .gitignore.
Ne pas versionner client_secret.json s'il contient des identifiants réels.
Hasher les mots de passe avant stockage en base de données.
Centraliser la configuration de connexion à la base de données.
Vérifier les permissions des comptes API utilisés.

Auteurs:
Projet réalisé dans le cadre du module PIDEV.

Équipe : Gryffindor
