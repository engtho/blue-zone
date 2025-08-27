package workshop.alarmservice.repository

import workshop.alarmservice.dto.AlarmEvent

/**
 * Repository interface for alarm event data access.
 *
 * This interface defines the contract for alarm event data operations. Follows the Repository
 * pattern for clean data access abstraction.
 */
interface AlarmRepository {

    /** Save an alarm event */
    fun save(alarmEvent: AlarmEvent): AlarmEvent

    /** Find all alarm events */
    fun findAll(): List<AlarmEvent>

    /** Find alarm events by alarm ID */
    fun findByAlarmId(alarmId: String): List<AlarmEvent>

    /** Find the latest alarm event for a given alarm ID */
    fun findLatestByAlarmId(alarmId: String): AlarmEvent?

    /** Find alarm events by service */
    fun findByService(service: String): List<AlarmEvent>

    /** Find alarm events by impact level */
    fun findByImpact(impact: String): List<AlarmEvent>

    /** Count total alarm events */
    fun count(): Long

    /** Delete all alarm events (for testing) */
    fun deleteAll()
}
