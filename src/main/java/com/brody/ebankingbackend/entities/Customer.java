package com.brody.ebankingbackend.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	
	@OneToMany(mappedBy = "customer")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private List<BankAccount> bankAccounts;
	
	
	public Customer(Long id, String name, String email, List<BankAccount> bankAccounts) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.bankAccounts = bankAccounts;
	}
	public Customer() {
		super();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<BankAccount> getBankAccounts() {
		return bankAccounts;
	}
	public void setBankAccounts(List<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}
	
	
}
