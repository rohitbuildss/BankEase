package model;

import java.time.LocalDateTime;

public class Transaction {

	private int transactionId;
	private int senderAccountNumber;
	private int receiverAccountNumber;
	private double amount;
	private LocalDateTime timeStamp;
	private  TransactionType transactionType;

	public Transaction(int transactionId, int senderAccountNumber, int receiverAccountNumber, double amount,
			LocalDateTime timeStamp,  TransactionType transactionType) {
		this.transactionId = transactionId;
		this.senderAccountNumber = senderAccountNumber;
		this.receiverAccountNumber = receiverAccountNumber;
		this.amount = amount;
		this.timeStamp = timeStamp;
		this.transactionType = transactionType;
	}

	public Transaction( int senderAccountNumber, int receiverAccountNumber, double amount,
					   LocalDateTime timeStamp,  TransactionType transactionType) {
		this.senderAccountNumber = senderAccountNumber;
		this.receiverAccountNumber = receiverAccountNumber;
		this.amount = amount;
		this.timeStamp = timeStamp;
		this.transactionType = transactionType;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getSenderAccountNumber() {
		return senderAccountNumber;
	}

	public void setSenderAccountNumber(int senderAccNumber) {
		this.senderAccountNumber = senderAccNumber;
	}

	public int getReceiverAccountNumber() {
		return receiverAccountNumber;
	}

	public void setReceiverAccountNumber(int receiverAccNumber) {
		this.receiverAccountNumber = receiverAccNumber;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

}
