
public class Purchase {
	private String itemCode;
	private int quantity;
	
	public Purchase(String itemCode, int quantity) {
		this.itemCode = itemCode;
		this.quantity = quantity;
	}

	public String getItemCode() {
		return itemCode;
	}

	public int getQuantity() {
		return quantity;
	}
}