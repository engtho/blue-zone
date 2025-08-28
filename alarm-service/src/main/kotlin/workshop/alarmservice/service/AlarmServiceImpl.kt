package workshop.alarmservice.service

import java.time.Instant
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import workshop.alarmservice.dto.AlarmEvent
import workshop.alarmservice.dto.AlarmRequest
import workshop.alarmservice.publisher.AlarmEventPublisher
import workshop.alarmservice.repository.AlarmRepository

@Service
class AlarmServiceImpl(
        private val alarmRepository: AlarmRepository,
        private val publisher: AlarmEventPublisher
) : AlarmService {
        private val logger = LoggerFactory.getLogger(AlarmServiceImpl::class.java)

        override fun startAlarm(request: AlarmRequest): AlarmEvent {
                val event =
                        AlarmEvent(
                                alarmId = request.alarmId,
                                service = request.service,
                                impact = request.impact,
                                affectedCustomers = request.affectedCustomers,
                                timestamp = Instant.now().epochSecond
                        )

                val savedEvent = alarmRepository.save(event)
                publisher.publish(savedEvent)
                logger.info("Alarm started: {}", event)

                return savedEvent
        }

        override fun stopAlarm(request: AlarmRequest): AlarmEvent {
                val event =
                        AlarmEvent(
                                alarmId = request.alarmId,
                                service = request.service,
                                impact = "RESOLVED",
                                affectedCustomers = request.affectedCustomers,
                                timestamp = Instant.now().epochSecond
                        )

                val savedEvent = alarmRepository.save(event)
                publisher.publish(savedEvent)
                logger.info("Alarm stopped: {}", event)

                return savedEvent
        }

        override fun getAllAlarms(): List<AlarmEvent> = alarmRepository.findAll()

        override fun getAlarmEvents(alarmId: String): List<AlarmEvent> =
                alarmRepository.findByAlarmId(alarmId)

        override fun getAlarmStatus(alarmId: String): String? =
                alarmRepository.findLatestByAlarmId(alarmId)?.impact

        override fun getAlarmsByService(service: String): List<AlarmEvent> =
                alarmRepository.findByService(service)

        override fun getActiveAlarms(): List<AlarmEvent> {
                // Get latest event per alarm ID that's not resolved
                return alarmRepository
                        .findAll()
                        .groupBy { it.alarmId }
                        .mapNotNull { (_, events) -> events.maxByOrNull { it.timestamp } }
                        .filter { it.impact != "RESOLVED" }
        }
}
