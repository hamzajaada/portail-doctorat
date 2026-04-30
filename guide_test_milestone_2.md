# 🎓 Guide Complet de Test : Portail Doctorat (Milestones 1 & 2)

Ce guide a pour but de vous accompagner pas à pas pour tester l'intégralité du circuit métier de votre application, de la création d'une campagne par l'administrateur jusqu'à la validation finale d'un dossier de doctorant.

---

## Étape 0 : Préparation et Lancement

1. **Lancer la base de données** : Si ce n'est pas déjà fait, lancez PostgreSQL via Docker :
   ```bash
   docker-compose up -d db
   ```
2. **Lancer l'application** : Depuis votre IDE ou via Maven :
   ```bash
   mvn spring-boot:run
   ```
3. **Vérification automatique** : Observez les logs de la console. Le `DataInitializer` doit avoir inséré automatiquement vos 3 comptes de test :
   - `admin@portail.ma` (mdp: *admin123*)
   - `directeur@portail.ma` (mdp: *dir123*)
   - `doctorant@portail.ma` (mdp: *doc123*)

---

## 🎬 SCÉNARIO COMPLET : Le cycle de vie d'une inscription

### Acte 1 : L'Administrateur ouvre une Campagne
*Un doctorant ne peut pas s'inscrire s'il n'y a pas de campagne active.*

1. Allez sur `http://localhost:8082/login` et connectez-vous avec `admin@portail.ma` / `admin123`.
2. Dans le menu de gauche, cliquez sur **Campagnes**.
3. Cliquez sur le bouton bleu **"Nouvelle Campagne"**.
4. Remplissez le formulaire :
   - *Année Universitaire* : 2026-2027
   - *Date Ouverture* : (Mettez la date d'hier)
   - *Date Fermeture* : (Mettez une date dans un mois)
   - *Activer immédiatement* : **Cochez la case** (Très important).
5. Cliquez sur **"Créer la campagne"**.
6. **Résultat attendu** : La campagne apparaît dans le tableau avec un badge vert **ACTIVE**. Déconnectez-vous.

---

### Acte 2 : Le Doctorant dépose son dossier
*La campagne est ouverte, le doctorant peut maintenant préparer son inscription.*

1. Connectez-vous avec `doctorant@portail.ma` / `doc123`.
2. Observez le **Dashboard** : il vous indique qu'une campagne est active et que vous n'avez aucun dossier en cours.
3. Cliquez sur **"Nouvelle Inscription"**.
4. Remplissez le formulaire :
   - Le champ "Campagne" est pré-rempli et verrouillé.
   - *Directeur de thèse* : Sélectionnez le directeur disponible dans la liste.
   - *Sujet* : Écrivez "Recherche sur l'Intelligence Artificielle en Santé".
5. Cliquez sur **"Enregistrer (Brouillon) et Suivant"**.
6. **L'écran des documents** s'affiche.
   - Dans le formulaire à droite, choisissez le type "CIN".
   - Sélectionnez une image ou un PDF de moins de 5Mo.
   - Cliquez sur **Téléverser**. Le document apparaît dans la liste à gauche.
7. Répétez l'opération pour un autre document (ex: Diplôme Master).
8. Une fois vos documents uploadés, cliquez sur le bouton vert **"Soumettre définitivement le dossier"** et confirmez.
9. **Résultat attendu** : Vous êtes redirigé vers votre Dashboard. La barre de progression indique que le statut est **SOUMIS** (orange). Déconnectez-vous.

---

### Acte 3 : Le Directeur valide (Avis scientifique)
*Le directeur de thèse reçoit le dossier et donne son accord.*

1. Connectez-vous avec `directeur@portail.ma` / `dir123`.
2. Dans le menu, allez sur **Dossiers Assigner**. Vous devriez voir le dossier du doctorant avec le statut **SOUMIS**.
3. Cliquez sur le bouton **"Détails"**.
4. Sur cet écran, vous pouvez voir le sujet de thèse et vous pouvez télécharger les PDF/Images que le doctorant a uploadés pour les vérifier.
5. Dans la zone *Action requise* (à droite), écrivez "Avis très favorable, sujet pertinent" et cliquez sur le bouton vert **"Valider ce dossier"**.
6. **Résultat attendu** : Le statut passe à **VALIDE** (vert). Déconnectez-vous.

---

### Acte 4 : L'Administrateur effectue la validation finale
*L'administration vérifie les pièces administratives après l'accord du directeur.*

1. Connectez-vous à nouveau avec `admin@portail.ma` / `admin123`.
2. Allez dans **Gestion des Dossiers**.
3. Vous voyez le dossier du doctorant avec le statut **VAL. DIRECTEUR** (bleu clair).
4. Cliquez sur **Détails**.
5. Cliquez sur le bouton vert **"Valider Définitivement (Admin)"**.
6. **Résultat attendu** : Le statut passe à **VAL. ADMIN** (vert foncé). Déconnectez-vous.

---

### Acte 5 : Le Doctorant consulte le résultat
1. Connectez-vous une dernière fois avec `doctorant@portail.ma` / `doc123`.
2. Observez votre Dashboard :
   - Le statut de votre dossier est **VALIDE_ADMIN**.
   - Dans le bloc **Notifications** à droite, vous verrez un message du type : *"Félicitations, votre dossier a été validé par l'administration."*

---

## 🛑 Cas d'erreurs métier à tester (La robustesse)

Pour valider que la sécurité métier fonctionne, essayez de reproduire ces erreurs :

1. **Test d'unicité de campagne** : Connectez-vous en Admin, et essayez de créer une deuxième campagne en cochant "Activer immédiatement". 
   - *Attendu : Un message d'erreur rouge vous interdit d'avoir deux campagnes actives simultanément.*
2. **Test de dates de campagne** : En Admin, modifiez la campagne actuelle pour mettre une "Date d'ouverture" dans le futur (ex: demain). Connectez-vous en Doctorant.
   - *Attendu : Le bouton "Nouvelle inscription" disparaît du Dashboard car la campagne n'est pas encore ouverte.*
3. **Test de sécurité des fichiers** : En Doctorant, essayez d'uploader un fichier `.exe` ou `.zip` sur l'écran des documents.
   - *Attendu : Une erreur rouge vous indique que seuls les PDF et les images (JPG/PNG) sont autorisés.*
4. **Test d'accès illicite** : En étant connecté en tant que Doctorant, tapez l'URL `http://localhost:8082/admin/campagnes` dans votre navigateur.
   - *Attendu : Spring Security va bloquer la page (Erreur 403 Forbidden).*
