# Instructions d'exécution de l'application

Ce document explique comment exécuter l'application après les corrections apportées.

## Méthode 1 : Utiliser le script de lancement

La méthode la plus simple est d'utiliser le script de lancement fourni :

### Sous Windows
Double-cliquez sur le fichier `run.bat` ou exécutez-le depuis une invite de commande :
```
run.bat
```

### Sous Linux/Mac
Rendez le script exécutable et lancez-le :
```
chmod +x run.sh
./run.sh
```

## Méthode 2 : Utiliser Maven directement

Vous pouvez également utiliser Maven directement pour exécuter l'application :

```
mvn clean compile exec:java
```

## Méthode 3 : Utiliser le plugin JavaFX Maven

Si les méthodes précédentes ne fonctionnent pas, essayez d'utiliser le plugin JavaFX Maven :

```
mvn clean javafx:run
```

## Méthode 4 : Exécuter depuis votre IDE

1. Ouvrez le projet dans votre IDE (IntelliJ IDEA, Eclipse, etc.)
2. Exécutez la classe `org.example.MainApp` comme une application Java standard

## Dépannage

Si vous rencontrez des erreurs :

1. **Erreur de chargement des fichiers FXML** : Vérifiez que les fichiers FXML sont bien dans le dossier `src/main/resources`
2. **Erreur de module** : Nous avons supprimé le fichier module-info.java pour éviter les problèmes de modules
3. **Erreur de dépendances** : Assurez-vous que toutes les dépendances JavaFX sont correctement téléchargées

## Remarques importantes

- L'application a été configurée pour fonctionner avec Java 17
- Les dépendances JavaFX sont gérées automatiquement par Maven
- Si vous modifiez le code, n'oubliez pas de recompiler avant d'exécuter
