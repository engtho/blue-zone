package workshop.alarmservice.dto

data class AlarmEvent(
        val alarmId: String,
        val service: String, // "BROADBAND", "MOBILE", "TV"
        val impact: String, // "OUTAGE", "DEGRADED", "SLOW"
        val affectedCustomers: List<String> = emptyList(),
        val timestamp: Long
)
