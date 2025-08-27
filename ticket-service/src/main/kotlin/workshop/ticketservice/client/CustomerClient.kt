package workshop.ticketservice.client

/**
 * Interface for customer service client operations.
 *
 * This abstraction allows for different implementations:
 * - REST client (current)
 * - GraphQL client
 * - gRPC client
 * - Mock client for testing
 */
interface CustomerClient {

    /**
     * Get customer information by ID
     * @param customerId the customer ID to look up
     * @return customer data as string, or null if not found
     */
    fun getCustomer(customerId: String): String?

    /**
     * Get customer information with fallback
     * @param customerId the customer ID to look up
     * @param defaultValue fallback value if customer not found
     * @return customer data or default value
     */
    fun getCustomerOrDefault(customerId: String, defaultValue: String): String

    /**
     * Check if customer exists
     * @param customerId the customer ID to check
     * @return true if customer exists, false otherwise
     */
    fun customerExists(customerId: String): Boolean
}
