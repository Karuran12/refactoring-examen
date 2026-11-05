package fr.efrei.residence.logement;

import java.util.ArrayList;
import java.util.List;
import fr.efrei.residence.etudiant.Etudiant;

public class Colocation extends Logement {

    private int places;
    private List<Etudiant> colocataires = new ArrayList<Etudiant>();

    public Colocation(String numero, double surface, int etage, boolean meuble, int places) {
        super(numero, TypeLogement.COLOCATION, surface, etage, meuble);
        this.places = places;
    }

    @Override
    public int getNombrePlaces() {
        return places;
    }

    @Override
    public int getNombreOccupants() {
        return colocataires.size();
    }

    @Override
    public boolean estLibre() {
        return colocataires.size() < places;
    }

    @Override
    public void loger(Etudiant e) {
        if (colocataires.size() >= places) {
            throw new IllegalStateException("Colocation " + numero + " complète");
        }
        colocataires.add(e);
    }

    @Override
    public void liberer(Etudiant e) {
        colocataires.remove(e);
    }

    @Override
    public void libererApresSortie(Etudiant e) {
        liberer(e);
        System.out.println("[ENTRETIEN] Nettoyage des parties communes de la colocation " + numero);
    }
}