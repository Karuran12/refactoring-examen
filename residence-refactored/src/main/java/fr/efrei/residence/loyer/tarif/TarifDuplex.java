package fr.efrei.residence.loyer.tarif;

import fr.efrei.residence.logement.Logement;

public class TarifDuplex extends StrategieTarif {

    private static final double PRIX_M2_DUPLEX = 12.50;
    private static final double FORFAIT_DUPLEX = 80;

    @Override
    public double loyerDeBase(Logement l) {
        return l.getSurface() * PRIX_M2_DUPLEX + FORFAIT_DUPLEX;
    }
}