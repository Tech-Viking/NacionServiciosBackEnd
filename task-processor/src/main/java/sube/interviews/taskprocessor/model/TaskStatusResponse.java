package sube.interviews.taskprocessor.model;

import java.util.List;

public class TaskStatusResponse {
	
	private List<ShipmentStatus> shipmentStatuses;

	public List<ShipmentStatus> getShipmentStatuses() {
	    return shipmentStatuses;
	}

	public void setShipmentStatuses(List<ShipmentStatus> shipmentStatuses) {
	    this.shipmentStatuses = shipmentStatuses;
	}

}
