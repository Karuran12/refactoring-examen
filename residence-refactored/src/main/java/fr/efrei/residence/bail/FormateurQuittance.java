package fr.efrei.residence.bail;

import java.util.Set;
import fr.efrei.residence.etudiant.Etudiant;
import fr.efrei.residence.logement.Logement;
import fr.efrei.residence.loyer.Option;
import fr.efrei.residence.util.Utils;

/**
 * Met en forme la quittance d'un mois payé.
 */
public class FormateurQuittance {

    public String genererQuittance(Bail b, String mois) {
        Paiement p = null;
        for (Paiement x : b.paiements) {
            if (x.mois.equals(mois)) p = x;
        }
        if (p == null) {
            return "Aucun paiement pour " + mois;
        }
        Etudiant etudiant = b.getEtudiant();
        Logement logement = b.getLogement();
        String q = "";
        q += "==========================================\n";
        q += "   RÉSIDENCE CAMPUS VERT - QUITTANCE\n";
        q += "==========================================\n";
        q += "Bail      : " + b.getId() + "\n";
        q += "Locataire : " + etudiant.getNomComplet() + " (" + etudiant.getProfil() + ")\n";
        q += "Logement  : " + logement.getNumero() + " - " + logement.getType() + " " + logement.getSurface() + " m²";
        if (logement.isMeuble()) q += " meublé";
        q += "\n";
        q += "Période   : " + mois + "\n";
        q += "Options   : " + listerOptions(b.getOptions()) + "\n";
        q += "------------------------------------------\n";
        q += "Loyer HC  : " + Utils.euros(b.getLoyerHorsCharges()) + "\n";
        q += "Charges   : " + Utils.euros(b.getCharges()) + "\n";
        if (p.penalite > 0) {
            q += "Pénalité  : " + Utils.euros(p.penalite) + "\n";
        }
        q += "Total dû  : " + Utils.euros(p.du) + "\n";
        q += "Payé le   : " + Utils.date(p.date) + " : " + Utils.euros(p.montant) + "\n";
        if (p.montant < p.du) {
            q += "RESTE DÛ  : " + Utils.euros(Utils.arrondir(p.du - p.montant)) + "\n";
        }
        q += "==========================================";
        return q;
    }

    private String listerOptions(Set<Option> options) {
        String s = "";
        for (Option option : Option.values()) {
            if (options.contains(option)) s = s + option.getLibelle() + ", ";
        }
        if (s.length() == 0) return "aucune";
        return s.substring(0, s.length() - 2);
    }
}