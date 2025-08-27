package workshop.ticketservice.repository

import workshop.ticketservice.dto.Ticket

/**
 * Repository interface for ticket data access.
 *
 * This interface defines the contract for ticket data operations. It follows the Repository
 * pattern, commonly used in Spring Boot applications.
 *
 * Benefits:
 * - Abstraction: Business logic doesn't depend on specific storage implementation
 * - Testability: Easy to mock for unit tests
 * - Flexibility: Can swap implementations (in-memory, database, etc.)
 */
interface TicketRepository {

    /** Save a ticket */
    fun save(ticket: Ticket): Ticket

    /** Update an existing ticket */
    fun update(ticket: Ticket): Ticket

    /** Find ticket by ID */
    fun findById(ticketId: String): Ticket?

    /** Find all tickets */
    fun findAll(): List<Ticket>

    /** Find tickets by alarm ID */
    fun findByAlarmId(alarmId: String): List<Ticket>

    /** Find tickets by customer ID */
    fun findByCustomerId(customerId: String): List<Ticket>

    /** Find tickets by status */
    fun findByStatus(status: String): List<Ticket>

    /** Count total tickets */
    fun count(): Long

    /** Delete all tickets (for testing) */
    fun deleteAll()
}
