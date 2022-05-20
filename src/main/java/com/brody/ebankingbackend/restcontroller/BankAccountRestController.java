package com.brody.ebankingbackend.restcontroller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brody.ebankingbackend.dto.AccountHistoryDTO;
import com.brody.ebankingbackend.dto.AccountOperationDTO;
import com.brody.ebankingbackend.dto.BankAccountDTO;
import com.brody.ebankingbackend.exception.BankAccountNotFoundException;
import com.brody.ebankingbackend.service.BankAccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ebank/account")
@Slf4j
@CrossOrigin(origins = "*")
public class BankAccountRestController {
	
	private BankAccountService bankAccountService;

	public BankAccountRestController(BankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}
	
	@GetMapping("/get/{accountId}")
	public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
		return bankAccountService.getBankAccount(accountId);
	}
	
	@GetMapping("/list")
	public List<BankAccountDTO> listAccount()  {
		return bankAccountService.listBankAccount();
	}
	
	
	@GetMapping("/operations/{accountId}")
	public List<AccountOperationDTO> getHistorique(@PathVariable String accountId){
		return bankAccountService.historique(accountId);
	}
	
	@GetMapping("/pageOperations/{accountId}")
	public AccountHistoryDTO getAccountHistorique(
			@PathVariable String accountId,
			@RequestParam(name ="page", defaultValue = "0") int page,
			@RequestParam(name ="size", defaultValue = "5") int size) throws BankAccountNotFoundException{
		
		return bankAccountService.getAccountHistory(accountId, page, size);
	}
	
	
	

}
