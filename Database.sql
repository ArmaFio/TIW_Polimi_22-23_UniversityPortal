-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: project_verbalizzazione_esami
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `appelli`
--

DROP TABLE IF EXISTS `appelli`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appelli` (
  `CodiceCorso` varchar(45) NOT NULL,
  `Data` date NOT NULL,
  PRIMARY KEY (`CodiceCorso`,`Data`),
  CONSTRAINT `CodiceCorso` FOREIGN KEY (`CodiceCorso`) REFERENCES `corsi` (`CodiceCorso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `corsi`
--

DROP TABLE IF EXISTS `corsi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `corsi` (
  `CodiceCorso` varchar(45) NOT NULL,
  `NomeCorso` varchar(45) NOT NULL,
  `Docente` varchar(45) NOT NULL,
  PRIMARY KEY (`CodiceCorso`),
  KEY `Docente_idx` (`Docente`),
  CONSTRAINT `Docente` FOREIGN KEY (`Docente`) REFERENCES `utenti` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iscrizioniappelli`
--

DROP TABLE IF EXISTS `iscrizioniappelli`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `iscrizioniappelli` (
  `CodiceCorso` varchar(45) NOT NULL,
  `Data` date NOT NULL,
  `Matricola` int NOT NULL,
  `StatoValutazione` int NOT NULL,
  `Voto` int DEFAULT NULL,
  `Verbale` int DEFAULT NULL,
  PRIMARY KEY (`CodiceCorso`,`Data`,`Matricola`),
  KEY `Data_idx` (`Data`),
  KEY `Verbale_idx` (`Verbale`),
  KEY `Date_idx` (`Data`),
  KEY `Verbal_idx` (`Verbale`),
  KEY `Codice Corso_idx` (`CodiceCorso`),
  KEY `Matricola_idx` (`Matricola`),
  CONSTRAINT `Codice Corso` FOREIGN KEY (`CodiceCorso`) REFERENCES `appelli` (`CodiceCorso`),
  CONSTRAINT `Verbale` FOREIGN KEY (`Verbale`) REFERENCES `verbali` (`NumVerbale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iscrizionicorsi`
--

DROP TABLE IF EXISTS `iscrizionicorsi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `iscrizionicorsi` (
  `Corso` varchar(45) NOT NULL,
  `Matricola` int NOT NULL,
  PRIMARY KEY (`Corso`,`Matricola`),
  CONSTRAINT `Corso` FOREIGN KEY (`Corso`) REFERENCES `corsi` (`CodiceCorso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `utenti`
--

DROP TABLE IF EXISTS `utenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utenti` (
  `ID` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `Nome` varchar(45) NOT NULL,
  `Cognome` varchar(45) NOT NULL,
  `Status` int NOT NULL,
  `Matricola` int DEFAULT NULL,
  `Mail` varchar(45) DEFAULT NULL,
  `CorsoLaurea` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Matricola_UNIQUE` (`Matricola`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `verbali`
--

DROP TABLE IF EXISTS `verbali`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `verbali` (
  `NumVerbale` int NOT NULL AUTO_INCREMENT,
  `DataOra` timestamp(6) NOT NULL,
  PRIMARY KEY (`NumVerbale`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-31 12:37:52
