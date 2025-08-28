package workshop.alarmservice.publisher

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import workshop.alarmservice.dto.AlarmEvent

/**
 * Kafka implementation of AlarmEventPublisher.
 *
 * This implementation publishes alarm events to Kafka topics. Uses Spring's KafkaTemplate for
 * reliable message publishing.
 */
@Service
class KafkaAlarmEventPublisher(private val kafkaTemplate: KafkaTemplate<String, String>) :
        AlarmEventPublisher {

    private val log = LoggerFactory.getLogger(KafkaAlarmEventPublisher::class.java)
    private val objectMapper = jacksonObjectMapper()

    override fun publish(event: AlarmEvent) {
        try {
            val json = objectMapper.writeValueAsString(event)
            // TODO FIX SOMETHING ? 
            kafkaTemplate.send("alarms", event.alarmId, json)
            log.info("Published alarm event: {}", event.alarmId)
        } catch (e: Exception) {
            log.error("Failed to publish alarm event {}: {}", event.alarmId, e.message, e)
            throw e
        }
    }
}
