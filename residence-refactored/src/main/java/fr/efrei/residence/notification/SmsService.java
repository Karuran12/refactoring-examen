package fr.efrei.residence.notification;

import java.util.ArrayList;
import java.util.List;

public class SmsService {

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
}
