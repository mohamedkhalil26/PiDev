# Résolution des problèmes de dépendances Maven

Ce document explique comment résoudre les problèmes de dépendances Maven que vous pourriez rencontrer avec cette application.

## Problèmes courants

### 1. Problèmes avec les dépendances JavaFX

Si vous rencontrez des erreurs comme :
```
6 problems were encountered while building the effective model for org.openjfx:javafx-controls:jar:17.0.2
```

### 2. Avertissements concernant MySQL Connector

Si vous voyez des avertissements comme :
```
[WARNING] The artifact mysql:mysql-connector-java:jar:8.0.33 has been relocated to com.mysql:mysql-connector-j:jar:8.0.33
```

## Solutions

### Solution 1 : Utiliser le script de lancement alternatif

Nous avons fourni des scripts alternatifs qui ne dépendent pas du plugin JavaFX Maven :

#### Windows
```
run_without_javafx_plugin.bat
```

#### Linux/Mac
```
chmod +x run_without_javafx_plugin.sh
./run_without_javafx_plugin.sh
```

### Solution 2 : Utiliser un miroir Maven alternatif

Si vous avez des problèmes pour télécharger les dépendances depuis le dépôt Maven central :

1. Copiez le fichier `settings.xml` dans votre répertoire `.m2` :
   - Windows : `C:\Users\<votre_nom>\.m2\`
   - Linux/Mac : `~/.m2/`

2. Exécutez la commande suivante pour nettoyer le cache Maven :
   ```
   mvn clean -U
   ```

3. Essayez de reconstruire le projet :
   ```
   mvn clean compile
   ```

### Solution 3 : Mettre à jour manuellement les dépendances

Si les solutions ci-dessus ne fonctionnent pas, vous pouvez essayer de mettre à jour manuellement les dépendances dans le fichier `pom.xml` :

1. Remplacez `mysql-connector-java` par `mysql-connector-j`
2. Essayez une version différente de JavaFX (par exemple, 17.0.1 au lieu de 17.0.2)
3. Essayez une version différente du plugin Maven JavaFX (par exemple, 0.0.6 au lieu de 0.0.8)

### Solution 4 : Vérifier votre connexion Internet et les paramètres de proxy

Si vous êtes derrière un proxy d'entreprise :

1. Décommentez et configurez la section proxy dans le fichier `settings.xml`
2. Assurez-vous que votre connexion Internet est stable

## Exécution sans Maven

Si vous continuez à rencontrer des problèmes avec Maven, vous pouvez exécuter l'application directement depuis votre IDE :

1. Ouvrez le projet dans votre IDE (IntelliJ IDEA, Eclipse, etc.)
2. Configurez les dépendances JavaFX dans votre IDE
3. Exécutez la classe `org.example.MainApp` directement depuis l'IDE
