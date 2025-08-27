package workshop.ticketservice.listener

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import workshop.ticketservice.service.TicketService

data class AlarmEvent(
        val alarmId: String,
        val service: String, // "BROADBAND", "MOBILE", "TV"
        val impact: String, // "OUTAGE", "DEGRADED", "SLOW", "RESOLVED"
        val affectedCustomers: List<String> = emptyList(),
        val timestamp: Long
)

@Component
class TicketListeners(private val ticketService: TicketService) {
    private val log = LoggerFactory.getLogger(TicketListeners::class.java)
    private val objectMapper = jacksonObjectMapper()

    @KafkaListener(topics = ["alarms"], groupId = "ticket-service")
    fun onAlarm(alarmJson: String) {
        try {
            val alarm: AlarmEvent = objectMapper.readValue(alarmJson)
            log.info("Ticket-service received alarm: {}", alarm)

            when (alarm.impact) {
                "OUTAGE", "DEGRADED", "SLOW" -> {
                    log.info(
                            "Creating tickets for alarm {} with impact {}",
                            alarm.alarmId,
                            alarm.impact
                    )
                    // Create tickets for all affected customers
                    alarm.affectedCustomers.forEach { customerId ->
                        ticketService.createTicketForAlarm(
                                alarm.alarmId,
                                customerId,
                                alarm.service,
                                alarm.impact
                        )
                    }
                    // If no specific customers, create a general ticket
                    if (alarm.affectedCustomers.isEmpty()) {
                        ticketService.createTicketForAlarm(
                                alarm.alarmId,
                                "general",
                                alarm.service,
                                alarm.impact
                        )
                    }
                }
                "RESOLVED" -> {
                    log.info("Alarm {} resolved, updating tickets", alarm.alarmId)
                    ticketService.resolveTicketsForAlarm(alarm.alarmId)
                }
                else -> {
                    log.info("Ignoring alarm with impact: {}", alarm.impact)
                }
            }
        } catch (e: Exception) {
            log.error("Error processing alarm event: {}", alarmJson, e)
        }
    }

    @KafkaListener(topics = ["notifications"], groupId = "ticket-service")
    fun onNotification(notificationJson: String) {
        log.info("Ticket-service observed notification: {}", notificationJson)
    }
}
