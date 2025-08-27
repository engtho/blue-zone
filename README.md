# Blue Zone - Microservices Workshop

Welcome to the **Blue Zone Workshop**! This is a hands-on workshop where you'll learn about microservices architecture with **Kafka**, **Spring Boot**, **Kotlin**, and **React TypeScript**.

## What You'll Learn
By the end of this workshop, you will understand:
- How microservices talk to each other using Kafka events
- Clean code patterns: Repository pattern, Service layers, and Interfaces
- How to make HTTP calls between services
- How to build a React frontend that talks to your backend
- How to organize your code properly
- Real-world telecom incident management workflows

## Scenario: Telecom Incident Management
You're working for a telecom company that provides **BROADBAND**, **MOBILE**, and **TV** services. When technical issues occur, the system needs to:

1. **Detect** service outages or degradations
2. **Create** support tickets for affected customers  
3. **Notify** customers about the incident
4. **Prioritize** critical customers (hospitals, emergency services)

## Architecture Overview
The system shows you how microservices work together:

**Frontend:**
- React TypeScript dashboard that updates in real-time
- Smart proxy that routes API calls to the right backend service
- Clean design that shows important tickets first

**Backend Services:**
```
┌─────────────┐    Kafka     ┌──────────────────┐    HTTP    ┌─────────────────┐
│ Alarm       │─────────────▶│ Ticket Service   │───────────▶│ Customer        │
│ Service     │              │                  │            │ Service         │
└─────────────┘              └──────────────────┘            └─────────────────┘
                                       │                              
                                       ▼ Kafka                       
                              ┌──────────────────┐                   
                              │ Notification     │                   
                              │ Service          │                   
                              └──────────────────┘                   
```

**Code Patterns You'll See:**
- **Repository Pattern**: Clean way to save and get data
- **Service Layer**: Where the business logic lives 
- **Interfaces**: Makes code easy to test and change
- **Client Services**: Clean way to call other services
- **Event-Driven Communication**: Services talk through Kafka messages

## Quick Start

### 1. Start All Services
```bash
docker compose up --build
```

### 2. Access the Services
- **Frontend Dashboard** → http://localhost:3000
- **Alarm Service** → http://localhost:8080 (Debug: 5005)
- **Customer Service** → http://localhost:8084 (Debug: 5006)  
- **Ticket Service** → http://localhost:8082 (Debug: 5007)
- **Notification Service** → (background service, no HTTP port)
- **Kafka UI** → http://localhost:8081

### 3. Test the System

**Option A: Use the Web Dashboard**
1. Open http://localhost:3000 in your browser
2. Use the "Create Service Alarm" form to create new alarms
3. Watch tickets appear automatically in the dashboard
4. Monitor real-time updates as the system processes events

**Option B: Use curl commands**
```bash
# Create an alarm for broadband outage
curl -X POST "http://localhost:8080/api/alarms/start" \
  -H "Content-Type: application/json" \
  -d '{
    "service": "BROADBAND",
    "impact": "OUTAGE", 
    "affectedCustomers": ["c-100", "c-42"]
  }'

# Check created tickets
curl http://localhost:8082/api/tickets

# View all alarms
curl http://localhost:8080/api/alarms
```

## What's Already Built

### 🏗️ Code Organization

**All services are organized the same way:**
- `controller/` - REST endpoints (your API)
- `service/` - Business logic (the smart stuff)
- `repository/` - Data access (saving and getting data)
- `client/` - Calling other services
- `listener/` - Processing Kafka messages
- `config/` - Spring configuration

### 🚀 Alarm Service
- **Clean Event Publishing**: `AlarmEventPublisher` interface → `KafkaAlarmEventPublisher` implementation
- **Service Layer**: `AlarmService` handles the business logic
- **Repository Pattern**: `AlarmRepository` interface → `InMemoryAlarmRepository` implementation
- **REST API**: Create/stop alarms, get alarm history
- **Kafka**: Sends events to `alarms` topic

### 👥 Customer Service  
- **Repository Pattern**: `CustomerRepository` interface → `InMemoryCustomerRepository` implementation
- **Service Layer**: `CustomerService` interface → `CustomerServiceImpl` implementation
- **Test Data**: Hospital customers (priority 1) and regular customers (priority 2)
- **REST API**: `GET /api/customers/{id}` gives you customer info

### 🎫 Ticket Service (The Most Complete Example)
- **Layered Setup**: Controller → Service → Repository → Client pattern
- **Event Processing**: `TicketListener` handles alarm events from Kafka
- **External Calls**: `CustomerClient` interface → `RestCustomerClient` implementation
- **Repository Pattern**: `TicketRepository` interface → `InMemoryTicketRepository` implementation
- **Business Logic**: `TicketService` handles ticket creation and management
- **Smart Creation**: Creates separate tickets for each affected customer
- **Priority Sorting**: Hospital customers get handled first

