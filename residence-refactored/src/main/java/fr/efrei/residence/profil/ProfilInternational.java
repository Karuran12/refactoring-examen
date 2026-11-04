package fr.efrei.residence.profil;

/**
 * International : numéro international obligatoire, caution de 2 mois, frais de dossier majorés,
 * et un email en anglais en plus des messages habituels.
 */
public class ProfilInternational extends StrategieProfil {

    private static final int MOIS_CAUTION_INTERNATIONAL = 2;
    private static final double FRAIS_DOSSIER_INTERNATIONAL = 150;

    @Override
    public String verifierInscription(int echelonBourse, String telephone) {
        if (telephone == null || !telephone.startsWith("+")) {
            return "numéro international (+XX) obligatoire";
        }
        return null;
    }

    @Override
    public double caution(double loyerHorsCharges) {
        return loyerHorsCharges * MOIS_CAUTION_INTERNATIONAL;
    }

    @Override
    public double fraisDossier() {
        return FRAIS_DOSSIER_INTERNATIONAL;
    }

    @Override
    public boolean recoitEmailEnAnglais() {
        return true;
    }
}