package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import dao.AccountDAO;
import dao.CustomerDAO;
import dao.TransactionDAO;
import database.DBConnection;
import exception.InvalidAmountException;
import exception.InvalidPasswordException;
import exception.InvalidTimePeriodException;
import exception.NotLoggedInException;
import exception.AccountNotFoundException;
import exception.DuplicateAccountException;
import exception.InsufficientBalanceException;
import exception.InvalidAccountDataException;
import model.Account;
import model.AccountDetails;
import model.Customer;
import model.InterestResult;
import model.Transaction;
import model.TransactionType;

public class BankService {

    private Account loggedInAccount;
    private static final double interestRate = 7.0;

    private CustomerDAO customerDAO = new CustomerDAO();
    private AccountDAO accountDAO = new AccountDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();

    private static void validateAccountData(double balance, int age, String name, String phone, String accountType, String password) throws InvalidAccountDataException, InvalidAmountException, InvalidPasswordException {

        if (balance <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        if (age < 18) {
            throw new InvalidAccountDataException("Age Must Be Greater Than Or Equal To 18");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidAccountDataException("Name Cannot Be Empty");
        }

        if (phone == null || !phone.matches("\\d{10}")) {
            throw new InvalidAccountDataException("Phone Number Must Contain Exactly 10 Digits.");
        }

        if (accountType == null || accountType.trim().isEmpty()) {
            throw new InvalidAccountDataException("Account Type Cannot Be Empty. Enter Either SAVINGS Or CURRENT.");
        }

        if (!accountType.equalsIgnoreCase("SAVINGS") && !accountType.equalsIgnoreCase("CURRENT")) {

            throw new InvalidAccountDataException("Invalid Account Type. Enter Either SAVINGS Or CURRENT.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new InvalidPasswordException("Password Cannot Be Empty");
        }

        if (password.length() < 8) {
            throw new InvalidPasswordException("Password Must Be At Least 8 Characters");
        }
    }

    private void checkLogin() throws NotLoggedInException {

        if (loggedInAccount == null) {
            throw new NotLoggedInException("Please Login First To Aceesss Features.");
        }

    }

    public Account createAccount(String accountType, double balance, String name, int age, String phone, String address, String gender, String password) throws InvalidAccountDataException, InvalidAmountException, InvalidPasswordException, SQLException {

        validateAccountData(balance, age, name, phone, accountType, password);

        Customer customer = new Customer(name, age, phone, address, gender);
        int customerID = customerDAO.insertCustomer(customer);
        customer.setCustomerId(customerID);
        Account account = new Account(accountType, balance, customerID, password);
        int accountNumber = accountDAO.insertAccount(account);
        account.setAccountNumber(accountNumber);
        return account;
    }

    public void login(int accountNumber, String password) throws AccountNotFoundException, InvalidPasswordException, SQLException {

        Account account = accountDAO.findAccount(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException("No Such Account Found With This Account Number");
        }
        if (!account.getPassword().equals(password)) {
            throw new InvalidPasswordException("Invalid Password");
        }

        loggedInAccount = account;
    }

    public void depositMoney(double amount) throws InvalidAmountException, NotLoggedInException, SQLException {

        checkLogin();

        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        int accountNumber = loggedInAccount.getAccountNumber();

        accountDAO.deposit(accountNumber, amount);

        Transaction transaction = new Transaction(loggedInAccount.getAccountNumber(), loggedInAccount.getAccountNumber(), amount, LocalDateTime.now(), TransactionType.DEPOSIT);
        int transactionId = transactionDAO.insertTransaction(transaction);
        transaction.setTransactionId(transactionId);

    }

    public void withdrawMoney(double amount) throws InsufficientBalanceException, InvalidAmountException, NotLoggedInException, SQLException {

        checkLogin();

        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
        if (amount > accountDAO.getBalance(loggedInAccount.getAccountNumber())) {
            throw new InsufficientBalanceException("Insufficient Balance");
        }
        int accountNumber = loggedInAccount.getAccountNumber();
        accountDAO.withdraw(accountNumber, amount);

        Transaction transaction = new Transaction(loggedInAccount.getAccountNumber(), loggedInAccount.getAccountNumber(), amount, LocalDateTime.now(), TransactionType.WITHDRAW);
        int transactionId = transactionDAO.insertTransaction(transaction);
        transaction.setTransactionId(transactionId);
    }

    public double checkBalance() throws NotLoggedInException, SQLException {

        checkLogin();

        int accountNumber = loggedInAccount.getAccountNumber();

        return accountDAO.getBalance(accountNumber);
    }

    public AccountDetails getDetails()
            throws NotLoggedInException, SQLException {

        checkLogin();

        int accountNumber = loggedInAccount.getAccountNumber();

        Account account = accountDAO.findAccount(accountNumber);

        int customerId = account.getCustomerId();

        Customer customer = customerDAO.findCustomer(customerId);

        return new AccountDetails(account, customer);
    }

    public void transferMoney(int receiverAccountNumber, double amount)
            throws NotLoggedInException, InvalidAmountException,
            AccountNotFoundException, DuplicateAccountException,
            InsufficientBalanceException, SQLException {

        checkLogin();

        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
        Account receiver = accountDAO.findAccount(receiverAccountNumber);

        if (receiver == null) {
            throw new AccountNotFoundException(
                    "No Such Account Found With This Account Number"
            );
        }

        if (receiverAccountNumber == loggedInAccount.getAccountNumber()) {
            throw new DuplicateAccountException(
                    "Transfer To Duplicate Account Not Allowed."
            );
        }
        double balance = accountDAO.getBalance(
                loggedInAccount.getAccountNumber()
        );

        if (amount > balance) {
            throw new InsufficientBalanceException("Insufficient Balance");
        }
        try (Connection con = DBConnection.getConnection()) {

            try {
                con.setAutoCommit(false);
                accountDAO.withdraw(con, loggedInAccount.getAccountNumber(), amount);
                accountDAO.deposit(con, receiver.getAccountNumber(), amount);
                Transaction transaction = new Transaction(
                        loggedInAccount.getAccountNumber(),
                        receiverAccountNumber,
                        amount,
                        LocalDateTime.now(),
                        TransactionType.TRANSFER
                );
                int transactionId = transactionDAO.insertTransaction(con, transaction);
                transaction.setTransactionId(transactionId);
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    public List<Transaction> getTransactionHistory() throws NotLoggedInException, SQLException {

        checkLogin();

        int senderAccountNumber = loggedInAccount.getAccountNumber();
        int receiverAccountNumber = loggedInAccount.getAccountNumber();

        List<Transaction> userTransactions = transactionDAO.findTransactionsByAccount(senderAccountNumber,receiverAccountNumber);

        return userTransactions;
    }

    public InterestResult calculateInterest(int years)
            throws NotLoggedInException, InvalidTimePeriodException, SQLException {

        checkLogin();

        if (years <= 0) {
            throw new InvalidTimePeriodException("Years Should Be Greter Than 0.");
        }

        int accountNumber = loggedInAccount.getAccountNumber();

        double balance = accountDAO.getBalance(accountNumber);

        double interest = (balance * interestRate * years) / 100;

        return new InterestResult(balance, interestRate, years, interest);
    }

    public void changePassword(String currentPassword, String newPassword) throws NotLoggedInException, InvalidPasswordException, SQLException {

        checkLogin();

        int accountNumber = loggedInAccount.getAccountNumber();
        Account account = accountDAO.findAccount(accountNumber);


        if (!account.getPassword().equals(currentPassword)) {
            throw new InvalidPasswordException("Wrong Password! Please Enter Valid Password");
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new InvalidPasswordException("New password cannot be empty.");
        }

        if (newPassword.length() < 8) {
            throw new InvalidPasswordException("Password must be at least 8 characters.");
        }

        accountDAO.updatePassword(newPassword,accountNumber);
    }

    public void logoutUser() throws NotLoggedInException {
        checkLogin();
        loggedInAccount = null;

    }

    public void updateCustomerDetails(String name, String phone, String address, String gender) throws NotLoggedInException, InvalidAccountDataException, SQLException {

        checkLogin();

        int accountNumber = loggedInAccount.getAccountNumber();

        Account account = accountDAO.findAccount(accountNumber);
        int customerId = account.getCustomerId();

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidAccountDataException("Name Cannot Be Empty");
        }

        if (address == null || address.trim().isEmpty()) {
            throw new InvalidAccountDataException("Address Cannot Be Empty");
        }

        if (phone == null || !phone.matches("\\d{10}")) {
            throw new InvalidAccountDataException("Phone Number Must Contain Exactly 10 Digits.");
        }

        if (gender == null || !gender.equalsIgnoreCase("MALE") && !gender.equalsIgnoreCase("FEMALE")) {

            throw new InvalidAccountDataException("Invalid Account Type. Enter Either MALE Or FEMALE.");
        }

        gender = gender.toUpperCase();

        customerDAO.updateCustomerDetails(customerId,name,phone,address,gender);

    }

}
