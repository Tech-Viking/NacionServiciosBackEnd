package sube.interviews.mareoenvios.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.repository.CustomerRepository;

class CustomerServiceTest {

    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void testGetCustomerById_WhenCustomerExists() {
        // Arrange
        Integer customerId = 1;
        Customer mockCustomer = new Customer(); // Crea una instancia mock de Customer
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));

        // Act
        Optional<Customer> result = customerService.getCustomerById(customerId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockCustomer, result.get());
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void testGetCustomerById_WhenCustomerDoesNotExist() {
        // Arrange
        Integer customerId = 1;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act
        Optional<Customer> result = customerService.getCustomerById(customerId);

        // Assert
        assertFalse(result.isPresent());
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void testGetCustomerById_WhenExceptionOccurs() {
        // Arrange
        Integer customerId = 1;
        when(customerRepository.findById(customerId)).thenThrow(new RuntimeException("Database error"));

        // Act
        Optional<Customer> result = customerService.getCustomerById(customerId);

        // Assert
        assertFalse(result.isPresent());
        verify(customerRepository, times(1)).findById(customerId);
    }
}
