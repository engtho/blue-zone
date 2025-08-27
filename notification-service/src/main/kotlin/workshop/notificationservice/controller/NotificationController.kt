package workshop.notificationservice.controller

import java.time.Instant
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import workshop.notificationservice.dto.NotificationEvent
import workshop.notificationservice.service.NotificationService

/**
 * REST Controller for notification operations.
 *
 * This controller handles HTTP requests and delegates business logic to the service layer.
 */
@RestController
@RequestMapping("/api/notifications")
class NotificationController(private val notificationService: NotificationService) {
    private val logger = LoggerFactory.getLogger(NotificationController::class.java)

    /** Get all notifications. GET /api/notifications */
    @GetMapping
    fun getAllNotifications(): ResponseEntity<List<NotificationEvent>> {
        logger.info("Request to get all notifications")

        return try {
            val notifications = notificationService.getAllNotifications()
            ResponseEntity.ok(notifications)
        } catch (e: Exception) {
            logger.error("Error getting all notifications", e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Get notifications by alarm ID. GET /api/notifications/alarm/{alarmId} */
    @GetMapping("/alarm/{alarmId}")
    fun getNotificationsByAlarmId(
            @PathVariable alarmId: String
    ): ResponseEntity<List<NotificationEvent>> {
        logger.info("Request to get notifications for alarm: {}", alarmId)

        return try {
            val notifications = notificationService.getNotificationsByAlarmId(alarmId)
            ResponseEntity.ok(notifications)
        } catch (e: Exception) {
            logger.error("Error getting notifications for alarm: {}", alarmId, e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Get notifications by status. GET /api/notifications/status/{status} */
    @GetMapping("/status/{status}")
    fun getNotificationsByStatus(
            @PathVariable status: String
    ): ResponseEntity<List<NotificationEvent>> {
        logger.info("Request to get notifications by status: {}", status)

        return try {
            val notifications = notificationService.getNotificationsByStatus(status)
            ResponseEntity.ok(notifications)
        } catch (e: IllegalArgumentException) {
            logger.warn("Invalid status: {}", status)
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            logger.error("Error getting notifications by status: {}", status, e)
            ResponseEntity.internalServerError().build()
        }
    }

    /**
     * Get notifications within a time range. GET
     * /api/notifications/range?from={timestamp}&to={timestamp}
     */
    @GetMapping("/range")
    fun getNotificationsByTimeRange(
            @RequestParam from: Long,
            @RequestParam to: Long
    ): ResponseEntity<List<NotificationEvent>> {
        logger.info("Request to get notifications between {} and {}", from, to)

        return try {
            val notifications = notificationService.getNotificationsByTimeRange(from, to)
            ResponseEntity.ok(notifications)
        } catch (e: IllegalArgumentException) {
            logger.warn("Invalid time range: from={}, to={}", from, to)
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            logger.error("Error getting notifications by time range", e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Get notifications from the last N hours. GET /api/notifications/recent?hours={hours} */
    @GetMapping("/recent")
    fun getRecentNotifications(
            @RequestParam(defaultValue = "24") hours: Int
    ): ResponseEntity<List<NotificationEvent>> {
        logger.info("Request to get notifications from last {} hours", hours)

        return try {
            val now = Instant.now().epochSecond
            val fromTimestamp = now - (hours * 3600) // hours to seconds
            val notifications = notificationService.getNotificationsByTimeRange(fromTimestamp, now)
            ResponseEntity.ok(notifications)
        } catch (e: Exception) {
            logger.error("Error getting recent notifications", e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Get notification count. GET /api/notifications/count */
    @GetMapping("/count")
    fun getNotificationCount(): ResponseEntity<Map<String, Long>> {
        logger.info("Request to get notification count")

        return try {
            val count = notificationService.getNotificationCount()
            ResponseEntity.ok(mapOf("count" to count))
        } catch (e: Exception) {
            logger.error("Error getting notification count", e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Clear all notifications (useful for testing). DELETE /api/notifications */
    @DeleteMapping
    fun clearAllNotifications(): ResponseEntity<Map<String, String>> {
        logger.info("Request to clear all notifications")

        return try {
            notificationService.clearAllNotifications()
            ResponseEntity.ok(mapOf("message" to "All notifications cleared"))
        } catch (e: Exception) {
            logger.error("Error clearing notifications", e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Clean up old notifications. DELETE /api/notifications/cleanup?olderThanHours={hours} */
    @DeleteMapping("/cleanup")
    fun cleanupOldNotifications(
            @RequestParam(defaultValue = "720") olderThanHours: Int
    ): ResponseEntity<Map<String, Any>> {
        logger.info("Request to cleanup notifications older than {} hours", olderThanHours)

        return try {
            val cutoffTimestamp = Instant.now().epochSecond - (olderThanHours * 3600)
            val deletedCount = notificationService.cleanupOldNotifications(cutoffTimestamp)
            ResponseEntity.ok(
                    mapOf(
                            "message" to "Cleanup completed",
                            "deletedCount" to deletedCount,
                            "cutoffHours" to olderThanHours
                    )
            )
        } catch (e: Exception) {
            logger.error("Error cleaning up old notifications", e)
            ResponseEntity.internalServerError().build()
        }
    }
}
