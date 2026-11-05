package fr.efrei.residence.loyer.tarif;

import fr.efrei.residence.logement.Logement;

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