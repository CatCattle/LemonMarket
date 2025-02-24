-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: lemon_market
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `buyers`
--

DROP TABLE IF EXISTS `buyers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `buyers` (
  `BuyerID` int NOT NULL,
  `DemandPrice` int DEFAULT NULL,
  `BoughtCar` tinyint DEFAULT NULL,
  `TargetLevel` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`BuyerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buyers`
--

LOCK TABLES `buyers` WRITE;
/*!40000 ALTER TABLE `buyers` DISABLE KEYS */;
INSERT INTO `buyers` VALUES (1,100000,0,'66'),(2,200000,0,'67'),(3,200000,0,'67'),(4,100000,0,'65'),(5,100000,0,'65'),(6,400000,0,'66'),(7,200000,0,'66'),(8,200000,0,'67'),(9,100000,0,'67');
/*!40000 ALTER TABLE `buyers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cars`
--

DROP TABLE IF EXISTS `cars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cars` (
  `CarID` varchar(16) NOT NULL,
  `Level` varchar(16) NOT NULL,
  `OriginalPrice` int NOT NULL,
  `Lemon` tinyint NOT NULL,
  `DF` double NOT NULL,
  `CurrentValue` int DEFAULT NULL,
  `Round` int DEFAULT NULL,
  `Price` int DEFAULT NULL,
  `Owner` enum('DEALER','SELLER') DEFAULT 'SELLER',
  PRIMARY KEY (`CarID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cars`
--

LOCK TABLES `cars` WRITE;
/*!40000 ALTER TABLE `cars` DISABLE KEYS */;
INSERT INTO `cars` VALUES ('EQ','A',100000,0,0.774,62694,1,0,'DEALER'),('FF','B',200000,0,0.644,104328,1,0,''),('GS','A',100000,0,0.465,37665,1,0,'DEALER'),('HS','A',100000,0,0.654,52974,1,0,''),('IQ','A',100000,0,0.572,46331,1,0,'DEALER'),('JS','A',100000,0,0.672,54432,1,0,'DEALER'),('JX','A',100000,0,0.799,64719,1,0,'DEALER'),('LR','A',100000,0,0.595,48195,1,0,'DEALER'),('MY','B',200000,1,0.544,17625,1,0,''),('NP','A',100000,0,0.796,64476,1,0,'DEALER'),('QU','B',200000,0,0.714,115668,1,0,'DEALER'),('RF','A',100000,0,0.753,60993,1,0,''),('RZ','C',400000,0,0.782,253368,1,0,'DEALER'),('SC','A',100000,0,0.736,59616,1,0,'DEALER'),('SD','A',100000,0,0.708,57348,1,0,''),('ST','C',400000,0,0.617,199908,1,0,''),('SV','B',200000,0,0.484,78408,1,0,'DEALER'),('TX','B',200000,1,0.709,22971,1,0,''),('UJ','A',100000,0,0.666,53946,1,0,'DEALER'),('WN','B',200000,0,0.771,124902,1,0,'DEALER'),('WV','A',100000,1,0.471,7630,1,0,'DEALER'),('XK','C',400000,0,0.656,212544,1,0,''),('ZA','B',200000,0,0.706,114372,1,0,'DEALER'),('ZC','C',400000,0,0.705,228420,1,0,'DEALER'),('ZO','A',100000,0,0.654,52974,1,0,'DEALER'),('ZR','B',200000,0,0.709,114858,1,0,'');
/*!40000 ALTER TABLE `cars` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dealers`
--

DROP TABLE IF EXISTS `dealers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dealers` (
  `DealerID` int NOT NULL,
  `Balance` int DEFAULT NULL,
  `NetProfit` int DEFAULT NULL,
  `InventoryCount` int DEFAULT NULL,
  PRIMARY KEY (`DealerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dealers`
--

LOCK TABLES `dealers` WRITE;
/*!40000 ALTER TABLE `dealers` DISABLE KEYS */;
INSERT INTO `dealers` VALUES (1,113040,-386960,4),(2,-192424,-692424,7),(3,-48055,-548055,7);
/*!40000 ALTER TABLE `dealers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `DealerID` int NOT NULL,
  `CarID` varchar(16) NOT NULL,
  PRIMARY KEY (`DealerID`,`CarID`),
  KEY `car_idx` (`CarID`),
  CONSTRAINT `car` FOREIGN KEY (`CarID`) REFERENCES `cars` (`CarID`),
  CONSTRAINT `dealer` FOREIGN KEY (`DealerID`) REFERENCES `dealers` (`DealerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (3,'EQ'),(2,'FF'),(3,'FF'),(2,'GS'),(3,'GS'),(1,'HS'),(3,'HS'),(1,'IQ'),(1,'JS'),(3,'JS'),(2,'JX'),(1,'LR'),(3,'LR'),(1,'MY'),(2,'MY'),(3,'NP'),(2,'QU'),(3,'QU'),(2,'RF'),(3,'RF'),(1,'RZ'),(2,'RZ'),(1,'SC'),(2,'SC'),(2,'SD'),(2,'ST'),(3,'ST'),(2,'SV'),(3,'SV'),(1,'TX'),(3,'TX'),(1,'UJ'),(2,'UJ'),(3,'WN'),(1,'WV'),(2,'WV'),(1,'XK'),(3,'XK'),(2,'ZA'),(3,'ZA'),(1,'ZC'),(2,'ZC'),(2,'ZO'),(3,'ZO'),(2,'ZR'),(3,'ZR');
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sellers`
--

DROP TABLE IF EXISTS `sellers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sellers` (
  `SellerID` int NOT NULL,
  `SellPrice` int DEFAULT NULL,
  `SoldCar` tinyint DEFAULT NULL,
  `SellCarID` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`SellerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sellers`
--

LOCK TABLES `sellers` WRITE;
/*!40000 ALTER TABLE `sellers` DISABLE KEYS */;
INSERT INTO `sellers` VALUES (1,0,0,'WV'),(2,0,0,'IQ'),(3,0,0,'RZ'),(4,0,0,'ZO'),(5,0,0,'ZA'),(6,0,0,'NP'),(7,0,0,'QU'),(8,0,0,'GS'),(9,0,0,'SV');
/*!40000 ALTER TABLE `sellers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-21 17:10:56
