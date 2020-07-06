
public class Purchase {
	private String itemCode, purchaseDate;
	private int quantity;
	
	public Purchase(String itemCode, int quantity) {
		this.itemCode = itemCode;
		this.quantity = quantity;
	}
	
	public Purchase(String itemCode, int quantity, String purchaseDate) {
		this.itemCode = itemCode;
		this.quantity = quantity;
		this.purchaseDate = purchaseDate;
	}

	public String getItemCode() {
		return itemCode;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public String toString() {
		return String.format("Purchase - itemCode: %s, quantity: %s, purchaseDate: %s", itemCode, quantity, purchaseDate);
	}
}