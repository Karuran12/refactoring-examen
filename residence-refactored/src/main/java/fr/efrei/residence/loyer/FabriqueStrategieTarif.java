package fr.efrei.residence.loyer;

import fr.efrei.residence.logement.TypeLogement;

/**
 * Seul endroit qui choisit le tarif selon le type de logement.
 */
public class FabriqueStrategieTarif {

    public static StrategieTarif creer(TypeLogement type) {
        return switch (type) {
            case STUDIO -> new TarifStudio();
            case T1 -> new TarifT1();
            case T2 -> new TarifT2();
            case COLOCATION -> new TarifColocation();
        };
    }
}