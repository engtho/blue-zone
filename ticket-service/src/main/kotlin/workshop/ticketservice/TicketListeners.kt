package workshop.ticketservice

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TicketListeners(private val ticketService: TicketService) {
    private val log = LoggerFactory.getLogger(TicketListeners::class.java)

    @KafkaListener(topics = ["alarms"], groupId = "ticket-service")
    fun onAlarm(event: Map<String, Any>) {
        log.info("Ticket-service mottok alarm: {}", event)
        val type = event["type"]?.toString()
        val alarmId = event["alarmId"]?.toString() ?: return
        if (type == "STARTED") {
            ticketService.createTicketForAlarm(alarmId)
        } else {
            log.info("Ignorerer alarm type {}", type)
        }
    }

    @KafkaListener(topics = ["notifications"], groupId = "ticket-service")
    fun onNotification(event: Map<String, Any>) {
        log.info("Ticket-service observerte notification: {}", event)
    }
}
