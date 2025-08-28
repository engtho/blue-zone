package workshop.customerservice.service

import workshop.customerservice.dto.Customer

interface CustomerService {

    fun getCustomer(id: String): Customer?

    fun getCustomersByIds(ids: List<String>): List<Customer>
}
