package fr.efrei.residence;

import fr.efrei.residence.bail.Bail;
import fr.efrei.residence.bail.FormateurQuittance;
import fr.efrei.residence.etudiant.Etudiant;
import fr.efrei.residence.etudiant.Profil;
import fr.efrei.residence.logement.Colocation;
import fr.efrei.residence.logement.Logement;
import fr.efrei.residence.logement.Residence;
import fr.efrei.residence.loyer.CalculateurLoyer;
import fr.efrei.residence.notification.FabriqueCanal;
import fr.efrei.residence.notification.Notificateur;
import fr.efrei.residence.persistance.Database;
import fr.efrei.residence.service.GenerateurRapport;
import fr.efrei.residence.service.ServiceAttribution;
import fr.efrei.residence.service.ServiceBail;
import fr.efrei.residence.service.ServiceInscription;
import fr.efrei.residence.service.ServiceRelance;

import fr.efrei.residence.contrat.Canal;
import fr.efrei.residence.contrat.DemandeAttribution;
import fr.efrei.residence.contrat.DemandeDevis;
import fr.efrei.residence.contrat.DemandeInscription;
import fr.efrei.residence.contrat.DemandeLogement;
import fr.efrei.residence.contrat.EtatBail;
import fr.efrei.residence.contrat.EtatLogement;
import fr.efrei.residence.contrat.Notification;
import fr.efrei.residence.contrat.Option;
import fr.efrei.residence.contrat.StatutBail;
import fr.efrei.residence.contrat.SystemeResidence;
import fr.efrei.residence.contrat.TypeLogement;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;

/**
 * Branche le code historique sur les tests de contrat.
 * <p>
 * Tout le « bricolage » nécessaire pour tester l'ancien code (état statique à vider,
 * conversion des codes {@code String}, booléens d'options…) est confiné ici.
 */
public class NouveauSystemeAdapter implements SystemeResidence {

    private GestionResidence gestion;
    private FabriqueCanal canaux;
    private Database db;
    private Residence residence;
    private CalculateurLoyer calculateur;

    @Override
    public void reinitialiser() {
        db = new Database();
        residence = new Residence();
        calculateur = new CalculateurLoyer();
        canaux = new FabriqueCanal();
        Notificateur notificateur = new Notificateur(canaux);
        gestion = new GestionResidence(
                                        residence,
                                        new ServiceInscription(db),
                                        new ServiceAttribution(residence, calculateur, db, notificateur),
                                        new ServiceBail(db, notificateur),
                                        new ServiceRelance(db, notificateur),
                                        new GenerateurRapport(residence, db, canaux),
                                        new FormateurQuittance());
    }

    @Override
    public Optional<String> inscrire(DemandeInscription d) {
        Etudiant e = gestion.inscrireEtudiant(d.nom(), d.prenom(), d.email(), d.telephone(), d.adresse(),
        versNotreProfil(d.profil()), d.echelonBourse(), d.ressourcesMensuelles(), d.pmr(), versNotreCanal(d.canalPrefere()));
        return Optional.ofNullable(e).map(Etudiant::getId);
    }

    @Override
    public boolean ajouterLogement(DemandeLogement d) {
        int param = d.type() == TypeLogement.COLOCATION ? d.places() : d.surfaceBalcon();
        return gestion.ajouterLogement(versNotreType(d.type()), d.numero(), d.surface(), d.etage(), d.meuble(), param) != null;
    }

    @Override
    public Optional<EtatLogement> logement(String numero) {
        Logement l = residence.trouver(numero);
        if (l == null) {
            return Optional.empty();
        }
        int occupants;
        if (l instanceof Colocation) {
            occupants = l.getColocataires().size();
        } else {
            occupants = l.estLibre() ? 0 : 1;
        }
        return Optional.of(new EtatLogement(l.getNumero(), versTypeDuContrat(l.getType()), l.getNombrePlaces(), occupants, l.estAccessiblePMR()));
    }

