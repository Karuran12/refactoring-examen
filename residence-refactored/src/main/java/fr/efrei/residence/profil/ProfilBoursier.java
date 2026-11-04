package fr.efrei.residence.profil;

/**
 * Boursier : remise selon l'échelon, garantie Visale, ni caution ni frais de dossier,
 * pas de pénalité de retard, préavis réduit.
 */
public class ProfilBoursier extends StrategieProfil {

    private static final int ECHELON_MIN = 0;
    private static final int ECHELON_MAX = 7;
    private static final double REMISE_PAR_ECHELON = 0.05;
    private static final double REMISE_MAX_BOURSIER = 0.30;
    private static final int PREAVIS_REDUIT_JOURS = 15;

    @Override
    public String verifierInscription(int echelonBourse, String telephone) {
        if (echelonBourse < ECHELON_MIN || echelonBourse > ECHELON_MAX) {
            return "échelon de bourse invalide";
        }
        return null;
    }

    @Override
    public double appliquerRemise(double loyer, int echelonBourse) {
        double taux = echelonBourse * REMISE_PAR_ECHELON;
        if (taux > REMISE_MAX_BOURSIER) taux = REMISE_MAX_BOURSIER;
        return loyer * (1 - taux);
    }

    @Override
    public boolean estSolvable(double ressources, double loyerChargesComprises) {
        return true; // garantie Visale
    }

    @Override
    public double caution(double loyerHorsCharges) {
        return 0;
    }

    @Override
    public double fraisDossier() {
        return 0;
    }

    @Override
    public double penaliteDeRetard(double loyerChargesComprises) {
        return 0;
    }

    @Override
    public int joursPreavis() {
        return PREAVIS_REDUIT_JOURS;
    }
}