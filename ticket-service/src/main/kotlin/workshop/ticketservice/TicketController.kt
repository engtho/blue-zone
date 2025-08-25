package workshop.ticketservice

import java.time.Instant
import java.util.Collections
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient
import workshop.ticketservice.dto.Ticket
import workshop.ticketservice.dto.TicketEvent

@Service
class TicketStore {
    private val log = LoggerFactory.getLogger(TicketStore::class.java)
    private val tickets = Collections.synchronizedList(mutableListOf<Ticket>())
    fun add(t: Ticket) {
        tickets.add(t)
        log.info("Ticket saved with auto reload: {}", t)
    }
    fun all(): List<Ticket> = tickets.toList()
}

@Service
class TicketService(
        private val store: TicketStore,
        private val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val client = RestClient.create("http://customer-service:8080")
    private val log = LoggerFactory.getLogger(TicketService::class.java)

    fun createTicketForAlarm(alarmId: String) {
        val customerId = "c-42"
        val resp =
                client.get()
                        .uri("/api/customers/{id}", customerId)
                        .retrieve()
                        .toEntity(String::class.java)
        log.info("Hentet kundedata {}: {}", customerId, resp.body)
        val ticketId = UUID.randomUUID().toString()
        val ticket = Ticket(ticketId, alarmId, customerId, "CREATED", Instant.now().epochSecond)
        store.add(ticket)
        val event = TicketEvent(ticketId, alarmId, customerId, "CREATED", ticket.createdAt)
        val json =
                com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().writeValueAsString(event)
        kafkaTemplate.send("tickets", ticketId, json)
    }
}

@RestController
@RequestMapping("/api/tickets")
class TicketController(private val store: TicketStore) {
    @GetMapping fun all(): ResponseEntity<List<Ticket>> = ResponseEntity.ok(store.all())
}
