package workshop.alarmservice.dto

import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class AlarmRequest(
        @field:NotBlank val alarmId: String = UUID.randomUUID().toString(),
        @field:NotBlank val service: String, // "BROADBAND", "MOBILE", "TV", "VOIP"
        @field:NotBlank val impact: String, // "OUTAGE", "DEGRADED", "SLOW"
        val affectedCustomers: List<String> = emptyList()
)
