package workshop.customerservice.repository

import workshop.customerservice.dto.Customer

interface CustomerRepository {
    fun findById(id: String): Customer?
    fun findAllByIds(ids: List<String>): List<Customer>
}
