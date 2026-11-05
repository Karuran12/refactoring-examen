package fr.efrei.residence.service;

import java.time.LocalDate;
import java.time.YearMonth;
import fr.efrei.residence.bail.Bail;
import fr.efrei.residence.bail.Paiement;
import fr.efrei.residence.bail.StatutBail;
import fr.efrei.residence.logement.Logement;
import fr.efrei.residence.notification.Notificateur;
import fr.efrei.residence.persistance.BailRepository;
import fr.efrei.residence.profil.FabriqueStrategieProfil;
import fr.efrei.residence.util.Utils;

/**
 * Fait avancer un bail dans son cycle de vie.
 */
public class ServiceBail {

    private static final int JOUR_ECHEANCE = 5;

    private final BailRepository baux;
    private final Notificateur notificateur;

    public ServiceBail(BailRepository baux, Notificateur notificateur) {
        this.baux = baux;
        this.notificateur = notificateur;
    }

    public boolean entrerDansLesLieux(String idBail, LocalDate date) {
        Bail b = baux.trouverBail(idBail);

        if (b == null || b.getStatut() != StatutBail.SIGNE) {
            System.out.println("Entrée impossible pour " + idBail);
            return false;
        }
        if (date.isBefore(b.getDateSignature())) {
            System.out.println("Entrée impossible avant la signature du bail");
            return false;
        }

        b.setDateEntree(date);
        b.setStatut(StatutBail.ACTIF);
        baux.mettreAJourBail(b);
        System.out.println("Entrée dans les lieux : " + idBail + " le " + Utils.date(date));
        notificateur.notifier(b.getEtudiant(), "État des lieux d'entrée",
                "Votre état des lieux d'entrée du logement " + b.getLogement().getNumero() + " est fait le "
                + Utils.date(date) + ". Remise des clés effectuée. Pensez à souscrire une assurance habitation si ce n'est pas déjà fait.");
        return true;
    }

    public boolean payerLoyer(String idBail, String mois, LocalDate datePaiement, double montant) {
        Bail b = baux.trouverBail(idBail);

        if (b == null || !(b.getStatut() == StatutBail.ACTIF || b.getStatut() == StatutBail.PREAVIS)) {
            System.out.println("Paiement refusé pour " + idBail);
            return false;
        }

        LocalDate echeance = YearMonth.parse(mois).atDay(JOUR_ECHEANCE);
        double penalite = 0;

        if (datePaiement.isAfter(echeance)) {
            penalite = FabriqueStrategieProfil.creer(b.getEtudiant().getProfil()).penaliteDeRetard(b.getLoyerChargesComprises());
        }

        double du = Utils.arrondir(b.getLoyerChargesComprises() + penalite);

        b.paiements.add(new Paiement(mois, datePaiement, montant, penalite, du));
        b.soldeDu = Utils.arrondir(b.soldeDu + du - montant);
        baux.mettreAJourBail(b);

        System.out.println("Paiement " + idBail + " " + mois + " : " + Utils.euros(montant) + " / " + Utils.euros(du)
                + (penalite > 0 ? " (dont pénalité " + Utils.euros(penalite) + ")" : ""));
        if (montant < du) {
            notificateur.notifier(b.getEtudiant(), "Paiement incomplet " + mois,
                    "Nous avons reçu " + Utils.euros(montant) + " sur " + Utils.euros(du) + ". Reste dû : "
                            + Utils.euros(Utils.arrondir(du - montant)) + ".");
        } else {
            notificateur.notifier(b.getEtudiant(), "Quittance " + mois, "Merci, votre loyer de " + mois + " est réglé.");
        }
        return true;
    }

    public boolean donnerPreavis(String idBail, LocalDate date) {
        Bail b = baux.trouverBail(idBail);

        if (b == null || b.getStatut() != StatutBail.ACTIF) {
            System.out.println("Préavis impossible pour " + idBail);
            return false;
        }

        int jours = FabriqueStrategieProfil.creer(b.getEtudiant().getProfil()).joursPreavis();

        b.setDateFin(date.plusDays(jours));
        b.setStatut(StatutBail.PREAVIS);
        baux.mettreAJourBail(b);
        System.out.println("Préavis " + idBail + " : fin du bail le " + Utils.date(b.getDateFin()));
        notificateur.notifier(b.getEtudiant(), "Préavis de départ",
                "Votre préavis de " + jours + " jours est enregistré. Fin du bail le " + Utils.date(b.getDateFin()) + ".");
        return true;
    }

    public boolean sortir(String idBail, LocalDate date, double retenuesDegats) {
        Bail b = baux.trouverBail(idBail);

        if (b == null || b.getStatut() != StatutBail.PREAVIS) {
            System.out.println("Sortie impossible pour " + idBail);
            return false;
        }
        if (date.isBefore(b.getDateFin())) {
            System.out.println("Sortie impossible avant la fin du préavis (" + Utils.date(b.getDateFin()) + ")");
            return false;
        }

        Logement l = b.getLogement();
        l.libererApresSortie(b.getEtudiant());

        double impayes = b.soldeDu > 0 ? b.soldeDu : 0;
        double restitution = b.getCaution() - retenuesDegats - impayes;
        double resteAPayer = 0;
        if (restitution < 0) {
            resteAPayer = -restitution;
            restitution = 0;
        }
        b.retenues = Utils.arrondir(retenuesDegats);
        b.cautionRestituee = Utils.arrondir(restitution);
        b.soldeDu = Utils.arrondir(resteAPayer);
        b.setStatut(StatutBail.TERMINE);
        baux.mettreAJourBail(b);

        System.out.println("Sortie " + idBail + " le " + Utils.date(date) + " : caution " + Utils.euros(b.getCaution())
                + ", retenues " + Utils.euros(b.retenues) + ", impayés " + Utils.euros(impayes)
                + " -> restitué " + Utils.euros(b.cautionRestituee));

        String message = "Votre dépôt de garantie : " + Utils.euros(b.cautionRestituee) + " vous sera restitué sous 30 jours.";
        if (resteAPayer > 0) {
            message = message + " Il reste " + Utils.euros(b.soldeDu) + " à régler.";
        }
        notificateur.notifier(b.getEtudiant(), "Sortie du logement " + l.getNumero(), message);
        return true;
    }

    public boolean annulerBail(String idBail) {
        Bail b = baux.trouverBail(idBail);

        if (b == null || b.getStatut() != StatutBail.SIGNE) {
            System.out.println("Annulation impossible pour " + idBail);
            return false;
        }

        Logement l = b.getLogement();
        l.liberer(b.getEtudiant());
        
        b.setStatut(StatutBail.ANNULE);
        baux.mettreAJourBail(b);
        System.out.println("Bail " + idBail + " annulé, frais de dossier conservés : " + Utils.euros(b.getFraisDossier()));
        notificateur.notifier(b.getEtudiant(), "Annulation du bail " + idBail, "Votre bail est annulé. Les frais de dossier (" + Utils.euros(b.getFraisDossier()) + ") restent acquis.");
        return true;
    }
}