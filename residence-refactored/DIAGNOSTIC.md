# Palier 1 – Diagnostic

## 1) Tableau défaut -> emplacement -> principe violé

| Principe violé                     | Exemples dans le legacy                                                                                                                                                                                                                                                                                                                   | Palier  |
|------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| **S** – Responsabilité unique      | `GestionResidence` valide les inscriptions, ajoute les logements, attribue, gère le cycle de vie du bail, relance, produit le rapport et envoie les notifications · `Bail.genererQuittance()`: une entité métier qui fait de la mise en forme · `CalculateurLoyer.calculerLoyer()` mélange loyer de base, majorations, options et remise | 3, 4, 5 |
| **O** – Ouvert/fermé               | `CalculateurLoyer`: `if/else` sur le type de logement, sur le profil et un `if` par option · `GestionResidence`: `if/else` sur le profil pour la solvabilité, la caution, les frais de dossier, le préavis et la pénalité · `notifier()` et `relancerImpayes()`: un bloc par canal · ajouter DUPLEX = modifier 3 fichiers existants   | 2, 3, 4 |
| **L** – Substitution de Liskov     | `Colocation.loger()` et `liberer()` lèvent une exception, d'où les 3 `instanceof Colocation` dans `GestionResidence` · `Studio.getSurfaceBalcon()` et `Colocation.getSurfaceBalcon()` lèvent une exception, cachée par un `catch` vide dans `genererRapport()` · `T1.getColocataires()` renvoie `null`                                    | 6       |
| **I** – Ségrégation des interfaces | `ILogement` impose 16 méthodes, dont celles de la colocation, du balcon et de l'entretien: `Studio.getColocataires()`, `T2.nettoyerPartiesCommunes()`… lèvent `UnsupportedOperationException`                                                                                                                                            | 6       |
| **D** – Inversion des dépendances  | `Database.getInstance()` (singleton) · `EmailService.envoyer()` statique, `new SmsService()` et `new CourrierService()` en dur · `new CalculateurLoyer()` dans `GestionResidence` · `Residence.logements` statique                                                                                                                        | 5       |

Hors SOLID, à relever aussi: nombres magiques (`CalculateurLoyer`, `GestionResidence`, `SmsService`), codes en
`String` (type de logement, profil, canal, statut), code dupliqué (création des logements, choix du service de
notification, arrondi), état global en `static` (compteurs, journaux des services), champs publics modifiables
(`Bail`, `Paiement`), commentaire faux dans `notifier()` (" les internationaux reçoivent toujours un email en
plus "), code mort (`EmailService.SMTP_HOST`, `Etudiant.setProfil()`, `Etudiant.setCanalPrefere()`,
`ILogement.getOccupant()`, `Bail.getDateEntree()`).

## 2) Endroits à modifier pour ajouter le type DUPLEX (dans le legacy)

| # | Fichier                                        | Modification                                                                                                  |
|---|------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| 1 | `CalculateurLoyer` -> l.12-23                    | nouvelle branche `else if (l.getType().equals("DUPLEX"))`: 12,50 €/m² + 80 €                                 |
| 2 | `Residence.initialiser()` -> l.39-55             | nouveau `case "DUPLEX"`, pour pouvoir en mettre dans le catalogue                                             |
| 3 | `GestionResidence.ajouterLogement()` -> l.70-79  | nouvelle branche `type.equals("DUPLEX")`                                                                      |
| 4 | `GestionResidence.genererRapport()` -> l.350-379 | deux nouveaux compteurs, une nouvelle ligne et les ajouter aux totaux du taux d'occupation                   |
| 5 | **nouveau** `Duplex.java`                      | implémenter les 6 méthodes de colocation, de balcon et d'entretien, dont 5 en `UnsupportedOperationException` |

**Bilan:** 3 fichiers existants modifiés, à 4 endroits différents, plus une nouvelle classe qui reproduit les
défauts L et I. Oublier l'endroit n°1 ne provoque aucune erreur de compilation: le calcul du loyer lève une
exception à l'exécution. Oublier le n°4 ne provoque aucune erreur du tout: le taux d'occupation devient faux.

