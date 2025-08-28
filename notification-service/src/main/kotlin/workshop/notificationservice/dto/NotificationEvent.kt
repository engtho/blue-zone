package workshop.notificationservice.dto

data class NotificationEvent(
        val ticketId: String,
        val customerId: String,
        val message: String,
        val status: String,
        val timestamp: Long
)
