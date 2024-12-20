package sube.interviews.mareoenvios.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sube.interviews.mareoenvios.model.TopProductResponse;
import sube.interviews.mareoenvios.repository.ShippingItemRepository;

class ReportServiceTest {

    private ReportService reportService;

    @Mock
    private ShippingItemRepository shippingItemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportService = new ReportService(shippingItemRepository);
    }

    @Test
    void testGetTopSentProducts_ReturnsTopProducts() {

        List<Object[]> mockResults = Arrays.asList(
            new Object[]{1, "Product A", 100},
            new Object[]{2, "Product B", 80},
            new Object[]{3, "Product C", 60}
        );
        when(shippingItemRepository.findTop3Products()).thenReturn(mockResults);


        List<TopProductResponse> result = reportService.getTopSentProducts();


        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getProductId());
        assertEquals("Product A", result.get(0).getDescription());
        assertEquals(100, result.get(0).getTotalCount());
        assertEquals(2, result.get(1).getProductId());
        assertEquals(3, result.get(2).getProductId());


        verify(shippingItemRepository, times(1)).findTop3Products();
    }

    @Test
    void testGetTopSentProducts_ReturnsEmptyListWhenNoResults() {

        when(shippingItemRepository.findTop3Products()).thenReturn(Arrays.asList());

        // Act: 
        List<TopProductResponse> result = reportService.getTopSentProducts();

        // Assert: 
        assertTrue(result.isEmpty());

        verify(shippingItemRepository, times(1)).findTop3Products();
    }
}
