package workshop.notificationservice.repository

import workshop.notificationservice.dto.NotificationEvent

/**
 * Repository interface for notification operations.
 *
 * This interface defines data access operations for notifications. Following the Repository pattern
 * separates data access from business logic.
 *
 * Benefits:
 * - Testability: Easy to mock for unit tests
 * - Flexibility: Can swap implementations (database, cache, etc.)
 * - Single responsibility: Only handles data persistence
 */
interface NotificationRepository {

    /**
     * Save a notification.
     * @param notification The notification to save
     * @return The saved notification
     */
    fun save(notification: NotificationEvent): NotificationEvent

    /**
     * Find all notifications.
     * @return List of all notifications
     */
    fun findAll(): List<NotificationEvent>

    /**
     * Find notifications by alarm ID.
     * @param alarmId The alarm ID to search for
     * @return List of notifications for the alarm
     */
    fun findByAlarmId(alarmId: String): List<NotificationEvent>

    /**
     * Find notifications by status.
     * @param status The notification status (SENT, PENDING, FAILED)
     * @return List of notifications with the status
     */
    fun findByStatus(status: String): List<NotificationEvent>

    /**
     * Find notifications within a time range.
     * @param fromTimestamp Start timestamp (epoch seconds)
     * @param toTimestamp End timestamp (epoch seconds)
     * @return List of notifications within the time range
     */
    fun findByTimestampRange(fromTimestamp: Long, toTimestamp: Long): List<NotificationEvent>

    /**
     * Get total number of notifications.
     * @return Notification count
     */
    fun count(): Long

    /** Delete all notifications. Useful for testing and cleanup operations. */
    fun deleteAll()

    /**
     * Delete notifications older than specified timestamp.
     * @param timestamp Notifications older than this will be deleted
     * @return Number of deleted notifications
     */
    fun deleteOlderThan(timestamp: Long): Int
}
