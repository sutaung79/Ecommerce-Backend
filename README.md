# E-Commerce Backend
This E-Commerce Application uses Java and Spring Boot. It focuses on security and maintenance. The backend uses Spring Data JPA. It works with an Oracle database. This setup makes it easy to manage and store important entities like users, products, categories, and orders.

## Tech Stack
* Java
* Spring Framework
* Spring Boot
* Spring Data JPA
* Hibernate
* JSON Web Tokens (JWT)
* Oracle

## Features
* User:
    * Register and log in
    * View categories and products by category
    * Add and remove products from the cart
    * Place orders and manage addresses
* Admin:
    * Users: Manage user accounts.
    * Address: Handle user addresses.
    * Categories: Update product categories.
    * Products: Add, change, and remove products.
    * Price & Discount: Set and change prices and discounts.
    * Orders: Track and manage orders.

## Security
The API is protected with JSON Web Tokens (JWT) managed by Auth0. To access the API, you need to get a JWT by authenticating with the system.

## Code Structure Overview

The code follows a typical **MVC (Model-View-Controller)** architecture, separating concerns for better organization and scalability:

### 1. **Controllers**
   - **Purpose**: Handle HTTP requests and responses.
   - **Examples**:
     - `UserController`: Manages user-related requests.
     - `ProductController`: Handles product actions.
     - `OrderController`: Manages order creation and payment processing.

### 2. **Services**
   - **Purpose**: Contain the business logic for the application.
   - **Examples**:
     - `UserService`: Handles user authentication and management.
     - `CartService`: Manages cart operations like adding/removing items.
     - `OrderService`: Handles order-related logic like calculating totals, applying discounts, etc.

### 3. **Repositories**
   - **Purpose**: Interact with the database to perform CRUD (Create, Read, Update, Delete) operations.
   - **Examples**:
     - `UserRepository`: Fetches and updates user data.
     - `ProductRepository`: Retrieves product listings from the database.
     - `OrderRepository`: Manages order records.

### 4. **Models**
   - **Purpose**: Define the structure of the data entities (e.g., Users, Products, Orders) used throughout the system.


## E-R Diagram for the application
![ecom-erDiagram](https://github.com/user-attachments/assets/b0b56de0-678a-4087-897e-9c425b913e00)
This ER diagram outlines the structure of an eCommerce platform's database, focusing on user management, products, and transactions.

- **Users** : Central entity representing customers, with links to addresses, roles, and carts.
- **Roles** : Defines user roles (e.g., admin, customer) assigned to each user.
- **Addresses** : Stores multiple addresses for each user.
- **Carts & Cart Items** : Manages user shopping carts and the items within each cart.
- **Products** : Catalog of products, each belonging to a category and containing pricing and discount details.
- **Categories** : Groups products into various categories.
- **Orders & Order Items** : Tracks user orders and the individual products in each order.
- **Payments** : Records the payment methods used for orders.

The relationships between entities ensure that users can manage products, place orders, and make payments efficiently within the system.




## Installation & Running
To install and run this project, make sure you have Java, Maven, and a Oracle database installed on your computer. Then, follow these steps:
1. Clone the repository: git clone https://github.com/sutaung79/Ecommerce-Backend.git
2. Update application.properties with your Oracle database connection details.
3. run the app : execute the main method in the BackendEcommerceApplication class from your IDE.
    Alternatively you can use the Spring Boot Maven plugin like so:
    
    ```
    mvn spring-boot:run
    ```

## API Documentation
* API documentation is available via Swagger UI at http://localhost:8080/swagger-ui/index.html

