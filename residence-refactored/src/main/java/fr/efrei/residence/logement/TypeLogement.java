package fr.efrei.residence.logement;

/**
 * Les types de logement de la résidence.
 */
public enum TypeLogement {
    STUDIO("Studios", ""),
    T1("T1", ""),
    T2("T2", ""),
    COLOCATION("Colocation", " places");

    private final String libelle;
    private final String unite;

    TypeLogement(String libelle, String unite) {
        this.libelle = libelle;
        this.unite = unite;
    }

    public String getLibelle() { return libelle; }
    public String getUnite() { return unite; }

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