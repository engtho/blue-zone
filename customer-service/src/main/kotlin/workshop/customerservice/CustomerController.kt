package workshop.customerservice

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import workshop.customerservice.dto.Customer

@Service
class CustomerService {
    private val logger = LoggerFactory.getLogger(CustomerService::class.java)
    private val customers =
            mutableMapOf(
                    "c-42" to Customer("c-42", "Ada Lovelace", "ada@example.com"),
                    "c-7" to Customer("c-7", "Grace Hopper", "grace@example.com")
            )

    fun findById(id: String): Customer {
        logger.info("Finding customer with auto reload - id: $id")
        return customers[id] ?: throw NoSuchElementException("Unknown customer: $id")
    }
}

@RestController
@RequestMapping("/api/customers")
class CustomerController(private val service: CustomerService) {
    @GetMapping("/{id}")
    fun byId(@PathVariable id: String): ResponseEntity<Customer> =
            try {
                ResponseEntity.ok(service.findById(id))
            } catch (e: NoSuchElementException) {
                ResponseEntity.notFound().build()
            }
}