### 📢 Notification Service
- **Interface-based Listeners**: `NotificationListener` interface → `KafkaNotificationListener` implementation
- **Publisher Pattern**: `NotificationPublisher` interface → `KafkaNotificationPublisher` implementation
- **Multi-Event Processing**: Handles both alarm and ticket events
- **Service Layer**: `NotificationService` interface → `NotificationServiceImpl` implementation

### 🖥️ Frontend Dashboard (React TypeScript)
- **Real-time Updates**: Refreshes every 5 seconds automatically
- **Create Alarms**: Nice form where you can pick service type and customers
- **Customer Info**: Shows full customer details in each ticket
- **Priority Display**: Hospital customers show up with red "Critical" badges
- **Works Everywhere**: Looks good on desktop and mobile
- **Smart Routing**: Automatically routes API calls to the right backend service
- **Type Safety**: TypeScript catches errors before you run the code

## Workshop Tasks

### 🎯 Level 1: Understanding the Code
**Goal**: Look around and see how everything works

**Tasks**:
1. **Check out Repository Pattern**: Look at `TicketRepository` interface and `InMemoryTicketRepository` class
2. **Trace Service Layer**: Follow the flow from `TicketController` → `TicketService` → `TicketRepository`
3. **Study Client Code**: See how `CustomerClient` interface makes calls to other services
4. **Look at Event Processing**: See how `TicketListener` processes Kafka messages

### 🔧 Level 2: Add Features  
**Goal**: Build on what's already there

**Current State**: Ticket service creates tickets for each affected customer when alarms happen

**Your tasks**:
1. **Add Ticket Status Updates**:
   - Add `PUT /api/tickets/{id}/status` endpoint in `TicketController`
   - Add business logic in `TicketService.updateTicketStatus()`
   - Send ticket events to Kafka when status changes

2. **Better Priority Handling**:
   - Add priority field to `Ticket` data class
   - Set ticket priority based on customer priority in `TicketService`
   - Sort tickets by priority in `TicketRepository.findAllSorted()`

### 🚀 Level 3: Advanced Stuff
**Goal**: Build new microservice features

**Your tasks**:
1. **Customer Notification Flow**:
   - Make `NotificationService` listen to `tickets` topic
   - Get customer contact details using the `CustomerClient` pattern
   - Send different notifications for ticket status changes:
     - `CREATED`: "We've received your incident report - Ticket #12345"
     - `IN_PROGRESS`: "We're working on your issue - Ticket #12345"  
     - `RESOLVED`: "Your issue has been resolved - Ticket #12345"

2. **Create New Service** (Advanced):
   - Build a new "SLA Service" using the same patterns
   - Listen to ticket events and calculate SLA metrics
   - Send SLA violations to a new Kafka topic
   - Follow the same structure: Controller → Service → Repository → Client

## 🛠️ Development Setup

### Hot Reload (Your Code Updates Automatically!)
- **Backend**: Change any `.kt` file and the service restarts automatically
- **Frontend**: Change React code and see changes instantly in your browser
- **File Sharing**: Your code folder is shared with Docker so changes work immediately
- **Debug Ports**: Connect your IDE to debug each service
- **Maven**: If you change `pom.xml`, everything rebuilds

### How Code is Organized
```
src/main/kotlin/workshop/{service}/
├── controller/          # REST API endpoints
├── service/            # Business logic  
├── repository/         # Data storage
├── client/             # Calls to other services
├── listener/           # Kafka message processing
├── config/             # Spring setup
└── dto/                # Data classes
```

### API Endpoints
All services follow REST patterns and log everything:

**Alarm Service** (Port 8080):
- `POST /api/alarms/start` - Create new service alarm
- `POST /api/alarms/stop` - Stop existing alarm  
- `GET /api/alarms` - List all alarms

**Customer Service** (Port 8084):
- `GET /api/customers/{id}` - Get customer details

