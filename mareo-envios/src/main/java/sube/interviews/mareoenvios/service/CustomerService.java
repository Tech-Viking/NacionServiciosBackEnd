package sube.interviews.mareoenvios.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.repository.CustomerRepository;

@Service
public class CustomerService {
	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public Optional<Customer> getCustomerById(Integer customerId) {
		logger.info("Getting customer with id: {}", customerId);
		return customerRepository.findById(customerId).map(customer -> {
			logger.info("Customer found with id: {}", customerId);
			return Optional.of(customer);
		}).orElseThrow(() -> {
			logger.error("Customer not found with id: {}", customerId);
			return new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + customerId);
		});
	}
}