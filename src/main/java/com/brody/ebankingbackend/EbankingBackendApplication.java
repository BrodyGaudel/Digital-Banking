package com.brody.ebankingbackend;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.brody.ebankingbackend.dto.BankAccountDTO;
import com.brody.ebankingbackend.dto.CurrentBankAccountDTO;
import com.brody.ebankingbackend.dto.CustomerDTO;
import com.brody.ebankingbackend.dto.SavingBankAccountDTO;
import com.brody.ebankingbackend.exception.BalanceNotSufficientException;
import com.brody.ebankingbackend.exception.BankAccountNotFoundException;
import com.brody.ebankingbackend.exception.CustomerNotFoundException;
import com.brody.ebankingbackend.service.BankAccountService;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}
	
	@Bean
	CommandLineRunner start(BankAccountService bankAccountService) {
		return args -> {
			Stream.of("Brody", "Gaudel", "Randy").forEach(name -> {
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name+"@ebanking.com");
				bankAccountService.saveCustomer(customer);
			});
			bankAccountService.listCustomers().forEach(customer -> {
				try {
					bankAccountService.saveCurrentBankAccount(Math.random()*9000, 9000, customer.getId());
					bankAccountService.saveSavingBankAccount(Math.random()*120000, 5.5, customer.getId());
					
					
				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				} 
			});
			List<BankAccountDTO> bankAccounts = bankAccountService.listBankAccount();
			for(BankAccountDTO bankAccount: bankAccounts) {
				for(int i=0; i<10; i++) {
					String accountId;
					if(bankAccount instanceof SavingBankAccountDTO) {
						accountId = ((SavingBankAccountDTO) bankAccount).getId();
					}
					else {
						accountId = ((CurrentBankAccountDTO) bankAccount).getId();
					}
					bankAccountService.credit(accountId, 10000+Math.random()*120000, "Credit");
					bankAccountService.debit(accountId, 1000+Math.random()*9000, "Debit");
				}
			}
		};
	}
	
	
	
	
	/*@Bean
	CommandLineRunner start(CustomerRepository customerRepository, 
			BankAccountRepository bankAccountRepository, 
			AccountOperationRepository accountOperationRepository) {
	
		return args -> {
			
			Stream.of("Brody", "Gaudel", "Randy").forEach(name -> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name+"@ebanking.com");
				customerRepository.save(customer);
			});
			
			customerRepository.findAll().forEach(cust -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*90000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(cust);
				currentAccount.setOverDraft(9000);
				bankAccountRepository.save(currentAccount);
			});
			
			customerRepository.findAll().forEach(cust -> {
				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*90000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(cust);
				savingAccount.setInterestRate(5.5);
				bankAccountRepository.save(savingAccount);
			});
			
			bankAccountRepository.findAll().forEach(acc -> {
				for(int i = 0; i<10; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random()*12000);
					accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
					accountOperation.setBankAccount(acc);
					accountOperationRepository.save(accountOperation);
				}
			});
			
		};
	}*/
}
