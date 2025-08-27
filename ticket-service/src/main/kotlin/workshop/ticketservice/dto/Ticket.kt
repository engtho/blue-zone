package workshop.ticketservice.dto

data class Ticket(
        val ticketId: String,
        val alarmId: String,
        val customerId: String,
        val status: String,
        val createdAt: Long,
        val description: String = ""
)
