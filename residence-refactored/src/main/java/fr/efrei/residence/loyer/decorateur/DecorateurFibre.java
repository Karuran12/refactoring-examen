package fr.efrei.residence.loyer.decorateur;

import fr.efrei.residence.loyer.Loyer;

public class DecorateurFibre extends DecorateurLoyer {

    private static final double PRIX_FIBRE = 20;

    public DecorateurFibre(Loyer loyer) {
        super(loyer);
    }

    @Override
    public double montant() {
        return loyer.montant() + PRIX_FIBRE;
    }
}