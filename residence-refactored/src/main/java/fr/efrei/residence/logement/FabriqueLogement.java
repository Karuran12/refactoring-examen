package fr.efrei.residence.logement;

/**
 * Fabrique des logements : seul endroit qui sait quelle classe créer pour chaque type.
 */
public class FabriqueLogement {

    private static final int PLACES_MIN_COLOCATION = 2;

    public static Logement creer(TypeLogement type, String numero, double surface, int etage, boolean meuble, int param) {
        return switch (type) {
            case STUDIO -> new Studio(numero, surface, etage, meuble);
            case T1 -> new T1(numero, surface, etage, meuble);
            case T2 -> new T2(numero, surface, etage, meuble, param);
            case COLOCATION -> new Colocation(numero, surface, etage, meuble, Math.max(param, PLACES_MIN_COLOCATION));
        };
    }
}