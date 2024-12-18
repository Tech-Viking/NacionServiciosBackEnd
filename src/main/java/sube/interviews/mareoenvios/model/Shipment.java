package sube.interviews.mareoenvios.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Shipment {
	
	private Long shippingId;
	@JsonProperty("time-start-in-seg")
	private int startTimeInSeconds;
	@JsonProperty("next-state")
	private boolean nextState;

	public Long getShippingId() {
	    return shippingId;
	}

	public void setShippingId(Long shippingId) {
	    this.shippingId = shippingId;
	}

	public int getStartTimeInSeconds() {
	    return startTimeInSeconds;
	}

	public void setStartTimeInSeconds(int startTimeInSeconds) {
	    this.startTimeInSeconds = startTimeInSeconds;
	}

	public boolean isNextState() {
	    return nextState;
	}

	public void setNextState(boolean nextState) {
	    this.nextState = nextState;
	}

	public String getTransitionPayload() {
	    if (nextState) {
	        return "{ \"transition\": \"sendToMail\" }";
	    } else {
	        return "{ \"transition\": \"cancelled\" }";
	    }
	}
}
