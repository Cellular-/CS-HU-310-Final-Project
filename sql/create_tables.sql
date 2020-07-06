CREATE TABLE `item` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ItemCode` varchar(10) NOT NULL,
  `ItemDescription` varchar(50) DEFAULT NULL,
  `Price` decimal(4,2) DEFAULT '0.00',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ItemCode_UNIQUE` (`ItemCode`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `purchase` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ItemID` int NOT NULL,
  `Quantity` int NOT NULL,
  `PurchaseDate` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `Item.ID_idx` (`ItemID`),
  CONSTRAINT `Item.ID` FOREIGN KEY (`ItemID`) REFERENCES `item` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `shipment` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ItemID` int NOT NULL,
  `Quantity` int NOT NULL,
  `ShipmentDate` date NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ShipmentDate_UNIQUE` (`ShipmentDate`),
  KEY `Item.ID_idx` (`ItemID`) /*!80000 INVISIBLE */,
  CONSTRAINT `Item.ID2` FOREIGN KEY (`ItemID`) REFERENCES `item` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;