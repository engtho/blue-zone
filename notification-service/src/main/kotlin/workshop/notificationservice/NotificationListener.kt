package workshop.notificationservice

import java.time.Instant
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

data class NotificationEvent(val alarmId: String, val status: String, val timestamp: Long)

@Service
class NotificationPublisher(private val kafkaTemplate: KafkaTemplate<String, String>) {
    fun publish(event: NotificationEvent) {
        val json =
                com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().writeValueAsString(event)
        kafkaTemplate.send("notifications", event.alarmId, json)
    }
}

@Service
class NotificationService(private val publisher: NotificationPublisher) {
    private val log = LoggerFactory.getLogger(NotificationService::class.java)

    @KafkaListener(topics = ["alarms"], groupId = "notification-service")
    fun onAlarm(event: Map<String, Any>) {
        log.info("Varsling mottok alarm: {}", event)
        val alarmId = event["alarmId"]?.toString() ?: "unknown"
        val out = NotificationEvent(alarmId, "SENT", Instant.now().epochSecond)
        publisher.publish(out)
        log.info("Varsling sendte notification: {}", out)
    }

    @KafkaListener(topics = ["tickets"], groupId = "notification-service")
    fun onTicket(event: Map<String, Any>) {
        log.info("Varsling fikk ticket created (for demo): {}", event)
    }
}