    @Override
    public OptionalDouble devisLoyer(DemandeDevis d) {
        Logement l = residence.trouver(d.numeroLogement());
        if (l == null) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(calculateur.calculerLoyer(l, versNotreProfil(d.profil()), d.echelonBourse(), versNosOptions(d.options())));
    }

    @Override
    public Optional<String> attribuer(DemandeAttribution d) {
        Etudiant e = db.trouverEtudiantParId(d.refEtudiant());
        if (e == null) {
            return Optional.empty();
        }
        Bail b = gestion.attribuerLogement(e, versNotreType(d.type()), versNosOptions(d.options()), d.dateSignature());
        return Optional.ofNullable(b).map(Bail::getId);
    }

    @Override
    public Optional<EtatBail> consulter(String idBail) {
        Bail b = db.trouverBail(idBail);
        if (b == null) {
            return Optional.empty();
        }
        return Optional.of(new EtatBail(b.getId(), b.getLogement().getNumero(), versStatutDuContrat(b.getStatut()),
                b.getLoyerHorsCharges(), b.getCharges(), b.getLoyerChargesComprises(), b.getCaution(),
                b.getFraisDossier(), b.getDateFin(), b.soldeDu, b.cautionRestituee));
    }

    @Override
    public boolean entrer(String idBail, LocalDate date) {
        return gestion.entrerDansLesLieux(idBail, date);
    }

    @Override
    public boolean payer(String idBail, YearMonth mois, LocalDate date, double montant) {
        return gestion.payerLoyer(idBail, mois.toString(), date, montant);
    }

    @Override
    public boolean donnerPreavis(String idBail, LocalDate date) {
        return gestion.donnerPreavis(idBail, date);
    }

    @Override
    public boolean sortir(String idBail, LocalDate date, double retenuesDegats) {
        return gestion.sortir(idBail, date, retenuesDegats);
    }

    @Override
    public boolean annuler(String idBail) {
        return gestion.annulerBail(idBail);
    }

    @Override
    public Optional<String> quittance(String idBail, YearMonth mois) {
        return Optional.ofNullable(db.trouverBail(idBail)).map(b -> gestion.genererQuittance(b, mois.toString()));
    }

    @Override
    public void relancerImpayes() {
        gestion.relancerImpayes();
    }

    @Override
    public String rapport() {
        return gestion.genererRapport();
    }

    @Override
    public List<Notification> notifications() {
        List<Notification> notifications = new ArrayList<>();
        for (String[] email : canaux.getEmail().getJournal()) {
            notifications.add(new Notification(Canal.EMAIL, email[0], email[1]));
        }
        for (String[] sms : canaux.getSms().getJournal()) {
            notifications.add(new Notification(Canal.SMS, sms[0], sms[1]));
        }
        for (String[] courrier : canaux.getCourrier().getJournal()) {
            notifications.add(new Notification(Canal.COURRIER, courrier[0], courrier[1]));
        }
        return notifications;
    }

    private static fr.efrei.residence.logement.TypeLogement versNotreType(TypeLogement type) {
        return fr.efrei.residence.logement.TypeLogement.valueOf(type.name());
    }

    private static TypeLogement versTypeDuContrat(fr.efrei.residence.logement.TypeLogement type) {
        return TypeLogement.valueOf(type.name());
    }

    private static Profil versNotreProfil(fr.efrei.residence.contrat.Profil profil) {
        return Profil.valueOf(profil.name());
    }

    private static fr.efrei.residence.notification.Canal versNotreCanal(Canal canal) {
        return fr.efrei.residence.notification.Canal.valueOf(canal.name());
    }

    private static StatutBail versStatutDuContrat(fr.efrei.residence.bail.StatutBail statut) {
        return StatutBail.valueOf(statut.name());
    }

    private static Set<fr.efrei.residence.loyer.Option> versNosOptions(Set<Option> options) {
        Set<fr.efrei.residence.loyer.Option> nosOptions = EnumSet.noneOf(fr.efrei.residence.loyer.Option.class);
        for (Option option : options) {
            nosOptions.add(fr.efrei.residence.loyer.Option.valueOf(option.name()));
        }
        return nosOptions;
    }
}