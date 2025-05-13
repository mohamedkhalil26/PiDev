# Guide d'utilisation de la classe MainApp

Ce document explique comment la classe `MainApp` a été configurée comme point d'entrée principal de l'application de covoiturage.

## La classe MainApp

La classe `MainApp` (située dans `src/main/java/org/example/MainApp.java`) est le point d'entrée principal de l'application JavaFX. Elle étend la classe `Application` de JavaFX et implémente les méthodes nécessaires pour démarrer l'application.

### Fonctionnalités principales

1. **Initialisation de la base de données** : Au démarrage, la classe appelle `DatabaseInitializer.initializeDatabase()` pour s'assurer que toutes les tables nécessaires existent.

2. **Démarrage du service d'emails** : Elle démarre le service d'envoi automatique d'emails avec `AutoEmailService.start()`.

3. **Chargement de l'interface utilisateur** : Elle charge le fichier FXML principal (`MainMenu.fxml`) et l'affiche dans une fenêtre.

4. **Gestion des erreurs** : Elle inclut une gestion robuste des erreurs pour afficher des messages appropriés en cas de problème.

5. **Nettoyage à la fermeture** : La méthode `stop()` est surchargée pour arrêter proprement les services en cours d'exécution.

## Configuration dans pom.xml

Le fichier `pom.xml` a été configuré pour utiliser `org.example.MainApp` comme classe principale :

```xml
<properties>
    <!-- Autres propriétés... -->
    <exec.mainClass>org.example.MainApp</exec.mainClass>
    <main.class>org.example.MainApp</main.class>
</properties>
```

Ces propriétés sont utilisées par les plugins Maven :

1. **javafx-maven-plugin** : Pour exécuter l'application avec `mvn javafx:run`
2. **exec-maven-plugin** : Pour exécuter l'application avec `mvn exec:java`
3. **maven-shade-plugin** : Pour créer un JAR exécutable

## Comment exécuter l'application

### Avec le script de lancement

Le script `run.bat` (Windows) ou `run.sh` (Linux/Mac) a été mis à jour pour essayer différentes méthodes d'exécution :

```batch
@echo off
echo Lancement de l'application...

REM Essayer d'abord avec javafx:run
echo Tentative avec javafx:run...
mvn clean javafx:run

IF %ERRORLEVEL% NEQ 0 (
    echo Tentative avec exec:java...
    mvn clean compile exec:java
)

pause
```

### Avec Maven directement

```
mvn clean javafx:run
```

ou

```
mvn clean compile exec:java
```

### Depuis votre IDE

1. Ouvrez le projet dans votre IDE
2. Localisez la classe `org.example.MainApp`
3. Exécutez-la comme une application Java standard

## Personnalisation

Si vous souhaitez modifier le comportement de démarrage de l'application, vous pouvez :

1. Modifier la méthode `start()` dans `MainApp.java`
2. Ajouter des initialisations supplémentaires avant le chargement de l'interface
3. Modifier les paramètres de la fenêtre principale (taille, titre, etc.)

## Création d'un JAR exécutable

Pour créer un JAR exécutable de l'application :

```
mvn clean package
```

Le JAR sera créé dans le dossier `target` et pourra être exécuté avec :

```
java -jar target/pi-1.0-SNAPSHOT.jar
```

Note : Pour que le JAR fonctionne correctement, toutes les dépendances JavaFX doivent être disponibles dans le classpath.
