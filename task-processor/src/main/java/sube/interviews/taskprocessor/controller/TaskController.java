package sube.interviews.taskprocessor.controller;

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

	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@Operation(summary = "Procesa tareas concurrentes", description = "Inicia el procesamiento de tareas de manera concurrente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tareas iniciadas correctamente", content = @Content(schema = @Schema(type = "string"))) })
	@PostMapping("/tarea-concurrente")
	public ResponseEntity<String> processTasks(@RequestBody TaskRequest request) {
		taskService.processTasks(request);
		return ResponseEntity.ok("Tasks are being processed");
	}

	@Operation(summary = "Obtiene el estado de las tareas", description = "Retorna el estado actual del procesamiento de tareas.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Estado de las tareas", content = @Content(schema = @Schema(implementation = TaskStatusResponse.class))) })
	@GetMapping("/status")
	public ResponseEntity<TaskStatusResponse> getStatus() {
		return ResponseEntity.ok(taskService.getStatus());
	}

	@Operation(summary = "Verifica la salud de la API", description = "Retorna un mensaje de confirmación para indicar que la API está funcionando correctamente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "API funcionando correctamente", content = @Content(schema = @Schema(type = "string"))) })
	@GetMapping("/health")
	public ResponseEntity<String> healthCheck() {
		return ResponseEntity.ok("OK");
	}

}