package model;

public class InterestResult {
	
	private double balance;
	private double interestRate;
	private int years;
	private double interest;
	
	public InterestResult(double balance, double interestRate, int years, double interest) {
		this.balance = balance;
		this.interestRate = interestRate;
		this.years = years;
		this.interest = interest;
	}

	public double getBalance() {
		return balance;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public int getYears() {
		return years;
	}

	public double getInterest() {
		return interest;
	}
	
	
}
