package fr.efrei.residence.loyer;

public enum Option {
    MENAGE("ménage"),
    PARKING("parking"),
    LAVERIE("laverie"),
    FIBRE("fibre"),
    ASSURANCE("assurance"),
    VELO("local vélo");

    private final String libelle;

    Option(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() { return libelle; }
}