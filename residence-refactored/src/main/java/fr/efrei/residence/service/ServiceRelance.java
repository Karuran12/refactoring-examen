package fr.efrei.residence.service;

import fr.efrei.residence.bail.Bail;
import fr.efrei.residence.bail.StatutBail;
import fr.efrei.residence.notification.Notificateur;
import fr.efrei.residence.persistance.BailRepository;

/**
 * Relance les étudiants qui ont encore un loyer à payer.
 */
public class ServiceRelance {

    private final BailRepository baux;
    private final Notificateur notificateur;

    public ServiceRelance(BailRepository baux, Notificateur notificateur) {
        this.baux = baux;
        this.notificateur = notificateur;
    }

    public void relancerImpayes() {
        System.out.println("--- Relance des impayés");
        for (Bail b : baux.tousLesBaux()) {
            if (b.soldeDu <= 0) continue;
            if (b.getStatut() == StatutBail.ANNULE) continue;
            notificateur.relancer(b.getEtudiant(), b.soldeDu, b.getId());
        }
    }
}