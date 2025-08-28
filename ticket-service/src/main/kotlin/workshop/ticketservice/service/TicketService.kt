package workshop.ticketservice.service

import workshop.ticketservice.dto.Ticket

interface TicketService {

    fun createTicketForAlarm(
            alarmId: String,
            customerId: String,
            service: String,
            impact: String
    ): Ticket

    fun resolveTicketsForAlarm(alarmId: String): List<Ticket>

    fun getAllTickets(): List<Ticket>

    fun getTicketById(ticketId: String): Ticket?

    fun getTicketsForCustomer(customerId: String): List<Ticket>

    fun getTicketsForAlarm(alarmId: String): List<Ticket>

    fun updateTicketStatus(ticketId: String, newStatus: String): Ticket?
}
