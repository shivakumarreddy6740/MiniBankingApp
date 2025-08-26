# MiniBankingApp

A simple **Java-based Mini Banking Application** built using **Java Swing** for the user interface, **MySQL** for the backend database, and **OOP principles** for modular design.  
This project was developed to strengthen the understanding of **Object-Oriented Programming (OOPs)** concepts in Java.

---

## 🚀 Features
- **User Authentication**
  - Register with ID and password  
  - Sign in with existing credentials  
- **Banking Operations**
  - Transfer money  
  - View account balance  
  - Logout securely  
- **GUI** built with Java Swing for interactive user experience  
- **Database Integration** using MySQL and Java Connector  

---

## 📂 Project Structure
The project consists of the following files:

- `connection.java` → Handles MySQL database connection  
- `bankmanagement.java` → Implements core banking logic (transfer, balance, logout)  
- `bank.java` → Main class with UI components and user interaction  
- `module-info.java` → Defines required modules for the application  
- `mysql-connector.jar` → MySQL JDBC driver for database connectivity  

---

## 🛠️ Tech Stack
- **Language**: Java  
- **GUI**: Java Swing  
- **Database**: MySQL  
- **Concepts**: OOP (classes, methods, packages, modular design)  

---

## ⚙️ Setup Instructions
1. Install **Java (JDK 8 or above)** and **MySQL**.  
2. Create a database in MySQL for storing user information. Example:  
   ```sql
   CREATE DATABASE bankdb;
   USE bankdb;

   CREATE TABLE customer (
       ac_no INT AUTO_INCREMENT PRIMARY KEY,
       cname VARCHAR(100) NOT NULL,
       balance DOUBLE DEFAULT 1000,
       passcode VARCHAR(50) NOT NULL
   );
