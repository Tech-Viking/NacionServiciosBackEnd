package sube.interviews.taskprocessor.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskRequest {
	
	@JsonProperty("envios")
	private List<Shipment> shipments;

	public List<Shipment> getShipments() {
	    return shipments;
	}

	public void setShipments(List<Shipment> shipments) {
	    this.shipments = shipments;
	}

}
