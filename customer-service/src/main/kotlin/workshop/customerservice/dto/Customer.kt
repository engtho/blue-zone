package workshop.customerservice.dto

data class Customer(
        val id: String,
        val name: String,
        val email: String,
        val phone: String,
        val services: List<String>, // BROADBAND, MOBILE, TV, VOIP
        val priority: Int, // 1 = kritisk (nødtjenester), 2 = standard
        val region: String
)
