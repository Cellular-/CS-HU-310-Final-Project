import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

	public static Purchase createPurchase(String itemCode, int quantity) throws SQLException {

		Connection connection = null;
		Purchase purchase = new Purchase(itemCode, quantity);
		connection = MySqlDatabase.getDatabaseConnection();
		Statement sqlStatement = connection.createStatement();
		int itemID = getItemID(itemCode);

		String sql = String.format("INSERT INTO Purchase (ItemID, quantity) VALUES (%s, %s);",
				itemID, purchase.getQuantity());

		sqlStatement.executeUpdate(sql);
		connection.close();

		return purchase;
	}
	
	public static void tryCreatePurchase(String itemCode, int quantity) {
		try {
			Purchase purchase = createPurchase(itemCode, quantity);
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
		int itemID = getItemID(itemCode);

		String sql = String.format("INSERT INTO Shipment (ItemID, quantity, shipmentDate) VALUES (%s, %s, '%s');",
				itemID, shipment.getQuantity(), shipment.getShipmentDate());

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
		int itemId = -1;
		
		try {
			connection = MySqlDatabase.getDatabaseConnection();
			Statement sqlStatement = connection.createStatement();
			String sql = String.format("select ID from Item where itemCode = '%s'", itemCode);
			results = sqlStatement.executeQuery(sql);
			
			if(results.next()) {
				itemId = results.getInt(1);
			}
		} catch (SQLException sqlException) {
			System.out.println("Error trying to get itemID.");
			System.out.println(sqlException.getMessage());
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
				items.add(new Item(results.getString(1), results.getString(2), results.getDouble(3)));
			}
		} catch (SQLException sqlException) {
			System.out.println("Error trying to get items.");
			System.out.println(sqlException.getMessage());
		}
		
		return items;
	}
	
	private static ArrayList<Shipment> getShipmentsByItemCode(String filterValue) {
		Connection connection = null;
		ArrayList<Shipment> shipments = new ArrayList<Shipment>();

		try {
			connection = MySqlDatabase.getDatabaseConnection();
			Statement sqlStatement = connection.createStatement();
			String sql = String.format("select b.ItemCode, a.Quantity, a.ShipmentDate\r\n" + 
									   "from Shipment a\r\n" + 
									   "inner join Item b\r\n" + 
									   "on a.ItemID = b.ID\r\n" + 
									   "where b.ItemCode like '%s'",
									  filterValue);
			ResultSet results = sqlStatement.executeQuery(sql); 

			while (results.next()) {
				shipments.add(new Shipment(results.getString(1), results.getInt(2), results.getString(3)));
			}
		} catch (SQLException sqlException) {
			System.out.println("Error trying to get shipments.");
			System.out.println(sqlException.getMessage());
		}
		
		return shipments;
	}

	private static ArrayList<Purchase> getPurchasesByItemCode(String filterValue) {
		Connection connection = null;
		ArrayList<Purchase> purchases = new ArrayList<Purchase>();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
		
		try {
			connection = MySqlDatabase.getDatabaseConnection();
			Statement sqlStatement = connection.createStatement();
			String sql = String.format("select b.ItemCode, a.Quantity, a.PurchaseDate\r\n" + 
									   "from Purchase a\r\n" + 
									   "inner join Item b\r\n" + 
									   "on a.ItemID = b.ID\r\n" + 
									   "where b.ItemCode like '%s'",
									  filterValue);
			ResultSet results = sqlStatement.executeQuery(sql); 

			while (results.next()) {
				purchases.add(new Purchase(results.getString(1), results.getInt(2), formatter.format(results.getDate(3))));
			}
		} catch (SQLException sqlException) {
			System.out.println("Error trying to get purchases.");
			System.out.println(sqlException.getMessage());
		}
		
		return purchases;
	}
	
	private static ArrayList<String> getItemsAvailable(String filterValue) {
		Connection connection = null;
		ArrayList<String> availableItems = new ArrayList<String>();
		
		try {
			connection = MySqlDatabase.getDatabaseConnection();
			Statement sqlStatement = connection.createStatement();
			String sql = String.format("select \r\n" + 
											"a.ItemCode, \r\n" + 
											"IF(shipments.total_shipments is NULL, 0, shipments.total_shipments) 'shipments',\r\n" + 
											"IF(purchases.total_purchases is NULL, 0, purchases.total_purchases) 'purchases',\r\n" + 
											"ABS(IF(shipments.total_shipments is NULL, 0, shipments.total_shipments) - IF(purchases.total_purchases is NULL, 0, purchases.total_purchases)) 'items_available'\r\n" + 
										"from Item a\r\n" + 
										"left join \r\n" + 
											"(select ItemID, sum(quantity) as 'total_purchases'\r\n" + 
											"from purchase\r\n" + 
											"group by 1)  purchases\r\n" + 
										"on a.ID = purchases.ItemID\r\n" + 
										"left join \r\n" + 
											"(select ItemID, sum(quantity) total_shipments\r\n" + 
											"from shipment\r\n" + 
											"group by 1) shipments\r\n" + 
										"on a.ID = shipments.ItemID\r\n" + 
										"where a.ItemCode like '%s'",
									  filterValue);
			ResultSet results = sqlStatement.executeQuery(sql); 

			while (results.next()) {
				availableItems.add(String.format("itemCode: %s, shipments: %s, purchases: %s, available: %s", 
								   results.getString(1), 
								   results.getInt(2), 
								   results.getInt(3),
								   results.getInt(4)));
			}
		} catch (SQLException sqlException) {
			System.out.println("Error trying to get available items.");
			System.out.println(sqlException.getMessage());
		}
		
		return availableItems;
	} 
 	
	private static Item updateItem(String itemCode, Double newPrice) {
		Connection connection = null;
		
		try {
			connection = MySqlDatabase.getDatabaseConnection();
			Statement sqlStatement = connection.createStatement();
			String sql = String.format("update Item a \r\n" +
									  "set a.Price = %s \r\n" +
									  "where a.ItemCode = '%s'",
									  newPrice, itemCode);
			sqlStatement.executeUpdate(sql);
		} catch (SQLException sqlException) {
			System.out.println("Error trying to update item.");
			System.out.println(sqlException.getMessage());
		}
		
		return getItemsByItemCode(itemCode).get(0);
	}
	
	private static void deleteItem(String itemCode) {
		Connection connection = null;
		
		try {
			connection = MySqlDatabase.getDatabaseConnection();
			Statement sqlStatement = connection.createStatement();
			String sql = String.format("delete from Item where ItemCode = '%s'", itemCode);
			int itemID = getItemID(itemCode);
			sqlStatement.executeUpdate(sql);
			
			if(itemID != -1) {
				sqlStatement.executeUpdate(sql);
				System.out.println(String.format("Deleted item withi temCode '%s'", itemCode));
			} else {
				System.out.println(String.format("No items to delete for ItemCode '%s'", itemCode));
			}
		} catch (SQLException sqlException) {
			System.out.println("Error trying to delete item.");
			System.out.println(sqlException.getMessage());
		}
	}
	
	private static void deleteShipment(String itemCode) {
		Connection connection = null;
		
		try {
			connection = MySqlDatabase.getDatabaseConnection();
			Statement sqlStatement = connection.createStatement();
			int itemID = getItemID(itemCode);
			String sql = String.format("delete from shipment where ItemID = '%s' order by ShipmentDate desc limit 1", itemID);

			if(itemID != -1) {
				sqlStatement.executeUpdate(sql);
				System.out.println(String.format("Deleted the most recent shipment for ItemCode '%s'", itemCode));
			} else {
				System.out.println(String.format("No shipments to delete for ItemCode '%s'", itemCode));
			}
		} catch (SQLException sqlException) {
			System.out.println("Error trying to delete shipment.");
			System.out.println(sqlException.getMessage());
		}
	}
	
	private static void deletePurchase(String itemCode) {
		Connection connection = null;
		
		try {
			connection = MySqlDatabase.getDatabaseConnection();
			int itemID = getItemID(itemCode);
			String sql = "delete from purchase where ItemID = ? order by PurchaseDate desc limit 1";

			if(itemID != -1) {
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setString(1, Integer.toString(itemID));
				ps.executeUpdate();
				System.out.println(String.format("Deleted the most recent purchase for ItemCode '%s'", itemCode));
			} else {
				System.out.println(String.format("No purchases to delete for ItemCode '%s'", itemCode));
			}
		} catch (SQLException sqlException) {
			System.out.println("Error trying to delete purchase.");
			System.out.println(sqlException.getMessage());
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args[0].equals("CreateItem")) {
			String itemCode = args[1];
			String itemDescription = args[2];
			Double price = Double.parseDouble(args[3]);

			tryCreateItem(itemCode, itemDescription, price);

		} else if (args[0].equals("CreatePurchase")) {
			String itemCode = args[1];
			int quantity = Integer.parseInt(args[2]);
			
			tryCreatePurchase(itemCode, quantity);

		} else if (args[0].equals("CreateShipment")) {
			String itemCode = args[1];
			int quantity = Integer.parseInt(args[2]);
			String dateTime = args[3];
			
			tryCreateShipment(itemCode, quantity, dateTime);
		} else if (args[0].equals("GetItems")) {
			String itemCode = args[1];
			
			for(Item member : getItemsByItemCode(itemCode)) {
				System.out.println(member.toString());
			}
		} else if (args[0].equals("GetShipments")) {
			String itemCode = args[1];
			
			for(Shipment member : getShipmentsByItemCode(itemCode)) {
				System.out.println(member.toString());
			}
		} else if (args[0].equals("GetPurchases")) {
			String itemCode = args[1];
			
			for(Purchase member : getPurchasesByItemCode(itemCode)) {
				System.out.println(member.toString());
			}
		} else if (args[0].equals("ItemsAvailable")) {
			String itemCode = args[1];
			
			System.out.println("List of available items:");
			for(String member : getItemsAvailable(itemCode)) {
				System.out.println(member.toString());
			}
		} else if (args[0].equals("UpdateItem")) {
			Item updatedItem = updateItem(args[1], Double.parseDouble(args[2]));
			System.out.println(updatedItem);
		} else if (args[0].equals("DeleteItem")) {
			deleteItem(args[1]);
		} else if (args[0].equals("DeleteShipment")) {
			deleteShipment(args[1]);
		} else if (args[0].equals("DeletePurchase")) {
			deletePurchase(args[1]);
		} else {
			System.out.println("Not a valid command!");
		}
	}
}