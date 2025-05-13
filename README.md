# Application de Gestion des Voitures

Cette application JavaFX permet de gérer une liste de voitures avec les fonctionnalités CRUD (Create, Read, Update, Delete).

## Prérequis

- Java 17 ou supérieur
- Maven 3.6 ou supérieur
- MySQL Server

## Configuration de la base de données

1. Créez une base de données MySQL nommée `covoiturage_app`
2. Exécutez le script SQL suivant pour créer la table nécessaire :

```sql
CREATE TABLE voiture (
    id INT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    nb_places INT NOT NULL,
    couleur VARCHAR(50) NOT NULL,
    type_voiture VARCHAR(50) NOT NULL,
    categorie VARCHAR(50) NOT NULL
);
```

## Exécution de l'application

### Méthode 1 : Depuis votre IDE

1. Ouvrez le projet dans votre IDE (IntelliJ IDEA, Eclipse, etc.)
2. Exécutez la classe `org.example.MainApp`

### Méthode 2 : Avec Maven

```bash
mvn clean javafx:run
```

### Méthode 3 : Avec le JAR exécutable

1. Générez le JAR avec Maven :
```bash
mvn clean package
```

2. Exécutez le JAR :
```bash
java -jar target/pi-1.0-SNAPSHOT.jar
```

## Fonctionnalités

- Afficher la liste des voitures
- Ajouter une nouvelle voiture
- Modifier une voiture existante
- Supprimer une voiture
- Rechercher des voitures par critère

## Structure du projet

- `src/main/java/org/example` : Classes principales de l'application
- `src/main/java/controller` : Contrôleurs JavaFX
- `src/main/java/model` : Modèles de données
- `src/main/java/service` : Services métier
- `src/main/java/dao` : Accès aux données
- `src/main/java/utils` : Utilitaires
- `src/main/resources` : Fichiers FXML et autres ressources
