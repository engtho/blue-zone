package workshop.notificationservice.service

import java.time.Instant
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import workshop.notificationservice.dto.NotificationEvent
import workshop.notificationservice.repository.NotificationRepository

/**
 * Implementation of NotificationService.
 *
 * This service encapsulates business logic for notification operations. It coordinates between the
 * repository layer and external publishers.
 */
@Service
class NotificationServiceImpl(
        private val notificationRepository: NotificationRepository,
        private val notificationPublisher: NotificationPublisher
) : NotificationService {

    private val logger = LoggerFactory.getLogger(NotificationServiceImpl::class.java)

    override fun processAlarmEvent(alarmEvent: Map<String, Any>): NotificationEvent {
        logger.info("Processing alarm event: {}", alarmEvent)

        val alarmId = alarmEvent["alarmId"]?.toString() ?: "unknown"
        val notification =
                NotificationEvent(
                        alarmId = alarmId,
                        status = "SENT",
                        timestamp = Instant.now().epochSecond
                )

        // Save the notification
        val savedNotification = notificationRepository.save(notification)

        // Publish the notification event
        try {
            notificationPublisher.publish(savedNotification)
            logger.info("Notification published successfully: {}", savedNotification.alarmId)
        } catch (e: Exception) {
            logger.error("Failed to publish notification: {}", savedNotification.alarmId, e)
            // In a real system, you might update the status to FAILED
            // and implement retry logic
        }

        return savedNotification
    }

    override fun processTicketEvent(ticketEvent: Map<String, Any>) {
        logger.info("Processing ticket event (demo): {}", ticketEvent)
        // This is for demonstration purposes
        // In a real system, you might create notifications for ticket updates
    }

    override fun getAllNotifications(): List<NotificationEvent> {
        logger.info("Fetching all notifications")
        return notificationRepository.findAll()
    }

    override fun getNotificationsByAlarmId(alarmId: String): List<NotificationEvent> {
        logger.info("Fetching notifications for alarm: {}", alarmId)
        return notificationRepository.findByAlarmId(alarmId)
    }

    override fun getNotificationsByStatus(status: String): List<NotificationEvent> {
        logger.info("Fetching notifications by status: {}", status)

        // Validate status
        val validStatuses = setOf("SENT", "PENDING", "FAILED")
        if (!validStatuses.contains(status.uppercase())) {
            throw IllegalArgumentException(
                    "Invalid status: $status. Valid statuses: $validStatuses"
            )
        }

        return notificationRepository.findByStatus(status)
    }

    override fun getNotificationsByTimeRange(
            fromTimestamp: Long,
            toTimestamp: Long
    ): List<NotificationEvent> {
        logger.info("Fetching notifications between {} and {}", fromTimestamp, toTimestamp)

        if (fromTimestamp > toTimestamp) {
            throw IllegalArgumentException("From timestamp cannot be greater than to timestamp")
        }

        return notificationRepository.findByTimestampRange(fromTimestamp, toTimestamp)
    }

    override fun getNotificationCount(): Long {
        return notificationRepository.count()
    }

    override fun clearAllNotifications() {
        logger.info("Clearing all notifications")
        notificationRepository.deleteAll()
    }

    override fun cleanupOldNotifications(olderThanTimestamp: Long): Int {
        logger.info("Cleaning up notifications older than {}", olderThanTimestamp)
        return notificationRepository.deleteOlderThan(olderThanTimestamp)
    }
}
