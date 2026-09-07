# BankEase — Java Banking Management System

BankEase is a console-based Banking Management System developed using **Core Java, Advanced Java, Maven, JDBC, SQL, and PostgreSQL**.

The project simulates real-world banking operations such as account creation, authentication, deposits, withdrawals, money transfers, transaction history, interest calculation, password management, and customer/KYC management.

The project was initially developed using Java Collections for in-memory data storage and was later upgraded to **JDBC + PostgreSQL**, making the database the persistent source of truth for customer, account, and transaction data.

## Features

- Create a new bank account
- Validate account information
- Login using account number and password
- Deposit money
- Withdraw money
- Check account balance
- View account and customer details
- Transfer money between accounts
- Prevent self-transfer
- View transaction history
- Calculate simple interest
- Change account password
- Update customer/KYC details
- Logout
- Protect account operations when not logged in
- Support multiple accounts with proper account isolation
- Store banking data persistently in PostgreSQL
- Handle invalid input and business-rule violations
- Custom exception handling
- Database transaction management for money transfers
- Automatic generation of customer, account, and transaction IDs using PostgreSQL

## Technologies Used

- Java
- Core Java
- Advanced Java
- Object-Oriented Programming (OOP)
- Collections Framework
- Exception Handling
- Custom Exceptions
- Enums
- Java Time API
- LocalDateTime
- Maven
- JDBC
- SQL
- PostgreSQL
- PreparedStatement
- ResultSet
- JDBC Transactions
- Try-with-Resources
- Scanner

## Database

BankEase uses **PostgreSQL** for persistent data storage.

### Tables

```text
customers
├── customerId
├── name
├── age
├── phone
├── address
└── gender

accounts
├── accountNumber
├── accountType
├── balance
├── customerId
└── password

transactions
├── transactionId
├── senderAccNumber
├── receiverAccNumber
├── amount
├── timeStamp
└── transactionType
