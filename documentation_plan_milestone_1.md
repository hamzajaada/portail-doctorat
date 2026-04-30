# 📂 Plan de Documentation - Milestone 1

Voici la structure arborescente de tous les fichiers impliqués dans la **Milestone 1**. Ce plan de documentation fichier par fichier vous servira de guide pour rédiger votre rapport ou documenter votre code.

```text
portail-doctorat1/
│
├── 🐳 docker-compose.yml          # Démarre la DB PostgreSQL et l'application (profil prod)
├── 🐳 Dockerfile                  # Compile le code Java et crée l'image Docker de l'application
├── 📦 pom.xml                     # Liste toutes les dépendances (Spring Boot, Security, Thymeleaf, H2, PostgreSQL)
│
└── src/main/
    ├── resources/
    │   ├── ⚙️ application.properties       # Fichier de configuration principal (pointe vers dev par défaut)
    │   ├── ⚙️ application-dev.properties   # Config pour le développement local (Utilise H2 en mémoire)
    │   ├── ⚙️ application-prod.properties  # Config pour la production/Docker (Utilise PostgreSQL)
    │   │
    │   └── templates/                      # (Les Vues Thymeleaf - Frontend)
    │       ├── 📄 login.html               # Page de connexion avec gestion des erreurs
    │       ├── 📄 register.html            # Formulaire d'inscription pour les nouveaux doctorants
    │       ├── 📁 fragments/
    │       │   └── 📄 layout.html          # Structure de base (navbar, menu latéral filtré selon le rôle avec sec:authorize)
    │       └── 📁 dashboard/
    │           ├── 📄 admin.html           # Tableau de bord exclusif pour l'Admin
    │           ├── 📄 directeur.html       # Tableau de bord exclusif pour le Directeur
    │           └── 📄 doctorant.html       # Tableau de bord exclusif pour le Doctorant
    │
    └── java/ma/emsi/doctorat/portaildoctorat1/
        │
        ├── 🚀 PortailDoctorat1Application.java # Le point d'entrée qui démarre l'application Spring Boot
        │
        ├── 📁 config/
        │   └── 🛠️ DataInitializer.java        # Injecte les 3 comptes de test (Admin, Directeur, Doctorant) au démarrage si la DB est vide.
        │
        ├── 📁 entities/                       # (La base de données - Modele de données)
        │   ├── 🗄️ User.java                   # Entité principale (id, email, password, role)
        │   ├── 🗄️ Doctorant.java              # Entité liée à User (cne, annee_these...)
        │   ├── 🗄️ Directeur.java              # Entité liée à User (grade, departement...)
        │   ├── 🗄️ Campagne.java               # Gestion de l'année universitaire
        │   ├── 🗄️ DossierInscription.java     # Le dossier reliant Doctorant, Directeur et Campagne
        │   ├── 🗄️ Document.java               # Les fichiers liés à un dossier
        │   ├── 🗄️ DemandeSoutenance.java      # Les prérequis de soutenance d'un doctorant
        │   ├── 🗄️ Jury.java                   # Le comité de soutenance proposé par le directeur
        │   ├── 🏷️ Role.java                   # Enum (DOCTORANT, DIRECTEUR, ADMIN)
        │   └── 🏷️ StatutDossier.java          # Enum (BROUILLON, SOUMIS, VALIDE_DIRECTEUR, etc.)
        │
        ├── 📁 repositories/                   # (La couche d'accès aux données - SQL automatisé)
        │   ├── 💾 UserRepository.java         # Pour chercher un utilisateur par email (findByEmail)
        │   ├── 💾 DoctorantRepository.java    # (Déclaré pour Milestone 1)
        │   └── 💾 DirecteurRepository.java    # (Déclaré pour Milestone 1)
        │
        ├── 📁 security/                       # (Le module de protection de l'application)
        │   ├── 🔐 SecurityConfig.java         # Règle d'accès des URLs, config du /login et /logout
        │   ├── 🔐 CustomUserDetails.java      # Fait le lien entre notre entité User et Spring Security
        │   ├── 🔐 CustomUserDetailsService.java # Explique à Spring comment charger un utilisateur depuis la base
        │   └── 🔐 CustomAuthenticationSuccessHandler.java # Redirige intelligemment vers le bon dashboard après login
        │
        ├── 📁 services/                       # (La logique métier)
        │   └── 🧠 AuthService.java            # Contient la méthode registerDoctorant() pour hacher le mot de passe et sauvegarder l'utilisateur
        │
        └── 📁 controllers/                    # (La réception des requêtes web HTTP)
            ├── 🔀 AuthController.java         # Gère les affichages et soumissions du /login et /register
            └── 🔀 WebController.java          # Gère la navigation vers les bons dashboards (ex: /dashboard/admin)
```

## 💡 Comment documenter ces fichiers ?

Si vous devez rédiger un rapport Word/PDF sur ce projet, je vous conseille de structurer votre document comme suit :

1. **Introduction & Environnement** : Expliquer le rôle de Docker (`docker-compose.yml`, `Dockerfile`) et de Maven (`pom.xml`).
2. **Couche Modèle (Entities)** : Présenter votre diagramme MCD et montrer comment les classes dans `entities/` et les enums le traduisent grâce aux annotations JPA (`@Entity`, `@ManyToOne`).
3. **Sécurité & Authentification** : C'est le gros morceau. Expliquer la chaîne de connexion : de `AuthController` à `SecurityConfig`, en passant par `CustomUserDetailsService` et la redirection par `CustomAuthenticationSuccessHandler`.
4. **Vues & Interface** : Montrer des captures d'écran des fichiers `.html` dans `templates/` et expliquer comment Thymeleaf et `layout.html` permettent d'adapter l'affichage au rôle connecté.
5. **Jeu de données** : Mentionner le `DataInitializer.java` pour montrer comment l'application est prête à être testée dès son lancement.
