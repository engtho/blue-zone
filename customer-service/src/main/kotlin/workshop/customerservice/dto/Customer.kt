package workshop.customerservice.dto

data class Customer(
        val id: String,
        val name: String,
        val email: String,
        val phone: String,
        val services: List<String>,
        val priority: Int,
        val region: String
)
