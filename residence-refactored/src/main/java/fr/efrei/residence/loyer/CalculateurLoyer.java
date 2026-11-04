package fr.efrei.residence.loyer;

import fr.efrei.residence.etudiant.Profil;
import fr.efrei.residence.logement.Logement;
import fr.efrei.residence.profil.FabriqueStrategieProfil;
import fr.efrei.residence.util.Utils;

public class CalculateurLoyer {

    
        private static final double COEF_MEUBLE = 1.15;
        private static final double COEF_REZ_DE_CHAUSSEE = 0.95;
    
        private static final double PRIX_PARKING = 45;
        private static final double PRIX_LAVERIE = 12;
        private static final double PRIX_FIBRE = 20;
        private static final double PRIX_ASSURANCE = 9;
    

    /**
     * Loyer mensuel hors charges, options comprises, remise déduite.
     */
    public double calculerLoyer(Logement l, Profil profil, int echelon, boolean parking, boolean menage,
                                boolean laverie, boolean fibre, boolean assurance) {
        // loyer de base selon le type
        StrategieTarif tarif = FabriqueStrategieTarif.creer(l.getType());
        double loyer = tarif.loyerDeBase(l);

        if (l.isMeuble()) loyer = loyer * COEF_MEUBLE;
        if (l.getEtage() == Logement.REZ_DE_CHAUSSEE) loyer = loyer * COEF_REZ_DE_CHAUSSEE;

        // options
        if (menage) loyer = loyer * tarif.coefMenage();
        if (parking) loyer = loyer + PRIX_PARKING;
        if (laverie) loyer = loyer + PRIX_LAVERIE;
        if (fibre && !tarif.fibreIncluse()) loyer = loyer + PRIX_FIBRE;
        if (assurance) loyer = loyer + PRIX_ASSURANCE;

        // remise selon le profil
        loyer = FabriqueStrategieProfil.creer(profil).appliquerRemise(loyer, echelon);

        return Utils.arrondir(loyer);
    }

    public double calculerCharges(Logement l) {
        return FabriqueStrategieTarif.creer(l.getType()).charges();
    }
}
