package workshop.alarmservice.service

import workshop.alarmservice.dto.AlarmEvent
import workshop.alarmservice.dto.AlarmRequest

/**
 * Service interface for alarm business operations.
 *
 * This interface defines the business operations available for alarms. It separates the business
 * logic contract from implementation details.
 */
interface AlarmService {

    /** Start a new alarm */
    fun startAlarm(request: AlarmRequest): AlarmEvent

    /** Stop/resolve an alarm */
    fun stopAlarm(request: AlarmRequest): AlarmEvent

    /** Get all alarm events */
    fun getAllAlarms(): List<AlarmEvent>

    /** Get alarm events by alarm ID */
    fun getAlarmEvents(alarmId: String): List<AlarmEvent>

    /** Get the current status of an alarm */
    fun getAlarmStatus(alarmId: String): String?

    /** Get alarms by service */
    fun getAlarmsByService(service: String): List<AlarmEvent>

    /** Get active alarms (not resolved) */
    fun getActiveAlarms(): List<AlarmEvent>
}
