package sube.interviews.mareoenvios.model;

public class ShipmentStatus {
	private Long shippingId;
	private String currentState;
	private String message;

	public ShipmentStatus() {
	}

	public ShipmentStatus(Long shippingId, String currentState, String message) {
	    this.shippingId = shippingId;
	    this.currentState = currentState;
	    this.message = message;
	}

	public Long getShippingId() {
	    return shippingId;
	}

	public void setShippingId(Long shippingId) {
	    this.shippingId = shippingId;
	}

	public String getCurrentState() {
	    return currentState;
	}

	public void setCurrentState(String currentState) {
	    this.currentState = currentState;
	}

	public String getMessage() {
	    return message;
	}

	public void setMessage(String message) {
	    this.message = message;
	}

}
