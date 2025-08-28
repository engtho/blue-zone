package workshop.alarmservice.service

import workshop.alarmservice.dto.AlarmEvent
import workshop.alarmservice.dto.AlarmRequest

interface AlarmService {

    fun startAlarm(request: AlarmRequest): AlarmEvent

    fun stopAlarm(request: AlarmRequest): AlarmEvent

    fun getAllAlarms(): List<AlarmEvent>

    fun getAlarmEvents(alarmId: String): List<AlarmEvent>

    fun getAlarmStatus(alarmId: String): String?

    fun getAlarmsByService(service: String): List<AlarmEvent>

    fun getActiveAlarms(): List<AlarmEvent>
}
