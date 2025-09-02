# Workshop Tasks

Progressive exercises to learn microservices architecture step by step.

## Before We Start
**Goal**: Get familiar with the codebase and tools.

### Explore the Code (10 minutes):
1. Start the system: `docker compose up --build`
2. Open the frontend: http://localhost:3000
3. Look at the folder structure - notice how each service follows the same pattern
4. Browse through one service (start with `customer-service`) and see:
   - `Controller` - REST endpoints
   - `Service` - Business logic  
   - `Repository` - Data storage
   - `dto/` - Data classes

---

## Task 1: Kafka Producer - Edit a Message
**Goal**: Learn how to publish events to Kafka and see them in the Kafka UI.

### Quick Kafka UI Demo:
- Open Kafka UI: http://localhost:8081
- Show the topics: `alarms`, `tickets`, `notifications`
- Create an alarm in the frontend and watch the `alarms` topic

### Your Mission:
Change the alarm message format that gets published to Kafka.

### TODO Location:
Look for `// TODO: Task 1` in:
- `alarm-service/src/main/kotlin/.../dto/AlarmEvent.kt`

### Instructions:
1. Find the `AlarmEvent` data class
2. Add a new field: `val priority: String = "NORMAL"`
3. Restart the alarm service: `docker compose restart alarm-service`
4. Create an alarm in the frontend
5. Check Kafka UI to see your new field in the message

### What You'll Learn:
- How Kafka messages work
- Data serialization
- Event schema changes

📖 **Learn More**: [Kafka Documentation](https://kafka.apache.org/documentation/)

---

## Task 2: Kafka Consumer - Process Events
**Goal**: Understand how services listen to Kafka events and react.

### Your Mission:
Make the ticket service create tickets when it receives alarm events.

### TODO Location:
Look for `// TODO: Task 2` in:
- `ticket-service/src/main/kotlin/.../TicketListener.kt`

### Instructions:
1. Find the `@KafkaListener` method
2. Implement ticket creation logic
3. For each `customerId` in the alarm, create a ticket
4. Test by creating an alarm and checking tickets appear

### What You'll Learn:
- Kafka consumers
- Event-driven architecture
- Asynchronous processing

📖 **Learn More**: [Spring Kafka Documentation](https://spring.io/projects/spring-kafka)

---

## Task 3: Kafka Producer - Publish New Events
**Goal**: Learn to publish events when business actions happen.

### Your Mission:
When a ticket is created, publish a `TicketCreated` event.

### TODO Location:
Look for `// TODO: Task 3` in:
- `ticket-service/src/main/kotlin/.../TicketService.kt`
- `ticket-service/src/main/kotlin/.../dto/TicketEvent.kt`

### Instructions:
1. Create a `TicketEvent` data class
2. Inject `KafkaTemplate` into `TicketService`
3. Publish event after creating ticket
4. Check Kafka UI to see events in `tickets` topic

### What You'll Learn:
- Publishing custom events
- Service integration patterns
- Kafka topics and messages

---

## Task 4: REST API - Create New Endpoint 
**Goal**: Build a new API endpoint from scratch.

### Your Mission:
Create an endpoint to get multiple customers at once.

### Current State:
Customer service only has `GET /api/customers/{id}` for single customers.

### TODO Location:
Look for `// TODO: Task 4` in:
- `customer-service/src/main/kotlin/.../CustomerController.kt`
- `customer-service/src/main/kotlin/.../CustomerService.kt`

### Instructions:
1. Create `GET /api/customers/batch?ids=1,2,3` endpoint
2. Parse the comma-separated IDs from query parameter
3. Return a list of customers for the given IDs
4. Test with: `curl "http://localhost:8081/api/customers/batch?ids=1,2,3"`

### What You'll Learn:
- REST API design
- Query parameter handling
- Batch operations

📖 **Learn More**: [Spring Boot REST Documentation](https://spring.io/guides/tutorials/rest/)

---

## Task 5: Service Integration - Make API Calls �
**Goal**: Learn how services communicate via HTTP APIs.

### Your Mission:
Use the new batch customer endpoint from the ticket service.

### TODO Location:
Look for `// TODO: Task 5` in:
- `ticket-service/src/main/kotlin/.../CustomerClient.kt`
- `ticket-service/src/main/kotlin/.../TicketService.kt`

### Instructions:
1. Add method to `CustomerClient` for batch customer lookup
2. When creating tickets, collect all customer IDs first
3. Make one batch call instead of multiple single calls
4. Test the full flow: alarm → tickets → customer data fetched efficiently

### What You'll Learn:
- HTTP client patterns
- Service-to-service communication
- Performance optimization with batch operations

📖 **Learn More**: [Spring WebClient Documentation](https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html)

---

## If You Want to Explore More 🚀

### Backend Improvements:
- Replace in-memory storage with PostgreSQL database
- Add proper error handling and validation
- Implement health checks for all services
- Add API documentation with OpenAPI/Swagger
- **Build new microservices**: SLA tracking, escalation service, or audit logging
- Create scheduled tasks for automatic ticket escalation

### DevOps & Monitoring:
- Add Prometheus metrics collection
- Create Grafana dashboards for monitoring

### Security & Authentication:
- Add JWT token-based authentication
- Implement role-based access control
- Add API rate limiting
- Secure inter-service communication

### Data & Analytics:
- Build a data warehouse for historical analysis
- Add real-time dashboards with streaming analytics
- Implement machine learning for predictive maintenance
- Create business intelligence reports

### Frontend Enhancements:
- Add real-time WebSocket updates instead of polling
- Create a map view showing geographical zones
- Add customer detail pages with full information
- Implement dark/light theme switching

📖 **Useful Documentation**:
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Apache Kafka](https://kafka.apache.org/documentation/)
- [Docker Documentation](https://docs.docker.com/)
- [React Documentation](https://react.dev/)
- [Microservices Patterns](https://microservices.io/patterns/)