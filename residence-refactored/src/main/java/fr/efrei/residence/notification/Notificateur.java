package fr.efrei.residence.notification;

import fr.efrei.residence.etudiant.Etudiant;
import fr.efrei.residence.profil.FabriqueStrategieProfil;

/**
 * Prévient un étudiant sur son canal préféré.
 */
public class Notificateur {

    private final FabriqueCanal canaux;

    public Notificateur(FabriqueCanal canaux) {
        this.canaux = canaux;
    }

    public void notifier(Etudiant e, String sujet, String message) {
        Canal canal = e.getCanalPrefere();
        canaux.pour(canal).notifier(e, sujet, message);
        if (FabriqueStrategieProfil.creer(e.getProfil()).recoitEmailEnAnglais() && canal != Canal.EMAIL) {
            canaux.pour(Canal.EMAIL).notifier(e, "[EN] " + sujet, message);
        }
    }

    public void relancer(Etudiant e, double montantDu, String idBail) {
        canaux.pour(e.getCanalPrefere()).relancer(e, montantDu, idBail);
    }
}