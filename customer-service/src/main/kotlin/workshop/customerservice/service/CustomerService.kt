package workshop.customerservice.service

import workshop.customerservice.dto.Customer

/**
 * Service interface for customer operations.
 *
 * This interface defines the business logic layer for customer management. It encapsulates complex
 * operations and provides a clean API for controllers.
 *
 * Benefits of using this interface:
 * - Testability: Easy to mock for unit tests
 * - Flexibility: Can swap implementations (caching, validation, etc.)
 * - Separation of concerns: Business logic separate from data access
 */
interface CustomerService {

    /**
     * Create a new customer.
     * @param customer The customer to create
     * @return The created customer
     * @throws IllegalArgumentException if customer data is invalid
     */
    fun createCustomer(customer: Customer): Customer

    /**
     * Update an existing customer.
     * @param customer The customer to update
     * @return The updated customer
     * @throws NoSuchElementException if customer doesn't exist
     */
    fun updateCustomer(customer: Customer): Customer

    /**
     * Find a customer by ID.
     * @param id The customer ID
     * @return The customer if found, null otherwise
     */
    fun getCustomer(id: String): Customer?

    /**
     * Get all customers.
     * @return List of all customers
     */
    fun getAllCustomers(): List<Customer>

    /**
     * Find customers by region.
     * @param region The region to search in
     * @return List of customers in the region
     */
    fun getCustomersByRegion(region: String): List<Customer>

    /**
     * Find customers by priority level.
     * @param priority The priority level (1-5, where 1 is highest)
     * @return List of customers with the specified priority
     */
    fun getCustomersByPriority(priority: Int): List<Customer>

    /**
     * Find high priority customers (priority 1). Useful for emergency services and critical
     * infrastructure.
     * @return List of high priority customers
     */
    fun getHighPriorityCustomers(): List<Customer>

    /**
     * Find customers using a specific service.
     * @param service The service type (BROADBAND, MOBILE, TV, VOIP)
     * @return List of customers using the service
     */
    fun getCustomersByService(service: String): List<Customer>

    /**
     * Check if a customer exists.
     * @param id The customer ID
     * @return true if customer exists, false otherwise
     */
    fun customerExists(id: String): Boolean

    /**
     * Get total number of customers.
     * @return Customer count
     */
    fun getCustomerCount(): Long

    /**
     * Delete a customer by ID.
     * @param id The customer ID
     * @return true if customer was deleted, false if not found
     */
    fun deleteCustomer(id: String): Boolean
}
