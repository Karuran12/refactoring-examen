package fr.efrei.residence.loyer.tarif;

import fr.efrei.residence.logement.Logement;

public class TarifT2 extends StrategieTarif {

    private static final double PRIX_M2_T2 = 12;
    private static final double FORFAIT_T2 = 100;

    @Override
    public double loyerDeBase(Logement l) {
        return l.getSurface() * PRIX_M2_T2 + FORFAIT_T2;
    }
}