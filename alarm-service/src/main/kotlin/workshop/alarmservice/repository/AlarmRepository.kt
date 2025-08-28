package workshop.alarmservice.repository

import workshop.alarmservice.dto.AlarmEvent

interface AlarmRepository {

    fun save(alarmEvent: AlarmEvent): AlarmEvent

    fun findAll(): List<AlarmEvent>

    fun findByAlarmId(alarmId: String): List<AlarmEvent>

    fun findLatestByAlarmId(alarmId: String): AlarmEvent?

    fun findByService(service: String): List<AlarmEvent>

    fun findByImpact(impact: String): List<AlarmEvent>

    fun count(): Long

    fun deleteAll()
}
