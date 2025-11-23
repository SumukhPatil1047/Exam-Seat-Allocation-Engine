-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               Microsoft SQL Server 2022 (RTM) - 16.0.1000.6
-- Server OS:                    Windows 10 Home Single Language 10.0 <X64> (Build 26200: ) (Hypervisor)
-- HeidiSQL Version:             12.4.0.6659
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES  */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for Exam_Seat_Allocation_Engine
CREATE DATABASE IF NOT EXISTS "Exam_Seat_Allocation_Engine";
USE "Exam_Seat_Allocation_Engine";

-- Dumping structure for table Exam_Seat_Allocation_Engine.Allocation
CREATE TABLE IF NOT EXISTS "Allocation" (
	"id" BIGINT NOT NULL,
	"application_post_id" BIGINT NOT NULL,
	"centre_id" BIGINT NOT NULL,
	"slot_id" BIGINT NOT NULL,
	"allocation_time" DATETIME NOT NULL,
	FOREIGN KEY INDEX "FK_Allocation_ApplicationPost" ("application_post_id"),
	FOREIGN KEY INDEX "FK_Allocation_Centre" ("centre_id"),
	FOREIGN KEY INDEX "FK_Allocation_Slot" ("slot_id"),
	PRIMARY KEY ("id"),
	CONSTRAINT "FK_Allocation_Centre" FOREIGN KEY ("centre_id") REFERENCES "Centre" ("id") ON UPDATE NO_ACTION ON DELETE NO_ACTION,
	CONSTRAINT "FK_Allocation_Slot" FOREIGN KEY ("slot_id") REFERENCES "Slot" ("id") ON UPDATE NO_ACTION ON DELETE NO_ACTION,
	CONSTRAINT "FK_Allocation_ApplicationPost" FOREIGN KEY ("application_post_id") REFERENCES "ApplicationPost" ("id") ON UPDATE NO_ACTION ON DELETE NO_ACTION
);

-- Dumping data for table Exam_Seat_Allocation_Engine.Allocation: 0 rows
/*!40000 ALTER TABLE "Allocation" DISABLE KEYS */;
/*!40000 ALTER TABLE "Allocation" ENABLE KEYS */;

-- Dumping structure for table Exam_Seat_Allocation_Engine.ApplicationPost
CREATE TABLE IF NOT EXISTS "ApplicationPost" (
	"id" BIGINT NOT NULL,
	"candidate_id" BIGINT NOT NULL,
	"applied_post" VARCHAR(255) NOT NULL COLLATE 'SQL_Latin1_General_CP1_CI_AS',
	"allocation_status" VARCHAR(20) NOT NULL DEFAULT '''PENDING''' COLLATE 'SQL_Latin1_General_CP1_CI_AS',
	FOREIGN KEY INDEX "FK_ApplicationPost_Candidate" ("candidate_id"),
	PRIMARY KEY ("id"),
	CONSTRAINT "FK_ApplicationPost_Candidate" FOREIGN KEY ("candidate_id") REFERENCES "Candidate" ("id") ON UPDATE NO_ACTION ON DELETE NO_ACTION
);

-- Dumping data for table Exam_Seat_Allocation_Engine.ApplicationPost: 5 rows
/*!40000 ALTER TABLE "ApplicationPost" DISABLE KEYS */;
INSERT INTO "ApplicationPost" ("id", "candidate_id", "applied_post", "allocation_status") VALUES
	(1, 1, 'Tech Assistant', 'PENDING'),
	(2, 1, 'Clerk', 'PENDING'),
	(3, 2, 'Clerk', 'PENDING'),
	(4, 3, 'Data Entry Operator', 'PENDING'),
	(5, 4, 'Tech Assistant', 'PENDING');
/*!40000 ALTER TABLE "ApplicationPost" ENABLE KEYS */;

-- Dumping structure for table Exam_Seat_Allocation_Engine.Candidate
CREATE TABLE IF NOT EXISTS "Candidate" (
	"id" BIGINT NOT NULL,
	"registration_number" INT NOT NULL,
	"candidate_name" VARCHAR(255) NOT NULL COLLATE 'SQL_Latin1_General_CP1_CI_AS',
	"gender" VARCHAR(10) NOT NULL COLLATE 'SQL_Latin1_General_CP1_CI_AS',
	"is_pwd" BIT NOT NULL,
	PRIMARY KEY ("id")
);

