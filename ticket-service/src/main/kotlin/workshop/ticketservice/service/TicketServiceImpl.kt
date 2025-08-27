package workshop.ticketservice.service

import java.time.Instant
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import workshop.ticketservice.client.CustomerClient
import workshop.ticketservice.dto.Ticket
import workshop.ticketservice.dto.TicketEvent
import workshop.ticketservice.repository.TicketRepository

/**
 * Implementation of TicketService interface.
 *
 * This class contains the business logic for ticket operations. It orchestrates between the
 * repository, external clients, and messaging.
 */
@Service
class TicketServiceImpl(
        private val ticketRepository: TicketRepository,
        private val kafkaTemplate: KafkaTemplate<String, String>,
        private val customerClient: CustomerClient
) : TicketService {
        private val log = LoggerFactory.getLogger(TicketServiceImpl::class.java)

        override fun createTicketForAlarm(
                alarmId: String,
                customerId: String,
                service: String,
                impact: String
        ): Ticket {
                try {
                        // Try to get customer information
                        val customerInfo =
                                if (customerId != "general") {
                                        customerClient.getCustomerOrDefault(
                                                customerId,
                                                "Customer $customerId"
                                        )
                                } else {
                                        "General incident"
                                }

                        val ticketId = UUID.randomUUID().toString()
                        val description = "Incident: $service $impact affecting $customerInfo"
                        val ticket =
                                Ticket(
                                        ticketId,
                                        alarmId,
                                        customerId,
                                        "OPEN",
                                        Instant.now().epochSecond,
                                        description
                                )

                        val savedTicket = ticketRepository.save(ticket)

                        val event =
                                TicketEvent(
                                        ticketId,
                                        alarmId,
                                        customerId,
                                        "CREATED",
                                        ticket.createdAt
                                )
                        publishTicketEvent(event, ticketId)

                        log.info(
                                "Created ticket {} for alarm {} and customer {}",
                                ticketId,
                                alarmId,
                                customerId
                        )
                        return savedTicket
                } catch (e: Exception) {
                        log.error(
                                "Error creating ticket for alarm {} and customer {}: {}",
                                alarmId,
                                customerId,
                                e.message,
                                e
                        )
                        throw e
                }
        }

        override fun resolveTicketsForAlarm(alarmId: String): List<Ticket> {
                val ticketsToResolve =
                        ticketRepository.findByAlarmId(alarmId).filter { it.status == "OPEN" }
                val resolvedTickets = mutableListOf<Ticket>()

                ticketsToResolve.forEach { ticket ->
                        val resolvedTicket = ticket.copy(status = "RESOLVED")
                        val updatedTicket = ticketRepository.update(resolvedTicket)
                        resolvedTickets.add(updatedTicket)

                        val event =
                                TicketEvent(
                                        ticket.ticketId,
                                        ticket.alarmId,
                                        ticket.customerId,
                                        "RESOLVED",
                                        Instant.now().epochSecond
                                )
                        publishTicketEvent(event, ticket.ticketId)

                        log.info("Resolved ticket {} for alarm {}", ticket.ticketId, alarmId)
                }

                return resolvedTickets
        }

        override fun getAllTickets(): List<Ticket> = ticketRepository.findAll()

        override fun getTicketById(ticketId: String): Ticket? = ticketRepository.findById(ticketId)

        override fun getTicketsForCustomer(customerId: String): List<Ticket> =
                ticketRepository.findByCustomerId(customerId)

        override fun getTicketsForAlarm(alarmId: String): List<Ticket> =
                ticketRepository.findByAlarmId(alarmId)

        override fun updateTicketStatus(ticketId: String, newStatus: String): Ticket? {
                val existingTicket = ticketRepository.findById(ticketId) ?: return null
                val updatedTicket = existingTicket.copy(status = newStatus)
                return ticketRepository.update(updatedTicket)
        }

        private fun publishTicketEvent(event: TicketEvent, ticketId: String) {
                val json =
                        com.fasterxml
                                .jackson
                                .module
                                .kotlin
                                .jacksonObjectMapper()
                                .writeValueAsString(event)
                kafkaTemplate.send("tickets", ticketId, json)
        }
}
