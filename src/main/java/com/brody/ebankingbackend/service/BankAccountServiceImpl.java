package com.brody.ebankingbackend.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brody.ebankingbackend.entities.AccountOperation;
import com.brody.ebankingbackend.entities.BankAccount;
import com.brody.ebankingbackend.entities.CurrentAccount;
import com.brody.ebankingbackend.entities.Customer;
import com.brody.ebankingbackend.entities.SavingAccount;
import com.brody.ebankingbackend.enums.OperationType;
import com.brody.ebankingbackend.exception.BalanceNotSufficientException;
import com.brody.ebankingbackend.exception.BankAccountNotFoundException;
import com.brody.ebankingbackend.exception.CustomerNotFoundException;
import com.brody.ebankingbackend.repository.AccountOperationRepository;
import com.brody.ebankingbackend.repository.BankAccountRepository;
import com.brody.ebankingbackend.repository.CustomerRepository;

@Transactional
@Service
public class BankAccountServiceImpl implements BankAccountService {
	
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	private CustomerRepository customerRepository;
	
	private BankAccountRepository bankAccountRepository;
	
	private AccountOperationRepository accountOperationRepository;
	
	

	public BankAccountServiceImpl(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository,
			AccountOperationRepository accountOperationRepository) {
		
		this.customerRepository = customerRepository;
		this.bankAccountRepository = bankAccountRepository;
		this.accountOperationRepository = accountOperationRepository;
	}

	@Override
	public Customer saveChstomer(Customer customer) {
		log.info("saving new customer");
		return customerRepository.save(customer);
	}


	@Override
	public List<Customer> listCustomers() {
		
		return customerRepository.findAll();
	}

	@Override
	public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
		
		return bankAccountRepository.findById(accountId)
				.orElseThrow( () -> new BankAccountNotFoundException("BankAccount not found"));
	}

	@Override
	public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
		
		BankAccount bankAccount = getBankAccount(accountId);
		if(bankAccount.getBalance()<amount) {
			throw new BalanceNotSufficientException("Balance not sufficient");
		}
		
		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.DEBIT);
		accountOperation.setAmount(amount);
		accountOperation.setOperationDate(new Date());
		accountOperation.setDescription(description);
		accountOperation.setBankAccount(bankAccount);
		accountOperationRepository.save(accountOperation);
		
		bankAccount.setBalance(bankAccount.getBalance()-amount);
		bankAccountRepository.save(bankAccount);
	}

	@Override
	public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
		BankAccount bankAccount = getBankAccount(accountId);
		
		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.CREDIT);
		accountOperation.setAmount(amount);
		accountOperation.setOperationDate(new Date());
		accountOperation.setDescription(description);
		accountOperation.setBankAccount(bankAccount);
		accountOperationRepository.save(accountOperation);
		
		bankAccount.setBalance(bankAccount.getBalance()+amount);
		bankAccountRepository.save(bankAccount);
		
	}

	@Override
	public void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
		
		debit(accountIdSource, amount, "Transfer to "+accountIdDestination);
		credit(accountIdDestination, amount, "Transfer from "+accountIdSource);
	}

	@Override
	public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)
			throws CustomerNotFoundException {
		Customer customer = customerRepository.findById(customerId).orElse(null);
		
		if(customer==null) {
			throw new CustomerNotFoundException("Customer not found");
		}
		CurrentAccount currentAccount = new CurrentAccount();
		currentAccount.setId(UUID.randomUUID().toString());
		currentAccount.setCreatedAt(new Date());
		currentAccount.setBalance(initialBalance);
		currentAccount.setCustomer(customer);
		currentAccount.setOverDraft(overDraft);
		return bankAccountRepository.save(currentAccount);
		
	}

	@Override
	public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)
			throws CustomerNotFoundException {
		Customer customer = customerRepository.findById(customerId).orElse(null);
		
		if(customer==null) {
			throw new CustomerNotFoundException("Customer not found");
		}
		SavingAccount savingAccount = new SavingAccount();
		savingAccount.setId(UUID.randomUUID().toString());
		savingAccount.setCreatedAt(new Date());
		savingAccount.setBalance(initialBalance);
		savingAccount.setCustomer(customer);
		savingAccount.setInterestRate(interestRate);
		return bankAccountRepository.save(savingAccount);
	}

}
