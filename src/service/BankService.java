package service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

	private List<Account> accounts = new ArrayList<>();
	private List<Customer> customers = new ArrayList<>();
	private List<Transaction> transactions = new ArrayList<>();

	// Counters For IDs:

	private int nextCustomerID = 1001;
	private int nextAccountID = 5001;
	private int nextTransactionID = 1001;

	private Account loggedInAccount;
	private static final double interestRate = 7.0;

	private static void validateAccountData(double balance, int age, String name, String phone, String accountType,
			String password) throws InvalidAccountDataException, InvalidAmountException, InvalidPasswordException {

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

	public Account createAccount(String accountType, double balance, String name, int age, String phone, String address,
			String gender, String password)
			throws InvalidAccountDataException, InvalidAmountException, InvalidPasswordException {

		validateAccountData(balance, age, name, phone, accountType, password);

		int customerID = nextCustomerID++;
		int accountNumber = nextAccountID++;

		Customer customer = new Customer(customerID, name, age, phone, address, gender);
		Account account = new Account(accountNumber, accountType, balance, customerID, password);

		customers.add(customer);
		accounts.add(account);

		return account;
	}

	public void login(int accountNumber, String password) throws AccountNotFoundException, InvalidPasswordException {

		for (Account account : accounts) {

			if (account.getAccountNumber() == accountNumber) {

				if (!account.getPassword().equals(password)) {
					throw new InvalidPasswordException("Invalid Password");
				}

				loggedInAccount = account;
				return;
			}
		}

		throw new AccountNotFoundException("No Such Account Found With This Account Number");
	}

	public void depositMoney(double amount) throws InvalidAmountException, NotLoggedInException {

		checkLogin();

		if (amount <= 0) {
			throw new InvalidAmountException("Amount must be greater than zero");
		}

		loggedInAccount.setBalance(loggedInAccount.getBalance() + amount);

		int transactionID = nextTransactionID++;

		Transaction transaction = new Transaction(transactionID, loggedInAccount.getAccountNumber(),
				loggedInAccount.getAccountNumber(), amount, LocalDateTime.now(), TransactionType.DEPOSIT);
		transactions.add(transaction);

	}

	public void withdrawMoney(double amount)
			throws InsufficientBalanceException, InvalidAmountException, NotLoggedInException {

		checkLogin();

		if (amount <= 0) {
			throw new InvalidAmountException("Amount must be greater than zero");
		}
		if (amount > loggedInAccount.getBalance()) {
			throw new InsufficientBalanceException("Insufficient Balance");
		}
		loggedInAccount.setBalance(loggedInAccount.getBalance() - amount);

		int transactionID = nextTransactionID++;

		Transaction transaction = new Transaction(transactionID, loggedInAccount.getAccountNumber(),
				loggedInAccount.getAccountNumber(), amount, LocalDateTime.now(), TransactionType.WITHDRAW);
		transactions.add(transaction);

	}

	public double checkBalance() throws NotLoggedInException {

		checkLogin();

		return loggedInAccount.getBalance();
	}

	public AccountDetails getDetails() throws NotLoggedInException {

		checkLogin();

		Customer customer = null;

		for (Customer c : customers) {

			if (loggedInAccount.getCustomerId() == c.getCustomerId()) {
				customer = c;
				break;
			}
		}

		return new AccountDetails(loggedInAccount, customer);
	}

	public void transferMoney(int receiverAccountNumber, double amount) throws NotLoggedInException,
			InvalidAmountException, AccountNotFoundException, DuplicateAccountException, InsufficientBalanceException {

		checkLogin();

		boolean isFound = false;

		if (amount <= 0) {
			throw new InvalidAmountException("Amount must be greater than zero");
		}

		for (Account account : accounts) {

			if (account.getAccountNumber() == receiverAccountNumber) {

				isFound = true;

				if (receiverAccountNumber == loggedInAccount.getAccountNumber()) {

					throw new DuplicateAccountException("Transfer To Duplicate Account Not Allowed.");

				}
				if (amount > loggedInAccount.getBalance()) {
					throw new InsufficientBalanceException("Insufficient Balance");
				}

				loggedInAccount.setBalance(loggedInAccount.getBalance() - amount);
				account.setBalance(account.getBalance() + amount);

				int transactionID = nextTransactionID++;

				Transaction transaction = new Transaction(transactionID, loggedInAccount.getAccountNumber(),
						receiverAccountNumber, amount, LocalDateTime.now(), TransactionType.TRANSFER);
				transactions.add(transaction);
				break;
			}
		}
		if (!isFound) {
			throw new AccountNotFoundException("No Such Account Found With This Account Number");
		}
	}

	public List<Transaction> getTransactionHistory() throws NotLoggedInException {

		checkLogin();

		List<Transaction> userTransactions = new ArrayList<>();

		for (Transaction transaction : transactions) {

			if (loggedInAccount.getAccountNumber() == transaction.getSenderAccountNumber()
					|| loggedInAccount.getAccountNumber() == transaction.getReceiverAccountNumber()) {

				userTransactions.add(transaction);
			}
		}

		return userTransactions;
	}

	public InterestResult calculateInterest(int years) throws NotLoggedInException, InvalidTimePeriodException {
		checkLogin();

		if (years <= 0) {
			throw new InvalidTimePeriodException("Years Should Be Greter Than 0.");
		}

		double interest = (loggedInAccount.getBalance() * interestRate * years) / 100;
		return new InterestResult(loggedInAccount.getBalance(), interestRate, years, interest);
	}

	public void changePassword(String currentPassword, String newPassword)
			throws NotLoggedInException, InvalidPasswordException {

		checkLogin();

		if (!loggedInAccount.getPassword().equals(currentPassword)) {
			throw new InvalidPasswordException("Wrong Password! Please Enter Valid Password");
		}

		if (newPassword == null || newPassword.trim().isEmpty()) {
			throw new InvalidPasswordException("New password cannot be empty.");
		}

		if (newPassword.length() < 8) {
			throw new InvalidPasswordException("Password must be at least 8 characters.");
		}

		loggedInAccount.setPassword(newPassword);
	}

	public void logoutUser() throws NotLoggedInException {
		checkLogin();
		loggedInAccount = null;

	}

	public void updateCustomerDetails(String name, String phone, String address, String gender)
			throws NotLoggedInException, InvalidAccountDataException {

		checkLogin();

		Customer customer = null;

		for (Customer c : customers) {

			if (c.getCustomerId() == loggedInAccount.getCustomerId()) {
				customer = c;
				break;
			}
		}

		if (customer == null) {
			throw new InvalidAccountDataException("Customer details not found.");
		}

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

		customer.setName(name);
		customer.setAddress(address);
		customer.setPhone(phone);
		gender = gender.toUpperCase();
		customer.setGender(gender);

	}

}
