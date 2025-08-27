package workshop.alarmservice.repository

import java.util.Collections
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import workshop.alarmservice.dto.AlarmEvent

/**
 * In-memory implementation of AlarmRepository.
 *
 * This implementation stores alarm events in memory using thread-safe collections. In production,
 * you would typically use:
 * - JpaAlarmRepository (with Spring Data JPA)
 * - MongoAlarmRepository (with Spring Data MongoDB)
 * - TimeSeriesAlarmRepository (for time-series databases)
 */
@Repository
class InMemoryAlarmRepository : AlarmRepository {
    private val log = LoggerFactory.getLogger(InMemoryAlarmRepository::class.java)

    // Thread-safe list for concurrent access
    private val alarmEvents = Collections.synchronizedList(mutableListOf<AlarmEvent>())

    override fun save(alarmEvent: AlarmEvent): AlarmEvent {
        alarmEvents.add(alarmEvent)
        log.info("Alarm event saved: {}", alarmEvent.alarmId)
        return alarmEvent
    }

    override fun findAll(): List<AlarmEvent> = alarmEvents.toList()

    override fun findByAlarmId(alarmId: String): List<AlarmEvent> =
            alarmEvents.filter { it.alarmId == alarmId }

    override fun findLatestByAlarmId(alarmId: String): AlarmEvent? =
            alarmEvents.filter { it.alarmId == alarmId }.maxByOrNull { it.timestamp }

    override fun findByService(service: String): List<AlarmEvent> =
            alarmEvents.filter { it.service == service }

    override fun findByImpact(impact: String): List<AlarmEvent> =
            alarmEvents.filter { it.impact == impact }

    override fun count(): Long = alarmEvents.size.toLong()

    override fun deleteAll() {
        alarmEvents.clear()
        log.info("All alarm events deleted")
    }
}
