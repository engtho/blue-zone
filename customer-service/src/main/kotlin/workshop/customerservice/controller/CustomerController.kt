package workshop.customerservice.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import workshop.customerservice.dto.Customer
import workshop.customerservice.service.CustomerService

/**
 * REST Controller for customer operations.
 *
 * This controller handles HTTP requests and delegates business logic to the service layer.
 *
 * Educational notes:
 * - @RestController combines @Controller and @ResponseBody
 * - ResponseEntity provides full control over HTTP response
 * - Exception handling with proper HTTP status codes
 * - Clean separation between HTTP layer and business logic
 */
@RestController
@RequestMapping("/api/customers")
class CustomerController(private val customerService: CustomerService) {
    private val logger = LoggerFactory.getLogger(CustomerController::class.java)

    /** Get customer by ID. GET /api/customers/{id} */
    @GetMapping("/{id}")
    fun getCustomerById(@PathVariable id: String): ResponseEntity<Customer> {
        logger.info("Request to get customer: {}", id)

        return try {
            val customer = customerService.getCustomer(id)
            if (customer != null) {
                ResponseEntity.ok(customer)
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: Exception) {
            logger.error("Error getting customer: {}", id, e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Get all customers. GET /api/customers */
    @GetMapping
    fun getAllCustomers(): ResponseEntity<List<Customer>> {
        logger.info("Request to get all customers")

        return try {
            val customers = customerService.getAllCustomers()
            ResponseEntity.ok(customers)
        } catch (e: Exception) {
            logger.error("Error getting all customers", e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Get customers by region. GET /api/customers/region/{region} */
    @GetMapping("/region/{region}")
    fun getCustomersByRegion(@PathVariable region: String): ResponseEntity<List<Customer>> {
        logger.info("Request to get customers by region: {}", region)

        return try {
            val customers = customerService.getCustomersByRegion(region)
            ResponseEntity.ok(customers)
        } catch (e: Exception) {
            logger.error("Error getting customers by region: {}", region, e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Get customers by priority. GET /api/customers/priority/{priority} */
    @GetMapping("/priority/{priority}")
    fun getCustomersByPriority(@PathVariable priority: Int): ResponseEntity<List<Customer>> {
        logger.info("Request to get customers by priority: {}", priority)

        return try {
            val customers = customerService.getCustomersByPriority(priority)
            ResponseEntity.ok(customers)
        } catch (e: Exception) {
            logger.error("Error getting customers by priority: {}", priority, e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Get high priority customers (priority 1). GET /api/customers/high-priority */
    @GetMapping("/high-priority")
    fun getHighPriorityCustomers(): ResponseEntity<List<Customer>> {
        logger.info("Request to get high priority customers")

        return try {
            val customers = customerService.getHighPriorityCustomers()
            ResponseEntity.ok(customers)
        } catch (e: Exception) {
            logger.error("Error getting high priority customers", e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Get customers by service type. GET /api/customers/service/{service} */
    @GetMapping("/service/{service}")
    fun getCustomersByService(@PathVariable service: String): ResponseEntity<List<Customer>> {
        logger.info("Request to get customers by service: {}", service)

        return try {
            val customers = customerService.getCustomersByService(service)
            ResponseEntity.ok(customers)
        } catch (e: IllegalArgumentException) {
            logger.warn("Invalid service type: {}", service)
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            logger.error("Error getting customers by service: {}", service, e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Create a new customer. POST /api/customers */
    @PostMapping
    fun createCustomer(@RequestBody customer: Customer): ResponseEntity<Customer> {
        logger.info("Request to create customer: {}", customer.id)

        return try {
            val createdCustomer = customerService.createCustomer(customer)
            ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer)
        } catch (e: IllegalArgumentException) {
            logger.warn("Invalid customer data: {}", e.message)
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            logger.error("Error creating customer: {}", customer.id, e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Update an existing customer. PUT /api/customers/{id} */
    @PutMapping("/{id}")
    fun updateCustomer(
            @PathVariable id: String,
            @RequestBody customer: Customer
    ): ResponseEntity<Customer> {
        logger.info("Request to update customer: {}", id)

        return try {
            // Ensure the path ID matches the customer ID
            val customerToUpdate = customer.copy(id = id)
            val updatedCustomer = customerService.updateCustomer(customerToUpdate)
            ResponseEntity.ok(updatedCustomer)
        } catch (e: NoSuchElementException) {
            logger.warn("Customer not found for update: {}", id)
            ResponseEntity.notFound().build()
        } catch (e: IllegalArgumentException) {
            logger.warn("Invalid customer data: {}", e.message)
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            logger.error("Error updating customer: {}", id, e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Delete a customer. DELETE /api/customers/{id} */
    @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable id: String): ResponseEntity<Void> {
        logger.info("Request to delete customer: {}", id)

        return try {
            val deleted = customerService.deleteCustomer(id)
            if (deleted) {
                ResponseEntity.noContent().build()
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: Exception) {
            logger.error("Error deleting customer: {}", id, e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Get customer count. GET /api/customers/count */
    @GetMapping("/count")
    fun getCustomerCount(): ResponseEntity<Map<String, Long>> {
        logger.info("Request to get customer count")

        return try {
            val count = customerService.getCustomerCount()
            ResponseEntity.ok(mapOf("count" to count))
        } catch (e: Exception) {
            logger.error("Error getting customer count", e)
            ResponseEntity.internalServerError().build()
        }
    }

    /** Check if customer exists. HEAD /api/customers/{id} */
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.HEAD])
    fun customerExists(@PathVariable id: String): ResponseEntity<Void> {
        logger.info("Request to check if customer exists: {}", id)

        return try {
            val exists = customerService.customerExists(id)
            if (exists) {
                ResponseEntity.ok().build()
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: Exception) {
            logger.error("Error checking customer existence: {}", id, e)
            ResponseEntity.internalServerError().build()
        }
    }
}
