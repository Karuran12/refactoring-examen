package fr.efrei.residence.loyer.decorateur;

import fr.efrei.residence.loyer.Loyer;

public abstract class DecorateurLoyer implements Loyer {

    protected final Loyer loyer;

    protected DecorateurLoyer(Loyer loyer) {
        this.loyer = loyer;
    }
}