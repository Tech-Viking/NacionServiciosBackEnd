package sube.interviews.mareoenvios.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ShippingClient {

	private final RestTemplate restTemplate;

	@Autowired
	public ShippingClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void sendCommand(Shipment shipment) {
		String url = "http://localhost:8081/shippings/" + shipment.getShippingId();
		restTemplate.patchForObject(url, shipment.getTransitionPayload(), Void.class);
	}


}
