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
- **User Registration and Login**: Users can create accounts and log in securely.
- **Product Browsing**: View products organized by categories.
- **Shopping Cart**: Add and remove products from the cart.
- **Order Placement**: Place orders and manage shipping addresses.
- **Admin**:
   - Manage user accounts
   - Handle addresses
   - Update product categories
   - Add, update, and remove products
   - Set and adjust prices and discounts
   - Track and manage orders
- **Authentication**: Secure API access using JSON Web Tokens (JWT) managed by Auth0.


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

### Database Design Decisions
- **Product** : Represents items available for purchase.
- **User** : Represents customers of the eCommerce platform.
- **Order** : Represents a purchase made by a user.
- **OrderItem** : Represents individual items within an order.
- **Cart** : Represents a user's shopping cart.
- **CartItem** : Represents items in a user's cart.

### Relationships
- **Product** : A product can be part of multiple `OrderItem` and `CartItem` entities.
- **User** : A user can have multiple `Orders` and a `Cart`. The `Cart` can contain multiple `CartItems`.
- **Order** : An order can contain multiple `OrderItems`.
- **Cart** : A cart can contain multiple `CartItems`.

The relationships between entities ensure that users can manage products, place orders, and make payments efficiently within the system.




## Installation & Running
To install and run this project, make sure you have Java, Maven, and a Oracle database installed on your computer. Then, follow these steps:
### Setup
1. **Clone the Repository:**
   ```
   git clone https://github.com/sutaung79/Ecommerce-Backend.git
   cd Ecommerce-Backend
   ```
2. **Set Up Dependencies:**
    - Ensure you have Java JDK 17 or later installed.
    - Dependencies are already included in the pom.xml, so you don't need to add them manually.
3. **Configure Database:**
    - Update the `application.properties` file located in `src/main/resources` with your Oracle database configuration:
    ---
    ```
    spring.datasource.url=jdbc:oracle:thin:@<host>:<port>:<sid>
    spring.datasource.username=<your-username>
    spring.datasource.password=<your-password>
    spring.jpa.hibernate.ddl-auto=update
    ```
4. **Run the Application:**
   - If you are using Maven, build the project with `mvn clean install` and run the application with `mvn spring-boot:run`.
   - Alternatively, you can run the `main` class of the Application directly.
  
   ### Build and Run with Maven
    ```
   mvn clean install
   mvn spring-boot:run
   ```
## Sample Request Data

- User
```
{
  "firstName": "Dante",
  "lastName": "Davis",
  "mobileNumber": "0934324343",
  "email": "davis@gmail.com",
  "password": "davis123",

  "address": {
    "street": "Kirby Dr",
    "building": "58080",
    "city": "Houston",
    "province": "Texas",
    "country": "US",
    "pincode": "770005"
  }
}
```

`NOTE` : The roles are already defined, with `role_id 101` assigned to the `ADMIN` role and `role_id 102` assigned to the `USER` role.  
For Admin access to create products, categories, and more, you need to manually update the database with the following command:
```
UPDATE user_role SET role_id = 101 WHERE user_id = your_user_id;
COMMIT;
```


- Category
```
{
   "categoryName": "Groceries"
}
```

- Product
```
{
 
  "productName": "egg",
  "image": "egg.png",
  "description": "great source of high-quality protein",
  "quantity": 40,
  "price": 5,
  "discount": 0.1
  
}
```

## API Documentation
* API documentation is available via Swagger UI at http://localhost:8080/swagger-ui/index.html

