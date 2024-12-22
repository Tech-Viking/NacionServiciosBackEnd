package sube.interviews.mareoenvios.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import sube.interviews.mareoenvios.entity.Shipping;

@Component
public class ShippingClient {

	private static final Logger logger = LoggerFactory.getLogger(ShippingClient.class);
	private final RestTemplate restTemplate;

	@Autowired
	public ShippingClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void sendCommand(Shipping shipping, String transition) {
		String url = "http://localhost:8081/shippings/" + shipping.getId();
		String payload = getTransitionPayload(transition);
		try {
			logger.info("Sending PATCH request to {}, payload: {}", url, payload);
			restTemplate.patchForObject(url, payload, Void.class);
		} catch (Exception e) {
			logger.error("Error sending PATCH request to {}: {}", url, e.getMessage());
			throw e;
		}
		applyDelay(transition);
	}

	private String getTransitionPayload(String transition) {
		return String.format("{ \"transition\": \"%s\" }", transition);
	}

	private void applyDelay(String transition) {
		try {
			switch (transition) {
			case "sendToMail":
				logger.info("Applying delay for sendToMail transition (1 second).");
				Thread.sleep(1000);
				break;
			case "inTravel":
				logger.info("Applying delay for inTravel transition (3 seconds).");
				Thread.sleep(3000);
				break;
			case "delivered":
				logger.info("Applying delay for delivered transition (5 seconds).");
				Thread.sleep(5000);
				break;
			case "cancelled":
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

}