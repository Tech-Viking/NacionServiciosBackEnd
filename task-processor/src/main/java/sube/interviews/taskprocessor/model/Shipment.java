package sube.interviews.taskprocessor.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Shipment {
	
	@JsonProperty("shippingId")
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

}
