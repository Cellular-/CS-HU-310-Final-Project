import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Project {
	public static Item createItem(String itemCode, String itemDescription, double price) throws SQLException {

		Connection connection = null;
		Item item = new Item(itemCode, itemDescription, price);
		connection = MySqlDatabase.getDatabaseConnection();
		Statement sqlStatement = connection.createStatement();

		String sql = String.format("INSERT INTO Item (itemCode, itemDescription, price) VALUES ('%s' , '%s', %s);",
				item.getItemCode(), item.getItemDescription(), item.getPrice());

		sqlStatement.executeUpdate(sql);
		connection.close();

		return item;
	}

	public static void tryCreateItem(String itemCode, String itemDescription, double price) {
		try {
			Item item = createItem(itemCode, itemDescription, price);
			System.out.println(item.toString());
		} catch (SQLException sqlException) {
			System.out.println("Error trying to create item");
			System.out.println(sqlException.getMessage());
		}
	}

	public static Purchase createPurchase(String itemID, int quantity) throws SQLException {

		Connection connection = null;
		Purchase purchase = new Purchase(itemID, quantity);
		connection = MySqlDatabase.getDatabaseConnection();
		Statement sqlStatement = connection.createStatement();

		String sql = String.format("INSERT INTO Purchase (ItemID, quantity) VALUES (%s, %s);",
				purchase.getItemCode(), purchase.getQuantity());

		sqlStatement.executeUpdate(sql);
		connection.close();

		return purchase;
	}
	
	public static void tryCreatePurchase(String itemID, int quantity) {
		try {
			Purchase purchase = createPurchase(itemID, quantity);
			System.out.println(purchase.toString());
		} catch (SQLException sqlException) {
			System.out.println("Error trying to create item");
			System.out.println(sqlException.getMessage());
		}
	}	
	
	public static Shipment createShipment(String itemCode, int quantity, String shipmentDate) throws SQLException {
		Connection connection = null;
		Shipment shipment = new Shipment(itemCode, quantity, shipmentDate);
		connection = MySqlDatabase.getDatabaseConnection();
		Statement sqlStatement = connection.createStatement();

		String sql = String.format("INSERT INTO Shipment (ItemID, quantity, shipmentDate) VALUES (%s, %s, '%s');",
				shipment.getItemCode(), shipment.getQuantity(), shipment.getShipmentDate());

		sqlStatement.executeUpdate(sql);
		connection.close();

		return shipment;
	}
	
	public static void tryCreateShipment(String itemCode, int quantity, String dateTime) {
		try {
			Shipment shipment = createShipment(itemCode, quantity, dateTime);
			System.out.println(shipment.toString());
		} catch (SQLException sqlException) {
			System.out.println("Error trying to create shipment.");
			System.out.println(sqlException.getMessage());
		}
	}

	private static int getItemID(String itemCode) {
		Connection connection = null;
		ResultSet results = null;
		int itemId = 0;
		
		try {
			connection = MySqlDatabase.getDatabaseConnection();
			Statement sqlStatement = connection.createStatement();
			String sql = String.format("select ID from Item where itemCode = %s", itemCode);
			results = sqlStatement.executeQuery(sql);
			results.next();
			itemId = results.getInt(1);
		} catch (SQLException sqlException) {
			
		}
		
		return itemId;
	}

	private static ArrayList<Item> getItemsByItemCode(String filterValue) {
		Connection connection = null;
		ArrayList<Item> items = new ArrayList<Item>();

		try {
			connection = MySqlDatabase.getDatabaseConnection();
			Statement sqlStatement = connection.createStatement();
			String sql = String.format("select ItemCode, ItemDescription, Price from Item where ItemCode like '%s'",
									  filterValue);
			ResultSet results = sqlStatement.executeQuery(sql); 

			while (results.next()) {
				items.add(new Item(results.getString(1), results.getString(2), results.getInt(3)));
			}
		} catch (SQLException sqlException) {
			System.out.println("Error trying to get items.");
			System.out.println(sqlException.getMessage());
		}
		
		return items;
	}
	
	public static void main(String[] args) {

		if (args[0].equals("CreateItem")) {
			String itemCode = args[1];
			String itemDescription = args[2];
			Double price = Double.parseDouble(args[3]);

			tryCreateItem(itemCode, itemDescription, price);

		} else if (args[0].equals("CreatePurchase")) {
			String itemCode = args[1];
			int quantity = Integer.parseInt(args[2]);
			
			tryCreatePurchase(Integer.toString(getItemID(itemCode)), quantity);

		} else if (args[0].equals("CreateShipment")) {
			String itemCode = args[1];
			int quantity = Integer.parseInt(args[2]);
			String dateTime = args[3];
			
			tryCreateShipment(Integer.toString(getItemID(itemCode)), quantity, dateTime);
		} else if (args[0].equals("GetItems")) {
			String itemCode = args[1];
			
			for(Item item : getItemsByItemCode(itemCode)) {
				System.out.println(item.toString());
			}
		} else if (args[0].equals("GetShipments")) {

		} else if (args[0].equals("GetPurchases")) {

		} else if (args[0].equals("ItemsAvailable")) {

		} else if (args[0].equals("UpdateItem")) {

		} else if (args[0].equals("DeleteItem")) {

		} else if (args[0].equals("DeleteShipment")) {

		} else if (args[0].equals("DeletePurchase")) {

		}
	}
}