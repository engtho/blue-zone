package workshop.notificationservice.listener

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import workshop.notificationservice.service.NotificationService

/**
 * Kafka listener for processing alarm and ticket events.
 *
 * This component listens to Kafka topics and delegates processing to the notification service.
 *
 * Educational notes:
 * - @KafkaListener annotation handles Kafka message consumption
 * - Group ID ensures load balancing across multiple instances
 * - Error handling prevents message processing failures from breaking the listener
 */
@Component
class NotificationEventListener(private val notificationService: NotificationService) {
    private val logger = LoggerFactory.getLogger(NotificationEventListener::class.java)

    /** Listen for alarm events and process notifications. */
    @KafkaListener(topics = ["alarms"], groupId = "notification-service")
    fun onAlarmEvent(event: Map<String, Any>) {
        try {
            logger.info("Received alarm event: {}", event)
            notificationService.processAlarmEvent(event)
        } catch (e: Exception) {
            logger.error("Error processing alarm event: {}", event, e)
            // In production, you might:
            // - Send to dead letter queue
            // - Implement retry logic
            // - Send alerts to monitoring system
        }
    }

    /** Listen for ticket events (demonstration). */
    @KafkaListener(topics = ["tickets"], groupId = "notification-service")
    fun onTicketEvent(event: Map<String, Any>) {
        try {
            logger.info("Received ticket event: {}", event)
            notificationService.processTicketEvent(event)
        } catch (e: Exception) {
            logger.error("Error processing ticket event: {}", event, e)
        }
    }
}
