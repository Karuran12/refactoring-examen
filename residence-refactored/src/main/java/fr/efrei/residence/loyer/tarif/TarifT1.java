package fr.efrei.residence.loyer.tarif;

import fr.efrei.residence.logement.Logement;

public class TarifT1 extends StrategieTarif {

    private static final double PRIX_M2_T1 = 13;
    private static final double FORFAIT_T1 = 50;

    @Override
    public double loyerDeBase(Logement l) {
        return l.getSurface() * PRIX_M2_T1 + FORFAIT_T1;
    }
}