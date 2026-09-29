# StudyBuddy — Student Study Group Formation Tool

A simple, clean college Java + DBMS assessment project to help students create and join subject-wise study groups with strict capacity limits.

---

## 📌 Project Overview
- **Project Name:** StudyBuddy
- **Project Purpose:** Helps students preparing for exams coordinate by forming small, subject-wise study groups. It provides real-time visibility into group capacity, member count, membership history, and creator member controls.
- **Application URL:** [http://localhost:8080](http://localhost:8080)

---

## 🛠 Technologies Used
- **Backend:** Java 17, Spring Boot, Spring Web, Spring Data JPA, Hibernate, Bean Validation
- **Database:** MySQL (`studybuddy_db`)
- **Frontend:** Thymeleaf template engine, HTML5, Vanilla CSS
- **Build Tool:** Maven

---

## 💻 Required Software
1. **JDK 17** (or compatible modern JDK)
2. **MySQL Server 8.0+**
3. **Maven** (or use included `mvnw.cmd` / `./mvnw`)

---

## 🗄️ Database Setup & Configuration
1. Open MySQL and ensure the server is running on port `3306`.
2. The database name is `studybuddy_db` (it is created automatically upon connection if not present).
3. Open `src/main/resources/application.properties`.
4. Update `spring.datasource.password` with your local MySQL password:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/studybuddy_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=YOUR_MYSQL_PASSWORD
   ```

---

## 🚀 How to Run the Application
From the project root folder:

### Using Maven:
```bash
mvn clean package
mvn spring-boot:run
```

### Using Maven Wrapper (Windows PowerShell/CMD):
```powershell
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

Once started, open your browser and navigate to:
👉 **[http://localhost:8080](http://localhost:8080)**

---

## ✨ Features
1. **Student Management:** Register and view students (Name, Email, Department, Year of Study).
2. **Subject Management:** Add and list subjects with Subject Name and Code.
3. **Study Group Formation:** Create study groups for a selected subject with a designated student creator and maximum capacity.
4. **Capacity Enforcement:** Prevents students from joining when group capacity (`maxMembers`) is reached.
5. **Duplicate Prevention:** Prevents the same student from joining a group more than once.
6. **Leave Group:** Students can leave a study group at any time.
7. **Creator Removal Control:** Only the group creator can remove another member.
8. **Subject-wise Filtering:** Filter and view groups belonging to specific subjects.
9. **REST API Endpoints:** Dedicated lightweight REST endpoints for Postman demonstration and viva evaluation.
