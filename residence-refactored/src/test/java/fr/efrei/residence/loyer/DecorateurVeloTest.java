package fr.efrei.residence.loyer;

import fr.efrei.residence.etudiant.Profil;
import fr.efrei.residence.logement.FabriqueLogement;
import fr.efrei.residence.logement.Logement;
import fr.efrei.residence.logement.TypeLogement;
import fr.efrei.residence.loyer.decorateur.DecorateurVelo;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecorateurVeloTest {

    private static final double PRECISION = 0.001;

    @Test
    void local_velo_ajoute_6_euros() {
        Loyer cent = () -> 100.0;
        assertEquals(106, new DecorateurVelo(cent).montant(), PRECISION);
    }

    @Test
    void local_velo_apres_menage_et_assurance_avant_la_remise() {

        Logement studio = FabriqueLogement.creer(TypeLogement.STUDIO, "X01", 20, 1, false, 0);
        double loyer = new CalculateurLoyer().calculerLoyer(studio, Profil.ALTERNANT, 0,
                EnumSet.of(Option.MENAGE, Option.ASSURANCE, Option.VELO));
        assertEquals(353.70, loyer, PRECISION);
    }
}