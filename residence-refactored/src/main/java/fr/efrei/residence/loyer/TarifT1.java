package fr.efrei.residence.loyer;

import fr.efrei.residence.logement.Logement;

/**
 * T1 : prix au m² plus un forfait.
 */
public class TarifT1 extends StrategieTarif {

    private static final double PRIX_M2_T1 = 13;
    private static final double FORFAIT_T1 = 50;

    @Override
    public double loyerDeBase(Logement l) {
        return l.getSurface() * PRIX_M2_T1 + FORFAIT_T1;
    }
}