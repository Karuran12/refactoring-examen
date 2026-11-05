package fr.efrei.residence.loyer.tarif;

import fr.efrei.residence.logement.Logement;

public class TarifStudio extends StrategieTarif {

    private static final double PRIX_M2_STUDIO = 14;
    private static final double LOYER_MIN_STUDIO = 350;

    @Override
    public double loyerDeBase(Logement l) {
        double loyer = l.getSurface() * PRIX_M2_STUDIO;
        if (loyer < LOYER_MIN_STUDIO) loyer = LOYER_MIN_STUDIO;
        return loyer;
    }
}