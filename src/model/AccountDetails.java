package model;

public class AccountDetails {

	Account account;
	Customer customer;
	
	public AccountDetails(Account account, Customer customer) {
		this.account = account;
		this.customer = customer;
	}
	
	public Account getAccount() {
		return this.account;
	}
	
	public Customer getCustomer() {
		return this.customer;
	}
}
