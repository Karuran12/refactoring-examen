package fr.efrei.residence.notification;

public class FabriqueCanal {

    private final EmailService email = new EmailService();
    private final SmsService sms = new SmsService();
    private final CourrierService courrier = new CourrierService();

    public CanalNotification pour(Canal canal) {
        return switch (canal) {
            case EMAIL -> email;
            case SMS -> sms;
            case COURRIER -> courrier;
        };
    }

    public EmailService getEmail() { return email; }
    public SmsService getSms() { return sms; }
    public CourrierService getCourrier() { return courrier; }
}