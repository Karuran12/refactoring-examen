package fr.efrei.residence.logement;

import fr.efrei.residence.etudiant.Etudiant;
import fr.efrei.residence.etudiant.Profil;
import fr.efrei.residence.loyer.CalculateurLoyer;
import fr.efrei.residence.notification.Canal;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DuplexTest {

    private static final double PRECISION = 0.001;

    private final Logement duplex = FabriqueLogement.creer(TypeLogement.DUPLEX, "D101", 60, 1, false, 0);

    private Etudiant etudiant(String id) {
        return new Etudiant(id, "Martin", "Alice", id + "@mail.fr", "0601020304", null,
                Profil.STANDARD, 0, 3000, false, Canal.EMAIL);
    }

    @Test
    void le_code_duplex_est_reconnu() {
        assertEquals(TypeLogement.DUPLEX, TypeLogement.depuisCode("DUPLEX"));
        assertEquals(TypeLogement.DUPLEX, duplex.getType());
    }

    @Test
    void loyer_de_12_euros_50_le_m2_plus_80_euros() {

        assertEquals(830, new CalculateurLoyer().calculerLoyer(duplex, Profil.STANDARD, 0, Set.of()), PRECISION);
    }

    @Test
    void un_seul_occupant() {
        assertEquals(1, duplex.getNombrePlaces());
        duplex.loger(etudiant("ETU-1"));
        assertFalse(duplex.estLibre());
        assertThrows(IllegalStateException.class, () -> duplex.loger(etudiant("ETU-2")));
    }

    @Test
    void pas_de_balcon() {
        assertEquals(0, duplex.getSurfaceBalcon(), PRECISION);
        
    }
}