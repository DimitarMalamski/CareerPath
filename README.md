# CareerPath

A production-ready full-stack job platform built with Angular and Spring Boot, focusing on clean architecture, CI/CD automation, and AI-assisted job matching.
  
📦 Repository: [Here](https://github.com/DimitarMalamski/CareerPath)  

---

## 🚀 Overview

CareerPath is a full-stack web application that allows users to browse job listings, manage professional profiles, and receive AI-assisted job recommendations based on their skills and experience.

The project emphasizes:

- Scalable backend architecture (Hexagonal / Ports & Adapters)
- Multi-stage CI/CD automation
- Strong software quality practices (>80% test coverage)
- Containerized full-stack deployment
- Production hosting using Raspberry Pi + Cloudflare

This project was built to simulate real-world engineering standards in modern software systems.

---

## 🛠 Tech Stack

### Frontend
- Angular (TypeScript)
- Tailwind CSS
- Vitest (Unit Testing)
- WebSockets (real-time updates)

### Backend
- Java
- Spring Boot
- Hexagonal Architecture (Ports & Adapters)
- JWT Authentication (Supabase)
- OpenAI API integration

### Database
- PostgreSQL
- Flyway (Database Migrations)

### DevOps & Quality
- Docker & Docker Compose
- GitLab CI/CD
- SonarQube (Static Analysis)
- Testcontainers (Integration Testing)
- Trivy (Container Security Scanning)
- Google Lighthouse (Frontend Performance)

### Infrastructure
- Raspberry Pi self-hosted deployment
- DockerHub (Image registry)
- Cloudflare Tunnel

---

## ✨ Core Features

- 🔐 JWT-based authentication & authorization (Supabase)
- 🤖 AI-powered job matching using OpenAI
- 📄 Job listing management with pagination
- 🧠 Profile-based skill management
- ⚡ Real-time updates via WebSockets
- 🧪 Unit, Integration, and E2E testing
- 📦 Fully containerized architecture
- 🚀 Automated CI/CD pipeline

---

## 🏗 Architecture

The backend follows a **Hexagonal Architecture (Ports & Adapters)** model.

This ensures:
- Clear separation between domain logic and infrastructure
- Easier testability
- Flexible replacement of external services
- Maintainable long-term system design

> C4 architecture diagrams are available in the `/docs` folder.

---

## 🔄 CI/CD Pipeline

The project uses a branch-based CI/CD strategy:

### feature branch
- Linting
- Unit tests
- SonarQube analysis

### dev branch
- Integration tests (Testcontainers)
- Docker image build

### main branch
- Production image build
- Docker image push
- Deployment via Raspberry Pi

All production code:
- Passes SonarQube quality gates
- Meets >80% test coverage
- Is automatically built and deployed

---

## 🧪 Testing Strategy

- Backend Unit Tests (Service Layer)
- Backend Integration Tests (Testcontainers)
- Frontend Component Unit Tests
- End-to-End Tests (Authentication + Core Flows)

The goal is to merge only tested and validated code into production branches.

---

## 📸 Preview

> Any relevant screenshots of the application can be found in the `/docs` folder.

---

## 🐳 Running Locally

### Prerequisites
- Docker
- Node.js (if running frontend separately)
- Java 17+

### Start the full system

```bash
docker compose up --build
```

Frontend: http://localhost:4200  
Backend: http://localhost:8080

---

## 🎯 Engineering Focus

- Clean backend architecture using Hexagonal principles
- Automated multi-environment CI/CD pipelines
- Production-grade Docker deployment
- Secure authentication & token-based authorization
- AI service integration within scalable backend structure
- Infrastructure-level deployment without managed cloud hosting

---

## 👤 Author

Dimitar Malamski  
Software Engineering Student, Eindhoven, Netherlands  
GitHub: [Here](https://github.com/DimitarMalamski)  
LinkedIn: [Here](https://www.linkedin.com/in/dimitar-malamski-a00253238/)
