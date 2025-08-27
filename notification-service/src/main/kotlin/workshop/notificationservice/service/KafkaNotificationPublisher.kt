package workshop.notificationservice.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import workshop.notificationservice.dto.NotificationEvent

/**
 * Kafka implementation of NotificationPublisher.
 *
 * This implementation publishes notification events to Kafka topics. In production, you might also
 * have:
 * - RabbitMQNotificationPublisher
 * - SQSNotificationPublisher
 * - WebhookNotificationPublisher
 */
@Service
class KafkaNotificationPublisher(private val kafkaTemplate: KafkaTemplate<String, String>) :
        NotificationPublisher {

    private val logger = LoggerFactory.getLogger(KafkaNotificationPublisher::class.java)
    private val objectMapper = jacksonObjectMapper()
    private val defaultTopic = "notifications"

    override fun publish(event: NotificationEvent) {
        publish(event, defaultTopic)
    }

    override fun publish(event: NotificationEvent, topic: String) {
        try {
            val json = objectMapper.writeValueAsString(event)
            kafkaTemplate.send(topic, event.alarmId, json)
            logger.info("Published notification to topic '{}': alarmId={}", topic, event.alarmId)
        } catch (e: Exception) {
            logger.error(
                    "Failed to publish notification to topic '{}': alarmId={}",
                    topic,
                    event.alarmId,
                    e
            )
            throw e
        }
    }
}
