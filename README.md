# 🏦 Banking System Project

## Overview 🌟
Welcome to the Banking System! 🎉  
This Java-based application manages user accounts and financial transactions. Whether you're creating a new account, transferring funds, or checking your balance, this system has you covered!

## Key Features 🔑
- **User Registration & Login:** Securely create and manage user accounts.
- **Account Creation:** Set up and configure new bank accounts.
- **Debit and Credit Transactions:** Perform deposits and withdrawals with ease.
- **Money Transfer:** Transfer funds between accounts securely.
- **Balance Inquiry:** Check account balances at any time.
- **Robust Security:** Ensures all transactions and data remain secure using encapsulation to protect sensitive information.

## Tech Stack ⚙️
This project is built using:
- **Java:** Core application logic.
- **JDBC:** For seamless connectivity between Java and the database.
- **MySQL:** Handles the application's database needs.

## Security Implementation
We have implemented **encapsulation** in the code to ensure that sensitive user data, such as account details and transaction information, is not exposed or accessible directly. This protects the integrity of the system by allowing controlled access to critical data via getter and setter methods, safeguarding it from unauthorized access or modifications.

## Getting Started 🚀

### Clone the Repository:
```bash
git clone https://github.com/omkarsnagre/banking-system.git
```
## Set up MySQL:
Create a new database named banking_system and the necessary tables for user accounts and transactions.
1. Creating the accounts Table:
```bash
CREATE TABLE accounts (
    accNumber BIGINT NOT NULL PRIMARY KEY,    -- Primary key for account number
    fullName VARCHAR(255) NOT NULL,           -- Full name of the account holder
    email VARCHAR(255) NOT NULL UNIQUE,       -- Unique email for the account holder
    balance DECIMAL(10,2) NOT NULL,           -- Account balance with 2 decimal precision
    securityPin CHAR(6) NOT NULL              -- Security PIN with a fixed length of 6 characters
);
```
2. Creating the user Table:
```bash
CREATE TABLE user (
    fullName VARCHAR(255) NOT NULL,           -- Full name of the user
    email VARCHAR(255) NOT NULL PRIMARY KEY,  -- Primary key is the user's email
    password VARCHAR(255) NOT NULL            -- Password for authentication
);
```
## Configure your MySQL database settings in `BankingApp.java`:
```bash
private static final String DB_URL = "jdbc:mysql://localhost:3306/banking_system"; -Database URL
private static final String DB_USER = "root"; - MySQL username
private static final String DB_PASSWORD = "root"; - MySQL password
```
## Compile and Run the Application:
```bash
javac *.java
java BankingApp
```
## Usage 📋
Upon running the application, you'll be presented with a menu to choose from various banking operations (account management, deposits, transfers, balance checks, etc.). Follow the on-screen instructions to input account details, perform transactions, and more.

## Contributing 🤝
Contributions are welcome! Feel free to open issues and submit pull requests to enhance the project or add new features.

## Acknowledgments 🙏
Special thanks to all contributors and supporters of the Banking System project.

## Contact 📫
Got questions or suggestions? I’d love to hear from you! You can reach me at omkarnagre777@gmail.com. Let’s connect!

## Happy Banking! 💰
