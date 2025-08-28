package workshop.ticketservice.repository

import workshop.ticketservice.dto.Ticket

interface TicketRepository {

    fun save(ticket: Ticket): Ticket

    fun update(ticket: Ticket): Ticket

    fun findById(ticketId: String): Ticket?

    fun findAll(): List<Ticket>

    fun findByAlarmId(alarmId: String): List<Ticket>

    fun findByCustomerId(customerId: String): List<Ticket>

    fun findByStatus(status: String): List<Ticket>

    fun count(): Long

    fun deleteAll()
}
