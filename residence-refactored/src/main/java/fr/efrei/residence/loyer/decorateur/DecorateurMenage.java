package fr.efrei.residence.loyer.decorateur;

import fr.efrei.residence.loyer.Loyer;

public class DecorateurMenage extends DecorateurLoyer {

    private final double coefMenage;

    public DecorateurMenage(Loyer loyer, double coefMenage) {
        super(loyer);
        this.coefMenage = coefMenage;
    }

    @Override
    public double montant() {
        return loyer.montant() * coefMenage;
    }
}