-- Dumping data for table Exam_Seat_Allocation_Engine.Candidate: 4 rows
/*!40000 ALTER TABLE "Candidate" DISABLE KEYS */;
INSERT INTO "Candidate" ("id", "registration_number", "candidate_name", "gender", "is_pwd") VALUES
	(1, 101, 'Abhay', 'M', b'0'),
	(2, 102, 'Sonam', 'F', b'0'),
	(3, 103, 'Ritika', 'F', b'0'),
	(4, 104, 'Akshay', 'M', b'1');
/*!40000 ALTER TABLE "Candidate" ENABLE KEYS */;

-- Dumping structure for table Exam_Seat_Allocation_Engine.Centre
CREATE TABLE IF NOT EXISTS "Centre" (
	"id" BIGINT NOT NULL,
	"center_name" VARCHAR(255) NOT NULL COLLATE 'SQL_Latin1_General_CP1_CI_AS',
	"capacity" INT NOT NULL,
	"is_pwd_friendly" BIT NOT NULL,
	PRIMARY KEY ("id")
);

-- Dumping data for table Exam_Seat_Allocation_Engine.Centre: 3 rows
/*!40000 ALTER TABLE "Centre" DISABLE KEYS */;
INSERT INTO "Centre" ("id", "center_name", "capacity", "is_pwd_friendly") VALUES
	(1, 'MIT College Pune', 30, b'1'),
	(2, 'COEP Pune', 25, b'0'),
	(3, 'Government Engg College Pune', 20, b'1');
/*!40000 ALTER TABLE "Centre" ENABLE KEYS */;

-- Dumping structure for table Exam_Seat_Allocation_Engine.Slot
CREATE TABLE IF NOT EXISTS "Slot" (
	"id" BIGINT NOT NULL,
	"slot_time" VARCHAR(50) NOT NULL COLLATE 'SQL_Latin1_General_CP1_CI_AS',
	"exam_date" DATE NOT NULL,
	PRIMARY KEY ("id")
);

-- Dumping data for table Exam_Seat_Allocation_Engine.Slot: 3 rows
/*!40000 ALTER TABLE "Slot" DISABLE KEYS */;
INSERT INTO "Slot" ("id", "slot_time", "exam_date") VALUES
	(1, '09:00–10:30', '2025-09-15'),
	(2, '12:30–14:00', '2025-09-15'),
	(3, '16:00–17:30', '2025-09-15');
/*!40000 ALTER TABLE "Slot" ENABLE KEYS */;

-- Dumping structure for procedure Exam_Seat_Allocation_Engine.sp_CentreSlotCapacity
DELIMITER //
CREATE PROCEDURE sp_CentreSlotCapacity
AS
BEGIN
    SELECT 
        c.center_name,
        s.slot_time,
        s.exam_date,
        c.capacity,
        (SELECT COUNT(*) FROM Allocation a
         WHERE a.centre_id = c.id AND a.slot_id = s.id) AS allocated,
        c.capacity - 
        (SELECT COUNT(*) FROM Allocation a
         WHERE a.centre_id = c.id AND a.slot_id = s.id) AS remaining
    FROM Centre c
    CROSS JOIN Slot s;
END//
DELIMITER ;

-- Dumping structure for procedure Exam_Seat_Allocation_Engine.sp_GetCandidateAllocation
DELIMITER //
CREATE PROCEDURE sp_GetCandidateAllocation @regNo INT
AS
BEGIN
    SELECT c.candidate_name, ap.applied_post, ap.allocation_status,
           ce.center_name, sl.slot_time, sl.exam_date
    FROM Candidate c
    JOIN ApplicationPost ap ON c.id = ap.candidate_id
    LEFT JOIN Allocation a ON ap.id = a.application_post_id
    LEFT JOIN Centre ce ON ce.id = a.centre_id
    LEFT JOIN Slot sl ON sl.id = a.slot_id
    WHERE c.registration_number = @regNo;
END//
DELIMITER ;

-- Dumping structure for procedure Exam_Seat_Allocation_Engine.sp_ResetAllocations
DELIMITER //
CREATE PROCEDURE sp_ResetAllocations
AS
BEGIN
    UPDATE ApplicationPost SET allocation_status = 'PENDING';
    DELETE FROM Allocation;
END
//
DELIMITER ;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
