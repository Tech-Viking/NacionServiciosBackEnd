package sube.interviews.mareoenvios.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.model.TaskRequest;
import sube.interviews.mareoenvios.model.TaskStatusResponse;
import sube.interviews.mareoenvios.model.TopProductResponse;
import sube.interviews.mareoenvios.service.CustomerService;
import sube.interviews.mareoenvios.service.ReportService;
import sube.interviews.mareoenvios.service.ShippingService;
import sube.interviews.mareoenvios.service.TaskService;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private ReportService reportService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private ShippingService shippingService;

    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void testGetStatus_ReturnsStatus() throws Exception {
        TaskStatusResponse mockResponse = new TaskStatusResponse();
        when(taskService.getStatus()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Processing"))
                .andExpect(jsonPath("$.completedTasks").value(5))
                .andExpect(jsonPath("$.totalTasks").value(10));
    }


    @Test
    void testGetTopSentProducts_ReturnsProducts() throws Exception {
        TopProductResponse product1 = new TopProductResponse(1, "Product1", 100);
        TopProductResponse product2 = new TopProductResponse(2, "Product2", 80);
        when(reportService.getTopSentProducts()).thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/api/reports/top-sent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].productName").value("Product1"))
                .andExpect(jsonPath("$[0].quantity").value(100))
                .andExpect(jsonPath("$[1].productId").value(2))
                .andExpect(jsonPath("$[1].productName").value("Product2"))
                .andExpect(jsonPath("$[1].quantity").value(80));
    }

    @Test
    void testGetCustomerById_CustomerExists() throws Exception {
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1);
        mockCustomer.setFirstName("John Doe");
        when(customerService.getCustomerById(1)).thenReturn(Optional.of(mockCustomer));

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testGetCustomerById_CustomerNotFound() throws Exception {
        when(customerService.getCustomerById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente no encontrado"));
    }

    @Test
    void testGetShippingById_ShippingExists() throws Exception {
        Shipping mockShipping = new Shipping();
        mockShipping.setId(1);
        mockShipping.setState("Inicial");
        when(shippingService.getShippingById(1)).thenReturn(Optional.of(mockShipping));

        mockMvc.perform(get("/api/shippings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.state").value("Inicial"));
    }

    @Test
    void testGetShippingById_ShippingNotFound() throws Exception {
        when(shippingService.getShippingById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/shippings/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Envío no encontrado"));
    }

    @Test
    void testUpdateShippingState_ValidRequest() throws Exception {
        String newState = "En camino";

        mockMvc.perform(patch("/api/shippings/1")
                .contentType("application/json")
                .content("\"" + newState + "\""))
                .andExpect(status().isOk())
                .andExpect(content().string("Shipping state updated"));

        verify(shippingService, times(1)).updateShippingState(1, newState);
    }
}

