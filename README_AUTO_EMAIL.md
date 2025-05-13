# Envoi automatique d'emails

Ce document explique comment fonctionne le système d'envoi automatique d'emails dans l'application de covoiturage.

## Fonctionnalités d'envoi automatique

L'application envoie automatiquement des emails à différents moments clés :

### 1. Lors de la création d'un trajet
- Un email de confirmation est envoyé au conducteur
- Contient les détails du trajet (départ, arrivée, date, heure, etc.)
- Envoyé immédiatement après la création du trajet

### 2. Lors de la réservation de places
- Un email de confirmation est envoyé au passager
- Contient les détails du trajet et de la réservation
- Envoyé immédiatement après la réservation

### 3. Rappels avant départ
- Des rappels sont envoyés 24 heures avant le départ
- Envoyés à la fois au conducteur et aux passagers
- Contiennent les détails du trajet et des conseils spécifiques

### 4. Rappels urgents
- Des rappels urgents sont envoyés 1 heure avant le départ
- Rappellent aux participants que le départ est imminent

### 5. Après le trajet
- Des emails de demande d'évaluation sont envoyés après le trajet
- Permettent aux utilisateurs de donner leur avis sur l'expérience

## Comment ça marche

Le système d'envoi automatique d'emails fonctionne grâce à deux composants principaux :

### 1. AutoEmailService
- Gère l'envoi automatique d'emails à différents moments
- Vérifie régulièrement les trajets pour envoyer des rappels
- S'exécute en arrière-plan pendant toute la durée de vie de l'application

### 2. EmailServiceSimulator
- Simule l'envoi d'emails en les affichant dans la console
- Permet de tester le système sans dépendance externe
- Peut être remplacé par un service d'email réel (SendGrid)

## Configuration

Par défaut, tous les emails sont envoyés à l'adresse `takwabouabid149@gmail.com` pour faciliter les tests.

Pour modifier cette adresse :
1. Ouvrez le fichier `src/main/java/utils/AutoEmailService.java`
2. Modifiez la valeur de `DEFAULT_EMAIL`

## Test de l'envoi automatique

L'application inclut une interface de test pour l'envoi automatique d'emails :

1. Dans le menu principal, cliquez sur "Test d'envoi automatique d'emails"
2. Vous pouvez :
   - Créer un trajet avec envoi automatique d'email
   - Réserver des places avec envoi automatique d'email
   - Simuler un rappel avant départ

## Personnalisation des emails

Pour personnaliser le contenu des emails :

1. Ouvrez le fichier `src/main/java/utils/EmailServiceSimulator.java`
2. Modifiez les méthodes correspondant aux différents types d'emails

## Passage à un service d'email réel

Pour utiliser un service d'email réel comme SendGrid :

1. Configurez la dépendance SendGrid dans le pom.xml
2. Configurez votre clé API dans `EmailService.java`
3. Modifiez les imports dans `TrajetService.java` et `AutoEmailService.java` pour utiliser `EmailService` au lieu de `EmailServiceSimulator`
