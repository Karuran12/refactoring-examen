package fr.efrei.residence.service;

import fr.efrei.residence.etudiant.Etudiant;
import fr.efrei.residence.etudiant.Profil;
import fr.efrei.residence.notification.Canal;
import fr.efrei.residence.persistance.EtudiantRepository;
import fr.efrei.residence.profil.FabriqueStrategieProfil;
import fr.efrei.residence.util.Utils;

/**
 * Inscrit un étudiant si sa demande est valide.
 */
public class ServiceInscription {

    private final EtudiantRepository etudiants;
    private int compteurEtudiant = 0;

    public ServiceInscription(EtudiantRepository etudiants) {
        this.etudiants = etudiants;
    }

    public Etudiant inscrireEtudiant(String nom, String prenom, String email, String telephone, String adresse,
                                     Profil profil, int echelonBourse, double ressources, boolean pmr,
                                     Canal canalPrefere) {
        if (nom == null || nom.trim().isEmpty()) {
            System.out.println("Inscription refusée : nom obligatoire");
            return null;
        }
        if (!Utils.emailValide(email)) {
            System.out.println("Inscription refusée : email invalide (" + email + ")");
            return null;
        }
        if (etudiants.trouverEtudiantParEmail(email) != null) {
            System.out.println("Inscription refusée : " + email + " déjà inscrit");
            return null;
        }
        if (profil == null) {
            System.out.println("Inscription refusée : profil inconnu " + profil);
            return null;
        }

        String refus = FabriqueStrategieProfil.creer(profil).verifierInscription(echelonBourse, telephone);
        if (refus != null) {
            System.out.println("Inscription refusée : " + refus);
            return null;
        }
        if (canalPrefere == Canal.SMS && telephone == null) {
            System.out.println("Inscription refusée : téléphone obligatoire pour les SMS");
            return null;
        }
        if (canalPrefere == Canal.COURRIER && (adresse == null || adresse.isEmpty())) {
            System.out.println("Inscription refusée : adresse obligatoire pour le courrier");
            return null;
        }
        compteurEtudiant++;

        Etudiant e = new Etudiant("ETU-" + compteurEtudiant, nom, prenom, email, telephone, adresse, profil, echelonBourse, ressources, pmr, canalPrefere);

        etudiants.sauvegarderEtudiant(e);
        System.out.println("Étudiant inscrit : " + e.getId() + " " + e.getNomComplet() + " (" + profil + ")");
        return e;
    }
}