
public class Item {
	private String itemCode, itemDescription;
	private double price;
	
	public Item(String itemCode,
				String itemDescription,
				double price) {
		
		this.itemCode = itemCode;
		this.itemDescription = itemDescription;
		this.price = price;
	}

	public String getItemCode() {
		return itemCode;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public double getPrice() {
		return price;
	}
}
