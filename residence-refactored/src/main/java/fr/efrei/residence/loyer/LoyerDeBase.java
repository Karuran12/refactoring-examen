package fr.efrei.residence.loyer;

import fr.efrei.residence.logement.Logement;
import fr.efrei.residence.loyer.tarif.StrategieTarif;

/**
 * La première couche : le loyer de base, selon le tarif du type de logement.
 */
public class LoyerDeBase implements Loyer {

    private final Logement logement;
    private final StrategieTarif tarif;

    public LoyerDeBase(Logement logement, StrategieTarif tarif) {
        this.logement = logement;
        this.tarif = tarif;
    }

    @Override
    public double montant() {
        return tarif.loyerDeBase(logement);
    }
}