package sube.interviews.mareoenvios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.model.TaskRequest;
import sube.interviews.mareoenvios.model.TaskStatusResponse;
import sube.interviews.mareoenvios.model.TopProductResponse;
import sube.interviews.mareoenvios.service.CustomerService;
import sube.interviews.mareoenvios.service.ReportService;
import sube.interviews.mareoenvios.service.ShippingService;
import sube.interviews.mareoenvios.service.TaskService;

@RestController
@RequestMapping("/api")
@Tag(name = "Task Management", description = "Endpoints para administrar tareas, reportes y entidades relacionadas.")
public class TaskController {

	private final TaskService taskService;
	private final ReportService reportService;
	private final CustomerService customerService;
	private final ShippingService shippingService;

	public TaskController(TaskService taskService, ReportService reportService, CustomerService customerService,
			ShippingService shippingService) {
		this.taskService = taskService;
		this.reportService = reportService;
		this.customerService = customerService;
		this.shippingService = shippingService;
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

	@Operation(summary = "Obtiene los productos más enviados", description = "Retorna Lista de los productos más enviados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de productos más enviados", content = @Content(schema = @Schema(implementation = TopProductResponse.class, type = "array"))) })
	@GetMapping("/reports/top-sent")
	public ResponseEntity<List<TopProductResponse>> getTopSentProducts() {
		return ResponseEntity.ok(reportService.getTopSentProducts());
	}

	@Operation(summary = "Obtiene un cliente por su ID", description = "Retorna la información de un cliente basado en su ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(schema = @Schema(implementation = Customer.class))),
			@ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(schema = @Schema(type = "string"))) })
	@GetMapping("/customers/{customerId}")
	public ResponseEntity<?> getCustomer(@PathVariable Integer customerId) {
		Optional<Customer> customer = customerService.getCustomerById(customerId);
		if (customer.isPresent()) {
			return ResponseEntity.ok(customer.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
		}
	}

	@Operation(summary = "Obtiene un envío por su ID", description = "Retorna la información de un envío basado en su ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Envío encontrado", content = @Content(schema = @Schema(implementation = Shipping.class))),
			@ApiResponse(responseCode = "404", description = "Envío no encontrado", content = @Content(schema = @Schema(type = "string"))) })
	@GetMapping("/shippings/{shippingId}")
	public ResponseEntity<?> getShipping(@PathVariable Integer shippingId) {
		Optional<Shipping> shipping = shippingService.getShippingById(shippingId);
		if (shipping.isPresent()) {
			return ResponseEntity.ok(shipping.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Envío no encontrado");
		}
	}

	@Operation(summary = "Actualiza el estado de un envío", description = "Este endpoint actualiza el estado de un envío basado en su ID y el nuevo estado proporcionado.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Estado de envío actualizado", content = @Content(schema = @Schema(type = "string"))) })
	@PatchMapping("/shippings/{shippingId}")
	public ResponseEntity<?> updateShipping(@PathVariable Integer shippingId, @RequestBody String newState) {
		shippingService.updateShippingState(shippingId, newState);
		return ResponseEntity.ok("Shipping state updated");
	}

}