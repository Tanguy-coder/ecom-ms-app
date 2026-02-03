# E-Commerce Microservices Application

Application e-commerce basée sur une architecture microservices utilisant Spring Boot et Spring Cloud.

## 📋 Table des matières

- [Vue d'ensemble](#-vue-densemble)
- [Architecture](#-architecture)
- [Technologies](#-technologies)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Démarrage](#-démarrage)
- [Services](#-services)
- [Endpoints API](#-endpoints-api)
- [Modèle de données](#-modèle-de-données)
- [Tests](#-tests)
- [Roadmap](#-roadmap)

## 🎯 Vue d'ensemble

Cette application démontre l'implémentation d'une architecture microservices pour un système e-commerce. Elle permet la gestion de clients, de produits et de factures à travers plusieurs services indépendants qui communiquent entre eux.

### Fonctionnalités principales

- Gestion des clients (CRUD)
- Gestion de l'inventaire de produits (CRUD)
- Génération de factures
- Découverte dynamique des services
- API Gateway pour le routage centralisé

## 🏗️ Architecture

L'application est composée de 5 microservices :

```
┌─────────────────────────────────────────────────────────────┐
│                         Client/Browser                       │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
                  ┌────────────────┐
                  │ Gateway Service│ :8888
                  │  (API Gateway) │
                  └────────┬───────┘
                           │
           ┌───────────────┼───────────────┐
           │               │               │
           ▼               ▼               ▼
    ┌──────────┐   ┌──────────┐   ┌──────────┐
    │Customer  │   │Inventory │   │ Billing  │
    │Service   │   │Service   │   │Service   │
    │:8082     │   │:8083     │   │:8084     │
    └────┬─────┘   └────┬─────┘   └────┬─────┘
         │              │               │
         │              │               │ (Feign)
         │              │          ┌────┴────┐
         │              │          │         │
         ▼              ▼          ▼         ▼
    ┌─────────┐   ┌─────────┐   ┌─────────┬─────────┐
    │H2 DB    │   │H2 DB    │   │H2 DB    │Feign    │
    │customers│   │inventory│   │bills    │Clients  │
    └─────────┘   └─────────┘   └─────────┴─────────┘
         │              │               │
         └──────────────┴───────────────┘
                        │
                        ▼
              ┌──────────────────┐
              │Discovery Service │ :8761
              │    (Eureka)      │
              └──────────────────┘
```

### Communication entre services

- **Gateway Service** : Point d'entrée unique, route les requêtes vers les services appropriés
- **Discovery Service** : Registre Eureka pour la découverte dynamique des services
- **Billing Service** : Utilise OpenFeign pour communiquer avec Customer et Inventory Services

## 🛠️ Technologies

### Backend
- **Java 21**
- **Spring Boot 4.0.0**
- **Spring Cloud 2025.1.0**
  - Spring Cloud Gateway
  - Netflix Eureka (Service Discovery)
  - OpenFeign (Client HTTP déclaratif)
- **Spring Data JPA**
- **Spring Data REST**
- **Spring HATEOAS**

### Base de données
- **H2 Database** (in-memory pour développement)

### Build & Dependencies
- **Maven**
- **Lombok** (réduction du code boilerplate)

### Monitoring
- **Spring Boot Actuator**

## 📦 Prérequis

- Java 21 ou supérieur
- Maven 3.8+
- Un IDE (IntelliJ IDEA, Eclipse, VS Code)

## 🚀 Installation

1. **Cloner le repository**
```bash
git https://github.com/Tanguy-coder/ecom-ms-app
cd ecom-ms-app
```

2. **Compiler tous les services**
```bash
mvn clean install
```

Ou compiler individuellement chaque service :
```bash
cd discovery-service && mvn clean install
cd ../gateway-service && mvn clean install
cd ../customer-service && mvn clean install
cd ../inventory-service && mvn clean install
cd ../billing-service && mvn clean install
```

## ▶️ Démarrage

### Ordre de démarrage recommandé

Il est important de démarrer les services dans cet ordre :

**1. Discovery Service (Eureka)**
```bash
cd discovery-service
mvn spring-boot:run
```
Attendre que Eureka soit complètement démarré (http://localhost:8761)

**2. Gateway Service**
```bash
cd gateway-service
mvn spring-boot:run
```

**3. Services métier** (peuvent être démarrés en parallèle)

Terminal 1:
```bash
cd customer-service
mvn spring-boot:run
```

Terminal 2:
```bash
cd inventory-service
mvn spring-boot:run
```

Terminal 3:
```bash
cd billing-service
mvn spring-boot:run
```

### Vérification du démarrage

- **Eureka Dashboard** : http://localhost:8761
- Vérifier que tous les services sont enregistrés dans Eureka

## 🔧 Services

### 1. Discovery Service
- **Port** : 8761
- **Rôle** : Registre Eureka pour la découverte de services
- **URL** : http://localhost:8761

### 2. Gateway Service
- **Port** : 8888
- **Rôle** : API Gateway, point d'entrée unique
- **Routes** :
  - `/customers/**` → Customer Service
  - `/products/**` → Inventory Service

### 3. Customer Service
- **Port** : 8082
- **Rôle** : Gestion des clients
- **Base de données** : H2 (customers-db)
- **Console H2** : http://localhost:8082/h2-console

### 4. Inventory Service
- **Port** : 8083
- **Rôle** : Gestion de l'inventaire des produits
- **Base de données** : H2 (inventory-db)
- **Console H2** : http://localhost:8083/h2-console

### 5. Billing Service
- **Port** : 8084
- **Rôle** : Gestion des factures
- **Base de données** : H2 (bills-db)
- **Console H2** : http://localhost:8084/h2-console

## 📡 Endpoints API

### Via Gateway (recommandé)

#### Customer Service
```bash
# Lister tous les clients
GET http://localhost:8888/customers

# Obtenir un client par ID
GET http://localhost:8888/customers/{id}

# Créer un client
POST http://localhost:8888/customers
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com"
}
```

#### Inventory Service
```bash
# Lister tous les produits
GET http://localhost:8888/products

# Obtenir un produit par ID
GET http://localhost:8888/products/{id}

# Créer un produit
POST http://localhost:8888/products
Content-Type: application/json

{
  "name": "Product Name",
  "price": 1500.0,
  "quantity": 50
}
```

### Accès direct aux services

#### Billing Service
```bash
# Obtenir une facture complète (avec client et produits)
GET http://localhost:8084/api/bills/{id}
```

#### Customer Service (direct)
```bash
GET http://localhost:8082/customers
GET http://localhost:8082/customers/{id}
```

#### Inventory Service (direct)
```bash
GET http://localhost:8083/products
GET http://localhost:8083/products/{id}
```

### Endpoints Actuator

```bash
# Customer Service
GET http://localhost:8082/actuator/health

# Inventory Service
GET http://localhost:8083/actuator/health
GET http://localhost:8083/actuator/refresh

# Billing Service
GET http://localhost:8084/actuator/health
GET http://localhost:8084/actuator/refresh
```

## 💾 Modèle de données

### Customer (Customer Service)
```java
{
  "id": Long,
  "name": String,
  "email": String
}
```

**Données de test** :
- Tanguy (tanguy@gmail.com)
- Yaya (yaya@gmail.com)
- Renaud (ren@gmail.com)

### Product (Inventory Service)
```java
{
  "id": Long,
  "name": String,
  "price": Double,
  "quantity": Integer
}
```

**Données de test** :
- Yaourt (1200.0, qty: 20)
- Milk (700.0, qty: 13)
- Sugar (1300.0, qty: 40)
- Cookies (900.0, qty: 20)
- Cake (1200.0, qty: 14)

### Bill (Billing Service)
```java
{
  "id": Long,
  "billingDate": Date,
  "customerId": Long,
  "customer": Customer,
  "productItems": [
    {
      "id": Long,
      "productId": Long,
      "quantity": Integer,
      "unitPrice": Double,
      "product": Product
    }
  ]
}
```

## 🧪 Tests

Exécuter les tests pour tous les services :
```bash
mvn test
```

Ou pour un service spécifique :
```bash
cd customer-service
mvn test
```

## 📊 Connexion aux bases de données H2

Pour chaque service, accéder à la console H2 :

**Customer Service** : http://localhost:8082/h2-console
- JDBC URL: `jdbc:h2:mem:customers-db`
- Username: `sa`
- Password: _(laisser vide)_

**Inventory Service** : http://localhost:8083/h2-console
- JDBC URL: `jdbc:h2:mem:inventory-db`
- Username: `sa`
- Password: _(laisser vide)_

**Billing Service** : http://localhost:8084/h2-console
- JDBC URL: `jdbc:h2:mem:bills-db`
- Username: `sa`
- Password: _(laisser vide)_

## 🗺️ Roadmap

### Améliorations prévues

- [ ] **Sécurité**
  - Implémenter Spring Security
  - Ajouter OAuth2/JWT pour l'authentification
  - Sécuriser les endpoints

- [ ] **Configuration centralisée**
  - Ajouter Spring Cloud Config Server
  - Externaliser les configurations

- [ ] **Résilience**
  - Intégrer Resilience4j pour les circuit breakers
  - Ajouter des patterns de retry et timeout
  - Implémenter le fallback pour les appels Feign

- [ ] **Observabilité**
  - Intégrer Spring Cloud Sleuth pour le tracing distribué
  - Ajouter Zipkin pour la visualisation des traces
  - Améliorer le monitoring avec Prometheus + Grafana

- [ ] **Base de données**
  - Migration vers PostgreSQL/MySQL pour la production
  - Ajouter Flyway/Liquibase pour la migration de schéma

- [ ] **Tests**
  - Implémenter les tests unitaires
  - Ajouter les tests d'intégration
  - Tests de contrat avec Spring Cloud Contract

- [ ] **Documentation**
  - Ajouter Swagger/OpenAPI pour la documentation des APIs
  - Générer la documentation automatique

- [ ] **Conteneurisation**
  - Dockeriser chaque microservice
  - Créer un docker-compose pour le démarrage simplifié
  - Préparer pour Kubernetes

- [ ] **Messaging**
  - Intégrer RabbitMQ/Kafka pour la communication asynchrone
  - Implémenter des événements pour la synchronisation des données

## 👥 Auteur

**TanguyDev**

## 📄 Licence

Ce projet est un projet éducatif pour l'apprentissage des microservices avec Spring Cloud.

## 🤝 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à ouvrir une issue ou une merge request.

### Comment contribuer

1. Fork le projet
2. Créer une branche pour votre feature (`git checkout -b feature/AmazingFeature`)
3. Commit vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Merge Request

## 📞 Support

Pour toute question ou problème, ouvrir une issue sur GitLab.

---

**Note** : Ce projet utilise des bases de données H2 en mémoire. Les données sont réinitialisées à chaque redémarrage des services.
