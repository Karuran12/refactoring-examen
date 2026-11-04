package fr.efrei.residence.notification;

/**
 * Fabrique des canaux : seul endroit qui sait quel service créer pour chaque canal.
 */
public class FabriqueCanal {

    public static CanalNotification creer(Canal canal) {
        return switch (canal) {
            case EMAIL -> new EmailService();
            case SMS -> new SmsService();
            case COURRIER -> new CourrierService();
        };
    }
}