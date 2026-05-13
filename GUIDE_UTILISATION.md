# 🎓 Guide d'Utilisation et de Test Ultime : Portail Doctorat

Ce guide est conçu sous forme de **Scénario Complet** pour exploiter 100% des fonctionnalités du Portail Doctorat. Vous allez simuler la vie d'un Doctorant, de son inscription jusqu'à la planification de sa soutenance de thèse, en utilisant les différents comptes.

---

## 🟢 PRÉREQUIS : Démarrage de l'application
Assurez-vous que l'application est lancée (Base de données + Spring Boot).
Les 3 comptes de test (qui ont déjà été créés automatiquement dans la base de données) sont :
- **Admin** : `admin@portail.ma` (Mot de passe : `admin123`)
- **Directeur** : `directeur@portail.ma` (Mot de passe : `dir123`)
- **Doctorant** : `doctorant@portail.ma` (Mot de passe : `doc123`)

---

## 🎯 PHASE 1 : Préparation & Inscription

### Étape 1 : L'Admin ouvre la campagne d'inscription
*L'application ne permet pas de s'inscrire si aucune campagne n'est ouverte.*
1. **Connectez-vous** avec le compte Admin (`admin@portail.ma`).
2. Allez dans le menu **Campagnes** à gauche.
3. Cliquez sur le bouton bleu **+ Nouvelle Campagne**.
4. Remplissez le formulaire :
   * *Année* : `2024-2025`
   * *Date de début* : Mettez la date d'hier.
   * *Date de fin* : Mettez la date de demain.
   * *Quota* : `50`
   * **Cochez la case "Ouverte"** (Très important).
5. Cliquez sur **Enregistrer**.

### Étape 2 : Le Doctorant dépose son dossier
1. **Déconnectez-vous** et connectez-vous avec le compte Doctorant (`doctorant@portail.ma`).
2. Allez dans **Mon Dossier** (dans le menu de gauche).
3. Remplissez le premier formulaire :
   * *CNE* : Un numéro de votre choix.
   * *Sujet de thèse* : "Intelligence Artificielle en Santé".
   * *Directeur de thèse* : Sélectionnez le directeur disponible dans la liste.
4. Cliquez sur **Enregistrer**.
5. Vous arrivez sur la page des documents. **Uploadez un document PDF** (moins de 5 Mo) pour chacun des 3 types requis : *CIN*, *Diplômes*, et *Projet de recherche*.
6. Cliquez sur le bouton final **Soumettre le dossier**.
   > *💡 UX Note : Sur votre Dashboard (Mon Tableau de bord), vous verrez que votre dossier a désormais le statut bleu "SOUMIS".*

### Étape 3 : Le Directeur donne son accord
1. **Connectez-vous** avec le compte Directeur (`directeur@portail.ma`).
2. Allez dans **Dossiers à valider**.
3. Vous y trouvez le dossier fraîchement soumis par le doctorant. Cliquez sur **Détails**.
4. Cliquez sur le bouton vert **Avis Favorable**.

### Étape 4 : L'Admin valide l'inscription
1. **Connectez-vous** avec le compte Admin.
2. Allez dans **Tous les Dossiers**.
3. Le dossier est là avec le statut "Valide par le directeur". Cliquez sur **Détails**.
4. Cliquez sur **Validation Définitive**.
   > *🎉 Fin de la Phase 1 : Le doctorant est officiellement inscrit dans le système !*

---

## 🎯 PHASE 2 : La Demande de Soutenance

### Étape 5 : Le Doctorant lance sa demande de soutenance
*Quelques années plus tard, la thèse est terminée.*
1. **Connectez-vous** avec le compte Doctorant (`doctorant@portail.ma`).
2. Allez dans le menu **Initier Soutenance**.
3. Remplissez les critères académiques obligatoires (Saisissez des valeurs élevées pour passer la validation automatique) :
   * *Publications* : `2`
   * *Conférences* : `2`
   * *Heures de formation* : `200`
4. Uploadez **un fichier PDF pour chacun des 5 livrables** demandés (La demande manuscrite, Le rapport de thèse, Le rapport anti-plagiat, Les publications, et L'attestation de formation).
5. Cliquez sur **Soumettre la demande**.
   > *💡 UX Note : Vous êtes instantanément redirigé vers l'écran **Suivi Soutenance**. Vous y verrez une belle **Timeline verticale** indiquant que votre demande est "Soumise" et en cours de traitement.*

### Étape 6 : L'Admin vérifie la scolarité
1. **Connectez-vous** avec le compte Admin.
2. Allez dans le menu **Soutenances**.
3. Cliquez sur **Détails** à côté de la demande du doctorant (qui est "En attente").
4. Vous pouvez consulter ses heures et ses PDF. Cliquez sur le bouton **Valider les prérequis**.

### Étape 7 : Le Directeur forme le Jury
1. **Connectez-vous** avec le compte Directeur.
2. Allez dans le menu **Gérer Jurys**.
3. La demande du doctorant apparaît (puisque ses prérequis sont validés). Cliquez sur le bouton pour **Proposer Jury**.
4. Saisissez **3 noms distincts** pour les professeurs (Ex: "Pr. Alaoui", "Pr. Tazi", "Pr. Benjelloun").
5. Cliquez sur **Enregistrer le jury**.

### Étape 8 : L'Admin planifie le jour J !
1. **Connectez-vous** une dernière fois avec le compte Admin.
2. Allez dans **Soutenances** et cliquez sur les **Détails** du dossier.
3. Vous voyez le Jury proposé par le directeur. Cliquez sur **Autoriser la soutenance**.
4. Une nouvelle section s'affiche pour fixer la date !
   * Choisissez une **Date et Heure**.
   * Entrez un **Lieu** (Ex: *Amphi 4 - Faculté des Sciences*).
5. Cliquez sur **Planifier la soutenance**.

---

## 🏆 RÉSULTAT FINAL (Vérification UX)
Connectez-vous avec le compte **Doctorant** et cliquez sur **Suivi Soutenance**. 
Vous devez y voir une interface magnifique avec :
1. La **Timeline de suivi 100% verte** (Soumise ➔ Prérequis validés ➔ Jury proposé ➔ Soutenance autorisée).
2. Un grand encart affichant la **Date, l'Heure et la Salle** de la soutenance.
3. Un tableau contenant la liste complète des **Membres du jury** qui vous évalueront.
