package workshop.customerservice.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import workshop.customerservice.dto.Customer
import workshop.customerservice.repository.CustomerRepository

/**
 * Implementation of CustomerService.
 *
 * This service encapsulates business logic for customer operations. It validates input, handles
 * business rules, and coordinates with the repository.
 *
 * Educational notes:
 * - @Service annotation marks this as a Spring service component
 * - Constructor injection used for dependency injection (preferred over field injection)
 * - Business validation logic is separate from data persistence
 * - Logging provides observability into service operations
 */
@Service
class CustomerServiceImpl(private val customerRepository: CustomerRepository) : CustomerService {

    private val logger = LoggerFactory.getLogger(CustomerServiceImpl::class.java)

    override fun createCustomer(customer: Customer): Customer {
        logger.info("Creating customer: {}", customer.id)

        // Business validation
        validateCustomer(customer)

        // Check if customer already exists
        if (customerRepository.existsById(customer.id)) {
            throw IllegalArgumentException("Customer with ID ${customer.id} already exists")
        }

        return customerRepository.save(customer)
    }

    override fun updateCustomer(customer: Customer): Customer {
        logger.info("Updating customer: {}", customer.id)

        // Business validation
        validateCustomer(customer)

        return customerRepository.update(customer)
    }

    override fun getCustomer(id: String): Customer? {
        logger.info("Fetching customer: {}", id)
        return customerRepository.findById(id)
    }

    override fun getAllCustomers(): List<Customer> {
        logger.info("Fetching all customers")
        return customerRepository.findAll()
    }

    override fun getCustomersByRegion(region: String): List<Customer> {
        logger.info("Fetching customers by region: {}", region)
        return customerRepository.findByRegion(region)
    }

    override fun getCustomersByPriority(priority: Int): List<Customer> {
        logger.info("Fetching customers by priority: {}", priority)
        return customerRepository.findByPriority(priority)
    }

    override fun getHighPriorityCustomers(): List<Customer> {
        logger.info("Fetching high priority customers")
        return customerRepository.findByPriority(1)
    }

    override fun getCustomersByService(service: String): List<Customer> {
        logger.info("Fetching customers by service: {}", service)

        // Validate service type
        val validServices = setOf("BROADBAND", "MOBILE", "TV", "VOIP")
        if (!validServices.contains(service.uppercase())) {
            throw IllegalArgumentException(
                    "Invalid service type: $service. Valid types: $validServices"
            )
        }

        return customerRepository.findByService(service)
    }

    override fun customerExists(id: String): Boolean {
        return customerRepository.existsById(id)
    }

    override fun getCustomerCount(): Long {
        return customerRepository.count()
    }

    override fun deleteCustomer(id: String): Boolean {
        logger.info("Deleting customer: {}", id)
        return customerRepository.deleteById(id)
    }

    /**
     * Business validation for customer data. In a real application, this might include:
     * - Email format validation
     * - Phone number format validation
     * - Region validation against known regions
     * - Service validation against available services
     */
    private fun validateCustomer(customer: Customer) {
        if (customer.id.isBlank()) {
            throw IllegalArgumentException("Customer ID cannot be blank")
        }
        if (customer.name.isBlank()) {
            throw IllegalArgumentException("Customer name cannot be blank")
        }
        if (customer.email.isBlank() || !customer.email.contains("@")) {
            throw IllegalArgumentException("Valid email is required")
        }
        if (customer.priority < 1 || customer.priority > 5) {
            throw IllegalArgumentException("Priority must be between 1 and 5")
        }
        if (customer.services.isEmpty()) {
            throw IllegalArgumentException("Customer must have at least one service")
        }

        // Validate service types
        val validServices = setOf("BROADBAND", "MOBILE", "TV", "VOIP")
        customer.services.forEach { service ->
            if (!validServices.contains(service.uppercase())) {
                throw IllegalArgumentException(
                        "Invalid service: $service. Valid services: $validServices"
                )
            }
        }
    }
}
