
# Contact Manager Application




## overview

The Contact Manager Application is a web-based application that allows users to manage their contacts efficiently. Users can add, update, delete, and view their contacts, making it easier to organize and manage their personal or professional network.
## Features

 - User Authentication: Secure sign-up and login functionality using Spring Security.
 - CRUD Operations: Create, Read, Update, and Delete contacts with ease.
 - Search and Filter: Quickly find contacts by name, email, or other criteria.
 - Responsive UI: A user-friendly interface built with Thymeleaf.
 - Validation: Robust validation for user input to ensure data   integrity.

## Technology Used

 - Backend: Spring Boot, Spring Security, Spring Data JPA, Hibernate
 - Frontend: Thymeleaf, Bootstrap
 - Database: MySQL
 - Build Tool: Maven
 - Other: Git, GitHub
## Project Structure

 - Backend: The backend API is built with Spring Boot and handles authentication, contact management, and data persistence.
 - Frontend: The frontend is developed with Java template Engine Thymeleaf, providing a responsive and interactive user interface.
 - Database: MySQL is used for storing user and contact information.

## Installation and Setup

### Prerequisites
 - Java 17 or higher
 - Thymeleaf 
 - MySQL database
 - Maven

### Install my-project with maven

#### 1. Clone the Repository

```bash
  git clone https://github.com/Rkarama26/ContactManager
  cd contact-manager

```
#### 2. Set up the database:

 - Create a MySQL database named contact_manager.
 - Update the application.properties file with your database credentials.

 #### 3. Build and run Backend 
 ```bash
cd backend
mvn clean install
mvn spring-boot:run

 ```
    
## Contributing

Contributions are welcome! If you have any ideas, suggestions, or bug reports, please open an issue or submit a pull request.
## 🛠 Skills
Java, Spring, Hibernate, JPA, MySQL, HTML, CSS...

