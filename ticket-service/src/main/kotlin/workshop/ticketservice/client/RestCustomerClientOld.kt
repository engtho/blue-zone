package workshop.ticketservice

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

/**
 * Client service for communicating with the customer service.
 *
 * This encapsulates all customer service API calls and provides a clean interface for other
 * services to use. This is a common pattern in microservices architecture.
 */
@Service
class CustomerClient {
    private val client = RestClient.create("http://customer-service:8080")
    private val log = LoggerFactory.getLogger(CustomerClient::class.java)

    /**
     * Get customer information by ID
     * @param customerId the customer ID to look up
     * @return customer data as string, or null if not found or error occurred
     */
    fun getCustomer(customerId: String): String? {
        return try {
            val response =
                    client.get()
                            .uri("/api/customers/{id}", customerId)
                            .retrieve()
                            .toEntity(String::class.java)

            log.info("Retrieved customer data for {}: {}", customerId, response.body)
            response.body
        } catch (e: Exception) {
            log.warn("Could not retrieve customer data for {}: {}", customerId, e.message)
            null
        }
    }

    /**
     * Get customer information by ID with default fallback
     * @param customerId the customer ID to look up
     * @param defaultValue the value to return if customer not found
     * @return customer data as string, or the default value
     */
    fun getCustomerOrDefault(customerId: String, defaultValue: String): String {
        return getCustomer(customerId) ?: defaultValue
    }
}
