# Blue Zone - Microservices Workshop

Learn microservices architecture with **Kafka**, **Spring Boot**, **Kotlin**, and **React**.

## What You'll Build
A telecom incident management system with 4 microservices:

```
Frontend → Alarm Service → Kafka → Ticket Service → Customer Service
                     ↓
               Notification Service
```

## The Services

- **Alarm Service** (Port 8080): Create service outage alarms
- **Customer Service** (Port 8081): Store customer data  
- **Ticket Service** (Port 8082): Manage support tickets
- **Notification Service**: Process events and log notifications
- **Frontend** (Port 3000): React dashboard

## Quick Start

```bash
# Start everything
docker compose up --build

# Open the dashboard and create alarms using the web form
open http://localhost:3000
```

## Current API Endpoints

**Alarm Service (8080):**
- `POST /api/alarms/start` - Create alarm
- `GET /api/alarms` - List alarms

**Customer Service (8081):**  
- `GET /api/customers/{id}` - Get customer
- `POST /api/customers/batch` - Get multiple customers

**Ticket Service (8082):**
- `GET /api/tickets` - List tickets
- `PUT /api/tickets/{id}/status` - Update ticket status

## Workshop Tasks

See `WORKSHOP-TASKS.md` for the progressive exercises.

## Architecture Documentation

- `ARCHITECTURE-SIMPLE.md` - Simple text diagrams and explanations
- `ARCHITECTURE.md` - Detailed Mermaid diagrams (requires extension)
