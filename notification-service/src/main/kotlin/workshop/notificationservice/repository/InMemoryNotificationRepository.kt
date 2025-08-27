package workshop.notificationservice.repository

import java.util.Collections
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import workshop.notificationservice.dto.NotificationEvent

/**
 * In-memory implementation of NotificationRepository.
 *
 * This implementation uses a thread-safe synchronized list for concurrent access. In production,
 * you would typically use:
 * - JpaNotificationRepository (with Spring Data JPA)
 * - MongoNotificationRepository (with Spring Data MongoDB)
 * - CassandraNotificationRepository (for high-volume time-series data)
 * - ElasticsearchNotificationRepository (for search and analytics)
 */
@Repository
class InMemoryNotificationRepository : NotificationRepository {
    private val logger = LoggerFactory.getLogger(InMemoryNotificationRepository::class.java)

    // Thread-safe list for concurrent access
    private val notifications = Collections.synchronizedList(mutableListOf<NotificationEvent>())

    override fun save(notification: NotificationEvent): NotificationEvent {
        notifications.add(notification)
        logger.info(
                "Notification saved: alarm={}, status={}",
                notification.alarmId,
                notification.status
        )
        return notification
    }

    override fun findAll(): List<NotificationEvent> {
        logger.info("Finding all notifications, count: {}", notifications.size)
        return notifications.toList()
    }

    override fun findByAlarmId(alarmId: String): List<NotificationEvent> {
        logger.info("Finding notifications by alarm ID: {}", alarmId)
        return notifications.filter { it.alarmId == alarmId }
    }

    override fun findByStatus(status: String): List<NotificationEvent> {
        logger.info("Finding notifications by status: {}", status)
        return notifications.filter { it.status.equals(status, ignoreCase = true) }
    }

    override fun findByTimestampRange(
            fromTimestamp: Long,
            toTimestamp: Long
    ): List<NotificationEvent> {
        logger.info("Finding notifications between {} and {}", fromTimestamp, toTimestamp)
        return notifications.filter { it.timestamp in fromTimestamp..toTimestamp }
    }

    override fun count(): Long {
        return notifications.size.toLong()
    }

    override fun deleteAll() {
        notifications.clear()
        logger.info("All notifications deleted")
    }

    override fun deleteOlderThan(timestamp: Long): Int {
        val sizeBefore = notifications.size
        notifications.removeIf { it.timestamp < timestamp }
        val deleted = sizeBefore - notifications.size
        logger.info("Deleted {} notifications older than {}", deleted, timestamp)
        return deleted
    }
}
