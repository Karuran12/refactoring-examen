package fr.efrei.residence.loyer;

import fr.efrei.residence.etudiant.Profil;
import fr.efrei.residence.logement.Logement;
import fr.efrei.residence.loyer.decorateur.*;
import fr.efrei.residence.loyer.tarif.FabriqueStrategieTarif;
import fr.efrei.residence.loyer.tarif.StrategieTarif;
import fr.efrei.residence.profil.FabriqueStrategieProfil;
import fr.efrei.residence.util.Utils;
import java.util.Set;

public class CalculateurLoyer {

    public double calculerLoyer(Logement l, Profil profil, int echelon, Set<Option> options) {
        StrategieTarif tarif = FabriqueStrategieTarif.creer(l.getType());
        Loyer loyer = new LoyerDeBase(l, tarif);

        if (l.isMeuble()) loyer = new DecorateurMeuble(loyer);
        if (l.getEtage() == Logement.REZ_DE_CHAUSSEE) loyer = new DecorateurRezDeChaussee(loyer);

        if (options.contains(Option.MENAGE)) loyer = new DecorateurMenage(loyer, tarif.coefMenage());
        if (options.contains(Option.PARKING)) loyer = new DecorateurParking(loyer);
        if (options.contains(Option.LAVERIE)) loyer = new DecorateurLaverie(loyer);
        if (options.contains(Option.FIBRE) && !tarif.fibreIncluse()) loyer = new DecorateurFibre(loyer);
        if (options.contains(Option.ASSURANCE)) loyer = new DecorateurAssurance(loyer);
        if (options.contains(Option.VELO)) loyer = new DecorateurVelo(loyer);
        
        loyer = new DecorateurRemise(loyer, FabriqueStrategieProfil.creer(profil), echelon);

        return Utils.arrondir(loyer.montant());
    }

    public double calculerCharges(Logement l) {
        return FabriqueStrategieTarif.creer(l.getType()).charges();
    }
}
