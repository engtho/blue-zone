package workshop.alarmservice.dto

import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class AlarmRequest(@field:NotBlank val alarmId: String = UUID.randomUUID().toString())