## 3) Endroits à modifier pour ajouter le profil SPORTIF (dans le legacy)

| # | Fichier                                                   | Modification                                                                                                 |
|---|-----------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| 1 | `GestionResidence.inscrireEtudiant()` -> l.34-35            | ajouter `SPORTIF` à la liste des profils acceptés                                                            |
| 2 | `CalculateurLoyer` -> l.42-48                               | nouvelle branche: remise de 15 %                                                                            |
| 3 | `GestionResidence.attribuerLogement()` -> l.109-115         | solvabilité à 2 × le loyer CC, comme l'alternant                                                             |
| 4 | `GestionResidence.attribuerLogement()` -> l.159-165         | nouvelle branche: frais de dossier de 50 €                                                                  |
| 5 | `GestionResidence.donnerPreavis()` -> l.238                 | préavis de 15 jours, comme le boursier et l'alternant                                                        |
| 6 | `attribuerLogement()` -> l.149-155 et `payerLoyer()` -> l.207 | caution d'un mois et pénalité normale: rien à changer, ce sont les cas par défaut, mais il faut le vérifier |

**Bilan:** 2 fichiers existants modifiés, à 5 endroits différents, plus 2 endroits à relire. Le compilateur ne
signale aucun oubli: un profil absent d'un `if` tombe dans le cas par défaut et reçoit le mauvais montant, sans
aucune erreur.

## 4) Endroits à modifier pour ajouter l'option " local vélo " (dans le legacy)

| # | Fichier                                                           | Modification                                                                    |
|---|-------------------------------------------------------------------|---------------------------------------------------------------------------------|
| 1 | `CalculateurLoyer` -> l.8-9                                         | nouveau paramètre `boolean velo`: la méthode passe à 9 paramètres              |
| 2 | `CalculateurLoyer` -> l.39                                          | ajouter `if (velo) loyer = loyer + 6;` juste après l'assurance, avant la remise |
| 3 | `GestionResidence.attribuerLogement()` -> l.91-92, 102-103, 139-143 | nouveau paramètre, à transmettre au calcul et à recopier dans le bail           |
| 4 | `Bail` -> l.22-26 et 61-70                                          | nouveau champ public `velo`, à ajouter dans `listerOptions()` pour la quittance |
| 5 | `Main` -> l.38-44                                                   | ajouter un argument aux 7 appels de `attribuerLogement()`                       |

**Bilan:** 4 fichiers existants modifiés, 5 avec `LegacyAdapter`. Ici le compilateur signale les appels à
compléter, mais chaque option rallonge des signatures déjà longues et il faut connaître l'ordre exact des calculs
pour placer la ligne. C'est l'argument pour le Decorator: après le palier 4, une option = une nouvelle classe plus
une ligne dans l'assemblage.

## 5) Endroits à modifier pour ajouter le canal PUSH (dans le legacy)

| # | Fichier                                         | Modification                                                                         |
|---|-------------------------------------------------|--------------------------------------------------------------------------------------|
| 1 | `GestionResidence.notifier()` -> l.407-415        | nouvelle branche `canal.equals("PUSH")`                                              |
| 2 | `GestionResidence.relancerImpayes()` -> l.323-338 | nouveau `case "PUSH"`, sinon la relance affiche seulement " Canal inconnu PUSH "     |
| 3 | `GestionResidence.notifier()` -> l.417            | relire la règle `[EN]`: un international en PUSH recevrait aussi l'email en anglais |
| 4 | **nouveau** `PushService.java`                  | afficher `[PUSH] -> email: sujet`                                                   |

**Bilan:** 1 fichier existant modifié, à 2 endroits, plus 1 endroit à relire et une nouvelle classe. Rien n'oblige
à le faire: `inscrireEtudiant()` ne vérifie pas le nom du canal. Un canal inconnu ou mal écrit (`"EMIAL"`) est
accepté et l'étudiant ne reçoit ensuite aucune notification, sans aucune erreur.

