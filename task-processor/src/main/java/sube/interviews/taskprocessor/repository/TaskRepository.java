package sube.interviews.taskprocessor.repository;

import java.time.LocalDate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import sube.interviews.taskprocessor.entity.TaskShipping;
import sube.interviews.taskprocessor.model.ShipmentStatus;
import sube.interviews.taskprocessor.model.TaskStatusResponse;
import static sube.interviews.taskprocessor.util.TaskConstants.*;


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
			taskShipping.setStatus(TASK_STATUS_SUCCESS);
			taskShipping.setMessage(MESSAGE_TASK_SUCCESS  + state);
			taskShipping.setStartDate(LocalDate.now());
			taskShipping.setEndDate(LocalDate.now());
			taskShippingRepository.save(taskShipping);
			logger.info("Tarea para el envío {} registrada como exitosa con estado: {}", shippingId, state);
		} catch (DataAccessException e) {
			logger.error("Error al grabar información del estado de la tarea", e);
			recordTaskError(shippingId, "Error al grabar información del estado de la tarea" + e.getMessage());
		}
	}

	public void recordConcurrencyConflict(Long shippingId) {
		try {
			TaskShipping taskShipping = new TaskShipping();
			taskShipping.setShippingId(shippingId.intValue());
			taskShipping.setStatus(TASK_STATUS_CONFLICT);
			taskShipping.setMessage(MESSAGE_TASK_CONFLICT);
			taskShipping.setStartDate(LocalDate.now());
			taskShipping.setEndDate(LocalDate.now());
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
			taskShipping.setStatus(TASK_STATUS_ERROR);
			taskShipping.setMessage(MESSAGE_TASK_FAILED + errorMessage);
			taskShipping.setStartDate(LocalDate.now());
			taskShipping.setEndDate(LocalDate.now());
			taskShippingRepository.save(taskShipping);
		} catch (DataAccessException e) {
			logger.error("Error al grabar información del error de la tarea para el envío {}: {}", shippingId, e.getMessage());
		}
	}

	public TaskStatusResponse getTaskStatus() {
		try {
			List<TaskShipping> taskShippings = taskShippingRepository.findAll();

			Map<Long, List<String>> transitionHistoryMap = taskShippings.stream()
					.filter(taskShipping -> taskShipping.getStatus().equals(TASK_STATUS_SUCCESS))
					.collect(Collectors.groupingBy(taskShipping -> taskShipping.getShippingId().longValue(),
							Collectors.mapping(TaskShipping::getMessage, Collectors.toList())));

			List<ShipmentStatus> shipmentStatuses = taskShippings.stream()
					.collect(Collectors.toMap(taskShipping -> taskShipping.getShippingId().longValue(),
							taskShipping -> taskShipping, (existing, replacement) -> existing))
					.values().stream().map(taskShipping -> {
						Long shippingId = taskShipping.getShippingId().longValue();
						List<String> history = transitionHistoryMap.getOrDefault(shippingId, List.of());
						return new ShipmentStatus(shippingId, taskShipping.getStatus(), taskShipping.getMessage(),
								history);
					}).collect(Collectors.toList());

			TaskStatusResponse taskStatusResponse = new TaskStatusResponse();
			taskStatusResponse.setShipmentStatuses(shipmentStatuses);
			return taskStatusResponse;
		} catch (DataAccessException e) {
			logger.error("Error al obtener el estado de las tareas: {}", e.getMessage());
			TaskStatusResponse taskStatusResponse = new TaskStatusResponse();
			return taskStatusResponse;
		}
	}
}