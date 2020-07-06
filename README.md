# BSU-HU-CS-310-Final-Project
CS310 Final Project

## Compile Java
`cd src`

`javac *.java`

## Setup
Run sql in `sql/create_tables.sql` on your own database.

Add the mysql connector JAR to your classpath.

Export the environment variables in your shell prior to running.
````
export MYSQL_DATABASE=yourDatabaseName
export MYSQL_HOST=localhost #or remotemysql.com
export MYSQL_PORT=port
export MYSQL_USERNAME=user
export MYSQL_PASSWORD=yourPassword
````

NOTE: you can put these in your bashrc file ``~/.bashrc``

## Valid commands
1. `java Project CreateItem <itemCode> <itemDescription> <price>`
2. `java Project CreatePurchase <itemCode> <PurchaseQuantity>`
3. `java Project CreateShipment <itemCode> <ShipmentQuantity> <shipmentDate>`
4. `java Project GetItems <itemCode>`
5. `java Project GetShipments <itemCode>`
6. `java Project GetPurchases <itemCode>`
7. `java Project ItemsAvailable <itemCode>`
8. `java Project UpdateItem <itemCode> <price>`
9. `java Project DeleteItem <itemCode>`
10. `java Project DeleteShipment <itemCode>`
11. `java Project DeletePurchase <itemCode>`

