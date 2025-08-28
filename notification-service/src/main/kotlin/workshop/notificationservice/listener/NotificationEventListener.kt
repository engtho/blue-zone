package workshop.notificationservice.listener

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import workshop.notificationservice.service.NotificationService

@Component
class NotificationEventListener(private val notificationService: NotificationService) {
    private val logger = LoggerFactory.getLogger(NotificationEventListener::class.java)

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
