package fr.efrei.residence.service;

import java.time.LocalDate;
import java.util.Set;
import fr.efrei.residence.bail.Bail;
import fr.efrei.residence.etudiant.Etudiant;
import fr.efrei.residence.logement.Colocation;
import fr.efrei.residence.logement.Logement;
import fr.efrei.residence.logement.Residence;
import fr.efrei.residence.logement.TypeLogement;
import fr.efrei.residence.loyer.CalculateurLoyer;
import fr.efrei.residence.loyer.Option;
import fr.efrei.residence.notification.Notificateur;
import fr.efrei.residence.persistance.BailRepository;
import fr.efrei.residence.profil.FabriqueStrategieProfil;
import fr.efrei.residence.profil.StrategieProfil;
import fr.efrei.residence.util.Utils;

/**
 * Attribue à un étudiant le premier logement libre qu'il peut payer.
 */
public class ServiceAttribution {

    private final Residence residence;
    private final CalculateurLoyer calculateur;
    private final BailRepository baux;
    private final Notificateur notificateur;
    private int compteurBail = 0;

    public ServiceAttribution(Residence residence, CalculateurLoyer calculateur, BailRepository baux,
                              Notificateur notificateur) {
        this.residence = residence;
        this.calculateur = calculateur;
        this.baux = baux;
        this.notificateur = notificateur;
    }

    public Bail attribuerLogement(Etudiant e, TypeLogement typeSouhaite, Set<Option> options, LocalDate dateSignature) {
        System.out.println("--- Attribution d'un " + typeSouhaite + " à " + e.getNomComplet());
        StrategieProfil regles = FabriqueStrategieProfil.creer(e.getProfil());
        Logement choisi = null;
        double loyer = 0;
        double charges = 0;

        for (Logement l : residence.getLogements()) {
            if (l.getType() != typeSouhaite) continue;
            if (!l.estLibre()) continue;
            if (e.isPmr() && !l.estAccessiblePMR()) continue;

            double loyerCandidat = calculateur.calculerLoyer(l, e.getProfil(), e.getEchelonBourse(), options);
            double chargesCandidat = calculateur.calculerCharges(l);
            double total = loyerCandidat + chargesCandidat;

            if (!regles.estSolvable(e.getRessourcesMensuelles(), total)) {
                System.out.println("  " + l.getNumero() + " : ressources insuffisantes (" + Utils.euros(total) + "/mois)");
                continue;
            }
            choisi = l;
            loyer = loyerCandidat;
            charges = chargesCandidat;
            break;
        }
        if (choisi == null) {
            System.out.println("  Aucun logement disponible pour " + e.getNomComplet());
            return null;
        }

        if (choisi instanceof Colocation) {
            choisi.ajouterColocataire(e);
        } else {
            choisi.loger(e);
        }

        compteurBail++;
        String id = "BAIL-" + String.format("%03d", compteurBail);
        Bail b = new Bail(id, e, choisi, dateSignature);

        b.setOptions(options);
        b.setLoyerHorsCharges(loyer);
        b.setCharges(charges);

        b.setCaution(Utils.arrondir(regles.caution(loyer)));
        b.setFraisDossier(regles.fraisDossier());

        baux.sauvegarderBail(b);
        System.out.println("  Bail " + id + " signé : " + choisi.getNumero() + ", " + Utils.euros(b.getLoyerChargesComprises())
                + " CC/mois, caution " + Utils.euros(b.getCaution()) + ", frais de dossier " + Utils.euros(b.getFraisDossier()));
        notificateur.notifier(e, "Votre bail " + id,
                "Bienvenue à la " + Residence.NOM + " ! Logement " + choisi.getNumero() + ", loyer " + Utils.euros(b.getLoyerChargesComprises()) + " charges comprises.");
        return b;
    }
}