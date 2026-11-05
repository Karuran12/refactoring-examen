package fr.efrei.residence.loyer.decorateur;

import fr.efrei.residence.loyer.Loyer;

public class DecorateurMeuble extends DecorateurLoyer {

    private static final double COEF_MEUBLE = 1.15;

    public DecorateurMeuble(Loyer loyer) {
        super(loyer);
    }

    @Override
    public double montant() {
        return loyer.montant() * COEF_MEUBLE;
    }
}