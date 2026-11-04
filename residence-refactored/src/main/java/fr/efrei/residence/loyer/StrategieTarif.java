package fr.efrei.residence.loyer;

import fr.efrei.residence.logement.Logement;

/**
 * Stratégie : le tarif propre à chaque type de logement.
 * Cette classe contient les règles communes. Chaque type redéfinit seulement ce qui change pour lui.
 */
public abstract class StrategieTarif {

    private static final double COEF_MENAGE = 1.08;
    private static final double CHARGES = 35;

    public abstract double loyerDeBase(Logement l);

    public double coefMenage() {
        return COEF_MENAGE;
    }

    public boolean fibreIncluse() {
        return false;
    }

    public double charges() {
        return CHARGES;
    }
}