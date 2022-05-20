package com.brody.ebankingbackend.service;

import java.util.List;

import com.brody.ebankingbackend.dto.CustomerDTO;
import com.brody.ebankingbackend.dto.SavingBankAccountDTO;
import com.brody.ebankingbackend.dto.AccountHistoryDTO;
import com.brody.ebankingbackend.dto.AccountOperationDTO;
import com.brody.ebankingbackend.dto.BankAccountDTO;
import com.brody.ebankingbackend.dto.CurrentBankAccountDTO;
import com.brody.ebankingbackend.exception.BalanceNotSufficientException;
import com.brody.ebankingbackend.exception.BankAccountNotFoundException;
import com.brody.ebankingbackend.exception.CustomerNotFoundException;

public interface BankAccountService {
	
	CustomerDTO saveCustomer(CustomerDTO customerDTO);
	
	CustomerDTO updateCustomer(CustomerDTO customerDTO);
	
	List<CustomerDTO> listCustomers();
	
	void deleteCustomer(Long customerId);
	
	CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
	
	CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
	
	SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
	
	BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
	
	void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
	
	void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
	
	void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
	
	List<BankAccountDTO> listBankAccount();
	
	List<AccountOperationDTO> historique(String accountId);
	
	AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
	
	List<CustomerDTO> searchCustomers(String keyword);
}
