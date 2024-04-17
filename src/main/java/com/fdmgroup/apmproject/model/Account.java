package com.fdmgroup.apmproject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "listOfAccounts")
public class Account {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Account ID")
	private long accountId;
	
	@Column(name = "Account Name")
	private String accountName;
	
	@Column(name = "Account Balance")
	private double balance;
	
	@Column(name = "Account Number")
	private String accountNumber;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK User ID")
	private User accountUser;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Status ID")
	private Status accountStatus;

	@OneToMany(mappedBy = "transactionAccount", fetch = FetchType.EAGER)
	private List<Transaction> transactions = new ArrayList<>();
	
	public Account() {};

	public Account(String accountName, double balance, String accountNumber, User accountUser, Status accountStatus) {
	        setAccountName(accountName);
	        setBalance(balance);
	        setAccountNumber(accountNumber);
	        setAccountUser(accountUser);
	        setAccountStatus(accountStatus);
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public User getAccountUser() {
		return accountUser;
	}

	public void setAccountUser(User accountUser) {
		this.accountUser = accountUser;
	}

	public Status getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Status accountStatus) {
		this.accountStatus = accountStatus;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Transaction transaction) {
		this.transactions.add(transaction);
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", accountName=" + accountName + ", balance=" + balance
				+ ", accountNumber=" + accountNumber + ", accountStatus=" + accountStatus + ", transactions="
				+ transactions + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountId, accountName, accountNumber, accountStatus, balance, transactions);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return accountId == other.accountId && Objects.equals(accountName, other.accountName)
				&& Objects.equals(accountNumber, other.accountNumber)
				&& Objects.equals(accountStatus, other.accountStatus)
				&& Double.doubleToLongBits(balance) == Double.doubleToLongBits(other.balance)
				&& Objects.equals(transactions, other.transactions);
	}
	
	
	
	
}
