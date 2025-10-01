Hello Talent Acquisition Team of Verto,
My Name is Swapnil Dube ,I have created Inventory Management System Project.
I have also provided a Loom video of the application, demonstrating each test case along with step-by-step debugging
Thank you for the opportunity to complete this task. Please find the detailed documentation for the Inventory Management System below.

Inventory Management System
1. Project Description
This project is a RESTful API for a multi-owner Inventory Management System built with Spring Boot. It provides a secure way for individual owners to manage their own product inventory. The system uses JWT (JSON Web Tokens) for authentication and authorization, ensuring that users can only access and manage their own data.

The core functionalities include creating, reading, updating, and deleting products, as well as specialized operations for managing stock levels and identifying items that are running low.

Tech Stack
Backend: Java, Spring Boot, Spring Data

Database: MongoDB Atlas

Security: Spring Security, JSON Web Token (JWT)

Build Tool: Maven

2. How to Set Up and Run Locally
Follow these instructions to get the project up and running on your local machine.

Prerequisites
Java Development Kit (JDK) 17 or later
Apache Maven
An IDE like IntelliJ IDEA or VS Code
Postman for API testing


Setup Instructions

1]Clone the Repository
Open your terminal or command prompt and clone the repository using the following command:

git clone [https://github.com/swapnildube07/inventory-management-system.git](https://github.com/swapnildube07/inventory-management-system.git)
cd inventory-management-system



I have Used Mongodb Altas and I have Also Implement String url in My project properties

So ,No changes are required to run the application locally.



After Cloning the Project
Build the Project
2] Run the following Maven command in the root directory of the project to download dependencies and build the application:

mvn clean install

Run the Application
You can run the application using your IDE by running the main application class, or by using the Maven command:

mvn spring-boot:run

The application will start on http://localhost:8080.

3] How to Run Test Cases (API Usage Guide)
Use Postman to interact with the API endpoints.

Step 1: User Authentication (Login)
First, you need to log in to get a JWT token. This token is required for all other API calls.

Endpoint: POST http://localhost:8080/public/login

Body (raw, JSON):

{
  "email": "swapnildube07@gmail.com",
  "password": "Swapnil@123"
}

Action: After sending the request, copy the token from the response body.

Step 2: Authorizing Requests
For all subsequent requests, you must include the JWT token in the Authorization header. In Postman:

Go to the Authorization tab.

Select Type: Bearer Token.

Paste the copied token into the Token field.

API Endpoints & Test Cases
a. Creating a Product
Endpoint: POST http://localhost:8080/products

Body (raw, JSON):

{
  "productname": "Apple",
  "description": "Fresh red apples full of nutrients",
  "stockQuantity": 150,
  "lowStockThreshold": 30
}

Success Response: Returns the created product with a success message.

Test Case (Product Already Exists): Send the same request again.

Expected Response: The API will throw an exception with a message like "Product already exists in inventory. Please use the update endpoint instead."

b. Getting All Products
Endpoint: GET http://localhost:8080/products

Success Response: Returns a list of all products in the user's inventory.

c. Search Product by Name
Endpoint: POST http://localhost:8080/products/name

Body (raw, JSON):

{
  "productname": "Apple"
}

Success Response: Returns the details of the product named "Apple".

Test Case (Product Not Found): Search for a product that doesn't exist.

Request Body:

{
  "productname": "Oranges"
}

Expected Response: An error message like "Product with name Oranges not found."

d. Update Product by Name
Endpoint: PUT http://localhost:8080/products/{productName} (e.g., http://localhost:8080/products/Apple)

Body (raw, JSON):

{
  "productname": "Apple",
  "description": "Apples are red and fresh",
  "stockQuantity": -50,
  "lowStockThreshold": 25
}

Test Case (Handle Negative Stock): The stockQuantity is provided as a negative value (-50).

Expected Response: The system automatically converts the negative stock to 0 and returns the updated product.

{
  "productname": "Apple",
  "description": "Apples are red and fresh",
  "stockQuantity": 0,
  "lowStockThreshold": 25
}

e. Increase Stock
Endpoint: PATCH http://localhost:8080/products/increase/{productName} (e.g., http://localhost:8080/products/increase/Apple)

Body (raw, JSON):

{
  "quantity": 25
}

Success Response: Returns the product with its stockQuantity increased by 25.

Test Case (Invalid Quantity): Try to increase the stock with a negative value.

Request Body:

{
  "quantity": -25
}

Expected Response: An error message stating that the quantity must be a positive value.

f. Decrease Stock
Endpoint: PATCH http://localhost:8080/products/decrease/{productName} (e.g., http://localhost:8080/products/decrease/Apple)

Body (raw, JSON):

{
  "quantity": 20
}

Success Response: Returns the product with its stockQuantity decreased by 20.

Test Case (Insufficient Stock): Try to decrease the stock by more than the available quantity.

Request Body:

{
  "quantity": 1120
}

Expected Response: An error message like "Cannot decrease stock. Insufficient quantity available."

g. Getting Low Stock Products
Endpoint: GET http://localhost:8080/products/low-stock

Success Response: Returns a list of all products where the stockQuantity is less than or equal to the lowStockThreshold.

4. Assumptions and Design Choices
Stateless Authentication: The application is stateless. JWT is used for securing the endpoints, and each request to a protected route must contain a valid token in the Authorization header.

Data Isolation: The system is designed for multi-tenancy where each user (owner) has their own separate inventory. A user can only view and manage products they have created.

Unique Product Names: The productname is treated as a unique identifier within a single user's inventory to prevent duplicate entries.

Input Validation:

Stock quantities cannot be negative. The update endpoint automatically corrects negative input to 0.

Stock modification endpoints (/increase, /decrease) include validation to ensure logical consistency (e.g., cannot increase by a negative number, cannot decrease more than is available).
