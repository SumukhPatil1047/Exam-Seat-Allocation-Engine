# Exam Seat Allocation Engine  
Java 8 | Spring Boot | Microsoft SQL Server | REST APIs | Stored Procedures

This project implements a complete Exam Seat Allocation System that assigns
candidates to exam centres and slots based on multiple constraints:

✔ Fair distribution  
✔ PWD-friendly allocation  
✔ Slot balancing  
✔ Centre capacity handling  
✔ Reporting (Daily, Candidate-wise, Centre-wise)

---

## 📌 Tech Stack

- Java 8  
- Spring Boot  
- Spring Data JPA  
- Microsoft SQL Server  
- Swagger (springdoc-openapi)  
- Stored Procedures  
- Postman (API testing)  

---

## 📁 Project Structure

src/main/java/com/example/examengine
│── controller/
│── service/
│── service/impl/
│── repository/
│── entity/
│── dto/
│── config/


---

## 🚀 How to Run the Project

### 1️⃣ Create Database in SQL Server

Run SQL scripts inside `/sql/` folder:

- `001_create_tables.sql`
- `002_insert_sample_data.sql`
- `003_stored_procedures.sql`

---

### 2️⃣ Configure `application.properties`

server.port=8082
spring.datasource.url=jdbc:sqlserver://dbhost:dbport;databaseName=dbname
spring.datasource.username=userusername
spring.datasource.password=yourPassword
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true


---

### 3️⃣ Start Application

```bash
mvn spring-boot:run

📘 API Documentation (Swagger)

Once app is running:

👉 http://localhost:8082/swagger-ui/index.html

🧪 Postman Collection

The collection is included in:

/postman/ExamSeatAllocation.postman_collection.json


Set environment variable:

baseUrl = http://localhost:8082/api

🔥 Core APIs
Run Seat Allocation

POST /api/allocate

Get Candidate Allocation

GET /api/allocation/{registrationNumber}

Daily Report

GET /api/report/daily?date=YYYY-MM-DD

Get Candidate Status

GET /api/status/{registrationNumber}

Reset Allocations

POST /api/reset

Centre Remaining Capacity

GET /api/capacity/remaining

🧠 Allocation Logic (Summary)

Load all candidates
Sort by:
PWD first
Applied post priority
Registration number
Pick nearest preferred centre with available seats
Pick next available exam slot
Save allocation to DB

📦 Stored Procedures
sp_GetCandidateAllocation
sp_DailyReport
sp_CapacityReport

👨‍💻 Developer

Sumukh Patil
Java | Spring Boot

✔ SQL data
✔ Postman collection
✔ Professional README
✔ Swagger documentation
✔ A GitHub repo perfect for your job assignment