## 6) Points à faire ressortir
- La plupart des défauts O, L et I se concentrent dans deux classes, `CalculateurLoyer` et `GestionResidence` et
  dans la hiérarchie des logements. Ce sont les cibles des paliers 2 à 6.
- Les défauts D expliquent pourquoi `LegacyAdapter` doit vider `Database`, les journaux des trois services,
  `SmsService.compteurSms`, `CourrierService.totalAffranchissement`, `Etudiant.compteur` et
  `GestionResidence.compteurBail`, puis recharger `Residence` avant chaque test.
- L'arrondi est une factorisation " ratée ": `Utils.arrondir()` existe, mais `CalculateurLoyer` refait le calcul
  à la main (`Math.round(loyer * 100.0) / 100.0`).
- Les codes en `String` ne sont jamais vérifiés par le compilateur: `"BOURSIR"` ou `"EMIAL"` compilent sans
  erreur. C'est l'objet de la suite du palier 1.
- Deux comportements ne sont couverts par aucun test: un international ne reçoit pas l'email `[EN]` lors d'une
  relance (seul `Main` le montre) et un étudiant SMS avec un numéro trop court est inscrit mais ne reçoit aucun
  SMS. Le refactoring doit les conserver, mais c'est une bonne question sur ce que couvrent les tests.


## 7) Palier 7 – Preuve par l'extension

| Extension        | Nouvelles classes        | Fichiers existants modifiés                                                                                        |
|------------------|--------------------------|--------------------------------------------------------------------------------------------------------------------|
| Type `DUPLEX`    | `Duplex`, `TarifDuplex`  | `TypeLogement` (+1 valeur), `FabriqueLogement` (+1 `case`), `FabriqueStrategieTarif` (+1 `case`), `GenerateurRapport` (+1 ligne) |
| Option vélo      | `DecorateurVelo`         | `Option` (+1 valeur), `CalculateurLoyer` (+1 ligne dans l'assemblage)                                              |
| Profil `SPORTIF` | `ProfilSportif`          | `Profil` (+1 valeur), `FabriqueStrategieProfil` (+1 `case`)                                                        |
| Canal `PUSH`     | `PushService`            | `Canal` (+1 valeur), `FabriqueCanal` (+1 champ, +1 `case`, +1 getter)                                              |

Tests unitaires ajoutés: `DuplexTest`, `DecorateurVeloTest`, `ProfilSportifTest` et `PushServiceTest` (15 tests).
Les 101 tests de contrat restent verts.

**Bilan: 10 fichiers existants modifiés**, tous des points d'extension: 4 enums (une valeur chacune), 4 fabriques
(un `case` chacune), l'assemblage du loyer (une ligne) et le rapport (une ligne). Aucun service, aucun décorateur
et aucune stratégie existante n'a été modifié et aucune signature n'a changé. Dans le legacy (sections 2 à 5), il
fallait aussi 10 modifications de fichiers, mais au cœur de `GestionResidence` et de `CalculateurLoyer`, avec des
signatures rallongées et des oublis silencieux. Ici, un `case` oublié dans une fabrique ne compile pas: le `switch`
sur l'enum doit couvrir toutes les valeurs.

La seule modification hors assemblage est dans `GenerateurRapport`: il affichait une ligne pour chaque valeur de
`TypeLogement`, même absente du parc. Une ligne "Duplex: 0/0" aurait cassé le rapport du contrat. Il n'affiche
plus que les types présents, donc le prochain type de logement n'y touchera plus.

`Main` n'a pas changé: il essayait déjà d'ajouter un duplex. Sa sortie diffère maintenant du legacy à deux
endroits et c'est voulu: `Logement ajouté: C003 (DUPLEX)` au lieu de `Type de logement inconnu: DUPLEX` et
une ligne `Duplex: 0/1` dans le rapport.