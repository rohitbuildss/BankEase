# BankEase — Java Banking Management System

BankEase is a console-based Banking Management System developed using Core Java and Advanced Java concepts.

The project simulates basic banking operations such as account creation, login, deposits, withdrawals, money transfers, transaction history, interest calculation, password management, KYC updates, and logout.

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
- Handle invalid console input
- Custom exception handling

## Technologies Used

- Java
- Object-Oriented Programming
- Collections Framework
- Exception Handling
- Custom Exceptions
- Enum
- Java Time API
- LocalDateTime
- ArrayList
- Scanner

## Project Structure

```text
BankEase
│
├── src
│   ├── main
│   │   └── Main.java
│   │
│   ├── model
│   │   ├── Account.java
│   │   ├── Customer.java
│   │   ├── AccountDetails.java
│   │   ├── Transaction.java
│   │   ├── TransactionType.java
│   │   └── InterestResult.java
│   │
│   ├── service
│   │   └── BankService.java
│   │
│   └── exception
│       ├── AccountNotFoundException.java
│       ├── DuplicateAccountException.java
│       ├── InsufficientBalanceException.java
│       ├── InvalidAccountDataException.java
│       ├── InvalidAmountException.java
│       ├── InvalidPasswordException.java
│       ├── InvalidTimePeriodException.java
│       └── NotLoggedInException.java