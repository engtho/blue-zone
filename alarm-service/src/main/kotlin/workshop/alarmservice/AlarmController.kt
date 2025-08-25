package workshop.alarmservice

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.validation.Valid
import java.time.Instant
import java.util.*
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import workshop.alarmservice.dto.AlarmEvent
import workshop.alarmservice.dto.AlarmRequest

@Service
class AlarmEventPublisher(private val kafkaTemplate: KafkaTemplate<String, String>) {
        fun publish(event: AlarmEvent) {
                val json = jacksonObjectMapper().writeValueAsString(event)
                kafkaTemplate.send("alarms", event.alarmId, json)
        }
}

@RestController
@RequestMapping("/api/alarms")
class AlarmController(private val publisher: AlarmEventPublisher) {
        private val logger = LoggerFactory.getLogger(AlarmController::class.java)

        @PostMapping("/start")
        fun start(@Valid @RequestBody req: AlarmRequest): ResponseEntity<AlarmEvent> {
                val event = AlarmEvent(req.alarmId, "Started", Instant.now().epochSecond)
                publisher.publish(event)
                logger.info("Alarm started: $event")
                return ResponseEntity.ok(event)
        }

        @PostMapping("/stop")
        fun stop(@Valid @RequestBody req: AlarmRequest): ResponseEntity<AlarmEvent> {
                val event = AlarmEvent(req.alarmId, "STOPPED", Instant.now().epochSecond)
                publisher.publish(event)
                logger.info("Alarm stopped: $event")
                return ResponseEntity.ok(event)
        }
}
