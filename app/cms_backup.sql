-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: cms
-- ------------------------------------------------------
-- Server version	8.0.43

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

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'admin@1234','admin');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKmi8vkhus4xbdbqcac2jm4spvd` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `appointment_time` datetime(6) DEFAULT NULL,
  `status` int NOT NULL,
  `doctor_id` bigint NOT NULL,
  `patient_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoeb98n82eph1dx43v3y2bcmsl` (`doctor_id`),
  KEY `FK4apif2ewfyf14077ichee8g06` (`patient_id`),
  CONSTRAINT `FK4apif2ewfyf14077ichee8g06` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`),
  CONSTRAINT `FKoeb98n82eph1dx43v3y2bcmsl` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES (1,'2025-05-01 09:00:00.000000',0,1,1),(2,'2025-05-02 10:00:00.000000',0,1,2),(3,'2025-05-03 11:00:00.000000',0,1,3),(4,'2025-05-04 14:00:00.000000',0,1,4),(5,'2025-05-05 15:00:00.000000',0,1,5),(6,'2025-05-06 13:00:00.000000',0,1,6),(7,'2025-05-07 09:00:00.000000',0,1,7),(8,'2025-05-08 16:00:00.000000',0,1,8),(9,'2025-05-09 11:00:00.000000',0,1,9),(10,'2025-05-10 10:00:00.000000',0,1,10),(11,'2025-05-11 12:00:00.000000',0,1,11),(12,'2025-05-12 15:00:00.000000',0,1,12),(13,'2025-05-13 13:00:00.000000',0,1,13),(14,'2025-05-14 10:00:00.000000',0,1,14),(15,'2025-05-15 11:00:00.000000',0,1,15),(16,'2025-05-16 14:00:00.000000',0,1,16),(17,'2025-05-17 09:00:00.000000',0,1,17),(18,'2025-05-18 12:00:00.000000',0,1,18),(19,'2025-05-19 13:00:00.000000',0,1,19),(20,'2025-05-20 16:00:00.000000',0,1,20),(21,'2025-05-21 14:00:00.000000',0,1,21),(22,'2025-05-22 10:00:00.000000',0,1,22),(23,'2025-05-23 11:00:00.000000',0,1,23),(24,'2025-05-24 15:00:00.000000',0,1,24),(25,'2025-05-25 09:00:00.000000',0,1,25),(26,'2025-05-01 10:00:00.000000',0,2,1),(27,'2025-05-02 11:00:00.000000',0,3,2),(30,'2025-05-05 10:00:00.000000',0,6,5),(31,'2025-05-06 11:00:00.000000',0,7,6),(33,'2025-05-08 15:00:00.000000',0,9,8),(34,'2025-05-09 10:00:00.000000',0,10,9),(35,'2025-05-10 14:00:00.000000',0,11,10),(36,'2025-05-11 13:00:00.000000',0,12,11),(37,'2025-05-12 14:00:00.000000',0,13,12),(38,'2025-05-13 15:00:00.000000',0,14,13),(39,'2025-05-14 10:00:00.000000',0,15,14),(40,'2025-05-15 11:00:00.000000',0,16,15),(41,'2025-05-16 14:00:00.000000',0,17,16),(42,'2025-05-17 10:00:00.000000',0,18,17),(43,'2025-05-18 13:00:00.000000',0,19,18),(44,'2025-05-19 14:00:00.000000',0,20,19),(45,'2025-05-20 11:00:00.000000',0,21,20),(46,'2025-05-21 13:00:00.000000',0,22,21),(47,'2025-05-22 14:00:00.000000',0,23,22),(48,'2025-05-23 10:00:00.000000',0,24,23),(49,'2025-05-24 15:00:00.000000',0,25,24),(50,'2025-05-25 13:00:00.000000',0,25,25),(51,'2025-04-01 10:00:00.000000',1,1,2),(52,'2025-04-02 11:00:00.000000',1,2,3),(53,'2025-04-03 14:00:00.000000',1,3,4),(56,'2025-04-06 11:00:00.000000',1,6,7),(57,'2025-04-07 14:00:00.000000',1,7,8),(59,'2025-04-09 10:00:00.000000',1,9,10),(60,'2025-04-10 14:00:00.000000',1,10,11),(61,'2025-04-11 13:00:00.000000',1,11,12),(62,'2025-04-12 14:00:00.000000',1,12,13),(63,'2025-04-13 15:00:00.000000',1,13,14),(64,'2025-04-14 10:00:00.000000',1,14,15),(65,'2025-04-15 11:00:00.000000',1,15,16),(66,'2025-04-16 14:00:00.000000',1,16,17),(67,'2025-04-17 10:00:00.000000',1,17,18),(68,'2025-04-18 13:00:00.000000',1,18,19),(69,'2025-04-19 14:00:00.000000',1,19,20),(70,'2025-04-20 11:00:00.000000',1,20,21),(71,'2025-04-21 13:00:00.000000',1,21,22),(72,'2025-04-22 14:00:00.000000',1,22,23),(73,'2025-04-23 10:00:00.000000',1,23,24),(74,'2025-04-24 15:00:00.000000',1,24,25),(75,'2025-04-25 13:00:00.000000',1,25,25),(76,'2025-04-01 09:00:00.000000',1,1,1),(77,'2025-04-02 10:00:00.000000',1,1,2),(78,'2025-04-03 11:00:00.000000',1,1,3),(79,'2025-04-04 14:00:00.000000',1,1,4),(80,'2025-04-05 10:00:00.000000',1,1,5),(81,'2025-04-10 10:00:00.000000',1,1,6),(82,'2025-04-11 09:00:00.000000',1,1,7),(83,'2025-04-14 13:00:00.000000',1,1,8),(84,'2025-04-01 10:00:00.000000',1,2,1),(85,'2025-04-01 11:00:00.000000',1,2,2),(86,'2025-04-02 09:00:00.000000',1,2,3),(87,'2025-04-02 10:00:00.000000',1,2,4),(88,'2025-04-03 11:00:00.000000',1,2,5),(89,'2025-04-03 12:00:00.000000',1,2,6),(90,'2025-04-04 14:00:00.000000',1,2,7),(91,'2025-04-04 15:00:00.000000',1,2,8),(92,'2025-04-05 10:00:00.000000',1,2,9),(93,'2025-04-05 11:00:00.000000',1,2,10),(94,'2025-04-06 13:00:00.000000',1,2,11),(95,'2025-04-06 14:00:00.000000',1,2,12),(96,'2025-04-07 09:00:00.000000',1,2,13),(97,'2025-04-07 10:00:00.000000',1,2,14),(98,'2025-04-08 11:00:00.000000',1,2,15),(99,'2025-04-08 12:00:00.000000',1,2,16),(100,'2025-04-09 13:00:00.000000',1,2,17),(101,'2025-04-09 14:00:00.000000',1,2,18),(102,'2025-04-10 11:00:00.000000',1,2,19),(103,'2025-04-10 12:00:00.000000',1,2,20),(104,'2025-04-11 14:00:00.000000',1,2,21),(105,'2025-04-11 15:00:00.000000',1,2,22),(106,'2025-04-12 10:00:00.000000',1,2,23),(107,'2025-04-12 11:00:00.000000',1,2,24),(108,'2025-04-13 13:00:00.000000',1,2,25),(109,'2025-04-13 14:00:00.000000',1,2,1),(110,'2025-04-14 09:00:00.000000',1,2,2),(111,'2025-04-14 10:00:00.000000',1,2,3),(112,'2025-04-15 12:00:00.000000',1,2,4),(113,'2025-04-15 13:00:00.000000',1,2,5),(114,'2025-04-01 12:00:00.000000',1,3,1),(115,'2025-04-02 11:00:00.000000',1,3,2),(116,'2025-04-03 13:00:00.000000',1,3,3),(117,'2025-04-04 15:00:00.000000',1,3,4),(118,'2025-04-05 12:00:00.000000',1,3,5),(119,'2025-04-08 13:00:00.000000',1,3,6),(120,'2025-04-09 10:00:00.000000',1,3,7),(121,'2025-04-10 14:00:00.000000',1,3,8),(122,'2025-04-11 13:00:00.000000',1,3,9),(123,'2025-04-12 09:00:00.000000',1,3,10),(131,'2025-10-03 14:00:00.000000',0,25,26),(132,'2025-09-18 09:00:00.000000',0,1,26),(133,'2025-09-17 16:00:00.000000',0,3,26),(134,'2025-09-18 09:00:00.000000',0,6,26),(135,'2025-09-18 14:00:00.000000',0,1,26),(136,'2025-09-19 16:00:00.000000',0,3,26),(137,'2025-09-25 16:00:00.000000',0,3,26),(138,'2025-09-18 16:00:00.000000',0,3,26),(139,'2025-09-19 14:00:00.000000',0,3,26),(140,'2025-09-20 15:00:00.000000',0,7,26),(141,'2025-09-20 16:00:00.000000',0,3,26),(142,'2025-09-20 14:00:00.000000',0,3,26),(143,'2025-09-20 11:00:00.000000',0,1,26),(144,'2025-09-20 14:00:00.000000',0,1,26),(145,'2025-09-26 14:00:00.000000',0,9,26),(146,'2025-09-20 11:00:00.000000',0,3,1);
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `appointment_time` datetime(6) NOT NULL,
  `status` int NOT NULL,
  `doctor_id` bigint NOT NULL,
  `patient_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmujeo4tymoo98cmf7uj3vsv76` (`doctor_id`),
  KEY `FK8exap5wmg8kmb1g1rx3by21yt` (`patient_id`),
  CONSTRAINT `FK8exap5wmg8kmb1g1rx3by21yt` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `FKmujeo4tymoo98cmf7uj3vsv76` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `specialty` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
