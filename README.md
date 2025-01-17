# Gestion des Stocks - Backend (Spring Boot)

Ce repository contient la **partie backend** de l'application de **Gestion des Stocks**, développée avec **Spring Boot**. Le backend fournit des **API RESTful** pour permettre à l'application frontend (construite avec Angular) de gérer et d'optimiser les stocks en entrepôt. Il inclut des fonctionnalités pour la gestion des produits, des commandes et des stocks, ainsi qu'une **authentification sécurisée** utilisant **JWT (JSON Web Tokens)**.

---

## 🚀 Fonctionnalités

- **API RESTful** : Fournit des endpoints pour la gestion des produits, des commandes et des stocks.
- **Gestion des Stocks** : Permet d'ajouter, de modifier, de supprimer et de consulter les stocks.
- **Base de Données** : Utilise une base de données relationnelle (MySQL ou PostgreSQL) pour stocker les données.
- **Sécurité** : Intègre Spring Security pour l'authentification et l'autorisation.

---

## 🛠️ Technologies Utilisées

- **Backend Framework** : Spring Boot
- **Base de Données** : MySQL / PostgreSQL
- **API Development** : Spring Web (RESTful APIs)
- **Sécurité** : Spring Security
- **Gestion des Dépendances** : Maven

---

## 📂 Structure du Repository
gestion-stocks-backend/
├── src/
│ ├── main/
│ │ ├── java/com/gestionstocks/
│ │ │ ├── controller/ # Contrôleurs pour les API
│ │ │ ├── model/ # Modèles de données (entités)
│ │ │ ├── repository/ # Interfaces pour l'accès aux données
│ │ │ ├── service/ # Logique métier
│ │ │ └── GestionStocksApplication.java # Classe principale
│ │ └── resources/
│ │ ├── application.properties # Configuration de l'application
│ │ └── data.sql # Données initiales (optionnel)
│ └── test/ # Tests unitaires et d'intégration
├── pom.xml # Fichier de configuration Maven
├── README.md # Documentation du projet
└── .gitignore # Fichier Git ignore

---

## 🚀 Guide de Démarrage

### Prérequis
- Java 17+
- Maven
- MySQL ou PostgreSQL

### Étapes pour Exécuter le Projet

1. **Cloner le Repository** :
   ```bash
   git clone https://github.com/SaifeddineBENZAIED/gestion-stocks-backend.git
   cd gestion-stocks-backend
Configurer la Base de Données :

Modifiez le fichier application.properties pour configurer la connexion à votre base de données :

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_stocks
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

Installer les Dépendances :

```bash
mvn clean install
```

Démarrer l'Application :

```bash
mvn spring-boot:run
```
Le backend sera disponible à l'adresse http://localhost:8080.

🔍 Fonctionnalités Clés

API RESTful

Gestion des Produits :

- GET /api/products : Récupérer la liste des produits.

- POST /api/products : Ajouter un nouveau produit.

- PUT /api/products/{id} : Mettre à jour un produit existant.

- DELETE /api/products/{id} : Supprimer un produit.

Gestion des Stocks :

- GET /api/stocks : Récupérer les niveaux de stock.

- POST /api/stocks : Ajouter un nouvel élément au stock.

- PUT /api/stocks/{id} : Mettre à jour un élément du stock.

- DELETE /api/stocks/{id} : Supprimer un élément du stock.

Gestion des Commandes :

- GET /api/orders : Récupérer la liste des commandes.

- POST /api/orders : Créer une nouvelle commande.

- PUT /api/orders/{id} : Mettre à jour une commande existante.

- DELETE /api/orders/{id} : Supprimer une commande.

Authentification et Autorisation avec JWT

- Authentification :

-- POST /api/auth/login : Permet aux utilisateurs de se connecter et de recevoir un JWT.

-- POST /api/auth/register : Permet aux utilisateurs de s'inscrire.

- Autorisation :

-- Les endpoints sont protégés par Spring Security et nécessitent un JWT valide.

-- Les rôles utilisateur (admin, utilisateur, etc.) sont gérés pour restreindre l'accès aux fonctionnalités.

- Sécurité :

-- Les tokens JWT sont signés avec une clé secrète pour garantir leur intégrité.

-- Les tokens ont une durée de validité limitée pour renforcer la sécurité.

📫 Contact
Pour toute question ou feedback, n'hésitez pas à me contacter :

- Email : saif2001benz2036@gmail.com
