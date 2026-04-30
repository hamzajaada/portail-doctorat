# Guide de test API avec Postman

Voici la liste complète des endpoints de votre application avec des exemples de JSON prêts à être copiés/collés dans Postman. 

> [!IMPORTANT]
> **Base URL :** `http://localhost:8080`
> Assurez-vous de définir le type de body sur **`raw`** et **`JSON`** dans Postman.

---

## 1. Utilisateurs (User)

> [!NOTE]
> Il faut toujours commencer par créer l'utilisateur (avec le rôle adéquat) avant de créer le Doctorant ou le Directeur correspondant.

### Créer un utilisateur (Doctorant)
- **Méthode :** `POST`
- **URL :** `/api/users`
- **Body JSON :**
```json
{
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean.dupont@example.com",
  "password": "password123",
  "role": "DOCTORANT"
}
```

### Créer un utilisateur (Directeur)
- **Méthode :** `POST`
- **URL :** `/api/users`
- **Body JSON :**
```json
{
  "nom": "Martin",
  "prenom": "Sophie",
  "email": "sophie.martin@example.com",
  "password": "password123",
  "role": "DIRECTEUR"
}
```

### Lire les utilisateurs
- **Lister tous :** `GET /api/users`
- **Trouver par ID :** `GET /api/users/1`
- **Trouver par Email :** `GET /api/users/email/jean.dupont@example.com`

### Mettre à jour un utilisateur
- **Méthode :** `PUT`
- **URL :** `/api/users/1` *(remplacez 1 par l'ID réel)*
- **Body JSON :**
```json
{
  "nom": "Dupont",
  "prenom": "Jean-Marie",
  "email": "jean.dupont@example.com",
  "password": "newpassword456",
  "role": "DOCTORANT"
}
```

### Supprimer un utilisateur
- **Méthode :** `DELETE`
- **URL :** `/api/users/1`

---

## 2. Doctorants

> [!WARNING]
> Avant de tester ce endpoint, vous **devez** avoir créé un utilisateur avec le rôle `DOCTORANT` et utiliser son email exact.

### Créer un doctorant
- **Méthode :** `POST`
- **URL :** `/api/doctorants`
- **Body JSON :**
```json
{
  "userEmail": "jean.dupont@example.com",
  "cne": "CNE12345678",
  "anneeThese": 1,
  "datePremiereInscription": "2024-09-01"
}
```

### Lire les doctorants
- **Lister tous :** `GET /api/doctorants`
- **Trouver par ID :** `GET /api/doctorants/1`
- **Trouver par Email :** `GET /api/doctorants/email/jean.dupont@example.com`

### Mettre à jour un doctorant
- **Méthode :** `PUT`
- **URL :** `/api/doctorants/1`
- **Body JSON :**
```json
{
  "userEmail": "jean.dupont@example.com",
  "cne": "CNE12345678",
  "anneeThese": 2,
  "datePremiereInscription": "2024-09-01"
}
```

### Supprimer un doctorant
- **Méthode :** `DELETE`
- **URL :** `/api/doctorants/1`

---

## 3. Directeurs

> [!WARNING]
> Avant de tester ce endpoint, vous **devez** avoir créé un utilisateur avec le rôle `DIRECTEUR` et utiliser son email exact.

### Créer un directeur
- **Méthode :** `POST`
- **URL :** `/api/directeurs`
- **Body JSON :**
```json
{
  "userEmail": "sophie.martin@example.com",
  "grade": "Professeur Habilité",
  "departement": "Informatique"
}
```

### Lire les directeurs
- **Lister tous :** `GET /api/directeurs`
- **Trouver par ID :** `GET /api/directeurs/1`
- **Trouver par Email :** `GET /api/directeurs/email/sophie.martin@example.com`

### Mettre à jour un directeur
- **Méthode :** `PUT`
- **URL :** `/api/directeurs/1`
- **Body JSON :**
```json
{
  "userEmail": "sophie.martin@example.com",
  "grade": "Professeur de l'Enseignement Supérieur (PES)",
  "departement": "Génie Logiciel"
}
```

### Supprimer un directeur
- **Méthode :** `DELETE`
- **URL :** `/api/directeurs/1`
