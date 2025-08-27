package workshop.customerservice.repository

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import workshop.customerservice.dto.Customer

/**
 * In-memory implementation of CustomerRepository.
 *
 * This implementation uses a concurrent map for thread-safe access. In production, you would
 * typically use:
 * - JpaCustomerRepository (with Spring Data JPA)
 * - MongoCustomerRepository (with Spring Data MongoDB)
 * - RedisCustomerRepository (with Redis)
 */
@Repository
class InMemoryCustomerRepository : CustomerRepository {
    private val logger = LoggerFactory.getLogger(InMemoryCustomerRepository::class.java)

    private val customers =
            mutableMapOf(
                    "c-42" to
                            Customer(
                                    id = "c-42",
                                    name = "Ada Lovelace",
                                    email = "ada@example.com",
                                    phone = "+47 123 45 678",
                                    services = listOf("BROADBAND", "TV"),
                                    priority = 2,
                                    region = "Oslo"
                            ),
                    "c-7" to
                            Customer(
                                    id = "c-7",
                                    name = "Grace Hopper",
                                    email = "grace@example.com",
                                    phone = "+47 987 65 432",
                                    services = listOf("MOBILE", "BROADBAND"),
                                    priority = 2,
                                    region = "Bergen"
                            ),
                    "c-100" to
                            Customer(
                                    id = "c-100",
                                    name = "Oslo Universitetssykehus",
                                    email = "it@ous.no",
                                    phone = "+47 23 07 00 00",
                                    services = listOf("BROADBAND", "MOBILE", "TV", "VOIP"),
                                    priority = 1, // Kritisk kunde (nødtjenester)
                                    region = "Oslo"
                            ),
                    "c-200" to
                            Customer(
                                    id = "c-200",
                                    name = "Katherine Johnson",
                                    email = "katherine@example.com",
                                    phone = "+47 444 98 765",
                                    services = listOf("MOBILE"),
                                    priority = 2,
                                    region = "Stavanger"
                            ),
                    "c-300" to
                            Customer(
                                    id = "c-300",
                                    name = "Bergen Brannvesen",
                                    email = "it@bergen-brann.no",
                                    phone = "+47 55 56 81 10",
                                    services = listOf("BROADBAND", "MOBILE"),
                                    priority = 1, // Kritisk kunde (nødtjenester)
                                    region = "Bergen"
                            )
            )

    override fun save(customer: Customer): Customer {
        customers[customer.id] = customer
        logger.info("Customer saved: {}", customer.id)
        return customer
    }

    override fun update(customer: Customer): Customer {
        if (customers.containsKey(customer.id)) {
            customers[customer.id] = customer
            logger.info("Customer updated: {}", customer.id)
            return customer
        } else {
            logger.warn("Customer not found for update: {}", customer.id)
            throw NoSuchElementException("Customer not found: ${customer.id}")
        }
    }

    override fun findById(id: String): Customer? {
        logger.info("Finding customer with id: {}", id)
        return customers[id]
    }

    override fun findAll(): List<Customer> = customers.values.toList()

    override fun findByRegion(region: String): List<Customer> =
            customers.values.filter { it.region.equals(region, ignoreCase = true) }

    override fun findByPriority(priority: Int): List<Customer> =
            customers.values.filter { it.priority == priority }

    override fun findByService(service: String): List<Customer> =
            customers.values.filter { it.services.contains(service.uppercase()) }

    override fun existsById(id: String): Boolean = customers.containsKey(id)

    override fun count(): Long = customers.size.toLong()

    override fun deleteById(id: String): Boolean {
        val removed = customers.remove(id) != null
        if (removed) {
            logger.info("Customer deleted: {}", id)
        } else {
            logger.warn("Customer not found for deletion: {}", id)
        }
        return removed
    }

    override fun deleteAll() {
        customers.clear()
        logger.info("All customers deleted")
    }
}
