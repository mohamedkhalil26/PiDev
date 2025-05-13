package model;

/**
 * Classe représentant l'empreinte carbone d'un trajet
 */
public class EmpreinteCarbone {
    
    // Émissions en grammes de CO2 par kilomètre
    private double emissionsGCO2ParKm;
    
    // Distance totale en kilomètres
    private double distanceKm;
    
    // Émissions totales en kilogrammes de CO2
    private double emissionsTotalesKgCO2;
    
    // Nombre d'arbres nécessaires pour compenser les émissions (1 arbre absorbe environ 25 kg de CO2 par an)
    private double arbresCompensation;
    
    // Économies par rapport à un trajet solo (en pourcentage)
    private double economiesPourcentage;
    
    // Économies par rapport à un trajet solo (en kg de CO2)
    private double economiesKgCO2;
    
    /**
     * Constructeur
     * @param emissionsGCO2ParKm Émissions en grammes de CO2 par kilomètre
     * @param distanceKm Distance totale en kilomètres
     * @param nbPassagers Nombre de passagers (incluant le conducteur)
     */
    public EmpreinteCarbone(double emissionsGCO2ParKm, double distanceKm, int nbPassagers) {
        this.emissionsGCO2ParKm = emissionsGCO2ParKm;
        this.distanceKm = distanceKm;
        
        // Calcul des émissions totales en kg de CO2
        this.emissionsTotalesKgCO2 = (emissionsGCO2ParKm * distanceKm) / 1000.0;
        
        // Calcul du nombre d'arbres nécessaires pour compenser
        this.arbresCompensation = this.emissionsTotalesKgCO2 / 25.0;
        
        // Calcul des économies par rapport à un trajet solo
        if (nbPassagers > 1) {
            double emissionsSolo = emissionsGCO2ParKm * distanceKm * nbPassagers / 1000.0;
            this.economiesKgCO2 = emissionsSolo - this.emissionsTotalesKgCO2;
            this.economiesPourcentage = (this.economiesKgCO2 / emissionsSolo) * 100.0;
        } else {
            this.economiesKgCO2 = 0.0;
            this.economiesPourcentage = 0.0;
        }
    }
    
    // Getters
    public double getEmissionsGCO2ParKm() {
        return emissionsGCO2ParKm;
    }
    
    public double getDistanceKm() {
        return distanceKm;
    }
    
    public double getEmissionsTotalesKgCO2() {
        return emissionsTotalesKgCO2;
    }
    
    public double getArbresCompensation() {
        return arbresCompensation;
    }
    
    public double getEconomiesPourcentage() {
        return economiesPourcentage;
    }
    
    public double getEconomiesKgCO2() {
        return economiesKgCO2;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Empreinte carbone :\n" +
            "- Distance : %.1f km\n" +
            "- Émissions : %.1f g CO2/km\n" +
            "- Total : %.2f kg CO2\n" +
            "- Arbres pour compenser : %.2f\n" +
            "- Économies : %.2f kg CO2 (%.1f%%)",
            distanceKm, emissionsGCO2ParKm, emissionsTotalesKgCO2, 
            arbresCompensation, economiesKgCO2, economiesPourcentage
        );
    }
}
