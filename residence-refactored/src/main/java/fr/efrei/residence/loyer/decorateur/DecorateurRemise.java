package fr.efrei.residence.loyer.decorateur;

import fr.efrei.residence.loyer.Loyer;
import fr.efrei.residence.profil.StrategieProfil;

public class DecorateurRemise extends DecorateurLoyer {

    private final StrategieProfil profil;
    private final int echelonBourse;

    public DecorateurRemise(Loyer loyer, StrategieProfil profil, int echelonBourse) {
        super(loyer);
        this.profil = profil;
        this.echelonBourse = echelonBourse;
    }

    @Override
    public double montant() {
        return profil.appliquerRemise(loyer.montant(), echelonBourse);
    }
}