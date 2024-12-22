package sube.interviews.taskprocessor.model;

import java.util.List;

public class ShipmentStatus {
	private Long shippingId;
	private String currentState;
	private String message;
	
	private List<String> transitionHistory;

	public ShipmentStatus() {
	}

	public ShipmentStatus(Long shippingId, String currentState, String message) {
	    this.shippingId = shippingId;
	    this.currentState = currentState;
	    this.message = message;
	}
	
    public ShipmentStatus(Long shippingId, String currentState, String message, List<String> transitionHistory) {
        this.shippingId = shippingId;
        this.currentState = currentState;
        this.message = message;
		this.transitionHistory = transitionHistory;
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
	
	public List<String> getTransitionHistory() {
		return transitionHistory;
	}

	public void setTransitionHistory(List<String> transitionHistory) {
		this.transitionHistory = transitionHistory;
	}

}
