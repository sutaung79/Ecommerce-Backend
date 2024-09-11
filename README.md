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

## E-R Diagram for the application
![ecom-erDiagram](https://github.com/user-attachments/assets/b0b56de0-678a-4087-897e-9c425b913e00)

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

