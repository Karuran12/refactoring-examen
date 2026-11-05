package fr.efrei.residence.profil;

import fr.efrei.residence.etudiant.Profil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProfilSportifTest {

    private static final double PRECISION = 0.001;

    private final StrategieProfil sportif = FabriqueStrategieProfil.creer(Profil.SPORTIF);

    @Test
    void remise_de_15_pourcent() {
        assertEquals(85, sportif.appliquerRemise(100, 0), PRECISION);
    }

    @Test
    void solvable_avec_2_fois_le_loyer_charges_comprises() {
        assertTrue(sportif.estSolvable(1000, 500));
        assertFalse(sportif.estSolvable(999, 500));
    }

    @Test
    void depot_de_garantie_d_un_mois_de_loyer_hors_charges() {
        assertEquals(400, sportif.caution(400), PRECISION);
    }

    @Test
    void frais_de_dossier_de_50_euros() {
        assertEquals(50, sportif.fraisDossier(), PRECISION);
    }

    @Test
    void preavis_de_15_jours() {
        assertEquals(15, sportif.joursPreavis());
    }

    @Test
    void penalites_de_retard_normales() {
        assertEquals(25, sportif.penaliteDeRetard(500), PRECISION);
        assertEquals(10, sportif.penaliteDeRetard(100), PRECISION);
    }
}