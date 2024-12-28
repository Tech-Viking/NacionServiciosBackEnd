package sube.interviews.mareoenvios.service;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.repository.ShippingRepository;
import static sube.interviews.mareoenvios.util.TaskConstants.*;

@Service
public class ShippingService {

	private static final Logger logger = LoggerFactory.getLogger(ShippingService.class);
	private final ShippingRepository shippingRepository;

	private static final Map<String, String[]> VALID_TRANSITIONS = Map.of(TRANSITION_STATE_INITIAL,
			new String[] { TRANSITION_STATE_DELIVERED_MAIL, TRANSITION_STATE_CANCELLED },
			TRANSITION_STATE_DELIVERED_MAIL, new String[] { TRANSITION_STATE_ON_THE_GO, TRANSITION_STATE_CANCELLED },
			TRANSITION_STATE_ON_THE_GO, new String[] { TRANSITION_STATE_DELIVERED, TRANSITION_STATE_CANCELLED }

	);

	public ShippingService(ShippingRepository shippingRepository) {
		this.shippingRepository = shippingRepository;
	}

	public Optional<Shipping> getShippingById(Integer shippingId) {
		logger.info("Getting shipping with id: {}", shippingId);
		return shippingRepository.findById(shippingId).map(shipping -> {
			logger.info("Shipping found with id: {}", shippingId);
			return Optional.of(shipping);
		}).orElseThrow(() -> {
			logger.error("Shipping not found with id: {}", shippingId);
			return new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipping not found with id: " + shippingId);
		});
	}

	public void updateShipping(Integer shippingId, String newState) {
		   logger.info("Updating shipping with id: {}, new state: {}", shippingId, newState);
		   Shipping shipping = shippingRepository.findById(shippingId).orElseThrow(() -> {
		       logger.error("Shipping not found with id: {}", shippingId);
		       return new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipping not found with id: " + shippingId);
		   });
		   logger.info("Shipping found with id: {}", shippingId);
		   if (isValidTransition(shipping.getState(), newState)) {
		       logger.info("Valid transition from {} to {}", shipping.getState(), newState);
		       applyDelay(newState);
		        logger.info("Delay applied for transition: {}", newState);
		       shipping.setState(newState);
		       logger.info("Shipping state updated to: {}", newState);
		       shippingRepository.save(shipping);
		       logger.info("Shipping with id: {} updated, new state: {} in db", shippingId, newState);

		   } else {
		       logger.error("Invalid state transition from {} to {}", shipping.getState(), newState);
		       throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
		               "Invalid state transition from " + shipping.getState() + " to " + newState);
		   }

		}

	private void applyDelay(String transition) {
		try {
			switch (transition) {
			case TRANSITION_SEND_TO_MAIL:
				logger.info("Applying delay for sendToMail transition (1 second).");
				Thread.sleep(1000);
				break;
			case TRANSITION_IN_TRAVEL:
				logger.info("Applying delay for inTravel transition (3 seconds).");
				Thread.sleep(3000);
				break;
			case TRANSITION_DELIVERED:
				logger.info("Applying delay for delivered transition (5 seconds).");
				Thread.sleep(5000);
				break;
			case TRANSITION_CANCELLED:
				logger.info("Applying delay for cancelled transition (3 seconds).");
				Thread.sleep(3000);
				break;
			default:
				logger.info("No delay applied for transition: " + transition);
				break;
			}
		} catch (InterruptedException e) {
			logger.error("Delay interrupted for transition: " + transition, e);
			Thread.currentThread().interrupt();
		}
	}

	private boolean isValidTransition(String currentState, String newState) {
		if (currentState.equals(newState)) {
			return false;
		}
		return Arrays.asList(VALID_TRANSITIONS.getOrDefault(currentState, new String[0])).contains(newState);
	}
}