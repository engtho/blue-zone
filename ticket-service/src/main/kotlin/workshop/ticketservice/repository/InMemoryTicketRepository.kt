package workshop.ticketservice.repository

import java.util.Collections
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import workshop.ticketservice.dto.Ticket

/**
 * In-memory implementation of TicketRepository.
 *
 * This is a concrete implementation of the TicketRepository interface. Uses @Repository annotation
 * to indicate this is a data access component.
 *
 * In production, you might have:
 * - JpaTicketRepository (using Spring Data JPA)
 * - MongoTicketRepository (using Spring Data MongoDB)
 * - RedisTicketRepository (using Redis) etc.
 */
@Repository
class InMemoryTicketRepository : TicketRepository {
    private val log = LoggerFactory.getLogger(InMemoryTicketRepository::class.java)

    // Thread-safe list for concurrent access from multiple Kafka listeners
    private val tickets = Collections.synchronizedList(mutableListOf<Ticket>())

    override fun save(ticket: Ticket): Ticket {
        tickets.add(ticket)
        log.info("Ticket saved: {}", ticket.ticketId)
        return ticket
    }

    override fun update(ticket: Ticket): Ticket {
        val index = tickets.indexOfFirst { it.ticketId == ticket.ticketId }
        if (index != -1) {
            tickets[index] = ticket
            log.info("Ticket updated: {}", ticket.ticketId)
            return ticket
        } else {
            log.warn("Ticket not found for update: {}", ticket.ticketId)
            throw NoSuchElementException("Ticket not found: ${ticket.ticketId}")
        }
    }

    override fun findById(ticketId: String): Ticket? = tickets.find { it.ticketId == ticketId }

    override fun findAll(): List<Ticket> = tickets.toList()

    override fun findByAlarmId(alarmId: String): List<Ticket> =
            tickets.filter { it.alarmId == alarmId }

    override fun findByCustomerId(customerId: String): List<Ticket> =
            tickets.filter { it.customerId == customerId }

    override fun findByStatus(status: String): List<Ticket> = tickets.filter { it.status == status }

    override fun count(): Long = tickets.size.toLong()

    override fun deleteAll() {
        tickets.clear()
        log.info("All tickets deleted")
    }
}
