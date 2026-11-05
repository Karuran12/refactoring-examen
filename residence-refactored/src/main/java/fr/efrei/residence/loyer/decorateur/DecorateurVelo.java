package fr.efrei.residence.loyer.decorateur;

import fr.efrei.residence.loyer.Loyer;

public class DecorateurVelo extends DecorateurLoyer {

    private static final double PRIX_LOCAL_VELO = 6;

    public DecorateurVelo(Loyer loyer) {
        super(loyer);
    }

    @Override
    public double montant() {
        return loyer.montant() + PRIX_LOCAL_VELO;
    }
}