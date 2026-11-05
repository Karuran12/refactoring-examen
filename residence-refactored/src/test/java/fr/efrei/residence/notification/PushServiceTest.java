package fr.efrei.residence.notification;

import fr.efrei.residence.etudiant.Etudiant;
import fr.efrei.residence.etudiant.Profil;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PushServiceTest {

    private final Etudiant alice = new Etudiant("ETU-1", "Martin", "Alice", "alice.martin@mail.fr", null, null,
            Profil.STANDARD, 0, 1500, false, Canal.PUSH);

    @Test
    void affiche_push_email_et_sujet() {
        PrintStream console = System.out;
        ByteArrayOutputStream sortie = new ByteArrayOutputStream();
        System.setOut(new PrintStream(sortie, true, StandardCharsets.UTF_8));
        try {
            new PushService().notifier(alice, "Votre bail BAIL-001", "Bienvenue !");
        } finally {
            System.setOut(console);
        }
        assertEquals("[PUSH] -> alice.martin@mail.fr : Votre bail BAIL-001", sortie.toString(StandardCharsets.UTF_8).trim());
    }

    @Test
    void la_fabrique_donne_le_service_push() {
        FabriqueCanal canaux = new FabriqueCanal();
        canaux.pour(Canal.PUSH).notifier(alice, "Quittance 2026-09", "Merci");
        assertEquals(1, canaux.getPush().getJournal().size());
        assertEquals("Quittance 2026-09", canaux.getPush().getJournal().get(0)[1]);
    }

    @Test
    void la_relance_donne_le_montant_et_le_bail() {
        PushService push = new PushService();
        push.relancer(alice, 40.01, "BAIL-004");
        assertEquals("Loyer impayé : 40,01 € sur BAIL-004", push.getJournal().get(0)[1]);
    }
}