INSERT INTO `doctor` VALUES (1,'dr.adams@example.com','Dr. Emily Adams','pass12345','555-101-2020','Cardiologist'),(2,'dr.johnson@example.com','Dr. Mark Johnson','secure4567','555-202-3030','Neurologist'),(3,'dr.lee@example.com','Dr. Sarah Lee','leePass987','555-303-4040','Orthopedist'),(6,'dr.taylor@example.com','Dr. Taylor Grant','taylor321','555-606-7070','Cardiologist'),(7,'dr.white@example.com','Dr. Sam White','whiteSecure1','555-707-8080','Neurologist'),(9,'dr.davis@example.com','Dr. Olivia Davis','davis789','555-909-0101','Pediatrician'),(10,'dr.miller@example.com','Dr. Henry Miller','millertime!','555-010-1111','Dermatologist'),(11,'dr.moore@example.com','Dr. Ella Moore','ellapass33','555-111-2222','Cardiologist'),(12,'dr.martin@example.com','Dr. Leo Martin','martinpass','555-222-3333','Neurologist'),(13,'dr.jackson@example.com','Dr. Ivy Jackson','jackson11','555-333-4444','Orthopedist'),(14,'dr.thomas@example.com','Dr. Owen Thomas','thomasPWD','555-444-5555','Pediatrician'),(15,'dr.hall@example.com','Dr. Ava Hall','hallhall','555-555-6666','Dermatologist'),(16,'dr.green@example.com','Dr. Mia Green','greenleaf','555-666-7777','Cardiologist'),(17,'dr.baker@example.com','Dr. Jack Baker','bakeitup','555-777-8888','Neurologist'),(18,'dr.walker@example.com','Dr. Nora Walker','walkpass12','555-888-9999','Orthopedist'),(19,'dr.young@example.com','Dr. Liam Young','young123','555-999-0000','Pediatrician'),(20,'dr.king@example.com','Dr. Zoe King','kingkong1','555-000-1111','Dermatologist'),(21,'dr.scott@example.com','Dr. Lily Scott','scottish','555-111-2223','Cardiologist'),(22,'dr.evans@example.com','Dr. Lucas Evans','evansEv1','555-222-3334','Neurologist'),(23,'dr.turner@example.com','Dr. Grace Turner','turnerBurner','555-333-4445','Orthopedist'),(24,'dr.hill@example.com','Dr. Ethan Hill','hillclimb','555-444-5556','Pediatrician'),(25,'dr.ward@example.com','Dr. Ruby Ward','wardWard','555-555-6667','Dermatologist'),(26,'dr.parker@example.com','Peter Parker','test123','1234567890','urologist');
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor_available_times`
--

DROP TABLE IF EXISTS `doctor_available_times`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_available_times` (
  `doctor_id` bigint NOT NULL,
  `available_times` varchar(255) DEFAULT NULL,
  `time_slot` varchar(255) DEFAULT NULL,
  KEY `FKdgs10srq75djpwnb9c22k3lmk` (`doctor_id`),
  CONSTRAINT `FKdgs10srq75djpwnb9c22k3lmk` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_available_times`
--

LOCK TABLES `doctor_available_times` WRITE;
/*!40000 ALTER TABLE `doctor_available_times` DISABLE KEYS */;
INSERT INTO `doctor_available_times` VALUES (1,'09:00-10:00',NULL),(1,'10:00-11:00',NULL),(1,'11:00-12:00',NULL),(1,'14:00-15:00',NULL),(2,'10:00-11:00',NULL),(2,'11:00-12:00',NULL),(2,'14:00-15:00',NULL),(2,'15:00-16:00',NULL),(3,'09:00-10:00',NULL),(3,'11:00-12:00',NULL),(3,'14:00-15:00',NULL),(3,'16:00-17:00',NULL),(6,'09:00-10:00',NULL),(6,'10:00-11:00',NULL),(6,'11:00-12:00',NULL),(6,'14:00-15:00',NULL),(7,'09:00-10:00',NULL),(7,'10:00-11:00',NULL),(7,'15:00-16:00',NULL),(7,'16:00-17:00',NULL),(9,'09:00-10:00',NULL),(9,'11:00-12:00',NULL),(9,'13:00-14:00',NULL),(9,'14:00-15:00',NULL),(10,'10:00-11:00',NULL),(10,'11:00-12:00',NULL),(10,'14:00-15:00',NULL),(10,'16:00-17:00',NULL),(11,'09:00-10:00',NULL),(11,'12:00-13:00',NULL),(11,'14:00-15:00',NULL),(11,'15:00-16:00',NULL),(12,'10:00-11:00',NULL),(12,'11:00-12:00',NULL),(12,'13:00-14:00',NULL),(12,'14:00-15:00',NULL),(13,'13:00-14:00',NULL),(13,'14:00-15:00',NULL),(13,'15:00-16:00',NULL),(13,'16:00-17:00',NULL),(14,'09:00-10:00',NULL),(14,'10:00-11:00',NULL),(14,'14:00-15:00',NULL),(14,'16:00-17:00',NULL),(15,'10:00-11:00',NULL),(15,'11:00-12:00',NULL),(15,'13:00-14:00',NULL),(15,'14:00-15:00',NULL),(16,'09:00-10:00',NULL),(16,'11:00-12:00',NULL),(16,'14:00-15:00',NULL),(16,'16:00-17:00',NULL),(17,'09:00-10:00',NULL),(17,'10:00-11:00',NULL),(17,'11:00-12:00',NULL),(17,'12:00-13:00',NULL),(18,'09:00-10:00',NULL),(18,'10:00-11:00',NULL),(18,'11:00-12:00',NULL),(18,'15:00-16:00',NULL),(19,'13:00-14:00',NULL),(19,'14:00-15:00',NULL),(19,'15:00-16:00',NULL),(19,'16:00-17:00',NULL),(20,'10:00-11:00',NULL),(20,'13:00-14:00',NULL),(20,'14:00-15:00',NULL),(20,'15:00-16:00',NULL),(21,'09:00-10:00',NULL),(21,'10:00-11:00',NULL),(21,'14:00-15:00',NULL),(21,'15:00-16:00',NULL),(22,'10:00-11:00',NULL),(22,'11:00-12:00',NULL),(22,'14:00-15:00',NULL),(22,'16:00-17:00',NULL),(23,'11:00-12:00',NULL),(23,'13:00-14:00',NULL),(23,'15:00-16:00',NULL),(23,'16:00-17:00',NULL),(24,'12:00-13:00',NULL),(24,'13:00-14:00',NULL),(24,'14:00-15:00',NULL),(24,'15:00-16:00',NULL),(25,'09:00-10:00',NULL),(25,'10:00-11:00',NULL),(25,'14:00-15:00',NULL),(25,'15:00-16:00',NULL),(26,'11:00-12:00',NULL),(26,'12:00-13:00',NULL);
/*!40000 ALTER TABLE `doctor_available_times` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctors`
--

DROP TABLE IF EXISTS `doctors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `specialty` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKcaifv0va46t2mu85cg5afmayf` (`email`),
  UNIQUE KEY `UKcwo2odv69yn42syib6wt69rs6` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors` DISABLE KEYS */;
/*!40000 ALTER TABLE `doctors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (1,'101 Oak St, Cityville','jane.doe@example.com','Jane Doe','passJane1','888-111-1111'),(2,'202 Maple Rd, Townsville','john.smith@example.com','John Smith','smithSecure','888-222-2222'),(3,'303 Pine Ave, Villageton','emily.rose@example.com','Emily Rose','emilyPass99','888-333-3333'),(4,'404 Birch Ln, Metropolis','michael.j@example.com','Michael Jordan','airmj23','888-444-4444'),(5,'505 Cedar Blvd, Springfield','olivia.m@example.com','Olivia Moon','moonshine12','888-555-5555'),(6,'606 Spruce Ct, Gotham','liam.k@example.com','Liam King','king321','888-666-6666'),(7,'707 Aspen Dr, Riverdale','sophia.l@example.com','Sophia Lane','sophieLane','888-777-7777'),(8,'808 Elm St, Newtown','noah.b@example.com','Noah Brooks','noahBest!','888-888-8888'),(9,'909 Willow Way, Star City','ava.d@example.com','Ava Daniels','avaSecure8','888-999-9999'),(10,'111 Chestnut Pl, Midvale','william.h@example.com','William Harris','willH2025','888-000-0000'),(11,'112 Redwood St, Fairview','mia.g@example.com','Mia Green','miagreen1','889-111-1111'),(12,'113 Cypress Rd, Edgewater','james.b@example.com','James Brown','jamiebrown','889-222-2222'),(13,'114 Poplar Ave, Crestwood','amelia.c@example.com','Amelia Clark','ameliacool','889-333-3333'),(14,'115 Sequoia Dr, Elmwood','ben.j@example.com','Ben Johnson','bennyJ','889-444-4444'),(15,'116 Palm Blvd, Harborview','ella.m@example.com','Ella Monroe','ellam123','889-555-5555'),(16,'117 Cottonwood Ct, Laketown','lucas.t@example.com','Lucas Turner','lucasTurn','889-666-6666'),(17,'118 Sycamore Ln, Hilltop','grace.s@example.com','Grace Scott','graceful','889-777-7777'),(18,'119 Magnolia Pl, Brookside','ethan.h@example.com','Ethan Hill','hill2025','889-888-8888'),(19,'120 Fir St, Woodland','ruby.w@example.com','Ruby Ward','rubypass','889-999-9999'),(20,'121 Beech Way, Lakeside','jack.b@example.com','Jack Baker','bakerjack','889-000-0000'),(21,'122 Alder Ave, Pinehill','mia.h@example.com','Mia Hall','hallMia','890-111-1111'),(22,'123 Hawthorn Blvd, Meadowbrook','owen.t@example.com','Owen Thomas','owen123','890-222-2222'),(23,'124 Dogwood Dr, Summit','ivy.j@example.com','Ivy Jackson','ivyIvy','890-333-3333'),(24,'125 Juniper Ct, Greenwood','leo.m@example.com','Leo Martin','leopass','890-444-4444'),(25,'126 Olive Rd, Ashville','ella.moore@example.com','Ella Moore','ellamoore','890-555-5555'),(26,'test','test1@example.com','Piotr','test123','1234567890');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patients` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKa370hmxgv0l5c9panryr1ji7d` (`email`),
  UNIQUE KEY `UK3losa44agqkfqpkxfdv7wf1dq` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
/*!40000 ALTER TABLE `patients` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-20 12:05:51
