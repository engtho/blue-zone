package workshop.ticketservice.client

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

/**
 * REST implementation of CustomerClient.
 *
 * This implements the CustomerClient interface using Spring's RestClient to communicate with the
 * customer service via HTTP/REST.
 */
@Service
class RestCustomerClient : CustomerClient {
    private val client = RestClient.create("http://customer-service:8080")
    private val log = LoggerFactory.getLogger(RestCustomerClient::class.java)

    override fun getCustomer(customerId: String): String? {
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

    override fun getCustomerOrDefault(customerId: String, defaultValue: String): String {
        return getCustomer(customerId) ?: defaultValue
    }

    override fun customerExists(customerId: String): Boolean {
        return try {
            client.get().uri("/api/customers/{id}", customerId).retrieve().toBodilessEntity()
            true
        } catch (e: Exception) {
            log.debug("Customer {} does not exist: {}", customerId, e.message)
            false
        }
    }
}
