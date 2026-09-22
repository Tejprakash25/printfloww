-- MySQL dump 10.13  Distrib 26.7.0, for macos26.6 (arm64)
--
-- Host: localhost    Database: printflow
-- ------------------------------------------------------
-- Server version	26.7.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '3981af04-b6b5-11f1-8f62-c4c48b6b750b:1-53';

--
-- Table structure for table `approvals`
--

DROP TABLE IF EXISTS `approvals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `approvals` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment` varchar(2000) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `type` enum('APPROVED','CHANGE_REQUESTED') NOT NULL,
  `design_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `requested_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5niaal02f4kt700pv45b5r9xy` (`design_id`),
  KEY `FKmrkxddfcs06b185hj58ii4dao` (`order_id`),
  KEY `FKby0y011n3pd3h0tng06osybqf` (`requested_by`),
  CONSTRAINT `FK5niaal02f4kt700pv45b5r9xy` FOREIGN KEY (`design_id`) REFERENCES `designs` (`id`),
  CONSTRAINT `FKby0y011n3pd3h0tng06osybqf` FOREIGN KEY (`requested_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKmrkxddfcs06b185hj58ii4dao` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `approvals`
--

LOCK TABLES `approvals` WRITE;
/*!40000 ALTER TABLE `approvals` DISABLE KEYS */;
INSERT INTO `approvals` VALUES (1,'Design approved by customer','2026-09-23 02:22:27.320247','APPROVED',1,2,5);
/*!40000 ALTER TABLE `approvals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deliveries`
--

DROP TABLE IF EXISTS `deliveries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `deliveries` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `delivered_at` datetime(6) DEFAULT NULL,
  `notes` varchar(2000) DEFAULT NULL,
  `scheduled_date` date DEFAULT NULL,
  `status` enum('DELIVERED','OUT_FOR_DELIVERY','PENDING','PICKED_UP','PICKUP_READY') NOT NULL,
  `tracking_number` varchar(255) DEFAULT NULL,
  `type` enum('DELIVERY','PICKUP') NOT NULL,
  `assigned_to` bigint DEFAULT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKk36n9p5v7dd96hpgkwybvbogt` (`order_id`),
  KEY `FK94p5fdvi7aeiwt6peb9uq7jc2` (`assigned_to`),
  CONSTRAINT `FK7isx0rnbgqr1dcofd5putl6jw` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FK94p5fdvi7aeiwt6peb9uq7jc2` FOREIGN KEY (`assigned_to`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliveries`
--

LOCK TABLES `deliveries` WRITE;
/*!40000 ALTER TABLE `deliveries` DISABLE KEYS */;
/*!40000 ALTER TABLE `deliveries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `designs`
--

DROP TABLE IF EXISTS `designs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `designs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `approved` bit(1) NOT NULL,
  `content_type` varchar(255) DEFAULT NULL,
  `file_size` bigint DEFAULT NULL,
  `original_file_name` varchar(255) NOT NULL,
  `stored_file_name` varchar(255) NOT NULL,
  `uploaded_at` datetime(6) NOT NULL,
  `version` int DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `uploaded_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK94gdvh5goi54u4y4y160l5duw` (`stored_file_name`),
  KEY `FKsg574g9d50q87nt1lpakl6prx` (`order_id`),
  KEY `FKr6prfnbmij4g15ey0bwmw95yk` (`uploaded_by`),
  CONSTRAINT `FKr6prfnbmij4g15ey0bwmw95yk` FOREIGN KEY (`uploaded_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKsg574g9d50q87nt1lpakl6prx` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `designs`
--

LOCK TABLES `designs` WRITE;
/*!40000 ALTER TABLE `designs` DISABLE KEYS */;
INSERT INTO `designs` VALUES (1,_binary '','image/png',1059422,'118887.png','10583672-0487-4325-8ea6-f7938320f59f-118887.png','2026-09-23 01:36:07.423268',1,2,5);
/*!40000 ALTER TABLE `designs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(12,2) NOT NULL,
  `invoice_number` varchar(255) NOT NULL,
  `issued_at` datetime(6) DEFAULT NULL,
  `pdf_path` varchar(255) DEFAULT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKl1x55mfsay7co0r3m9ynvipd5` (`invoice_number`),
  UNIQUE KEY `UKe718q5klx5pempy28p2nx88a6` (`order_id`),
  CONSTRAINT `FK4ko3y00tkkk2ya3p6wnefjj2f` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `message` varchar(2000) NOT NULL,
  `is_read` bit(1) NOT NULL,
  `title` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9y21adhxn0ayjhfocscqox7bh` (`user_id`),
  CONSTRAINT `FK9y21adhxn0ayjhfocscqox7bh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,'2026-09-23 01:17:39.276899','PF1002 needs a quotation.',_binary '\0','New order received',1),(2,'2026-09-23 01:57:32.072717','Quotation QT/PF1002 is ready for review.',_binary '\0','Quotation ready',5),(3,'2026-09-23 02:10:07.397859','PF1002 quotation was accepted.',_binary '\0','Quotation decision',1),(4,'2026-09-23 02:22:27.366415','PF1002 has a design approval.',_binary '\0','Design decision',1);
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_history`
--

DROP TABLE IF EXISTS `order_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `status` enum('APPROVED','CANCELLED','CHANGE_REQUESTED','COMPLETED','DELIVERED','IN_PRODUCTION','ORDER_PLACED','OUT_FOR_DELIVERY','PAYMENT_PENDING','PICKED_UP','PICKUP_READY','PROOF_PENDING','QUALITY_CHECK','QUOTATION_ACCEPTED','QUOTATION_PENDING','QUOTATION_REJECTED','QUOTATION_SENT','READY') DEFAULT NULL,
  `changed_by` bigint DEFAULT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg92c0lo2t94pmm26fcuf791me` (`changed_by`),
  KEY `FKnw2ljd8jnpdc9y2ild52e79t2` (`order_id`),
  CONSTRAINT `FKg92c0lo2t94pmm26fcuf791me` FOREIGN KEY (`changed_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKnw2ljd8jnpdc9y2ild52e79t2` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_history`
--

LOCK TABLES `order_history` WRITE;
/*!40000 ALTER TABLE `order_history` DISABLE KEYS */;
INSERT INTO `order_history` VALUES (1,'2026-09-23 01:17:39.246962','Order created.','ORDER_PLACED',5,2),(2,'2026-09-23 01:57:32.069981','Quotation sent to customer.','QUOTATION_SENT',1,2),(3,'2026-09-23 02:10:07.371979','Quotation accepted.','QUOTATION_ACCEPTED',5,2),(4,'2026-09-23 02:22:27.354261','Design approved.','APPROVED',5,2);
/*!40000 ALTER TABLE `order_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `delivery_address` varchar(255) DEFAULT NULL,
  `delivery_type` varchar(255) DEFAULT NULL,
  `finishing` varchar(255) DEFAULT NULL,
  `height` varchar(255) DEFAULT NULL,
  `material` varchar(255) NOT NULL,
  `order_number` varchar(255) NOT NULL,
  `product_type` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  `required_date` date DEFAULT NULL,
  `special_instructions` varchar(2000) DEFAULT NULL,
  `status` enum('APPROVED','CANCELLED','CHANGE_REQUESTED','COMPLETED','DELIVERED','IN_PRODUCTION','ORDER_PLACED','OUT_FOR_DELIVERY','PAYMENT_PENDING','PICKED_UP','PICKUP_READY','PROOF_PENDING','QUALITY_CHECK','QUOTATION_ACCEPTED','QUOTATION_PENDING','QUOTATION_REJECTED','QUOTATION_SENT','READY') NOT NULL,
  `total_amount` decimal(38,2) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `width` varchar(255) DEFAULT NULL,
  `customer_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnthkiu7pgmnqnu86i2jyoe2v7` (`order_number`),
  KEY `FKsjfs85qf6vmcurlx43cnc16gy` (`customer_id`),
  CONSTRAINT `FKsjfs85qf6vmcurlx43cnc16gy` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2026-09-23 00:38:43.146356','Kothrud, Pune','Delivery','Eyelet','4 ft','Star Flex','PF1024','Flex Banner',2,NULL,NULL,'IN_PRODUCTION',1200.00,'2026-09-23 00:38:43.146369','6 ft',2),(2,'2026-09-23 01:17:39.214036',NULL,'DELIVERY',NULL,NULL,'Glossy Cardstock','PF1002','Business Cards',100,NULL,NULL,'APPROVED',1770.00,'2026-09-23 02:22:27.368250',NULL,5);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(12,2) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `method` enum('BANK_TRANSFER','CARD','CASH','UPI') NOT NULL,
  `paid_at` datetime(6) DEFAULT NULL,
  `status` enum('FAILED','PENDING','REFUNDED','SUCCESS') NOT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK81gagumt0r8y3rmudcgpbk42l` (`order_id`),
  CONSTRAINT `FK81gagumt0r8y3rmudcgpbk42l` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `production_jobs`
--

DROP TABLE IF EXISTS `production_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `production_jobs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `completed_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `notes` varchar(2000) DEFAULT NULL,
  `started_at` datetime(6) DEFAULT NULL,
  `status` enum('COMPLETED','IN_PRODUCTION','PENDING','QUALITY_CHECK','READY') NOT NULL,
  `assigned_to` bigint DEFAULT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKfcn9q6du2q0knx0p5gbgriho7` (`order_id`),
  KEY `FKtjyi1xkh1el5pold6obu0ququ` (`assigned_to`),
  CONSTRAINT `FK7njko0mlgkha5wrg9vlwx0bhq` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKtjyi1xkh1el5pold6obu0ququ` FOREIGN KEY (`assigned_to`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `production_jobs`
--

LOCK TABLES `production_jobs` WRITE;
/*!40000 ALTER TABLE `production_jobs` DISABLE KEYS */;
INSERT INTO `production_jobs` VALUES (1,NULL,'2026-09-23 02:22:27.360556',NULL,NULL,'PENDING',NULL,2);
/*!40000 ALTER TABLE `production_jobs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quotations`
--

DROP TABLE IF EXISTS `quotations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quotations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `quotation_number` varchar(255) NOT NULL,
  `status` enum('ACCEPTED','DRAFT','EXPIRED','REJECTED','SENT') NOT NULL,
  `subtotal` decimal(12,2) NOT NULL,
  `tax_amount` decimal(12,2) NOT NULL,
  `tax_percent` decimal(5,2) NOT NULL,
  `total` decimal(12,2) NOT NULL,
  `valid_until` date DEFAULT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK9kbnjdxcf5d7qxwy80ple68bh` (`quotation_number`),
  UNIQUE KEY `UKqit4m5dusp8kcn1xdoip3806k` (`order_id`),
  CONSTRAINT `FK6hnwe5fpk1w3vp4qpgl0p7883` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quotations`
--

LOCK TABLES `quotations` WRITE;
/*!40000 ALTER TABLE `quotations` DISABLE KEYS */;
INSERT INTO `quotations` VALUES (1,'2026-09-23 01:57:32.038485','Quotation for 100 premium business cards','QT/PF1002','ACCEPTED',1500.00,270.00,18.00,1770.00,'2026-09-30',2);
/*!40000 ALTER TABLE `quotations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','CUSTOMER','DELIVERY','PRODUCTION') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,_binary '',NULL,'Pune','2026-09-23 00:38:42.864246','admin@printflow.in','PrintFlow Admin','$2a$10$zrqFi1OvAcSyV8fmqST8Zu5IA3h3vRkPIZma/AO0ntgYukmSHV2ZK',NULL,'ADMIN'),(2,_binary '',NULL,'Pune','2026-09-23 00:38:42.961289','priya@printflow.in','Priya Sharma','$2a$10$MX51c1ZIiewxeYGhHTppLePF2FBFuzLFtb3rafzrUEg.xy5zRebQm',NULL,'CUSTOMER'),(3,_binary '',NULL,'Pune','2026-09-23 00:38:43.042430','production@printflow.in','Production Staff','$2a$10$RT9QssZkwySd1MaaBPLaneIzalAAnau9I8HCyRyLjoP7uqit6Y5BO',NULL,'PRODUCTION'),(4,_binary '',NULL,'Pune','2026-09-23 00:38:43.124753','delivery@printflow.in','Delivery Staff','$2a$10$9GhSru2NVOYRFQzI4Qma5u54llhlPuA0eBwciuda03rbMkYFj2rRa',NULL,'DELIVERY'),(5,_binary '',NULL,NULL,'2026-09-23 00:57:33.989948','testcustomer@printflow.com','Test Customer','$2a$10$GiojAk0XPhnOxmzLpFmZdOPMnGZSGO19MH87ax2pc8MfACFwZ1vY6',NULL,'CUSTOMER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-23  3:48:40
