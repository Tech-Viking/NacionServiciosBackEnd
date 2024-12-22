package sube.interviews.taskprocessor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceAccessException.class)
	public ResponseEntity<String> handleResourceAccessException(ResourceAccessException ex) {
		logger.error("Error al realizar la petición al servicio externo", ex);
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Error al realizar la petición al servicio externo. " + ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex) {
		logger.error("Error interno inesperado: ", ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Error interno inesperado. " + ex.getMessage());
	}

}
