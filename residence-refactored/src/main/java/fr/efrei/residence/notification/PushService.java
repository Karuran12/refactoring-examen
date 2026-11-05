package fr.efrei.residence.notification;

import fr.efrei.residence.etudiant.Etudiant;
import fr.efrei.residence.util.Utils;
import java.util.ArrayList;
import java.util.List;

public class PushService implements CanalNotification {

    private final List<String[]> journal = new ArrayList<String[]>();

    public List<String[]> getJournal() { return journal; }

    public void envoyerPush(String email, String sujet) {
        journal.add(new String[]{email, sujet});
        System.out.println("[PUSH] -> " + email + " : " + sujet);
    }

    @Override
    public void notifier(Etudiant e, String sujet, String message) {
        envoyerPush(e.getEmail(), sujet);
    }

    @Override
    public void relancer(Etudiant e, double montantDu, String idBail) {
        envoyerPush(e.getEmail(), "Loyer impayé : " + Utils.euros(montantDu) + " sur " + idBail);
    }
}