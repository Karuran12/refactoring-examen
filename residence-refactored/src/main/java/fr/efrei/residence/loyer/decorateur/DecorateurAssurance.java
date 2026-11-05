package fr.efrei.residence.loyer.decorateur;

import fr.efrei.residence.loyer.Loyer;

public class DecorateurAssurance extends DecorateurLoyer {

    private static final double PRIX_ASSURANCE = 9;

    public DecorateurAssurance(Loyer loyer) {
        super(loyer);
    }

    @Override
    public double montant() {
        return loyer.montant() + PRIX_ASSURANCE;
    }
}