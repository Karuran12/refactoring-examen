package fr.efrei.residence.loyer.decorateur;

import fr.efrei.residence.loyer.Loyer;

public class DecorateurParking extends DecorateurLoyer {

    private static final double PRIX_PARKING = 45;

    public DecorateurParking(Loyer loyer) {
        super(loyer);
    }

    @Override
    public double montant() {
        return loyer.montant() + PRIX_PARKING;
    }
}