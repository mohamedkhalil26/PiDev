# Implémentation de l'entité Trajet

Ce document explique comment utiliser la nouvelle entité Trajet ajoutée à l'application.

## Configuration de la base de données

Avant d'utiliser les fonctionnalités liées aux trajets, vous devez créer la table correspondante dans votre base de données MySQL.

### Option 1 : Script avec contraintes de clé étrangère

Si vous avez déjà les tables `utilisateur` et `voiture` dans votre base de données, utilisez le script suivant :

```sql
-- Exécutez ce script dans votre client MySQL
source src/main/resources/sql/create_trajet_table.sql
```

### Option 2 : Script sans contraintes de clé étrangère

Si vous n'avez pas encore les tables référencées ou si vous souhaitez tester sans contraintes :

```sql
-- Exécutez ce script dans votre client MySQL
source src/main/resources/sql/create_trajet_table_simple.sql
```

## Utilisation de l'application

1. Lancez l'application en exécutant la classe `org.example.MainApp`
2. Dans le menu principal, cliquez sur "Gestion des Trajets"
3. Vous pouvez maintenant :
   - Voir la liste des trajets
   - Ajouter un nouveau trajet
   - Modifier un trajet existant
   - Supprimer un trajet
   - Rechercher des trajets par ville de départ, ville d'arrivée ou date

## Structure des fichiers

### Backend
- `model/Trajet.java` : Classe modèle pour l'entité Trajet
- `dao/TrajetDAO.java` : Classe d'accès aux données pour les trajets
- `service/TrajetService.java` : Classe de service pour la logique métier des trajets

### Frontend
- `resources/ListeTrajet.fxml` : Interface pour afficher la liste des trajets
- `resources/AjouterTrajet.fxml` : Formulaire pour ajouter un trajet
- `resources/ModifierTrajet.fxml` : Formulaire pour modifier un trajet
- `resources/AfficherTrajet.fxml` : Interface pour afficher les détails d'un trajet
- `controller/ListeTrajetController.java` : Contrôleur pour la liste des trajets
- `controller/AjouterTrajetController.java` : Contrôleur pour l'ajout de trajet
- `controller/ModifierTrajetController.java` : Contrôleur pour la modification de trajet
- `controller/AfficherTrajetController.java` : Contrôleur pour l'affichage des détails

## Dépannage

Si vous rencontrez des erreurs :

1. **Erreur de connexion à la base de données** : Vérifiez les paramètres dans `utils/DatabaseUtil.java`
2. **Erreur de clé étrangère** : Utilisez le script sans contraintes ou créez d'abord les tables référencées
3. **Erreur JavaFX** : Assurez-vous que le module-info.java est correctement configuré
4. **Problèmes avec les dates** : Vérifiez le format des dates dans les contrôleurs

## Notes importantes

- Le statut d'un trajet doit être l'une des valeurs suivantes : "planifié", "en cours", "terminé", "annulé"
- Les ID de conducteur doivent correspondre à des utilisateurs existants
- Les ID de voiture doivent correspondre à des voitures existantes
