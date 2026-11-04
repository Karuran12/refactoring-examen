package fr.efrei.residence.notification;

import fr.efrei.residence.etudiant.Etudiant;

/**
 * Un moyen de prévenir un étudiant : email, SMS ou courrier.
 */
public interface CanalNotification {

    void notifier(Etudiant e, String sujet, String message);

    void relancer(Etudiant e, double montantDu, String idBail);
}