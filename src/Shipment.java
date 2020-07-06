import java.time.LocalDateTime;

public class Shipment {
	private String itemCode, shipmentDate;
	private int quantity;
	
	public Shipment(String itemCode, int quantity, String shipmentDate) {
		this.itemCode = itemCode;
		this.quantity = quantity;
		this.shipmentDate = shipmentDate;
	}

	public String getItemCode() {
		return itemCode;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getShipmentDate() {
		return shipmentDate;
	}
	
	public String toString() {
		return String.format("Shipment - itemCode : %s, quantity: %s, shipmentDate: %s", itemCode, quantity, shipmentDate);
	}
}