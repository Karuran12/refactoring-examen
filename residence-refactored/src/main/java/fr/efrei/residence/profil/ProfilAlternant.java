package fr.efrei.residence.profil;

/**
 * Alternant : 10 % de remise, solvabilité à 2 fois le loyer, préavis réduit.
 */
public class ProfilAlternant extends StrategieProfil {

    private static final double COEF_ALTERNANT = 0.90;
    private static final int MULTIPLE_SOLVABILITE_ALTERNANT = 2;
    private static final int PREAVIS_REDUIT_JOURS = 15;

    @Override
    public double appliquerRemise(double loyer, int echelonBourse) {
        return loyer * COEF_ALTERNANT;
    }

    @Override
    public boolean estSolvable(double ressources, double loyerChargesComprises) {
        return ressources >= loyerChargesComprises * MULTIPLE_SOLVABILITE_ALTERNANT;
    }

    @Override
    public int joursPreavis() {
        return PREAVIS_REDUIT_JOURS;
    }
}