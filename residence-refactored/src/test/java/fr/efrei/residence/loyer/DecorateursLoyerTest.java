package fr.efrei.residence.loyer;

import fr.efrei.residence.etudiant.Profil;
import fr.efrei.residence.logement.FabriqueLogement;
import fr.efrei.residence.logement.Logement;
import fr.efrei.residence.logement.TypeLogement;
import fr.efrei.residence.loyer.decorateur.DecorateurAssurance;
import fr.efrei.residence.loyer.decorateur.DecorateurFibre;
import fr.efrei.residence.loyer.decorateur.DecorateurLaverie;
import fr.efrei.residence.loyer.decorateur.DecorateurMenage;
import fr.efrei.residence.loyer.decorateur.DecorateurMeuble;
import fr.efrei.residence.loyer.decorateur.DecorateurParking;
import fr.efrei.residence.loyer.decorateur.DecorateurRemise;
import fr.efrei.residence.loyer.decorateur.DecorateurRezDeChaussee;
import fr.efrei.residence.loyer.tarif.TarifStudio;
import fr.efrei.residence.profil.ProfilAlternant;
import fr.efrei.residence.profil.ProfilBoursier;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecorateursLoyerTest {

    private static final double PRECISION = 0.001;

    private final Loyer cent = () -> 100.0;

    @Test
    void loyer_de_base_d_un_petit_studio_au_minimum() {

        Logement studio = FabriqueLogement.creer(TypeLogement.STUDIO, "X01", 20, 1, false, 0);
        assertEquals(350, new LoyerDeBase(studio, new TarifStudio()).montant(), PRECISION);
    }

    @Test
    void meuble_ajoute_15_pourcent() {
        assertEquals(115, new DecorateurMeuble(cent).montant(), PRECISION);
    
    }

    @Test
    void rez_de_chaussee_enleve_5_pourcent() {
        assertEquals(95, new DecorateurRezDeChaussee(cent).montant(), PRECISION);
    }

    @Test
    void menage_applique_le_coefficient_recu() {
        assertEquals(108, new DecorateurMenage(cent, 1.08).montant(), PRECISION);
    }

    @Test
    void parking_ajoute_45_euros() {
        assertEquals(145, new DecorateurParking(cent).montant(), PRECISION);
    }

    @Test
    void laverie_ajoute_12_euros() {
        assertEquals(112, new DecorateurLaverie(cent).montant(), PRECISION);
    }

    @Test
    void fibre_ajoute_20_euros() {
        assertEquals(120, new DecorateurFibre(cent).montant(), PRECISION);
    }

    @Test
    void assurance_ajoute_9_euros() {

        assertEquals(109, new DecorateurAssurance(cent).montant(), PRECISION);
    }

    @Test
    void remise_alternant_de_10_pourcent() {
        assertEquals(90, new DecorateurRemise(cent, new ProfilAlternant(), 0).montant(), PRECISION);
    }

    @Test
    void remise_boursier_de_5_pourcent_par_echelon() {
        assertEquals(85, new DecorateurRemise(cent, new ProfilBoursier(), 3).montant(), PRECISION);
    
    }

    @Test
    void le_menage_passe_avant_les_options_a_prix_fixe() {

        Logement studio = FabriqueLogement.creer(TypeLogement.STUDIO, "X02", 30, 1, false, 0);
        double loyer = new CalculateurLoyer().calculerLoyer(studio, Profil.STANDARD, 0,
                EnumSet.of(Option.MENAGE, Option.PARKING));
        assertEquals(498.60, loyer, PRECISION);
    }
}