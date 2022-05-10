package com.brody.ebankingbackend.restcontroller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.brody.ebankingbackend.dto.CustomerDTO;
import com.brody.ebankingbackend.exception.CustomerNotFoundException;
import com.brody.ebankingbackend.service.BankAccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ebank/customer")
@Slf4j
public class CustomerRestController {
	
	private BankAccountService bankAccountService;

	public CustomerRestController(BankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}
	
	@GetMapping("/list")
	@ResponseBody
	public List<CustomerDTO> customers(){
		return bankAccountService.listCustomers();
	}
	
	@GetMapping("/get/{id}")
	@ResponseBody
	public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
		return bankAccountService.getCustomer(customerId);
	}
	
	@PostMapping("/save/{id}")
	@ResponseBody
	public CustomerDTO saveCustomer(@RequestBody CustomerDTO request) {
		return bankAccountService.saveCustomer(request);
	}
	
	
	
	
	

}
