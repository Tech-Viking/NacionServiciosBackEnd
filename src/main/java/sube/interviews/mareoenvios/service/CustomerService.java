package sube.interviews.mareoenvios.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
	        try{
	            Optional<Customer> customer = customerRepository.findById(customerId);
	            if (customer.isPresent()){
	               return customer;
	            } else {
	                logger.error("Cliente no encontrado con el id " + customerId);
	                return Optional.empty();
	             }
	         } catch (Exception e) {
	             logger.error("Error al intentar obtener el cliente con el id " + customerId, e);
	             return Optional.empty();
	        }
	    }

}
