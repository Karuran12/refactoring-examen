package fr.efrei.residence.loyer.tarif;

import fr.efrei.residence.logement.Logement;

public class TarifColocation extends StrategieTarif {

    private static final double PRIX_M2_COLOCATION = 11;
    private static final double COEF_MENAGE_COLOCATION = 1.04;
    private static final double CHARGES_COLOCATION = 25;

    @Override
    public double loyerDeBase(Logement l) {
        return l.getSurface() * PRIX_M2_COLOCATION / l.getNombrePlaces();
    }

    @Override
    public double coefMenage() {
        return COEF_MENAGE_COLOCATION; // parties communes déjà nettoyées
    }

    @Override
    public boolean fibreIncluse() {
        return true;
    }

    @Override
    public double charges() {
        return CHARGES_COLOCATION;
    }
}