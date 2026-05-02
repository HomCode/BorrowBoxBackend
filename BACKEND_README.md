# 🚀 BorrowBox Backend API

**Campus Item Borrowing System - REST API**

Built with Spring Boot 3.1.5, PostgreSQL, and Supabase

---

## 📋 Project Structure (Vertical Slicing)

The backend is organized by **features/modules** following vertical slicing approach:

```
src/main/java/com/example/BorrowBoxBackend/
│
├── 📦 AUTHENTICATION MODULE
│   ├── controller/AuthController.java     (Login/Register endpoints)
│   ├── service/AuthService.java           (Business logic)
│   ├── service/SupabaseAuthService.java   (Supabase integration)
│   └── dto/
│       ├── LoginRequest.java
│       ├── RegisterRequest.java
│       └── AuthResponse.java
│
├── 👤 PROFILE MODULE  
│   ├── controller/ProfileController.java  (Profile endpoints)
│   └── model/User.java                   (User entity)
│
├── 📦 ITEM MODULE (Inventory Management)
│   ├── controller/ItemController.java     (Item CRUD endpoints)
│   ├── service/ItemService.java           (Business logic: search, filter, CRUD)
│   ├── repository/ItemRepository.java    (Database queries)
│   ├── model/Item.java                   (Item entity)
│   └── dto/
│       ├── ItemDTO.java
│       └── request/ItemRequest.java
│
├── 🔄 BORROW MODULE (Borrow Management)
│   ├── controller/BorrowController.java   (Borrow endpoints: borrow, return)
│   ├── service/BorrowService.java         (Business logic: borrow, return, tracking)
│   ├── repository/BorrowRepository.java  (Database queries)
│   ├── model/Borrow.java                 (Borrow entity)
│   └── dto/
│       ├── BorrowDTO.java
│       ├── request/BorrowRequest.java
│       └── request/ReturnBorrowRequest.java
│
├── 📊 TRANSACTION MODULE (Tracking)
│   ├── controller/TransactionController.java  (Transaction endpoints)
│   ├── service/TransactionService.java        (Business logic)
│   ├── repository/TransactionRepository.java (Database queries)
│   ├── model/Transaction.java                (Transaction entity)
│   └── dto/TransactionDTO.java
│
├── 🔐 SECURITY MODULE
│   ├── security/JwtUtils.java            (JWT token generation/validation)
│   └── config/SecurityConfig.java        (Security configuration)
│
├── ⚙️ CONFIGURATION
│   ├── config/SupabaseConfig.java       (Supabase connection)
│   ├── config/WebConfig.java            (CORS & web configuration)
│   └── application.properties           (Database & app config)
│
└── 🎯 BorrowBoxBackendApplication.java   (Application entry point)
```

---

## 🏛️ Architecture Pattern (MVP-style)

### Model
- **Entity Classes**: `User.java`, `Item.java`, `Borrow.java`, `Transaction.java`
- **DTOs**: `ItemDTO.java`, `BorrowDTO.java`, `TransactionDTO.java`
- **Repositories**: Data access layer using Spring Data JPA

### Presenter (Service)
- **ItemService**: Handles item operations, search, filtering
- **BorrowService**: Manages borrow/return operations
- **TransactionService**: Tracks all transactions
- **AuthService**: Authentication logic

### View (Controller)
- **ItemController**: REST endpoints for items (`/api/items`)
- **BorrowController**: REST endpoints for borrows (`/api/borrows`)
- **TransactionController**: REST endpoints for transactions (`/api/transactions`)
- **AuthController**: Authentication endpoints (`/api/auth`)

---

## 📱 API Endpoints

### Authentication (`/api/auth`)
```
POST   /auth/login              - User login
POST   /auth/register           - User registration
```

### Items (`/api/items`)
```
GET    /items                   - Get all items
GET    /items/{id}              - Get item by ID
POST   /items/create            - Create new item
PUT    /items/{id}              - Update item
DELETE /items/{id}              - Delete item
GET    /items/search/{term}     - Search items
GET    /items/category/{cat}    - Filter by category
GET    /items/available/all     - Get available items
```

### Borrows (`/api/borrows`)
```
POST   /borrows/borrow          - Borrow an item
POST   /borrows/{id}/return     - Return an item
GET    /borrows                 - Get all borrows
GET    /borrows/{id}            - Get borrow by ID
GET    /borrows/student         - Get user's borrows
GET    /borrows/student/active  - Get active borrows
GET    /borrows/overdue/all     - Get overdue items
```

### Transactions (`/api/transactions`)
```
GET    /transactions            - Get all transactions
GET    /transactions/{id}       - Get transaction by ID
GET    /transactions/student/{id}    - Get student transactions
GET    /transactions/status/{status} - Filter by status
GET    /transactions/item/{id}  - Get item transactions
GET    /transactions/range?start=...&end=... - Date range filter
```

