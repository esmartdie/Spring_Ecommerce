# Spring Ecommerce

This is an e-commerce application project developed with Spring Boot and Java.

## Features

- **Languages**: Java, HTML
- **Docker**: Includes `Dockerfile` and `docker-compose.yml` for easy development environment setup.
- **Database**: Includes `backup.sql` for database setup.

## Project Structure

- **src/**: Contains the application's source code.
- **images/**: Contains the images used in the application.
- **.mvn/**: Maven configuration files.
- **pom.xml**: Maven configuration file.

## Installation and Running

1. Clone the repository:
   ```sh
   git clone https://github.com/esmartdie/Spring_Ecommerce.git

2. Navigate to the project directory:
   ```sh
   cd Spring_Ecommerce

3. Build and run the application using Docker:
   ```sh
   docker-compose up --build

## User Operations
- **User Registration:** Allows new users to create an account.
- **Login:** Registered users can log in to the application.
- **Profile Update:** Users can update their personal information.
- **View Orders:** Users can view their order history.

## Product Operations
- **Product Listing:** View all available products.
- **Product Details:** View details of a specific product.
- **Add Product to Cart:** Users can add products to their shopping cart.
- **Product Management:** Administrators can add, update, and delete products.
