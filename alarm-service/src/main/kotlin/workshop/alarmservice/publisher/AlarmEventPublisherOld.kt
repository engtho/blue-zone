package workshop.alarmservice

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import workshop.alarmservice.dto.AlarmEvent

/**
 * Publisher service for alarm events.
 *
 * Handles publishing alarm events to Kafka topics. Separated from business logic for better
 * testability and maintainability.
 */
@Service
class AlarmEventPublisher(private val kafkaTemplate: KafkaTemplate<String, String>) {

    fun publish(event: AlarmEvent) {
        val json = jacksonObjectMapper().writeValueAsString(event)
        kafkaTemplate.send("alarms", event.alarmId, json)
    }
}
