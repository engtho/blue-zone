package workshop.alarmservice.publisher

import workshop.alarmservice.dto.AlarmEvent

/**
 * Interface for alarm event publishing.
 *
 * This abstraction allows for different publishing implementations:
 * - Kafka publisher (current)
 * - RabbitMQ publisher
 * - AWS SNS publisher
 * - Mock publisher for testing
 */
interface AlarmEventPublisher {

    /**
     * Publish an alarm event
     * @param event the alarm event to publish
     */
    fun publish(event: AlarmEvent)

    /**
     * Publish multiple alarm events
     * @param events the list of alarm events to publish
     */
    fun publishBatch(events: List<AlarmEvent>) {
        events.forEach { publish(it) }
    }
}
