# Kafka Microservices Worksho## ⚡ Hot Reload Features
- **Custom File Watcher**: Each container has an embedded `inotifywait` script
- **Automatic Restart**: Changes to any `.kt` file trigger application restart
- **Volume Mounting**: Source code is mounted from host to container
- **Debug Ports**: Each service exposes a debug port for IDE connection
- **No Spring DevTools**: Uses lightweight custom restart mechanismot Reload Edition

## 🚀 Quick Start with Hot Reload
This workshop uses custom inotify-based file watching for automatic hot reload during development.

```bash
docker compose up --build
```

Your services will automatically reload when you change source code thanks to embedded file watchers in each container.

### Available Services
- **Alarm Service** → http://localhost:8080 (Debug: 5005)
- **Customer Service** → http://localhost:8084 (Debug: 5006)  
- **Ticket Service** → http://localhost:8082 (Debug: 5007)
- **Notification Service** → (background service, no HTTP port)
- **Kafka UI** → http://localhost:8081

## 📡 Test the Flow
```bash
# Start an alarm (this will trigger the whole flow)
curl -X POST "http://localhost:8080/api/alarms/start" \
  -H "Content-Type: application/json" \
  -d '{"alarmId": "test-123"}'

# Check that tickets were created
curl http://localhost:8082/api/tickets
```

## � Hot Reload Features
- **Automatic Code Sync**: Edit any `.kt` file and see changes instantly
- **Spring Boot DevTools**: Automatic application restart
- **Maven Integration**: Changes to `pom.xml` trigger full rebuild
- **No Manual Steps**: Just save your files and test!

## 📖 Development Tips
- Edit source code in `src/main/kotlin/` and watch containers restart automatically
- Monitor Kafka messages in the UI: http://localhost:8081
- Topics: `alarms`, `notifications`, `tickets`
- Watch logs: `docker compose logs -f [service-name]`
- Debug: Connect your IDE to the debug ports (5005, 5006, 5007)

## 🏗️ Architecture
Each service is a standalone Spring Boot application with:
- Independent `pom.xml` (no parent/multi-module setup)
- Kotlin source code
- Kafka integration for event-driven communication
- Embedded file watcher for development hot reload

## 🛑 Stop Services
```bash
docker compose down
```
