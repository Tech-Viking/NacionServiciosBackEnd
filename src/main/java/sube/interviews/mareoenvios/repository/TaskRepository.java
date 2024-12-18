package sube.interviews.mareoenvios.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import sube.interviews.mareoenvios.entity.TaskShipping;
import sube.interviews.mareoenvios.model.ShipmentStatus;
import sube.interviews.mareoenvios.model.TaskStatusResponse;

@Component
public class TaskRepository {

	private static final Logger logger = LoggerFactory.getLogger(TaskRepository.class);
	private final TaskShippingRepository taskShippingRepository;

	public TaskRepository(TaskShippingRepository taskShippingRepository) {
		this.taskShippingRepository = taskShippingRepository;
	}

	public void recordSuccessfulTask(Long shippingId, String state) {
		try {
			TaskShipping taskShipping = new TaskShipping();
			taskShipping.setShippingId(shippingId.intValue());
			taskShipping.setStatus("SUCCESS");
			taskShipping.setMessage("Task processed successfully, moved to state: " + state);
			taskShipping.setStartDate(new java.util.Date());
			taskShipping.setEndDate(new java.util.Date());
			taskShippingRepository.save(taskShipping);
		} catch (DataAccessException e) {
			logger.error("Error al grabar información del estado de la tarea", e);
			recordTaskError(shippingId, "Error al grabar información del estado de la tarea" + e.getMessage());
		}
	}

	public void recordConcurrencyConflict(Long shippingId) {
		try {
			TaskShipping taskShipping = new TaskShipping();
			taskShipping.setShippingId(shippingId.intValue());
			taskShipping.setStatus("CONFLICT");
			taskShipping.setMessage("Concurrency conflict detected.");
			taskShipping.setStartDate(new java.util.Date());
			taskShipping.setEndDate(new java.util.Date());
			taskShippingRepository.save(taskShipping);
		} catch (DataAccessException e) {
			logger.error("Error al grabar información de conflicto de concurrencia", e);
			recordTaskError(shippingId, "Error al grabar información de conflicto de concurrencia" + e.getMessage());
		}
	}

	public void recordTaskError(Long shippingId, String errorMessage) {
		try {
			TaskShipping taskShipping = new TaskShipping();
			taskShipping.setShippingId(shippingId.intValue());
			taskShipping.setStatus("ERROR");
			taskShipping.setMessage("Task failed: " + errorMessage);
			taskShipping.setStartDate(new java.util.Date());
			taskShipping.setEndDate(new java.util.Date());
			taskShippingRepository.save(taskShipping);
		} catch (DataAccessException e) {
			logger.error("Error al grabar información del error de la tarea", e);
		}
	}

	public TaskStatusResponse getTaskStatus() {
		try {
			List<TaskShipping> taskShippings = taskShippingRepository.findAll();
			List<ShipmentStatus> shipmentStatuses = taskShippings.stream()
					.map(taskShipping -> new ShipmentStatus(taskShipping.getShippingId().longValue(),
							taskShipping.getStatus(), taskShipping.getMessage()))
					.collect(Collectors.toList());
			TaskStatusResponse taskStatusResponse = new TaskStatusResponse();
			taskStatusResponse.setShipmentStatuses(shipmentStatuses);
			return taskStatusResponse;
		} catch (DataAccessException e) {
			logger.error("Error al obtener el estado de las tareas", e);
			TaskStatusResponse taskStatusResponse = new TaskStatusResponse();
			return taskStatusResponse;
		}
	}

}