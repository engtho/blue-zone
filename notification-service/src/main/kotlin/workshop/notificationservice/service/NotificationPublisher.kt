package workshop.notificationservice.service

import workshop.notificationservice.dto.NotificationEvent

/**
 * Interface for publishing notification events.
 *
 * This interface abstracts the notification publishing mechanism, allowing for different
 * implementations (Kafka, RabbitMQ, SQS, etc.).
 */
interface NotificationPublisher {

    /**
     * Publish a notification event.
     * @param event The notification event to publish
     */
    fun publish(event: NotificationEvent)

    /**
     * Publish a notification event to a specific topic/queue.
     * @param event The notification event to publish
     * @param topic The topic or queue to publish to
     */
    fun publish(event: NotificationEvent, topic: String)
}
