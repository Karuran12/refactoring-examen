package fr.efrei.residence.loyer.tarif;

import fr.efrei.residence.logement.TypeLogement;

public class FabriqueStrategieTarif {

    public static StrategieTarif creer(TypeLogement type) {
        return switch (type) {
            case STUDIO -> new TarifStudio();
            case T1 -> new TarifT1();
            case T2 -> new TarifT2();
            case COLOCATION -> new TarifColocation();
            case DUPLEX -> new TarifDuplex();        
        };
    }
}