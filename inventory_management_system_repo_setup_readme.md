# Inventory Management System

> RESTful API for a multi-owner Inventory Management System built with Spring Boot and MongoDB Atlas. Provides secure JWT-based authentication so that each owner manages their own products and stock levels independently.

---

## 1. Project Description
This project is a backend system that allows owners to manage their product inventory with authentication and authorization powered by JWT. It includes CRUD operations for products, stock adjustments, and low-stock detection.

**Tech Stack:**
- **Backend:** Java, Spring Boot, Spring Data
- **Database:** MongoDB Atlas
- **Security:** Spring Security, JSON Web Token (JWT)
- **Build Tool:** Maven

Core functionalities:
- Create, read, update, and delete products
- Increase and decrease stock levels
- Identify low-stock items

---

## 2. How to Set Up and Run Locally

### Prerequisites
- Java Development Kit (JDK) 17 or later
- Apache Maven
- An IDE (IntelliJ IDEA, VS Code, etc.)
- Postman for API testing

### Setup Instructions

**Clone the Repository**
```bash
git clone https://github.com/swapnildube07/inventory-management-system.git
cd inventory-management-system
```

**Configure MongoDB Atlas Connection**
- The project uses `src/main/resources/application.properties`.
- Replace `spring.data.mongodb.uri` with your own Atlas connection string for production/personal use.
- Example placeholder:
```properties
spring.data.mongodb.uri=mongodb+srv://<USER>:<PASSWORD>@cluster0.xxxxx.mongodb.net/<DB_NAME>
spring.security.jwt.secret=your_jwt_secret
server.port=8080
```

**Build the Project**
```bash
mvn clean install
```

**Run the Application**
```bash
mvn spring-boot:run
```

The application runs at [http://localhost:8080](http://localhost:8080).

---

## 3. How to Run Test Cases (API Usage Guide)
Use Postman to interact with endpoints.

### Step 1: Login (Get JWT Token)
- **Endpoint:** `POST http://localhost:8080/public/login`
- **Body:**
```json
{
  "email": "swapnildube07@gmail.com",
  "password": "Swapnil@123"
}
```
- **Action:** Copy JWT token from response.

### Step 2: Authorize Requests
- In Postman → Authorization → Type: `Bearer Token` → Paste token.

### API Endpoints & Test Cases

**a. Create Product**  
`POST /products`
```json
{
  "productname": "Apple",
  "description": "Fresh red apples full of nutrients",
  "stockQuantity": 150,
  "lowStockThreshold": 30
}
```
- Success → Returns product.
- Test (duplicate) → Error: *Product already exists in inventory. Use update endpoint.*

**b. Get All Products**  
`GET /products` → Returns all products for owner.

**c. Search by Name**  
`POST /products/name`
```json
{"productname": "Apple"}
```
- Success → Returns product details.
- Test (not found):
```json
{"productname": "Oranges"}
```
- Error → *Product with name Oranges not found.*

**d. Update Product**  
`PUT /products/{productName}`
```json
{
  "productname": "Apple",
  "description": "Apples are red and fresh",
  "stockQuantity": -50,
  "lowStockThreshold": 25
}
```
- Test (negative stock) → Saved as `0`.
```json
{
  "productname": "Apple",
  "description": "Apples are red and fresh",
  "stockQuantity": 0,
  "lowStockThreshold": 25
}
```

**e. Increase Stock**  
`PATCH /products/increase/{productName}`
```json
{"quantity": 25}
```
- Success → Increases stock.
- Test (invalid quantity):
```json
{"quantity": -25}
```
- Error → *Quantity must be positive.*

**f. Decrease Stock**  
`PATCH /products/decrease/{productName}`
```json
{"quantity": 20}
```
- Success → Decreases stock.
- Test (insufficient stock):
```json
{"quantity": 1120}
```
- Error → *Cannot decrease stock. Insufficient quantity available.*

**g. Low Stock**  
`GET /products/low-stock` → Returns products at or below threshold.

---

## 4. Assumptions & Design Choices
- **Stateless Authentication:** JWT secures all endpoints. Each request must include a valid token.
- **Data Isolation:** Each owner manages only their own inventory.
- **Unique Product Names:** Product names are unique within an owner’s inventory.
- **Input Validation:**
  - Negative stock coerced to `0`.
  - Increase/decrease operations validate positive numbers and sufficient stock.

---

**Repository:** [Inventory Ma