package model;

public  class Account {

	private int accountNumber;	
	private String accountType;
	private String password;
	private double balance;
	private int customerId;
	
	public Account(int accountNumber, String accountType, double balance, int customerId, String password) {
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.password = password;
		this.balance = balance;
		this.customerId = customerId;
		
	}
	public Account(String accountType, double balance,int customerId, String password){
		this.accountType=accountType;
		this.balance=balance;
		this.customerId=customerId;
		this.password=password;
	}

    
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber){
		this.accountNumber=accountNumber;
	}


	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getCustomerId() {
		return customerId;
	}
	
}
