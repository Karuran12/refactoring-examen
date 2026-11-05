package fr.efrei.residence.loyer.decorateur;

import fr.efrei.residence.loyer.Loyer;

public class DecorateurRezDeChaussee extends DecorateurLoyer {

    private static final double COEF_REZ_DE_CHAUSSEE = 0.95;

    public DecorateurRezDeChaussee(Loyer loyer) {
        super(loyer);
    }

    @Override
    public double montant() {
        return loyer.montant() * COEF_REZ_DE_CHAUSSEE;
    }
}