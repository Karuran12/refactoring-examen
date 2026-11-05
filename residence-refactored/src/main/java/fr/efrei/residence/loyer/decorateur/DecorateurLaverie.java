package fr.efrei.residence.loyer.decorateur;

import fr.efrei.residence.loyer.Loyer;

public class DecorateurLaverie extends DecorateurLoyer {

    private static final double PRIX_LAVERIE = 12;

    public DecorateurLaverie(Loyer loyer) {
        super(loyer);
    }

    @Override
    public double montant() {
        return loyer.montant() + PRIX_LAVERIE;
    }
}