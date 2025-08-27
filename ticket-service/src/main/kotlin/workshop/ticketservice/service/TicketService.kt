package workshop.ticketservice.service

import workshop.ticketservice.dto.Ticket

/**
 * Service interface for ticket business operations.
 *
 * This interface defines the business operations available for tickets. It separates the business
 * logic contract from implementation details.
 */
interface TicketService {

    /** Create a ticket for an alarm */
    fun createTicketForAlarm(
            alarmId: String,
            customerId: String,
            service: String,
            impact: String
    ): Ticket

    /** Resolve all tickets for a specific alarm */
    fun resolveTicketsForAlarm(alarmId: String): List<Ticket>

    /** Get all tickets */
    fun getAllTickets(): List<Ticket>

    /** Get ticket by ID */
    fun getTicketById(ticketId: String): Ticket?

    /** Get tickets for a customer */
    fun getTicketsForCustomer(customerId: String): List<Ticket>

    /** Get tickets for an alarm */
    fun getTicketsForAlarm(alarmId: String): List<Ticket>

    /** Update ticket status */
    fun updateTicketStatus(ticketId: String, newStatus: String): Ticket?
}
