package workshop.customerservice.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import workshop.customerservice.dto.Customer
import workshop.customerservice.repository.CustomerRepository

@Service
class CustomerServiceImpl(private val customerRepository: CustomerRepository) : CustomerService {

    private val logger = LoggerFactory.getLogger(CustomerServiceImpl::class.java)

    override fun getCustomer(id: String): Customer? {
        logger.info("Fetching customer: {}", id)
        return customerRepository.findById(id)
    }

    override fun getCustomersByIds(ids: List<String>): List<Customer> {
        logger.info("Fetching customers by IDs: {}", ids)
        return customerRepository.findAllByIds(ids)
    }
}
