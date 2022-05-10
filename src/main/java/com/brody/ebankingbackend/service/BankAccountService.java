package com.brody.ebankingbackend.service;

import java.util.List;

import com.brody.ebankingbackend.entities.BankAccount;
import com.brody.ebankingbackend.entities.CurrentAccount;
import com.brody.ebankingbackend.entities.Customer;
import com.brody.ebankingbackend.entities.SavingAccount;
import com.brody.ebankingbackend.exception.BalanceNotSufficientException;
import com.brody.ebankingbackend.exception.BankAccountNotFoundException;
import com.brody.ebankingbackend.exception.CustomerNotFoundException;

public interface BankAccountService {
	
	Customer saveChstomer(Customer customer);
	
	CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
	
	SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
	
	List<Customer> listCustomers();
	
	BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
	
	void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
	
	void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
	
	void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
}
