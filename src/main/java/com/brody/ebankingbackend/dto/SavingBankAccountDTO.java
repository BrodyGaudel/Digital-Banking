package com.brody.ebankingbackend.dto;

import java.util.Date;

import com.brody.ebankingbackend.enums.AccountStatus;

public class SavingBankAccountDTO extends BankAccountDTO{
	
	private String id;
	private double balance;
	private Date createdAt;
	private AccountStatus status;
	private CustomerDTO customerDTO;
	private double interestRate;
	
	
	public SavingBankAccountDTO() {
		super();
	}
	public SavingBankAccountDTO(String id, double balance, Date createdAt, AccountStatus status,
			CustomerDTO customerDTO, double interestRate) {
	
		this.id = id;
		this.balance = balance;
		this.createdAt = createdAt;
		this.status = status;
		this.customerDTO = customerDTO;
		this.interestRate = interestRate;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public AccountStatus getStatus() {
		return status;
	}
	public void setStatus(AccountStatus status) {
		this.status = status;
	}
	public CustomerDTO getCustomerDTO() {
		return customerDTO;
	}
	public void setCustomerDTO(CustomerDTO customerDTO) {
		this.customerDTO = customerDTO;
	}
	public double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	
	

}
