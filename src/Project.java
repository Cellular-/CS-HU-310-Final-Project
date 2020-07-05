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
	
	public static List<String> getItemIDs() throws SQLException {
		Connection connection = null;
		connection = MySqlDatabase.getDatabaseConnection();
		Statement sqlStatement = connection.createStatement();
		String sql = "select ID from Item;";
		ResultSet results = sqlStatement.executeQuery(sql); 
		
		List<String> ids = new ArrayList<String>();
		
		while (results.next()) {
			ids.add(results.getString(1));
		}
		
		return ids;
	}

	public static void main(String[] args) {

		if (args[0].equals("CreateItem")) {
			String itemCode = args[1];
			String itemDescription = args[2];
			Double price = Double.parseDouble(args[3]);

			tryCreateItem(itemCode, itemDescription, price);

		} else if (args[0].equals("CreatePurchase")) {
			String itemID = args[1];
			int quantity = Integer.parseInt(args[2]);
			
			tryCreatePurchase(itemID, quantity);

		} else if (args[0].equals("CreateShipment")) {

		} else if (args[0].equals("GetItems")) {

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