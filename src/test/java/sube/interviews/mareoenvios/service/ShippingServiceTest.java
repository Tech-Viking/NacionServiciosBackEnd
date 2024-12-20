package sube.interviews.mareoenvios.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.model.ShippingClient;
import sube.interviews.mareoenvios.repository.ShippingRepository;
import sube.interviews.mareoenvios.repository.TaskRepository;

class ShippingServiceTest {

    private ShippingService shippingService;

    @Mock
    private ShippingRepository shippingRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ShippingClient shippingClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shippingService = new ShippingService(shippingRepository, taskRepository, shippingClient);
    }

    @Test
    void testGetShippingById_ShippingExists() {
        // Arrange
        Integer shippingId = 1;
        Shipping mockShipping = new Shipping();
        when(shippingRepository.findByIdWithItems(shippingId)).thenReturn(Optional.of(mockShipping));

        // Act
        Optional<Shipping> result = shippingService.getShippingById(shippingId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockShipping, result.get());
        verify(shippingRepository, times(1)).findByIdWithItems(shippingId);
    }

    @Test
    void testGetShippingById_ShippingDoesNotExist() {
        // Arrange
        Integer shippingId = 1;
        when(shippingRepository.findByIdWithItems(shippingId)).thenReturn(Optional.empty());

        // Act
        Optional<Shipping> result = shippingService.getShippingById(shippingId);

        // Assert
        assertFalse(result.isPresent());
        verify(shippingRepository, times(1)).findByIdWithItems(shippingId);
    }

    @Test
    void testUpdateShippingState_ValidTransition() {
        // 
        Integer shippingId = 1;
        String currentState = "Inicial";
        String newState = "Entregado al correo";
        Shipping mockShipping = new Shipping();
        mockShipping.setId(shippingId);
        mockShipping.setState(currentState);

        when(shippingRepository.findById(shippingId)).thenReturn(Optional.of(mockShipping));

        shippingService.updateShippingState(shippingId, newState);

        assertEquals(newState, mockShipping.getState());
        verify(shippingRepository, times(1)).save(mockShipping);
        verify(taskRepository, times(1)).recordSuccessfulTask(shippingId.longValue(), newState);
        verify(shippingClient, times(1)).sendCommand(mockShipping, newState);
    }

    @Test
    void testUpdateShippingState_InvalidTransition() {
        // Arrange
        Integer shippingId = 1;
        String currentState = "Entregado";
        String newState = "En camino";
        Shipping mockShipping = new Shipping();
        mockShipping.setId(shippingId);
        mockShipping.setState(currentState);

        when(shippingRepository.findById(shippingId)).thenReturn(Optional.of(mockShipping));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            shippingService.updateShippingState(shippingId, newState);
        });

        assertEquals("400 BAD_REQUEST \"Transición de estado inválida de Entregado a En camino\"", exception.getMessage());
        verify(shippingRepository, never()).save(any());
        verify(taskRepository, never()).recordSuccessfulTask(anyLong(), anyString());
        verify(shippingClient, never()).sendCommand(any(), anyString());
    }

    @Test
    void testUpdateShippingState_ShippingNotFound() {
        // Arrange
        Integer shippingId = 1;
        String newState = "En camino";

        when(shippingRepository.findById(shippingId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            shippingService.updateShippingState(shippingId, newState);
        });

        assertEquals("404 NOT_FOUND \"Shipping not found with id: 1\"", exception.getMessage());
        verify(shippingRepository, times(1)).findById(shippingId);
        verify(shippingRepository, never()).save(any());
    }

    @Test
    void testUpdateShippingState_ExternalServiceError() {
        Integer shippingId = 1;
        String currentState = "Inicial";
        String newState = "Entregado al correo";
        Shipping mockShipping = new Shipping();
        mockShipping.setId(shippingId);
        mockShipping.setState(currentState);

        when(shippingRepository.findById(shippingId)).thenReturn(Optional.of(mockShipping));
        doThrow(new RuntimeException("External service error")).when(shippingClient).sendCommand(mockShipping, newState);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            shippingService.updateShippingState(shippingId, newState);
        });

        assertEquals("500 INTERNAL_SERVER_ERROR \"Error al hacer la petición al servicio externo: External service error\"", exception.getMessage());
        verify(taskRepository, times(1)).recordTaskError(shippingId.longValue(),
                "Error al hacer la petición al servicio externo: External service error");
    }
}
