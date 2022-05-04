package com.brody.ebankingbackend.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.brody.ebankingbackend.enums.AccountStatus;

@Entity
@DiscriminatorValue("CA")
public class CurrentAccount extends BankAccount {
	
	private double overDraft; //decouvert
	
	

	public CurrentAccount(String id, double balance, Date createdAt, AccountStatus status, Customer customer,
			List<AccountOperation> accountOperations, double overDraft) {
		super(id, balance, createdAt, status, customer, accountOperations);
		this.overDraft = overDraft;
	}
	public CurrentAccount() {
		super();
	}
	public CurrentAccount(String id, double balance, Date createdAt, AccountStatus status, Customer customer,
			List<AccountOperation> accountOperations) {
		super(id, balance, createdAt, status, customer, accountOperations);
	}


	public double getOverDraft() {
		return overDraft;
	}

	public void setOverDraft(double overDraft) {
		this.overDraft = overDraft;
	}
	
	
	
	
}
