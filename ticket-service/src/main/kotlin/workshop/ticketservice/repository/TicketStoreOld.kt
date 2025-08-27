package workshop.ticketservice

import java.util.Collections
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import workshop.ticketservice.dto.Ticket

/**
 * In-memory storage for tickets.
 *
 * Note: This is a simple in-memory store for workshop purposes. In a production system, you would
 * typically use:
 * - JPA with a database (PostgreSQL, MySQL, etc.)
 * - MongoDB with Spring Data
 * - Redis for caching
 * - Other persistent storage solutions
 */
@Service
class TicketStore {
    private val log = LoggerFactory.getLogger(TicketStore::class.java)

    // Thread-safe list for concurrent access from multiple Kafka listeners
    private val tickets = Collections.synchronizedList(mutableListOf<Ticket>())

    /** Add a new ticket to the store */
    fun add(ticket: Ticket) {
        tickets.add(ticket)
        log.info("Ticket saved: {}", ticket.ticketId)
    }

    /** Update an existing ticket in the store */
    fun update(updatedTicket: Ticket) {
        val index = tickets.indexOfFirst { it.ticketId == updatedTicket.ticketId }
        if (index != -1) {
            tickets[index] = updatedTicket
            log.info("Ticket updated: {}", updatedTicket.ticketId)
        } else {
            log.warn("Ticket not found for update: {}", updatedTicket.ticketId)
        }
    }

    /** Get all tickets as an immutable list */
    fun all(): List<Ticket> = tickets.toList()

    /** Find a ticket by ID */
    fun findById(ticketId: String): Ticket? = tickets.find { it.ticketId == ticketId }

    /** Find tickets by alarm ID */
    fun findByAlarmId(alarmId: String): List<Ticket> = tickets.filter { it.alarmId == alarmId }

    /** Find tickets by customer ID */
    fun findByCustomerId(customerId: String): List<Ticket> =
            tickets.filter { it.customerId == customerId }

    /** Get count of tickets */
    fun count(): Int = tickets.size

    /** Clear all tickets (useful for testing) */
    fun clear() {
        tickets.clear()
        log.info("All tickets cleared")
    }
}
