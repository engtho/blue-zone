package workshop.ticketservice.controller

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import workshop.ticketservice.client.CustomerClient
import workshop.ticketservice.dto.Ticket
import workshop.ticketservice.service.TicketService

@RestController
@RequestMapping("/api/tickets")
class TicketController(
        private val ticketService: TicketService,
        private val customerClient: CustomerClient
) {
    private val log = LoggerFactory.getLogger(TicketController::class.java)

    @GetMapping
    fun all(): ResponseEntity<List<Ticket>> = ResponseEntity.ok(ticketService.getAllTickets())

    @GetMapping("/{ticketId}")
    fun getById(@PathVariable ticketId: String): ResponseEntity<Ticket> {
        val ticket = ticketService.getTicketById(ticketId)
        return if (ticket != null) {
            ResponseEntity.ok(ticket)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/customer/{customerId}")
    fun getByCustomerId(@PathVariable customerId: String): ResponseEntity<List<Ticket>> {
        val tickets = ticketService.getTicketsForCustomer(customerId)
        return ResponseEntity.ok(tickets)
    }

    @GetMapping("/alarm/{alarmId}")
    fun getByAlarmId(@PathVariable alarmId: String): ResponseEntity<List<Ticket>> {
        val tickets = ticketService.getTicketsForAlarm(alarmId)
        return ResponseEntity.ok(tickets)
    }

    @GetMapping("/{ticketId}/customer-info")
    fun getCustomerForTicket(@PathVariable ticketId: String): ResponseEntity<String> {
        val ticket =
                ticketService.getTicketById(ticketId) ?: return ResponseEntity.notFound().build()

        val customerInfo = customerClient.getCustomer(ticket.customerId)
        return if (customerInfo != null) {
            log.info("Retrieved customer info for ticket {}", ticketId)
            ResponseEntity.ok(customerInfo)
        } else {
            log.error("Failed to get customer for ticket {}", ticketId)
            ResponseEntity.ok("Customer information not available")
        }
    }

    @PutMapping("/{ticketId}/status")
    fun updateStatus(
            @PathVariable ticketId: String,
            @RequestParam status: String
    ): ResponseEntity<Ticket> {
        val updatedTicket = ticketService.updateTicketStatus(ticketId, status)
        return if (updatedTicket != null) {
            ResponseEntity.ok(updatedTicket)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
