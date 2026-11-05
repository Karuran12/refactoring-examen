package fr.efrei.residence.profil;

public class ProfilSportif extends StrategieProfil {

    private static final double COEF_SPORTIF = 0.85;
    private static final int MULTIPLE_SOLVABILITE_SPORTIF = 2;
    private static final double FRAIS_DOSSIER_SPORTIF = 50;
    private static final int PREAVIS_REDUIT_JOURS = 15;

    @Override
    public double appliquerRemise(double loyer, int echelonBourse) {
        return loyer * COEF_SPORTIF;
    }

    @Override
    public boolean estSolvable(double ressources, double loyerChargesComprises) {
        return ressources >= loyerChargesComprises * MULTIPLE_SOLVABILITE_SPORTIF;
    }

    @Override
    public double fraisDossier() {
        return FRAIS_DOSSIER_SPORTIF;
    }

    @Override
    public int joursPreavis() {
        return PREAVIS_REDUIT_JOURS;
    }
}