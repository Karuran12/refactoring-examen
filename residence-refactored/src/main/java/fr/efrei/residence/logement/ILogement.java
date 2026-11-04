package fr.efrei.residence.logement;

import java.util.List;
import fr.efrei.residence.etudiant.Etudiant;

/**
 * Tout ce qu'on peut faire avec un logement de la résidence.
 */
public interface ILogement {

    String getNumero();

    TypeLogement getType();

    double getSurface();

    int getEtage();

    boolean isMeuble();

    boolean estLibre();

    boolean estAccessiblePMR();

    void loger(Etudiant e);

    void liberer(Etudiant e);

    Etudiant getOccupant();

    // --- colocation ---
    void ajouterColocataire(Etudiant e);

    void retirerColocataire(Etudiant e);

    List<Etudiant> getColocataires();

    int getNombrePlaces();

    // --- extérieur ---
    double getSurfaceBalcon();

    // --- entretien ---
    void nettoyerPartiesCommunes();
}
