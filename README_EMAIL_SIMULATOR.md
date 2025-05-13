# Simulateur d'envoi d'emails

Ce document explique comment utiliser le simulateur d'envoi d'emails, une alternative à SendGrid qui ne nécessite pas de dépendances externes.

## Pourquoi un simulateur ?

Si vous rencontrez des problèmes avec la dépendance SendGrid, comme :
- Erreurs de téléchargement des dépendances Maven
- Problèmes d'accès aux fichiers JAR
- Absence de connexion Internet
- Absence de clé API SendGrid

Le simulateur vous permet de continuer à développer et tester votre application sans être bloqué par ces problèmes.

## Comment fonctionne le simulateur ?

Au lieu d'envoyer réellement des emails via SendGrid, le simulateur :
1. Affiche le contenu des emails dans la console
2. Simule toujours un envoi réussi
3. Utilise exactement la même interface que le service d'email réel

## Comment utiliser le simulateur ?

### 1. Dans le code

Le simulateur est déjà configuré dans l'application. Les classes suivantes ont été modifiées pour utiliser le simulateur :
- `TrajetService` : Utilise maintenant `EmailServiceSimulator` au lieu de `EmailService`

Si vous souhaitez revenir à l'utilisation de SendGrid une fois les problèmes résolus, modifiez simplement les imports dans `TrajetService.java` :
```java
// Remplacez cette ligne :
import utils.EmailServiceSimulator;

// Par celle-ci :
import utils.EmailService;
```

Et remplacez tous les appels à `EmailServiceSimulator` par `EmailService`.

### 2. Interface de test

Une interface de test du simulateur est disponible dans l'application :
1. Dans le menu principal, cliquez sur "Test d'envoi d'emails"
2. Remplissez les champs comme vous le feriez normalement
3. Cliquez sur "Simuler l'envoi"
4. Consultez la console pour voir le contenu de l'email simulé

## Fonctionnalités simulées

Le simulateur prend en charge les mêmes fonctionnalités que le service d'email réel :
- Confirmation de création de trajet
- Notification de réservation
- Rappels avant départ

## Revenir à SendGrid

Lorsque vous êtes prêt à revenir à l'utilisation de SendGrid :

1. Résolvez les problèmes de dépendances Maven :
   - Nettoyez le cache Maven : `mvn clean`
   - Fermez tous les processus qui pourraient utiliser les fichiers JAR
   - Redémarrez votre ordinateur si nécessaire

2. Modifiez le code pour utiliser SendGrid :
   - Modifiez les imports dans `TrajetService.java`
   - Remplacez les appels à `EmailServiceSimulator` par `EmailService`

3. Configurez votre clé API SendGrid dans `EmailService.java`
