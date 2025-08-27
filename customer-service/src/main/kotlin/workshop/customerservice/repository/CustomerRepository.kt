package workshop.customerservice.repository

import workshop.customerservice.dto.Customer

/**
 * Repository interface for customer data access.
 *
 * This interface defines the contract for customer data operations. Follows the Repository pattern
 * for clean data access abstraction.
 */
interface CustomerRepository {

    /** Save a customer */
    fun save(customer: Customer): Customer

    /** Update an existing customer */
    fun update(customer: Customer): Customer

    /** Find customer by ID */
    fun findById(id: String): Customer?

    /** Find all customers */
    fun findAll(): List<Customer>

    /** Find customers by region */
    fun findByRegion(region: String): List<Customer>

    /** Find customers by priority */
    fun findByPriority(priority: Int): List<Customer>

    /** Find customers by service type */
    fun findByService(service: String): List<Customer>

    /** Check if customer exists */
    fun existsById(id: String): Boolean

    /** Count total customers */
    fun count(): Long

    /** Delete customer by ID */
    fun deleteById(id: String): Boolean

    /** Delete all customers (for testing) */
    fun deleteAll()
}
