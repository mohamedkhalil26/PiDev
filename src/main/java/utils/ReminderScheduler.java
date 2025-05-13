package utils;

import service.TrajetService;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Planificateur pour l'envoi automatique de rappels
 */
public class ReminderScheduler {
    
    private static Timer timer;
    private static final long PERIOD = TimeUnit.HOURS.toMillis(24); // Vérification quotidienne
    
    /**
     * Démarre le planificateur de rappels
     */
    public static void start() {
        if (timer != null) {
            stop(); // Arrêter le timer existant s'il y en a un
        }
        
        timer = new Timer(true); // true = daemon thread
        
        // Planifier la tâche pour s'exécuter chaque jour
        timer.scheduleAtFixedRate(new ReminderTask(), 0, PERIOD);
        
        System.out.println("Planificateur de rappels démarré");
    }
    
    /**
     * Arrête le planificateur de rappels
     */
    public static void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            System.out.println("Planificateur de rappels arrêté");
        }
    }
    
    /**
     * Tâche qui envoie les rappels pour les trajets du lendemain
     */
    private static class ReminderTask extends TimerTask {
        @Override
        public void run() {
            try {
                System.out.println("Exécution de la tâche d'envoi de rappels...");
                
                TrajetService trajetService = new TrajetService();
                int nombreRappelsEnvoyes = trajetService.envoyerRappelsTrajetsLendemain();
                
                System.out.println("Nombre de rappels envoyés : " + nombreRappelsEnvoyes);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi des rappels : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
