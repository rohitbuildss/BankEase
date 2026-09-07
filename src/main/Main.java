package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import database.DBConnection;
import exception.AccountNotFoundException;
import exception.DuplicateAccountException;
import exception.InsufficientBalanceException;
import exception.InvalidAccountDataException;
import exception.InvalidAmountException;
import exception.InvalidPasswordException;
import exception.InvalidTimePeriodException;
import exception.NotLoggedInException;
import model.Account;
import model.AccountDetails;
import model.Customer;
import model.InterestResult;
import model.Transaction;
import model.TransactionType;
import service.BankService;

public class Main {

	private static final Scanner scanner = new Scanner(System.in);
	private static final BankService bankService = new BankService();

	private static void showMenu() {
		System.out.println("\n================================");
		System.out.println("        BANKING PORTAL");
		System.out.println("================================");
		System.out.println("1. Create Account");
		System.out.println("2. Login");
		System.out.println("3. Deposit Money");
		System.out.println("4. Withdraw Money");
		System.out.println("5. Check Balance");
		System.out.println("6. View Account Details");
		System.out.println("7. Transfer Money");
		System.out.println("8. View Transaction History");
		System.out.println("9. Calculate Interest");
		System.out.println("10. Change Password");
		System.out.println("11. Update Customer Details");
		System.out.println("12. Logout");
		System.out.println("13. Exit");
		System.out.print("Enter your choice: ");
	}

	private static int readInt() {
		while (true) {
			try {
				return scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a number.");
				scanner.nextLine();
			}
		}
	}

	private static double readDouble() {
		while (true) {
			try {
				return scanner.nextDouble();
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a valid number.");
				scanner.nextLine();
			}
		}
	}

	private static Account createAccount() {

		try {
			System.out.print("Enter Your Name: ");
			String name = scanner.nextLine();

			System.out.print("Enter Your Age: ");
			int age = readInt();
			scanner.nextLine();

			System.out.print("Enter Your Phone Number: ");
			String phone = scanner.nextLine();

			System.out.print("Enter Your Address: ");
			String address = scanner.nextLine();

			System.out.print("Enter Your Gender(Male/Female): ");
			String gender = scanner.nextLine();

			System.out.print("Enter Account Type(SAVINGS/CURRENT): ");
			String accountType = scanner.nextLine();

			System.out.print("Enter Unique Password: ");
			String password = scanner.nextLine();

			System.out.print("Enter Initial Deposit: ");
			double balance = readDouble();
			scanner.nextLine();

			Account account = bankService.createAccount(accountType, balance, name, age, phone, address, gender,
					password);

			System.out.println("\nAccount Created Successfully\n");
			return account;

		} catch (InvalidAccountDataException | InvalidPasswordException | InvalidAmountException | SQLException e) {
			System.out.println("\n" + e.getMessage());
		}

		return null;
	}

	private static void login() {

		System.out.print("Enter Account Number: ");
		int accountNumber = readInt();
		scanner.nextLine();

		System.out.print("Enter Password: ");
		String password = scanner.nextLine();

		try {
			bankService.login(accountNumber, password);
			System.out.println("\nLogin Successful!\n");
		} catch (AccountNotFoundException | InvalidPasswordException | SQLException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		}
	}

	private static void depositMoney() {

		System.out.print("Enter Amount: ");
		double amount = readDouble();
		scanner.nextLine();

		try {
			bankService.depositMoney(amount);
			System.out.println("\nAmount Deposited Successfully\n");
		} catch (InvalidAmountException | NotLoggedInException | SQLException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		}
	}

	private static void withdrawMoney() {

		System.out.print("Enter Amount: ");
		double amount = readDouble();
		scanner.nextLine();

		try {
			bankService.withdrawMoney(amount);
			System.out.println("\nMoney Withdrawn Successfully\n");
		} catch (InvalidAmountException | InsufficientBalanceException | NotLoggedInException | SQLException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		}
	}

	private static void checkBalance() {

		try {
			double balance = bankService.checkBalance();
			System.out.println("\nAccount Balance Is: " + balance + "\n");

		} catch (NotLoggedInException | SQLException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		}
	}

	private static void viewAccountDetails() {

		try {
			AccountDetails details = bankService.getDetails();

			System.out.println("\n===== ACCOUNT DETAILS =====");
			System.out.println("Account Number : " + details.getAccount().getAccountNumber());
			System.out.println("Account Type   : " + details.getAccount().getAccountType());
			System.out.println("Balance        : ₹" + details.getAccount().getBalance());
			System.out.println("Customer ID    : " + details.getAccount().getCustomerId());
			System.out.println("Name           : " + details.getCustomer().getName());
			System.out.println("Age            : " + details.getCustomer().getAge());
			System.out.println("Phone          : " + details.getCustomer().getPhone());
			System.out.println("Address        : " + details.getCustomer().getAddress());
			System.out.println("Gender         : " + details.getCustomer().getGender());
			System.out.println("============================\n");

		} catch (NotLoggedInException | SQLException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		}
	}

