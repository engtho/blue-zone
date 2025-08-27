package workshop.notificationservice.service

import workshop.notificationservice.dto.NotificationEvent

/**
 * Service interface for notification operations.
 *
 * This interface defines the business logic layer for notification management. It encapsulates
 * complex operations and provides a clean API for controllers and listeners.
 */
interface NotificationService {

    /**
     * Process an alarm event and create a notification.
     * @param alarmEvent The alarm event data
     * @return The created notification
     */
    fun processAlarmEvent(alarmEvent: Map<String, Any>): NotificationEvent

    /**
     * Process a ticket event (for demonstration).
     * @param ticketEvent The ticket event data
     */
    fun processTicketEvent(ticketEvent: Map<String, Any>)

    /**
     * Get all notifications.
     * @return List of all notifications
     */
    fun getAllNotifications(): List<NotificationEvent>

    /**
     * Get notifications for a specific alarm.
     * @param alarmId The alarm ID
     * @return List of notifications for the alarm
     */
    fun getNotificationsByAlarmId(alarmId: String): List<NotificationEvent>

    /**
     * Get notifications by status.
     * @param status The notification status (SENT, PENDING, FAILED)
     * @return List of notifications with the status
     */
    fun getNotificationsByStatus(status: String): List<NotificationEvent>

    /**
     * Get notifications within a time range.
     * @param fromTimestamp Start timestamp (epoch seconds)
     * @param toTimestamp End timestamp (epoch seconds)
     * @return List of notifications within the time range
     */
    fun getNotificationsByTimeRange(fromTimestamp: Long, toTimestamp: Long): List<NotificationEvent>

    /**
     * Get total number of notifications.
     * @return Notification count
     */
    fun getNotificationCount(): Long

    /** Clear all notifications (useful for testing). */
    fun clearAllNotifications()

    /**
     * Clean up old notifications.
     * @param olderThanTimestamp Delete notifications older than this timestamp
     * @return Number of deleted notifications
     */
    fun cleanupOldNotifications(olderThanTimestamp: Long): Int
}
