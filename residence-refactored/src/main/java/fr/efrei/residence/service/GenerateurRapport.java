package fr.efrei.residence.service;

import fr.efrei.residence.bail.Bail;
import fr.efrei.residence.bail.StatutBail;
import fr.efrei.residence.logement.Logement;
import fr.efrei.residence.logement.Residence;
import fr.efrei.residence.logement.TypeLogement;
import fr.efrei.residence.notification.FabriqueCanal;
import fr.efrei.residence.persistance.BailRepository;
import fr.efrei.residence.util.Utils;

/**
 * Construit le rapport d'activité de la résidence.
 */
public class GenerateurRapport {

    private final Residence residence;
    private final BailRepository baux;
    private final FabriqueCanal canaux;

    public GenerateurRapport(Residence residence, BailRepository baux, FabriqueCanal canaux) {
        this.residence = residence;
        this.baux = baux;
        this.canaux = canaux;
    }

    public String genererRapport() {
        String r = "";
        r += "=============== RAPPORT " + Residence.NOM + " ===============" + "\n";
        int total = 0;
        int occupes = 0;

        for (TypeLogement type : TypeLogement.values()) {
            int places = 0;
            int occupants = 0;
            for (Logement l : residence.getLogements()) {
                if (l.getType() != type) continue;
                places += l.getNombrePlaces();
                occupants += l.getNombreOccupants();
            }
            if (places == 0) continue;
            r += String.format("%-13s", type.getLibelle()) + ": " + occupants + "/" + places + type.getUnite() + "\n";
            total += places;
            occupes += occupants;
        }
        r += "Occupation   : " + Math.round(occupes * 100.0 / total) + " %" + "\n";

        double surfaceBalcons = 0;

        for (Logement l : residence.getLogements()) {
            surfaceBalcons += l.getSurfaceBalcon();
        }
        
        r += "Balcons      : " + surfaceBalcons + " m²" + "\n";

        double loyersMensuels = 0;
        double impayes = 0;
        double cautions = 0;

        for (Bail b : baux.tousLesBaux()) {
            if (b.getStatut() == StatutBail.ACTIF || b.getStatut() == StatutBail.PREAVIS) {
                loyersMensuels += b.getLoyerChargesComprises();
                cautions += b.getCaution();
            }
            if (b.soldeDu > 0) impayes += b.soldeDu;
        }
        r += "Loyers CC    : " + Utils.euros(Utils.arrondir(loyersMensuels)) + " / mois" + "\n";
        r += "Cautions     : " + Utils.euros(Utils.arrondir(cautions)) + "\n";
        r += "Impayés      : " + Utils.euros(Utils.arrondir(impayes)) + "\n";
        r += "SMS envoyés  : " + canaux.getSms().getCompteurSms() + "\n";
        r += "Timbres      : " + Utils.euros(Utils.arrondir(canaux.getCourrier().getTotalAffranchissement())) + "\n";
        r += "============================================================";
        return r;
    }
}