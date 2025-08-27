package workshop.alarmservice.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import workshop.alarmservice.dto.AlarmEvent
import workshop.alarmservice.dto.AlarmRequest
import workshop.alarmservice.service.AlarmService

@RestController
@RequestMapping("/api/alarms")
class AlarmController(private val alarmService: AlarmService) {

        @PostMapping("/start")
        fun start(@Valid @RequestBody req: AlarmRequest): ResponseEntity<AlarmEvent> {
                val event = alarmService.startAlarm(req)
                return ResponseEntity.ok(event)
        }

        @PostMapping("/stop")
        fun stop(@Valid @RequestBody req: AlarmRequest): ResponseEntity<AlarmEvent> {
                val event = alarmService.stopAlarm(req)
                return ResponseEntity.ok(event)
        }

        @GetMapping
        fun getAllAlarms(): ResponseEntity<List<AlarmEvent>> {
                return ResponseEntity.ok(alarmService.getAllAlarms())
        }

        @GetMapping("/{alarmId}")
        fun getAlarmEvents(@PathVariable alarmId: String): ResponseEntity<List<AlarmEvent>> {
                return ResponseEntity.ok(alarmService.getAlarmEvents(alarmId))
        }

        @GetMapping("/{alarmId}/status")
        fun getAlarmStatus(@PathVariable alarmId: String): ResponseEntity<Map<String, String?>> {
                val status = alarmService.getAlarmStatus(alarmId)
                return ResponseEntity.ok(mapOf("alarmId" to alarmId, "status" to status))
        }

        @GetMapping("/service/{service}")
        fun getAlarmsByService(@PathVariable service: String): ResponseEntity<List<AlarmEvent>> {
                return ResponseEntity.ok(alarmService.getAlarmsByService(service))
        }

        @GetMapping("/active")
        fun getActiveAlarms(): ResponseEntity<List<AlarmEvent>> {
                return ResponseEntity.ok(alarmService.getActiveAlarms())
        }
}
