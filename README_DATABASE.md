# Configuration de la base de données

Ce document explique comment configurer la base de données pour l'application de covoiturage.

## Erreur "Table 'covoiturage_app.trajet' doesn't exist"

Si vous rencontrez cette erreur, cela signifie que la table `trajet` n'existe pas encore dans votre base de données. Voici comment résoudre ce problème :

### Solution 1 : Utiliser l'initialisation automatique (recommandé)

L'application est maintenant configurée pour créer automatiquement la table `trajet` au démarrage. Il vous suffit de :

1. Vous assurer que la base de données `covoiturage_app` existe
2. Vérifier que les paramètres de connexion dans `utils/DatabaseUtil.java` sont corrects
3. Redémarrer l'application

### Solution 2 : Exécuter le script SQL manuellement

Si l'initialisation automatique ne fonctionne pas, vous pouvez créer la table manuellement :

1. Ouvrez votre client MySQL (MySQL Workbench, phpMyAdmin, ou ligne de commande MySQL)
2. Connectez-vous à votre base de données `covoiturage_app`
3. Exécutez le script SQL fourni dans le fichier `create_trajet_table.sql`

### Solution 3 : Exécuter les commandes SQL directement

Vous pouvez également exécuter les commandes SQL suivantes directement dans votre client MySQL :

```sql
USE covoiturage_app;

CREATE TABLE IF NOT EXISTS trajet (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ville_depart VARCHAR(100) NOT NULL,
    ville_arrivee VARCHAR(100) NOT NULL,
    date_heure_depart DATETIME NOT NULL,
    nb_places_disponibles INT NOT NULL,
    prix_par_place DECIMAL(10, 2) NOT NULL,
    conducteur_id INT NOT NULL,
    voiture_id INT NOT NULL,
    statut VARCHAR(50) NOT NULL
);

-- Insertion de quelques exemples de trajets (optionnel)
INSERT INTO trajet (ville_depart, ville_arrivee, date_heure_depart, nb_places_disponibles, prix_par_place, conducteur_id, voiture_id, statut) VALUES
('Paris', 'Lyon', '2025-05-15 08:30:00', 3, 25.50, 1, 1, 'planifié'),
('Marseille', 'Nice', '2025-05-16 10:00:00', 2, 15.00, 1, 1, 'planifié'),
('Toulouse', 'Bordeaux', '2025-05-17 14:45:00', 4, 18.75, 2, 2, 'planifié'),
('Lille', 'Paris', '2025-05-18 07:15:00', 1, 22.00, 3, 3, 'planifié'),
('Lyon', 'Grenoble', '2025-05-19 16:30:00', 3, 12.50, 2, 2, 'planifié');
```

## Vérification de la configuration

Pour vérifier que votre base de données est correctement configurée :

1. Connectez-vous à MySQL
2. Exécutez les commandes suivantes :

```sql
USE covoiturage_app;
SHOW TABLES;
SELECT * FROM trajet;
```

Vous devriez voir la table `trajet` dans la liste des tables et les données d'exemple si vous avez choisi de les insérer.
