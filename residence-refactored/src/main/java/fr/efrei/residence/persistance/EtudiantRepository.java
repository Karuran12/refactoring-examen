package fr.efrei.residence.persistance;

import fr.efrei.residence.etudiant.Etudiant;


public interface EtudiantRepository {

    void sauvegarderEtudiant(Etudiant e);

    Etudiant trouverEtudiantParEmail(String email);
    Etudiant trouverEtudiantParId(String id);
}