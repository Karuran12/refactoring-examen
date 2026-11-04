package fr.efrei.residence.profil;

import fr.efrei.residence.util.Utils;

/**
 * Stratégie : les règles qui dépendent du profil de l'étudiant.
 * Cette classe contient les règles communes. Chaque profil redéfinit seulement ce qui change pour lui.
 */
public abstract class StrategieProfil {

    private static final int MULTIPLE_SOLVABILITE = 3;
    private static final double FRAIS_DOSSIER = 100;
    private static final double TAUX_PENALITE = 0.05;
    private static final double PENALITE_MIN = 10;
    private static final int PREAVIS_JOURS = 30;

    /**
     * @return le motif du refus, ou {@code null} si l'inscription est acceptée
     */
    public String verifierInscription(int echelonBourse, String telephone) {
        return null;
    }

    public double appliquerRemise(double loyer, int echelonBourse) {
        return loyer;
    }

    public boolean estSolvable(double ressources, double loyerChargesComprises) {
        return ressources >= loyerChargesComprises * MULTIPLE_SOLVABILITE;
    }

    public double caution(double loyerHorsCharges) {
        return loyerHorsCharges;
    }

    public double fraisDossier() {
        return FRAIS_DOSSIER;
    }

    public double penaliteDeRetard(double loyerChargesComprises) {
        double penalite = loyerChargesComprises * TAUX_PENALITE;
        if (penalite < PENALITE_MIN) penalite = PENALITE_MIN;
        return Utils.arrondir(penalite);
    }

    public int joursPreavis() {
        return PREAVIS_JOURS;
    }

    public boolean recoitEmailEnAnglais() {
        return false;
    }
}