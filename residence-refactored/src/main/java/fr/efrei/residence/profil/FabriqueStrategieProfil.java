package fr.efrei.residence.profil;

import fr.efrei.residence.etudiant.Profil;

/**
 * Seul endroit qui choisit la stratégie selon le profil.
 */
public class FabriqueStrategieProfil {

    public static StrategieProfil creer(Profil profil) {
        return switch (profil) {
            case STANDARD -> new ProfilStandard();
            case BOURSIER -> new ProfilBoursier();
            case ALTERNANT -> new ProfilAlternant();
            case INTERNATIONAL -> new ProfilInternational();
            case SPORTIF -> new ProfilSportif();
        };
    }
}