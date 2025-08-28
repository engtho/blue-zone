package workshop.notificationservice.service

import java.time.Instant
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import workshop.notificationservice.dto.NotificationEvent

@Service
class NotificationServiceImpl(private val kafkaTemplate: KafkaTemplate<String, NotificationEvent>) :
        NotificationService {

    private val logger = LoggerFactory.getLogger(NotificationServiceImpl::class.java)

    override fun processTicketEvent(ticketEvent: Map<String, Any>) {
        val ticketId = ticketEvent["ticketId"]?.toString() ?: "unknown"
        val customerId = ticketEvent["customerId"]?.toString() ?: "unknown"
        val status = ticketEvent["status"]?.toString() ?: "unknown"

        logger.info(
                "Processing ticket event - Ticket: {}, Customer: {}, Status: {}",
                ticketId,
                customerId,
                status
        )

        // Create descriptive message for customer
        val message =
                when (status.uppercase()) {
                    "CREATED" ->
                            "Your support ticket #$ticketId has been created and is being processed. We'll keep you updated on progress."
                    "IN_PROGRESS" ->
                            "Good news! Your ticket #$ticketId is now being actively worked on by our team."
                    "RESOLVED" ->
                            "Your ticket #$ticketId has been resolved. Please contact us if you need further assistance."
                    "CLOSED" ->
                            "Ticket #$ticketId has been closed. Thank you for contacting support!"
                    else -> "Your ticket #$ticketId status has been updated to: $status"
                }

        // Create and publish notification event to Kafka
        val notificationEvent =
                NotificationEvent(
                        ticketId = ticketId,
                        customerId = customerId,
                        message = message,
                        status = "PUBLISHED",
                        timestamp = Instant.now().epochSecond
                )

        try {
            kafkaTemplate.send("notifications", notificationEvent)
            logger.info("Published notification event to Kafka: {}", notificationEvent)
        } catch (e: Exception) {
            logger.error("Failed to publish notification event: {}", e.message, e)
        }

        // Log the "sending" to customer (simulating SMS/email/push notification)
        logger.info("[SIMULATION] Sending to customer {}: {}", customerId, message)
    }
}
