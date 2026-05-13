# 🎓 Portail Doctorat - Système de Gestion de Thèses

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker&logoColor=white)

Une solution complète, robuste et moderne pour la gestion académique des doctorants, couvrant l'ensemble du parcours doctoral : de l'inscription initiale à la soutenance finale.

---

## ✨ Fonctionnalités Clés

### 📝 Gestion des Inscriptions
- **Candidature en ligne** : Soumission de dossiers avec documents justificatifs.
- **Circuit de validation** : Validation multiniveau (Administration, Directeur de thèse).
- **Réinscriptions annuelles** : Système intelligent de suivi des années de thèse.
- **Règles métier strictes** : Blocage automatique après 3 ans sans dérogation et alertes fortes à l'approche des 6 ans.

### 🏛️ Gestion des Soutenances
- **Vérification des prérequis** : Contrôle automatisé des publications (Q1/Q2), conférences et heures de formation.
- **Planification & Jury** : Gestion complexe des jurys avec validation des conflits d'intérêt (président et rapporteurs distincts).
- **Workflow de décision** : Processus de validation étape par étape pour la soutenance.

### 👥 Administration & Utilisateurs
- **Dashboards dédiés** : Interfaces personnalisées pour les Administrateurs, Directeurs et Doctorants.
- **Gestion des rôles** : Sécurité granulaire avec Spring Security.
- **Notifications** : Système d'alerte intégré pour les étapes critiques.

---

## 🛠️ Stack Technique

- **Backend** : Java 17, Spring Boot 3.x, Spring Data JPA, Spring Security
- **Frontend** : Thymeleaf, Bootstrap 5, FontAwesome
- **Base de données** : PostgreSQL
- **Infrastructure** : Docker, Docker Compose
- **Tests** : JUnit 5, Mockito, Integration Tests

---

## 🚀 Installation & Lancement

### 1. Prérequis
- Java 17 ou supérieur
- Maven 3.8+
- Docker & Docker Compose

### 2. Clonage et Configuration
```bash
git clone https://github.com/hamzajaada/portail-doctorat.git
cd portail-doctorat
```

### 3. Démarrage de la base de données
```bash
docker-compose up -d
```

### 4. Lancement de l'application
```bash
mvn spring-boot:run
```
L'application sera accessible sur `http://localhost:8081`.

---

## 🔑 Comptes de Démonstration

| Rôle | Email | Mot de passe |
| :--- | :--- | :--- |
| **Administrateur** | `admin@portail.ma` | `admin123` |
| **Directeur** | `directeur@portail.ma` | `dir123` |
| **Doctorant** | `doctorant@portail.ma` | `doc123` |

---

## 📊 Structure du Projet

- `src/main/java` : Code source Java (Controllers, Services, Entities, Repositories).
- `src/main/resources/templates` : Pages Thymeleaf organisées par rôle.
- `src/main/resources/static` : Ressources statiques (CSS, JS, Images).
- `Dockerfile` & `docker-compose.yml` : Configuration de la conteneurisation.
- `Postman` : Collections fournies pour tester les endpoints.

---

## 📝 Documentation Additionnelle
- [Guide d'utilisation détaillé](GUIDE_UTILISATION.md)
- [Journal des modifications](JOURNAL.md)
- [Plan de documentation](documentation_plan_milestone_1.md)

---

## 🤝 Contribution
Les contributions sont les bienvenues ! Pour des changements majeurs, veuillez d'abord ouvrir une issue pour discuter de ce que vous aimeriez changer.

---

© 2024 - Système de Portail Doctorat - Développé avec ❤️ par [Hamza Jaada](https://github.com/hamzajaada)
