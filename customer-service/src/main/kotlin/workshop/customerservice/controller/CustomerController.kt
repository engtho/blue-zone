package workshop.customerservice.controller

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import workshop.customerservice.dto.Customer
import workshop.customerservice.service.CustomerService

@RestController
@RequestMapping("/api/customers")
class CustomerController(private val customerService: CustomerService) {
    private val logger = LoggerFactory.getLogger(CustomerController::class.java)

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

    @PostMapping("/batch")
    fun getCustomersByIds(@RequestBody ids: List<String>): ResponseEntity<List<Customer>> {
        logger.info("Request to get customers by IDs: {}", ids)

        return try {
            val customers = customerService.getCustomersByIds(ids)
            ResponseEntity.ok(customers)
        } catch (e: Exception) {
            logger.error("Error getting customers by IDs: {}", ids, e)
            ResponseEntity.internalServerError().build()
        }
    }
}
