package fr.efrei.residence.logement;

public class Duplex extends Logement {

    public Duplex(String numero, double surface, int etage, boolean meuble) {
        super(numero, TypeLogement.DUPLEX, surface, etage, meuble);
    }
}