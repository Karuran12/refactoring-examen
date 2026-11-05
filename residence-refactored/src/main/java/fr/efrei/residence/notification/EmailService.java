package fr.efrei.residence.notification;

import fr.efrei.residence.etudiant.Etudiant;
import fr.efrei.residence.util.Utils;
import java.util.ArrayList;
import java.util.List;

public class EmailService implements CanalNotification {

    public static final String SMTP_HOST = "smtp.campusvert.fr";
    public static final String EXPEDITEUR = "gestion@campusvert.fr";

    private final List<String[]> journal = new ArrayList<String[]>();
    public List<String[]> getJournal() { return journal; }

    public boolean envoyer(String destinataire, String sujet, String corps) {
        if (!Utils.emailValide(destinataire)) {
            return false;
        }
        System.out.println("[EMAIL] " + EXPEDITEUR + " -> " + destinataire + " | " + sujet);
        System.out.println("        " + corps);
        journal.add(new String[]{destinataire, sujet, corps});
        return true;
    }

    @Override
    public void notifier(Etudiant e, String sujet, String message) {
        envoyer(e.getEmail(), sujet, message);
    }

    @Override
    public void relancer(Etudiant e, double montantDu, String idBail) {
        envoyer(e.getEmail(), "RELANCE - loyer impayé",
                "Bonjour " + e.getPrenom() + ", il reste " + Utils.euros(montantDu) + " à régler sur le bail " + idBail + ".");
    }
}
