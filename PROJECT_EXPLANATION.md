# StudyBuddy — Project Explanation & Viva Guide

This document contains short, student-friendly explanations of every architectural layer and business rule in the project for exam and viva preparation.

---

## 1. Project Flow
```
Browser / Postman Request
        ↓
Controller (receives HTTP requests, validates input, calls Service, returns View or JSON)
        ↓
Service (contains core business logic, capacity check, duplicate check, creator check)
        ↓
Repository (Spring Data JPA interfaces with derived query methods)
        ↓
Hibernate (ORM provider generating SQL queries)
        ↓
MySQL Database (persists tables and foreign keys)
```

---

## 2. Entities & Database Tables

The application contains **4 normalized entities**:

1. **`Student` (`student` table)**
   - Fields: `id`, `studentName`, `email`, `department`, `yearOfStudy`.
   - Purpose: Stores student master records.

2. **`Subject` (`subject` table)**
   - Fields: `id`, `subjectName`, `subjectCode`.
   - Purpose: Stores academic subject master records (e.g., DBMS, Operating Systems).

3. **`StudyGroup` (`study_group` table)**
   - Fields: `id`, `groupName`, `subject_id` (FK), `creator_id` (FK), `maxMembers`, `description`, `createdDate`.
   - Notice: `currentMembers` is **not** stored as a column. It is dynamically counted from the `membership` table to avoid data inconsistency.

4. **`Membership` (`membership` table)**
   - Fields: `id`, `study_group_id` (FK), `student_id` (FK), `joinedAt`.
   - Acts as the intermediate entity managing the relationship between `Student` and `StudyGroup`.

---

## 3. Relationships & Foreign Keys
- **Subject to StudyGroup (1 : N):**
  One Subject can have many StudyGroups (`@ManyToOne` in `StudyGroup` pointing to `Subject`).
  Foreign key: `study_group.subject_id`.
- **Student to StudyGroup (1 : N for Creation):**
  One Student can create many StudyGroups (`@ManyToOne` in `StudyGroup` pointing to `Student creator`).
  Foreign key: `study_group.creator_id`.
- **Student to StudyGroup (M : N through Membership):**
  Rather than a direct `@ManyToMany`, we use the intermediate `Membership` entity with `@ManyToOne` to `StudyGroup` and `@ManyToOne` to `Student`.
  Foreign keys: `membership.study_group_id`, `membership.student_id`.
  **Why?** This lets us track extra attributes like `joinedAt` (join timestamp).

---

## 4. Architecture Layers

### A. Controller Layer
- Annotated with `@Controller` (for Thymeleaf pages) and `@RestController` (for REST endpoints).
- Extracts request parameters, passes data to services, and binds results to the Thymeleaf `Model` or returns `ResponseEntity`.
- **Crucial Rule:** Controllers do **not** perform business validations (like group capacity checks); they delegate to Services.

### B. Service Layer
- Annotated with `@Service` and `@Transactional`.
- Contains all business rules, checks constraints, and coordinates repository calls.
- Throws meaningful custom exceptions (`BusinessRuleException`, `ResourceNotFoundException`) when rules are violated.

### C. Repository Layer
- Extends `JpaRepository<Entity, Long>`.
- Uses Spring Data JPA derived method names (e.g., `countByStudyGroupId`, `existsByStudyGroupIdAndStudentId`) without manual SQL.

### D. JPA & Hibernate
- **JPA (Jakarta Persistence API):** Standard Java specification for Object-Relational Mapping (ORM).
- **Hibernate:** The underlying ORM implementation that converts Java entity objects and relationships into SQL queries and executes them against MySQL.

---

## 5. Core Business Rules Implemented in `MembershipService`

### Rule 1: Group Capacity Limit Check
- **Code:**
  ```java
  long currentMembers = membershipRepository.countByStudyGroupId(groupId);
  if (currentMembers >= studyGroup.getMaxMembers())
  {
      throw new BusinessRuleException("Study group is full");
  }
  ```
- **Explanation:** Before adding a member, the system counts the existing memberships for that group. If the count has reached `maxMembers`, the join request is rejected.

### Rule 2: Duplicate Membership Check
- **Code:**
  ```java
  if (membershipRepository.existsByStudyGroupIdAndStudentId(groupId, studentId))
  {
      throw new BusinessRuleException("Student is already a member of this group");
  }
  ```
- **Explanation:** Ensures a student cannot join the same study group twice.

### Rule 3: Creator-Only Member Removal Check
- **Code:**
  ```java
  if (!studyGroup.getCreator().getId().equals(requesterId))
  {
      throw new BusinessRuleException("Only the group creator can remove members");
  }
  ```
- **Explanation:** Only the student who created the group is authorized to remove another member. Any attempt by a non-creator is rejected.

---

## 6. REST API Endpoints (`StudyBuddyRestController`)

| Method | Endpoint | Description | HTTP Status |
|---|---|---|---|
| `POST` | `/api/groups` | Create a new StudyGroup | `201 Created` |
| `POST` | `/api/groups/{groupId}/join/{studentId}` | Join group with capacity & duplicate checks | `200 OK` / `409 Conflict` |
| `DELETE` | `/api/groups/{groupId}/leave/{studentId}` | Leave a group | `200 OK` / `400 Bad Request` |
| `GET` | `/api/groups/subject/{subjectId}` | Get all study groups for a given subject | `200 OK` |
| `DELETE` | `/api/groups/{groupId}/members/{studentId}?requesterId=1` | Creator removes a group member | `200 OK` / `400 Bad Request` |

---

## 7. Thymeleaf Frontend Flow
- Uses standard Spring MVC with server-side template rendering.
- Layout styled via `style.css` without external JS frameworks.
- Clean user alerts:
  - Success messages: Green notification card (`successMessage` flash attribute).
  - Business rule violations: Red notification card (`errorMessage` flash attribute).
- Status indicators automatically calculate and display `Available` vs `Full` based on real-time membership counts.
