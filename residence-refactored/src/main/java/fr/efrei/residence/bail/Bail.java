package fr.efrei.residence.bail;

import fr.efrei.residence.etudiant.Etudiant;
import fr.efrei.residence.logement.Logement;
import fr.efrei.residence.loyer.Option;
import fr.efrei.residence.util.Utils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Bail {

    private String id;
    private Etudiant etudiant;
    private Logement logement;
    private LocalDate dateSignature;
    private LocalDate dateEntree;
    private LocalDate dateFin;
    private StatutBail statut; // SIGNE, ACTIF, PREAVIS, TERMINE, ANNULE

    private double loyerHorsCharges;
    private double charges;
    private double caution;
    private double fraisDossier;

    private Set<Option> options = Set.of();

    public List<Paiement> paiements = new ArrayList<Paiement>();
    public double soldeDu = 0;
    public double retenues = 0;
    public double cautionRestituee = 0;

    public Bail(String id, Etudiant etudiant, Logement logement, LocalDate dateSignature) {
        this.id = id;
        this.etudiant = etudiant;
        this.logement = logement;
        this.dateSignature = dateSignature;
        this.statut = StatutBail.SIGNE;
    }

    public String getId() { return id; }
    public Etudiant getEtudiant() { return etudiant; }
    public Logement getLogement() { return logement; }
    public LocalDate getDateSignature() { return dateSignature; }
    public LocalDate getDateEntree() { return dateEntree; }
    public void setDateEntree(LocalDate d) { this.dateEntree = d; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate d) { this.dateFin = d; }
    public StatutBail getStatut() { return statut; }
    public void setStatut(StatutBail statut) { this.statut = statut; }
    public double getLoyerHorsCharges() { return loyerHorsCharges; }
    public void setLoyerHorsCharges(double l) { this.loyerHorsCharges = l; }
    public double getCharges() { return charges; }
    public void setCharges(double c) { this.charges = c; }
    public double getLoyerChargesComprises() { return Utils.arrondir(loyerHorsCharges + charges); }
    public double getCaution() { return caution; }
    public void setCaution(double c) { this.caution = c; }
    public double getFraisDossier() { return fraisDossier; }
    public void setFraisDossier(double f) { this.fraisDossier = f; }
    public Set<Option> getOptions() { return options; }
    public void setOptions(Set<Option> options) { this.options = Set.copyOf(options); }

}
