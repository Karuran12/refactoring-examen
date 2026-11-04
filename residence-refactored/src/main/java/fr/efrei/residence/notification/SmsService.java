package fr.efrei.residence.notification;

import fr.efrei.residence.etudiant.Etudiant;
import fr.efrei.residence.util.Utils;
import java.util.ArrayList;
import java.util.List;

public class SmsService implements CanalNotification {

    private static final int LONGUEUR_MIN_NUMERO = 10;
    private static final int LONGUEUR_MAX_SMS = 160;
    private static final String POINTS_DE_SUSPENSION = "...";

    public static int compteurSms = 0;
    public static List<String[]> journal = new ArrayList<String[]>();

    public void envoyerSms(String numero, String message) {
        if (numero == null || numero.length() < LONGUEUR_MIN_NUMERO) {
            System.out.println("[SMS] numéro invalide : " + numero);
            return;
        }
        if (message.length() > LONGUEUR_MAX_SMS) {
            message = message.substring(0, LONGUEUR_MAX_SMS - POINTS_DE_SUSPENSION.length()) + POINTS_DE_SUSPENSION;
        }
        compteurSms++;
        journal.add(new String[]{numero, message});
        System.out.println("[SMS] -> " + numero + " : " + message);
    }

    @Override
    public void notifier(Etudiant e, String sujet, String message) {
        envoyerSms(e.getTelephone(), sujet + " - " + message);
    }

    @Override
    public void relancer(Etudiant e, double montantDu, String idBail) {
        envoyerSms(e.getTelephone(), "Campus Vert : " + Utils.euros(montantDu) + " impayés sur " + idBail);
    }

}