---

## 🔄 Feature Flow (Vertical Slice Example: Borrow Item)

**UI → Logic → Data**

1. **UI (Frontend)**
   - Student clicks "Borrow Item" button
   - WebStudentBorrows component sends POST request to `/api/borrows/borrow`

2. **Logic (Service Layer)**
   - `BorrowController` receives request
   - `BorrowService.borrowItem()` executes:
     - Validates item availability
     - Creates Borrow record
     - Reduces item quantity
     - Creates Transaction record
     - Returns BorrowDTO

3. **Data (Repository)**
   - `ItemRepository.save()` - Updates available quantity
   - `BorrowRepository.save()` - Stores borrow record
   - `TransactionRepository.save()` - Logs transaction

4. **Response**
   - Returns success response with borrow details
   - Frontend updates UI with new data

---

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| **Spring Boot** | 3.1.5 | Web framework |
| **PostgreSQL** | 15+ | Database |
| **Supabase** | Latest | Backend-as-a-Service |
| **Spring Data JPA** | Latest | ORM |
| **JWT** | 0.11.5 | Authentication |
| **jBCrypt** | 0.4 | Password hashing |
| **Maven** | 3.8+ | Build tool |
| **Java** | 17+ | Language |

---

## ⚙️ Installation & Setup

### 1. Clone the Repository
```bash
cd C:\Users\homer\Desktop\BorrowBoxBackend\BorrowBoxBackend
```

### 2. Configure Environment
Copy `.env.example` to `.env` and update:
```env
SPRING_DATASOURCE_URL=your_supabase_url
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
```

### 3. Build Project
```bash
mvn clean install
```

### 4. Run Application
```bash
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`

---

## 🗄️ Database Schema

### users
```sql
- id (UUID, Primary Key)
- username (String, Unique)
- password (String, Hashed)
- full_name (String)
- role (STUDENT/OFFICER)
- student_id, org_id, supabase_id
- profile_photo (BLOB)
- created_at, updated_at
```

### items
```sql
- id (UUID, Primary Key)
- name, description, category (String)
- total_quantity, available_quantity (Integer)
- serial_number (String, Unique)
- status, created_by (String)
- created_at, updated_at
```

### borrows
```sql
- id (UUID, Primary Key)
- student_id, item_id (FK)
- serial_number (String)
- borrow_date, due_date, return_date (DateTime)
- status (ACTIVE/RETURNED)
- condition, notes (String)
- created_at, updated_at
```

### transactions
```sql
- id (UUID, Primary Key)
- borrow_id, student_id, item_id (FK)
- transaction_type (BORROW/RETURN)
- status (String)
- borrow_date, due_date, return_date (DateTime)
- condition, notes (String)
- created_at, updated_at
```

---

## 🔒 Security Features

- ✅ JWT token-based authentication
- ✅ Password hashing with jBCrypt
- ✅ CORS configuration for frontend
- ✅ Role-based access control (RBAC)
- ✅ SQL injection protection (JPA)
- ✅ Request validation

---

## 🚀 Deployment Ready

### For Render.com
1. Connect GitHub repository
2. Set environment variables
3. Deploy main branch
4. Database: Use Supabase PostgreSQL

### For Railway.app
1. Push to GitHub
2. Connect Railway to repo
3. Add PostgreSQL plugin
4. Deploy

### Environment Variables Required
```
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
SUPABASE_URL
SUPABASE_JWT_SECRET
JWT_SECRET
JWT_EXPIRATION
SERVER_PORT=8080
```

---

## 📝 Response Format

All API responses follow this format:

```json
{
  "success": true/false,
  "message": "Operation message",
  "data": {...},
  "count": 10,
  "timestamp": "2026-05-02T12:00:00"
}
```

---

## 🧪 Testing

### Login
```bash
POST http://localhost:8080/api/auth/login
Body: {"email": "student@test.com", "password": "password"}
```

### Get All Items
```bash
GET http://localhost:8080/api/items
```

### Borrow Item
```bash
POST http://localhost:8080/api/borrows/borrow
Body: {"itemId": "item-uuid", "dueDate": "2026-06-01T23:59:59"}
Headers: Authorization: Bearer {token}
```

---

## 📊 Project Statistics

- **Total Endpoints**: 25+
- **Models**: 4 (User, Item, Borrow, Transaction)
- **Repositories**: 3
- **Services**: 4
- **Controllers**: 4
- **DTOs**: 6
- **Code Lines**: 2000+

---

## 🎓 Learning Resources

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [JWT Auth](https://jwt.io)
- [Supabase Docs](https://supabase.com/docs)

---

## 📞 Support

For issues or questions, refer to:
- Check API endpoint documentation above
- Review model classes for structure
- Check service layer for business logic

---

**Built for PSITE 7 ICT Congress - Final Examination Project** 🚀