**Ticket Service** (Port 8082):
- `GET /api/tickets` - List all tickets (sorted by priority)
- `PUT /api/tickets/{id}/status` - Update ticket status (you'll build this!)

**Frontend Dashboard** (Port 3000):
- Real-time ticket monitoring that updates automatically
- Form to create alarms with customer selection

## 💡 Tips for Working on This

### Understanding the Code
- **Same Patterns Everywhere**: Each service follows the same structure, so once you get one, you get them all
- **Interfaces First**: Look for interfaces before the actual implementation classes
- **Spring Magic**: Services use Spring's `@Autowired` to connect things together cleanly
- **Keep Things Separate**: Business logic goes in service classes, not controllers

### Debugging & Monitoring
- **Kafka UI**: Watch messages flow at http://localhost:8081 - this is super helpful!
- **Service Logs**: Use `docker compose logs -f [service-name]` to see what's happening
- **Hot Reload**: Just edit any `.kt` file and watch it restart automatically
- **IDE Debug**: Connect your IDE to ports 5005 (alarm), 5006 (customer), 5007 (ticket)
- **Frontend DevTools**: Use browser DevTools to debug the React app

### How to Test Things
1. **Create Alarm**: Use the web form or curl to create an alarm
2. **Watch Kafka**: Check Kafka UI to see messages flowing between services
3. **Check Tickets**: Tickets should appear automatically for affected customers  
4. **Look at Customer Data**: Each ticket should show full customer info
5. **Priority Check**: Hospital customers should appear first in the dashboard

## 🧪 Test Data
The system has some fake telecom customers you can play with:

**Hospital Customers (Priority 1) - These Get Handled First:**
- `c-100`: Oslo Universitetssykehus (BROADBAND, MOBILE, TV) 
- `c-300`: Bergen Brannvesen (BROADBAND, MOBILE)

**Regular Customers (Priority 2):**
- `c-42`: Ada Lovelace (BROADBAND, TV) 
- `c-7`: Grace Hopper (MOBILE, BROADBAND)
- `c-200`: Katherine Johnson (MOBILE)

### 🔬 Try These Test Scenarios

**Scenario 1: Hospital Emergency**
```bash
curl -X POST "http://localhost:8080/api/alarms/start" \
  -H "Content-Type: application/json" \
  -d '{
    "service": "BROADBAND",
    "impact": "OUTAGE", 
    "affectedCustomers": ["c-100", "c-300"]
  }'
```
**What Should Happen**: Hospital customers get tickets first, notifications sent right away

**Scenario 2: Mixed Customer Types**
```bash
curl -X POST "http://localhost:8080/api/alarms/start" \
  -H "Content-Type: application/json" \
  -d '{
    "service": "MOBILE",
    "impact": "DEGRADED", 
    "affectedCustomers": ["c-42", "c-100", "c-7"]
  }'
```
**What Should Happen**: Hospital (c-100) ticket shows up first, others follow

**Scenario 3: Multiple Customers**
```bash
curl -X POST "http://localhost:8080/api/alarms/start" \
  -H "Content-Type: application/json" \
  -d '{
    "service": "TV",
    "impact": "OUTAGE", 
    "affectedCustomers": ["c-42", "c-100"]
  }'
```
**What Should Happen**: Multiple tickets created, customer details fetched, frontend updates

## 📚 What You'll Learn

After working through this stuff, you'll know how to:

### 🏛️ Clean Code Patterns
- **Repository Pattern**: Keep your data stuff separate from your business logic
- **Service Layer Pattern**: Put your business rules in service classes, not controllers
- **Interface Segregation**: Write code that's easy to test and change
- **Client Services**: Make clean calls to other APIs
- **Event-Driven Architecture**: Let services talk to each other with messages (Kafka)

### 🔧 Spring Boot & Kotlin Tricks
- **Dependency Injection**: Let Spring wire your classes together automatically
- **Configuration Management**: Different settings for different environments
- **Exception Handling**: Handle errors gracefully without crashing
- **REST API Design**: Build proper APIs that make sense
- **Package Organization**: Organize your code so you can find stuff

### ⚡ Kafka & Message Streaming
- **Event Publishing**: Send messages reliably to other services
- **Event Consumption**: Listen for messages and handle them properly
- **Topic Management**: Understand how Kafka organizes messages
- **Serialization**: Turn objects into JSON and back again

### 🖥️ Frontend + Backend Together
- **React TypeScript**: Build modern web apps with type safety
- **API Integration**: Connect your frontend to your backend
- **Real-time Updates**: Make dashboards that update automatically
- **Proxy Configuration**: Set up your dev environment to work smoothly

### 🚀 Development Tools & Workflow
- **Containerization**: Use Docker to run everything easily
- **Hot Reload**: See your changes instantly without restarting
- **Debugging**: Debug multiple services at the same time
- **Monitoring**: Watch your system work with Kafka UI and logs

## 🏆 Want to Take This Further?

Once you're comfortable with this workshop, try these fun challenges:

1. **Add a Real Database**: Replace the in-memory stores with PostgreSQL or MongoDB
2. **Add Login/Security**: Implement JWT tokens so users have to log in
3. **Add Monitoring**: Set up Prometheus and Grafana to see how your services are performing
4. **Add Tests**: Create integration tests that test the whole flow from start to finish
5. **Add Resilience**: Implement circuit breakers with Hystrix so services don't crash each other
6. **Add API Gateway**: Use Spring Cloud Gateway to centralize all your API routing

## 🆘 Stuck? Here's How to Debug

- **Kafka UI**: Check what messages are flowing at http://localhost:8081
- **Service Logs**: Use `docker compose logs -f [service-name]` to see what's happening
- **Frontend Issues**: Open browser DevTools to debug the React app
- **Architecture Questions**: All services follow the same patterns - if you understand one, you understand them all

---

**Have fun coding! 🎉** These patterns work in real projects too, so you're learning stuff you'll actually use.
