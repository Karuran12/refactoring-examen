package fr.efrei.residence;

/**
 * Les types de logement de la résidence.
 */
public enum TypeLogement {
    STUDIO, T1, T2, COLOCATION;

    /**
     * Convertit un code texte (ligne du catalogue, saisie) en type de logement.
     *
     * @return le type correspondant, ou {@code null} si le code est inconnu
     */
    public static TypeLogement depuisCode(String code) {
        for (TypeLogement type : values()) {
            if (type.name().equals(code)) {
                return type;
            }
        }
        return null;
    }
}