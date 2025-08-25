package workshop.ticketservice.dto

data class TicketEvent(
        val ticketId: String,
        val alarmId: String,
        val customerId: String,
        val status: String,
        val timestamp: Long
)
