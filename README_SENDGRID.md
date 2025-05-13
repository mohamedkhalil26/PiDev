# Intégration de SendGrid pour l'envoi d'emails

Ce document explique comment configurer et utiliser l'intégration SendGrid pour l'envoi d'emails dans l'application de covoiturage.

## Configuration de SendGrid

### 1. Créer un compte SendGrid

1. Rendez-vous sur [SendGrid](https://sendgrid.com/) et créez un compte gratuit
2. Une fois connecté, accédez à la section "API Keys" dans les paramètres
3. Créez une nouvelle clé API avec les permissions "Mail Send"
4. Copiez la clé API générée (vous ne pourrez plus la voir après avoir quitté la page)

### 2. Configurer l'application

1. Ouvrez le fichier `src/main/java/utils/EmailService.java`
2. Remplacez la valeur de `SENDGRID_API_KEY` par votre clé API SendGrid :
   ```java
   private static final String SENDGRID_API_KEY = "VOTRE_CLE_API_SENDGRID";
   ```
3. Modifiez également l'adresse email d'expédition si nécessaire :
   ```java
   private static final String FROM_EMAIL = "votre-email@domaine.com";
   private static final String FROM_NAME = "Nom de votre service";
   ```

## Fonctionnalités d'envoi d'emails

L'application intègre l'envoi d'emails pour trois cas d'utilisation principaux :

### 1. Confirmation de création de trajet

Lorsqu'un conducteur crée un nouveau trajet, un email de confirmation lui est envoyé avec les détails du trajet.

Pour utiliser cette fonctionnalité :
- Lors de la création d'un trajet, renseignez l'email du conducteur dans le champ prévu à cet effet
- Un email sera automatiquement envoyé après la création réussie du trajet

### 2. Notification de réservation

Lorsqu'un passager réserve une place pour un trajet, un email de confirmation lui est envoyé.

Pour utiliser cette fonctionnalité :
- Utilisez la méthode `reserverPlaces` du service `TrajetService` en fournissant l'email du passager
- Un email sera automatiquement envoyé après la réservation réussie

### 3. Rappels avant départ

Le système envoie automatiquement des rappels aux conducteurs et passagers 24 heures avant le départ d'un trajet.

Cette fonctionnalité est gérée par un planificateur qui s'exécute en arrière-plan :
- Le planificateur démarre automatiquement au lancement de l'application
- Il vérifie quotidiennement les trajets prévus pour le lendemain
- Des emails de rappel sont envoyés aux conducteurs et passagers concernés

## Test de l'envoi d'emails

L'application inclut une interface de test pour vérifier la configuration de SendGrid :

1. Dans le menu principal, cliquez sur "Test d'envoi d'emails"
2. Remplissez les champs :
   - Email du destinataire : votre adresse email pour recevoir le test
   - Sujet : sujet de l'email de test
   - Message : contenu de l'email (le HTML est supporté)
3. Cliquez sur "Envoyer l'email"
4. Vérifiez votre boîte de réception (et éventuellement le dossier spam)

## Dépannage

Si vous rencontrez des problèmes avec l'envoi d'emails :

1. **Vérifiez la console** pour les messages d'erreur détaillés
2. **Vérifiez votre clé API** dans `EmailService.java`
3. **Vérifiez votre connexion Internet**
4. **Vérifiez les quotas SendGrid** (le plan gratuit a des limites)
5. **Vérifiez le dossier spam** de votre boîte de réception

## Personnalisation des templates d'emails

Pour personnaliser les templates d'emails :

1. Ouvrez le fichier `src/main/java/utils/EmailService.java`
2. Modifiez les méthodes correspondant aux différents types d'emails :
   - `envoyerConfirmationCreationTrajet`
   - `envoyerNotificationReservation`
   - `envoyerRappelAvantDepart`
3. Personnalisez le contenu HTML selon vos besoins