	private static void transferMoney() {

		System.out.print("Enter Receiver Account Number: ");
		int receiverAccountNumber = readInt();

		System.out.print("Enter Amount: ");
		double amount = readDouble();
		scanner.nextLine();

		try {
			bankService.transferMoney(receiverAccountNumber, amount);
			System.out.println("\nMoney Transfered SucessFully\n");
		} catch (InvalidAmountException | InsufficientBalanceException | NotLoggedInException |
                 DuplicateAccountException | AccountNotFoundException | SQLException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		}
	}

	private static void viewTransactionHistory() {

		try {
			List<Transaction> history = bankService.getTransactionHistory();

			if (history.isEmpty()) {
				System.out.println("\nNo transactions found for this account.\n");
				return;
			}

			for (Transaction transaction : history) {

				System.out.println("\n===== TRANSACTION DETAILS =====");
				System.out.println("Transaction ID : " + transaction.getTransactionId());
				System.out.println("Type           : " + transaction.getTransactionType());

				if (transaction.getTransactionType() == TransactionType.TRANSFER) {
					System.out.println("Sender         : " + transaction.getSenderAccountNumber());
					System.out.println("Receiver       : " + transaction.getReceiverAccountNumber());
				}

				System.out.println("Amount         : " + transaction.getAmount());
				System.out.println("Time           : " + transaction.getTimeStamp());
				System.out.println("==============================");
			}

			System.out.println();

		} catch (NotLoggedInException | SQLException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		}
	}

	private static void getInterest() {

		System.out.print("Enter Years: ");
		int years = readInt();
		scanner.nextLine();

		try {
			InterestResult result = bankService.calculateInterest(years);

			System.out.println("\n===== INTEREST DETAILS =====");
			System.out.println("Current Balance : ₹" + result.getBalance());
			System.out.println("Interest Rate   : " + result.getInterestRate() + "%");
			System.out.println("Time            : " + result.getYears() + " years");
			System.out.println("Interest        : ₹" + result.getInterest());
			System.out.println("============================\n");

		} catch (NotLoggedInException | InvalidTimePeriodException | SQLException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		}
	}

	private static void updatePassword() {

		System.out.print("Enter Current Password: ");
		String currentPassword = scanner.nextLine();

		System.out.print("Enter New Password: ");
		String newPassword = scanner.nextLine();

		try {
			bankService.changePassword(currentPassword, newPassword);
			System.out.println("\nPassword Updated Successfully\n");

		} catch (NotLoggedInException | InvalidPasswordException | SQLException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		}
	}

	private static void logout() {

		try {
			bankService.logoutUser();
			System.out.println("\nLogged out successfully\n");

		} catch (NotLoggedInException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		}
	}

	private static void updateCustomerDetails() {

		try {
			System.out.print("Enter Your Name: ");
			String name = scanner.nextLine();

			System.out.print("Enter Your Phone Number: ");
			String phone = scanner.nextLine();

			System.out.print("Enter Your Address: ");
			String address = scanner.nextLine();

			System.out.print("Enter Your Gender(Male/Female): ");
			String gender = scanner.nextLine();

			bankService.updateCustomerDetails(name, phone, address, gender);

			System.out.println("\nDetails Updated SuccessFully.\n");

		} catch (NotLoggedInException | InvalidAccountDataException | SQLException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		}
	}

	public static void main(String[] args) {

		try(Connection con = DBConnection.getConnection();){
			System.out.println("Connection Estabilished SuceessFully");
		} catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while (true) {

			showMenu();

			int choice = readInt();
			scanner.nextLine();

			switch (choice) {

			case 1:

				Account account = createAccount();

				if (account != null) {
					System.out.println("===== ACCOUNT CREATED =====");
					System.out.println("Customer ID    : " + account.getCustomerId());
					System.out.println("Account Number : " + account.getAccountNumber());
					System.out.println("Account Type   : " + account.getAccountType());
					System.out.println("Balance        : ₹" + account.getBalance());
					System.out.println("============================\n");
				}

				break;

			case 2:
				login();
				break;

			case 3:
				depositMoney();
				break;

			case 4:
				withdrawMoney();
				break;

			case 5:
				checkBalance();
				break;

			case 6:
				viewAccountDetails();
				break;

			case 7:
				transferMoney();
				break;

			case 8:
				viewTransactionHistory();
				break;

			case 9:
				getInterest();
				break;

			case 10:
				updatePassword();
				break;

			case 11:
				updateCustomerDetails();
				break;

			case 12:
				logout();
				break;

			case 13:
				System.out.println("Thank you for using our banking application.");
				return;

			default:
				System.out.println("\nInvalid choice. Please try again.\n");
			}
		}
	}
}