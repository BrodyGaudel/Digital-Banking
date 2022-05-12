package com.brody.ebankingbackend.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.brody.ebankingbackend.dto.AccountOperationDTO;
import com.brody.ebankingbackend.dto.CurrentBankAccountDTO;
import com.brody.ebankingbackend.dto.CustomerDTO;
import com.brody.ebankingbackend.dto.SavingBankAccountDTO;
import com.brody.ebankingbackend.entities.AccountOperation;
import com.brody.ebankingbackend.entities.CurrentAccount;
import com.brody.ebankingbackend.entities.Customer;
import com.brody.ebankingbackend.entities.SavingAccount;

@Service
public class BankAccountMapperImpl {
	
	public CustomerDTO fromCustomer(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		BeanUtils.copyProperties(customer, customerDTO);
		return customerDTO;
	}
	
	public Customer fromCustomerDTO(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerDTO, customer);
		return customer;
	}
	
	public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount) {
		SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
		BeanUtils.copyProperties(savingAccount, savingBankAccountDTO);
		savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
		savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
		return savingBankAccountDTO;
		
	}
	
	public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO) {
		SavingAccount savingAccount = new SavingAccount();
		BeanUtils.copyProperties(savingBankAccountDTO, savingAccount);
		savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
		return null;
		
	}

	public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount) {
		CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO();
		BeanUtils.copyProperties(currentAccount, currentBankAccountDTO);
		currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
		currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());
		return currentBankAccountDTO;
		
	}
	
	public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO) {
		CurrentAccount currentAccount = new CurrentAccount();
		BeanUtils.copyProperties(currentBankAccountDTO, currentAccount);
		currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
		return currentAccount;
		
	}
	
	public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
		AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
		BeanUtils.copyProperties(accountOperation, accountOperationDTO);
		return accountOperationDTO;
	}
	
	
}
