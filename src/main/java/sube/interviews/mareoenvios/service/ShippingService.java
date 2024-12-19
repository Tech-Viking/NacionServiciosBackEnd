package sube.interviews.mareoenvios.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.repository.ShippingRepository;
import sube.interviews.mareoenvios.repository.TaskRepository;

@Service
public class ShippingService {

	private static final Logger logger = LoggerFactory.getLogger(ShippingService.class);
	private final ShippingRepository shippingRepository;
	private final TaskRepository taskRepository;
	private final ShippingClient shippingClient;

	public ShippingService(ShippingRepository shippingRepository, TaskRepository taskRepository,
			ShippingClient shippingClient) {
		this.shippingRepository = shippingRepository;
		this.taskRepository = taskRepository;
		this.shippingClient = shippingClient;

	}

	public Optional<Shipping> getShippingById(Integer shippingId) {
		try {
			Optional<Shipping> shipping = shippingRepository.findByIdWithItems(shippingId);
			if (shipping.isPresent()) {
				return shipping;
			} else {
				logger.error("Envío no encontrado con el id " + shippingId);
				return Optional.empty();
			}
		} catch (Exception e) {
			logger.error("Error al intentar obtener el envío con el id " + shippingId, e);
			return Optional.empty();
		}

	}

	public void updateShippingState(Integer shippingId, String newState) {
		try {
			Shipping shipping = shippingRepository.findById(shippingId).orElseThrow(() -> {
				logger.error("Shipping not found with id: " + shippingId);
				return new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipping not found with id: " + shippingId);
			});
			if (isValidTransition(shipping.getState(), newState)) {
				shipping.setState(newState);
				shippingRepository.save(shipping);
				taskRepository.recordSuccessfulTask(shipping.getId().longValue(), newState);
				// shippingClient.sendCommand(shipping, newState); // lo comento por ahora
			} else {
				logger.error("Transición de estado invalida de " + shipping.getState() + " a " + newState);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Transición de estado inválida de " + shipping.getState() + " a " + newState);
			}
		} catch (ResourceAccessException e) {
			logger.error("Error al hacer la petición al servicio externo", e);
			taskRepository.recordTaskError(shippingId.longValue(),
					"Error al hacer la petición al servicio externo: " + e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al hacer la petición al servicio externo: " + e.getMessage());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Error al actualizar el estado del envío", e);
			taskRepository.recordTaskError(shippingId.longValue(),
					"Error al actualizar el estado del envío: " + e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al actualizar el estado del envío: " + e.getMessage());
		}
	}

	private boolean isValidTransition(String currentState, String newState) {
		switch (currentState) {
		case "Inicial":
			return newState.equals("Entregado al correo") || newState.equals("Cancelado");
		case "Entregado al correo":
			return newState.equals("En camino") || newState.equals("Cancelado");
		case "En camino":
			return newState.equals("Entregado") || newState.equals("Cancelado");
		case "Entregado":
		case "Cancelado":
			return false;
		default:
			return false;
		}
	}
}
