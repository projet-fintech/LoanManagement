
---

### **Loan Management Microservice (README)**

![image](https://github.com/user-attachments/assets/5801cabf-2cdc-4747-b278-5aa4d4d0aff8)


```markdown
# Loan Management Microservice

## Description
Ce microservice gère les demandes de crédit des clients. Il intègre une application Flask pour effectuer des prédictions et communique avec le microservice Transaction pour gérer les remboursements mensuels.

## Fonctionnalités
- Gestion des demandes de crédit.
- Intégration avec Flask pour les prédictions.

## Dépendances
- Spring Boot
- Flask (externe)
- Eureka Client

## Configuration
- Configurez l'URL de l'application Flask dans `application.yml`.

## Lancer le service
```bash
mvn clean install
java -jar target/loan-management-service.jar
