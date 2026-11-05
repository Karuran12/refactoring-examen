package fr.efrei.residence;

import java.time.LocalDate;
import java.util.Set;
import fr.efrei.residence.bail.Bail;
import fr.efrei.residence.bail.FormateurQuittance;
import fr.efrei.residence.etudiant.Etudiant;
import fr.efrei.residence.etudiant.Profil;
import fr.efrei.residence.logement.Logement;
import fr.efrei.residence.logement.Residence;
import fr.efrei.residence.logement.TypeLogement;
import fr.efrei.residence.loyer.Option;
import fr.efrei.residence.notification.Canal;
import fr.efrei.residence.service.GenerateurRapport;
import fr.efrei.residence.service.ServiceAttribution;
import fr.efrei.residence.service.ServiceBail;
import fr.efrei.residence.service.ServiceInscription;
import fr.efrei.residence.service.ServiceRelance;

/**
 * Façade : transmet chaque demande au service qui s'en occupe.
 */
public class GestionResidence {

    private final Residence residence;
    private final ServiceInscription inscription;
    private final ServiceAttribution attribution;
    private final ServiceBail cycleBail;
    private final ServiceRelance relance;
    private final GenerateurRapport rapport;
    private final FormateurQuittance quittances;

    public GestionResidence(Residence residence, ServiceInscription inscription, ServiceAttribution attribution,
                            ServiceBail cycleBail, ServiceRelance relance, GenerateurRapport rapport,
                            FormateurQuittance quittances) {
        this.residence = residence;
        this.inscription = inscription;
        this.attribution = attribution;
        this.cycleBail = cycleBail;
        this.relance = relance;
        this.rapport = rapport;
        this.quittances = quittances;
    }

    public Etudiant inscrireEtudiant(String nom, String prenom, String email, String telephone, String adresse,
                                     Profil profil, int echelonBourse, double ressources, boolean pmr,
                                     Canal canalPrefere) {
        return inscription.inscrireEtudiant(nom, prenom, email, telephone, adresse, profil, echelonBourse,
                ressources, pmr, canalPrefere);
    }

    public Logement ajouterLogement(TypeLogement type, String numero, double surface, int etage, boolean meuble, int param) {
        return residence.ajouterLogement(type, numero, surface, etage, meuble, param);
    }

    public Bail attribuerLogement(Etudiant e, TypeLogement typeSouhaite, Set<Option> options, LocalDate dateSignature) {
        return attribution.attribuerLogement(e, typeSouhaite, options, dateSignature);
    }

    public boolean entrerDansLesLieux(String idBail, LocalDate date) {
        return cycleBail.entrerDansLesLieux(idBail, date);
    }

    public boolean payerLoyer(String idBail, String mois, LocalDate datePaiement, double montant) {
        return cycleBail.payerLoyer(idBail, mois, datePaiement, montant);
    }

    public boolean donnerPreavis(String idBail, LocalDate date) {
        return cycleBail.donnerPreavis(idBail, date);
    }

    public boolean sortir(String idBail, LocalDate date, double retenuesDegats) {
        return cycleBail.sortir(idBail, date, retenuesDegats);
    }

    public boolean annulerBail(String idBail) {
        return cycleBail.annulerBail(idBail);
    }

    public void relancerImpayes() {
        relance.relancerImpayes();
    }

    public String genererQuittance(Bail b, String mois) {
        return quittances.genererQuittance(b, mois);
    }

    public void afficherRapport() {
        System.out.println();
        System.out.println(rapport.genererRapport());
    }

    public String genererRapport() {
        return rapport.genererRapport();
    }
}