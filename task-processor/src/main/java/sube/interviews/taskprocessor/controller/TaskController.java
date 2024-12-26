package sube.interviews.taskprocessor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import sube.interviews.taskprocessor.model.TaskRequest;
import sube.interviews.taskprocessor.model.TaskStatusResponse;
import sube.interviews.taskprocessor.service.TaskService;

@RestController
@RequestMapping("/api")
@Tag(name = "Task Management", description = "Endpoints para administrar tareas, reportes y entidades relacionadas.")
public class TaskController {

	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@Operation(summary = "Procesa tareas concurrentes", description = "Inicia el procesamiento de tareas de manera concurrente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tareas iniciadas correctamente", content = @Content(schema = @Schema(type = "string"))) })
	@PostMapping("/tarea-concurrente")
	public ResponseEntity<String> processTasks(@RequestBody TaskRequest request) {
		logger.info("Received POST request to /tarea-concurrente with {} shipments.", request.getShipments().size());
		try {
			taskService.processTasks(request);
			logger.info("Tasks are being processed.");
			return ResponseEntity.ok("Tasks are being processed");
		} catch (Exception e) {
			logger.error("Error processing tasks: {}", e.getMessage(), e);
			return ResponseEntity.status(500).body("Error processing tasks: " + e.getMessage());
		}
	}

	@Operation(summary = "Obtiene el estado de las tareas", description = "Retorna el estado actual del procesamiento de tareas.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Estado de las tareas", content = @Content(schema = @Schema(implementation = TaskStatusResponse.class))) })
	@GetMapping("/status")
	public ResponseEntity<TaskStatusResponse> getStatus() {
		logger.info("Received GET request to /status");
		try {
			TaskStatusResponse response = taskService.getStatus();
			logger.info("Returning task status response with {} shipments.", response.getShipmentStatuses().size());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error getting task status: {}", e.getMessage(), e);
			return ResponseEntity.status(500).body(null);
		}

	}

	@Operation(summary = "Verifica la salud de la API", description = "Retorna un mensaje de confirmación para indicar que la API está funcionando correctamente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "API funcionando correctamente", content = @Content(schema = @Schema(type = "string"))) })
	@GetMapping("/health")
	public ResponseEntity<String> healthCheck() {
		return ResponseEntity.ok("OK");
	}

}