# Guide de Test Postman - Portail Doctorat API

Voici la liste complète des endpoints de votre API avec des exemples de requêtes JSON (Data) à copier-coller dans Postman pour tester vos opérations `POST` et `PUT`.

L'URL de base par défaut est : `http://localhost:8080` (ajustez le port si nécessaire).

---

## 1. Utilisateurs (User)
**Base URL:** `/api/users`

### Créer un utilisateur (POST)
- **Endpoint:** `POST /api/users`
- **Body (JSON):**
```json
{
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean.dupont@email.com",
  "password": "password123",
  "role": "DOCTORANT" 
}
```
*(Roles possibles: DOCTORANT, DIRECTEUR, etc.)*

### Autres endpoints :
- `GET /api/users` (Liste tous les utilisateurs)
- `GET /api/users/{id}` (Récupère par ID)
- `GET /api/users/email/{email}` (Récupère par Email)
- `PUT /api/users/{id}` (Mise à jour, utiliser le même JSON que pour la création)
- `DELETE /api/users/{id}` (Supprime l'utilisateur)

---

## 2. Doctorants
**Base URL:** `/api/doctorants`

### Créer un doctorant (POST)
- **Endpoint:** `POST /api/doctorants`
- **Body (JSON):**
```json
{
  "userEmail": "jean.dupont@email.com",
  "cne": "D123456789",
  "anneeThese": 1,
  "datePremiereInscription": "2023-09-01"
}
```

### Autres endpoints :
- `GET /api/doctorants`
- `GET /api/doctorants/{id}`
- `GET /api/doctorants/email/{email}`
- `PUT /api/doctorants/{id}`
- `DELETE /api/doctorants/{id}`

---

## 3. Directeurs de Thèse
**Base URL:** `/api/directeurs`

### Créer un directeur (POST)
- **Endpoint:** `POST /api/directeurs`
- **Body (JSON):**
```json
{
  "userEmail": "professeur.martin@email.com",
  "grade": "Professeur de l'Enseignement Supérieur",
  "departement": "Informatique"
}
```

### Autres endpoints :
- `GET /api/directeurs`
- `GET /api/directeurs/{id}`
- `GET /api/directeurs/email/{email}`
- `PUT /api/directeurs/{id}`
- `DELETE /api/directeurs/{id}`

---

## 4. Campagnes
**Base URL:** `/api/campagnes`

### Créer une campagne (POST)
- **Endpoint:** `POST /api/campagnes`
- **Body (JSON):**
```json
{
  "anneeUniv": "2023-2024",
  "dateOuverture": "2023-10-01",
  "dateFermeture": "2023-12-31",
  "active": true
}
```

### Autres endpoints :
- `GET /api/campagnes`
- `GET /api/campagnes/{id}`
- `GET /api/campagnes/active` (Récupère la campagne active actuelle)
- `PUT /api/campagnes/{id}`
- `DELETE /api/campagnes/{id}`
- `POST /api/campagnes/{id}/activer` (Rend cette campagne active et désactive les autres)

---

## 5. Dossiers d'Inscription
**Base URL:** `/api/dossiers`

### Créer un dossier (POST)
- **Endpoint:** `POST /api/dossiers`
- **Body (JSON):**
```json
{
  "doctorantId": 1,
  "directeurId": 1,
  "campagneId": 1,
  "sujetThese": "Intelligence Artificielle et Médecine"
}
```
*(Le statut "BROUILLON" et la date sont gérés automatiquement)*

### Changer le statut (PUT)
- **Endpoint:** `PUT /api/dossiers/{id}/statut?statut=SOUMIS`
*(Exemples de statuts: BROUILLON, SOUMIS, EN_ATTENTE_DIRECTEUR, VALIDE, REJETE)*

### Autres endpoints :
- `GET /api/dossiers`
- `GET /api/dossiers/{id}`
- `GET /api/dossiers/doctorant/{doctorantId}`
- `GET /api/dossiers/directeur/{directeurId}`
- `PUT /api/dossiers/{id}`
- `DELETE /api/dossiers/{id}`

---

## 6. Documents
**Base URL:** `/api/documents`

### Ajouter un document (POST)
- **Endpoint:** `POST /api/documents`
- **Body (JSON):**
```json
{
  "dossierInscriptionId": 1,
  "nomFichier": "carte_identite.pdf",
  "typeDocument": "CIN",
  "cheminFichier": "/uploads/documents/carte_identite.pdf"
}
```

### Autres endpoints :
- `GET /api/documents`
- `GET /api/documents/{id}`
- `GET /api/documents/dossier/{dossierId}`
- `DELETE /api/documents/{id}`

---

## 7. Demandes de Soutenance
**Base URL:** `/api/demandes-soutenance`

### Créer une demande (POST)
- **Endpoint:** `POST /api/demandes-soutenance`
- **Body (JSON):**
```json
{
  "doctorantId": 1,
  "nbPublications": 2,
  "nbConferences": 2,
  "heuresFormation": 200,
  "dateSoutenance": "2024-06-15T09:00:00",
  "lieuSoutenance": "Amphi A"
}
```
*(Doit respecter les prérequis: >=2 publications, >=2 confs, >=200h formation)*

### Vérifier les prérequis avant création (POST)
- **Endpoint:** `POST /api/demandes-soutenance/verifier-prerequis`
- **Body (JSON):** *(Même JSON que pour la création)*

### Changer le statut (PUT)
- **Endpoint:** `PUT /api/demandes-soutenance/{id}/statut?statut=ACCEPTEE`
*(Exemples: EN_ATTENTE, RAPPORTS_ENVOYES, ACCEPTEE, REFUSEE, PLANIFIEE, EFFECTUEE)*

### Autres endpoints :
- `GET /api/demandes-soutenance`
- `GET /api/demandes-soutenance/{id}`
- `GET /api/demandes-soutenance/doctorant/{doctorantId}`
- `PUT /api/demandes-soutenance/{id}`
- `DELETE /api/demandes-soutenance/{id}`

---

## 8. Jurys
**Base URL:** `/api/jurys`

### Créer un jury (POST)
- **Endpoint:** `POST /api/jurys`
- **Body (JSON):**
```json
{
  "demandeSoutenanceId": 1,
  "proposeParId": 1,
  "president": "Pr. Alan Turing",
  "rapporteur1": "Pr. Marie Curie",
  "rapporteur2": "Pr. Albert Einstein",
  "examinateur1": "Pr. Ada Lovelace",
  "examinateur2": "Pr. John von Neumann"
}
```

### Autres endpoints :
- `GET /api/jurys`
- `GET /api/jurys/{id}`
- `GET /api/jurys/demande/{demandeId}`
- `PUT /api/jurys/{id}`
- `DELETE /api/jurys/{id}`
