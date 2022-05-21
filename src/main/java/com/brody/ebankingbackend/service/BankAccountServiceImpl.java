package com.brody.ebankingbackend.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brody.ebankingbackend.dto.AccountHistoryDTO;
import com.brody.ebankingbackend.dto.AccountOperationDTO;
import com.brody.ebankingbackend.dto.BankAccountDTO;
import com.brody.ebankingbackend.dto.CurrentBankAccountDTO;
import com.brody.ebankingbackend.dto.CustomerDTO;
import com.brody.ebankingbackend.dto.SavingBankAccountDTO;
import com.brody.ebankingbackend.entities.AccountOperation;
import com.brody.ebankingbackend.entities.BankAccount;
import com.brody.ebankingbackend.entities.CurrentAccount;
import com.brody.ebankingbackend.entities.Customer;
import com.brody.ebankingbackend.entities.SavingAccount;
import com.brody.ebankingbackend.enums.OperationType;
import com.brody.ebankingbackend.exception.BalanceNotSufficientException;
import com.brody.ebankingbackend.exception.BankAccountNotFoundException;
import com.brody.ebankingbackend.exception.CustomerNotFoundException;
import com.brody.ebankingbackend.mappers.BankAccountMapperImpl;
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
	
	private BankAccountMapperImpl dtoMapper;
	
	

	public BankAccountServiceImpl(CustomerRepository customerRepository,
			BankAccountRepository bankAccountRepository,
			AccountOperationRepository accountOperationRepository, 
			BankAccountMapperImpl  dtoMapper) {
		
		this.customerRepository = customerRepository;
		this.bankAccountRepository = bankAccountRepository;
		this.accountOperationRepository = accountOperationRepository;
		this.dtoMapper =  dtoMapper;
	}

	@Override
	public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
		log.info("saving new customer");
		Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
		Customer savedCustomer = customerRepository.save(customer);
		return dtoMapper.fromCustomer(savedCustomer);
		
	}


	@Override
	public List<CustomerDTO> listCustomers() {
		
		List<Customer> customers = customerRepository.findAll();
		return customers.stream()
				.map(customer -> dtoMapper.fromCustomer(customer))
				.collect(Collectors.toList());
	}

	@Override
	public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
		
		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow( () -> new BankAccountNotFoundException("BankAccount not found"));
		
		if(bankAccount instanceof SavingAccount) {
			SavingAccount savingAccount = (SavingAccount) bankAccount;
			return dtoMapper.fromSavingBankAccount(savingAccount);
		}
		else {
			CurrentAccount currentAccount = (CurrentAccount) bankAccount;
			return dtoMapper.fromCurrentBankAccount(currentAccount);
		}
	}

	@Override
	public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
		
		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow( () -> new BankAccountNotFoundException("BankAccount not found"));
		
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
		
		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow( () -> new BankAccountNotFoundException("BankAccount not found"));
		
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
	public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)
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
		CurrentAccount savedBankAccout = bankAccountRepository.save(currentAccount);
		return dtoMapper.fromCurrentBankAccount(savedBankAccout);
		
	}

	@Override
	public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)
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
		SavingAccount savedSavingAccount = bankAccountRepository.save(savingAccount);
		return dtoMapper.fromSavingBankAccount(savedSavingAccount);
	}

	@Override
	public List<BankAccountDTO> listBankAccount() {
		
		List<BankAccount> bankAccounts = bankAccountRepository.findAll();
		
		return bankAccounts.stream().map(bankAccount -> {
			
			if(bankAccount instanceof SavingAccount) {
				SavingAccount savingAccount = (SavingAccount) bankAccount;
				return dtoMapper.fromSavingBankAccount(savingAccount);
			}
			else {
				CurrentAccount currentAccount = (CurrentAccount) bankAccount;
				return dtoMapper.fromCurrentBankAccount(currentAccount);
			}
			
		}).collect(Collectors.toList());
		
	}

	@Override
	public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
		
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer Not Found"));
		
		return dtoMapper.fromCustomer(customer);
	}

	@Override
	public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
		log.info("update customer");
		Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
		Customer savedCustomer = customerRepository.save(customer);
		return dtoMapper.fromCustomer(savedCustomer);
	}

	@Override
	public void deleteCustomer(Long customerId) {
		customerRepository.deleteById(customerId);
		
	}

	@Override
	public List<AccountOperationDTO> historique(String accountId) {
		List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
		return accountOperations.stream()
				.map(op -> dtoMapper.fromAccountOperation(op))
				.collect(Collectors.toList());
	}

	@Override
	public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
		
		BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
		if(bankAccount==null) {
			throw new BankAccountNotFoundException("Bank Account Not Found");
		}
		Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
		AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
		List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
		accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
		accountHistoryDTO.setAccountId(bankAccount.getId());
		accountHistoryDTO.setBalance(bankAccount.getBalance());
		accountHistoryDTO.setPageSize(size);
		accountHistoryDTO.setCurrentPage(page);
		accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
		return accountHistoryDTO;
	}

	@Override
	public List<CustomerDTO> searchCustomers(String keyword) {
		List<Customer> customers = customerRepository.findByNameContains(keyword);
		return customers.stream()
				.map(customer -> dtoMapper.fromCustomer(customer))
				.collect(Collectors.toList());
	}
	

}
