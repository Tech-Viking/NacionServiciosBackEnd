package sube.interviews.mareoenvios.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.model.TopProductResponse;
import sube.interviews.mareoenvios.model.TransitionRequest;
import sube.interviews.mareoenvios.service.CustomerService;
import sube.interviews.mareoenvios.service.ReportService;
import sube.interviews.mareoenvios.service.ShippingService;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api")
@Tag(name = "Mareo Envíos", description = "Endpoints para administrar la empresa de envíos.")
public class ShippingController {

	private static final Logger logger = LoggerFactory.getLogger(ShippingController.class);
	private final ReportService reportService;
	private final CustomerService customerService;
	private final ShippingService shippingService;

	public ShippingController(ReportService reportService, CustomerService customerService,
			ShippingService shippingService) {
		this.reportService = reportService;
		this.customerService = customerService;
		this.shippingService = shippingService;
	}

	@Operation(summary = "Verifica la salud de la API", description = "Retorna un mensaje de confirmación para indicar que la API está funcionando correctamente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "API funcionando correctamente", content = @Content(schema = @Schema(type = "string"))) })
	@GetMapping("/health")
	public ResponseEntity<String> checkApiHealth() {
		logger.info("Health check requested");
		return ResponseEntity.ok("API is healthy");
	}

	@Operation(summary = "Obtiene los productos más enviados", description = "Retorna Lista de los productos más enviados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de productos más enviados", content = @Content(schema = @Schema(implementation = TopProductResponse.class, type = "array"))) })
	@GetMapping("/reports/top-sent")
	public ResponseEntity<List<TopProductResponse>> getTopSentProducts() {
		logger.info("Top sent products requested");
		List<TopProductResponse> products = reportService.getTopSentProducts();
		logger.info("Top sent products found: {}", products.size());
		return ResponseEntity.ok(products);
	}

	@Operation(summary = "Obtiene un cliente por su ID", description = "Retorna la información de un cliente basado en su ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(schema = @Schema(implementation = Customer.class))),
			@ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(schema = @Schema(type = "string"))) })
	@GetMapping("/customers/{customerId}")
	public ResponseEntity<Customer> getCustomer(@PathVariable Integer customerId) {
		logger.info("Customer requested with id: {}", customerId);
		Customer customer = customerService.getCustomerById(customerId)
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cliente no encontrado"));
		logger.info("Customer found with id: {}", customerId);
		return ResponseEntity.ok(customer);

	}

	@Operation(summary = "Obtiene un envío por su ID", description = "Retorna la información de un envío basado en su ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Envío encontrado", content = @Content(schema = @Schema(implementation = Shipping.class))),
			@ApiResponse(responseCode = "404", description = "Envío no encontrado", content = @Content(schema = @Schema(type = "string"))) })
	@GetMapping("/shippings/{shippingId}")
	public ResponseEntity<Shipping> getShipping(@PathVariable Integer shippingId) {
		logger.info("Shipping requested with id: {}", shippingId);
		Shipping shipping = shippingService.getShippingById(shippingId)
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Envío no encontrado"));
		logger.info("Shipping found with id: {}", shippingId);
		return ResponseEntity.ok(shipping);
	}

	@Operation(summary = "Actualiza el estado de un envío", description = "Este endpoint actualiza el estado de un envío basado en su ID y el nuevo estado proporcionado.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Estado de envío actualizado", content = @Content(schema = @Schema(type = "string"))) })
	@PatchMapping("/shippings/{shippingId}")
	public ResponseEntity<?> updateShipping(@PathVariable Integer shippingId,
			@Valid @RequestBody TransitionRequest transition) {
		logger.info("Update shipping requested with id: {}, transition: {}", shippingId, transition.getTransition());
		shippingService.updateShipping(shippingId, transition.getTransition());
		logger.info("Shipping state updated with id: {}, transition: {}", shippingId, transition.getTransition());
		return ResponseEntity.ok("Shipping state updated");
	}

}