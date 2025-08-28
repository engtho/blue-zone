package workshop.customerservice.repository

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import workshop.customerservice.dto.Customer

@Repository
class StaticCustomerRepository : CustomerRepository {
        private val logger = LoggerFactory.getLogger(StaticCustomerRepository::class.java)

        private val customers =
                mapOf(
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
                                        priority = 1, // Hospital - emergency services
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
                                        priority = 1, // Fire department - emergency services
                                        region = "Bergen"
                                )
                )

        override fun findById(id: String): Customer? {
                logger.info("Finding customer with id: {}", id)
                return customers[id]
        }

        override fun findAllByIds(ids: List<String>): List<Customer> {
                logger.info("Finding customers with ids: {}", ids)
                return ids.mapNotNull { customers[it] }
        }
}
